package tankrotationexample;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Clip> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();

    public static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path), "Resource not found: " + path)
        );
    }
    public static void  initSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank2.png"));
        ResourceManager.sprites.put("bullet", loadSprite("Rocket.gif"));
        ResourceManager.sprites.put("wall", loadSprite("wall1.png"));
        ResourceManager.sprites.put("wall2", loadSprite("wall2.png"));
        ResourceManager.sprites.put("background", loadSprite("Background.bmp"));
        ResourceManager.sprites.put("pickUp", loadSprite("Pickup.gif"));
        ResourceManager.sprites.put("health", loadSprite("health.png"));
        ResourceManager.sprites.put("shield1", loadSprite("Shield1.gif"));
        ResourceManager.sprites.put("shield2", loadSprite("Shield2.gif"));
        ResourceManager.sprites.put("title", loadSprite("title.bmp"));
        ResourceManager.sprites.put("rocket", loadSprite("Rocket.gif"));

    }

    public static void loadAssets(){

        try {
            initSprites();
        } catch (IOException e) {
            throw new RuntimeException("Loading assets failed", e);
        }
    }

    public static BufferedImage getSprite(String name) {
        if (!ResourceManager.sprites.containsKey(name)) {
            throw new IllegalArgumentException("No sprite with name " + name);
        }
        return ResourceManager.sprites.get(name);
    }

    public static void main(String[] args) {
        ResourceManager.loadAssets();
        System.out.println("Assets loaded successfully");

    }

}
