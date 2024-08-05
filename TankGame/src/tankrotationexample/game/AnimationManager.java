package tankrotationexample.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private static List<Animation> animations;

    public AnimationManager() {
        animations = new ArrayList<>();
    }

    public static void update() {
        if (animations == null) {
            return;
        }
        for (int i = 0; i < animations.size(); i++) {
            Animation animation = animations.get(i);
            animation.update();
        }
    }

    public static void render(Graphics2D g) {
        if (animations == null) {
            return;
        }
        for (int i = 0; i < animations.size(); i++) {
            Animation animation = animations.get(i);
            animation.render(g);
        }
    }

    public static void add(Animation animation) {
        if (animations == null) {
            animations = new ArrayList<>();
        }
        animations.add(animation);
    }
}
