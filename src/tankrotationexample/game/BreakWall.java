package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakWall extends Wall {
    int x, y;
    int state = 2;
    BufferedImage wallImage;

    public BreakWall(int x, int y, BufferedImage wallImage) {
        super();
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
        this.hitBox = new Rectangle(x, y, this.wallImage.getWidth(), this.wallImage.getHeight());
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.wallImage, x, y, null);
    }

    @Override
    public void update() {

    }
}
