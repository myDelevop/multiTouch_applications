package org.battelle.katana.scenes;

import java.util.ArrayList;

import org.battelle.katana.mechanics.Timer;
import org.battelle.katana.model.Mode;
import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.font.IFontCharacter;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKey;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTTextKeyboard;
import org.mt4j.sceneManagement.transition.BlendTransition;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PGraphics;

public class HighScoresKeyboardScene extends GameScene
{
  private MTKeyboard kb;
  private MTApplication mtApp;
  private IFont arialWhite;
  private IFont arialWhiteBig;
  private IFont arialWhiteSmall;
  private IFont bonusPointsFont;
  private Timer timer = new Timer();
  private ArrayList<Integer> values;
  private boolean scoreDisplayed;
  private boolean consecutiveCutsDisplayed;
  private boolean totalFruitCutDisplayed;
  private boolean doneDisplayingTotalScore;
  private boolean drumRollUsed;
  private Mode gameMode;
  private MTTextArea userNameField;
  private int displayedScore;
  private int consCutBonus;
  private int totalCutBonus;
  private int totalScore;
  private MTTextArea totalScoreBox;
  private SoundFactory soundFactory = SoundFactory.getInstance();

  private Thread drumRoll = this.soundFactory.generateSound(SoundType.SCORE);
  private boolean isVisible;
  private MTColor textColor;

