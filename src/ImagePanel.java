/**
 * Created by Lucy on 1/21/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Lucy on 1/21/2016.
 */
public class ImagePanel extends JPanel{

    BufferedImage image;

    public ImagePanel (BufferedImage i)
    {
        super();
        image = i;
    }

    public void displayPanel()
    {
        JFrame f = new JFrame("Image");
        f.add(this);
        f.setSize(image.getWidth(), image.getHeight() + 30);
        f.setVisible(true);
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
        repaint();
    }
}
