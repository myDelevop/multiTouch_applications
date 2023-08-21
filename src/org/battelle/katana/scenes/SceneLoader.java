package org.battelle.katana.scenes;

import org.battelle.katana.model.Mode;
import org.battelle.katana.object.KatanaObjectFactory;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.progressBar.AbstractProgressThread;
import org.mt4j.util.MTColor;

public class SceneLoader extends AbstractProgressThread
{
  private MTApplication mtApp;
  private boolean isClicked = false;
  private long sleepTime;
  private Mode gameMode;
  private KatanaObjectFactory katanaFac;
  private MTColor textColor;
  private MTColor trailColor;
  private SaladSamuraiScene gameScene;

  public SceneLoader(MTApplication mtApp, long sleepTime, Mode gameMode, KatanaObjectFactory katanaFac, MTColor textColor, MTColor trailColor)
  {
    super(sleepTime);
    this.mtApp = mtApp;
    this.sleepTime = sleepTime;
    this.gameMode = gameMode;
    this.katanaFac = katanaFac;
    this.textColor = textColor;
    this.trailColor = trailColor;
  }

  public boolean isClicked()
  {
    return this.isClicked;
  }

  public void setClicked(boolean clicked)
  {
    this.isClicked = clicked;
  }

  public void run()
  {
    try
    {
      Thread.sleep(getSleepTime());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    this.gameScene = new SaladSamuraiScene(this.mtApp, "Salad Samurai Scene", this.gameMode, this.katanaFac, this.textColor, this.trailColor);

    this.mtApp.invokeLater(new SceneChanger(this.mtApp, this.gameScene));

    setFinished(true);
    setCurrent(1.0F);
  }

  public SaladSamuraiScene getGameScene() {
    return this.gameScene;
  }
}