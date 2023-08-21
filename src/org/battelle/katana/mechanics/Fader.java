package org.battelle.katana.mechanics;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;

/**
 * Handles fading the line out of sight of the canvas and eventually deleting it
 * 
 * @author cyber group
 * 
 */
public class Fader implements Runnable {
	/** Our reference to the line we want to fade */
	private MTLine localLine;
	/** Our reference to the rectangle we want to fade */
	private MTPolygon rectangle;
	/** Our reference to the text we want to fade */
	private MTTextArea text;
	/** The amount we want to fade out at a time. Higher values fade faster */
	private int fadeAmt;
	/** The color we want our component to be, the alpha value is stored in this as well */
	private MTColor localColor;

	/**
	 * Default constructor for passing in values
	 * 
	 * @param component The component we are going to fade
	 * @param color The color we are going to fade the line with
	 * @param fadeAmount the amount per frame we wish to fade the component
	 */
	public Fader(MTComponent component, MTColor color, int fadeAmount) {
	  //TODO: Might need to be more modularized
		if (component instanceof MTLine) {
			this.localLine = (MTLine) component;
		}
		else if (component instanceof MTTextArea) {
			this.text = (MTTextArea) component;
		}
		else if (component instanceof MTPolygon) {
			this.rectangle = (MTPolygon) component;
		}

		this.localColor = color;
		this.fadeAmt = fadeAmount;
	}

	/**
	 * Default constructor for passing in values
	 * 
	 * @param component The component we are going to fade
	 * @param color The color we are going to fade the line with
	 * 
	 */
	public Fader(MTComponent component, MTColor color) {
		this(component, color, 7);
	}

	@Override
	public void run() {

		// Initialize the interpolator, here we fade over one second and only do
		// it once
		final MultiPurposeInterpolator mt = new MultiPurposeInterpolator(0, 1,
				3100, 0, 0, 1);

		// Create the toss animation
		Animation fade = new Animation("fade", mt, this.localLine);
		// Define the animation
		fade.addAnimationListener(new IAnimationListener() {
			public void processAnimationEvent(AnimationEvent ae) {
				// Decrement the alpha
				localColor.setAlpha(localColor.getAlpha() - fadeAmt);
				// Set the stroke color
				if (localLine != null) {
					localLine.setStrokeColor(localColor);
					if (localColor.getAlpha() <= 0 || mt.isFinished()) {
						localLine.destroy();
					}
				}
				// If we're using text, set the fill color
				else if (text != null) {
					text.setFillColor(localColor);
					if (localColor.getAlpha() <= 0 || mt.isFinished()) {
						text.destroy();
					}
				}
				// If we're using rectangles, set the fillColor
				else if (rectangle != null) {
					rectangle.setFillColor(localColor);
					if (localColor.getAlpha() <= 0 || mt.isFinished()) {
						localColor.setAlpha(255);
						rectangle.setFillColor(localColor);
						rectangle.destroy();
					}
				}
			}
		});

		// Start the animation
		fade.start();
	}
}
