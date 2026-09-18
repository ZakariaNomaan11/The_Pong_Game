import java.awt.*;
import java.util.*;

public final class Ball extends Rectangle {

    Random random;
    int xVelocity;
    int yVelocity;
    int initialSpeed = 3;

    Ball(int x, int y, int width, int height, int unused) {
        super(x, y, width, height);
        random = new Random();

        int rx = random.nextBoolean() ? 1 : -1;
        int ry = random.nextBoolean() ? 1 : -1;

        setXDirection(rx * initialSpeed);
        setYDirection(ry * initialSpeed);
    }

    public final void setXDirection(int vx) {
        xVelocity = vx;
    }

    public void setYDirection(int vy) {
        yVelocity = vy;
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillOval(x, y, width, height);
    }
}
