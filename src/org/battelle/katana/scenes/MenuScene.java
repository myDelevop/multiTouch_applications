package org.battelle.katana.scenes;

import java.util.ArrayList;

import org.battelle.katana.mechanics.Cutter;
import org.battelle.katana.mechanics.Launcher;
import org.battelle.katana.mechanics.Timer;
import org.battelle.katana.model.GameModel;
import org.battelle.katana.model.Mode;
import org.battelle.katana.object.KatanaObject;
import org.battelle.katana.object.KatanaObjectFactory;
import org.battelle.katana.object.ObjectType;
import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PGraphics;

public class MenuScene extends GameScene
{
  private MTApplication mtApp;
  private GameModel model;
  private Launcher launcher;
  private KatanaObjectFactory katanaFac;
  private MTCanvas gameCanvas;
  private KatanaObject arcadeButton;
  private KatanaObject practiceButton;
  private KatanaObject exitButton;
  private KatanaObject suddenDeathButton;
  private Cutter cutter;
  private SoundFactory soundFactory = SoundFactory.getInstance();
  private boolean isVisible;
  private boolean arcadeTwoD = true;
  private boolean practiceTwoD = true;
  private boolean deathTwoD = true;
  private boolean exitTwoD = true;
  private MTColor textColor;
  private MTColor trailColor;

  public MenuScene(MTApplication mtApplication, String name, ArrayList<MTComponent> fruitComponents, MTComponent bombComponent, MTComponent powerupComponent, MTColor textColor, MTColor trailColor)
  {
    super(mtApplication, name);
    this.mtApp = mtApplication;

    this.textColor = textColor;
    this.trailColor = trailColor;

    this.gameCanvas = getCanvas();

    this.model = new GameModel(Mode.ARCADE);

    setClearColor(new MTColor(0.0F, 0.0F, 0.0F));

    this.cutter = new Cutter(this.mtApp, getCanvas(), this.textColor, this.trailColor);
    getCanvas().addChild(this.cutter);

    this.katanaFac = new KatanaObjectFactory(this.mtApp, this.model, this.launcher, new Timer(), fruitComponents, bombComponent, powerupComponent);

    createFruitMenu();
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

  private void createFruitMenu()
  {
    Vector3D arcadeStartPos = new Vector3D(this.mtApp.width / 4.0F, this.mtApp.height / 2.0F);
    Vector3D practiceStartPos = new Vector3D(this.mtApp.width / 2.0F, this.mtApp.height / 4.0F);
    Vector3D suddenDeathStartPos = new Vector3D(3 * this.mtApp.width / 4.0F, this.mtApp.height / 2.0F);
    Vector3D quitPos = new Vector3D(this.mtApp.width / 2.0F, 3 * this.mtApp.height / 4.0F);

    this.arcadeButton = this.katanaFac.create(ObjectType.FRUIT_ONE);
    AbstractShape arcadeButtonPoly = (AbstractShape)this.arcadeButton.getComponent();
    arcadeButtonPoly.setNoStroke(true);
    arcadeButtonPoly.setPositionGlobal(arcadeStartPos);
    arcadeButtonPoly.removeAllGestureEventListeners();
    this.gameCanvas.addChild(arcadeButtonPoly);

    if ((arcadeButtonPoly instanceof MTTriangleMesh)) {
      this.arcadeTwoD = false;
    }

    this.practiceButton = this.katanaFac.create(ObjectType.FRUIT_TWO);
    AbstractShape practiceButtonPoly = (AbstractShape)this.practiceButton.getComponent();
    practiceButtonPoly.setNoStroke(true);
    practiceButtonPoly.setPositionGlobal(practiceStartPos);
    practiceButtonPoly.removeAllGestureEventListeners();
    this.gameCanvas.addChild(practiceButtonPoly);

    if ((practiceButtonPoly instanceof MTTriangleMesh)) {
      this.practiceTwoD = false;
    }

    this.suddenDeathButton = this.katanaFac.create(ObjectType.FRUIT_THREE);
    AbstractShape suddenDeathButtonPoly = (AbstractShape)this.suddenDeathButton.getComponent();
    suddenDeathButtonPoly.setNoStroke(true);
    suddenDeathButtonPoly.setPositionGlobal(suddenDeathStartPos);
    suddenDeathButtonPoly.removeAllGestureEventListeners();

    this.gameCanvas.addChild(suddenDeathButtonPoly);

    if ((suddenDeathButtonPoly instanceof MTTriangleMesh)) {
      this.deathTwoD = false;
    }

    MTTextArea arcadeStartText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 40));
    arcadeStartText.setNoFill(true);
    arcadeStartText.setNoStroke(true);
    arcadeStartText.setText("Arcade");
    arcadeStartText.removeAllGestureEventListeners();
    this.gameCanvas.addChild(arcadeStartText);
    arcadeStartText.setPositionGlobal(new Vector3D(this.arcadeButton.getCenterPointGlobal().getX(), this.arcadeButton.getCenterPointGlobal().getY() + this.arcadeButton.getRadius() * 1.5F, 0.0F));

