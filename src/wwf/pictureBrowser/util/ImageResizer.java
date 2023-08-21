package wwf.pictureBrowser.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

/**
 * Classe che ridimensiona le immagini.
 */
public class ImageResizer {
    
    /** Costruttore di default. */
    private ImageResizer() { }
    
    /** The picture threshold. */
    private static int pictureThreshold = 600;
 
    /**
     * Resize.
     *
     * @param inputImagePath the input image path
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String resize(final String inputImagePath) 
            throws IOException {
        // reads input image
    	File tempFile = new File("fileTemp.jpg");
    	if (inputImagePath.contains("http")) {
    		FileUtils.copyURLToFile(new URL(inputImagePath), tempFile);
    	} else {
            tempFile = new File(inputImagePath);
    	}
        
    	BufferedImage inputImage = ImageIO.read(tempFile);
        
        float immWidth = inputImage.getWidth();
        float immHeight = inputImage.getHeight();
        float ratio = immWidth / immHeight;
        
        if (immWidth >= pictureThreshold 
                || immHeight >= pictureThreshold) {

            float newWidth;
            float newHeight;
            
            if (inputImage.getWidth() > inputImage.getHeight()) {
                newWidth = pictureThreshold;
                newHeight = pictureThreshold / ratio;
            } else {
                newHeight = pictureThreshold;
                newWidth = pictureThreshold / ratio;
            }
     
            // creates output image
            BufferedImage outputImage = new BufferedImage((int) newWidth,
                    (int) newHeight, inputImage.getType());
     
            // scales the input image to the output image
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, (int) newWidth,
                    (int) newHeight, null);
            g2d.dispose();
     
            // extracts extension of output file
            String formatName = inputImagePath.substring(inputImagePath
                    .lastIndexOf(".") + 1);
     
            // writes to output file
            String outputFile = "tmp." + formatName;
            ImageIO.write(outputImage, formatName, new File(outputFile));
            return "tmp." + formatName;
        } else {
            return inputImagePath;           
        }
    }
  
}