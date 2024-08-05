package tankrotationexample.game;

import tankrotationexample.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected BufferedImage img;
    protected Rectangle hitBox;
    protected boolean dead = false;

    public GameObject(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y, img.getWidth(), img.getHeight());
    }


    public static GameObject newInstanceOf(String type, float x, float y) {
        return switch (type) {
            case "1" -> new Wall(x, y, ResourceManager.getSprite("wall"));
            case "2" -> new BreakableWalls(x, y, ResourceManager.getSprite("wall2"));
            case "3" -> new SpeedUp(x, y, ResourceManager.getSprite("speed"));
            case "4" -> new RocketPowerUp(x, y, ResourceManager.getSprite("pickUp"));
            case "5" -> new HealthPowerUp(x, y, ResourceManager.getSprite("health"));
            default -> throw new IllegalArgumentException("Unknown type -> %s\n" .formatted(type));
        };
    }



    public void drawImg(Graphics g) {
            g.drawImage(this.img, (int) this.x, (int) this.y, null);
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public void handleCollision(GameObject obj2) {
    }

    public boolean isDead() {
        return this.dead;
    }
}
