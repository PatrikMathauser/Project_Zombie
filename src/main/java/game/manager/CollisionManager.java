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

/**
 * Zprostředkovává nám detekce kolizí
 */
public class CollisionManager {
    // Reprezentuje herní okno
    private final JPanel window;

    /**
     * Konstruktor manažera kolizí
     * @param window herní okno
     */
    public CollisionManager(JPanel window) {
        this.window = window;
    }

    /**
     * Volá se v herní smyčce a pracuje s kolizemi
     * @param player hráč
     * @param enemies aktuální nepřátelé
     */
    public void manage(Player player, List<Enemy> enemies) {
        HashSet<Bullet> bulletsToDestroy = new HashSet<>();
        HashSet<Enemy> enemiesToDestroy = new HashSet<>();

        for (Bullet bullet : player.getBullets()) {
            // Pokud náboj opustí herní plochu, přidáme ho do množiny pro odstranění
            if (bullet.getX() > WINDOW_WIDTH || bullet.getY() > WINDOW_HEIGHT || bullet.getX() < 0 || bullet.getY() < 0) {
                bulletsToDestroy.add(bullet);
            }
            // Kolize mezi enemy a bullet
            for (Enemy enemy : enemies) {
                // Pokud se protínají
                if (enemy.getHitbox().intersects(bullet.getHitbox())) {
                    bulletsToDestroy.add(bullet);
                    bullet.dealDamageTo(enemy);
                    // Při úmrtí enemy, odstranit ho + jeho healthBar
                    if (enemy.getHp() == 0) {
                        enemiesToDestroy.add(enemy);
                        window.remove(enemy.getHealthBar());
                    }
                }
            }
        }

        // Filtrování nábojů podle toho zda-li mají býti odstraněny
        player.setBullets(player.getBullets().stream().filter(bullet ->
                !bulletsToDestroy.contains(bullet)).collect(Collectors.toCollection(ArrayList::new)));

        // Filtrování enemy podle toho zda-li mají býti odstraněny
        List<Enemy> enemiesToLive = enemies.stream().filter(enemy -> !enemiesToDestroy.contains(enemy)).toList();
        // Ponechávní živých enemáků
        enemies.clear();
        enemies.addAll(enemiesToLive);
    }
}
