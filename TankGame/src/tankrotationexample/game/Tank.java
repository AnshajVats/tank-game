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
    private int health;


    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x, y, img);
        this.screen_x = x;
        this.screen_y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.health = 3;
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

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public float getHealthPercentage() {
        return (float) this.health / 3 * 100; // Assuming max health is 3
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
        float offsetX = tankImg.getWidth() ;
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


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

        int healthBarWidth = this.img.getWidth();
        int healthBarHeight = 5;
        int healthBarY = (int)y - 10; // 10 pixels above the tank

        // Background (red)
        g2d.setColor(Color.RED);
        g2d.fillRect((int)x, healthBarY, healthBarWidth, healthBarHeight);

        // Foreground (green)
        g2d.setColor(Color.GREEN);
        int currentHealthWidth = (int)(healthBarWidth * (getHealthPercentage() / 100));
        g2d.fillRect((int)x, healthBarY, currentHealthWidth, healthBarHeight);

    }

    @Override
    public void handleCollision(GameObject by) {
        if (by instanceof Wall || by instanceof BreakableWalls) {
            // Calculate direction in degrees for better readability
            double directionDegrees = Math.toDegrees(angle);

            // Calculate overlap between tank and wall
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
        if (by instanceof Rocket) {
            Random random = new Random();
            int spawnAreaMinX = 50;
            int spawnAreaMaxX = GameConstants.GAME_WORLD_WIDTH - 50;
            int spawnAreaMinY = 50;
            int spawnAreaMaxY = GameConstants.GAME_WORLD_HEIGHT - 50;
            this.setX(random.nextInt(spawnAreaMaxX - spawnAreaMinX) + spawnAreaMinX);
            this.setY(random.nextInt(spawnAreaMaxY - spawnAreaMinY) + spawnAreaMinY);
            ResourceManager.getSound("tankBlast").play();
            AnimationManager.add(new Animation(this.x, this.y, ResourceManager.getAnim("explosion_lg")));
        }

        if (by instanceof PowerUp){
            ((PowerUp) by).applyPowerUp(this);
            ResourceManager.getSound("powerUp").play();
            AnimationManager.add(new Animation(by.x, by.y, ResourceManager.getAnim("powerpick")));
        }

    }

    public void setSpeed(float i) {
        this.R = i;
    }
}
