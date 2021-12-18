package tankrotationexample.game;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    protected double x, y, height, width;
    protected Image img;
    protected Rectangle hitBox;

    public GameObject() {
        //default constructor
    }
    public GameObject(int x, int y, int angle, BufferedImage img) {

        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }
    public Rectangle2D.Double getRectangle() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public abstract void drawImage(Graphics g);
    public abstract void update();
}
