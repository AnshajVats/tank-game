package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class SpeedUp extends GameObject implements PowerUp, CollideAble {

    public SpeedUp(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void applyPowerUp(Tank tank) {
        tank.setSpeed(tank.getSpeed() + 1);
    }

    @Override
    public void handleCollision(GameObject obj) {
        if (obj instanceof Tank) {
            this.dead = true;
        }
    }
}
