package org.battelle.katana.scenes;

import org.battelle.katana.mechanics.Cutter;
import org.battelle.katana.model.Mode;
import org.battelle.katana.object.KatanaObject;
import org.battelle.katana.object.KatanaObjectFactory;
import org.battelle.katana.object.ObjectType;
import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PGraphics;
import processing.core.PImage;

public class SelectionScene extends GameScene
{
  private MTApplication mtApp;
  private MTCanvas gameCanvas;
  private String imagesPath = "resources" + MTApplication.separator + "images" + MTApplication.separator + "misc" + MTApplication.separator;
  private KatanaObjectFactory katanaFac;
  private Cutter cutter;
  private KatanaObject gameStartButton;
  private KatanaObject highScoresButton;
  private SoundFactory soundFactory = SoundFactory.getInstance();
  private Mode gameMode;
  private boolean isVisible;
  private MTColor textColor;
  private MTColor trailColor;
  private MTRectangle spinCircle;
  private MTRectangle loadText;
  private SceneLoader loader;
  private boolean hasCut = false;

  public SelectionScene(MTApplication mtApplication, String name, Mode gameMode, KatanaObjectFactory kFac, MTColor textColor, MTColor trailColor)
  {
    super(mtApplication, name);

    this.mtApp = mtApplication;
    this.gameCanvas = getCanvas();
    this.gameMode = gameMode;
    this.textColor = textColor;
    this.katanaFac = kFac;
    this.trailColor = trailColor;

    setClearColor(new MTColor(0.0F, 0.0F, 0.0F));

    this.cutter = new Cutter(this.mtApp, getCanvas(), this.textColor, this.trailColor);
    this.gameCanvas.addChild(this.cutter);

    Vector3D gameStartPos = new Vector3D(this.mtApp.width / 3.0F, this.mtApp.height / 2.0F);
    Vector3D highScoresPos = new Vector3D(2 * this.mtApp.width / 3.0F, this.mtApp.height / 2.0F);

    this.gameStartButton = this.katanaFac.create(ObjectType.RANDOMFRUIT);
    AbstractShape gameStartSphere = (AbstractShape)this.gameStartButton.getComponent();
    gameStartSphere.setPositionGlobal(gameStartPos);
    gameStartSphere.removeAllGestureEventListeners();
    this.gameCanvas.addChild(gameStartSphere);

    this.highScoresButton = this.katanaFac.create(ObjectType.MULTIPLIER);
    AbstractShape highScoresCube = (AbstractShape)this.highScoresButton.getComponent();
    highScoresCube.setPositionGlobal(highScoresPos);
    highScoresCube.removeAllGestureEventListeners();
    this.gameCanvas.addChild(highScoresCube);

    MTTextArea gameStartText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 40));
    gameStartText.setNoFill(true);
    gameStartText.setNoStroke(true);
    gameStartText.setText("Start Game");
    gameStartText.removeAllGestureEventListeners();
    this.gameCanvas.addChild(gameStartText);
    gameStartText.setPositionGlobal(new Vector3D(this.gameStartButton.getCenterPointGlobal().getX(), this.gameStartButton.getCenterPointGlobal().getY() + this.gameStartButton.getRadius() * 1.5F, 0.0F));

    MTTextArea highScoresStartText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 40));
    highScoresStartText.setNoFill(true);
    highScoresStartText.setNoStroke(true);
    highScoresStartText.setText("High Scores");
    highScoresStartText.removeAllGestureEventListeners();
    this.gameCanvas.addChild(highScoresStartText);
    highScoresStartText.setPositionGlobal(new Vector3D(this.highScoresButton.getCenterPointGlobal().getX(), this.highScoresButton.getCenterPointGlobal().getY() + this.highScoresButton.getRadius() * 1.5F, 0.0F));

    createQuitButton();
  }

  public void onEnter()
  {
    super.init();
    this.isVisible = true;
    this.hasCut = false;
  }

  public void onLeave()
  {
    super.shutDown();
    this.isVisible = false;
  }

  private void createQuitButton()
  {
    MTImageButton quit = new MTImageButton(this.mtApp.loadImage(this.imagesPath + "closebutton.png"), this.mtApp);
    quit.setSizeXYGlobal(this.mtApp.width / 16, this.mtApp.width / 16);
    quit.setNoStroke(true);
    if (MT4jSettings.getInstance().isOpenGlMode()) {
      quit.setUseDirectGL(true);
    }
    quit.addGestureListener(TapProcessor.class, new IGestureEventListener() {
      public boolean processGestureEvent(MTGestureEvent ge) {
        TapEvent te = (TapEvent)ge;
        if (te.isTapped()) {
          SelectionScene.this.mtApp.invokeLater(new SceneChanger(SelectionScene.this.mtApp, SelectionScene.this.mtApp.getScene("Menu Scene")));
        }
        return true;
      }
    });
    quit.setPositionGlobal(new Vector3D(this.mtApp.width - quit.getWidthXY(TransformSpace.GLOBAL) / 2.0F, this.mtApp.height - quit.getHeightXY(TransformSpace.GLOBAL) / 2.0F));
    this.gameCanvas.addChild(quit);
  }

  public void drawAndUpdate(PGraphics graphics, long timeDelta)
  {
    if (this.isVisible) {
      if ((this.gameStartButton.getComponent() instanceof MTTriangleMesh)) {
        this.gameStartButton.getComponent().rotateX(this.gameStartButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
        this.gameStartButton.getComponent().rotateY(this.gameStartButton.getCenterPointLocal(), 1.0F, TransformSpace.LOCAL);
      }
      else {
        this.gameStartButton.getComponent().rotateZ(this.gameStartButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
      }

      if ((this.gameStartButton.getComponent() instanceof MTTriangleMesh)) {
        this.highScoresButton.getComponent().rotateX(this.highScoresButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
        this.highScoresButton.getComponent().rotateY(this.highScoresButton.getCenterPointLocal(), 1.0F, TransformSpace.LOCAL);
      }
      else {
        this.highScoresButton.getComponent().rotateZ(this.highScoresButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
      }

      if ((!this.hasCut) && (this.gameStartButton.checkValidCut(this.cutter)))
      {
        this.hasCut = true;
        this.soundFactory.generateSound(SoundType.HIT).start();

        PImage loadingTextureImage = new PImage();
        loadingTextureImage = getMTApplication().loadImage(this.imagesPath + "Loading.png");
        this.spinCircle = new MTRectangle(loadingTextureImage, getMTApplication());

        this.spinCircle.setHeightXYGlobal(getMTApplication().height / 6.0F);
        this.spinCircle.setWidthXYGlobal(getMTApplication().height / 6.0F);

        this.spinCircle.setNoStroke(true);
        this.spinCircle.setPositionGlobal(new Vector3D(getMTApplication().getWidth() / 2.0F, 3.0F * getMTApplication().getHeight() / 4.0F, 0.0F));

        getCanvas().addChild(this.spinCircle);

        PImage loadingTextImage = new PImage();
        loadingTextImage = getMTApplication().loadImage(this.imagesPath + "LoadingText.png");
        this.loadText = new MTRectangle(loadingTextImage, getMTApplication());

        this.loadText.setHeightXYGlobal(getMTApplication().height / 3.0F);
        this.loadText.setWidthXYGlobal(getMTApplication().height / 3.0F);

        this.loadText.setNoStroke(true);
        this.loadText.setPositionGlobal(new Vector3D(getMTApplication().width / 2.0F, 9.0F * getMTApplication().height / 10.0F));

        getCanvas().addChild(this.loadText);

        this.loader = new SceneLoader(this.mtApp, 0L, this.gameMode, this.katanaFac, this.textColor, this.trailColor);

        this.loader.start();
      }
      else if (this.highScoresButton.checkValidCut(this.cutter)) {
        this.soundFactory.generateSound(SoundType.HIT).start();
        this.mtApp.invokeLater(new SceneChanger(this.mtApp, new HighScoresListScene(this.mtApp, "High Scores List", new HighScoresList(this.gameMode), this.textColor)));
      }

      if (this.spinCircle != null) {
        this.spinCircle.rotateZ(this.spinCircle.getCenterPointGlobal(), 3.0F);

        if ((this.loader != null) && (this.loader.isFinished())) {
          this.spinCircle.destroy();
          this.loadText.destroy();
        }
      }

      super.drawAndUpdate(graphics, timeDelta);
    }
  }
}