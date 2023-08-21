package org.battelle.katana.object;
import org.battelle.katana.mechanics.Cutter;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

/**
 * 
 * 
 * @author CYBER-100
 *
 */
public interface IKatanaObject
{
  /** Start ICuttable */
  
  /**
   * Checks whether or not a cutter object is cutting through any of the
   * fruits on the screen
   * 
   * @param cutter
   *            The cutter object we wish to check against all fruits on the
   *            screen
   * @return True iff the cut is determined as valid by the checking
   *         algorithm, false otherwise
   */
  public boolean checkValidCut(Cutter cutter);

  /**
   * Updates the combocount, score, and fruitcounter, and also plays any cut
   * sounds that are appropriate as well as starts the procedure to let two
   * halves fall and destroys the current sphere we cut through.
   */
  public abstract void performOnCut();
  
  /** End ICuttable */
  
  
  
  
  /** Start ILaunchable */
  /**
   * Gets the velocity of our Launchable object in the form of a Vector3D
   * @return The velocity of our object
   */
  public Vector3D getVelocity();
  
  /**
   * Sets the velocity of our Launchable object in the form of a Vector3D
   * @param velocity The velocity we wish to set our object to
   */
  public void setVelocity(Vector3D velocity);
  
  /**
   * Accessor for the velocity of the launchable
   * @return The Vector3D of the launchable
   */
  public Vector3D getCurrentVelocity();

  /**
   * Mutator for the velocity of the launchable
   * @param currentVelocity The new Vecotr3D representing the desired velocity
   */
  public void setCurrentVelocity(Vector3D currentVelocity);
  
  /**
   * Gets the position of our Launchable object in the current canvas
   * @return The current position of our object
   */
  public Vector3D getPosition();
  
  /**
   * Sets the position of our Launchable object in the current canvas
   * @param position The position we wish to set our object to
   */
  public void setPosition(Vector3D position);

  /**
   * Accessor for the current position of the launchable
   * @return The Vector3D representing the current position
   */
  public Vector3D getCurrentPosition();
  
  /** 
   * Mutator for the current position of the launchable
   * @param currentPosition the Vector3D of the desired position
   */
  public void setCurrentPosition(Vector3D currentPosition);
  
  /**
   * Destroys all relevant components of the ILaunchable object
   */
  public void destroy();
  
  /**
   * Sends the relevant information to the Launcher for firing
   * @return An IKatanaObject with a velocity and initial position.
   */
  public IKatanaObject launch();
  
  /**
   * Gets the MTSphere or MTCube associated with this ILaunchable
   * @return The component associated with the ILaunchable that appears on screen
   */
  public MTComponent getComponent();
  
  /**
   * Sets the MTSphere or MTCube associated with this ILaunchable
   * @param component The component associated with the ILaunchable that appears on screen
   */
  public void setComponent(MTComponent component);
  
  /** End ILaunchable */
  
  
  /** Start remaining IKatanaObject methods */
  
  public void rotateX(Vector3D rotationPoint, float degrees, TransformSpace transformSpace);
  
  public void rotateY(Vector3D rotationPoint, float degrees, TransformSpace transformSpace);
  
  public Vector3D getCenterPointLocal();
  
  public void setCenterPointLocal(Vector3D centerPointLocal);
  
  public Vector3D getCenterPointGlobal();
  
  public void setCenterPointGlobal(Vector3D centerPointGlobal);
  
  public Vector3D getPositionGlobal();
  
  public void setPositionGlobal(Vector3D positionGlobal);
  
  public void setTexture(PImage texture);
  
  public PImage getTexture();
  
  /** End remaining IKatanaObject methods */

}
