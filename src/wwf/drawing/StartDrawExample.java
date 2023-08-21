package wwf.drawing;

import org.mt4j.MTApplication;


/**
 * The Class StartDrawExample.
 */
public class StartDrawExample extends MTApplication {

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
	
/**
  @see org.mt4j.MTApplication#startUp()
	 */
	@Override
    public final void startUp() {
		this.addScene(new MainDrawingScene(this, "MT Paint"));
	}
	
}



