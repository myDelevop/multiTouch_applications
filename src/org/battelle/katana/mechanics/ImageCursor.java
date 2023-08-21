package org.battelle.katana.mechanics;
import org.mt4j.MTApplication;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;



public class ImageCursor extends CursorTracer
{  
  private PImage textureImage;
  private MTApplication mtApp;
  
  public ImageCursor(MTApplication mtApp, Iscene currentScene, PImage textureImage)
  {
    super(mtApp, currentScene);
    this.textureImage = textureImage;
    this.mtApp = mtApp;
  }
  
  @Override
  protected AbstractShape createDisplayComponent(PApplet applet, Vector3D position)
  {
    MTRectangle displayShape = new CursorImage(applet, textureImage);
    displayShape.setPositionGlobal(position);
    displayShape.setPickable(false);
    displayShape.setDrawSmooth(true);
    displayShape.setSizeXYGlobal(mtApp.width/10, mtApp.width/10);
    displayShape.setNoStroke(true);
    return displayShape;
  }

  private class CursorImage extends MTRectangle
  {
    public CursorImage(PApplet mtApp, PImage textureImage)
    {
      super(textureImage, mtApp);
    }

    @Override
    protected IBoundingShape computeDefaultBounds()
    {
      return null;
    }

    @Override
    protected void setDefaultGestureActions()
    {
      // Dont need gestures
    }
  }
}
