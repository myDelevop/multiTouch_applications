package org.battelle.katana.scenes;

import java.io.File;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;


public class GameScene extends AbstractScene
{
  private final String backgroundPath = "resources" + MTApplication.separator + "images" + MTApplication.separator + "background" + MTApplication.separator;
  private MTApplication mtApp;
  private static MTBackgroundImage bkg;

  public GameScene(MTApplication mtApplication, String name)
  {
    super(mtApplication, name);
    this.mtApp = mtApplication;

    if (bkg == null) {
      try {
        File backgroundFolder = new File(this.backgroundPath);

        MTBackgroundImage bkgImg = new MTBackgroundImage(this.mtApp, this.mtApp.loadImage(this.backgroundPath + backgroundFolder.list()[0]), false);
        bkgImg.setPositionGlobal(new Vector3D(this.mtApp.width / 2, this.mtApp.height / 2, -this.mtApp.height / 5));
        bkgImg.scale(1.25F, 1.25F, 1.0F, bkgImg.getCenterPointGlobal());
        getCanvas().addChild(bkgImg);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else
    {
      getCanvas().addChild(bkg);
    }
  }

@Override
public void init()
{
	// TODO Auto-generated method stub
	
}

@Override
public void shutDown()
{
	// TODO Auto-generated method stub
	
}
}