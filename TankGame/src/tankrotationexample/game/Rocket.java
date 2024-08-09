package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Rocket extends GameObject implements Poolable, UpdateAble, CollideAble {

    private float vx;
    private float vy;
    private float angle;

    private float R = 2;
    private final float ROTATIONSPEED = 3.0f;

    public Rocket(BufferedImage img) {
        super(0, 0, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
    }

    public Rocket(float x, float y, float angle, BufferedImage img) {
        super(x, y, img);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
    }

    public void update(GameWorld gw) {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation((int) x, (int) y);
    }

    public void handleCollision(GameObject otherObject) {

        if (otherObject instanceof Wall || otherObject instanceof BreakableWalls) {
            this.dead = true;
            ResourceManager.getSound("hit").play();
            AnimationManager.add(new Animation(this.x, this.y, ResourceManager.getAnim("explosion_lg")));
        }
        if (otherObject instanceof Tank) {
            this.dead = true;
        }

    }

    public void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 50) {
            x = GameConstants.GAME_WORLD_WIDTH - 50;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 50) {
            y = GameConstants.GAME_WORLD_HEIGHT - 50;
        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        g2d.drawRect((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        g2d.setColor(Color.RED);
        g2d.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);

    }

    @Override
    public void initObject(float x, float y) {
        super.x = x;
        super.y = y;
    }

    @Override
    public void initObject(float x, float y, float angle) {
        super.x = x;
        super.y = y;
        this.angle = angle;
        this.hitBox.setLocation((int) x, (int) y);

    }

    @Override
    public void resetObject() {
        super.x = -5;
        super.y = -5;
    }
}
