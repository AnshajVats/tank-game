package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class BreakableWalls extends GameObject implements CollideAble {

    public BreakableWalls(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void handleCollision(GameObject obj) {
        if (obj instanceof Rocket) {
            this.dead = true;
        }
    }

}
