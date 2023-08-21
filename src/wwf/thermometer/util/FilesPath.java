package wwf.thermometer.util;


import java.io.File;

/**
 * La classe contiene variabili che indicano il percorso dei file.
 */
public final class FilesPath {

 
    /** costruttore di default. */
    private FilesPath() { }
    
	/** The Constant ROOT. */
	public static final String ROOT = System.getProperty("user.dir")
	        + File.separator + "data" + File.separator + "thermometer" 
	        + File.separator;

	/** The Constant BACKGROUNDS_PATH. */
	public static final String BACKGROUNDS_PATH =
	        ROOT + "backgrounds" + File.separator;
	
   /** The Constant IMAGES_PATH. */
    public static final String IMAGES_PATH =
            ROOT + "images" + File.separator;
    
    /** The Constant MODELS3D_PATH. */
    public static final String MODELS3D_PATH =
            ROOT + "models3D" + File.separator;
    

}
