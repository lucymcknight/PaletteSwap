import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Lucy on 1/21/2016.
 */
public class PaletteSwap{

    public static void main(String[] args)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File("images/Spheres.png"));
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        for(int x=0; x<image.getWidth(); x++)
        {
            for(int y=0; y<image.getHeight(); y++)
            {
                image.setRGB(x,y,Color.WHITE.getRGB());
            }
        }

        ImagePanel imagePanel = new ImagePanel(image);
        imagePanel.displayPanel();


        Color color = new Color(image.getRGB(0,0));
        System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
    }
}
