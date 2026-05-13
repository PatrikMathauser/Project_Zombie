package game;

import game.spriteloader.SpriteLoader;
import ui.ImageButton;
import ui.ImageComponent;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JPanel {

    /** Reprezentuje velikost tlačítek */
    private final Dimension BUTTON_DIMENSION = new Dimension(390, 81);

    /** Načtení pozadí menu  */
    private static Image wallpaper = SpriteLoader.load("/menu/menuBackground.png");

    /** Načtení loga hry */
    private static Image logoImage = SpriteLoader.load("/menu/gameTitle.png");

    /** Načtení tlačítka pro odejití  */
    private ImageButton quitButton = new ImageButton(SpriteLoader.load("/menu/quitButton.png"), BUTTON_DIMENSION);

    /** Načtení tlačítka pro spuštění herní smyčky */
    private ImageButton playButton = new ImageButton(SpriteLoader.load("/menu/playButton.png"),BUTTON_DIMENSION);

    /**
     * Reprezentuje menu hry
     * @param mainFrame hlavní panel
     * @param game hlavní okno
     */
    public MenuFrame(JPanel mainFrame, Game game) {

        // Nastavení BoxLayoutu podél osy Y
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Vycentrování tlačítek v layout
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        quitButton.setAlignmentX(CENTER_ALIGNMENT);

        // Při zmáčknutí tlačítka Play spustíme herní smyčku
        playButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainFrame.getLayout();
            cl.show(mainFrame, "game");
        });

        // Při zmáčknutí tlačítka Quit vypneme hru
        quitButton.addActionListener(e -> game.dispose());

        // Vytvoření herního loga
        ImageComponent logo = new ImageComponent(logoImage, 452,195);

        // Vyplnění layoutu
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

        // Vykreslení pozadí menu
        g.drawImage(wallpaper, 0,0, getWidth(), getHeight(), this);
    }
}
