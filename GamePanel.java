import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;


public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * 0.5555);
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    private static final int MAX_BALL_SPEED = 12;

    private Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Random random;
    private Paddles paddle1;
    private Paddles paddle2;
    private Ball ball;
    private Score score;

    public GamePanel() {
        setPreferredSize(SCREEN_SIZE);
        setBackground(Color.WHITE);
        setFocusable(true);

        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);

        addKeyListener(new AL());

        // Request focus after the panel is realized
        SwingUtilities.invokeLater(() -> {
            requestFocusInWindow();
        });

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        int x = (GAME_WIDTH / 2) - (BALL_DIAMETER / 2);
        int y = random.nextInt(GAME_HEIGHT - BALL_DIAMETER);
        // Ball constructor: Ball(int x, int y, int width, int height, int unused)
        ball = new Ball(x, y, BALL_DIAMETER, BALL_DIAMETER, 0);
    }

    public void newPaddles() {
        paddle1 = new Paddles(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddles(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // simple double buffering
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        // draw background cleared by super.paintComponent already
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move() {
        ball.move();
        paddle1.move();
        paddle2.move();
    }

    public void checkCollision() {

        // Bounce ball off top and bottom
        if (ball.y <= 0) {
            ball.setYDirection(Math.abs(ball.yVelocity));
            ball.y = 0;
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-Math.abs(ball.yVelocity));
            ball.y = GAME_HEIGHT - BALL_DIAMETER;
        }

        // Paddle 1 collision
        if (ball.intersects(paddle1)) {
            // ensure positive x velocity and increase speed slightly
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity = Math.min(ball.xVelocity + 1, MAX_BALL_SPEED);

            if (ball.yVelocity > 0) ball.yVelocity = Math.min(ball.yVelocity + 1, MAX_BALL_SPEED);
            else ball.yVelocity = Math.max(ball.yVelocity - 1, -MAX_BALL_SPEED);

            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);

            // Nudge ball outside the paddle to avoid repeated collisions
            ball.x = paddle1.x + paddle1.width;
        }

        // Paddle 2 collision
        if (ball.intersects(paddle2)) {
            // ensure negative x velocity and increase speed slightly
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity = Math.min(ball.xVelocity + 1, MAX_BALL_SPEED);

            if (ball.yVelocity > 0) ball.yVelocity = Math.min(ball.yVelocity + 1, MAX_BALL_SPEED);
            else ball.yVelocity = Math.max(ball.yVelocity - 1, -MAX_BALL_SPEED);

            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);

            // Nudge ball outside the paddle to avoid repeated collisions
            ball.x = paddle2.x - ball.width;
        }

        // Clamp paddles to screen (defensive; Paddles.move may also clamp)
        if (paddle1.y <= 0) paddle1.y = 0;
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;

        if (paddle2.y <= 0) paddle2.y = 0;
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        // Score and reset when ball goes past left or right edges
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: " + score.player2);
        }

        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: " + score.player1);
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1_000_000_000.0 / amountOfTicks;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }

            // small sleep to reduce CPU usage
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Key adapter forwards events to paddles
    public class AL extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
