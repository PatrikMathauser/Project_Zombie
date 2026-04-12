package game.entity;

import javax.swing.*;
import java.awt.*;


/**
 * Entita reprezentuje herní objekt.
 * Obsahuje pozici, velikost a logiku pohybu.
 */
public class Entity {


    // Rychlost animace (0.0333s = 33.3 ms na snímek → 30 FPS animace)
    private final double frameSpeed = 0.0333;


    private int hp; // Health Points

    private int attackDamage;

    private int speed;
    private boolean isAttack = false;
    private boolean isLive = true;

    private boolean isOpposite = false;

    private Rectangle hitbox;

    private final JProgressBar healthBar = new JProgressBar();


    /**
     * Konstruktor nastaví pozici a velikost entity.
     *
     * @param x počáteční X pozice
     * @param y počáteční Y pozice
     * @param width šířka
     * @param height výška
     */
    public Entity(int x, int y, int width, int height, int speed, int hp, int attackDamage) {
        this.speed = speed;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.hitbox = new Rectangle(x, y, width, height);
        this.healthBar.setMaximum(hp);
        this.healthBar.setValue(hp);
        this.healthBar.setForeground(Color.GREEN);
        this.healthBar.setBackground(Color.RED);
        this.healthBar.setPreferredSize(new Dimension(100,20));
    }



    public void draw_static(Graphics g) {}

    public void draw_animation(Graphics g) {}

    public void update(){}

    public double getFrameSpeed() {
        return frameSpeed;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public double getX() {
        return hitbox.getX();
    }

    public double getY() {
        return hitbox.getY();
    }

    public int getWidth() {
        return hitbox.width;
    }

    public int getHeight() {
        return hitbox.height;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public void setX(double x) {
        this.hitbox.x = (int) x;
    }

    public void setY(double y) {
        this.hitbox.y = (int) y;
    }

    public void setWidth(int width) {
        this.hitbox.width = width;
    }

    public void setHeight(int height) {
        this.hitbox.height = height;
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public JProgressBar getHealthBar() {
        return healthBar;
    }

    /**
     * Udělí poškození o velikost našeho Attack Damage
     * @param entity která poškození obdrží
     */
    public void dealDamageTo(Entity entity) {
        entity.setHp(entity.getHp() - this.getAttackDamage());
        if (entity.getHp() < 0) {
            entity.setHp(0);
        }
    }
}



