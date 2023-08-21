package wwf.pictureBrowser;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.progressBar.MTProgressBar;
import org.mt4j.components.visibleComponents.widgets.video.MTMovieClip;
import org.mt4j.input.IMTEventListener;
import org.mt4j.input.MTEvent;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.DefaultZoomAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.camera.MTCamera;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PImage;
import wwf.database.DBAccess;
import wwf.pictureBrowser.data.SerializeObjects;
import wwf.pictureBrowser.data.dao.KeywordDaoImpl;
import wwf.pictureBrowser.data.dao.SocialNetworkDaoImpl;
import wwf.pictureBrowser.saveLocalPictures.SocialNetworkPicturesSaver;
import wwf.pictureBrowser.util.FilesPath;
import wwf.pictureBrowser.util.ImageResizer;


// TODO: Auto-generated Javadoc
/**
 * Classe principale dell'applicazione picture browser.
 */
public class PictureBrowser extends AbstractScene {
	
	/** The app. */
	private MTApplication app;
	
	/** The progress bar. */
	private MTProgressBar progressBar;
	
	/** The picture layer. */
	private MTComponent pictureLayer;
	
	/** The lasso processor. */
	private LassoProcessor lassoProcessor;
	
	/** The top layer. */
	private MTComponent topLayer;
	
	/** The foto files. */
	private LinkedList<File> fotoFiles = new LinkedList<File>();
	
	/** The video files. */
	private LinkedList<File> videoFiles = new LinkedList<File>();
	
	/** The foto files number. */
	private int foto_files_number = 0;
	
	/** The video files number. */
	private int video_files_number = 0;
	
	/** The videos. */
	private LinkedList<MTMovieClip> videos = new LinkedList<MTMovieClip>();
	
	/** The list. */
	private MTList list;

	/** Indica se è presente un aggiornamento nel db. */
	private boolean isUpdateDB = false;
	


