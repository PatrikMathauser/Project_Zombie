package game;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JPanel {



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
}
