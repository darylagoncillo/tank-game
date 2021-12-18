package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    int x, y, vx, vy, angle;
    int R = 7;
    BufferedImage bulletImage;
    Rectangle hitBox;

    public Bullet(int x, int y, int angle, BufferedImage bulletImage) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.hitBox = new Rectangle(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());
    }

    public void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    public void checkBorder() {
        if (x < 33) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 85) {
            x = GameConstants.GAME_WORLD_WIDTH - 85;
        }
        if (y < 35) {
            y = 35;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    public Rectangle2D.Double getRectangle() {
        return new Rectangle2D.Double(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());
    }

    public void update() {
        moveForwards();
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.bulletImage.getWidth() / 2.0, this.bulletImage.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.bulletImage, rotation, null);
        //tank bullet hitbox
        //g2d.drawRect(x, y, this.bulletImage.getWidth(), this.bulletImage.getHeight());
    }
}
