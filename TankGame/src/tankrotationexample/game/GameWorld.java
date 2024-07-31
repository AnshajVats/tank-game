package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    List <GameObject> gObjs = new ArrayList<>(1000);

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update(); // update tank
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {


        int row = 0;

        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(GameWorld.class.getClassLoader().getResourceAsStream("map.csv")));

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            while (mapReader.ready()) {
                String line = mapReader.readLine();
                String[] mapInfo = line.split(",");
                for (int col = 0; col < mapInfo.length; col++) {
                    String type = mapInfo[col];
                    if (type.equals("0")) continue;
                    this.gObjs.add(GameObject.newInstanceOf(type, col * 32, row * 32));
                }
                row++;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("t1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);


        t2 = new Tank(400, 400, 0, 0, (short) 180, ResourceManager.getSprite("t2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);
        buffer.fillRect(0,0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        this.gObjs.forEach(obj -> obj.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        g2.drawImage(world, 0, 0, null);
    }

    public void renderFloor(Graphics buffer) {
        BufferedImage floor = ResourceManager.getSprite("background");
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }

    }
}
