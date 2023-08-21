package wwf.drawing;
 
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException; 
import javax.imageio.ImageIO;

 
/**
 * Questa classe provvede al ridimensionamento di una immagine.
 *
 */
public final class ImageResizer {
	
    /** costruttore di default. */
    private ImageResizer() { }
    
	//private static int pictureThreshold = 600;
 
    /**
     * Ridimensiona un immagine con width e height assoluti (le dimensioni
     * possono non essere proporzionali).
     * @param inputImagePath percorso dell'immagine originale
     * @param outputImagePath percorso dell'immagine ridimensionata
     * @param scaledWidth  width in pixels assoluti
     * @param scaledHeight height in pixels assoluti
     * @throws IOException in caso di errore
     */
    public static void resize(final String inputImagePath,
            final String outputImagePath, final int scaledWidth,
            final int scaledHeight) throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
    
    /**
     * Ridimensiona un immagine in percentuale usando le proporzioni 
     * delle dimensioni originali.
     * 
     * @param inputImagePath percorso dell'immagine originale
     * @param outputImagePath percorso dell'immagine ridimensionata
     * @param percent numero double che rappresenta la percentuale
     * @throws IOException in caso di errore
     */
    public static void resize(final String inputImagePath,
            final String outputImagePath, final double percent) 
                    throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
 
}