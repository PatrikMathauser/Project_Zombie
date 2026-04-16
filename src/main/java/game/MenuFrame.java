package game;

import game.spriteloader.SpriteLoader;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JPanel {

    private static Image wallpaper = SpriteLoader.load("/menu/menuBackground.png");


    public MenuFrame(JPanel mainFrame, Game game) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton playButton = new JButton("Play");
        JButton quitButton = new JButton("Quit");

        playButton.setAlignmentX(CENTER_ALIGNMENT);
        quitButton.setAlignmentX(CENTER_ALIGNMENT);

        playButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getLayout();
            cl.show(mainFrame, "game");
        });

        quitButton.addActionListener(e -> game.dispose());



        add(playButton);
        add(quitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(wallpaper, 0,0, getWidth(), getHeight(), this);
    }
}
