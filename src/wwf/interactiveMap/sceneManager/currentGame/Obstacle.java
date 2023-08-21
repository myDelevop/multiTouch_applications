package wwf.interactiveMap.sceneManager.currentGame;

import java.io.Serializable;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.interactiveMap.util.FilesPath;

// TODO: Auto-generated Javadoc
/**
 * Classe che modella l'ostacolo visualizzato nella mappa.
 */
public class Obstacle implements Serializable {


    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The pa. */
    private MTApplication pa;
    
    /** The obstacle. */
    private MTComponent obstacle;
    
    /** The upper layer. */
    private MTRectangle upperLayer;
    
    /** The layer. */
    private MTRectangle layer;

    /** The lock. */
    private boolean lock;

    /**
     * Instantiates a new obstacle.
     *
     * @param pa the pa
     * @param x the x
     * @param y the y
     * @param width the width
     * @param height the height
     * @param imageFile the image file
     */
    public Obstacle(final MTApplication pa, final float x, 
            final float y, final float width, final float height,
            final String imageFile) {
        this.pa = pa;

        obstacle = new MTComponent(pa);
        upperLayer = new MTRectangle(pa, x, y, width, height);
        upperLayer.setNoFill(false);
        upperLayer.setNoStroke(true);
        upperLayer.setFillColor(Colors.LOCK_OSTACLE);
        upperLayer.setName("upperLayer");
        upperLayer.unregisterAllInputProcessors();
        upperLayer.removeAllGestureEventListeners();

        layer = new MTRectangle(pa, x, y, width, height);
        layer.setNoFill(false);
        layer.setNoStroke(true);
        layer.setName("layer");
        layer.setTexture(pa.loadImage(FilesPath.OBSTACLES_PATH + imageFile));
        layer.unregisterAllInputProcessors();
        layer.removeAllGestureEventListeners();

        obstacle.addChild(upperLayer);
        obstacle.addChild(layer);
        
        lock = true;
    }

    /**
     * Sets the un lock status.
     */
    public final void setUnLockStatus() {
        
        this.lock = false;

        obstacle.getChildByName("layer").unregisterAllInputProcessors();
        obstacle.getChildByName("layer").removeAllGestureEventListeners();
        
        ((MTRectangle) obstacle.getChildByName("layer")).setNoStroke(true);
        ((MTRectangle) obstacle.getChildByName("layer")).setTexture(
                pa.loadImage(FilesPath.IMAGES_PATH + "unlock.png"));



        obstacle.getChildByName("upperLayer").unregisterAllInputProcessors();
        obstacle.getChildByName("upperLayer").removeAllGestureEventListeners();
        ((MTRectangle) obstacle.getChildByName("upperLayer")).setNoFill(true);
        ((MTRectangle) obstacle.getChildByName("upperLayer")).setNoStroke(true);

    }

    /**
     * Sets the position.
     *
     * @param x the x
     * @param y the y
     */
    public final void setPosition(final float x, final float y) {
        upperLayer.setPositionGlobal(new Vector3D(x, y));
        layer.setPositionGlobal(new Vector3D(x, y));
    }
    
    /**
     * Gets the obstacle.
     *
     * @return the obstacle
     */
    public final MTComponent getObstacle() {
        return obstacle;
    }

    /**
     * Sets the obstacle.
     *
     * @param obstacle the new obstacle
     */
    public final void setObstacle(final MTComponent obstacle) {
        this.obstacle = obstacle;
    }

    /**
     * Checks if is lock.
     *
     * @return true, if is lock
     */
    public final boolean isLock() {
        return lock;
    }

    /**
     * Sets the lock.
     *
     * @param lock the new lock
     */
    public final void setLock(final boolean lock) {
        this.lock = lock;
    }

    
}

/**
 * Colori stato bloccato/sbloccato.
 * */
final class Colors {
    
    /** Costruttore di default. */
    private Colors() { }
    
    /** colore stato 'bloccato'. */
    static MTColor LOCK_OSTACLE = new MTColor(255f, 0f, 0f, 212f);
    
    /** colore stato 'sbloccato'. */
    static MTColor UNLOCK_OSTACLE = new MTColor(0f, 255f, 0f, 212f);

}
