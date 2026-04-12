package game.entity.enemy;

import game.entity.Entity;
import game.GameFrame;
import game.entity.player.Player;
import game.spriteloader.SpriteLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Enemy extends Entity {


    private static final Image[] ENEMY_WALK_FRAMES =
            SpriteLoader.getFrames("/enemy/Walk/", 1, 15);

    private static final Image[] ENEMY_ATTACK_FRAMES =
            SpriteLoader.getFrames("/enemy/Attack/", 460, 11);

    private final Player playerToFollow;

    private int enemyWalkFrame = 0;
    private int enemyAttackFrame = 0;

    private double timer = 0;


    public Enemy(int x, int y, int width, int height, int hp, Player player) {
        super(x, y, width, height, 100, hp, 20);
        this.playerToFollow = player;
    }

    public boolean isAttackDone() {
        double attackTimer = timer + GameFrame.getDt();
        if (attackTimer >= getFrameSpeed()) {
            return enemyAttackFrame == 10; // poslední snímek animace
        }
        return false;
    }


    @Override
    public void draw_animation(Graphics g){
        JProgressBar healthBar = this.getHealthBar();
        JPanel window = (JPanel) playerToFollow.getWindow();
        healthBar.setLocation((int) this.getX() + (this.getWidth() - healthBar.getWidth()) / 2, (int) this.getY() - 30);
        healthBar.setValue(this.getHp());
        if (healthBar.getParent() == null) {
            window.add(healthBar);
            window.revalidate();
            window.repaint();
        }
        int oppositeX = 0;
        int oppositeWidth = 1;

        boolean isOpposite = playerToFollow.getX() < getX();

        if (isOpposite) {
            oppositeX = getWidth();
            oppositeWidth = -1;
        }

        if (isAttack()) {
            g.drawImage(ENEMY_ATTACK_FRAMES[enemyAttackFrame], (int) getX() + oppositeX, (int) getY(), getWidth() * oppositeWidth, getHeight(), null);
        } else {
            g.drawImage(ENEMY_WALK_FRAMES[enemyWalkFrame], (int) getX() + oppositeX, (int) getY(), getWidth() * oppositeWidth, getHeight(), null);
        }


    }


    @Override
    public void update(){

        double dt = GameFrame.getDt();
        double x = getX();
        double y = getY();

        double distanceX = playerToFollow.getX() - x;
        double distanceY = playerToFollow.getY() - y;
        double angle = Math.atan2(distanceY, distanceX);
        setX(x + getSpeed() * Math.cos(angle) * dt);
        setY(y + getSpeed() * Math.sin(angle) * dt);


        timer += dt;
        if (timer >= getFrameSpeed()) {
            timer = 0;
            if (isAttack()) {
                enemyAttackFrame++;
                enemyWalkFrame = 0;
                // Když skončíme poslední snímek, vrátíme se na 0 (loop)
                if (enemyAttackFrame >= ENEMY_ATTACK_FRAMES.length)
                    enemyAttackFrame = 0;
            } else {
                enemyWalkFrame++;
                enemyAttackFrame = 0;
                // Když skončíme poslední snímek, vrátíme se na 0 (loop)
                if (enemyWalkFrame >= ENEMY_WALK_FRAMES.length)
                    enemyWalkFrame = 0;
            }

        }
    }
}
