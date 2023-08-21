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
package wwf.modestMapsMT;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.nio.DoubleBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.media.opengl.GL;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.progressBar.AbstractProgressThread;
import org.mt4j.components.visibleComponents.widgets.progressBar.MTProgressBar;
import org.mt4j.input.IMTEventListener;
import org.mt4j.input.MTEvent;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.AnimationManager;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.camera.MTCamera;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

import com.modestmaps.TestInteractiveMap;
import com.modestmaps.core.Coordinate;
import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.AbstractMapProvider;
import com.modestmaps.providers.BlueMarble;
import com.modestmaps.providers.CloudMade;
import com.modestmaps.providers.DailyPlanet;
import com.modestmaps.providers.Microsoft;
import com.modestmaps.providers.OpenStreetMaps;


/**
 * The Class MapsScene.
 * 
 * @author Christopher Ruff
 */
public class MapsScene extends AbstractScene implements MouseWheelListener, MouseListener {

	/** The map. */
	private TestInteractiveMap map;

	/** The p. */
	private MTApplication p;

	/** The tag container. */
	private MTComponent tagContainer;

	/** The foto container. */
	private MTComponent fotoContainer;

	/** The button container. */
	private MTComponent buttonContainer;

	/** The default center cam. */
	private MTCamera defaultCenterCam;

	/** The progress bar. */
	private MTProgressBar progressBar;


	private boolean animateToBestZoomLevel = true;

	//TODO button/gesture for optimal zoom level - map.setZoom(map.bestZoomForScale((float) map.sc)); ?

	/**
	 * Instantiates a new maps scene.
	 * 
	 * @param mtApplication the mt application
	 * @param name the name
	 */
	public MapsScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.p = mtApplication;

		if (!MT4jSettings.getInstance().isOpenGlMode()){
			System.err.println("Scene only usable when using the OpenGL renderer! - See settings.txt");
			return;
		}

		//Show our touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));

		defaultCenterCam = new MTCamera(p);

		//Container for the foto tags on the map
		tagContainer = new MTComponent(p);

		//Container for the Fotos
		fotoContainer = new MTComponent(p);
		fotoContainer.attachCamera(defaultCenterCam);

		//Container for the buttons
		buttonContainer = new MTComponent(p);
		buttonContainer.attachCamera(defaultCenterCam);

		//Create map
		AbstractMapProvider mapProvider = new Microsoft.HybridProvider();
		map = new TestInteractiveMap(mtApplication, mapProvider);
		map.setName("map");
		map.MAX_IMAGES_TO_KEEP = 256;
		map.sc = 4;  //Initial map scale
		//Map gestures
		map.registerInputProcessor(new DragProcessor(mtApplication));
		map.addGestureListener(DragProcessor.class, new MapDrag());

		map.registerInputProcessor(new ScaleProcessor(mtApplication));
		map.addGestureListener(ScaleProcessor.class, new MapScale());

		map.setGestureAllowance(RotateProcessor.class, false);
		float latitude = 40.413163f;
		float longitude = 17.494911f;
		map.setCenterZoom(new Location(latitude, longitude), 15);
		this.getCanvas().addChild(map);


		//Set up the progressbar
		//		progressBar = new MTProgressBar(p, p.loadFont(MT4jSettings.getInstance().getDefaultFontPath() + "Ziggurat.vlw"));
		//		progressBar = new MTProgressBar(p, p.loadFont("arial"));
		progressBar = new MTProgressBar(p, p.createFont("arial", 18));
		progressBar.attachCamera(defaultCenterCam);
		progressBar.setDepthBufferDisabled(true);
		progressBar.setVisible(false);
		progressBar.setPickable(false);
		this.getCanvas().addChild(progressBar);

		this.getCanvas().addChild(tagContainer);
		this.getCanvas().addChild(fotoContainer);
		this.getCanvas().addChild(buttonContainer);


		/// Create map provider menu \\\
		IFont font = FontManager.getInstance().createFont(p, "SansSerif.Bold", 35, MTColor.WHITE);
		MTRoundRectangle mapMenu = new MTRoundRectangle(p,0,0, 0,500, 600,10, 10);
		//		mapMenu.setFillColor(new MTColor(110,110,110,180));
		//		mapMenu.setStrokeColor(new MTColor(110,110,110,180));
		mapMenu.setFillColor(new MTColor(45,45,45,180));
		mapMenu.setStrokeColor(new MTColor(45,45,45,180));
		mapMenu.setPositionGlobal(new Vector3D(p.width/2f, p.height/2f));
		mapMenu.translateGlobal(new Vector3D(-p.width/2f - 150,0));
		getCanvas().addChild(mapMenu);

