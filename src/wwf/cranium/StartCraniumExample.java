package wwf.cranium;


import org.mt4j.MTApplication;

import wwf.cranium.sceneManager.CraniumSceneManager;


/**
 * The Class StartCraniumExample.
 */
public class StartCraniumExample extends MTApplication {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see org.mt4j.MTApplication#startUp()
	 */
	@Override
    public final void startUp() {
		
		this.addScene(new CraniumSceneManager(this, "Cranium Scene"));
	}
}
