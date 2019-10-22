import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

public class ImageTest {
    public static void main(String[] args) throws IOException {
        System.out.println("test lol");
        BufferedImage blank = ImageIO.read(new File("img/blank.png"));
        BufferedImage sink = ImageIO.read(new File("img/sink.png"));
        BufferedImage wall = ImageIO.read(new File("img/wall.png"));


        BufferedImage image = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB); // Creates blank BufferedImage
        Graphics2D graphics2D = image.createGraphics(); //Allows you to draw

        graphics2D.setColor(Color.RED); // Text color

        graphics2D.drawImage(blank, 0*32, 0*32, null); //Draw tiles
        graphics2D.drawImage(blank, 1*32, 0*32, null);
        graphics2D.drawImage(blank, 2*32, 0*32, null);
        graphics2D.drawImage(wall, 3*32, 0*32, null);
        graphics2D.drawImage(wall, 0*32, 1*32, null);
        graphics2D.drawImage(blank, 0*32, 2*32, null);
        graphics2D.drawImage(sink, 7*32, 0*32, null);
        graphics2D.drawString("hello", 128, 128); //Draw text

        ImageIO.write(image, "png", new File("lol.png")); //Saves new BufferedImage
        System.out.println("done");
    }
}
