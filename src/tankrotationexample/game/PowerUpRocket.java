package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUpRocket extends GameObject{
    int x, y, vx, vy, angle;
    BufferedImage rocketImage;
    Rectangle hitBox;

    public PowerUpRocket(int x, int y, BufferedImage rocketImage) {
        this.x = x;
        this.y = y;
        this.rocketImage = rocketImage;
        this.hitBox = new Rectangle(x, y, this.rocketImage.getWidth(), this.rocketImage.getHeight());
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.rocketImage, x, y, null);
    }

    @Override
    public void update() {

    }
}
