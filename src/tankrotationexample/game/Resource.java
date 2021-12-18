package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.imageio.ImageIO.read;

public class Resource {
    private static Map<String, BufferedImage> resources;

    static {
        Resource.resources = new HashMap<>();
        try {
            Resource.resources.put("floor", read((TRE.class.getClassLoader().getResource("floor.png"))));
            Resource.resources.put("tank1", read((TRE.class.getClassLoader().getResource("tank1.png"))));
            Resource.resources.put("tank2", read((TRE.class.getClassLoader().getResource("tank2.png"))));
            Resource.resources.put("bullet", read((TRE.class.getClassLoader().getResource("bullet.png"))));
            Resource.resources.put("break", read((TRE.class.getClassLoader().getResource("break.png"))));
            Resource.resources.put("unbreak", read((TRE.class.getClassLoader().getResource("unbreak.png"))));
            Resource.resources.put("puHealth", read((TRE.class.getClassLoader().getResource("puHealth.png"))));
            Resource.resources.put("puRocket", read((TRE.class.getClassLoader().getResource("puRocket.png"))));
            Resource.resources.put("puShield", read((TRE.class.getClassLoader().getResource("puShield.png"))));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-5);
        }
    }

    public static BufferedImage getResourceImage(String key) {
        return Resource.resources.get(key);
    }
}