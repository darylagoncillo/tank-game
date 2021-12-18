package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUpShield extends GameObject {
    int x, y, vx, vy, angle;
    BufferedImage shieldImage;
    Rectangle hitBox;

    public PowerUpShield(int x, int y, BufferedImage shieldImage) {
        this.x = x;
        this.y = y;
        this.shieldImage = shieldImage;
        this.hitBox = new Rectangle(x, y, this.shieldImage.getWidth(), this.shieldImage.getHeight());
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.shieldImage, x, y, null);
    }

    @Override
    public void update() {

    }
}