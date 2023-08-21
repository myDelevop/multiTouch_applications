package wwf.thermometer.proportions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import wwf.thermometer.util.FilesPath;

/**
 * Classe che gestisce le proporzioni dell'immagine "glass.png".
 */
public final class GlassProportions {

    /**
     * Costruttore di default.
     * */
    private GlassProportions() { }
    
    /**
     * Restituisce la X in proporzione alle dimensioni originali.
     *
     * @param value the value
     * @return the scaled X
     */
    public static float getScaledGlassX(final float value) {
        float width = 0;
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new File(FilesPath.IMAGES_PATH + "glass.png"));
            width          = bimg.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        

        return value * width / 210f;
    }

    /**
     * Restituisce la Y in proporzione alle dimensioni originali.
     *
     * @param value the value
     * @return the scaled Y
     */
    public static float getScaledGlassY(final float value) {
        float height = 0;
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new File(FilesPath.IMAGES_PATH + "glass.png"));
            height          = bimg.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        return value * height / 210f;
    }
}
