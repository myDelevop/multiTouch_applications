package org.battelle.katana.view;
import java.util.Observable;
import java.util.Observer;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

/**
 * Observer for the total score achieved this game. Updates the score with every increase (or decrease)
 * 
 * @author cyber team
 *
 */
public class ScoreObserver implements Observer {
	/** The application our game is taking place on */
	private MTApplication mtApp;
	/** The canvas we are currently able to draw on */
	private MTCanvas currentCanvas;
	/** The text we are displaying based on our observed model */
	private MTTextArea observedView;
	/** The text color for the game */
	private MTColor textColor;

	/**
	 * Default constructor setting up the score observer.
	 * 
	 * @param mta The application our game is taking place on
	 * @param canvas The canvas we are currently able to draw on
	 */
	public ScoreObserver(MTApplication mta, MTCanvas canvas, MTColor textColor) {
		//Set up our variables
		this.mtApp = mta;
		this.currentCanvas = canvas;
		this.textColor = textColor;
		//Create our text area
		this.observedView = new MTTextArea(mtApp, FontManager.getInstance()
				.createFont(mtApp, "arial", mtApp.width/30));
		//Remove fill and borders
		this.observedView.setNoFill(true);
		this.observedView.setNoStroke(true);
		//Remove the ability to move or alter this text
		this.observedView.removeAllGestureEventListeners();
		//Set the position
		this.observedView.setPositionGlobal(new Vector3D(mtApp.width * .85f,
				mtApp.height/20f, 0f));
		this.observedView.setText("0");
		this.currentCanvas.addChild(this.observedView);
	}

	@Override
	public void update(Observable o, Object arg) {
		//Cast the score into an integer
		int newScore = (Integer) arg;
		//Set it on the screen
		this.observedView.setText(Integer.toString(newScore));
	}

	public MTTextArea getText() {
		return observedView;
	}

}