    MTTextArea practiceStartText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 40));
    practiceStartText.setNoFill(true);
    practiceStartText.setNoStroke(true);
    practiceStartText.setText("Practice");
    practiceStartText.removeAllGestureEventListeners();
    this.gameCanvas.addChild(practiceStartText);
    practiceStartText.setPositionGlobal(new Vector3D(this.practiceButton.getCenterPointGlobal().getX(), this.practiceButton.getCenterPointGlobal().getY() + this.practiceButton.getRadius() * 1.5F, 0.0F));

    MTTextArea suddenDeathStartText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 40));
    suddenDeathStartText.setNoFill(true);
    suddenDeathStartText.setNoStroke(true);
    suddenDeathStartText.setText("Sudden Death");
    suddenDeathStartText.removeAllGestureEventListeners();
    this.gameCanvas.addChild(suddenDeathStartText);
    suddenDeathStartText.setPositionGlobal(new Vector3D(this.suddenDeathButton.getCenterPointGlobal().getX(), this.suddenDeathButton.getCenterPointGlobal().getY() + this.suddenDeathButton.getRadius() * 1.5F, 0.0F));

    this.exitButton = this.katanaFac.create(ObjectType.ARCADEBOMB);
    AbstractShape buttonTwoPoly = (AbstractShape)this.exitButton.getComponent();
    buttonTwoPoly.setNoStroke(true);
    buttonTwoPoly.setPositionGlobal(quitPos);
    buttonTwoPoly.removeAllGestureEventListeners();
    this.gameCanvas.addChild(buttonTwoPoly);

    if ((buttonTwoPoly instanceof MTTriangleMesh)) {
      this.exitTwoD = false;
    }

    MTTextArea endText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial", this.mtApp.height / 40));
    endText.setNoFill(true);
    endText.setNoStroke(true);
    endText.setText("Exit");
    endText.removeAllGestureEventListeners();
    getCanvas().addChild(endText);
    endText.setPositionGlobal(new Vector3D(new Vector3D(this.exitButton.getCenterPointGlobal().getX(), this.exitButton.getCenterPointGlobal().getY() + this.exitButton.getRadius() * 1.5F, 0.0F)));
  }

  public void drawAndUpdate(PGraphics graphics, long timeDelta)
  {
    if (this.isVisible) {
      if (this.arcadeTwoD) {
        this.arcadeButton.getComponent().rotateZ(this.arcadeButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
      }
      else {
        this.arcadeButton.getComponent().rotateX(this.arcadeButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
        this.arcadeButton.getComponent().rotateY(this.arcadeButton.getCenterPointLocal(), 1.0F, TransformSpace.LOCAL);
      }

      if (this.practiceTwoD) {
        this.practiceButton.getComponent().rotateZ(this.practiceButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
      }
      else {
        this.practiceButton.getComponent().rotateX(this.practiceButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
        this.practiceButton.getComponent().rotateY(this.practiceButton.getCenterPointLocal(), 1.0F, TransformSpace.LOCAL);
      }

      if (this.exitTwoD) {
        this.exitButton.getComponent().rotateZ(this.exitButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
      }
      else {
        this.exitButton.getComponent().rotateX(this.exitButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
        this.exitButton.getComponent().rotateY(this.exitButton.getCenterPointLocal(), 1.0F, TransformSpace.LOCAL);
      }

      if (this.deathTwoD) {
        this.suddenDeathButton.getComponent().rotateZ(this.suddenDeathButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
      }
      else {
        this.suddenDeathButton.getComponent().rotateX(this.suddenDeathButton.getCenterPointLocal(), 2.0F, TransformSpace.LOCAL);
        this.suddenDeathButton.getComponent().rotateY(this.suddenDeathButton.getCenterPointLocal(), 1.0F, TransformSpace.LOCAL);
      }

      if (this.arcadeButton.checkValidCut(this.cutter))
      {
        this.soundFactory.generateSound(SoundType.HIT).start();
        boolean sceneExists = false;
        for (Iscene scene : this.mtApp.getScenes()) {
          if (scene.getName().equals("Arcade Selection Scene")) {
            sceneExists = true;
          }
        }

        if (sceneExists) {
          this.mtApp.invokeLater(new SceneChanger(this.mtApp, this.mtApp.getScene("Arcade Selection Scene")));
        }
        else {
          this.mtApp.invokeLater(new SceneChanger(this.mtApp, new SelectionScene((MTApplication)this.mtApp, "Arcade Selection Scene", Mode.ARCADE, this.katanaFac, this.textColor, this.trailColor)));
        }

      }
      else if (this.practiceButton.checkValidCut(this.cutter))
      {
        this.soundFactory.generateSound(SoundType.HIT).start();

        this.mtApp.invokeLater(new SceneChanger(this.mtApp, new SaladSamuraiScene((MTApplication)this.mtApp, "Salad Samurai Scene", Mode.PRACTICE, this.katanaFac, this.textColor, this.trailColor)));
      }
      else if (this.suddenDeathButton.checkValidCut(this.cutter))
      {
        this.soundFactory.generateSound(SoundType.HIT).start();
        boolean sceneExists = false;
        for (Iscene scene : this.mtApp.getScenes()) {
          if (scene.getName().equals("Sudden Death Selection Scene")) {
            sceneExists = true;
          }
        }

        if (sceneExists) {
          this.mtApp.invokeLater(new SceneChanger(this.mtApp, this.mtApp.getScene("Sudden Death Selection Scene")));
        }
        else {
          this.mtApp.invokeLater(new SceneChanger(this.mtApp, new SelectionScene((MTApplication)this.mtApp, "Sudden Death Selection Scene", Mode.SUDDEN_DEATH, this.katanaFac, this.textColor, this.trailColor)));
        }

      }
      else if (this.exitButton.checkValidCut(this.cutter))
      {
        this.soundFactory.generateSound(SoundType.HIT).start();
        System.exit(0);
      }
      super.drawAndUpdate(graphics, timeDelta);
    }
  }
}