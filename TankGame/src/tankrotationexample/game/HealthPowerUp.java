package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class HealthPowerUp extends GameObject implements PowerUp, CollideAble {

    public HealthPowerUp(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyPowerUp(Tank tank) {
        tank.setLives(tank.getLives() + 1);
    }

    @Override
    public void handleCollision(GameObject obj) {
        if (obj instanceof Tank) {
            this.dead = true;
        }
    }
}
