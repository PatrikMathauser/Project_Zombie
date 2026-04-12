package game.spriteloader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * SpriteLoader je utilitní třída pro načítání obrázků a práci se sprite sheety.
 * Obsahuje statické metody, takže se nikde neinstancuje.
 */
public class SpriteLoader {

    /**
     * Načte celý obrázek z resources a vrátí ho jako Image.
     *
     * @param path Cesta k obrázku v resources (např. "/ship/ship.png")
     * @return Načtený obrázek jako Image
     */
    public static Image load(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(SpriteLoader.class.getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException("Nelze načíst obrázek: " + path, e);
        }
    }

    /**
     * Vyřízne a vrátí konkrétní snímek ze sprite sheetu (řady obrázků vedle sebe).
     *
     * @param path        Cesta ke sprite sheetu v resources
     * @param frameWidth  Šířka jednoho snímku (px)
     * @param frameHeight Výška jednoho snímku (px)
     * @param frameCount  Počet snímků v jednom řádku
     * @param index       Index snímku (0 = první)
     * @return Obrázek (Image) obsahující vyříznutý snímek
     */
    public static Image getFrame(String path, int frameWidth, int frameHeight, int frameCount, int index) {
        try {
            BufferedImage sheet = ImageIO.read(
                    Objects.requireNonNull(SpriteLoader.class.getResource(path))
            );

            if (index < 0 || index >= frameCount)
                throw new IllegalArgumentException("Index snímku mimo rozsah: " + index);

            int x = index * frameWidth;
            int y = 0;

            return sheet.getSubimage(x, y, frameWidth, frameHeight);

        } catch (IOException e) {
            throw new RuntimeException("Nelze načíst spritesheet: " + path, e);
        }
    }

    /**
     * Načte celý sprite sheet a vrátí pole všech snímků v jednom řádku.
     *
     * @param path        Cesta ke sprite sheetu
     * @param startingInt Číslo prvního scnímku
     * @param frameCount  Počet snímků v jednom řádku
     * @return Pole Image[] obsahující jednotlivé snímky
     */
    public static Image[] getFrames(String path, int startingInt, int frameCount) {
        try {
            Image[] frames = new Image[frameCount];
            for (int i = 0; i < frameCount; i++) {
                BufferedImage image = ImageIO.read(
                            Objects.requireNonNull(SpriteLoader.class.getResource(path + String.format("%04d", startingInt + i) + ".png"))
                );
                frames[i] = image;
            }

            return frames;

        } catch (IOException e) {
            throw new RuntimeException("Nelze načíst spritesheet: " + path, e);
        }
    }
}
