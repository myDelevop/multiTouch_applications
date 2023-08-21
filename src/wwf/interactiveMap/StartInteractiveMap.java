package wwf.interactiveMap;


import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.util.MTColor;

import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.interactiveMap.util.WaterSceneBackground;


// TODO: Auto-generated Javadoc
/**
 * The Class StartInteractiveMap.
 */
public class StartInteractiveMap extends MTApplication { 
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	/* (non-Javadoc).
	 * @see org.mt4j.MTApplication#startUp()
	 */
	@Override
	public void startUp() {
	    InteractiveMapSceneManager scene 
	        = new InteractiveMapSceneManager(this, "Water Scene");

        this.addScene(scene);
	}
	
}
