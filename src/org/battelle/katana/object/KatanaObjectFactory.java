package org.battelle.katana.object;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.battelle.katana.mechanics.Launcher;
import org.battelle.katana.mechanics.Timer;
import org.battelle.katana.model.GameModel;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.GeometryInfo;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.mesh.MTTriangleMesh;
import org.mt4j.util.MTColor;
import org.mt4j.util.opengl.GLTexture;
import org.mt4j.util.opengl.GLTexture.EXPANSION_FILTER;
import org.mt4j.util.opengl.GLTexture.SHRINKAGE_FILTER;
import org.mt4j.util.opengl.GLTexture.TEXTURE_TARGET;
import org.mt4j.util.opengl.GLTexture.WRAP_MODE;
import org.mt4j.util.opengl.GLTextureSettings;

public class KatanaObjectFactory {
  /** The game we are using */
  private MTApplication mtApp;
  /** The backend model representation of the game we are using */
  private GameModel model;
  /** The launcher we are using to launch fruit with */
  private Launcher launcher;
  /** The bomb texture */
  private static GLTexture bombTexture;
  /** The frenzy texture */
  private static GLTexture frenzyTexture;
  /** The timewarp texture */
  private static GLTexture timewarpTexture;
  /** The multiplier texture */
  private static GLTexture multiplierTexture;
  /** The path the images are located in on disk */
  private String imagesPath = "resources" + MTApplication.separator + "images" + MTApplication.separator + "textures" + MTApplication.separator;
  /** Random number generator */
  private Random random;
  /** ArrayList for fruit textures */
  private static ArrayList<GLTexture> fruitList;
  /** Timer for game */
  private Timer timer;
  /** The geometry info for each fruit */
  private ArrayList<MTComponent> fruitInfo;
  /** Geometry info for the bomb */
  private MTComponent bombInfo;
  /** Geometry info for the powerup */
  private MTComponent powerupInfo;

  /**
   * Private constructor so that the default constructor cannot be invoked.
   */
  @SuppressWarnings("unused")
  private KatanaObjectFactory() {
    // TODO Auto-generated constructor stub
  }

  /**
   * Default constructor for our FruitFactory. Creates a factory with cached textures and passed in values required by fruits.
   * 
   * @param mtApp The application we are running, used in the creation of fruits' spheres.
   * @param model The game model we are using, used for updating values in the game based on cuts.
   * @param launcher The launcher we are using which launches the fruits.
   */
  public KatanaObjectFactory(MTApplication mtApp, GameModel model,
      Launcher launcher, Timer timer, ArrayList<MTComponent> fruitGeometry, MTComponent bombGeometry, MTComponent powerupGeometry) {
    //Initialize variables and set the textures
    this.mtApp = mtApp;
    this.launcher = launcher;
    this.model = model;
    this.random = new Random();
    fruitList = new ArrayList<GLTexture>();
    if (fruitList.size() == 0) {
    File fruitDirectory = new File(imagesPath + "fruit/");
      for (File fruitFile : fruitDirectory.listFiles()) {
        fruitList.add(new GLTexture(mtApp, imagesPath + "fruit/" + fruitFile.getName(),
            new GLTextureSettings(TEXTURE_TARGET.RECTANGULAR,
                SHRINKAGE_FILTER.NearestNeighborLinearMipMap, EXPANSION_FILTER.NearestNeighbor,
                WRAP_MODE.CLAMP_TO_EDGE, WRAP_MODE.CLAMP_TO_EDGE)));
      }
    }
    // Geometry info for available objects
    this.fruitInfo = fruitGeometry;
    this.bombInfo = bombGeometry;
    this.powerupInfo = powerupGeometry;
    if (fruitGeometry.size() != fruitList.size()) {
      System.err.println("Warning: not all fruit will contain geometry info");
    }
    if (bombTexture == null) {
      File bombFolder = new File(imagesPath + "bomb");
      String bombPath = imagesPath + "bomb/" + bombFolder.listFiles()[0].getName();
      bombTexture = new GLTexture(mtApp, bombPath, new GLTextureSettings(TEXTURE_TARGET.RECTANGULAR, SHRINKAGE_FILTER.NearestNeighborLinearMipMap, EXPANSION_FILTER.NearestNeighbor, WRAP_MODE.CLAMP_TO_EDGE, WRAP_MODE.CLAMP_TO_EDGE));
    }
    File powerupFolder = new File(imagesPath + "powerups");
    if (frenzyTexture == null)
    {
      String frenzyPath = imagesPath + "powerups/" + powerupFolder.listFiles()[0].getName();
      frenzyTexture = new GLTexture(mtApp, frenzyPath, new GLTextureSettings(TEXTURE_TARGET.RECTANGULAR, SHRINKAGE_FILTER.NearestNeighborLinearMipMap, EXPANSION_FILTER.NearestNeighbor, WRAP_MODE.CLAMP_TO_EDGE, WRAP_MODE.CLAMP_TO_EDGE));
    }
    if (multiplierTexture == null)
    {
      String multiplierPath = imagesPath + "powerups/" + powerupFolder.listFiles()[1].getName();
      multiplierTexture = new GLTexture(mtApp, multiplierPath, new GLTextureSettings(TEXTURE_TARGET.RECTANGULAR, SHRINKAGE_FILTER.NearestNeighborLinearMipMap, EXPANSION_FILTER.NearestNeighbor, WRAP_MODE.CLAMP_TO_EDGE, WRAP_MODE.CLAMP_TO_EDGE));
    }
    if (timewarpTexture == null)
    {     
      String timewarpPath = imagesPath + "powerups/" + powerupFolder.listFiles()[2].getName();
      timewarpTexture = new GLTexture(mtApp, timewarpPath, new GLTextureSettings(TEXTURE_TARGET.RECTANGULAR, SHRINKAGE_FILTER.NearestNeighborLinearMipMap, EXPANSION_FILTER.NearestNeighbor, WRAP_MODE.CLAMP_TO_EDGE, WRAP_MODE.CLAMP_TO_EDGE));
    }

  }

