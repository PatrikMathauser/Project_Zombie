package game.entity.bullet;

import game.GameFrame;
import game.entity.Entity;
import game.spriteloader.SpriteLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet extends Entity {

    /**
     * Konstruktor nastaví pozici a velikost entity.
     *
     * @param x      počáteční X pozice
     * @param y      počáteční Y pozice
     * @param width  šířka
     * @param height výška
     */

    private final double angleRadians;

    public Bullet(int x, int y, int width, int height, double angleRadians) {
        super(x, y, width, height, 400, 1, 10);
        this.angleRadians = angleRadians;
    }

    public double getAngle() {
        return angleRadians;
    }

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
        double dt = GameFrame.getDt();
        setX(getX() + Math.cos(getAngle()) * getSpeed() * dt);
        setY(getY() + Math.sin(getAngle()) * getSpeed() * dt);
    }
}
