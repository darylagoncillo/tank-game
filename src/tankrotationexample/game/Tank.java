package tankrotationexample.game;


import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author anthony-pc
 */
public class Tank extends GameObject {

    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private int health;
    private int livesRemaining;

    private int saveX;
    private int saveY;

    private final int R = 2;
    private final float ROTATIONSPEED = 3.0f;
    private Rectangle hitBox;
    private final ArrayList<Bullet> ammo;
    private final int respawnX, respawnY, respawnAngle;


    private final BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    private boolean collides;
    //powers
    private boolean hasShield = false;
    private boolean hasSuperBullet = false;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        super();
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.ammo = new ArrayList<>();
        this.hitBox = new Rectangle(x,y,this.img.getWidth(), this.img.getHeight());

        this.health = 5;
        this.hasShield = false;
        this.livesRemaining = 2;
        respawnX = x;
        respawnY = y;
        respawnAngle = angle;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getHitBox() {
        return hitBox.getBounds();
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() {
        this.ShootPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    public ArrayList<Bullet> getAmmo() {
        return this.ammo;
    }

    public void update() {
        if(!collides) {
            saveCoordinatesX();
            saveCoordinatesY();
        }
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.ShootPressed && TRE.tick % 40 == 0) {
            Bullet b = new Bullet(x, y, angle, Resource.getResourceImage("bullet"));
            this.ammo.add(b);
        }
        this.hitBox.setLocation(x,y);
        this.ammo.forEach(bullet -> bullet.update());
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        this.hitBox.setLocation(x,y);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation(x,y);
    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 120) {
            y = GameConstants.GAME_WORLD_HEIGHT - 120;
        }
    }

    public void saveCoordinatesX() {
        saveX = x;
    }

    public void saveCoordinatesY() {
        saveY = x;
    }

    public void setLives(int lives) {
        this.livesRemaining = lives;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public void setCollides(boolean collides) {
        this.collides = collides;
    }

    public void setBulletDelay(boolean bullet) {
        hasSuperBullet = bullet;
    }

    public int getLives() {
        return this.livesRemaining;
    }

    public int getHealth() {
        return this.health;
    }

    public int getLivesRemaining() {
        return livesRemaining;
    }

    public boolean getBulletDelay() {
        return hasSuperBullet;
    }

    public void handleCollision() {

        if (collides) {
            this.x = saveX;
            this.y = saveY;
            collides = false;
        }
    }

    public void Respawn(int health) {
        x = respawnX;
        y = respawnY;
        angle = respawnAngle;
        this.health = health;
    }

    public Rectangle2D.Double getRectangle() {
        return new Rectangle2D.Double(x, y, this.img.getWidth(), this.img.getHeight());
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        //tank
        g2d.drawImage(this.img, rotation, null);

        //bullets
        this.ammo.forEach(bullet -> bullet.drawImage(g));

        //tank hitbox visual
        g2d.drawRect(x, y, this.img.getWidth(), this.img.getHeight());
    }
}
