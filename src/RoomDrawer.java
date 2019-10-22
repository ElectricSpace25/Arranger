import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RoomDrawer {


    public static void draw(String[][] roomGrid, ArrayList<obj> objects, int width, int height) throws IOException {

        System.out.println("Drawing...");
        int angle = 0;
        int direction = 0;

        BufferedImage tile;

        BufferedImage image = new BufferedImage(width * 32, height * 32, BufferedImage.TYPE_INT_ARGB); // Creates blank BufferedImage
        Graphics2D graphics2D = image.createGraphics(); //Allows you to draw


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (roomGrid[y][x] == "X") {
                    graphics2D.drawImage(ImageIO.read(new File("img/wall.png")), x * 32, y * 32, null);
                }
                if (roomGrid[y][x] == "-") {
                    graphics2D.drawImage(ImageIO.read(new File("img/blank.png")), x * 32, y * 32, null);
                }
            }
        }


        //0 = left, 1 = right, 2 = down, 3 = up (directions of path)
        for (obj o : objects) {

            if (o.size == 2) {
//                System.out.println("0 = left, 1 = right, 2 = down, 3 = up");
//                System.out.println("direction: " + o.direction);
//                System.out.println("direction2: " + o.direction2);
//                System.out.println("initial direction: " + o.initialDirection);

                if (o.initialDirection == o.direction) {
                    direction = o.direction;
                } else if (o.initialDirection == o.direction2) {
                    direction = o.direction2;
                } else {
                    if (o.initialDirection == 0) direction = 1;
                    if (o.initialDirection == 1) direction = 0;
                    if (o.initialDirection == 2) direction = 3;
                    if (o.initialDirection == 3) direction = 2;

                }
            } else {
                direction = o.direction;
            }

            if (direction == 0) angle = 90; //Right
            if (direction == 1) angle = 270; //Left
            if (direction == 2) angle = 0; //Up
            if (direction == 3) angle = 180; //Down

            tile = rotateImageByDegrees(o.tile, angle);
            graphics2D.drawImage(tile, o.x * 32, o.y * 32, null);

            if (o.size == 2) {
                graphics2D.drawImage(tile, o.x2 * 32, o.y2 * 32, null);
            }

        }

        ImageIO.write(image, "png", new File("house.png")); //Saves new BufferedImage
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

}
