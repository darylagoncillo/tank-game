package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUpHealth extends GameObject {
    int x, y, vx, vy, angle;
    BufferedImage healthImage;
    Rectangle hitBox;

    public PowerUpHealth(int x, int y, BufferedImage healthImage) {
        this.x = x;
        this.y = y;
        this.healthImage = healthImage;
        this.hitBox = new Rectangle(x, y, this.healthImage.getWidth(), this.healthImage.getHeight());
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.healthImage, x, y, null);
    }

    @Override
    public void update() {

    }
}
