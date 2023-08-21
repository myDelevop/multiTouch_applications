package org.battelle.katana.view;
import java.util.Observable;
import java.util.Observer;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class TotalFruitObserver implements Observer {

	private MTApplication mtApp;
	private MTCanvas currentCanvas;
	private MTTextArea observedView;
	/** The color for text */
	private MTColor textColor;

	public TotalFruitObserver(MTApplication mta, MTCanvas canvas, MTColor textColor) {
		this.mtApp = mta;
		this.currentCanvas = canvas;
		this.textColor = textColor;
		// Initialize the fruit counter field
		this.observedView = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial", mtApp.height / 30));
		this.observedView.setNoFill(true);
		this.observedView.setNoStroke(true);
		this.observedView.setText("0");
		this.observedView.removeAllGestureEventListeners();
		this.observedView.setPositionGlobal(new Vector3D(2 * mtApp.width / 11, mtApp.height / 24, 0));
		this.currentCanvas.addChild(this.observedView);

	}

	@Override
	public void update(Observable o, Object arg) {
		int totalFruit = (Integer) arg;

		this.observedView.setText(Integer.toString(totalFruit));

	}

}
