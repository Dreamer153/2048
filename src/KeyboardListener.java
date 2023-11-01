import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KeyboardListener extends KeyAdapter {

    private static final int[] KEYS = { KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_R };
    private static final String[] METHOD_NAMES = { "up", "down", "left", "right", "initializeTiles" };

    private Grid board;
    private static final KeyboardListener INSTANCE = new KeyboardListener();

    private KeyboardListener() {
    }

    public static KeyboardListener getKeyboardListener(Grid g) {
        INSTANCE.board = g;
        return INSTANCE;
    }

    public void keyPressed(KeyEvent k) {
        super.keyPressed(k);
        int keyCode = k.getKeyCode();
        for (int i = 0; i < KEYS.length; i++) {
            if (keyCode == KEYS[i]) {
                String methodName = METHOD_NAMES[i];
                try {
                    Method action = Grid.class.getMethod(methodName);
                    action.invoke(board);
                    board.repaint();
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                         | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!board.checkIfCanMove()) {
            board.host.lose();
        }
    }
}