    /**
	 * Inserisce uno sfondo interattivo e un menu. Ogni voce del menu 
	 * rappresenta la cartella cui vogliamo caricare le foto.
	 *
	 * @param mtAppl the mt appl
	 * @param name the name
	 */
	public PictureBrowser(final MTApplication mtAppl, final String name) {
		super(mtAppl, name);
		this.app = mtAppl;
		
		try {
            checkUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		new Sfondo(app, "Prova", getCanvas());
		
		FilesPath.createFilePaths();

		// Set a zoom limit
		final MTCamera camManager = new MTCamera(mtAppl);
		this.setSceneCam(camManager);
		this.getSceneCam().setZoomMinDistance(80);
		this.setClearColor(new MTColor(70, 70, 72, 255));

		// Show touches
		registerGlobalInputProcessor(new CursorTracer(app, this));


		this.getCanvas().registerInputProcessor(
		        new PanProcessorTwoFingers(app));
		this.getCanvas().addGestureListener(PanProcessorTwoFingers.class, 
		        new DefaultPanAction());

		this.getCanvas().registerInputProcessor(new ZoomProcessor(app));
		this.getCanvas().addGestureListener(ZoomProcessor.class, 
		        new DefaultZoomAction());

		pictureLayer = new MTComponent(app);

		topLayer = new MTComponent(app, "top layer group", new MTCamera(app));

		// Load from classpath
		progressBar = new MTProgressBar(app, app.createFont("arial", 18));

		progressBar.setDepthBufferDisabled(true);
		progressBar.setVisible(false);
		topLayer.addChild(progressBar);


		this.getCanvas().addChild(pictureLayer);
		this.getCanvas().addChild(topLayer);

		SocialNetworkPicturesSaver flickrSaver = 
		        new SocialNetworkPicturesSaver(app,  this);
		flickrSaver.start();

		generateTopLeftMenu();
		loadInitialScene();
		//ShowVideoPreview showVideoPreview = new ShowVideoPreview(videos);
		//showVideoPreview.start();
	}


    /**
	 * Metodo che provvede alla creazione del menu.
	 */
	private void generateTopLeftMenu() {
		//Apri cartella button
		IFont font = FontManager.getInstance().createFont(
		        app, "SansSerif", 45, MTColor.WHITE, false);
		MTRoundRectangle r = getRoundRectWithText(
		        0, 0, 300, 100, "Apri cartella", font);
		r.setFillColor(new MTColor(30, 30, 30, 255));
		r.registerInputProcessor(new TapProcessor(getMTApplication()));
		r.addGestureListener(TapProcessor.class, 
		        new DefaultButtonClickAction(r));
		r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.isTapped()) {
					if (list.isVisible()) {
						list.setVisible(false);
					} else {
						list.setVisible(true);
					}
				}
				return false;
			}
		});
		r.setPositionGlobal(new Vector3D(
		        r.getWidthXY(TransformSpace.GLOBAL) / 2f + 3,
		        r.getHeightXY(TransformSpace.GLOBAL) / 2f + 3));
		this.getCanvas().addChild(r);

		//Image list
		float cellWidth = 500;
		float cellHeight = 100;
		list = new MTList(getMTApplication(), 
		        r.getWidthXY(TransformSpace.GLOBAL) + 5, 0, 
		        cellWidth + 2, 8 * cellHeight + 8 * 3);
		list.setNoFill(true);
		list.setNoStroke(true);
		list.unregisterAllInputProcessors();
		list.setAnchor(PositionAnchor.UPPER_LEFT);
		list.setVisible(false);

		MTColor cellFillColor = new MTColor(MTColor.BLACK);
		MTColor cellPressedFillColor = new MTColor(new MTColor(105,105,105));

		File dir = new File(FilesPath.CUSTOM_FOTOS_DIR_PATH);
		System.out.println("Cerco in " + dir.getAbsolutePath());
		File[] fList = dir.listFiles();
		int j = 0;

		while (j < fList.length) {
			if (fList[j].isDirectory()) {
				list.addListElement(this.createListCell(
				        fList[j], fList[j].getName(), font,
				        cellWidth, cellHeight, cellFillColor, 
				        cellPressedFillColor));
			} 
			j++;
		}
		list.addListElement(this.createListCell(null, 
		        "Social networks", font, cellWidth, cellHeight, 
		        cellFillColor, cellPressedFillColor));

		this.getCanvas().addChild(list);
	}

	/**
	 * Crea una voce del menu.
	 *
	 * @param dir the dir
	 * @param imageName the image name
	 * @param font the font
	 * @param cellWidth the cell width
	 * @param cellHeight the cell height
	 * @param cellFillColor the cell fill color
	 * @param cellPressedFillColor the cell pressed fill color
	 * @return the MT list cell
	 */
	private MTListCell createListCell(final File dir, 
	        final String imageName, final IFont font, 
	        final float cellWidth, final float cellHeight,
	        final MTColor cellFillColor, 
	        final MTColor cellPressedFillColor) {
	    
		final MTListCell cell =
		        new MTListCell(getMTApplication(), cellWidth, cellHeight);
		cell.setFillColor(cellFillColor);
		MTTextArea listLabel = new MTTextArea(getMTApplication(), font);
		listLabel.setNoFill(true);
		listLabel.setNoStroke(true);
		if (imageName != null) {
            listLabel.setText(imageName);
        }
		cell.addChild(listLabel);
		listLabel.setPositionRelativeToParent(cell.getCenterPointLocal());
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(getMTApplication(), 15));
		cell.addGestureListener(TapProcessor.class,
		        new IGestureEventListener() {
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				switch (te.getTapID()) { 
				case TapEvent.TAP_DOWN:
					cell.setFillColor(cellPressedFillColor);
					break;
				case TapEvent.TAP_UP:
					cell.setFillColor(cellFillColor);
					break;
				case TapEvent.TAPPED:
					//System.out.println("Button clicked: " + label);
					cell.setFillColor(cellFillColor);
					list.setVisible(false);
					//loadingScreen.setVisible(true);
					registerPreDrawAction(new IPreDrawAction() {
						public void processAction() {
							getMTApplication().invokeLater(new Runnable() {
								public void run() {
									if (dir != null)  {
									addLocalPictures(dir.getAbsolutePath());
									} else {
										searchSocialNetworkPictures();	
									}
								}
							});
						}
						public boolean isLoop() {
						    return false;
						}
					});
					break;
                default:
                    break;
				}
				return false;
			}
		});
		return cell;
	}

	/**
	 * Gets the round rect with text.
	 *
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 * @param text the text
	 * @param font the font
	 * @return the round rect with text
	 */
	private MTRoundRectangle getRoundRectWithText(final float x, 
	        final float y, final float width, final float height, 
	        final String text, final IFont font) {
		MTRoundRectangle r = new MTRoundRectangle(
		        getMTApplication(), x, y, 0, width, height, 12, 12);
		r.unregisterAllInputProcessors();
		r.setFillColor(MTColor.BLACK);
		r.setStrokeColor(MTColor.BLACK);
		MTTextArea rText = new MTTextArea(getMTApplication(), font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(text);
		r.addChild(rText);
		rText.setPositionRelativeToParent(r.getCenterPointLocal());
		return r;
	}

	/**
	 * Load initial scene.
	 */
	public final void loadInitialScene() {
		addLocalPictures(FilesPath.FOTOS_PATH);
		//loadLocalVideo();
		searchSocialNetworkPictures();
	}

	/**
	 * Carica i video presenti nella cartella localee.
	 */
	private void loadLocalVideo() {
		File file = new File(FilesPath.VIDEOS_PATH);
		getVideoFiles(file);
		Iterator<File> it = videoFiles.iterator();

		while (it.hasNext()) {
			File videoFile = (File) it.next();

			MTMovieClip video = new MTMovieClip(
			        videoFile.toString(), new Vertex(100, 100), 
			        getMTApplication());
			videos.add(video);
			while (video.getHeightXY(TransformSpace.LOCAL) > app.height / 5f
					&& video.getWidthXY(TransformSpace.LOCAL) 
					> app.height / 5f) {
				video.setHeightLocal(video.getHeightXY(
				        TransformSpace.LOCAL) / 1.1f);
				video.setWidthLocal(video.getWidthXY(
				        TransformSpace.LOCAL) / 1.1f);
			}

			video.rotateZ(video.getCenterPointRelativeToParent(), 
			        ToolsMath.getRandom(0, 359));
			video.setPositionGlobal(
					new Vector3D(ToolsMath.getRandom(app.width / 4f
					        /* 0 */, 3 * app.width / 4f /* 359 */),
							ToolsMath.getRandom(app.height / 3f /* 0 */, 
							        3 * app.height / 3f /* 359 */), 0));
			video.getCloseButton().setVisible(false);
			video.setGestureAllowance(ScaleProcessor.class, true);
			video.addGestureListener(DragProcessor.class, 
			        new InertiaDragAction());
			video.setNoStroke(true);
			pictureLayer.addChild(video);
		}
	}

	/**
	 * Aggiunge le foto presenti in una cartelle sullo schermo interrattivo.
	 *
	 * @param path percorso cui vogliamo caricare le foto
	 */
	private void addLocalPictures(final String path) {
		//remove all pictures from the workspace
		MTComponent[] components = pictureLayer.getChildren();
		for (int i = 0; i < components.length; i++) {
			pictureLayer.removeChild(components[i]);
			components[i].destroy();
		}
		pictureLayer.removeAllChildren();
		
		//load and add new pictures to the workspace
		File file = new File(path);
		// final File files[] = file.listFiles();
		fotoFiles = getPictureFiles(file);

		Iterator<File> it = fotoFiles.iterator();

		while (it.hasNext()) {
			File fotoFile = (File) it.next();
			
		     String imagePath = "";
		        try {
		            imagePath = ImageResizer.resize(fotoFile.toString());
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		    PImage p = app.loadImage(imagePath);

			MTImageCustomized foto = new MTImageCustomized(app, p);
			foto.setNoStroke(false);
			foto.setStrokeWeight(10);
			foto.setDisplayCloseButton(true);
			
			foto.setWidthXYGlobal(app.getWidth() / 5);
			
			foto.rotateZ(foto.getCenterPointRelativeToParent(), 
			        ToolsMath.getRandom(0, 359));
			foto.setPositionGlobal(
					new Vector3D(ToolsMath.getRandom(app.width / 4f, 
					        3 * app.width / 4f ),
							ToolsMath.getRandom(app.height / 3f, 
							        3 * app.height / 3f ), 0));

			foto.setGestureAllowance(ScaleProcessor.class, true);
			foto.addGestureListener(DragProcessor.class, 
			        new InertiaDragAction(200, .95f, 17));
			foto.setNoStroke(true);
			pictureLayer.addChild(foto);
		}
	}

	/**
	 * Restituisce, a partire da una cartella, tutte le immagini con 
	 * estensione jpeg presenti anche nelle sottocartelle.
	 *
	 * @param dir cartella root principale
	 * @return lista di file di immagini
	 */
	public final LinkedList<File> getPictureFiles(final File dir) {
		File[] fList = dir.listFiles();
		LinkedList<File> fotoFiles = new LinkedList<File>();
		FileNameExtensionFilter image_filter =
		        new FileNameExtensionFilter("JPEG file", "jpeg", "jpg");
		int j = 0;

		while (j < fList.length) {
			if (fList[j].isDirectory()) {
				getPictureFiles(fList[j]);
			} else if (fList[j].isFile()) {
				if (image_filter.accept(fList[j])) {
					System.out.println("Path " + fList[j]);
					// System.out.println(""+files.length);
					fotoFiles.add(fList[j]);
				}
			}
			j++;
		}
		return fotoFiles;
	}

	/**
	 * crea, a partire da una cartella, una lista tutti i video con estensione 
	 * mp4 presenti anche nelle sottocartelle. Viene istanziato {@link #videoFiles}}
	 *
	 * @param dir cartella root principale
	 */
	public final void getVideoFiles(final File dir) {
		File[] fList = dir.listFiles();
		FileNameExtensionFilter videoFilter =
		        new FileNameExtensionFilter("MPEG file", "mp4");
		int j = 0;

		while (j < fList.length) {
			if (fList[j].isDirectory()) {
				getVideoFiles(fList[j]);
			} else if (fList[j].isFile()) {
				if (videoFilter.accept(fList[j])) {
					videoFiles.add(fList[j]);
				}
			}
			j++;
		}
	}

	/**
	 * Carica le foto dai social networks.
	 */
	private void searchSocialNetworkPictures() {
		
		MTComponent[] components = pictureLayer.getChildren();
		for (int i = 0; i < components.length; i++){
			pictureLayer.removeChild(components[i]);
			components[i].destroy();
		}
		pictureLayer.removeAllChildren();

		fotoFiles.clear();
		videoFiles.clear();

		// Create flickr loader thread
		final SocialNetworkPicturesLoader flickrLoader
		= new SocialNetworkPicturesLoader(app, 300, this, 5);
		// Set number photo flickr
		// Define action when loader thread finished
		flickrLoader.addProgressFinishedListener(new IMTEventListener() {
			public void processMTEvent(final MTEvent mtEvent) {
				// Add the loaded fotos in the main drawing thread to
				// avoid threading problems
				registerPreDrawAction(new IPreDrawAction() {
					public void processAction() {
						//	progressBar.setVisible(false);
					}
					public boolean isLoop() {
						return false;
					}
				});
			}
		});
		progressBar.setProgressInfoProvider(flickrLoader);
		progressBar.setVisible(true);
		// Run the thread
		flickrLoader.start();
		// Clear textarea
	}

	/**
	 * Aggiunge una foto nello schermo interattivo.
	 *
	 * @param card foto da visualizzare
	 */
	public final void addPhotoToCanvas(final MTImageCustomized card) {
		card.setUseDirectGL(true);
		card.setDisplayCloseButton(false);
		card.setPositionGlobal(new Vector3D(ToolsMath.getRandom(10,
		        MT4jSettings.getInstance().getWindowWidth() - 100),
				ToolsMath.getRandom(10, MT4jSettings.getInstance()
				        .getWindowHeight() - 50), 0));
		card.scale(0.6f, 0.6f, 0.6f,
		        card.getCenterPointLocal(), TransformSpace.LOCAL);
		card.addGestureListener(DragProcessor.class, 
		        new InertiaDragAction(200, .95f, 17));
		if (card instanceof SocialNetworkPicture) {
            if (((SocialNetworkPicture) card).getDescription().length() > 0) {
                addInfoToPhoto(card, ((SocialNetworkPicture) card)
                        .getDescription());
            }
        }
		//lassoProcessor.addClusterable(card); // make fotos lasso-able
		pictureLayer.addChild(card);
	}

	private void checkUpdate() throws SQLException {
       Connection connection = DBAccess.getConnection();
       Statement stmt = connection.createStatement();

       List<Date> dates = new ArrayList<Date>();
       List<String> tables = new ArrayList<String>();
       
       tables.add(new KeywordDaoImpl().getTableName());
       tables.add(new SocialNetworkDaoImpl().getTableName());

       ResultSet rs = null;

       String query = "SELECT MAX(time) FROM (";

       Iterator<String> it = tables.iterator();
       while(it.hasNext()) {
           String tableName = it.next();

           query = query.concat(" SELECT ");
           query = query.concat(tableName);
           query = query.concat(".create_time as time");
           query = query.concat(" FROM ");
           query = query.concat(tableName);
           if(it.hasNext())
               query = query.concat(" UNION ");
       }
       query = query.concat(") foo;");
       
       rs = stmt.executeQuery(query);
       while(rs.next()) {
           if(rs.getTimestamp(1)!=null) {
               Date data = new Date(rs.getTimestamp(1).getTime());
               dates.add(data);               
           }           
       }

       String fileName = "mostRecentData.dat";

       Date mostRecentDB = dates.get(0);
       if(dates.size()>0)
           mostRecentDB = Collections.max(dates); // avrà sempre un solo elemento
        Date mostRecentLocal = null;
       
        File file = new File(FilesPath.SERIALIZABLE_PATH + fileName);
        
        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(
                        new FileInputStream(FilesPath.SERIALIZABLE_PATH + fileName));
                mostRecentLocal = (Date) 
                        ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
        	mostRecentLocal = new Date(200);
            FileOutputStream out;
			try {
				out = new FileOutputStream(FilesPath.SERIALIZABLE_PATH + fileName);
				ObjectOutputStream oout = new ObjectOutputStream(out);

	            // write something in the file
	            oout.writeObject(mostRecentLocal);
	            // close the stream
	            oout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }

        if(mostRecentLocal.compareTo(mostRecentDB) < 0) {
            ObjectOutputStream output = null;
            
            try {
                SerializeObjects serialize = new SerializeObjects();
                serialize.run();

                output = new ObjectOutputStream(
                        new FileOutputStream(FilesPath.SERIALIZABLE_PATH + fileName));
                output.writeObject(mostRecentDB);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //this.isUpdateDB = true;
        }
        
	}
	/**
	 * Adds the info to photo.
	 *
	 * @param card the card
	 * @param description the description
	 */
	private void addInfoToPhoto(final MTImage card, final String description) {
		/*FontManager.getInstance().createFont(app, "arial.ttf", 16, // Font
				// size
				MTColor.WHITE, // Font fill color
				true);*/
	    String imagePath = "";
	    try {
            imagePath = ImageResizer.resize(FilesPath.IMAGES_PATH + "info.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		app.loadImage(imagePath);
	}

    /**
	 * @see org.mt4j.sceneManagement.AbstractScene#onEnter()
	 */
	public final void onEnter() {
		getMTApplication().registerKeyEvent(this);
	}

    /**
	 * @see org.mt4j.sceneManagement.AbstractScene#onLeave()
	 */
	public final void onLeave() {
		getMTApplication().unregisterKeyEvent(this);
	}

	/**
	 * Key event.
	 *
	 * @param e the e
	 */
	public final void keyEvent(final KeyEvent e) {
		int evtID = e.getID();
		if (evtID != KeyEvent.KEY_PRESSED) {
            return;
        }
		switch (e.getKeyCode()) {
		case KeyEvent.VK_BACK_SPACE:
			app.popScene();
			break;
		case KeyEvent.VK_F1:
			this.setClearColor(new MTColor(100, 99, 99, 255));
			break;
		case KeyEvent.VK_F2:
			this.setClearColor(new MTColor(120, 119, 119, 255));
			break;
		case KeyEvent.VK_F3:
			this.setClearColor(new MTColor(130, 129, 129, 255));
			break;
		case KeyEvent.VK_F4:
			this.setClearColor(new MTColor(160, 159, 159, 255));
			break;
		case KeyEvent.VK_F5:
			this.setClearColor(new MTColor(180, 179, 179, 255));
			break;
		case KeyEvent.VK_F6:
			this.setClearColor(new MTColor(100, 100, 102, 255));
			break;
		case KeyEvent.VK_F7:
			this.setClearColor(new MTColor(70, 70, 72, 255));
			break;
		case KeyEvent.VK_F:
			System.out.println("FPS: " + app.frameRate);
			break;
		default:
			break;
		}
	}

	public boolean isUpdateDB() {
        return isUpdateDB;
    }

	/**
	 * Gets the top layer.
	 *
	 * @return the top layer
	 */
	public final MTComponent getTopLayer() {
		return topLayer;
	}

	/**
	 * Sets the top layer.
	 *
	 * @param topLayer the new top layer
	 */
	public final void setTopLayer(final MTComponent topLayer) {
		this.topLayer = topLayer;
	}

	/**
	 * Gets the progress bar.
	 *
	 * @return the progress bar
	 */
	public final MTProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * The Class ShowVideoPreview.
	 */
	class ShowVideoPreview extends Thread {

		/** The clips. */
		private LinkedList<MTMovieClip> clips = new LinkedList<MTMovieClip>();

		/**
		 * Instantiates a new show video preview.
		 *
		 * @param clips the clips
		 */
		ShowVideoPreview(final LinkedList<MTMovieClip> clips) {
			super();
			this.clips = clips;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			super.run();
			Iterator<MTMovieClip> it  = clips.iterator();
			while (it.hasNext()) {
				MTMovieClip mtMovieClip = (MTMovieClip) it.next();
				mtMovieClip.getMovieClip().play();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mtMovieClip.getMovieClip().pause();
			}
		}

	}
	
}