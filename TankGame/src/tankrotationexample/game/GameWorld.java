package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
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
    private final List <GameObject> gObjs = new ArrayList<>(1000);

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        this.resetGame();
        Sound bg = ResourceManager.getSound("background");
//        bg.loop();
//        bg.play();
        try {
            while (true) {
                this.tick++;
                for (int i = this.gObjs.size() - 1; i >= 0; i--) {
                    GameObject obj = this.gObjs.get(i);
                    if (obj instanceof UpdateAble u) {
                        u.update(this);
                    }
                    else {
                        break;
                    }
                }
                AnimationManager.update();
                this.checkCollisions();
                checkDead();
                this.gObjs.removeIf(obj -> obj.isDead());
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

    private void checkDead() {
        if (this.t1.isDead()) {
            this.t1.setX(300);
            this.t1.setY(300);
            Thread.currentThread().interrupt();
            SwingUtilities.invokeLater(() -> lf.setFrame("end"));
        }
        if (this.t2.isDead()) {
            this.t2.setX(400);
            this.t2.setY(400);
            Thread.currentThread().interrupt();
            SwingUtilities.invokeLater(() -> lf.setFrame("end"));
        }
    }

    public String getWinner() {
        if (this.t1.isDead() && !this.t2.isDead()) {
            t2.setDead(false);
            return "Player 2";
        } else if (this.t2.isDead() && !this.t1.isDead()) {
            t1.setDead(false);
            return "Player 1";
        } else {
            return "Draw";
        }
    }


    private void checkCollisions() {
        int count = 0;
        for (int i = 0; i < this.gObjs.size(); i++) {
            GameObject obj1 = this.gObjs.get(i);
            if (!(obj1 instanceof UpdateAble)) {
                continue;
            }
            for (int j = 0; j < this.gObjs.size(); j++) {
                if (i == j) continue;

                GameObject obj2 = this.gObjs.get(j);
                if (!(obj2 instanceof CollideAble)) {
                    continue;
                }
                count++;
                if (obj1.getHitBox().intersects(obj2.getHitBox())) {
                    ((CollideAble) obj1).handleCollision(obj2);
                    ((CollideAble) obj2).handleCollision(obj1);
                }
            }
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
        this.gObjs.add(t1);


        t2 = new Tank(400, 400, 0, 0, (short) 180, ResourceManager.getSprite("t2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);
        this.gObjs.add(t2);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.fillRect(0,0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        this.renderFloor(buffer);
        for (int i = 0; i < this.gObjs.size(); i++) {
            GameObject obj = this.gObjs.get(i);
            obj.drawImg(buffer);
        }
        AnimationManager.render(buffer);
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.displaySplitScreen(g2);
        this.displayMiniMap(g2);

    }

    private void displaySplitScreen(Graphics2D buffer) {

        BufferedImage lh = this.world.getSubimage((int)this.t1.getScreenX(), (int)this.t1.getScreenY(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = this.world.getSubimage((int)this.t2.getScreenX(), (int)this.t2.getScreenY(), GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_SCREEN_HEIGHT);
        buffer.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH/2, 0, null);
        buffer.drawImage(lh, 0, 0, null);

    }

    public void renderFloor(Graphics2D buffer) {
        BufferedImage floor = ResourceManager.getSprite("background");
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    static double scaleFactor = 0.15;
    public void displayMiniMap(Graphics2D onScreenPanel) {

        double mmx = GameConstants.GAME_SCREEN_WIDTH/2. - (GameConstants.GAME_WORLD_WIDTH* scaleFactor)/2.;
        double mmy = GameConstants.GAME_SCREEN_HEIGHT - (GameConstants.GAME_WORLD_HEIGHT* scaleFactor) - 50;

        BufferedImage mm = this.world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        AffineTransform sc =  AffineTransform.getTranslateInstance(mmx, mmy);
        sc.scale(scaleFactor, scaleFactor);
        onScreenPanel.drawImage(mm, sc, null);

    }

    public  void addGameObject(GameObject obj) {
        this.gObjs.add(obj);
    }
}
