package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected BufferedImage img;

    public GameObject(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }


    public static GameObject newInstanceOf(String type, float x, float y) {
        return switch (type) {
            case "1" -> new Wall(x, y, ResourceManager.getSprite("wall"));
            case "2" -> new BreakableWalls(x, y, ResourceManager.getSprite("wall2"));
            case "3" -> new Shield(x, y, ResourceManager.getSprite("shield1"));
            case "4" -> new Rocket(x, y, ResourceManager.getSprite("rocket"));
            case "5" -> new Health(x, y, ResourceManager.getSprite("health"));
            default -> throw new IllegalArgumentException("Unknown type -> %s\n" .formatted(type));
        };
    }

    public void drawImage(Graphics g) {
            g.drawImage(this.img, (int) this.x, (int) this.y, null);
        }
}
