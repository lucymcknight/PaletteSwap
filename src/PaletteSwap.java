import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * Created by Lucy on 1/21/2016.
 */
public class PaletteSwap{

    private static BufferedImage AMERICAN_GOTHIC = null;
    private static BufferedImage MONA_LISA = null;
    private static BufferedImage RIVER = null;
    private static BufferedImage SPHERES = null;
    private static BufferedImage STARRY_NIGHT = null;
    private static BufferedImage THE_SCREAM = null;

    static
    {
        try
        {
            AMERICAN_GOTHIC = ImageIO.read(new File("images/AmericanGothic.png"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            MONA_LISA = ImageIO.read(new File("images/MonaLisa.png"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            RIVER = ImageIO.read(new File("images/River.png"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            SPHERES = ImageIO.read(new File("images/Spheres.png"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            STARRY_NIGHT = ImageIO.read(new File("images/StarryNight.png"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            THE_SCREAM = ImageIO.read(new File("images/TheScream.png"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        BufferedImage source = STARRY_NIGHT;
        BufferedImage palette = THE_SCREAM;

        BufferedImage image = arrangePalettePixelsInDimensionsOfSource(source, palette);
        ImagePanel imagePanel = new ImagePanel(image);
        imagePanel.displayPanel();

        for(int heat=99; heat>=0; heat--)
        {
            for(int x1=0; x1<image.getWidth(); x1++)
            {
                for(int y1=0; y1<image.getHeight(); y1++)
                {
                    int x2 = (int)(Math.random()*image.getWidth());
                    int y2 = (int)(Math.random()*image.getHeight());

                    int improvement = estimateImprovementAfterSwappingPixels(image, x1, y1, x2, y2, source, 0);

                    if(improvement >= 0)
                    {
                        swapPixels(image, x1, y1, x2, y2);
                    }
                    else
                    {
                        int swapProb = heat/(-1*improvement);
                        if(Math.random()*100 > (100-swapProb))
                        {
                            swapPixels(image, x1, y1, x2, y2);
                        }
                    }
                }
            }
            System.out.println("Simulated annealing: "+ (100-heat) + "% complete.");
        }
    }

    private static int estimateImprovementAfterSwappingPixels(BufferedImage image, int x1, int y1, int x2, int y2, BufferedImage source, int ditheringAmount)
    {
        double originalColorDiff = colorDistance(avgColorAround(image,x1,y1,ditheringAmount), avgColorAround(source,x1,y1,ditheringAmount))
                                 + colorDistance(avgColorAround(image,x2,y2,ditheringAmount), avgColorAround(source,x2,y2,ditheringAmount));

        double newColorDiff = colorDistance(avgColorAround(image,x1,y1,ditheringAmount), avgColorAround(source,x2,y2,ditheringAmount))
                            + colorDistance(avgColorAround(image,x2,y2,ditheringAmount), avgColorAround(source,x1,y1,ditheringAmount));

        return (int)(originalColorDiff-newColorDiff);
    }

    private static Color avgColorAround(BufferedImage img, int x, int y, int range)
    {
        int totalRed = 0;
        int totalGrn = 0;
        int totalBlu = 0;
        int pixCount = 0;

        for(int i=x-range; i<=x+range; i++)
        {
            for(int j=y-range; j<=y+range; j++)
            {
                if(i >= 0 && j >= 0 && i<img.getWidth() && j<img.getHeight())
                {
                    Color color = new Color(img.getRGB(i,j));
                    totalRed += color.getRed();
                    totalGrn += color.getGreen();
                    totalBlu += color.getBlue();
                    pixCount++;
                }
            }
        }

        return new Color(totalRed/pixCount, totalGrn/pixCount, totalBlu/pixCount);
    }

    private static void swapPixels(BufferedImage img, int x1, int y1, int x2, int y2)
    {
        int tempColor = img.getRGB(x1,y1);
        img.setRGB(x1,y1,img.getRGB(x2,y2));
        img.setRGB(x2,y2,tempColor);
    }

    private static BufferedImage arrangePalettePixelsInDimensionsOfSource(BufferedImage source, BufferedImage palette)
    {
        BufferedImage newImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);

        if(source.getHeight()*source.getWidth() == palette.getHeight()*palette.getWidth()) {
            int[] paletteArray = palette.getRGB(0, 0, palette.getWidth(), palette.getHeight(), null, 0, palette.getWidth());

            int curIndex = 0;
            for (int y = 0; y < newImage.getHeight(); y++) {
                for (int x = 0; x < newImage.getWidth(); x++) {
                    newImage.setRGB(x, y, paletteArray[curIndex]);
                    curIndex++;
                }
            }
        }
        else
        {
            System.out.println("Error in arrangePalettePixelsInDimensionsOfSource: the two images are not of the same size");
        }

        return newImage;
    }

    private static double colorDistance(int color1, int color2)
    {
        Color c1 = new Color(color1);
        Color c2 = new Color(color2);

        return colorDistance(c1, c2);
    }

    private static double colorDistance(Color c1, Color c2)
    {
        long rmean = ( (long)c1.getRed() + (long)c2.getRed() ) / 2;
        long r = (long)c1.getRed() - (long)c2.getRed();
        long g = (long)c1.getGreen() - (long)c2.getGreen();
        long b = (long)c1.getBlue() - (long)c2.getBlue();
        return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
    }
}
