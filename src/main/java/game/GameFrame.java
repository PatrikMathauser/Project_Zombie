package game;

import game.entity.bullet.Bullet;
import game.entity.enemy.Enemy;
import game.entity.player.Player;
import game.listener.Keyboard;
import game.manager.CollisionManager;
import game.manager.WaveManager;
import game.spriteloader.SpriteLoader;

import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GameFrame je hlavní herní panel.
 * Obsahuje:
 * - herní smyčku (Timer)
 * - delta-time výpočet
 * - vstup z klávesnice
 * - vykreslování herních objektů
 */
public class GameFrame extends JPanel {

    private static final int BULLET_COOLDOWN = 15;

    private static final int RELOAD_TIME = 80;

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    /** Poslední časový údaj pro výpočet delta-time */
    private long lastTime = System.nanoTime();

    private int bulletCooldown = 0;

    private int reloadTime = RELOAD_TIME;

    /** Keyboard objekt – zaznamenává stisknuté klávesy */
    private static Keyboard input = new Keyboard();

    private MouseAdapter mouseInput = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            player.setShooting(true);
            player.setAttack(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            player.setShooting(false);
            player.setAttack(false);
        }
    };

    private static double dt;

    private static Image wallpaper = SpriteLoader.load("/wallpaper/background.png");

    /** Herní entita (loď), kterou ovládáme */
    private Player player = new Player(64, 64, 128, 128, this);//TODO

    private WaveManager waveManager = new WaveManager(player, this);

    /**
     * Konstruktor GameFrame:
     * - aktivuje vstup z klávesnice
     * - spustí herní smyčku (Timer)
     */
    public GameFrame(JPanel mainFrame) {

        CollisionManager collisionManager = new CollisionManager(this);

        setLayout(new GridBagLayout());

        // Panel musí být fokusovatelný, aby mohl přijímat klávesy
        setFocusable(true);

        // Připojení našeho KeyListeneru pro záznam vstupů
        addKeyListener(input);

        // Připojení MouseAdapteru pro záznam kliknutí
        addMouseListener(mouseInput);

        /**
         * Herní smyčka – Timer se spustí každých cca 16 ms (≈ 60 FPS)
         * Timer volá anonymní funkci (lambda) e -> { ... }
         */
        new Timer(16, e -> {
            for (Component component : mainFrame.getComponents()) {
                if (component.isVisible() && Objects.equals(component.getName(), "menu")) {
                    return;
                }
            }

            // Aktuální čas
            long now = System.nanoTime();

            // Delta time = čas mezi dvěma snímky v sekundách
            dt = (now - lastTime) / 1_000_000_000.0;

            // Uložíme čas aktuálního snímku pro další výpočet
            lastTime = now;

            // Aktualizace logiky lodi (pohyb + animace)
            player.update();

            waveManager.manage();

            collisionManager.manage(player, waveManager.getEnemies());


            if (bulletCooldown < BULLET_COOLDOWN) {
                bulletCooldown++;
            }

            if (reloadTime < RELOAD_TIME) {
                reloadTime++;
            }

            double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
            mouseX = mouseX - this.getLocationOnScreen().getX();

            double mouseY = MouseInfo.getPointerInfo().getLocation().getY();
            mouseY = mouseY - this.getLocationOnScreen().getY();

            if (player.getShooting() && getBulletCooldown() == BULLET_COOLDOWN && getReloadTime() == RELOAD_TIME) {
                if (player.getMagazineBulletCount() == 0) {
                    setReloadTime(0);
                    player.setMagazineBulletCount(player.getMagazineCapacity());
                } else {
                    player.getBullets().add(new Bullet((int) player.getBarrelX(), (int) player.getBarrelY(), 16, 16, Math.atan2(mouseY - player.getY(), mouseX - player.getX())));
                    setBulletCooldown(0);
                    player.setMagazineBulletCount(player.getMagazineBulletCount() - 1);
                }
            }


            for (Bullet bullet : player.getBullets()) {
                bullet.update();
            }
            if (player.getHp() == 0) {
                System.exit(0);
            }

            // Pře-kreslení herního panelu
            repaint();

        }).start();
    }

    /**
     * paintComponent je metoda Swingu, která vykresluje grafiku.
     * Každý snímek se zavolá po repaint().
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(wallpaper, 0, 0, 800, 600, this);

        // Vykreslí statické tělo lodě
        player.draw_static(g);

        // Vykreslí animaci motoru
        player.draw_animation(g);

        for (Bullet bullet : player.getBullets()) {
            bullet.draw_animation(g);
        }

        waveManager.paint(g);

    }

    public static Keyboard getInput() {
        return input;
    }

    public MouseAdapter getMouseInput() {
        return mouseInput;
    }

    public static double getDt() {
        return dt;
    }

    public int getBulletCooldown() {
        return bulletCooldown;
    }

    public void setBulletCooldown(int bulletCooldown) {
        this.bulletCooldown = bulletCooldown;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }
}