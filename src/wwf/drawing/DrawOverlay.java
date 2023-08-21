package wwf.drawing;

import java.util.HashMap;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;


/**
 * The Class DrawOverlay.
 */
public class DrawOverlay extends AbstractScene {

	/** The mt app. */
	private MTApplication mtApp;

	/** The draw shape. */
	private AbstractShape drawShape;

	/** The step distance. */
	private float stepDistance;

	/** The local brush center. */
	private Vector3D localBrushCenter;

	/** The brush width half. */
	private float brushWidthHalf;

	/** The cursor to last drawn point. */
	private HashMap<InputCursor, Vector3D> cursorToLastDrawnPoint;

	/** The brush height half. */
	private float brushHeightHalf;

	/** The brush scale. */
	private float brushScale;

	/** The brush color. */
	private MTColor brushColor;

	/** The dynamic brush. */
	private boolean dynamicBrush;

	/**
	 * Instantiates a new draw overlay.
	 *
	 * @param mtApplication the mt application
	 * @param name the name
	 */
	public DrawOverlay(final MTApplication mtApplication, final String name) {
		super(mtApplication, name);
		this.mtApp = mtApplication;

		this.getCanvas().setDepthBufferDisabled(true);

		this.brushColor = new MTColor(0, 0, 0);
		this.brushScale = 1.0f;
		this.dynamicBrush = true;

		this.cursorToLastDrawnPoint = new HashMap<InputCursor, Vector3D>();

		this.getCanvas().addInputListener(new IMTInputEventListener() {
			public boolean processInputEvent(final MTInputEvent inEvt) {
				if (inEvt instanceof AbstractCursorInputEvt) {
					final AbstractCursorInputEvt posEvt = 
					        (AbstractCursorInputEvt) inEvt;
					final InputCursor m = posEvt.getCursor();

					if (posEvt.getId() != AbstractCursorInputEvt.INPUT_ENDED) {
						registerPreDrawAction(new IPreDrawAction() {
							public void processAction() {
								boolean firstPoint = false;
								Vector3D lastDrawnPoint = 
								        cursorToLastDrawnPoint.get(m);
								Vector3D pos = new Vector3D(
								        posEvt.getX(), posEvt.getY(), 0);

								if (lastDrawnPoint == null) {
									lastDrawnPoint = new Vector3D(pos);
									cursorToLastDrawnPoint.put(m,
									        lastDrawnPoint);
									firstPoint = true;
								} else {
								    if (lastDrawnPoint.equalsVector(pos)) {
                                        return;
                                    }	
								}

								float scaledStepDistance = 
								        stepDistance * brushScale;

								Vector3D direction = pos.getSubtracted(
								        lastDrawnPoint);
								float distance = direction.length();
								direction.normalizeLocal();
								direction.scaleLocal(scaledStepDistance);

								float howManySteps = distance 
								        / scaledStepDistance;
								int stepsToTake = Math.round(howManySteps);

								//Force draw at 1st point
								if (firstPoint && stepsToTake == 0) {
									stepsToTake = 1;
								}
								//				
								mtApp.pushMatrix();
								getSceneCam().update(); 

								Vector3D currentPos = new Vector3D(
								        lastDrawnPoint);
								for (int i = 0; i < stepsToTake; i++) {
									currentPos.addLocal(direction);
									Vector3D diff = currentPos.
									        getSubtracted(localBrushCenter);

									mtApp.pushMatrix();
									mtApp.translate(diff.x, diff.y);

									mtApp.translate(brushWidthHalf,
									        brushHeightHalf);
									mtApp.scale(brushScale);

									if (dynamicBrush) {
										mtApp.rotateZ(PApplet.
										        radians(ToolsMath.getRandom(
										                -25, 25)));
										mtApp.translate(-brushWidthHalf, 
										        -brushHeightHalf);
									}

									AbstractShape brushToDraw = drawShape;
									brushToDraw.drawComponent(mtApp.g);

									mtApp.popMatrix();
								}
								mtApp.popMatrix();

								cursorToLastDrawnPoint.put(m, currentPos);
							}

							public boolean isLoop() {
								return false;
							}
						});
					} else {
						cursorToLastDrawnPoint.remove(m);
					}
				}
				return false;
			}
		});

	}


	/**
	 * Sets the brush.
	 *
	 * @param brush the new brush
	 */
	public final void setBrush(final AbstractShape brush) {
		this.drawShape = brush;
		this.localBrushCenter = drawShape.getCenterPointLocal();
		this.brushWidthHalf = drawShape.getWidthXY(TransformSpace.LOCAL) / 2f;
		this.brushHeightHalf = drawShape.getHeightXY(TransformSpace.LOCAL) / 2f;
		this.stepDistance = brushWidthHalf / 2.8f;
		this.drawShape.setFillColor(this.brushColor);
		this.drawShape.setStrokeColor(this.brushColor);
	}

	/**
	 * Sets the brush color.
	 *
	 * @param color the new brush color
	 */
	public final void setBrushColor(final MTColor color) {
		this.brushColor = color;
		if (this.drawShape != null) {
			drawShape.setFillColor(color);
			drawShape.setStrokeColor(color);
		}
	}

	/**
	 * Sets the brush scale.
	 *
	 * @param scale the new brush scale
	 */
	public final void setBrushScale(final float scale) {
		this.brushScale = scale;
	}


	/**
  	 * @see org.mt4j.sceneManagement.AbstractScene#onEnter()
	 */
	public void onEnter() { }

	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#onLeave()
	 */
	public void onLeave() { }
}