		float cellWidth = 350;
		float cellHeight = 100;
		MTColor cellFillColor = new MTColor(new MTColor(0,0,0,210));
		MTColor cellPressedFillColor = new MTColor(new MTColor(20,20,20,220));

		MTList list = new MTList(p,0, 0, 300, 5* cellHeight + 5*3);
		list.setChildClip(null); //FIXME TEST -> do no clipping for performance
		list.setNoFill(true);
		list.setNoStroke(true);
		list.unregisterAllInputProcessors();
		list.setAnchor(PositionAnchor.CENTER);
		list.setPositionRelativeToParent(new Vector3D(mapMenu.getCenterPointLocal().getX()-50, mapMenu.getCenterPointLocal().getY()));
		mapMenu.addChild(list);

		list.addListElement(this.createListCell("Microsoft Aerial", font, new Microsoft.AerialProvider(), cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
		list.addListElement(this.createListCell("Microsoft Road", font, new Microsoft.RoadProvider(), cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
		list.addListElement(this.createListCell("Microsoft Hybrid", font, new Microsoft.HybridProvider(), cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
		list.addListElement(this.createListCell("Open Street Maps", font, new OpenStreetMaps(), cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
		//list.addListElement(this.createListCell("Cloudmade Tourist", font, new CloudMade.Tourist(), cellWidth, cellHeight, cellFillColor, cellPressedFillColor));
		list.addListElement(this.createListCell("Blue Marble", font, new BlueMarble(), cellWidth, cellHeight, cellFillColor, cellPressedFillColor));

		final IAnimation slideOut = new AniAnimation(0, 400, 700, AniAnimation.BACK_OUT, mapMenu);
		slideOut.addAnimationListener(new IAnimationListener() {
			public void processAnimationEvent(AnimationEvent ae) {
				float delta = ae.getDelta();
				((IMTComponent3D)ae.getTarget()).translateGlobal(new Vector3D(delta,0,0));
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_ENDED:
					doSlideIn = true;
					animationRunning = false;
					break;
				}
			}
		});

		//		final IAnimation slideIn = new Animation("slide out animation", in, mapMenu);
		final IAnimation slideIn = new AniAnimation(0, 400, 700, AniAnimation.BACK_OUT, mapMenu);
		slideIn.addAnimationListener(new IAnimationListener() {
			public void processAnimationEvent(AnimationEvent ae) {
				float delta = -ae.getDelta();
				((IMTComponent3D)ae.getTarget()).translateGlobal(new Vector3D(delta,0,0));
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_ENDED:
					doSlideIn = false;
					animationRunning = false;
					break;
				}
			}
		});

		mapMenu.unregisterAllInputProcessors();
		mapMenu.registerInputProcessor(new TapProcessor(mtApplication, 50));
		mapMenu.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				if (((TapEvent)ge).getTapID() == TapEvent.TAPPED){
					if (!animationRunning){
						animationRunning = true;
						if (doSlideIn){
							slideIn.start();
						}else{
							slideOut.start();
						}
					}
				}
				return false;
			}
		});

		updateTagContainerScale(); //needed to initialize..if not i observed strange behavior with the photo tags 
	}


	private boolean animationRunning = false;
	private boolean doSlideIn = false;

	private MTListCell createListCell(final String label, IFont font, final AbstractMapProvider mapProvider, float cellWidth, float cellHeight, final MTColor cellFillColor, final MTColor cellPressedFillColor){
		final MTListCell cell = new MTListCell(p, cellWidth, cellHeight);

		cell.setChildClip(null); //FIXME TEST, no clipping for performance!

		cell.setFillColor(cellFillColor);
		MTTextArea listLabel = new MTTextArea(p, font);
		listLabel.setNoFill(true);
		listLabel.setNoStroke(true);
		listLabel.setText(label);
		cell.addChild(listLabel);
		listLabel.setPositionRelativeToParent(cell.getCenterPointLocal());
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(p, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getTapID()) { 
				case TapEvent.TAP_DOWN:
					cell.setFillColor(cellPressedFillColor);
					break;
				case TapEvent.TAP_UP:
					cell.setFillColor(cellFillColor);
					break;
				case TapEvent.TAPPED:
					//					System.out.println("Button clicked: " + label);
					cell.setFillColor(cellFillColor);
					map.setMapProvider(mapProvider);
					break;
				}
				return false;
			}
		});
		return cell;
	}


	/**
	 * The Class MapDrag.
	 * @author C.Ruff
	 */
	private class MapDrag implements IGestureEventListener{
		public boolean processGestureEvent(MTGestureEvent g) {
			if (g instanceof DragEvent){
				DragEvent dragEvent = (DragEvent)g;
				Vector3D tVect = dragEvent.getTranslationVect();
				map.move(tVect.x, tVect.y);
				/*
				transVect.setXYZ(tVect.x, tVect.y, 0);
				fotoTagContainer.translate(transVect);
				 */
				updateTagContainerScale();
			}
			return false;
		}
	}

	/**
	 * The Class MapScale.
	 * 
	 * @author C.Ruff
	 */
	private class MapScale implements IGestureEventListener{
		private Vector3D lastMiddle;

		//		private Vector3D scaleP =  new Vector3D(p.width/2, p.height/2, 0);
		//		scaleP.setXYZ(p.width/2, p.height/2, 0);
		public boolean processGestureEvent(MTGestureEvent g) {
			if (g instanceof ScaleEvent){
				ScaleEvent se = (ScaleEvent)g;
				float scaleX = se.getScaleFactorX();
				//System.out.println("X:" + x + " Y:" +y);


				//Add a little panning to scale, so if we can pan while we scale
				InputCursor c1 = se.getFirstCursor();
				InputCursor c2 = se.getSecondCursor();
				if (se.getId() == MTGestureEvent.GESTURE_STARTED){
					Vector3D i1 = c1.getPosition();
					Vector3D i2 = c2.getPosition();
					lastMiddle = i1.getAdded(i2.getSubtracted(i1).scaleLocal(0.5f));
				}else if (se.getId() == MTGestureEvent.GESTURE_UPDATED){ 
					Vector3D i1 =  c1.getPosition();
					Vector3D i2 =  c2.getPosition();
					Vector3D middle = i1.getAdded(i2.getSubtracted(i1).scaleLocal(0.5f));
					Vector3D middleDiff = middle.getSubtracted(lastMiddle);
					map.move(middleDiff.x, middleDiff.y);
					lastMiddle = middle;
				}

				//Scale the map and the tags
				scaleMap(scaleX);

				if (animateToBestZoomLevel){
					//Stop previous animations
					IAnimation[] currentAnims = AnimationManager.getInstance().getAnimationsForTarget(map);
					for (IAnimation iAnimation : currentAnims) {
						iAnimation.stop();
					}

					//FIXME messes up tagContainer scale
					//Animate to the best zoom level for better clarity
					if (se.getId() == MTGestureEvent.GESTURE_ENDED){
						double current = map.sc;
						float currentF = (float)current;
						final int best = map.bestZoomForScale((float) map.sc);
						map.setZoom(best);
						float bestZoom = (float) map.sc;
						map.sc = current;
						//					System.out.println("current: " + currentF + " bestZoom: " + bestZoom);

						AniAnimation anim = new AniAnimation(currentF, bestZoom, 1000, map);
						anim.addAnimationListener(new IAnimationListener() {
							public void processAnimationEvent(AnimationEvent ae) {
								//								map.sc += ae.getDelta();
								double nowScale = map.sc;
								double destScale = nowScale + ae.getDelta();
								double diff = destScale/nowScale;
								scaleMap((float) diff);

								if (ae.getId() == AnimationEvent.ANIMATION_ENDED){
									map.setZoom(best);
									map.setZoom(map.bestZoomForScale((float) map.sc));
									//								System.out.println("Ended: " + map.sc);
								}
							}
						});
						anim.start();
					}
				}
			}
			return false;
		}
	}


	/**
	 * Scale the map and also the tags.
	 * 
	 * @param scaleFactor the scale factor
	 */
	private void scaleMap(float scaleFactor){
		if (scaleFactor != 1){
			map.sc *= scaleFactor;
			updateTagContainerScale();
			updateTagShapeScale(scaleFactor);
		}
	}

	//TODO CLEANUP
	/** The model. */
	private DoubleBuffer model = DoubleBuffer.allocate(16);
	/** The mgl. */
	private Matrix mgl = new Matrix();

	/**
	 * Kind of a hack to fit the scale of the foto tags to the map scale.
	 * Has to be called each time the map scale changes.
	 */
	private void updateTagContainerScale(){
		model.clear();
		PGraphicsOpenGL pgl = ((PGraphicsOpenGL)p.g);
		//		GL gl = pgl.beginGL();
		GL gl = pgl.gl;

		gl.glPushMatrix();
		gl.glScalef(1, -1, 1);
		gl.glTranslatef(p.width/2, p.height/2, 0);
		gl.glScalef((float)map.sc, (float)map.sc, 1);
		gl.glTranslatef((float)map.tx, (float)map.ty, 0);
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, model);
		gl.glPopMatrix();
		//		pgl.endGL();

		try {
			mgl.set(new float[]{
					(float)model.get(0), (float)model.get(4), (float)model.get(8),  (float)model.get(12),
					(float)model.get(1), (float)model.get(5), (float)model.get(9),  (float)model.get(13),
					(float)model.get(2), (float)model.get(6), (float)model.get(10), (float)model.get(14),
					(float)model.get(3), (float)model.get(7), (float)model.get(11), (float)model.get(15)});
		} catch (Exception e) {
			e.printStackTrace();
		}		
		tagContainer.setLocalMatrix(mgl);
		//		System.out.println(mgl);

		/*
		Matrix m = Matrix.getTranslationMatrix((float)p.width/2, (float)p.height/2, 0);
		Matrix s = Matrix.getScalingMatrix((float)map.sc, (float)map.sc, 1);
		Matrix t = Matrix.getTranslationMatrix((float)map.tx, (float)map.ty, 0);
		m.multLocal(s);
		m.multLocal(t);
//		m.translate((float)p.width/2, (float)p.height/2, 0);
//		m.scale((float)map.sc);
//		m.translate((float)map.tx, (float)map.ty);
		System.out.println("2:" + m);
//		fotoTagContainer.setLocalBasisMatrix(m);
		 */
	}


	/**
	 * Inversely scales the tagcontainers child shapes so they
	 * appear to be the same size, independent of the map's scale.
	 * 
	 * @param scale the scale
	 */
	private void updateTagShapeScale(float scale){
		MTComponent[] tags = tagContainer.getChildren();
		float scX = 1f/scale;
		for (MTComponent baseComponent : tags) {
			if (baseComponent instanceof AbstractShape) {
				AbstractShape shape = (AbstractShape) baseComponent;
				//				System.out.println("Scaling: " + scX + " " + scY);
				//				shape.scale(scX, scY, 1, shape.getCenterPointGlobal(), TransformSpace.GLOBAL);
				shape.scale(scX, scX, 1, shape.getCenterPointRelativeToParent(), TransformSpace.RELATIVE_TO_PARENT);
			}
		}
	}

	public void onEnter() {
		/*try {
			getMTApplication().registerKeyEvent(this);
			getMTApplication().addMouseWheelListener(this);
			getMTApplication().addMouseListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

	public void onLeave() {	
		/*
		try {
			getMTApplication().unregisterKeyEvent(this);
			getMTApplication().removeMouseWheelListener(this);
			getMTApplication().removeMouseListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Returns some more or less random points
	 * on the screen to search for nearby flickr fotos.
	 * 
	 * @return the screen points
	 */
	private Point[] getScreenPoints(){
		Point[] p = new Point[5];
		int sw = MT4jSettings.getInstance().getWindowWidth();
		int sh = MT4jSettings.getInstance().getWindowHeight();

		float wThird = sw/3f;
		float wThirdHalf = wThird/2f;
		float hHalf = sh/2f;

		p[0] = new Point( Math.round(wThirdHalf), Math.round(hHalf/2f) );
		p[1] = new Point( Math.round(wThirdHalf), sh - Math.round(hHalf/2f) );
		p[2] = new Point( Math.round(sw/2f), Math.round(sh/2f) );
		p[3] = new Point( sw - Math.round(wThirdHalf), Math.round(hHalf/2f) );
		p[4] = new Point( sw - Math.round(wThirdHalf), sh - Math.round(hHalf/2f) );
		return p;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}
}
