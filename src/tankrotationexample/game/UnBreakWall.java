package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnBreakWall extends Wall {
    int x, y;
    int state = 2;
    BufferedImage unbreak;

    public UnBreakWall(int x, int y, BufferedImage wallImage) {
        super();
        this.x = x;
        this.y = y;
        this.unbreak = wallImage;
        this.hitBox = new Rectangle(x, y, this.unbreak.getWidth(), this.unbreak.getHeight());
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.unbreak, x, y, null);
    }

    @Override
    public void update() {

    }
}
