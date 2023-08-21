package org.battelle.katana.view;

import java.util.Observable;
import java.util.Observer;

import org.battelle.katana.mechanics.Fader;
import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

/**
 * Observer for the number of fruit you have cut in a row without missing one. Updates the picture fading number every 5 fruits cut.
 * 
 * @author cyber team
 * 
 */
public class FruitCutInRowObserver implements Observer {
  /** The application we are using for this particular game */
  private MTApplication mtApp;
  /** The canvas we are using to draw on for this game */
  private MTCanvas currentCanvas;
  /** The text field our observer updates when notified of a change */
  private MTTextArea observedView;
  /** The sound factory for creating our random sounds upon cut */
  private SoundFactory soundFactory = SoundFactory.getInstance();
  /** The text color for the game */
  private MTColor textColor;

  /**
   * Default constructor for setting up our observer and our graphic on the screen
   * 
   * @param mta The application we are using for this game
   * @param canvas The canvas we are using to draw on
   */
  public FruitCutInRowObserver(MTApplication mta, MTCanvas canvas, MTColor textColor) {
    //Initialize our global variables
    this.mtApp = mta;
    this.currentCanvas = canvas;
    this.textColor = textColor;
    //Create our text area we will be fading out
    this.observedView = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial", mtApp.height / 5));
    this.observedView.setNoFill(true);
    this.observedView.setNoStroke(true);
    this.observedView.removeAllGestureEventListeners();
    this.observedView.setPositionGlobal(new Vector3D(mtApp.width / 2f - mtApp.width / 20, mtApp.height / 2f, 0));
  }

  @Override
  public void update(Observable o, Object arg) {
    //Cast the argument we pass in into an integer
    int fruitCutInRow = (Integer) arg;

    //Every 5 cuts in a row, display the combo and fade it out
    if (fruitCutInRow % 10 == 0 && fruitCutInRow != 0) {
      //Update the text with the new number
      this.observedView.setText(Integer.toString(fruitCutInRow));
      //Reset the alpha
      this.observedView.setStrokeColor(new MTColor(textColor));
      //Add it to the screen
      this.currentCanvas.addChild(this.observedView);

      //Prepare to fade out the text
      Fader fader = new Fader(this.observedView, this.observedView.getFont().getFillColor());
      mtApp.invokeLater(fader);

      //Play the sounds depending on what combo we have
      if (fruitCutInRow == 40) {
        soundFactory.generateSound(SoundType.WICKEDSICK).start();
      }
      else if (fruitCutInRow == 30) {
        soundFactory.generateSound(SoundType.GODLIKE).start();
      }
      else if (fruitCutInRow == 20) {
        soundFactory.generateSound(SoundType.UNSTOPPABLE).start();
      }
      else if (fruitCutInRow == 10) {
        soundFactory.generateSound(SoundType.DOMINATING).start();
      }
      else if (fruitCutInRow % 10 == 0) {
        soundFactory.generateSound(SoundType.LUDICROUS).start();
      }
    }
  }
}
