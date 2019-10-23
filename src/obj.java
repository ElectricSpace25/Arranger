import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class obj {
    int size;
    int x;
    int y;
    int x2;
    int y2;
    int initialDirection;
    int direction = 0;
    int direction2;
    BufferedImage tile;
    BufferedImage tile2;


    public void singleObj(String tile, int size, int x, int y) throws IOException {
        this.size = size;
        this.x = x;
        this.y = y;
        this.tile = ImageIO.read(new File("img/" + tile + ".png"));
    }

    public void doubleObj(String tile, int size, int x, int y, int x2, int y2) throws IOException {
        this.size = size;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.tile = ImageIO.read(new File("img/" + tile + ".png"));
        this.tile2 = ImageIO.read(new File("img/" + tile + 2 + ".png"));
    }

}
