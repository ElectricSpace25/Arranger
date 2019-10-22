import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RoomDrawer {
    static BufferedImage blank;
    static BufferedImage wall;
    static BufferedImage sink;
    static BufferedImage toilet;
    static BufferedImage bath;

    public static void setup() throws IOException{
        blank = ImageIO.read(new File("img/blank.png"));
        wall = ImageIO.read(new File("img/wall.png"));
        sink = ImageIO.read(new File("img/sink.png"));
        toilet = ImageIO.read(new File("img/toilet.png"));
        bath = ImageIO.read(new File("img/bath.png"));
    }


    public static void draw(String[][] roomGrid, int width, int height, ArrayList<Integer> directions, ArrayList<Integer> coords) throws IOException {
        setup();

        System.out.println("Drawing...");
        int count = 0;
        int angle = 0;

        BufferedImage tile;

        BufferedImage image = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_INT_ARGB); // Creates blank BufferedImage
        Graphics2D graphics2D = image.createGraphics(); //Allows you to draw


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                tile = setTile(roomGrid[y][x]);
                graphics2D.drawImage(tile, x * 32, y * 32, null);
            }
        }

        //Rotate tile

        //0 = left, 1 = right, 2 = down, 3 = up (directions of path)
        for (int i = 0; i < coords.size()-1; i += 2) {
            int x = coords.get(i);
            int y = coords.get(i+1);
            if (directions.get(count) == 0) angle = 270;
            if (directions.get(count) == 1) angle = 90;
            if (directions.get(count) == 2) angle = 0;
            if (directions.get(count) == 3) angle = 180;
            count++;
            tile = setTile(roomGrid[y][x]);
            System.out.println(angle);
            tile = rotateImageByDegrees(tile, angle);
            graphics2D.drawImage(tile, x * 32, y * 32, null);

        }

        ImageIO.write(image, "png", new File("lol.png")); //Saves new BufferedImage
        System.out.println("Done!");

    }

    public static BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {

        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w / 2, h / 2);
        graphic.drawImage(img, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    public static BufferedImage setTile(String tile) {
        if (tile.equals("X")) return wall;
        else if (tile.equals("-")) return blank;
        else if (tile.equals("S")) return sink;
        else if (tile.equals("B")) return bath;
        else if (tile.equals("T")) return toilet;
        else return blank;
    }

}
