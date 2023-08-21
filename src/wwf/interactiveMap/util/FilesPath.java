package wwf.interactiveMap.util;

import java.io.File;

/**
 * La classe contiene variabili statiche che indicano il percorso dei file.
 */
public final class FilesPath {

    /**
     * Costruttre di default.
     */
    private FilesPath() { }
    
    /** The Constant ROOT. */
    public static final String ROOT = System.getProperty("user.dir")
            + File.separator + "data" + File.separator + "interactiveMap" 
            + File.separator;

    /** The Constant IMAGES_PATH. */
    public static final String IMAGES_PATH = ROOT 
            + "images" + File.separator;

    /** The Constant SERIALIZABLE_PATH. */
    public static final String SERIALIZABLE_PATH =
            ROOT + "serializableObjects" + File.separator;

    /** The Constant SERIALIZABLE_SCORE_PATH. */
    public static final String SERIALIZABLE_SCORE_PATH =
            SERIALIZABLE_PATH + "score" + File.separator;

    /** The Constant OBSTACLES_PATH. */
    public static final String OBSTACLES_PATH =
            ROOT + "obstacles" + File.separator;
    
    /** The Constant QUESTIONS_IMAGES_PATH. */
    public static final String QUESTIONS_IMAGES_PATH = ROOT 
            + "questions" + File.separator;


    /** The Constant QUESTIONS_DESCRIPTIONS_PATH. */
    public static final String QUESTIONS_DESCRIPTIONS_PATH = ROOT 
            + "questions" + File.separator + "descriptions" + File.separator;

    /** The Constant SOUNDS_PATH. */
    public static final String SOUNDS_PATH = ROOT 
            + "sounds" + File.separator;

}
