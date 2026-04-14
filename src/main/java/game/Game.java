package game;

import javax.swing.*;
import java.awt.*;

/**
 * Game je hlavní okno celé aplikace.
 * Vytváří JFrame, nastavuje jeho vlastnosti a přidává do něj GameFrame,
 * což je panel se skutečnou hrou (logika, animace, vykreslování).
 */
public class Game extends JFrame {

    /**
     * Konstruktor vytvoří herní okno a nastaví jeho základní parametry.
     */
    public Game() {
        // Titulek okna
        setTitle("Zombie Prototype");

        // Zavření okna ukončí celou aplikaci
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainFrame = new JPanel(new CardLayout());

        MenuFrame menuFrame = new MenuFrame(mainFrame, this);

        //Nastavení velikosti vnitřního okna
        mainFrame.setPreferredSize(new Dimension(800, 600));

        //Přidá GameFrame do Game
        setContentPane(mainFrame);

        //Pořeší velikosti uvnitř hry pro vytvoření rámečku
        pack();

        //Nelze pozměnit velikost okna
        setResizable(false);

        // Umístí okno doprostřed obrazovky
        setLocationRelativeTo(null);

        mainFrame.add(menuFrame, menuFrame.getName());
        CardLayout cl = (CardLayout) mainFrame.getLayout();
        cl.show(mainFrame, "menu");

        // Vytvoříme hlavní herní panel (obsahuje smyčku, vykreslování,…)
        GameFrame gameFrame = new GameFrame(mainFrame);
        mainFrame.add(gameFrame, "game");
        cl.show(mainFrame, gameFrame.getName());


    }

    /**
     * Hlavní metoda – start aplikace.
     * SwingUtilities.invokeLater zajistí, že GUI poběží v Java EDT vláknu.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Vytvoříme objekt Game a zobrazíme okno
            new Game().setVisible(true);
        });
    }
}
