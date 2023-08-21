package wwf.drawing;

import java.io.File;
import java.io.FilenameFilter;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;


/**
 * Classe che mostra tutti gli sfondi che possono essere scelti nella 
 * schermata principale.
 */
public class DrawBackgrounds {

	/** The canvas. */
	private MTCanvas canvas;
	
	/** The list. */
	private MTList list;
	
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
	
	/** The frame. */
	private MTRectangle frame;
	
	/** The background. */
	private MTRectangle background;
	
	/** The mt application. */
	private MTApplication mtApplication;
	
	/** The font. */
	private IFont font; 
	
	/** The selected image. */
	private static String selectedImage;

	/**
	 * Costruttore parametrico.
	 * 
	 * @param mtApplication riferimento dell'applicazione corrente
	 * @param canvas the canvas
	 */
	public DrawBackgrounds(final MTApplication mtApplication, 
	        final MTCanvas canvas) {
		this.mtApplication = mtApplication;
		this.canvas = canvas;
	}

	/**
	 * metodo che mostra una preview degli sfondi nella scena.
	 */
	public final void drawBackgrounds() {
		frame = new MTRectangle(mtApplication, -50, -50, 
		        mtApplication.width + 100,
		        mtApplication.height + 100);
		frame.setSizeXYGlobal(mtApplication.width - 10, 
		        mtApplication.height - 10);
		frame.removeAllGestureEventListeners();
		frame.unregisterAllInputProcessors();

		frame.setFillColor(new MTColor(146, 150, 188, 255));

		canvas.addChild(frame);

		MTTextArea textField = new MTTextArea(mtApplication,
				FontManager.getInstance().createFont(mtApplication,
				        "arial.ttf", 50, MTColor.WHITE));
		textField.setNoFill(true);
		textField.setNoStroke(true);
		textField.setText("Select background");
		textField.setPositionGlobal(new Vector3D(
		        mtApplication.width / 2f, 38f));
		textField.removeAllGestureEventListeners();
		textField.unregisterAllInputProcessors();
		frame.addChild(textField);

		preferredIconWidth = 256;
		preferredIconHeight = 192;
		gapBetweenIconAndReflection = 9;
		displayHeightOfReflection = preferredIconHeight * 0.6f;

		listWidth = preferredIconHeight + displayHeightOfReflection 
		        + gapBetweenIconAndReflection;
		listHeight = mtApplication.width;
		list = new MTList(mtApplication, 0, 0, listWidth, listHeight, 40);
		list.setFillColor(new MTColor(150, 150, 150, 200));
		list.setNoFill(true);
		list.setNoStroke(true);

		font = FontManager.getInstance().createFont(
		        mtApplication, "SansSerif", 18, MTColor.WHITE);

		frame.addChild(list);
		list.rotateZ(list.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		list.setPositionGlobal(new Vector3D(mtApplication.width / 2f,
		        mtApplication.height / 2f));
		canvas.setFrustumCulling(true); 

		File rootDirectory = new File(FilesPath.RESIZEICONS_PATH);

		String[] listFiles = rootDirectory.list(new FilenameFilter() {
			// @Override
			public boolean accept(final File file, final String name) {
				return name.toLowerCase().endsWith(".jpg");
			}
		});

		for (String s : listFiles) {
			PImage img = mtApplication.loadImage(FilesPath.RESIZEICONS_PATH 
					+ MTApplication.separator + s);
			this.addImage(img, s);
		}

		PImage arrow = mtApplication.loadImage(
		        FilesPath.IMAGES_PATH + "arrowRight.png");
		MTImageButton previousSceneButton = 
		        new MTImageButton(mtApplication, arrow);
		previousSceneButton.setNoStroke(true);
		if (MT4jSettings.getInstance().isOpenGlMode()) {
            previousSceneButton.setUseDirectGL(true);
        }
		previousSceneButton.addGestureListener(TapProcessor.class,
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					frame.setVisible(false);
					MainDrawingScene mainScene = ((MainDrawingScene) 
					        mtApplication.getScene("MT Paint"));
					mainScene.getFrame().setVisible(true);
				}
				return true;
			}
		});

		frame.addChild(previousSceneButton);
		previousSceneButton.scale(-1, 1, 1, 
		        previousSceneButton.getCenterPointLocal(), 
		        TransformSpace.LOCAL);
		previousSceneButton.setPositionGlobal(new Vector3D(
		        previousSceneButton.getWidthXY(TransformSpace.GLOBAL) + 5,
				mtApplication.height - previousSceneButton.getHeightXY(
				        TransformSpace.GLOBAL) - 5, 0));


		frame.registerInputProcessor(new FlickProcessor());
		frame.addGestureListener(FlickProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				FlickEvent e = (FlickEvent) ge;
				if (e.getId() == MTGestureEvent.GESTURE_ENDED && e.isFlick()) {
					switch (e.getDirection()) {
					case EAST:
					case NORTH_EAST:
					case SOUTH_EAST:
					    mtApplication.popScene();
						break;
					default:
						break;
					}
				}
				return false;
			}
		});


	}

	/**
	 * aggiunge la preview di una immagine nella scena, che puo essere 
	 * usata come sfondo.
	 *
	 * @param icon icona dell'immagine
	 * @param imageName nome dell' imaggine
	 */
	public final void addImage(final PImage icon, final String imageName) {
		PImage reflection = this.getReflection(mtApplication, icon);

		float border = 1;
		float bothBorders = 2 * border;
		float topShift = 30;
		float reflectionDistanceFromImage = 
		        topShift + gapBetweenIconAndReflection; 

		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;
		float listCellHeight = preferredIconWidth;

		MTListCell cell = new MTListCell(mtApplication, 
		        realListCellWidth, listCellHeight);
		cell.setNoFill(true);
		cell.setNoStroke(true);

        Vertex[] vertices = new Vertex[] { new Vertex(realListCellWidth 
                - topShift, border, 0, 0, 0),
                new Vertex(realListCellWidth - topShift, listCellHeight 
                        - border, 0, 1, 0),
                new Vertex(realListCellWidth - topShift - icon.height, 
                        listCellHeight - border, 0, 1, 1),
                new Vertex(realListCellWidth - topShift - icon.height, 
                        border, 0, 0, 1),
                new Vertex(realListCellWidth - topShift,
                        border, 0, 0, 0), };
		MTPolygon p = new MTPolygon(mtApplication, vertices);
		p.setTexture(icon);
		p.setNoStroke(true);
		p.setStrokeColor(new MTColor(80, 80, 80, 255));

        Vertex[] verticesRef = new Vertex[] {
                new Vertex(listCellWidth - icon.height 
                        - reflectionDistanceFromImage, border, 0, 0, 0),
                new Vertex(listCellWidth - icon.height 
                        - reflectionDistanceFromImage, 
                        listCellHeight - border, 0, 1, 0),
                new Vertex(listCellWidth - icon.height 
                        - reflection.height - reflectionDistanceFromImage,
                        listCellHeight - border, 0, 1, 1),
                new Vertex(listCellWidth - icon.height 
                        - reflection.height 
                        - reflectionDistanceFromImage, border, 0, 0, 1),
                new Vertex(listCellWidth - icon.height
                        - reflectionDistanceFromImage, border, 0, 0, 0), };
		MTPolygon pRef = new MTPolygon(mtApplication, verticesRef);
		pRef.setTexture(reflection);
		pRef.setNoStroke(true);

		cell.addChild(p);
		cell.addChild(pRef);
		list.addListElement(cell);

		cell.registerInputProcessor(new TapProcessor(mtApplication, 15));

		cell.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					setSelectedImage(imageName);
					frame.setVisible(false);

					Iscene[] scenes = mtApplication.getScenes();
					System.out.println(scenes.length);
					MainDrawingScene mainScene = ((MainDrawingScene) 
					        mtApplication.getScene("MT Paint"));
					mainScene.getFrame().setVisible(true);

					if (imageName != null) {
						PImage im = mtApplication.loadImage(FilesPath.
						        RESIZEBACKGROUNDS_PATH + imageName);
						background = new MTRectangle(mtApplication, 96, 0, 
						        mtApplication.width - 96, mtApplication.height);
						mainScene.getFrame().addChild(background);
						//mainScene.getSceneTexture().getFbo()
						//	.clear(true, 0, 0, 0, 0, true);
						
						background.setTexture(im);
						
						background.addChild(mainScene.getSceneTexture());
						
					} 
				}
				return true;
			}
		});

		MTTextArea text = new MTTextArea(mtApplication, font);
		text.setFillColor(new MTColor(150, 150, 250, 200));
		text.setNoFill(true);
		text.setNoStroke(true);
		text.rotateZ(text.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		cell.addChild(text);

		text.setPositionRelativeToParent(cell.getCenterPointLocal());
		text.translate(new Vector3D(realListCellWidth * 0.5f
		        - text.getHeightXY(TransformSpace.LOCAL) * 0.5f, 0));
	}


	/**
	 * Restituisce l'ombra dell'immagine passata come parametro.
	 *
	 * @param pa applicazione corrente
	 * @param image immagine 
	 * @return ombra
	 */
	private PImage getReflection(final PApplet pa, final PImage image) {
		int width =  image.width; 
		int height = image.height;

		PImage copyOfImage = pa.createImage(image.width, 
		        image.height, PApplet.ARGB);
		image.loadPixels();
		copyOfImage.loadPixels();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int imageIndex = y * image.width + x;
				int currR = (image.pixels[imageIndex] >> 16)
				        & 0xFF;
				int currG = (image.pixels[imageIndex] >> 8) 
				        & 0xFF;
				int currB = image.pixels[imageIndex]
				        & 0xFF;

				int col = image.pixels[imageIndex];
				float alpha = pa.alpha(col);

				int reflectImageIndex = (image.height - y - 1) 
				        * image.width + x;

				if (alpha <= 0.0f) {
					copyOfImage.pixels[reflectImageIndex] 
					        = pa.color(currR, currG, currB, 0.0f); 
				} else {
					copyOfImage.pixels[reflectImageIndex] 
					        = pa.color(currR, currG, currB, Math.round(
					                y * y * y * (0.00003f) - 60)); //WORKS	
				}
			}
		} 
		copyOfImage.updatePixels();
		return copyOfImage;
	}

	/**
	 * seleziona l' Immagine selezionata.
	 *
	 * @param selectedImage the new selected image
	 */
	public static void setSelectedImage(final String selectedImage) {
		DrawBackgrounds.selectedImage = selectedImage;
	}

	/**
	 * Restituisce l'immagine selezionata.
	 *
	 * @return immagine selezionata
	 */
	public static String getSelectedImage() {
		return selectedImage;
	}


}
