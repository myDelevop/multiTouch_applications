package wwf.drawing;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTColorPicker;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLFBO;

import processing.core.PImage;


/**
 * Questa classe rappresenta la scena centrale dell'applicazione drawing.
 */
public class MainDrawingScene extends AbstractScene {
	
	/** The pa. */
	private MTApplication pa;
	
	/** The texture brush. */
	private MTRectangle textureBrush;
	
	/** The pencil brush. */
	private MTEllipse pencilBrush;
	
	/** The drawing scene. */
	private ChooseBackgroundScene drawingScene;
	
	/** The frame. */
	private MTRoundRectangle frame;
	
	/** The scene texture. */
	private MTSceneTexture sceneTexture;
	
	/** The texture rubber. */
	private MTRectangle textureRubber;
	
	/** The rubber button. */
	private MTImageButton rubberButton;
		
    /** The icon dimension. */
    private float iconDimension;

	/**
	 * Costruttore parametrico.
	 *
	 * @param mtApplication applicazione in questione
	 * @param name nome dell'applicazione
	 */
	public MainDrawingScene(final MTApplication mtApplication, 
	        final String name) {
		super(mtApplication, name);
		this.pa = mtApplication;

		if (!(MT4jSettings.getInstance().isOpenGlMode() 
		        && GLFBO.isSupported(pa))) {
			System.err.println("Drawing example can only be run in"
			        + " OpenGL mode on a gfx card supporting the "
			        + "GL_EXT_framebuffer_object extension!");
			return;
		}
		this.registerGlobalInputProcessor(new CursorTracer(
		        mtApplication, this));
		this.iconDimension = 120;

		//Create window frame
		frame = new MTRoundRectangle(pa, -50, -50, 0, 
		        pa.width + 100, pa.height + 100, 25, 25);
		frame.unregisterAllInputProcessors();
		frame.removeAllGestureEventListeners();
		frame.setSizeXYGlobal(pa.width - 10, pa.height - 10);
		this.getCanvas().addChild(frame);
		//Create the scene in which we actually draw
		drawingScene = new ChooseBackgroundScene(pa, "Choose Background Scene");
		drawingScene.setClear(false);

		//Create texture brush
		PImage brushImage = getMTApplication()
		        .loadImage(FilesPath.IMAGES_PATH + "brush1.png");
		textureBrush = new MTRectangle(getMTApplication(), brushImage);
		textureBrush.setPickable(false);
		textureBrush.setNoFill(false);
		textureBrush.setNoStroke(true);
		textureBrush.setDrawSmooth(true);
		textureBrush.setFillColor(new MTColor(0, 0, 0));
		//Set texture brush as default
		drawingScene.setBrush(textureBrush);

		//Create texture brush
		PImage rubberImage = getMTApplication().
		        loadImage(FilesPath.IMAGES_PATH + "brush1.png");
		textureRubber = new MTRectangle(getMTApplication(), rubberImage);
		textureRubber.setPickable(false);
		textureRubber.setNoFill(false);
		textureRubber.setNoStroke(true);
		textureRubber.setDrawSmooth(true);
		textureRubber.setFillColor(new MTColor(0, 255, 0, 0));

		resizeBackgroundImages(drawingScene.getMTApplication().width,
		        drawingScene.getMTApplication().height);
		resizeIconImages(150, 150);

		//Create pencil brush
		pencilBrush = new MTEllipse(pa, new Vector3D(brushImage.width / 2f, 
		        brushImage.height / 2f, 0), brushImage.width / 2f, 
		        brushImage.width / 2f, 60);
		pencilBrush.setPickable(false);
		pencilBrush.setNoFill(false);
		pencilBrush.setNoStroke(false);
		pencilBrush.setDrawSmooth(true);
		pencilBrush.setStrokeColor(new MTColor(0, 0, 0, 255));
		pencilBrush.setFillColor(new MTColor(0, 0, 0, 255));

		sceneTexture = new MTSceneTexture(pa, 96, 0, pa.width,
		        pa.height, drawingScene);
		sceneTexture.getFbo().clear(true, 255, 255, 255, 0, true);
		sceneTexture.setStrokeColor(new MTColor(155, 155, 155));
		frame.addChild(sceneTexture);

		//Eraser button
		PImage eraser = pa.loadImage(FilesPath.IMAGES_PATH 
		        + "Kde_crystalsvg_eraser.png");
		MTImageButton b = new MTImageButton(pa, eraser);
        b.setWidthLocal(iconDimension);
        b.setHeightLocal(iconDimension);
		b.setNoStroke(true);
		b.translate(new Vector3D(-38f, -26f, 0));
		
		b.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					pa.invokeLater(new Runnable() {
						public void run() {
							sceneTexture.getFbo().clear(true, 255, 255,
							        255, 0, true);
						}
					});
				}
				return true;
			}
		});
		frame.addChild(b);

		float padding = 20f;
		//Pen brush selector button
		PImage penIcon = pa.loadImage(FilesPath.IMAGES_PATH + "pen.png");
		final MTImageButton penButton = new MTImageButton(pa, penIcon);
		penButton.setWidthLocal(iconDimension);
		penButton.setHeightLocal(iconDimension);
		frame.addChild(penButton);
		penButton.translate(new Vector3D(-38f, (this.iconDimension - 26f) 
		        + padding, 0));
		penButton.setNoStroke(true);
		penButton.setStrokeColor(new MTColor(0, 0, 0));

		
		//Texture brush selector button
		PImage brushIcon = pa.loadImage(
		        FilesPath.IMAGES_PATH + "paintbrush.png");
		final MTImageButton brushButton = new MTImageButton(pa, brushIcon);
		brushButton.setWidthLocal(iconDimension);
		brushButton.setHeightLocal(iconDimension);
		frame.addChild(brushButton);
		brushButton.translate(new Vector3D(-38f, (this.iconDimension * 2
		        - 26f) + padding * 2, 0));
		brushButton.setStrokeColor(new MTColor(0, 0, 0));
		

		brushButton.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					drawingScene.setBrush(textureBrush);
					brushButton.setNoStroke(false);
					penButton.setNoStroke(true);
					rubberButton.setNoStroke(true);
				}
				return true;
			}
		});

		//Texture rubber selector button
		PImage rubberIcon = pa.loadImage(FilesPath.IMAGES_PATH + "rubber.png");
		rubberButton = new MTImageButton(pa, rubberIcon);
		//frame.addChild(rubberButton);
		rubberButton.translate(new Vector3D(-38f, 400, 0));
		rubberButton.setStrokeColor(new MTColor(0, 0, 0));
		rubberButton.setNoStroke(true);
		
		rubberButton.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					drawingScene.setBrush(textureRubber);
					drawingScene.setBrushColor(new MTColor(255, 0, 0, 0));
					rubberButton.setNoStroke(false);
					penButton.setNoStroke(true);
					brushButton.setNoStroke(true);
				}
				return true;
			}
		});

		penButton.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					drawingScene.setBrush(pencilBrush);
					penButton.setNoStroke(false);
					brushButton.setNoStroke(true);
					rubberButton.setNoStroke(true);
				}
				return true;
			}
		});

		//Save to file button
		PImage floppyIcon = pa.loadImage(FilesPath.IMAGES_PATH + "floppy.png");
		final MTImageButton floppyButton = new MTImageButton(pa, floppyIcon);
		floppyButton.setWidthLocal(iconDimension);
		floppyButton.setHeightLocal(iconDimension);
		frame.addChild(floppyButton);
		floppyButton.translate(new Vector3D(-38f, (this.iconDimension * 6 - 26f)
		        + padding * 6, 0));
		floppyButton.setNoStroke(true);

		
		floppyButton.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					drawingScene.registerPreDrawAction(new IPreDrawAction() {
						public void processAction() {
							pa.saveFrame();
						}
						public boolean isLoop() {
							return false;
						}
					});
				}
				return true;
			}
		});

		/////////////////////////
		//ColorPicker and colorpicker button
		PImage colPick = pa.loadImage(FilesPath.IMAGES_PATH 
		        + "colorcircle.png");
		final MTColorPicker colorWidget = new MTColorPicker(pa, 0, 0, colPick);
		colorWidget.setAnchor(PositionAnchor.CENTER);
		colorWidget.translate(new Vector3D(0f, 135, 0));
		colorWidget.setNoStroke(false);
		colorWidget.setStrokeColor(new MTColor(0, 0, 0));
		
		colorWidget.addGestureListener(DragProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				if (ge.getId() == MTGestureEvent.GESTURE_ENDED) {
					if (colorWidget.isVisible()) {
						colorWidget.setVisible(false);
					}
				} else {
					drawingScene.setBrushColor(colorWidget.getSelectedColor());
				}
				return false;
			}
		});
		getCanvas().addChild(colorWidget);
		colorWidget.setVisible(false);

		PImage colPickIcon = pa.loadImage(
		        FilesPath.IMAGES_PATH + "ColorPickerIcon.png");
		MTImageButton colPickButton = new MTImageButton(pa, colPickIcon);
		frame.addChild(colPickButton);
		colPickButton.setWidthLocal(iconDimension);
		colPickButton.setHeightLocal(iconDimension);

		colPickButton.translate(new Vector3D(-38f, (this.iconDimension 
		        * 3 - 26f) + padding * 3, 0));
		colPickButton.setNoStroke(true);
		
		
		colPickButton.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (colorWidget.isVisible()) {
						colorWidget.setVisible(false);
					} else {
						colorWidget.setVisible(true);
						colorWidget.sendToFront();
					}				
				}
				return true;
			}
		});

        colorWidget.setPositionGlobal(colPickButton.getCenterPointGlobal());
        colorWidget.translate(new Vector3D(this.iconDimension, 
                -this.iconDimension / 2f));

		// Choose background to sceneTexture
		PImage changeBackgroundIcon = 
		        pa.loadImage(FilesPath.IMAGES_PATH + "changeBackIcon.png");
		MTImageButton changeBackgroundButton = 
		        new MTImageButton(pa, changeBackgroundIcon);
		changeBackgroundButton.setWidthLocal(iconDimension);
		changeBackgroundButton.setHeightLocal(iconDimension);

		changeBackgroundButton.translate(new Vector3D(-38f, 
		        (this.iconDimension * 5 - 26f) + padding * 5, 0));
		frame.addChild(changeBackgroundButton);

		changeBackgroundButton.setNoStroke(true);

		changeBackgroundButton.addGestureListener(
		        TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					frame.setVisible(false);
					DrawBackgrounds draw = new 
					        DrawBackgrounds(getMTApplication(), getCanvas());
					draw.drawBackgrounds();

				}
				return true;
			}
		});
		
	      //Add a slider to set the brush width
        final MTSlider slider = new MTSlider(pa, 0.0f, 0.0f, 
                420f, 79.8f, 0.05f, 2.0f);
        slider.setVisible(false);
        
	      //Texture brush selector button
        PImage sliderIcon = pa.loadImage(
                FilesPath.IMAGES_PATH + "sliderIcon.png");
        final MTImageButton sliderButton = new MTImageButton(pa, sliderIcon);
        sliderButton.setWidthLocal(iconDimension);
        sliderButton.setHeightLocal(iconDimension);
        frame.addChild(sliderButton);
        sliderButton.translate(new Vector3D(-38f, (this.iconDimension * 4 - 26f)
                + padding * 4, 0));
        sliderButton.setNoStroke(true);

        sliderButton.addGestureListener(TapProcessor.class, 
                new IGestureEventListener() {
            public boolean processGestureEvent(final MTGestureEvent ge) {
                TapEvent te = (TapEvent) ge;
                if (te.isTapped()) {
                    slider.setVisible(true);
                }
                return true;
            }
        });




		slider.setValue(1.0f);
		getCanvas().addChild(slider);
		slider.rotateZ(new Vector3D(), 90, TransformSpace.LOCAL);

		slider.setPositionGlobal(sliderButton.getCenterPointGlobal());
		slider.translate(new Vector3D(this.iconDimension, 
		        -this.iconDimension / 2f));
		
		slider.setStrokeColor(new MTColor(0, 0, 0));
		slider.setFillColor(new MTColor(255, 255, 255));
		slider.getKnob().setFillColor(new MTColor(0, 0, 0));
		slider.getKnob().setStrokeColor(new MTColor(0, 0, 0));
		slider.addPropertyChangeListener("value", new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent p) {
				drawingScene.setBrushScale((Float) p.getNewValue());
			}
		});

		
		//Add triangle in slider to indicate brush width
		MTPolygon p = new MTPolygon(pa, 
				new Vertex[]{
						new Vertex(2 + slider.getKnob().getWidthXY(
						        TransformSpace.LOCAL),
						        slider.getHeightXY(TransformSpace.LOCAL) 
						        / 2f, 0),
						new Vertex(slider.getWidthXY(TransformSpace.LOCAL) - 3, 
						        slider.getHeightXY(TransformSpace.LOCAL) 
						        / 4f + 2, 0),
						new Vertex(slider.getWidthXY(TransformSpace.LOCAL) - 1, 
						        slider.getHeightXY(TransformSpace.LOCAL) 
						        / 2f, 0),
						new Vertex(slider.getWidthXY(TransformSpace.LOCAL) - 3, 
						        -slider.getHeightXY(TransformSpace.LOCAL) 
						        / 4f - 2
						        + slider.getHeightXY(TransformSpace.LOCAL), 0),
						new Vertex(2, slider.getHeightXY(TransformSpace.LOCAL) 
						        / 2f, 0), });
		p.setFillColor(new MTColor(0, 0, 0, 180));
		p.setStrokeColor(new MTColor(160, 160, 160, 190));
		p.unregisterAllInputProcessors();
		p.setPickable(false);
		slider.getOuterShape().addChild(p);
		slider.getKnob().sendToFront();

		sceneTexture.setWidthLocal(pa.width);
		
	}

	/**
	 * Get dimension of the icon.
	 * 
	 * @return dimension
	 * 
	 * */
	public final float getIconDimension() {
        return iconDimension;
    }

	/**
	 * Set dimension of the icon.
	 * 
	 * @param iconDimension dimension
	 * */
    public final void setIconDimension(final float iconDimension) {
        this.iconDimension = iconDimension;
    }

    /**
	 * Prima di visualizzare le immagini nella parte centrale, vengono 
	 * ridimensionate in proporzione alla lunghezza dello schermo.
	 *
	 * @param width the width
	 * @param height the height
	 */
	private void resizeBackgroundImages(final int width, final int height) {
		try {

			File rootDirectory = new File(FilesPath.BACKGROUNDS_PATH);

			String[] listFiles = rootDirectory.list(new FilenameFilter() {
				// @Override
				public boolean accept(final File file, final String name) {
					return name.toLowerCase().endsWith(".jpg");
				}
			});

			for (String s : listFiles) {
				// resize to a fixed width (not proportional)
				BufferedImage bimg = ImageIO.read(new File(
				        FilesPath.RESIZEBACKGROUNDS_PATH + s));

				if (bimg.getWidth() != width || bimg.getHeight() != height) {
					System.out.println("\nresize\n");
					ImageResizer.resize(rootDirectory.getAbsolutePath() 
					        + File.separator + s, 
					        FilesPath.RESIZEBACKGROUNDS_PATH  
					        + s, width,	height);
				} else {
					System.out.println("\nNo need to resize\n");
				}
			}

		} catch (IOException ex) {
			System.out.println("Error resizing the image. ");
			ex.printStackTrace();
		}

	}

	/**
	 * Prima di visualizzare le previw degli sfondi nella seconda scena, 
	 * vengono ridimensionate in proporzione alla lunghezza dello schermo.
	 *
	 * @param width the width
	 * @param height the height
	 */
	private void resizeIconImages(final int width, final int height) {
		try {

			File rootDirectory = new File(FilesPath.BACKGROUNDS_PATH);

			String[] listFiles = rootDirectory.list(new FilenameFilter() {
				// @Override
				public boolean accept(final File file, final String name) {
					return name.toLowerCase().endsWith(".jpg");
				}
			});

			for (String s : listFiles) {
				BufferedImage bimg = ImageIO.read(new File(
				        FilesPath.RESIZEICONS_PATH + s));
				System.out.println("Confronto lunghezza originale: " 
				        + bimg.getWidth() + " con lunghezza finale: " + width);
				System.out.println("Confronto altezza originale: " 
				        + bimg.getHeight() + " con lunghezza finale: " 
				        + height);
				if (bimg.getWidth() != width || bimg.getHeight() != height) {
					// resize to a fixed width (not proportional)
					ImageResizer.resize(FilesPath.BACKGROUNDS_PATH + s,
							FilesPath.RESIZEICONS_PATH + s, width, height);
					System.out.println("Resize");
				} 
			}

		} catch (IOException ex) {
			System.out.println("Error resizing the image.");
			ex.printStackTrace();
		}

	}

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public final MTRoundRectangle getFrame() {
		return frame;
	}

	/**
	 * Gets the scene texture.
	 *
	 * @return the scene texture
	 */
	public final MTSceneTexture getSceneTexture() {
		return sceneTexture;
	}

	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#onEnter()
	 */
	public void onEnter() {


	}

	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#onLeave()
	 */
	public void onLeave() {	}

	/**
	 * @see org.mt4j.sceneManagement.AbstractScene#destroy()
	 */
	@Override
    public final boolean destroy() {
		boolean destroyed = super.destroy();
		if (destroyed) {
			drawingScene.destroy(); 
			//Destroy the scene manually since it isnt destroyed 
			//in the MTSceneTexture atm!
		}
		return destroyed;
	}

}
