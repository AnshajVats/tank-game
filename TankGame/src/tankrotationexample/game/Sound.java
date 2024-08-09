package tankrotationexample.game;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    private final Clip clip;
    private int loopCount;

    public Sound(Clip c) {
        this.clip = c;
        this.loopCount = 0;
    }

    public Sound(Clip c, int loopCount) {
        this.clip = c;
        this.loopCount = loopCount;
        this.clip.loop(this.loopCount);
    }

    public void play() {
        if (this.clip.isRunning()) {
            this.clip.stop();
        }
        this.clip.setFramePosition(0);
        this.clip.start();
    }

    public void stop() {
        this.clip.stop();
    }

    public void loop() {
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setVolume(float volume) {
        FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20.0f * (float) Math.log10(volume));
    }
}
