package wwf.mtShell;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class MTRoundRectangleCustom extends MTRoundRectangle {

	/** The x inverted. */
	private boolean xInverted = false;

	/** The y inverted. */
	private boolean yInverted = false;

	private float minimumWidthThreshoald = 250;

	private float maximumWidthThreshoald = 1500;
	
	PApplet pApplet;

	public MTRoundRectangleCustom(float x, float y, float z, float width, float height, float arcWidth, float arcHeight,
			int segments, PApplet pApplet) {
		super(x, y, z, width, height, arcWidth, arcHeight, segments, pApplet);
		this.pApplet = pApplet;
	}

	public MTRoundRectangleCustom(MTApplication applet, float f, float g, int i, float h, float j, int k, int l) {
		super(applet,f,g,i,h,j,k,l);
		this.pApplet = applet;
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
}
