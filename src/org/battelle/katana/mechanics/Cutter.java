package org.battelle.katana.mechanics;
import java.util.HashMap;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.AbstractVisibleComponent;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Plane;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Cutter extends AbstractVisibleComponent {
  /** The array of cuts to enable multiple cuts at once */
  private Cut[] cutArray;
  /** Plane that listens to the gesture  */
  private Plane plane;
  /** The number of gestures allowed at once */
  private final int nGestures = 30;
  /** The minimum distance between points on the cut */
  private final int maxDist = 5;
  /** The ID of the current gesture */
  private int currentGestureID;
  /** The PApplet */
  private PApplet app;
  /** Canvas passed to the component from the scene */
  private MTCanvas canvas;
  /** HashMap mapping gesture IDs to the Cuts associated with them */
  private HashMap<Long, Cut> idToGesture;
  /** The text color for easy referencing */
  private MTColor textColor;
  /** The trail Color for our lines following the cuts */
  private MTColor trailColor;
  /** A segement of the cut */
  private Cut cut;
  /** The number of cuts on the screen */
  private int activeCuts;

  /**
   * The primary constructor for a cutter
   * 
   * @param applet The application
   * @param mtCanvas The canvas that belongs to the application
   * @param textColor The color of "DOUBLE BEARCLAW!"
   * @param trailColor The color of the trail behind input points
   */
  public Cutter(PApplet applet, MTCanvas mtCanvas, MTColor textColor, MTColor trailColor) {
    // Set the superclass and some fields
    super(applet);
    this.app = applet;
    this.canvas = mtCanvas;
    this.textColor = textColor;
    this.trailColor = trailColor;
    this.activeCuts = 0;
    idToGesture = new HashMap<Long, Cut>();
    currentGestureID = -1;
    
    // Initialize the array of cuts to allow for the maximum number of cuts on the screen
    cutArray = new Cut[nGestures];

    // Fill the array with cuts
    for (int i = 0; i < nGestures; i++) {
      cutArray[i] = new Cut();
    }

    // Register the input processor and add the gesture listener
    this.registerInputProcessor(new MultipleDragProcessor(app));
    this.addGestureListener(MultipleDragProcessor.class, new CutListener());

    // Construct the plane that the cutter will interact on
    Vector3D norm = new Vector3D(0, 0, 1);
    Vector3D pointInPlane = new Vector3D(0, 0, 0);
    plane = new Plane(pointInPlane, norm);
  }

  @Override
  protected boolean componentContainsPointLocal(Vector3D testPoint) {
    return plane.componentContainsPointLocal(testPoint);
  }

  @Override
  public Vector3D getIntersectionLocal(Ray ray) {
    return plane.getIntersectionLocal(ray);
  }

  /**
   * Defines a cut listener class
   * 
   * @author CYBER-100
   */
  private class CutListener implements IGestureEventListener {

    public boolean processGestureEvent(MTGestureEvent ge) {
      
      DragEvent de = (DragEvent) ge;
      // Gets the "to"
      Vector3D to = de.getTo();
      switch (de.getId()) {
      case DragEvent.GESTURE_DETECTED: {
        if (idToGesture.size() < nGestures) {
          // Increment the number of cuts on the screen
          activeCuts++;
          
          // Selects a new ID to be the current ID
          currentGestureID = (currentGestureID + 1) % nGestures;
          
          // Creates a new cut from the current spot in the cut array
          cut = cutArray[currentGestureID];
          
          // Adds the current gestureID/Cut pair to the hash map
          idToGesture.put(de.getDragCursor().getId(), cut);
          
          // Clears the array for new cuts
          cut.clear();
          
          // Adds the current "to" to the array
          cut.addPoint(to);
          
          if (idToGesture.size() == 10) {
            MTTextArea doubleBearClaw = new MTTextArea(app, FontManager.getInstance().createFont(app, "arial.ttf", 
                    50, MTColor.WHITE, MTColor.BLACK, false));
            doubleBearClaw.setNoFill(true);
            doubleBearClaw.setNoStroke(true);
            doubleBearClaw.setText("Double Bearclaw!!!!");
            canvas.addChild(doubleBearClaw);
            doubleBearClaw.setPositionGlobal(new Vector3D(app.width/2f, app.height - 135));

            
            Fader bearClawFader = new Fader(doubleBearClaw, doubleBearClaw.getFont().getFillColor());
            ((MTApplication) app).invokeLater(bearClawFader);
            
          }
        }
      }
        break;
      case DragEvent.GESTURE_UPDATED: {
        //System.out.println(de.getDragCursor().getPosition().getX());
        // TODO figure out drawing and valid cutting
        // Gets the current gestureID/Cut pair from the hash map
        Cut cut = idToGesture.get(de.getDragCursor().getId());
        // If the cut is longer than maxDist, add the point to the array
        if ((cut != null) && (cut.distFromLast(to) > maxDist)) {
          Vertex current = new Vertex();
          current.setValues(to);
          Vertex last = new Vertex();
          last.setValues(cut.getLast());

          if (cut.addPoint(to)) {
            // Create a new line for the two coordinates
            MTLine line1 = new MTLine(app, current, last);
//            MTLine sideLine = new MTLine(app, new Vertex(current.x + 6,current.y + 6), new Vertex(current.x - 6, current.y - 6));
            line1.setStrokeWeight(2.5f);
//            sideLine.setStrokeWeight(2.0f);
            // Create a teal line
            line1.setStrokeColor(trailColor);
//            sideLine.setStrokeColor(MTColor.randomColor());
            // Remove the ability for the line to be interacted with
            line1.removeAllGestureEventListeners();
//            sideLine.removeAllGestureEventListeners();
            // Add it to the canvas
            canvas.addChild(line1);
//            canvas.addChild(sideLine);
            // Create a line fader for our current line segment to
            // fade incrementally
            Fader fader = new Fader(line1, new MTColor(trailColor));
//            Fader fader2 = new Fader(sideLine, new MTColor(0, 180, 200, 255));
            ((MTApplication) app).invokeLater(fader);
//            ((AbstractMTApplication) app).invokeLater(fader2);
          }
        }
      }
        break;
      case DragEvent.GESTURE_ENDED: {
        if(idToGesture.containsKey(de.getDragCursor().getId())) {
          idToGesture.get(de.getDragCursor().getId()).clear();
        }
        activeCuts--;
        // Remove the cut from the hash map
        idToGesture.remove(de.getDragCursor().getId());
        // Clears the array for new cuts
        //cut.clear();
//        System.out.println("Current Gesture: " + currentGestureID + " End + clear. ID is: " + de.getDragCursor().getId());
        
      }
        break;
      default:
        break;
      }
      return true;
    }
  }

  public int getActiveCuts() {
    return this.activeCuts;
  }
  
  @Override
  public void drawComponent(PGraphics g) {
  }
  
  public Cut[] getCuts()
  {
    return this.cutArray;
  }
  
  public Cut getCut() {
    return cut;
  }
  
  @Override
  public void destroyComponent() {
    super.destroyComponent();
    cutArray = null;
    plane = null;
    cut = null;
  }
}