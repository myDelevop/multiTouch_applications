package wwf.thermometer.data;

import org.mt4j.MTApplication;

import wwf.thermometer.ThermometerScene;

/**
 * Contiene le immagini delle molecole.
 */
public class CurrentData {
    
    /** The images. */
    private Images images;
    
    /** The molecules. */
    private Molecules molecules;
    
    /**
     * Instantiates a new current data.
     *
     * @param pa the pa
     * @param therm the therm
     */
    public CurrentData(final MTApplication pa, ThermometerScene therm) {
        images = new Images(pa);
        molecules = new Molecules(pa, therm);
    }

    /**
     * Gets the images.
     *
     * @return the images
     */
    public final Images getImages() {
        return images;
    }

    /**
     * Sets the images.
     *
     * @param images the new images
     */
    public final void setImages(final Images images) {
        this.images = images;
    }

    /**
     * Gets the molecules.
     *
     * @return the molecules
     */
    public Molecules getMolecules() {
        return molecules;
    }

    /**
     * Sets the molecules.
     *
     * @param molecules the new molecules
     */
    public void setMolecules(Molecules molecules) {
        this.molecules = molecules;
    }

    

}
