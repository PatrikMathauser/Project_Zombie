package ui;

import javax.swing.*;
import java.awt.*;

public class ImageComponent extends JComponent {
    private Image image;

    private int width;

    private int height;

    public ImageComponent(Image image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;
        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0,0, width, height, this);
    }
}
