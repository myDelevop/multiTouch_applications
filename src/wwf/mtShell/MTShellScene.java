/***********************************************************************
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package wwf.mtShell;

import java.awt.event.KeyEvent;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTSceneWindow;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.sceneManagement.transition.BlendTransition;
import org.mt4j.sceneManagement.transition.FadeTransition;
import org.mt4j.util.MTColor;
import org.mt4j.util.logging.ILogger;
import org.mt4j.util.logging.MTLoggerFactory;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PApplet;
import processing.core.PImage;
import scenes.WaterSceneExportObf;
import sun.misc.GC;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.drawing.MainDrawingScene;
import wwf.fluidSimulator.FluidSimulationScene;
import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.models3D.Models3DScene;
import wwf.modestMapsMT.MapsScene;
import wwf.physics.scenes.AirHockeyScene;
import wwf.physics.scenes.PhysicsScene;
import wwf.pictureBrowser.PictureBrowser;
import wwf.pictureBrowser.util.FilesPath;
import wwf.puzzle.PuzzleScene;
import wwf.space3D.Space3DScene;
import wwf.thermometer.ThermometerScene;
import wwf.todelete.Fonts;
import wwf.touchTail.TouchTailScene;


/**
 * The Class MTShellScene. A scene which displays other scenes icons and loads them.
 * 
 * @author Christopher Ruff
 */
public class MTShellScene extends AbstractScene {
	/** The Constant logger. */
	private static final ILogger logger = MTLoggerFactory.getLogger(MTShellScene.class.getName());
	static{
		//		logger.setLevel(ILogger.WARN);
		//		logger.setLevel(ILogger.DEBUG);
		logger.setLevel(ILogger.INFO);
	}

	/** The app. */
	private MTApplication app;

	/** The has fbo. */
	private boolean hasFBO;

	/** The list. */
	private MTList list;

	/** The font. */
	private IFont font;

	/** The preferred icon height. */
	private int preferredIconHeight;

	/** The gap between icon and reflection. */
	private int gapBetweenIconAndReflection;

	/** The display height of reflection. */
	private float displayHeightOfReflection;

	/** The list width. */
	private float listWidth;

	/** The list height. */
	private int listHeight;

	/** The preferred icon width. */
	private int preferredIconWidth;

	/** The switch directly to scene. */
	private boolean switchDirectlyToScene = false;

	private boolean useBackgroundGradient = true;

	//TODO (dont allow throwing stuff out of the screen) or destroy it then
	//TODO loading screen

	/**
	 * Instantiates a new mT shell scene.
	 * 
	 * @param mtApplication the mt application
	 * @param name the name
	 */
	public MTShellScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		FilesPath.createFilePaths();
		this.app = mtApplication;
		this.hasFBO = GLFBO.isSupported(app);
		//		this.hasFBO = false; // TEST
		//IF we have no FBO directly switch to scene and ignore setting
		this.switchDirectlyToScene = !this.hasFBO || switchDirectlyToScene;

		this.registerGlobalInputProcessor(new CursorTracer(app, this));

		//		this.setClearColor(new MTColor(230,230,230,255));
		//		this.setClearColor(new MTColor(30,30,30,255));
		this.setClearColor(new MTColor(0,0,0,255));

		//BACKGROUND GRADIENT
		if (this.useBackgroundGradient){
			Vertex[] vertices = new Vertex[]{
					new Vertex(0, 			app.height/3f,	0, 	0,0,0,255),
					new Vertex(app.width, 	app.height/3,	0, 	0,0,0,255),
					new Vertex(app.width, 	app.height/1.7f,0,	170,170,140,255),
					new Vertex(0,			app.height/1.7f,0,	170,170,140,255),
					new Vertex(0, 			app.height/3,	0,	0,0,0,255),
			};
			MTPolygon p = new MTPolygon(getMTApplication(), vertices);
			p.setName("upper gradient");
			p.setNoStroke(true);
			p.generateAndUseDisplayLists();
			p.setPickable(false);
			this.getCanvas().addChild(p);

			Vertex[] vertices2 = new Vertex[]{
					new Vertex(0, 			app.height/1.7f,	0, 	170,170,140,255),
					new Vertex(app.width, 	app.height/1.7f,	0, 	170,170,140,255),
					new Vertex(app.width, 	app.height,			0,	0,0,0,255),
					new Vertex(0,			app.height,			0,	0,0,0,255),
					new Vertex(0, 			app.height/1.7f,	0, 	170,170,140,255),
			};
			MTPolygon p2 = new MTPolygon(getMTApplication(), vertices2);
			p2.setNoStroke(true);
			p2.generateAndUseDisplayLists();
			p2.setPickable(false);
			this.getCanvas().addChild(p2);
		}
		//BACKGROUND

