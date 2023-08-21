package wwf.ClassificationGame.util;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Questa classe inverte le coodinate x e y delle foto che superano un certo
 * margine, in modo da ottenere l'effetto rimbalzo quando le foto escono dallo
 * schermo.
 */
public class MTImageCustomized extends MTImage {

    /** The istanza. */
    private MTImageCustomized istanza;

    /** The dir vect. */
    private Vector3D dirVect;

    /** The p applet. */
    private PApplet pApplet;

    /** The x inverted. */
    private boolean xInverted = false;

    /** The y inverted. */
    private boolean yInverted = false;
    
    private float minimumWidthThreshoald = 250;
    
    private float maximumWidthThreshoald = 1000;

    /**
     * Costruttore parametrico.
     *
     * @param pApplet
     *            the applet
     * @param texture
     *            the texture
     */
    public MTImageCustomized(final PApplet pApplet, final PImage texture) {
        super(texture, pApplet);
        this.pApplet = pApplet;
        istanza = this;
        this.addGestureListener(DragProcessor.class, listnerButton);
        this.addGestureListener(ScaleProcessor.class, listnerButton);
        this.addGestureListener(RotateProcessor.class, listnerButton);
    }
    
    @Override
    public void scaleGlobal(float X, float Y, float Z, Vector3D scalingPoint) {
    	System.out.println("X: " + X + " WIDTH: " + this.getWidthXYGlobal());
    	if (this.getWidthXYGlobal() <= minimumWidthThreshoald && X<1)
    		this.scale(1, 1, 1, scalingPoint);
    	else if (this.getWidthXYGlobal() >= maximumWidthThreshoald && X>1)
    		this.scale(1, 1, 1, scalingPoint);
    		
    	else
    		this.scale(X, Y, Z, scalingPoint);
    }

    /**
     * Override del metodo translate che viene chiamato nelle azioni di drag,
     * inverte x e y in determinate condizioni.
     * 
     * @see org.mt4j.components.MTComponent#translate(
     *      org.mt4j.util.math.Vector3D)
     */
    @Override
    public final void translate(final Vector3D dirVect) {

        // System.out.println(dirVect);

        if (getCenterPointGlobal().y < 0 || getCenterPointGlobal().y 
                > pApplet.getHeight() && yInverted == false) {
            // dirVect.y = -dirVect.y;
            yInverted = true;
            System.out.println("invertito y");
        }

        if (getCenterPointGlobal().x < 0 || getCenterPointGlobal().x
                > pApplet.getWidth() && xInverted == false) {
            // dirVect.x = -dirVect.x;
            xInverted = true;
            System.out.println("invertito X");
        }

        if (xInverted) {
            dirVect.x = -dirVect.x;
        }

        if (yInverted) {
            dirVect.y = -dirVect.y;
        }
        super.translate(dirVect);
    }

    /**
     * se la gesture è appena iniziata, mette a false le variabili che indicano
     * se x e y sono invertite.
     */
    private IGestureEventListener listnerButton = new IGestureEventListener() {

        @Override
        public boolean processGestureEvent(final MTGestureEvent ge) {
            if (ge.GESTURE_ENDED == ge.getId()) {
                // new LanciaMTComponent(istanza,dirVect,pApplet);
            }
            if (ge.GESTURE_STARTED == ge.getId()) {
                istanza.setxInverted(false);
                istanza.setyInverted(false);
            }
            return false;
        }
    };

    /**
     * Checks if is x inverted.
     *
     * @return true, if is x inverted
     */
    public final boolean isxInverted() {
        return xInverted;
    }

    /**
     * Sets if x inverted.
     *
     * @param xInverted
     *            the new check x inverted
     */
    public final void setxInverted(final boolean xInverted) {
        this.xInverted = xInverted;
    }

    /**
     * Checks if is y inverted.
     *
     * @return true, if is y inverted
     */
    public final boolean isyInverted() {
        return yInverted;
    }

    /**
     * Sets if y inverted.
     *
     * @param yInverted
     *            the new check y inverted
     */
    public final void setyInverted(final boolean yInverted) {
        this.yInverted = yInverted;
    }

}