package tankrotationexample.game;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Floor extends GameObject{
    int x, y;
    int state = 2;
    BufferedImage floorImage;

    public Floor(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.floorImage = wallImage;
    }

    @Override
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.floorImage, x, y, null);
    }

    @Override
    public void update() {

    }
}
