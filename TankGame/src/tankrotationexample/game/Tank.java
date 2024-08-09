package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject implements UpdateAble, CollideAble {

    private float screen_x;
    private float screen_y;

    private float vx;
    private float vy;
    private float angle;

    private float R = 1;
    private final float ROTATIONSPEED = 3.0f;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shoot;

    private  long coolDown = 2000;
    private long lastShot = 0;
    private int lives;
    private int maxHealth;
    private int health;
    private boolean dead;


    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x, y, img);
        this.screen_x = x;
        this.screen_y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.lives = 3;
        maxHealth = 100;
        this.health = this.maxHealth;
        this.dead = false;

    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleShootPressed() {this.shoot = true;}

    void toggleLeftPressed() {
        this.LeftPressed = true;
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
        this.shoot = false;
    }
    public float getScreenX() {
        return screen_x;
    }
    public  float getScreenY() {
        return screen_y;
    }

    public boolean setCoolDown(long coolDown) {
        if (coolDown < 0) {
            return false;
        }
        this.coolDown = coolDown;
        return true;
    }



    public void update(GameWorld gw) {
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

        long currentTime = System.currentTimeMillis();
        if (this.shoot && currentTime - this.lastShot > this.coolDown) {
            this.lastShot = currentTime;
            this.shoot(gw);
        }

        centerScreen();
        this.hitBox.setLocation((int)x, (int)y);
    }

    private void shoot(GameWorld gw) {
        BufferedImage roc = ResourceManager.getSprite("rocket");
        BufferedImage tankImg = this.img;

        // Calculate the center of the tank
        float tankCenterX = x + tankImg.getWidth() / 2f;
        float tankCenterY = y + tankImg.getHeight() / 2f;

        // Adjust these values as needed
        float offsetX = tankImg.getWidth() + 10;
        float offsetY = tankImg.getHeight();

        // Calculate the position of the rocket in front of the tank
        float rocketX = tankCenterX + offsetX * (float)Math.cos(Math.toRadians(angle)) - roc.getWidth() / 2f;
        float rocketY = tankCenterY + offsetY * (float)Math.sin(Math.toRadians(angle)) - roc.getHeight() / 2f;

        var p = ResourcePools.getPool("rocket");
        p.initObject(rocketX, rocketY, angle);
        gw.addGameObject((Rocket) p);
        ResourceManager.getSound("shoot").play();
        AnimationManager.add(new Animation(rocketX, rocketY, ResourceManager.getAnim("explosion_sm")));

    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }


    private void centerScreen() {

        this.screen_x = this.x - GameConstants.GAME_SCREEN_WIDTH/4f;
        this.screen_y = this.y - GameConstants.GAME_SCREEN_HEIGHT/2f;
        if (this.screen_x < 0) this.screen_x = 0;
        if (this.screen_x > GameConstants.GAME_WORLD_WIDTH -  GameConstants.GAME_SCREEN_WIDTH /2f)
            this.screen_x = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f;
        if(this.screen_y < 0) this.screen_y = 0;
        if(this.screen_y > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT)
            this.screen_y = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;


    }

    public void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 110) {
            y = GameConstants.GAME_WORLD_HEIGHT -  110;
        }
    }


    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void drawHealthBar(Graphics2D g2d) {
        int healthPercentage = getHealthPercentage();
        g2d.setColor(Color.RED);
        g2d.fillRect((int)x, (int)y - 10, this.img.getWidth(), 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int)x, (int)y - 10, (int)(this.img.getWidth() * (healthPercentage / 100.0)), 5);
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

        drawHealthBar(g2d);
        drawLives(g2d);
    }

    private void drawLives(Graphics2D g2d) {
        int circleSize = 10;
        int spacing = 5;
        int startX = (int)x;
        int startY = (int)y + this.img.getHeight() + 10;

        for (int i = 0; i < lives; i++) {
            g2d.setColor(Color.RED);
            g2d.fillOval(startX + (circleSize + spacing) * i, startY, circleSize, circleSize);
        }
    }

    private int getHealthPercentage() {
        return (int)(((double)this.health / this.maxHealth) * 100);
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int health) {
        this.lives = health;
    }

    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        if (this.health == 0) {
            loseLife();
        }
    }

    private void loseLife() {
        this.lives--;
        if (this.lives > 0) {
            respawnAtRandom();
        } else {
            this.dead = true;
        }
    }
    public boolean isDead() {
        return this.dead;
    }
    public boolean hasRemainingLives() {
        return this.lives > 0;
    }

    public void respawnAtRandom(){
        Random random = new Random();
        int spawnAreaMinX = 50;
        int spawnAreaMaxX = GameConstants.GAME_WORLD_WIDTH - 50;
        int spawnAreaMinY = 50;
        int spawnAreaMaxY = GameConstants.GAME_WORLD_HEIGHT - 50;
        this.setX(random.nextInt(spawnAreaMaxX - spawnAreaMinX) + spawnAreaMinX);
        this.setY(random.nextInt(spawnAreaMaxY - spawnAreaMinY) + spawnAreaMinY);

        // Reset health
        this.health = this.maxHealth;
    }
    @Override
    public void handleCollision(GameObject by) {
        if (by instanceof Wall || by instanceof BreakableWalls) {
            hitsObj(by);
        }
        if (by instanceof Rocket) {
            takeDamage(10); // Increased damage to make it more noticeable
            ResourceManager.getSound("tankBlast").play();
            AnimationManager.add(new Animation(this.x, this.y, ResourceManager.getAnim("explosion_lg")));
        }

        if (by instanceof PowerUp){
            ((PowerUp) by).applyPowerUp(this);
            ResourceManager.getSound("powerUp").play();
            AnimationManager.add(new Animation(by.x, by.y, ResourceManager.getAnim("powerpick")));
        }

        if (by instanceof Tank) {
            // Calculate direction in degrees for better readability
            hitsObj(by);


        }

    }

    public void hitsObj(GameObject by){
        // Calculate overlap between tank and wall
        double directionDegrees = Math.toDegrees(angle);
        Rectangle tankRect = this.getHitBox();
        Rectangle wallRect = by.getHitBox();
        int overlapX = Math.min(tankRect.x + tankRect.width, wallRect.x + wallRect.width) - Math.max(tankRect.x, wallRect.x);
        int overlapY = Math.min(tankRect.y + tankRect.height, wallRect.y + wallRect.height) - Math.max(tankRect.y, wallRect.y);

        // Push the tank out of the wall
        if (overlapX < overlapY) {
            if (tankRect.x < wallRect.x) {
                x -= overlapX;
            } else {
                x += overlapX;
            }
        } else {
            if (tankRect.y < wallRect.y) {
                y -= overlapY;
            } else {
                y += overlapY;
            }
        }

        // Prevent forward movement
        if ((directionDegrees >= 0 && directionDegrees < 90) ||
                (directionDegrees >= 270 && directionDegrees < 360)) {
            this.vx = 0;
            this.vy = 0;
        }
    }
    public void setSpeed(float i) {
        this.R = i;
    }

    public void setDead(boolean b) {
        this.dead = b;
    }
}
