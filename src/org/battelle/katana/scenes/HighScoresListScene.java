package org.battelle.katana.scenes;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;


public class HighScoresListScene extends GameScene
{
  private MTApplication mtApp;
  private String imagesPath = "resources/images/misc" + MTApplication.separator;
  private IFont arialWhite;
  private IFont arialWhiteBig;
  private MTColor textColor;

  public HighScoresListScene(MTApplication mtApplication, String name, HighScoresList hs, MTColor textColor)
  {
    super(mtApplication, name);
    this.mtApp = mtApplication;
    this.textColor = textColor;

    this.arialWhite = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", this.mtApp.height / 18);
    this.arialWhiteBig = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", this.mtApp.height / 13);

    createQuitButton();

    MTTextArea scoresList = new MTTextArea(this.mtApp, this.arialWhite);
    MTTextArea namesList = new MTTextArea(this.mtApp, this.arialWhite);
    MTTextArea header = new MTTextArea(this.mtApp, this.arialWhiteBig);
    String scoresListText = "Score: \n";
    String namesListText = "Name: \n";

    String[][] namesAndScores = hs.getNamesAndScores();

    for (int i = 0; (i < hs.getNumScores()) && (i < 10); i++) {
      scoresListText = scoresListText + namesAndScores[0][i] + "\n";
      namesListText = namesListText + namesAndScores[1][i] + "\n";
    }

    scoresList.setText(scoresListText);
    scoresList.removeAllGestureEventListeners();
    scoresList.setNoFill(true);
    scoresList.setNoStroke(true);
    scoresList.setAnchor(MTRectangle.PositionAnchor.UPPER_LEFT);
    scoresList.setPositionGlobal(new Vector3D(2 * this.mtApp.width / 8.0F, this.mtApp.height / 10.0F, 0.0F));

    namesList.setText(namesListText);
    namesList.removeAllGestureEventListeners();
    namesList.setNoFill(true);
    namesList.setNoStroke(true);
    namesList.setAnchor(MTRectangle.PositionAnchor.UPPER_LEFT);
    namesList.setPositionGlobal(new Vector3D(5 * this.mtApp.width / 8.0F, this.mtApp.height / 10.0F, 0.0F));

    header.setText("High Scores");
    header.removeAllGestureEventListeners();
    header.setNoFill(true);
    header.setNoStroke(true);
    header.setAnchor(MTRectangle.PositionAnchor.CENTER);
    header.setPositionGlobal(new Vector3D(4 * this.mtApp.width / 8.0F, this.mtApp.height / 20.0F, 0.0F));

    getCanvas().addChild(scoresList);
    getCanvas().addChild(namesList);
    getCanvas().addChild(header);
  }

  private void createQuitButton()
  {
    MTImageButton quit = new MTImageButton(this.mtApp.loadImage(this.imagesPath + "closebutton.png"), this.mtApp);

    quit.setSizeXYGlobal(this.mtApp.width / 16, this.mtApp.width / 16);
    quit.setNoStroke(true);

    if (MT4jSettings.getInstance().isOpenGlMode()) {
      quit.setUseDirectGL(true);
    }

    quit.addGestureListener(TapProcessor.class, new IGestureEventListener()
    {
      public boolean processGestureEvent(MTGestureEvent ge) {
        TapEvent te = (TapEvent)ge;
        if (te.isTapped()) {
          HighScoresListScene.this.mtApp.invokeLater(new SceneChanger(HighScoresListScene.this.mtApp, HighScoresListScene.this.mtApp.getScene("Menu Scene")));
        }
        return true;
      }
    });
    quit.setPositionGlobal(new Vector3D(this.mtApp.width - quit.getWidthXY(TransformSpace.GLOBAL) / 2.0F, this.mtApp.height - quit.getHeightXY(TransformSpace.GLOBAL) / 2.0F));

    getCanvas().addChild(quit);
  }
}