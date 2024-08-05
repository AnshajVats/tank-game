package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {
    private float x,y;
    List<BufferedImage> frames;
    private long delay = 30;
    private long lastTime = 0;
    private boolean running = false;
    private int currentFrame;

    public Animation(float x, float y, List<BufferedImage> frames){
        this.x = x - frames.getFirst().getWidth()/2f;
        this.y = y - frames.getFirst().getHeight()/2f;
        this.frames = frames;
        this.running = true;
        this.currentFrame = 0;
    }

    public void update(){
        long Ctime = System.currentTimeMillis();
        if (Ctime > lastTime + delay){
            this.currentFrame++;
            if (currentFrame == frames.size()){
                this.running = false;
            }
            this.lastTime = Ctime;
        }

    }

    public void render(Graphics g){
        if (this.running){
            g.drawImage(this.frames.get(currentFrame), (int)x, (int)y, null);
        }
    }
}
