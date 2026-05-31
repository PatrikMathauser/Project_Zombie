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

/**
 * Má na starosti herní vlny
 */
public class WaveManager {
    // Délka ohraničení okolo herní plochy kde se spawnují enemy
    private static final int WAVE_BOUND_LENGTH = 200;
    // Čas mezi spawnutím enemáků
    private static final int ENEMY_COOLDOWN = 72;
    // Čas mezi hernímy vlnami když jedna skončí a druhá začne
    private static final int WAVE_COOLDOWN = 300;

    private int currentWave = 1;
    // Počet enemy
    private int enemyCounter = 0;

    private Player player;

    private JPanel window;

    private List<Enemy> enemies = new ArrayList<>();
    // Počítadlo času pro spawnutí enemy
    private int enemyCooldown = 0;
    // Počítadlo času pro započatí nové wave
    private int waveCooldown = 0;

    /**
     * Kostruktor vlnového manažera
     * @param player hráč
     * @param window herní okno
     */
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

    /**
     * Volá se v herní smyčce a pracuje s vlnami
     */
    public void manage() {
        // Navýší počítadlo pro spawn enemy pokud ještě nenastal čas
        if (enemyCooldown < ENEMY_COOLDOWN) {
            enemyCooldown++;
        }
        // Dokaď se nespawnul počet enemy v dané vlně (1-5, 2-7, 3-9,...)
        if (enemyCounter != 3 + currentWave * 2) {
            // Pokud nastal čas pro spawnutí enemy, který se s novými vlnami zkracuje
            if (getEnemyCooldown() >= ENEMY_COOLDOWN - currentWave * 4) {
                Random rd = new Random();
                // 0 = up, 1 = right, 2 = down, 3 = left
                int direction = rd.nextInt(0, 4);
                int enemyX;
                int enemyY;
                // Pokud je direction up nebo down (% je zbytek po dělení)
                if (direction % 2 == 0) {
                    // Náhodně vybere souřadnici X v rozmezí 0, šířka okna
                    enemyX = rd.nextInt(0, WINDOW_WIDTH);
                    // Pokud je směr nahoru, spawnume nad horní okraj obrazovky jinak pod spodní
                    enemyY = direction == 0 ? -1 * WAVE_BOUND_LENGTH : WINDOW_HEIGHT + WAVE_BOUND_LENGTH;
                } else {
                    // Pokud je směr doleva, spawnume ho zleva levého okraje obrazovky jinak zprava pravého
                    enemyX = direction == 3 ? -1 * WAVE_BOUND_LENGTH : WINDOW_WIDTH + WAVE_BOUND_LENGTH;
                    enemyY = rd.nextInt(0, WINDOW_HEIGHT);
                }
                enemies.add(new Enemy(enemyX, enemyY, 128, 128, 20, player));
                setEnemyCooldown(0);
                enemyCounter++;
            }
        } else {
            // Pokud vyzabíjíme veškeré enemy, vyhlásíme konec kola
            if (enemies.isEmpty()) {
                if (waveCooldown == 0) {
                    makeAnnouncement("WAVE " + currentWave + " HAS ENDED");
                }
                waveCooldown++;
            }
            // Pokud nastal čas nového kola vlny, započneme a vyhlásíme nové kolo
            if (waveCooldown == WAVE_COOLDOWN) {
                currentWave++;
                enemyCounter = 0;
                waveCooldown = 0;
                makeAnnouncement("WAVE " + currentWave + " HAS STARTED");
            }
        }

        // Kolize mezi enemy a player
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

    /**
     * Vytvoří ohlášení
     * @param announcementText obsah hlášení
     */
    public void makeAnnouncement(String announcementText) {
        for (Component component : window.getComponents()) {
            if ("Announcement".equals(component.getName())) {
                JLabel label = (JLabel) component;
                label.setText(announcementText);
                // Délka zobrazení textu, poté vynulujeme
                Timer timer = new Timer(4000, e -> {
                    label.setText("");
                });
                timer.setRepeats(false);
                timer.start();


            }
        }

    }


}
