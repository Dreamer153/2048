import java.awt.*;
import javax.swing.*;

//游戏运行类
public class Game extends JFrame {
    JLabel statusBar;
    private static final String TITLE = "2048";
    public static final String WIN_MSG = "You win, continue?";
    public static final String LOSE_MSG = "You lose,press R to reset the game";

    public static void main(String[] args) {

        Game game = new Game();
        Grid board = new Grid(game);
        if (args.length != 0 && args[0].matches("[0-9]*")) {
            Grid.goal = NumbersAndColors.of(Integer.parseInt(args[0]));
        }
        KeyboardListener kb = KeyboardListener.getKeyboardListener(board);
        board.addKeyListener(kb);
        game.add(board);

        game.setLocationRelativeTo(null);
        game.setVisible(true);
    }

    // constructor
    public Game() {
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(340, 400);
        setResizable(false);

        statusBar = new JLabel("");
        add(statusBar, BorderLayout.SOUTH);
    }

    void win() {
        statusBar.setText(WIN_MSG);
    }
    void lose() {
        statusBar.setText(LOSE_MSG);
    }
}
