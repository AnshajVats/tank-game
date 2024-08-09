package tankrotationexample.menus;

import tankrotationexample.Launcher;
import tankrotationexample.ResourceManager;
import tankrotationexample.game.GameWorld;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private final BufferedImage menuBackground;
    private final Launcher lf;
    private final GameWorld gameWorld;
    private JLabel winnerLabel;


    public EndGamePanel(Launcher lf, GameWorld gameWorld) {
        this.lf = lf;
        this.gameWorld = gameWorld;
        this.menuBackground = ResourceManager.getSprite("title");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        this.winnerLabel = new JLabel();
        this.winnerLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        this.winnerLabel.setForeground(Color.WHITE);
        this.winnerLabel.setBounds(150, 200, 250, 50);
        this.add(winnerLabel);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(150, 300, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("game")));


        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(150, 400, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
        this.add(exit);
    }

    public void setWinnerText(String winner) {
        this.winnerLabel.setText("Winner: " + winner);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }
}
