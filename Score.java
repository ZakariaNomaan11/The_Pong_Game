import java.awt.*;

public class Score {

    private final int gameWidth;
    private final int gameHeight;
    int player1;
    int player2;

    public Score(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));

        // center divider
        g.drawLine(gameWidth / 2, 0, gameWidth / 2, gameHeight);

        // format scores as two digits
        String s1 = String.format("%02d", player1);
        String s2 = String.format("%02d", player2);

        // positions relative to the vertical center line
        int leftX = (gameWidth / 2) - 85;
        int rightX = (gameWidth / 2) + 20;
        int y = 50;

        g.drawString(s1, leftX, y);
        g.drawString(s2, rightX, y);
    }
}