  public void init(Launcher launcher, GameModel model, Timer timer)
  {
    this.launcher = launcher;
    this.model = model;
    this.timer = timer;
  }
  
  /**
   * Creates a Fruit for launching given the type of fruit you want.
   * 
   * @param type
   *            The type of fruit to launch (watermelon, kiwi, orange)
   * @return The Fruit, as an ILaunchable
   */
  public KatanaObject create(ObjectType type) {

    // Create a size variable for creating our fruits with
//    int size = 0;
    // Create a texture for creating our fruits with
    GLTexture textureToUse;
    // Based on the type of fruit read in, generate different sizes and
    // textures
    GeometryInfo geoToUse;
    switch (type) {
    case RANDOMFRUIT:
      int fruitIndex = random.nextInt(fruitList.size());
      textureToUse = fruitList.get(fruitIndex);
      geoToUse = ((AbstractShape)fruitInfo.get(fruitIndex)).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, fruitInfo.get(fruitIndex), type);
    case FRUIT_ONE:
      textureToUse = fruitList.get(0);
      geoToUse = ((AbstractShape)fruitInfo.get(0)).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, fruitInfo.get(0), type);
    case FRUIT_TWO:
      textureToUse = fruitList.get(1);
      geoToUse = ((AbstractShape)fruitInfo.get(1)).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, fruitInfo.get(1), type);
    case FRUIT_THREE:
      textureToUse = fruitList.get(2);
      geoToUse = ((AbstractShape)fruitInfo.get(2)).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, fruitInfo.get(2), type);
    case FRENZY:
      textureToUse = frenzyTexture;
      geoToUse = ((AbstractShape)powerupInfo).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, powerupInfo, type);
    case TIMEWARP:
      textureToUse = timewarpTexture;
      geoToUse = ((AbstractShape)powerupInfo).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, powerupInfo, type);
    case MULTIPLIER:
      textureToUse = multiplierTexture;
      geoToUse = ((AbstractShape)powerupInfo).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, powerupInfo, type);
    case ARCADEBOMB:
      textureToUse = bombTexture;
      geoToUse = ((AbstractShape)bombInfo).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, bombInfo, type);
    case CLASSICBOMB:
      textureToUse = bombTexture;
      geoToUse = ((AbstractShape)bombInfo).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, bombInfo, type);
    default:
      System.err.println("Warning: invalid object request to KatanaObjectFactory. Launching bomb instead.");
      textureToUse = bombTexture;
      geoToUse = ((AbstractShape)bombInfo).getGeometryInfo();
      return getReturnObject(geoToUse, textureToUse, bombInfo, type);
    }
    // Create a new object based on the sphere, launcher, and model we
    // already have
    
    
    
  }
  
  private KatanaObject getReturnObject(GeometryInfo geoToUse, GLTexture textureToUse, MTComponent comp, ObjectType type)
  {
    if (comp instanceof MTTriangleMesh)
    {
      MTTriangleMesh polyToUse = new MTTriangleMesh(mtApp, geoToUse);
      KatanaObject returnObject = new KatanaObject(polyToUse, launcher, model, type, timer);
      returnObject.setTexture(textureToUse);
      // Return that fruit
      return returnObject;
    }
    else
    {
      MTPolygon polyToUse = new MTPolygon(mtApp, geoToUse.getVertices());
      KatanaObject returnObject = new KatanaObject(polyToUse, launcher, model, type, timer);
      returnObject.setTexture(textureToUse);
      MTColor fill = ((AbstractShape)returnObject.getComponent()).getFillColor();
      fill.setAlpha(255);
      ((AbstractShape)returnObject.getComponent()).setFillColor(fill);
      return returnObject;
    }
  }

  /**
   * Creates a random fruit using a randomly generated enumerated fruit type.
   * 
   * @return A random Fruit as an ILaunchable object.
   */
  public KatanaObject createRandom() {
    //Create a random fruit using the create method above, passing in a random type.
    int i = random.nextInt(ObjectType.values().length);
    return create(ObjectType.values()[i]);
  }

}

