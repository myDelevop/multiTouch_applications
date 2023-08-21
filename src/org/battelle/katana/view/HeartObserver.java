package org.battelle.katana.view;
import java.util.Observable;
import java.util.Observer;

import org.battelle.katana.mechanics.Fader;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

/**
 * Observes the amount of hearts currently on in the model and displays an appropriate number to the screen
 * 
 * @author cyber team
 * 
 */
public class HeartObserver implements Observer {
	/** Our current application that the game is running on */
	private MTApplication mtApp;
	/** The current canvas we are able to draw on */
	private MTCanvas currentCanvas;
	/** All of the hearts we will be modifying */
	private MTRectangle[] observedView = new MTRectangle[3];
	/** Heart one, that we will modify when needing to remove/add a heart */
	private MTRectangle heartOne;
	/** Heart two, that we will modify when needing to remove/add a heart */
	private MTRectangle heartTwo;
	/** Heart three, that we will modify when needing to remove/add a heart */
	private MTRectangle heartThree;
	/** The path our heartImage is located at */
	private String heartImage = "resources" + MTApplication.separator + "images" + MTApplication.separator + "misc" + MTApplication.separator + "invisoHeart.png";

	/**
	 * Default constructor for declaring a new observer for our hearts
	 * 
	 * @param mta The mtApplication we are currently using
	 * @param canvas The MTCanvas we are currently able to draw to
	 */
	public HeartObserver(MTApplication mta, MTCanvas canvas) {
		this.mtApp = mta;
		this.currentCanvas = canvas;
		// To make the image transparent, we had to make a Rectangle set to the
		// image
		PImage hearts = new PImage();
		hearts = mta.loadImage(heartImage);
		//Create new hearts with the correct images
		heartOne = new MTRectangle(hearts, mta);
		heartTwo = new MTRectangle(hearts, mta);
		heartThree = new MTRectangle(hearts, mta);
		//Set each of them to not be bordered (noStroke)
		heartOne.setNoStroke(true);
		heartTwo.setNoStroke(true);
		heartThree.setNoStroke(true);

		//Prepare to set their positions
		int xPos = mtApp.width / 30;
		int yPos = mtApp.width / 40;

		//Set their sizes and positions relative to the screen
		heartOne.setSizeXYGlobal(xPos, xPos);
		heartTwo.setSizeXYGlobal(xPos, xPos);
		heartThree.setSizeXYGlobal(xPos, xPos);
		heartOne.setPositionGlobal(new Vector3D(xPos, yPos, 0));
		heartTwo.setPositionGlobal(new Vector3D(2 * xPos, yPos, 0));
		heartThree.setPositionGlobal(new Vector3D(3 * xPos, yPos, 0));

		//Do not allow them to be interacted with
		heartOne.removeAllGestureEventListeners();
		heartTwo.removeAllGestureEventListeners();
		heartThree.removeAllGestureEventListeners();

		//Add them to our array
		this.observedView[0] = heartOne;
		this.observedView[1] = heartTwo;
		this.observedView[2] = heartThree;
		//Add them to our screen
		for (int i = 0; i < this.observedView.length; i++) {
			this.currentCanvas.addChild(this.observedView[i]);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		int heartCount = (Integer) arg;

		//Fade them out whenever we lose a heart
		if (heartCount <= 0) {
			Fader fader = new Fader(this.observedView[Math.abs(heartCount)], this.observedView[Math.abs(heartCount)].getFillColor());
			mtApp.invokeLater(fader);
			//Add another to the screen whenever we gain a heart
		}
		else if (heartCount > 0) {
			MTColor tempColor = this.observedView[heartCount - 1].getFillColor();
			tempColor.setAlpha(255);
			this.observedView[heartCount - 1].setFillColor(tempColor);
			this.currentCanvas.addChild(this.observedView[heartCount - 1]);
		}
	}

}
