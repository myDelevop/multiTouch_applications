package wwf.todelete;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.util.MTColor;


/**
 * La classe contiene variabili relative ai fonts usati nell'applicazione.
 */
public class Fonts {

	private static Fonts fonts = null;

	/** The pa. */
	private MTApplication pa;


	/** The semi light 24 white. */
	private IFont semiLight24White;

	/** The semi light 24 black. */
	private IFont semiLight24Black;

	/** The semi light 32 black. */
	private IFont semiLight32Black;

	/** The semi light 62 black. */
	private IFont semiLight64Black;

	/** The bold 24 white. */
	private IFont segoe50White;

	/** The bold 26 black. */
	private IFont bold26Black;

	/** The seguisb 24 white. */
	private IFont seguisb24White;

	/** The seguisb 32 white. */
	private IFont seguisb32White;

	/** The seguisb 36 white. */
	private IFont seguisb36White;

	/** The seguisb 36 red. */
    private IFont seguisb36Red;

	/** The bold time. */
	private IFont boldTime;

	public static Fonts getInstance(MTApplication pa){
		if(fonts == null) {
			fonts = new Fonts(pa);
		}
		return fonts;
	}



	/**
	 * Instantiates a new fonts.
	 *
	 * @param pa the pa
	 */
	public Fonts(final MTApplication pa) {
		this.pa = pa;
		this.semiLight24White = FontManager.getInstance().
				createFont(pa, "segoeuisl.ttf", 24, MTColor.WHITE);
		this.semiLight24Black = FontManager.getInstance().
				createFont(pa, "segoeuisl.ttf", 24, MTColor.BLACK);
		this.semiLight32Black = FontManager.getInstance().
				createFont(pa, "segoeuisl.ttf", 32, MTColor.BLACK);
		this.semiLight64Black = FontManager.getInstance().
				createFont(pa, "segoeuisl.ttf", 64, MTColor.BLACK);
		this.segoe50White = FontManager.getInstance().
				createFont(pa, "Segoe_UI_bold.ttf", 50, MTColor.WHITE);
		this.bold26Black = FontManager.getInstance().
				createFont(pa, "Segoe_UI_bold.ttf", 56, MTColor.BLACK);
		this.seguisb24White = FontManager.getInstance().
				createFont(pa, "seguisb.ttf", 50, MTColor.WHITE);
		this.seguisb32White = FontManager.getInstance().
				createFont(pa, "seguisb.ttf", 60, MTColor.WHITE);
        this.seguisb36White = FontManager.getInstance().
                createFont(pa, "seguisb.ttf", 60, MTColor.WHITE);
        this.seguisb36Red = FontManager.getInstance().
                createFont(pa, "seguisb.ttf", 60, MTColor.RED);
		this.boldTime = FontManager.getInstance().
				createFont(pa, "Segoe_UI_bold.ttf", 60, MTColor.BLACK);

	}


	/**
	 * Gets the semi light 24 white.
	 *
	 * @return the semi light 24 white
	 */
	public final IFont getSemiLight24White() {
		return semiLight24White;
	}


	/**
	 * Gets the bold 24 white.
	 *
	 * @return the bold 24 white
	 */
	public final IFont getSegoe50White() {
		return segoe50White;
	}


	/**
	 * Gets the seguisb 24 white.
	 *
	 * @return the seguisb 24 white
	 */
	public final IFont getSeguisb24White() {
		return seguisb24White;
	}

	/**
	 * Gets the seguisb 32 white.
	 *
	 * @return the seguisb 32 white
	 */
	public final IFont getSeguisb32White() {
		return seguisb32White;
	}

    /**
     * Gets the seguisb 36 white.
     *
     * @return the seguisb 36 white
     */
    public final IFont getSeguisb36White() {
        return seguisb36White;
    }

    /**
     * Gets the seguisb 36 white.
     *
     * @return the seguisb 36 white
     */
    public final IFont getSeguisb36Red() {
        return seguisb36Red;
    }


	/**
	 * Gets the semi light 24 black.
	 *
	 * @return the semi light 24 black
	 */
	public final IFont getSemiLight24Black() {
		return semiLight24Black;
	}


	/**
	 * Gets the bold 26 black.
	 *
	 * @return the bold 26 black
	 */
	public final IFont getBold26Black() {
		return bold26Black;
	}


	/**
	 * Gets the bold time.
	 *
	 * @return the bold time
	 */
	public final IFont getBoldTime() {
		return boldTime;
	}


	/**
	 * Gets the semi light 32 black.
	 *
	 * @return the semi light 32 black
	 */
	public final IFont getSemiLight32Black() {
		return semiLight32Black;
	}


	public IFont getSemiLight64Black() {
		return semiLight64Black;
	}	
}