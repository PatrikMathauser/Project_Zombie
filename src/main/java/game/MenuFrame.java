package game;

import game.spriteloader.SpriteLoader;
import ui.ImageButton;
import ui.ImageComponent;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JPanel {
    private final Dimension BUTTON_DIMENSION = new Dimension(390, 81);

    private static Image wallpaper = SpriteLoader.load("/menu/menuBackground.png");

    private static Image logoImage = SpriteLoader.load("/menu/gameTitle.png");

    private ImageButton quitButton = new ImageButton(SpriteLoader.load("/menu/quitButton.png"), BUTTON_DIMENSION);

    private ImageButton playButton = new ImageButton(SpriteLoader.load("/menu/playButton.png"),BUTTON_DIMENSION);


    public MenuFrame(JPanel mainFrame, Game game) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        playButton.setAlignmentX(CENTER_ALIGNMENT);
        quitButton.setAlignmentX(CENTER_ALIGNMENT);

        playButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getLayout();
            cl.show(mainFrame, "game");
        });

        quitButton.addActionListener(e -> game.dispose());

        ImageComponent logo = new ImageComponent(logoImage, 452,195);

        add(Box.createVerticalGlue());
        add(logo);
        add(Box.createRigidArea(new Dimension(BUTTON_DIMENSION.width,BUTTON_DIMENSION.height / 2)));
        add(playButton);
        add(Box.createRigidArea(new Dimension(BUTTON_DIMENSION.width,BUTTON_DIMENSION.height / 2)));
        add(quitButton);
        add(Box.createVerticalGlue());


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(wallpaper, 0,0, getWidth(), getHeight(), this);
    }
}
