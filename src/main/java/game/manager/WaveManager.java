package game.manager;

import game.entity.enemy.Enemy;
import game.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game.GameFrame.WINDOW_HEIGHT;
import static game.GameFrame.WINDOW_WIDTH;

public class WaveManager {

    private static final int WAVE_BOUND_LENGTH = 200;

    private static final int ENEMY_COOLDOWN = 72;

    private static final int WAVE_COOLDOWN = 300;

    private int currentWave = 1;

    private int enemyCounter = 0;

    private Player player;

    private JPanel window;

    private List<Enemy> enemies = new ArrayList<>();

    private int enemyCooldown = 0;

    private int waveCooldown = 0;

    public WaveManager(Player player, JPanel window) {
        this.player = player;
        this.window = window;
    }

    public int getEnemyCooldown() {
        return enemyCooldown;
    }

    public void setEnemyCooldown(int enemyCooldown) {
        this.enemyCooldown = enemyCooldown;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void manage() {
        if (enemyCooldown < ENEMY_COOLDOWN) {
            enemyCooldown++;
        }

        if (enemyCounter != 3 + currentWave * 2) {
            if (getEnemyCooldown() >= ENEMY_COOLDOWN - currentWave * 4) {
                Random rd = new Random();
                // 0 = up, 1 = right, 2 = down, 3 = left
                int direction = rd.nextInt(0, 4);
                int enemyX;
                int enemyY;
                if (direction % 2 == 0) {
                    enemyX = rd.nextInt(0, WINDOW_WIDTH);
                    enemyY = direction == 0 ? -1 * WAVE_BOUND_LENGTH : WINDOW_HEIGHT + WAVE_BOUND_LENGTH;
                } else {
                    enemyX = direction == 3 ? -1 * WAVE_BOUND_LENGTH : WINDOW_WIDTH + WAVE_BOUND_LENGTH;
                    enemyY = rd.nextInt(0, WINDOW_HEIGHT);
                }
                enemies.add(new Enemy(enemyX, enemyY, 128, 128, 20, player));
                setEnemyCooldown(0);
                enemyCounter++;
            }
        } else {
            if (enemies.isEmpty()) {
                if (waveCooldown == 0) {
                    makeAnnouncement("WAVE " + currentWave + " HAS ENDED");
                }
                waveCooldown++;
            }
            if (waveCooldown == WAVE_COOLDOWN) {
                currentWave++;
                enemyCounter = 0;
                waveCooldown = 0;
                //TODO pruhledne pozadi + centering
                makeAnnouncement("WAVE " + currentWave + " HAS STARTED");
            }
        }

        for (Enemy enemy : enemies) {
            if (player.getHitbox().intersects(enemy.getHitbox())) {
                enemy.setAttack(true);
                if (enemy.isAttackDone()) {
                    enemy.dealDamageTo(player);
                }
            } else {
                enemy.setAttack(false);
            }
            enemy.update();
        }
    }

    public void paint(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.draw_animation(g);
        }
    }

    public void makeAnnouncement(String announcementText) {
        JLabel label = new JLabel(announcementText);
        label.setFont(new Font("Arial", Font.BOLD, 64));
        label.setOpaque(false);
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.75;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        window.add(label, gbc);
        new Timer(5000, e -> window.remove(label)).start();
        window.revalidate();
        window.repaint();
    }


}
