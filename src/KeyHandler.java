import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler  implements KeyListener {

    private char direction;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> direction = 'U';
            case KeyEvent.VK_DOWN -> direction = 'D';
            case KeyEvent.VK_LEFT -> direction = 'L';
            case KeyEvent.VK_RIGHT -> direction = 'R';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        direction = ' ';
    }

    public char getDirection() {
        return direction;
    }
}
