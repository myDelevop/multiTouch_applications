package wwf.thermometer.proportions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import wwf.thermometer.util.FilesPath;

/**
 * Classe che gestisce le proporzioni dell'immagine "thermometer.png".
 */
public final class ThermometerProportions {

    
    /** 
     * Costruttore di default.
     * */
    private ThermometerProportions() { }
    
    /** The width thermometer. */
    public static Float WIDTH_THERMOMETER 
        = ThermometerProportions.getScaledThermometerX(21.9f);
    
    /** The thermometer x. */
    public static Float THERMOMETER_X 
        = ThermometerProportions.getScaledThermometerX(17.2f);

    
    /**
     * Restituisce la X in proporzione alle dimensioni originali.
     *
     * @param value the value
     * @return the scaled X
     */
    public static float getScaledThermometerX(final float value) {
        float width = 0;
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new File(
                    FilesPath.IMAGES_PATH + "thermometer.png"));
            width          = bimg.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        return value * width / 120f;
    }

    /**
     * Gets the scaled thermometer Y.
     *
     * @param value the value
     * @return the scaled thermometer Y
     */
    public static float getScaledThermometerY(final float value) {
        float height = 0;
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new File(
                    FilesPath.IMAGES_PATH + "thermometer.png"));
            height          = bimg.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        return value * height / 347;
    }
    
    /**
     * Restituisce la Y in proporzione alle dimensioni originali.
     *
     * @param p the p
     * @return the y position
     */
    public static float getYPosition(float p) {
        return ThermometerProportions
                .getScaledThermometerY(300f) - p;
    }


}
