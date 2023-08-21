package wwf.cranium.util;


import java.io.File;


/**
 * La classe contiene variabili che indicano il percorso dei file.
 */

public final class FilesPath {
    
    /**
     * Costruttore di default.
     * */
    private FilesPath() { }
 
	/** The Constant ROOT. */
	public static final String ROOT = System.getProperty("user.dir")
	        + File.separator + "data" + File.separator + "cranium" 
	        + File.separator;

	/** The Constant SERIALIZABLE_PATH. */
	public static final String SERIALIZABLE_PATH =
	        ROOT + "serializableObjects" + File.separator;


	/** The Constant AVATARS_PATH. */
	public static final String AVATARS_PATH = ROOT 
	        + "avatars" + File.separator;
	
	/** The Constant QUESTIONS_IMAGES_PATH. */
	public static final String QUESTIONS_IMAGES_PATH = ROOT 
	        + "questions" + File.separator;
	
	/** The Constant SOUNDS_PATH. */
	public static final String SOUNDS_PATH = ROOT 
	        + "sounds" + File.separator;

}
