package game.entity.player;

import game.entity.Entity;
import game.GameFrame;
import game.entity.bullet.Bullet;
import game.listener.Keyboard;
import game.spriteloader.SpriteLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    // Animace motoru (8 snímků)
    private static final Image[] WALK_FRAMES =
            SpriteLoader.getFrames("/player/Run/", 1, 15);
    private static final Image[] WALK_FIRE_FRAMES =
            SpriteLoader.getFrames("/player/Run_Shoot/", 30,15);

    private static final Image[] IDLE_FRAMES =
            SpriteLoader.getFrames("/player/Idle/", 90, 15);
    private static final Image[] IDLE_FIRE_FRAMES =
            SpriteLoader.getFrames("/player/Idle_Shoot/", 60,15);


    /** Proměnné animace motoru */

    // Aktuální snímek animace motoru
    private int walkFrame = 0;
    private int idleFrame = 0;

    // Časový akumulátor – sleduje, kdy se má přepnout snímek
    private double timer = 0;

    private boolean isWalking;
    private boolean isShooting;

    // Hra začíná na 6 nábojích
    private int magazineCapacity = 6;

    private int magazineBulletCount = 6;

    private ArrayList<Bullet> bullets = new ArrayList<>();

    private Component window;

    /**
     * Konstruktor nastaví pozici a velikost entity.
     *
     * @param x      počáteční X pozice
     * @param y      počáteční Y pozice
     * @param width  šířka
     * @param height výška
     * @param window komponenta obrazovky
     */
    public Player(int x, int y, int width, int height, Component window) {
        super(x, y, width, height, 200, 100, 0);
        this.window = window;
    }

    public boolean getWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public boolean getShooting() {
        return isShooting;
    }

    public void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    public int getMagazineCapacity() {
        return magazineCapacity;
    }

    public void setMagazineCapacity(int magazineCapacity) {
        this.magazineCapacity = magazineCapacity;
    }

    public int getMagazineBulletCount() {
        return magazineBulletCount;
    }

    public void setMagazineBulletCount(int magazineBulletCount) {
        this.magazineBulletCount = magazineBulletCount;
    }

    public Component getWindow() {
        return window;
    }

    public double getBarrelX() {
        double half = getWidth() / 2.0;
        double distance = 50;
        if (isOpposite()) {
            return getX() + half - distance;
        }
        return getX() + half + distance;
    }
    public double getBarrelY() {
        double distance = 56;
        return getY() + distance;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Bullet> bullets) {
        this.bullets = bullets;
    }

    /**
     * Vykreslí animaci motoru – používá engineFrame index.
     */
    @Override
    public void draw_animation(Graphics g){
        JProgressBar healthBar = this.getHealthBar();
        healthBar.setLocation((int) this.getX() + (this.getWidth() - healthBar.getWidth()) / 2, (int) this.getY() - 30);
        healthBar.setValue(this.getHp());
        ((JPanel) this.getWindow()).add(healthBar);

        double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        mouseX = mouseX - this.window.getLocationOnScreen().getX();
        boolean isOpposite = mouseX < getX() + getWidth() / 2.0;
        setOpposite(isOpposite);


        int oppositeX = 0;
        int oppositeWidth = 1;

        if (isOpposite) {
            oppositeX = getWidth();
            oppositeWidth = -1;
        }

        if (getWalking()) {
            if (getShooting()) {
                g.drawImage(WALK_FIRE_FRAMES[walkFrame], (int) getX() + oppositeX, (int) getY(), getWidth() * oppositeWidth, getHeight(), null);
            } else {
                g.drawImage(WALK_FRAMES[walkFrame], (int) getX() + oppositeX, (int) getY(), getWidth() * oppositeWidth, getHeight(), null);
            }
        } else {
            if (getShooting()) {
                g.drawImage(IDLE_FIRE_FRAMES[idleFrame], (int) getX() + oppositeX, (int) getY(), getWidth() * oppositeWidth, getHeight(), null);
            } else {
                g.drawImage(IDLE_FRAMES[idleFrame], (int) getX() + oppositeX, (int) getY(), getWidth() * oppositeWidth, getHeight(), null);
            }
        }
    }

    @Override
    public void update(){

        /** Pohyb lodi podle stisknutých kláves */

        Keyboard input = GameFrame.getInput();
        MouseListener mouseInput = window.getMouseListeners()[0];
        double dt = GameFrame.getDt();
        double speed = getSpeed();
        double x = getX();
        double y = getY();

        setWalking(false);


        boolean isHorizontal = false;
        boolean isVertical = false;

        if (input.isDown(KeyEvent.VK_W)) {
            y -= speed * dt;
            setWalking(true);
            isVertical = true;
        }
        if (input.isDown(KeyEvent.VK_S)) {
            y += speed * dt;
            setWalking(true);
            isVertical = true;
        }
        if (input.isDown(KeyEvent.VK_A)) {
            x -= speed * dt;
            setWalking(true);
            isHorizontal = true;
        }
        if (input.isDown(KeyEvent.VK_D)) {
            x += speed * dt;
            setWalking(true);
            isHorizontal = true;
        }

        if (isHorizontal && isVertical) {
            // 1.414 je delka prepony, timto normalizujeme diagonalni chod
            x = getX() + (x - getX()) / 1.414;
            y = getY() + (y - getY()) / 1.414;
        }
        setX(x);
        setY(y);


        int canvasWidth = 800;     // ← tady dosadíš šířku svého okna
        int canvasHeight = 600;    // ← tady dosadíš výšku okna

        // Oříznutí X
        if (getX() < 0) setX(0);
        if (getX() > canvasWidth - getWidth()) setX(canvasWidth - getWidth());

        // Oříznutí Y
        if (getY() < 0) setY(0);
        if (getY() > (canvasHeight - getHeight())) {
            setY(canvasHeight - getHeight());
        }


        /** Animace motoru */
        // Přičti čas od posledního snímku
        timer += dt;


        // Pokud uplynulo více než engineFrameSpeed → posuň animaci
        if (timer >= getFrameSpeed()) {
            timer = 0;      // restart timeru
            if (getWalking()) {
                idleFrame = 0;
                walkFrame++;// další snímek animace
            } else {
                walkFrame = 0;
                idleFrame++;
            }

            // Když skončíme poslední snímek, vrátíme se na 0 (loop)
            if (walkFrame >= WALK_FRAMES.length)
                walkFrame = 0;

            // Když skončíme poslední snímek, vrátíme se na 0 (loop)
            if (idleFrame >= IDLE_FRAMES.length)
                idleFrame = 0;
        }
    }
}
