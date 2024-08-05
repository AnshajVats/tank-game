package tankrotationexample;

import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> anims = new HashMap<>();
    private final static Map<String, Integer> anamInfo = new HashMap<>(){{
        put("explosion_lg", 6);
        put("explosion_sm", 6);
        put("powerpick",32);

    }};

    public static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path), "Resource not found: " + path)
        );
    }

    public static Sound loadSound(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path), "Sound Resource not found: " + path)
        );

        Clip clip = AudioSystem.getClip();
        clip.open(ais);
        return new Sound(clip);
    }



    private static void  initSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank2.png"));
        ResourceManager.sprites.put("bullet", loadSprite("Rocket.gif"));
        ResourceManager.sprites.put("wall", loadSprite("wall1.png"));
        ResourceManager.sprites.put("wall2", loadSprite("wall2.png"));
        ResourceManager.sprites.put("background", loadSprite("Background.bmp"));
        ResourceManager.sprites.put("pickUp", loadSprite("Pickup.gif"));
        ResourceManager.sprites.put("health", loadSprite("health.png"));
        ResourceManager.sprites.put("shield1", loadSprite("Shield1.gif"));
        ResourceManager.sprites.put("shield2", loadSprite("Shield2.gif"));
        ResourceManager.sprites.put("title", loadSprite("title.png"));
        ResourceManager.sprites.put("rocket", loadSprite("Rocket.gif"));
        ResourceManager.sprites.put("speed", loadSprite("speed.png"));

    }


    private static void initSounds() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        ResourceManager.sounds.put("background", loadSound("Music.mid"));
        ResourceManager.sounds.put("shoot", loadSound("Explosion_small.wav"));
        ResourceManager.sounds.put("hit", loadSound("Explosion_large.wav"));
        ResourceManager.sounds.put("powerUp", loadSound("PickUpPowerupSound.wav"));
        ResourceManager.sounds.put("tankBlast", loadSound("TankExplosionSound.wav"));
    }

    private static void initAnime() {

        String baseFormat = "%s/%s_%04d.png";
        ResourceManager.anamInfo.forEach((animationName, frameCount) -> {
            List<BufferedImage> frames = new ArrayList<>(frameCount);
            try {
                for (int i = 0; i < frameCount; i++) {
                     String path = String.format(baseFormat, animationName, animationName, i);
                    frames.add(loadSprite(path));

                }
                ResourceManager.anims.put(animationName, frames);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }



    public static void loadAssets(){

        try {
            initSprites();
            initSounds();
            initAnime();
        } catch (IOException e) {
            throw new RuntimeException("Loading assets failed", e);
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }



    public static BufferedImage getSprite(String name) {
        if (!ResourceManager.sprites.containsKey(name)) {
            throw new IllegalArgumentException("No sprite with name " + name);
        }
        return ResourceManager.sprites.get(name);
    }

    public static Sound getSound(String name) {
        if (!ResourceManager.sounds.containsKey(name)) {
            throw new IllegalArgumentException("No sprite with name " + name);
        }
        return ResourceManager.sounds.get(name);
    }
    public static List<BufferedImage> getAnim(String name) {
        if (!ResourceManager.anims.containsKey(name)) {
            throw new IllegalArgumentException("No sprite with name " + name);
        }
        return ResourceManager.anims.get(name);
    }


    public static void main(String[] args) {
        ResourceManager.loadAssets();
        System.out.println("Assets loaded successfully");

    }

}
