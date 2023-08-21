package wwf.ClassificationGame.view;

import static wwf.ClassificationGame.util.Utility.*;
import static wwf.ClassificationGame.util.MyFonts.specieSplashScene;
import java.sql.SQLException;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import wwf.ClassificationGame.data.model.CGClasse;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.util.FilesPath;
import wwf.ClassificationGame.util.MyColors;

public class SplashScene {
	private MTComponent root;
	private MTApplication context;
	private CGSceneManager cgScene;

	/**
	 * Inserimento delle nuove specie per la prima colonna
	 */
	private int i = 0;

	/**
	 * Inserimento delle nuove specie per la seconda colonna
	 */
	private int j = 0;

	/**
	 * Numero di specie inseribili in una colonna
	 */
	private int count = 1;

	public SplashScene(CGSceneManager cgScene, MTApplication app) throws SQLException {
		root = new MTComponent(app);
		this.context = app;
		this.cgScene = cgScene;

		buildIntestazione(app, cgScene, root, "splash");
		buildCorpo();
	}

	public void buildCorpo() throws SQLException {
		MyColors color = new MyColors();
		IFont fontSpecie = specieSplashScene(context);

		for (CGClasse specie : listaClassi(cgScene)) {
			String nomeSpecie = specie.getSpecie().toLowerCase();
			String colore = specie.getColore();

			MTRectangle specieButton = new MTRectangle(context, 480, 200);
			specieButton.setStrokeColor(MTColor.BLACK);
			color.setColor(colore);
			specieButton.setFillColor(color.getColor());

			MTTextArea specieNome = new MTTextArea(context);
			specieNome.setFont(fontSpecie);
			specieNome.setText("Informazioni" + "\n" + specie.getSpecie());

			existsImage(nomeSpecie, "data/cg/");
			MTRectangle logoTexture = insertImage(context);

			if (count <= 3) {
				primaColonnaSpecie(specieButton, specieNome, logoTexture);
			} else {
				secondaColonnaSpecie(specieButton, specieNome, logoTexture);
			}

			proprietàSpecie(context, root, specie, specieButton, specieNome, logoTexture);

			specieButton.addGestureListener(TapProcessor.class, new gestureListenerSpecie(specie));
			specieNome.addGestureListener(TapProcessor.class, new gestureListenerSpecie(specie));
			logoTexture.addGestureListener(TapProcessor.class, new gestureListenerSpecie(specie));

			count++;
		}
		giocatoreSingolo(fontSpecie);
		multiGiocatore(fontSpecie);
	}

	private void primaColonnaSpecie(MTRectangle specieButton, MTTextArea specieNome, MTRectangle logoTexture) {
		specieButton.setPositionGlobal(new Vector3D(context.width / 7, context.height / 3 + (i + 145)));
		specieNome.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 365, context.getAlignmentY() + 95));
		logoTexture.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 125, context.getAlignmentY() + 105));

		i = i + 225;
	}

	private void secondaColonnaSpecie(MTRectangle specieButton, MTTextArea specieNome, MTRectangle logoTexture) {
		specieButton.setPositionGlobal(new Vector3D(context.width / 2 - 30, context.height / 3 + (j + 145)));
		specieNome.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 365, context.getAlignmentY() + 95));
		logoTexture.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 125, context.getAlignmentY() + 105));

		j = j + 225;
	}

	private void giocatoreSingolo(IFont fontSpecie) {
		MTRectangle singleButton = new MTRectangle(context, 480, 200);
		singleButton.setPositionGlobal(new Vector3D(context.width - 290, context.height / 3 + 145));
		singleButton.setFillColor(MyColors.orange());
		singleButton.setStrokeColor(MTColor.BLACK);

		MTTextArea singleText = new MTTextArea(context);
		singleText.setPositionRelativeToOther(singleButton,
				new Vector3D(context.getAlignmentX() + 260, context.getAlignmentY() + 65));
		singleText.setFont(fontSpecie);
		String giocatore = "Giocatore ";
		String singolo = "singolo";
		String singlePlayer = giocatore + singolo;
		singleText.setText(giocatore + "\n" + singolo);

		PImage logosingle = context.loadImage(FilesPath.rootPath + "singleplayer.png");
		MTRectangle singleTexture = new MTRectangle(context, logosingle);
		singleTexture.setPositionRelativeToOther(singleButton,
				new Vector3D(context.getAlignmentX() + 115, context.getAlignmentY() + 100));

		proprietàGiocatore(context, root, singleButton, singleText, singleTexture);

		singleButton.addGestureListener(TapProcessor.class, new gestureListenerGame(singlePlayer));
		singleText.addGestureListener(TapProcessor.class, new gestureListenerGame(singlePlayer));
		singleTexture.addGestureListener(TapProcessor.class, new gestureListenerGame(singlePlayer));
	}

	private void multiGiocatore(IFont fontSpecie) {
		MTRectangle multiButton = new MTRectangle(context, 480, 200);
		multiButton.setPositionGlobal(new Vector3D(context.width - 290, context.height / 3 + 370));
		multiButton.setFillColor(MyColors.red());
		multiButton.setStrokeColor(MTColor.BLACK);

		MTTextArea multiText = new MTTextArea(context);
		multiText.setPositionRelativeToOther(multiButton,
				new Vector3D(context.getAlignmentX() + 260, context.getAlignmentY() + 60));
		multiText.setFont(fontSpecie);
		String sfida = "Sfida a ";
		String multi = "piu' giocatori";
		String multiPlayer = sfida + multi;
		multiText.setText(sfida + "\n" + multi);

		PImage logomulti = context.loadImage(FilesPath.rootPath + "multiplayer.png");
		MTRectangle multiTexture = new MTRectangle(context, logomulti);
		multiTexture.setPositionRelativeToOther(multiButton,
				new Vector3D(context.getAlignmentX() + 115, context.getAlignmentY() + 100));

		proprietàGiocatore(context, root, multiButton, multiText, multiTexture);

		multiButton.addGestureListener(TapProcessor.class, new gestureListenerGame(multiPlayer));
		multiText.addGestureListener(TapProcessor.class, new gestureListenerGame(multiPlayer));
		multiTexture.addGestureListener(TapProcessor.class, new gestureListenerGame(multiPlayer));
	}

	public class gestureListenerSpecie implements IGestureEventListener {
		private CGClasse specie;

		public gestureListenerSpecie(CGClasse specie) {
			super();
			this.specie = specie;
		}

		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					cgScene.setClasse(specie);
					try {
						root.removeAllChildren();
						root.destroy();
						System.gc();
						cgScene.buildInformationScene();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		}
	}

	public class gestureListenerGame implements IGestureEventListener {
		String game;

		public gestureListenerGame(String game) {
			super();
			this.game = game;
		}

		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					cgScene.setGame(game);
					try {
						root.removeAllChildren();
						root.destroy();
						System.gc();
						cgScene.buildGameRegoleScene();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		}
	}

	public MTComponent getRootComponent() {
		return root;
	}

}