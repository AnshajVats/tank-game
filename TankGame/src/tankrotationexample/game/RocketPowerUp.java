package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class RocketPowerUp extends GameObject implements PowerUp, CollideAble {
    public RocketPowerUp(float x, float y, BufferedImage rocket) {
        super(x, y, rocket);
    }

    @Override
    public void applyPowerUp(Tank tank) {
        tank.setCoolDown(1500);
    }

    @Override
    public void handleCollision(GameObject obj) {
        if (obj instanceof Tank) {
            this.dead = true;
        }
    }
}