		preferredIconWidth = 256;
		preferredIconHeight = 192;
		gapBetweenIconAndReflection = 9;
		displayHeightOfReflection = preferredIconHeight * 0.6f;

		//CREATE LIST
		listWidth = preferredIconHeight + displayHeightOfReflection + gapBetweenIconAndReflection;
		//		listHeight = app.width - 50;
		listHeight = app.width;
		list = new MTList(mtApplication,0, 0, listWidth, listHeight, 40);
		list.setFillColor(new MTColor(150,150,150,200));
		list.setNoFill(true);
		list.setNoStroke(true);


		font = Fonts.getInstance(mtApplication).getSemiLight24White();

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new TouchTailScene(app, "Touch Tails");
			}
			public String getTitle() {
				return "Code volanti";
			}
		}, app.loadImage(this.getPathToIcons() + "touchtails_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new MapsScene(app, "Maps");
			}
			public String getTitle() {
				return "Google Maps";
			}
		}, app.loadImage(this.getPathToIcons() + "maps_ss.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new AirHockeyScene(app, "Air Hockey");
			}
			public String getTitle() {
				return "Air Hockey";
			}
		}, app.loadImage(this.getPathToIcons() + "airhockey_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new PuzzleScene(app, "Puzzle");
			}
			public String getTitle() {
				return "Puzzle";
			}
		}, app.loadImage(this.getPathToIcons() + "puzz.png"));

		if (this.hasFBO){
			this.addScene(new ICreateScene() {
				public Iscene getNewScene() {
					return new MainDrawingScene(app, "MT Paint");
				}
				public String getTitle() {
					return "Lavagna interattiva";
				}
			}, app.loadImage(this.getPathToIcons() + "drawing_s.png"));
		}

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new PictureBrowser(app, "Multimedia Browser");
			}
			public String getTitle() {
				return "Visualizzatore foto";
			}
		}, app.loadImage(this.getPathToIcons() + "flickr_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new ThermometerScene(app, "Stati dell'acqua");
			}
			public String getTitle() {
				return "Stati dell'acqua";
			}
		}, app.loadImage(this.getPathToIcons() + "termometro.jpg"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new CraniumSceneManager(app, "Cranium");
			}
			public String getTitle() {
				return "Cranium";
			}
		}, app.loadImage(this.getPathToIcons() + "cranium.jpg"));


		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new WaterSceneExportObf(app, "Interactive Water"); 
			}
			public String getTitle() {
				return "Acqua interattiva";
			}
		}, app.loadImage(this.getPathToIcons() + "water_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new FluidSimulationScene(app, "Fluid Particles");
			}
			public String getTitle() {
				return "Particelle fluide";
			}
		}, app.loadImage(this.getPathToIcons() + "fluidparticles_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new Models3DScene(app, "3D Models");
			}
			public String getTitle() {
				return "Modelli 3D";
			}
		}, app.loadImage(this.getPathToIcons() + "teapot.jpg"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new PhysicsScene(app, "Physics Playground");
			}
			public String getTitle() {
				return "Palline interattive";
			}
		}, app.loadImage(this.getPathToIcons() + "pyhsics_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new Space3DScene(app, "Earth 3D");
			}
			public String getTitle() {
				return "Google Earth 3D";
			}
		}, app.loadImage(this.getPathToIcons() + "earth_s.png"));

		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new CGSceneManager(app, "Classification Game");
			}
			public String getTitle() {
				return "Classification Game";
			}
		}, app.loadImage(this.getPathToIcons() + "cg.png"));
		
		this.addScene(new ICreateScene() {
			public Iscene getNewScene() {
				return new InteractiveMapSceneManager(app, "Interactive Map");
			}
			public String getTitle() {
				return "Interactive Map";
			}
		}, app.loadImage(this.getPathToIcons() + "interactiveMap.jpg"));


		getCanvas().addChild(list);
		list.rotateZ(list.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		//		list.setPositionGlobal(new Vector3D(app.width/2f, app.height - list.getHeightXY(TransformSpace.GLOBAL) - 1));
		list.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
		//		list.setAnchor(PositionAnchor.UPPER_LEFT);
		//		list.setPositionGlobal(new Vector3D(app.width/2f - list.getWidthXY(TransformSpace.GLOBAL)/2f, app.height - 20));
		getCanvas().setFrustumCulling(true); 

		//Scene transition effect
		if (this.hasFBO){
			this.setTransition(new BlendTransition(app, 730));	
		}else{
			this.setTransition(new FadeTransition(app, 730));	
		}
	}


	/**
	 * Gets the path to icons.
	 * 
	 * @return the path to icons
	 */
	private String getPathToIcons(){
		//		return System.getProperty("user.dir")+File.separator+"examples"+File.separator+"advanced"+File.separator+"mtShell"+ File.separator +"data"+ File.separator+"images"+File.separator; 
		//Load from classpath
		return  "wwf" + MTApplication.separator + "mtShell" + MTApplication.separator + "data"+ MTApplication.separator + "images" + MTApplication.separator;
	}


	/**
	 * Adds the tap processor.
	 * 
	 * @param cell the cell
	 * @param createScene the create scene
	 */
	private void addTapProcessor(MTListCell cell, final ICreateScene createScene){
		cell.registerInputProcessor(new TapProcessor(app, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				try{
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						final Iscene scene = createScene.getNewScene();

						if (!switchDirectlyToScene){//We have FBO support -> show scene in a window first

							if (hasFBO && scene instanceof AbstractScene){
								((AbstractScene) scene).setTransition(new BlendTransition(app, 300));	
							}

							final MTSceneWindow sceneWindow = new MTSceneWindow(app, scene,100, 50);
							sceneWindow.setFillColor(new MTColor(50,50,50,200));
							//MODIFICARE QUI PER GRANDEZZA SHELL RISPETTO A SCHERMO
							sceneWindow.scaleGlobal(0.5f, 0.5f, 0.5f, sceneWindow.getCenterPointGlobal());

							sceneWindow.removeAllGestureEventListeners(RotateProcessor.class);
							/*sceneWindow.registerInputProcessor(new ScaleProcessor(sceneWindow.getRenderer()));
							sceneWindow.setGestureAllowance(ScaleProcessor.class, true);
							sceneWindow.addGestureListener(ScaleProcessor.class, new DefaultScaleAction());

							sceneWindow.registerInputProcessor(new DragProcessor(sceneWindow.getRenderer()));
							sceneWindow.setGestureAllowance(DragProcessor.class, true);
							sceneWindow.addGestureListener(DragProcessor.class, new InertiaDragAction());*/

							getCanvas().addChild(sceneWindow);
							sceneWindow.createResetTimerThread();
						}else{
							//No FBO available -> change to the new scene fullscreen directly

							float menuWidth = 128;
							float menuHeight = 128;
							MTSceneMenu sceneMenu = 
									//new MTSceneMenu(this, app.width-menuWidth/2f, 0-menuHeight/2f, menuWidth, menuHeight, app);
									new MTSceneMenu(app, scene, app.width-menuWidth, app.height-menuHeight, menuWidth, menuHeight);
							sceneMenu.addToScene();

							app.addScene(scene);
							app.pushScene();
							app.changeScene(scene);
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}


	/**
	 * Adds the scene.
	 * 
	 * @param sceneToCreate the scene to create
	 * @param icon the icon
	 */
	public void addScene(ICreateScene sceneToCreate, PImage icon){
		try {


			//		System.out.println("Width: " + width + " Height:" + height);

			//Create reflection image
			PImage reflection = this.getReflection(getMTApplication(), icon);

			float border = 1;
			float bothBorders = 2*border;
			float topShift = 30;
			float reflectionDistanceFromImage = topShift + gapBetweenIconAndReflection; //Gap width between image and reflection

			float listCellWidth = listWidth;		
			float realListCellWidth = listCellWidth - bothBorders;
			//		float listCellHeight = preferredIconWidth - border;
			float listCellHeight = preferredIconWidth ;

			MTListCell cell = new MTListCell(app ,  realListCellWidth, listCellHeight);
			cell.setNoFill(true);
			cell.setNoStroke(true);

			//		/*
			Vertex[] vertices = new Vertex[]{
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
					new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
					new Vertex(realListCellWidth-topShift - icon.height, listCellHeight -border,	0, 1,1),
					new Vertex(realListCellWidth-topShift - icon.height,	border,		  		0, 0,1),
					new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
			};
			MTPolygon p = new MTPolygon(getMTApplication(), vertices);
			p.setTexture(icon);
			//		p.setNoStroke(true);
			//		p.setStrokeColor(new MTColor(150,150,150, 255));
			p.setStrokeColor(new MTColor(80,80,80, 255));

			Vertex[] verticesRef = new Vertex[]{
					new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 				 					border,	0, 	0,0),
					new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage,						listCellHeight -border,	0, 	1,0),
					new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage, 	listCellHeight -border,	0, 	1,1),
					new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage,				border,	0, 	0,1),
					new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 									border,	0, 	0,0),
			};
			MTPolygon pRef = new MTPolygon(getMTApplication(), verticesRef);
			pRef.setTexture(reflection);
			pRef.setNoStroke(true);

			cell.addChild(p);
			cell.addChild(pRef);

			list.addListElement(cell);
			addTapProcessor(cell, sceneToCreate);


			///Add scene title
			MTTextArea text = new MTTextArea(app, font);
			text.setFillColor(new MTColor(150,150,250,200));
			text.setNoFill(true);
			text.setNoStroke(true);
			text.setText(sceneToCreate.getTitle());
			text.rotateZ(text.getCenterPointLocal(), 90, TransformSpace.LOCAL);
			cell.addChild(text);

			text.setPositionRelativeToParent(cell.getCenterPointLocal());
			text.translate(new Vector3D(realListCellWidth*0.5f - text.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));
			///
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Creates the reflection image.
	 * 
	 * @param pa the pa
	 * @param image the image
	 * @return the reflection image
	 */
	private PImage getReflection(PApplet pa, PImage image) {
		int width =  image.width; 
		int height = image.height;

		PImage copyOfImage = pa.createImage(image.width, image.height, PApplet.ARGB);
		image.loadPixels();
		copyOfImage.loadPixels();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int imageIndex = y*image.width+x;
				//				int currA = (image.pixels[imageIndex] >> 32) & 0xFF;
				int currR = (image.pixels[imageIndex] >> 16) & 0xFF;
				int currG = (image.pixels[imageIndex] >> 8) & 0xFF;
				int currB = image.pixels[imageIndex] & 0xFF;

				int col = image.pixels[imageIndex];
				float alpha = pa.alpha(col);

				int reflectImageIndex = (image.height-y-1) * image.width+x;

				//TOD clamp 0-255, map 0-255, 255- y*y * x
				//			    copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y*0.8));
				//			    copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y * (0.005f*y) * 0.5));
				//			    copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(Tools3D.clamp(255 - y*y , 0, 255)));
				//			    copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y*y*y * (0.00003f) - 20)); //WORKS
				if (alpha <= 0.0f){
					copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , 0.0f); 
				}else{
					copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y*y*y * (0.00003f) - 60)); //WORKS	
				}
			}
		} 
		copyOfImage.updatePixels();
		return copyOfImage;
	}


	public void onEnter() {
		getMTApplication().registerKeyEvent(this);
	}

	public void onLeave() {	
		getMTApplication().unregisterKeyEvent(this);
	}

	/**
	 * Key event.
	 * 
	 * @param e the e
	 */
	public void keyEvent(KeyEvent e){
		int evtID = e.getID();
		if (evtID != KeyEvent.KEY_PRESSED)
			return;
		switch (e.getKeyCode()){
		case KeyEvent.VK_F:
			System.out.println("FPS: " + getMTApplication().frameRate);
			break;
		case KeyEvent.VK_M:
			System.out.println("Max memory: " + Runtime.getRuntime().maxMemory() + " <-> Free memory: " + Runtime.getRuntime().freeMemory());
			break;	
		case KeyEvent.VK_C:
			getMTApplication().invokeLater(new Runnable() {
				public void run() {
					System.gc();
					GC.maxObjectInspectionAge();
					System.runFinalization();
				}
			});
			break;
		default:
			break;
		}
	}

	private IGestureEventListener listnerButton = new IGestureEventListener() {

		@Override
		public boolean processGestureEvent(final MTGestureEvent ge) {
			if (ge.GESTURE_ENDED == ge.getId()) {
				// new LanciaMTComponent(istanza,dirVect,pApplet);
			}
			if (ge.GESTURE_STARTED == ge.getId()) {

			}
			return false;
		}
	};

}
