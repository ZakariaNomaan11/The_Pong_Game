import java.awt.*;
import java.awt.event.*;

public class Paddles extends Rectangle {

    private final int id;
    private int yVelocity;
    private final int speed = 10;
    private boolean upPressed = false;
    private boolean downPressed = false;

    Paddles(int x, int y, int paddleWidth, int paddleHeight, int id) {
        super(x, y, paddleWidth, paddleHeight);
        this.id = id;
    }

    public void keyPressed(KeyEvent e) {
        switch (id) {
            case 1:
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    upPressed = true;
                    setYDirection(-speed);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    downPressed = true;
                    setYDirection(speed);
                }
                break;
            case 2:
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    upPressed = true;
                    setYDirection(-speed);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    downPressed = true;
                    setYDirection(speed);
                }
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (id) {
            case 1:
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    upPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    downPressed = false;
                }
                break;
            case 2:
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    upPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    downPressed = false;
                }
                break;
        }

        // Update yVelocity based on current key state
        if (upPressed && !downPressed) {
            setYDirection(-speed);
        } else if (downPressed && !upPressed) {
            setYDirection(speed);
        } else {
            setYDirection(0);
        }
    }

    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }

    public void move() {
        y += yVelocity;

        // Optional: clamp inside the paddle class so it never leaves the screen.
        if (y < 0) y = 0;
        if (y > GamePanel.GAME_HEIGHT - height) y = GamePanel.GAME_HEIGHT - height;
    }

    public void draw(Graphics g) {
        g.setColor(id == 1 ? Color.blue : Color.red);
        g.fillRect(x, y, width, height);
    }
}
