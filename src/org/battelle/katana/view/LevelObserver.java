package org.battelle.katana.view;
import java.util.Observable;
import java.util.Observer;

import org.battelle.katana.mechanics.Fader;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

/**
 * Observes the current level in the model and displays an appropriate number to the screen
 * 
 * @author cyber team
 * 
 */
public class LevelObserver implements Observer {

	/** The application we are using this game */
	private MTApplication mtApp;
	/** The current canvas we are able to draw to */
	private MTCanvas currentCanvas;
	/** The text area we are acting on when we observe changes */
	private MTTextArea observedView;
	/** The text color for the game */
	private MTColor textColor;

	/**
	 * Default constructor for creating a new observer for the levels
	 * 
	 * @param mta The mtApplication we are using this game
	 * @param canvas The canvas we are able to draw to
	 */
	public LevelObserver(MTApplication mta, MTCanvas canvas, MTColor textColor) {
		//Initialize variables
		this.mtApp = mta;
		this.currentCanvas = canvas;
		this.textColor = textColor;
		//Initialize the text area with sizes and locations relative to our mtApp's size
		this.observedView = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial", mtApp.width / 30, MTColor.WHITE, MTColor.BLACK));
		//Remove the borders and fill
		this.observedView.setNoFill(true);
		this.observedView.setNoStroke(true);
		//Make it impossible to move or alter it manually
		this.observedView.removeAllGestureEventListeners();
		//Set the position relative to the total screen size
		this.observedView.setPositionGlobal(new Vector3D((mtApp.width / 2f) - mtApp.width / 20, mtApp.height - mtApp.height / 10, 0f));
		//Set the initial level to be level 1
		this.observedView.setText("Level 1");
		//Add the child to the canvas
		this.currentCanvas.addChild(this.observedView);

		//Fade it out
		Fader levelFader = new Fader(this.observedView, this.observedView.getFont().getFillColor(), 3);
		mtApp.invokeLater(levelFader);
	}

	@Override
	public void update(Observable o, Object arg) {
		//Get the new level and update the text area with it
		int newLevel = (Integer) arg;
		((AbstractShape) this.observedView.getFont()).setFillColor(new MTColor(textColor));
		this.observedView.setText("Level " + newLevel);
		this.currentCanvas.addChild(this.observedView);

		//Fade out the new text area
		Fader levelFader = new Fader(this.observedView, this.observedView.getFont().getFillColor(), 3);
		mtApp.invokeLater(levelFader);

	}

}
