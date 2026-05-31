package game.entity.bullet;

import game.GameFrame;
import game.entity.Entity;
import game.spriteloader.SpriteLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet extends Entity {

    private final double angleRadians;

    /**
     * Konstruktor náboje
     * @param x - souřadnice X
     * @param y - souřadnice Y
     * @param width - šířka
     * @param height - výška
     * @param angleRadians - úhel v radiánech o který je náboj otočený
     */
    public Bullet(int x, int y, int width, int height, double angleRadians) {
        super(x, y, width, height, 400, 1, 10);
        this.angleRadians = angleRadians;
    }

    public double getAngle() {
        return angleRadians;
    }

    // Vykreslí animaci náboje
    @Override
    public void draw_animation(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(getAngle(), getX() + getWidth() / 2.0, getY() + getHeight() / 2.0);
        g.drawImage(SpriteLoader.load("/bullet/bullet.gif"), (int) getX(), (int) getY(), getWidth(), getHeight(),null);
        g2d.setTransform(old);
    }

    @Override
    public void update() {
        // Výpočet souřadnic podle úhlu a goniometrických funkcí
        double dt = GameFrame.getDt();
        setX(getX() + Math.cos(getAngle()) * getSpeed() * dt);
        setY(getY() + Math.sin(getAngle()) * getSpeed() * dt);
    }
}