  public HighScoresKeyboardScene(MTApplication mtApplication, String name, ArrayList<Integer> values, Mode gameMode, MTColor textColor)
  {
    super(mtApplication, name);
    this.mtApp = mtApplication;
    this.gameMode = gameMode;
    this.textColor = textColor;

    this.arialWhite = FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 15);
    this.arialWhiteBig = FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 13);
    this.arialWhiteSmall = FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 30);
    this.bonusPointsFont = FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 15);

    this.values = values;

    initializeTotalScoreBox();

    initializeKeyboard();

    this.timer.start();
  }

  public void onEnter()
  {
    super.init();
    this.isVisible = true;
  }

  public void onLeave()
  {
    super.shutDown();
    this.isVisible = false;
  }

  public void initializeTotalScoreBox()
  {
    this.consCutBonus = (((Integer)this.values.get(1)).intValue() * 100);
    this.totalCutBonus = (((Integer)this.values.get(2)).intValue() * 50);
    this.totalScore = (((Integer)this.values.get(0)).intValue() + this.consCutBonus + this.totalCutBonus);

    this.totalScoreBox = new MTTextArea(this.mtApp, this.arialWhite);
    this.totalScoreBox.setNoFill(true);
    this.totalScoreBox.setNoStroke(true);
    this.totalScoreBox.setPositionGlobal(new Vector3D(this.mtApp.width / 8.0F, 2.0F * this.mtApp.height / 5.0F, 0.0F));
    this.totalScoreBox.removeAllGestureEventListeners();
  }

  public void initializeKeyboard()
  {
    this.kb = new MTTextKeyboard(this.mtApp)
    {
      protected void onKeyboardButtonClicked(MTKey clickedkey, boolean shiftPressed) {
        if (clickedkey.getCharacterToWrite().equals("\n")) {
          HighScoresKeyboardScene.this.userNameField.removeLastCharacter();
          HighScoresKeyboardScene.this.onNameAccepted();
        }
        //super.keyboardButtonClicked(clickedkey, shiftPressed);
      }

      protected void closeKeyboard()
      {
        HighScoresKeyboardScene.this.onNameAccepted();
      }
    };
    this.kb.setFillColor(new MTColor(30.0F, 30.0F, 30.0F, 210.0F));
    this.kb.setStrokeColor(new MTColor(0.0F, 0.0F, 0.0F, 255.0F));

    this.userNameField = new MTTextArea(this.mtApp, this.arialWhiteBig)
    {
      protected void characterAdded(IFontCharacter character) {
        if (!character.getUnicode().equalsIgnoreCase("\n"))
          super.characterAdded(character);
      }
    };
    this.userNameField.setNoFill(true);
    this.userNameField.setNoStroke(true);
    this.userNameField.unregisterAllInputProcessors();
    this.userNameField.setEnableCaret(true);

    this.userNameField.snapToKeyboard(this.kb);
    this.kb.addTextInputListener(this.userNameField);
    this.kb.setPositionGlobal(new Vector3D(this.mtApp.width / 2.0F, 4 * this.mtApp.height / 5.0F, 0.0F));

    this.kb.removeAllGestureEventListeners();
  }

  public void displayScore() {
    if (!this.scoreDisplayed)
    {
      MTTextArea score = new MTTextArea(this.mtApp, this.arialWhite);
      score.removeAllGestureEventListeners();

      score.setNoFill(true);
      score.setNoStroke(true);
      score.setPositionGlobal(new Vector3D(this.mtApp.width / 8.0F, 0.5F * this.mtApp.height / 5.0F, 0.0F));
      score.setText("Score: " + this.values.get(0));

      getCanvas().addChild(score);

      this.soundFactory.generateSound(SoundType.HIT).start();
    }
  }

  public void displayConsecutiveCuts() {
    if (!this.consecutiveCutsDisplayed)
    {
      MTTextArea consCuts = new MTTextArea(this.mtApp, this.arialWhite);
      consCuts.removeAllGestureEventListeners();

      consCuts.setNoFill(true);
      consCuts.setNoStroke(true);
      consCuts.setPositionGlobal(new Vector3D(this.mtApp.width / 8.0F, this.mtApp.height / 5.0F, 0.0F));
      consCuts.setText("Max Consecutive Cuts: " + this.values.get(1));

      getCanvas().addChild(consCuts);

      MTTextArea consCutBonusBox = new MTTextArea(this.mtApp, this.bonusPointsFont);
      consCutBonusBox.removeAllGestureEventListeners();

      consCutBonusBox.setNoFill(true);
      consCutBonusBox.setNoStroke(true);

      consCuts.setAnchor(MTRectangle.PositionAnchor.LOWER_RIGHT);
      float bonusX = consCuts.getPosition(TransformSpace.GLOBAL).getX() + this.mtApp.width / 50;
      consCuts.setAnchor(MTRectangle.PositionAnchor.CENTER);
      float bonusY = consCuts.getPosition(TransformSpace.GLOBAL).getY();

      consCutBonusBox.setPositionGlobal(new Vector3D(bonusX, bonusY, 0.0F));
      consCutBonusBox.setText("+ " + this.consCutBonus);

      getCanvas().addChild(consCutBonusBox);

      this.soundFactory.generateSound(SoundType.HIT).start();
    }
  }

  public void displayTotalFruitCut() {
    if (!this.totalFruitCutDisplayed)
    {
      MTTextArea totalCut = new MTTextArea(this.mtApp, this.arialWhite);
      totalCut.removeAllGestureEventListeners();

      totalCut.setNoFill(true);
      totalCut.setNoStroke(true);
      totalCut.setPositionGlobal(new Vector3D(this.mtApp.width / 8.0F, 1.5F * this.mtApp.height / 5.0F, 0.0F));
      totalCut.setText("Total Fruits Cut: " + this.values.get(2));

      getCanvas().addChild(totalCut);

      MTTextArea totalCutBonusBox = new MTTextArea(this.mtApp, this.bonusPointsFont);
      totalCutBonusBox.removeAllGestureEventListeners();

      totalCutBonusBox.setNoFill(true);
      totalCutBonusBox.setNoStroke(true);

      totalCut.setAnchor(MTRectangle.PositionAnchor.LOWER_RIGHT);
      float bonusX = totalCut.getPosition(TransformSpace.GLOBAL).getX() + this.mtApp.width / 50;
      totalCut.setAnchor(MTRectangle.PositionAnchor.CENTER);
      float bonusY = totalCut.getPosition(TransformSpace.GLOBAL).getY();

      totalCutBonusBox.setPositionGlobal(new Vector3D(bonusX, bonusY, 0.0F));
      totalCutBonusBox.setText("+ " + this.totalCutBonus);

      getCanvas().addChild(totalCutBonusBox);

      this.soundFactory.generateSound(SoundType.HIT).start();
    }
  }

  private void displayTotalScore()
  {
    this.totalScoreBox.setText("Total Score: " + this.displayedScore);
    getCanvas().addChild(this.totalScoreBox);
  }

  private void displayEntryPrompt()
  {
    MTTextArea enterYourName = new MTTextArea(this.mtApp, this.arialWhiteSmall);
    enterYourName.removeAllGestureEventListeners();
    enterYourName.setNoFill(true);
    enterYourName.setNoStroke(true);
    enterYourName.setPositionGlobal(new Vector3D(this.mtApp.width / 2.5F, 2.6F * this.mtApp.height / 5.0F, 0.0F));
    enterYourName.setText("Enter Your Name Here:");
    getCanvas().addChild(enterYourName);
    getCanvas().addChild(this.kb);
  }

  public void drawAndUpdate(PGraphics graphics, long timeDelta)
  {
    super.drawAndUpdate(graphics, timeDelta);
    if (this.isVisible)
    {
      if ((this.timer != null) && (this.timer.getElapsedTimeSecs() > 0.5D)) {
        displayScore();
        this.timer.stop();
        this.scoreDisplayed = true;
      }

      if ((this.timer != null) && (this.timer.getElapsedTimeSecs() > 1.5D)) {
        displayConsecutiveCuts();
        this.timer.stop();
        this.consecutiveCutsDisplayed = true;
      }

      if ((this.timer != null) && (this.timer.getElapsedTimeSecs() > 2.5D)) {
        displayTotalFruitCut();
        this.timer.stop();
        this.totalFruitCutDisplayed = true;
      }

      if ((this.timer != null) && (this.timer.getElapsedTimeSecs() > 3.5D)) {
        if ((!this.drumRollUsed) && (this.totalScore > 0)) {
          this.drumRoll.start();
          this.drumRollUsed = true;
        }
        if (this.displayedScore < this.totalScore)
        {
          this.displayedScore += this.totalScore / 190;
          if (this.displayedScore >= this.totalScore) {
            this.displayedScore = this.totalScore;
            this.doneDisplayingTotalScore = true;
          }
          displayTotalScore();
        }
        else if (this.totalScore <= 0) {
          this.displayedScore = this.totalScore;
          displayTotalScore();
          this.soundFactory.generateSound(SoundType.HIT).start();
          this.doneDisplayingTotalScore = true;
        }
      }
      ((AbstractShape) this.bonusPointsFont).setFillColor(MTColor.randomColor());

      if ((this.timer != null) && (this.doneDisplayingTotalScore)) {
        displayEntryPrompt();

        this.timer = null;
      }
    }
  }

  public void onNameAccepted() {
    HighScoresList hs = new HighScoresList(this.gameMode);
    hs.addScore(this.totalScore, this.userNameField.getText());

    if ((MT4jSettings.getInstance().isOpenGlMode()) && (GLFBO.isSupported(this.mtApp)))
      setTransition(new BlendTransition(this.mtApp, 750));
    else {
      setTransition(new FadeTransition(this.mtApp, 750L));
    }

    this.mtApp.invokeLater(new SceneChanger(this.mtApp, new HighScoresListScene(this.mtApp, "High Scores List", hs, this.textColor)));
  }
}