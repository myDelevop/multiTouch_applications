package wwf.pictureBrowser.util;



import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * La classe contiene variabili che indicano il percorso dei file.
 */
public final class FilesPath {

    /**Costruttore di default. */
    private FilesPath() { }

    /** The Constant rootPath. */
    public static final String ROOTPATH = 
            System.getProperty("user.dir") + File.separator + "data" 
            + File.separator + "pictureBrowser" + File.separator;

    /** The Constant APIKEYS_PATH. */
    public static final String APIKEYS_PATH 
                = ROOTPATH + File.separator + "API_keys" + File.separator;

    /** The Constant IMAGES_PATH. */
    public static final String IMAGES_PATH 
                = ROOTPATH + "images" + File.separator;

    /** The Constant FOTOS_PATH. */
    public static final String FOTOS_PATH 
                = ROOTPATH + "fotos" + File.separator
                + "social networks (ifttt)" + File.separator;

    /** The Constant FACEBOOK_FOTOS_PATH. */
    public static final String FACEBOOK_FOTOS_PATH 
                = FOTOS_PATH + "facebook" + File.separator;

    /** The Constant TWITTER_FOTOS_PATH. */
    public static final String TWITTER_FOTOS_PATH 
                = FOTOS_PATH + "twitter" + File.separator;

    /** The Constant INSTAGRAM_FOTOS_PATH. */
    public static final String INSTAGRAM_FOTOS_PATH 
                = FOTOS_PATH + "instagram" + File.separator;

    /** The Constant FLICKR_FOTOS_PATH. */
    public static final String FLICKR_FOTOS_PATH 
                = FOTOS_PATH + "flickr" + File.separator;

    /** The Constant CUSTOM_FOTOS_DIR_PATH. */
    public static final String CUSTOM_FOTOS_DIR_PATH 
                = ROOTPATH + "custom" + File.separator;

    /** The Constant VIDEOS_PATH. */
    public static final String VIDEOS_PATH 
                = ROOTPATH + File.separator + "videos" + File.separator;

    /** The Constant FACEBOOK_VIDEOS_PATH. */
    public static final String FACEBOOK_VIDEOS_PATH 
                = VIDEOS_PATH + "facebook" + File.separator;

    /** The Constant TWITTER_VIDEOS_PATH. */
    public static final String TWITTER_VIDEOS_PATH
                = VIDEOS_PATH + "twitter" + File.separator;

    /** The Constant INSTAGRAM_VIDEOS_PATH. */
    public static final String INSTAGRAM_VIDEOS_PATH
                = VIDEOS_PATH + "instagram" + File.separator;

    /** The Constant FLICKR_VIDEOS_PATH. */
    public static final String FLICKR_VIDEOS_PATH 
                = VIDEOS_PATH + "flickr" + File.separator;

    /** The Constant OFFLINE_FOTOS_PATH. */
    public static final String OFFLINE_FOTOS_PATH 
                = ROOTPATH + "fotos" + File.separator 
                + "social networks (offline)" + File.separator;

    /** The Constant OFFLINE_FACEBOOK_FOTOS_PATH. */
    public static final String OFFLINE_FACEBOOK_FOTOS_PATH 
                = OFFLINE_FOTOS_PATH + "facebook" + File.separator;

    /** The Constant OFFLINE_TWITTER_FOTOS_PATH. */
    public static final String OFFLINE_TWITTER_FOTOS_PATH 
                = OFFLINE_FOTOS_PATH + "twitter" + File.separator;

    /** The Constant OFFLINE_INSTAGRAM_FOTOS_PATH. */
    public static final String OFFLINE_INSTAGRAM_FOTOS_PATH 
                = OFFLINE_FOTOS_PATH + "instagram" + File.separator;

    /** The Constant OFFLINE_FLICKR_FOTOS_PATH. */
    public static final String OFFLINE_FLICKR_FOTOS_PATH
                = OFFLINE_FOTOS_PATH + "flickr" + File.separator;

    /** The Constant SERIALIZABLE_PATH. */
    public static final String SERIALIZABLE_PATH 
                = ROOTPATH + "serializableObjects" + File.separator;

    /** The paths. */
    private static LinkedList<String> paths = new LinkedList<String>();

    /**
     * Creates the file paths.
     */
    public static void createFilePaths() {
        paths.add(ROOTPATH);
        paths.add(APIKEYS_PATH);
        paths.add(IMAGES_PATH);
        paths.add(FOTOS_PATH);
        paths.add(FACEBOOK_FOTOS_PATH);
        paths.add(TWITTER_FOTOS_PATH);
        paths.add(INSTAGRAM_FOTOS_PATH);
        paths.add(FLICKR_FOTOS_PATH);
        paths.add(CUSTOM_FOTOS_DIR_PATH);
        paths.add(VIDEOS_PATH);
        paths.add(FACEBOOK_VIDEOS_PATH);
        paths.add(TWITTER_VIDEOS_PATH);
        paths.add(INSTAGRAM_VIDEOS_PATH);
        paths.add(FLICKR_VIDEOS_PATH);
        paths.add(OFFLINE_FOTOS_PATH);
        paths.add(OFFLINE_FACEBOOK_FOTOS_PATH);
        paths.add(OFFLINE_TWITTER_FOTOS_PATH);
        paths.add(OFFLINE_INSTAGRAM_FOTOS_PATH);
        paths.add(OFFLINE_FLICKR_FOTOS_PATH);
        paths.add(SERIALIZABLE_PATH);

        Iterator<String> it = paths.iterator();
        while (it.hasNext()) {
            String path = (String) it.next();
            File file = new File(path);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory is created!");
                } else {
                    System.out.println("Failed to create directory!");
                }
            }
        }

    }

}
