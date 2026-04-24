package ui;

import game.spriteloader.SpriteLoader;

import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {
    public ImageButton(Image image, Dimension size){
        this.setIcon(new ImageIcon(image));
        setPreferredSize(size);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }


}
