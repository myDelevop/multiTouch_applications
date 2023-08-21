package wwf.thermometer;


import org.mt4j.MTApplication;


// TODO: Auto-generated Javadoc
/**
 * The Class StartThermometer.
 */
public class StartThermometer extends MTApplication {
	
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
	
	/** (non-Javadoc).
	 * @see org.mt4j.MTApplication#startUp()
	 */
	@Override
	public final void startUp() {
		this.addScene(new ThermometerScene(this, "Thermometer Scene"));
	}
}
