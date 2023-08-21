package org.battelle.katana.scenes;

import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.Iscene;


public class SceneChanger
  implements Runnable
{
  private MTApplication mtApp;
  private Iscene nextScene;

  SceneChanger(MTApplication mtApp, Iscene menu)
  {
    this.mtApp = mtApp;
    this.nextScene = menu;
  }

  public void run()
  {
    Iscene originalScene = this.mtApp.getCurrentScene();
    this.mtApp.changeScene(this.nextScene);
    for (Iscene scene : this.mtApp.getScenes())
    {
      if ((scene == this.nextScene) || (scene == originalScene) || (scene.getName().equals("Menu Scene")) || (scene.getName().toLowerCase().contains("selection")))
        continue;
      scene.destroy();
    }
  }
}