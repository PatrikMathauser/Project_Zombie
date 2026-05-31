package ui;

import javax.swing.*;
import java.awt.*;

public class ShadowLabel extends JLabel {
    // Simulace stínu textu
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GRAY);
        g2.drawString(getText(), 2, getHeight() - 5);

        g2.setColor(getForeground());
        g2.drawString(getText(), 0, getHeight() - 7);
    }
}
