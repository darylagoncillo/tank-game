package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Wall extends GameObject {
    private int wallType;

    public Wall(Image img, double x, double y, int type) {
        this.wallType = type;
    }

    public Wall() {

    }

    public void drawImage(Graphics g) {
    }

    public void update() {
    }

    public Rectangle2D.Double getRectangle() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public int getType() {
        return this.wallType;
    }
}
