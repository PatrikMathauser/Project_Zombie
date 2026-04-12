package game.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard je třída, která sleduje stav kláves na klávesnici.
 * Umožňuje zjistit, zda je konkrétní klávesa právě stisknutá.
 *
 * Na rozdíl od běžného KeyListenera si pamatuje všechny stisknuté
 * klávesy zároveň (W + A → funguje diagonální pohyb).
 */
public class Keyboard implements KeyListener {

    /**
     * Pole o velikosti 256 pro uložení stavu kláves.
     *
     * index = KeyEvent.VK_*
     * hodnota:
     *     true  = klávesa je aktuálně stisknutá
     *     false = klávesa je uvolněná
     *
     * Používáme pole místo jednoho integeru, protože to umožňuje
     * podporovat více kláves najednou (např. W + D).
     */
    private final boolean[] keys = new boolean[256];

    /**
     * Metoda se automaticky volá, když uživatel stiskne klávesu.
     * Zaznamená klávesu jako "stisknutou".
     *
     * @param e informace o události
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;       // označíme klávesu jako aktivní
    }

    /**
     * Metoda se automaticky volá, když uživatel uvolní klávesu.
     * Zaznamená klávesu jako "neaktivní".
     *
     * @param e informace o události
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;      // klávesa již není stisknutá
    }

    /**
     * Tuto metodu nevyužíváme – slouží pro znaky (char), ne pro pohyb.
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Vrací informaci, zda je určitá klávesa aktuálně stisknutá.
     *
     * @param key KeyEvent.VK_*, např. VK_W, VK_D, VK_SPACE...
     * @return true pokud je klávesa stisknutá
     */
    public boolean isDown(int key) {
        return keys[key];
    }
}
