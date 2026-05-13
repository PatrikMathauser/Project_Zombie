package game;

import game.entity.bullet.Bullet;
import game.entity.player.Player;
import game.listener.Keyboard;
import game.manager.CollisionManager;
import game.manager.WaveManager;
import game.spriteloader.SpriteLoader;
import ui.ShadowLabel;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * GameFrame je hlavní herní panel.
 * Obsahuje:
 * - herní smyčku (Timer)
 * - delta-time výpočet
 * - výstup z klávesnice a myše
 * - vykreslování herních objektů
 * - rešení kolizí a objektů
 *
 */
public class GameFrame extends JPanel {

    /** Nastavení cooldownu mezi střelami  */
    private static final int BULLET_COOLDOWN = 15;

    /** Nastavení cooldownu při přebíjení  */
    private static final int RELOAD_TIME = 80;

    /** Nastavení dimenzí okna  */
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    /** Poslední časový údaj pro výpočet delta-time */
    private long lastTime = System.nanoTime();

    /** Počítadlo pro BULLET_COOLDOWN */
    private int bulletCooldown = 0;

    /** Počítadlo pro REALOAD_TIME */
    private int reloadTime = RELOAD_TIME;

    /** Keyboard objekt – zaznamenává stisknuté klávesy */
    private static Keyboard input = new Keyboard();

    /** Mouse objekt - zaznamenává stisknuté klávesy + polohu  */
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

    /** Herní entita, kterou ovládáme */
    private Player player = new Player(64, 64, 128, 128, this);

    /** Objekt sloužící k generování vln enemy */
    private WaveManager waveManager = new WaveManager(player, this);

    /**
     * Konstruktor GameFrame:
     * - aktivuje vstup z klávesnice a myše
     * - spustí herní smyčku (Timer)
     */
    public GameFrame(JPanel mainFrame) {

        //
        setUp(mainFrame);

        // Má na starosti kolize všech entit
        CollisionManager collisionManager = new CollisionManager(this);

        // Nastaví absolutní pozici objektu v layout
        setLayout(null);

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
            // Pokud jsme v menu, nechceme jít dál v herní smyčce
            for (Component component : mainFrame.getComponents()) {
                if (component.isVisible() && Objects.equals(component.getName(), "menu")) {
                    return;
                }
            }
            // Přepneme obrazovku z menu na hru pokud již není
            if (!this.hasFocus()){
                this.requestFocusInWindow();
            }


            // Aktuální čas
            long now = System.nanoTime();

            // Delta time = čas mezi dvěma snímky v sekundách
            dt = (now - lastTime) / 1_000_000_000.0;

            // Uložíme čas aktuálního snímku pro další výpočet
            lastTime = now;

            // Aktualizace logiky lodi (pohyb + animace)
            player.update();

            // Spustíme aktualizaci vlny
            waveManager.manage();

            // Spustíme aktualizaci kolizí
            collisionManager.manage(player, waveManager.getEnemies());

            //Navýšíme cooldown bulletu
            if (bulletCooldown < BULLET_COOLDOWN) {
                bulletCooldown++;
            }

            // Navýšíme čas k plnému přebití
            if (reloadTime < RELOAD_TIME) {
                reloadTime++;
            }

            // Získání X souřadnice myši
            double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
            mouseX = mouseX - this.getLocationOnScreen().getX();

            // Získání Y souřadnice myši
            double mouseY = MouseInfo.getPointerInfo().getLocation().getY();
            mouseY = mouseY - this.getLocationOnScreen().getY();

            // Pokud hráč střílí a nepřebíjí a má další náboj připraven
            if (player.getShooting() && getBulletCooldown() == BULLET_COOLDOWN && getReloadTime() == RELOAD_TIME) {
                // Přebijeme pokud nemáme náboje
                if (player.getMagazineBulletCount() == 0) {
                    setReloadTime(0);
                    player.setMagazineBulletCount(player.getMagazineCapacity());
                // Jinak vystřelíme, nastavíme cooldown mezi střelami a snižíme zásobník
                } else {
                    player.getBullets().add(new Bullet((int) player.getBarrelX(), (int) player.getBarrelY(), 16, 16, Math.atan2(mouseY - player.getY(), mouseX - player.getX())));
                    setBulletCooldown(0);
                    player.setMagazineBulletCount(player.getMagazineBulletCount() - 1);
                }
            }

            // Aktualizujeme náboje
            for (Bullet bullet : player.getBullets()) {
                bullet.update();
            }
            // Vypneme hru když umřeme
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

        // Vykreslení pozadí
        g.drawImage(wallpaper, 0, 0, 800, 600  , this);

        // Vykreslení animace hráče
        player.draw_animation(g);

        // Vykreslení nábojů
        for (Bullet bullet : player.getBullets()) {
            bullet.draw_animation(g);
        }

        // Vykreslení textu u vlny a enemáků
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

    /**
     * Vytvoří a nastaví Annoucement text
     * @param frame kde chceme zobrazit ten label
     */
    private void setUp(JPanel frame) {
        JLabel label = new ShadowLabel();
        label.setText("");
        label.setFont(new Font("Arial", Font.BOLD, 48));
        label.setOpaque(false);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setName("Announcement");
        label.setBounds((frame.getWidth() - 500) / 2, 0, 600, 100);
        this.add(label);
    }
}
