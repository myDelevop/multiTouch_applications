package org.battelle.katana.mechanics;

import java.util.ArrayList;

import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class Cut {
  /** The current number of points added */
  private int nPoints = -1;
  /** The path that the line follows in a Vector3D array */
  private ArrayList<Vector3D> path;
  /** The distance to the last point in the path from the current point NOT PRIVATE */
  float distance;
  /** The color of the line */
  private MTColor color;

  /**
   * The default constructor that initializes the ArrayList of points
   */
  public Cut() {
    path = new ArrayList<Vector3D>();
  }

  /**
   * Adds a point to the path
   * 
   * @param point The new Point to be added to the ArrayList of points
   */
  public boolean addPoint(Vector3D point) {
    // Add the vector to the array
    path.add(point);
    nPoints++;

    // Update the distance if the there is more than one point on the cut
    if (nPoints > 1) {
      distance = Vector3D.distance(path.get(nPoints - 1), path.get(0));
    }

    return true;
  }

  /**
   * Calculates the distance from the current point to the last point
   * 
   * @return Distance from last point
   * @param vector - the current vector to be checked
   */
  public float distFromLast(Vector3D vector) {
    if (nPoints > 0) {
      Vector3D v = path.get(nPoints - 1);
      return Vector3D.distance(v, vector);
    }
    return 20;
  }

  /**
   * Gets the last point added to the cut
   * 
   * @return The Vector3D that was last added to the cut
   */
  public Vector3D getLast() {
    if (nPoints > 0) {
      return path.get(nPoints - 1);
    }
    // A point that could never get touched
    return new Vector3D(-10000, -10000, -10000);
  }
  
  /**
   * Gets change in x
   * 
   * @return deltaX
   */
  public float getDeltaX() {
	  if (nPoints > 0) {
		  return path.get(nPoints).getX() - path.get(nPoints - 1).getX();
	  }
	  
	  return 0;
  }
  
  /**
   * Gets change in y
   * 
   * @return deltaY
   */
  public float getDeltaY() {
	  if (nPoints > 0) {
		  return path.get(nPoints).getY() - path.get(nPoints - 1).getY();
	  }
	  
	  return 0;
  }

  /**
   * Gets the point at the offset from the last point added to the path array
   * 
   * @param offset the offset from the last point
   * @return the point
   */
  public Vector3D getOffsetFromLast(int offset) {
    if (nPoints > offset) {
      return path.get(nPoints - 1 - offset);
    }
    else if (nPoints > 0) {
      return path.get(nPoints - 1);
    }
    return new Vector3D();
  }

  /**
   * Clears the point array
   */
  public void clear() {
    path.clear();
    nPoints = 0;
  }

  /**
   * Returns the color of the line
   * 
   * @return Color of line
   */
  public MTColor getColor() {
    return color;
  }

  /**
   * Sets the color of the line
   * 
   * @param color The desired color for the trail of the cut
   */
  public void setColor(MTColor color) {
    this.color = color;
  }
}
