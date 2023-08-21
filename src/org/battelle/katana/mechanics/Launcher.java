package org.battelle.katana.mechanics;
import java.util.Random;

import org.battelle.katana.object.IKatanaObject;
import org.mt4j.MTApplication;
import org.mt4j.util.math.Vector3D;

public class Launcher {
  private MTApplication mtApp;

  public Launcher(MTApplication mtApp) {
    this.mtApp = mtApp;
  }

  /**
   * Adds an ILaunchable object onto the canvas and passes it and it's velocity to the physics engine
   * 
   * @param objectToLaunch The ILaunchable object to add to the screen
   */
  public IKatanaObject fire(IKatanaObject objectToLaunch) {

    // Instantiate the variables used in Position and Velocity
    Random random = new Random();
    int halfWidth = mtApp.width / 2;
    int height = mtApp.height;

    // This randomizes whether they fruit will come from the second half
    // of the screen or not
    if (random.nextInt(2) % 2 == 0) {
      // X velocity is negative here to move the fruit from the right
      // to the left
      objectToLaunch.setVelocity(new Vector3D(-random.nextInt(mtApp.width / 7), -random.nextInt(2 * height / 3) - (height / 1.2f), 0));
      objectToLaunch.setPosition(new Vector3D((random.nextInt((int) (.95 * halfWidth)) + halfWidth), height, 0));
    }
    else {
      // X velocity is positive here to move the fruit from the left
      // to the right
      objectToLaunch.setVelocity(new Vector3D(random.nextInt(mtApp.width / 7), -random.nextInt(2 * height / 3) - (height / 1.2f), 0));
      objectToLaunch.setPosition(new Vector3D((random.nextInt((int) (.95 * halfWidth))), height, 0));
    }
    // Set the sphere on the screen based on where it is supposed to
    // originate
    objectToLaunch.setPositionGlobal(objectToLaunch.getPosition());

    // Add the sphere to the canvas
    return objectToLaunch;

  }
}
