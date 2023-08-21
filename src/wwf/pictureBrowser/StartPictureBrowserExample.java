package wwf.pictureBrowser;

import org.mt4j.MTApplication;


/**
 * The Class StartPictureBrowserExample.
 */
public class StartPictureBrowserExample extends MTApplication {

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
 * 	 * @see org.mt4j.MTApplication#startUp()
	 */
	@Override
    public final void startUp() {
		PictureBrowser pictureBrowser =
		        new PictureBrowser(this, "Picture Browser");
		this.addScene(pictureBrowser);
	}
}