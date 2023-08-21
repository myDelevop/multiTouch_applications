package org.battelle.katana.mechanics;
import org.battelle.katana.object.IKatanaObject;
import org.mt4j.MTApplication;
import org.mt4j.util.math.Vector3D;


public class Physics {
  
  private float acceleration;
  private Vector3D currentPos = new Vector3D();

  public Physics(MTApplication mtApp) {
    // Placeholder until we figure out an equation to get the acceleration
    // based on the screen size.
    acceleration = (float) (mtApp.height);
  }

  /**
   * Processes the current position and velocity for each launchedItem passed to it
   * 
   * @param launchedItem the item that will be affected by the physics engine
   * @param time the elapsed time
   */
  public void doPhysicsStep(IKatanaObject launchedItem, float time) {
    // Create the vector to store the generated position

    float xInitVel = launchedItem.getVelocity().getX();
    float yInitVel = launchedItem.getVelocity().getY();
    float xInitPos = launchedItem.getPosition().getX();
    float yInitPos = launchedItem.getPosition().getY();

    // Get the x position
    float xPos = xInitPos + xInitVel * time;

    // Get the Y position
    float yPos = (float) (yInitPos + yInitVel * time + .5f * acceleration * Math.pow(time, 2f));
    
    // Set the positions in the vector being returned
    currentPos.setXYZ(xPos, yPos, 0);
    launchedItem.setCurrentPosition(currentPos);
    launchedItem.setCurrentVelocity(new Vector3D(xInitVel, yInitVel + acceleration * time, 0));
    
  }
}