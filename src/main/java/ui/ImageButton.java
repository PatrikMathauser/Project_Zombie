package ui;

import game.spriteloader.SpriteLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Tlačítko přizpůsobené pro zobrazení obrázku
 */
public class ImageButton extends JButton {
    /**
     * Konstruktor obrázkového tlačítka
     * @param image obrázek co chceme zobrazit v rámci tlačítka
     * @param size velikost tlačítka
     */
    public ImageButton(Image image, Dimension size){
        this.setIcon(new ImageIcon(image));
        setPreferredSize(size);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }


}
