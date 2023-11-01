import java.util.*;

public class Tile {
    private final NumbersAndColors nac;
    private final static HashMap<Integer, Tile> cache = new HashMap<>();
    public final static Tile ZERO = new Tile(NumbersAndColors._0);
    public final static Tile TWO = new Tile(NumbersAndColors._2);
    public final static Tile FOUR = new Tile(NumbersAndColors._4);

    // 将常用的值0、2和4被放入缓存
    static {
        for (NumbersAndColors n : NumbersAndColors.values()) {
            switch (n) {
                case _0:
                    cache.put(n.value(), ZERO);
                    break;
                case _2:
                    cache.put(n.value(), TWO);
                    break;
                case _4:
                    cache.put(n.value(), FOUR);
                    break;
                default:
                    cache.put(n.value(), new Tile(n));
                    break;
            }
        }
    }

    // 构造函数
    public Tile(NumbersAndColors n) {
        nac = n;
    }

    NumbersAndColors value() {
        return nac;
    }

    public static Tile valueOf(int num) {
        return cache.get(num);
    }

    public Tile getDouble() {
        return valueOf(nac.value() << 1);
    }

    boolean empty() {
        return nac == NumbersAndColors._0;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Tile))
            return false;
        Tile other = (Tile) obj;
        if (nac != other.nac)
            return false;
        return true;
    }

    // 随机生成一个4或2的方块
    static Tile randomTile() {
        return Math.random() < 0.15 ? FOUR : TWO;
    }

    public String toString() {
        return String.format("%1$4d", nac.value());

    }
}