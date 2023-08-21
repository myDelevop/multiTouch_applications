package org.battelle.katana.mechanics;

import org.mt4j.MTApplication;
import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import processing.core.PImage;

public class BackgroundImage extends MTRectangle
{

  public BackgroundImage(MTApplication mtApp, PImage bgImage) {
    super(bgImage, mtApp);

    //Sets the background image texture
    this.setTexture(bgImage);

   /* this.setAnchor(PositionAnchor.UPPER_LEFT);
    this.setWidthLocal(mtApp.width);
    this.setHeightLocal(mtApp.height);
    this.setPositionGlobal(new Vector3D());*/
    this.setNoStroke(true);
    this.setPickable(false);
    this.removeAllChildren();
    this.removeAllGestureEventListeners();
  }
  
  @Override
  protected void setDefaultGestureActions() {
    //register no gesture processors
  }
  
  @Override
  protected IBoundingShape computeDefaultBounds() {
    return  new BoundsZPlaneRectangle(this);
  }
  
}
