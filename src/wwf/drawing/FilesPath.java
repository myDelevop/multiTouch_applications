package wwf.drawing;

import java.io.File;


/**
 * La classe contiene variabili statiche che indicano il percorso dei file.
 */
public final class FilesPath {
    
    /** Costruttore di default. */
    private FilesPath() { }
    
	/** The Constant ROOT. */
	private static final String ROOT = System.getProperty("user.dir") 
	        + File.separator + "data" + File.separator
											+ "drawing" + File.separator;
	
	/** The Constant BACKGROUNDS_PATH. */
	public static final String BACKGROUNDS_PATH = ROOT + File.separator 
	        + "backgrounds" + File.separator;
	
	
	/** The Constant RESIZEBACKGROUNDS_PATH. */
	public static final String RESIZEBACKGROUNDS_PATH = ROOT + "backgrounds"
	+ File.separator + "resizeBackgrounds" + File.separator;
	
	
	/** The Constant IMAGES_PATH. */
	public static final String IMAGES_PATH = ROOT + "images" + File.separator;
	
	
	/** The Constant RESIZEICONS_PATH. */
	public static final String RESIZEICONS_PATH = ROOT + "backgrounds" 
	+ File.separator + "resizeIcons" + File.separator;

}
