package wwf.thermometer.data;

import java.util.EnumMap;

import org.mt4j.MTApplication;

import processing.core.PImage;
import wwf.thermometer.util.FilesPath;
import wwf.thermometer.util.WaterStatus;

/**
 *  Associa ad ogni stato dell'acqua un'immagine.
 */
public class Images {
    
    /** The images. */
    private EnumMap<WaterStatus, PImage> images;
    
    /**
     * Instantiates a new images.
     *
     * @param pa the pa
     */
    public Images(final MTApplication pa) { 
        images = new EnumMap<WaterStatus, PImage>(WaterStatus.class);
        images.put(WaterStatus.ICE, pa.loadImage(FilesPath.BACKGROUNDS_PATH 
                + "ice.jpg"));
        images.put(WaterStatus.WATER, pa.loadImage(FilesPath.BACKGROUNDS_PATH 
                + "water.jpg"));
        images.put(WaterStatus.VAPOR, pa.loadImage(FilesPath.BACKGROUNDS_PATH
                + "vapor.jpg"));
    }

    /**
     * Gets the images.
     *
     * @return the images
     */
    public final EnumMap<WaterStatus, PImage> getImages() {
        return images;
    }

    /**
     * Sets the images.
     *
     * @param images the images
     */
    public final void setImages(final EnumMap<WaterStatus,
            PImage> images) {
        this.images = images;
    }
    
    
}
