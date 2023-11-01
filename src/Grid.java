import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Grid extends JPanel {
    public static final int ROW = 4;
    final Game host;
    private Tile[] tiles;
    // 将目标值设置为2048，定义在NumbersAndColors类中
    public static NumbersAndColors goal = NumbersAndColors._2048;

    private static final Color BG_COLOR = new Color(0xbbada0);
    // 设置数字的字体，可以在此处更改字体的大小和样式
    private static final Font STR_FONT = new Font(Font.SERIF, Font.BOLD, 16);
    private static final int SIDE = 64;
    private static final int MARGIN = 16;

    // 构造函数
    public Grid(Game main) {
        host = main;
        setFocusable(true);
        initializeTiles();
    }

    // addNewTile找到一个空的随机位置，并在其上初始化一个新的随机方块
    private void addNewTile() {
        // 存储所有空方块的列表
        List<Integer> list = findEmptyIndex();
        // 从列表中随机选择一个位置
        int index = list.get((int) (Math.random() * list.size()));
        tiles[index] = Tile.randomTile();
    }

    // initializeTiles在程序首次运行时设置游戏板，将所有方块的值初始化为0，并添加两个新方块,也可用于重置游戏板
    public void initializeTiles() {
        tiles = new Tile[ROW * ROW];
        // 将所有方块的值初始化为0
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.ZERO;
        }
        // 添加两个初始方块
        addNewTile();
        addNewTile();
        host.statusBar.setText("");
    }

    // findEmptyIndex返回一个列表，其中包含所有空方块的位置
    private List<Integer> findEmptyIndex() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].empty())
                list.add(i);
        }
        return list;
    }

    // 检查网格是否已满（即没有空方块）
    private boolean isGridFull() {
        return findEmptyIndex().size() == 0;
    }

    // 获取位于(x, y)位置的方块，其中x为列，y为行
    private Tile tileAt(int x, int y) {
        return tiles[x + y * ROW];
    }

    // 检查是否有方块阻挡了某个方向的移动
    boolean checkIfCanMove() {
        if (!isGridFull()) {
            return true;
        }
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                Tile t = tileAt(x, y);
                if ((x < ROW - 1 && t.equals(tileAt(x +1, y)))) {
                    return true;
                }
                if ((y < ROW - 1 && t.equals(tileAt(x, y + 1)))) {
                    return true;
                }
            }
        }
        return false;
    }

    // rotate函数返回一个被旋转了指定角度的Tile数组,用于实际旋转游戏板
    private Tile[] rotate(int degree) {
        Tile[] newTiles = new Tile[ROW * ROW];
        int offsetX = 3, offsetY = 3;
        if (degree == 90) {
            offsetY = 0;
        } else if (degree == 180) {
        } else if (degree == 270) {
            offsetX = 0;
        }
        double radians = Math.toRadians(degree);
        int cos = (int) Math.cos(radians);
        int sin = (int) Math.sin(radians);
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * ROW] = tileAt(x, y);
            }
        }
        return newTiles;
    }

    // getLine返回选定行的数组
    private Tile[] getLine(int index) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++){
            result[i] = tileAt(i, index);
        }
        return result;
    }

    // 确保大小正确，如果不正确则添加0，因为当你移动方块时，会留下空位
    private static void ensureSize(List<Tile> l, int s) {
        while (l.size() < s) {
            l.add(Tile.ZERO);
        }
    }

    // 移动行，返回新行
    private Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<>();
        for (int i = 0; i < 4; i++){
            if (!oldLine[i].empty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++){
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    // 合并行
    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < ROW; i++) {
            if (i < ROW - 1 && !oldLine[i].empty()
                    && oldLine[i].equals(oldLine[i + 1])) {
                Tile merged = oldLine[i].getDouble();
                i++;
                list.add(merged);
                if (merged.value() == goal) {
                    host.win();
                }
            } else {
                list.add(oldLine[i]);
            }
        }
        ensureSize(list, 4);
        return list.toArray(new Tile[4]);
    }

    // 设置行
    private void setLine(int index, Tile[] re) {
        for (int i = 0; i < 4; i++){
            tiles[i + index * ROW] = re[i];
        }
    }

    // 使用getLine、moveLine、mergeLine、setLine依次向左移动并合并所有行
    public void left(){
        boolean needAddTile = false;
        for (int i = 0; i < 4; i++){
            Tile[] origin = getLine(i);
            Tile[] afterMove = moveLine(origin);
            Tile[] merged = mergeLine(afterMove);
            setLine(i, merged);
            if (!needAddTile && !Arrays.equals(origin, merged)) {
                needAddTile = true;
            }
        }
        if (needAddTile) {
            addNewTile();
        }
    }

    // 向右移动
    public void right() {
        // 旋转过程
        tiles = rotate(180);
        left();
        tiles = rotate(180);
    }
    // 向上移动，与之前的逻辑相同
    public void up() {
        tiles = rotate(270);
        left();
        tiles = rotate(90);
    }
    // 向下移动，与之前的逻辑相同
    public void down() {
        tiles = rotate(90);
        left();
        tiles = rotate(270);
    }

    // 用于绘制的函数
    private static int offsetCoors(int arg) {
        return arg * (MARGIN + SIDE) + MARGIN;
    }

    // 绘制方块
    private void drawTile(Graphics g, Tile tile, int x, int y) {
        NumbersAndColors val = tile.value();
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(val.color());
        g.fillRect(xOffset, yOffset, SIDE, SIDE);
        g.setColor(val.fontColor());

        if (val.value() != 0)
            g.drawString(tile.toString(), xOffset
                    + (SIDE >> 1) - MARGIN, yOffset + (SIDE >> 1));
    }

    // 绘制网格，在每次按键事件以更新游戏板时调用
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.setFont(STR_FONT);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        for (int x = 0; x < 4; x++){
            for (int y = 0; y < 4; y++){
                drawTile(g, tiles[x + y * ROW], x, y);
            }
        }
    }
}
