package game.manager;

import game.entity.bullet.Bullet;
import game.entity.enemy.Enemy;
import game.entity.player.Player;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static game.GameFrame.WINDOW_HEIGHT;
import static game.GameFrame.WINDOW_WIDTH;

public class CollisionManager {

    private final JPanel window;

    public CollisionManager(JPanel window) {
        this.window = window;
    }

    public void manage(Player player, List<Enemy> enemies) {
        HashSet<Bullet> bulletsToDestroy = new HashSet<>();
        HashSet<Enemy> enemiesToDestroy = new HashSet<>();

        for (Bullet bullet : player.getBullets()) {
            if (bullet.getX() > WINDOW_WIDTH || bullet.getY() > WINDOW_HEIGHT || bullet.getX() < 0 || bullet.getY() < 0) {
                bulletsToDestroy.add(bullet);
            }
            // Kolize
            for (Enemy enemy : enemies) {
                if (enemy.getHitbox().intersects(bullet.getHitbox())) {
                    bulletsToDestroy.add(bullet);
                    bullet.dealDamageTo(enemy);
                    if (enemy.getHp() == 0) {
                        enemiesToDestroy.add(enemy);
                        window.remove(enemy.getHealthBar());
                    }
                }
            }
        }

        player.setBullets(player.getBullets().stream().filter(bullet ->
                !bulletsToDestroy.contains(bullet)).collect(Collectors.toCollection(ArrayList::new)));

        List<Enemy> enemiesToLive = enemies.stream().filter(enemy -> !enemiesToDestroy.contains(enemy)).toList();
        enemies.clear();
        enemies.addAll(enemiesToLive);
    }
}
