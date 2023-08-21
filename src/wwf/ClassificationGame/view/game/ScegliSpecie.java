package wwf.ClassificationGame.view.game;

import static wwf.ClassificationGame.util.Utility.*;
import static wwf.ClassificationGame.util.MyFonts.specieGioco;

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
import wwf.ClassificationGame.data.model.CGClasse;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.util.MyColors;
import wwf.ClassificationGame.util.Utility;

public class ScegliSpecie {
	private MTComponent root;
	private MTApplication context;
	private CGSceneManager cgScene;

	/**
	 * Inserimento delle nuove specie per la prima riga
	 */
	private int i = 0;

	/**
	 * Inserimento delle nuove specie per la seconda riga
	 */
	private int j = 0;

	/**
	 * Numero di specie inseribili in una riga
	 */
	private int count = 1;

	public ScegliSpecie(CGSceneManager cgScene, MTApplication app) throws SQLException {
		this.context = app;
		this.root = new MTComponent(app);
		this.cgScene = cgScene;

		this.cgScene.setClearColor(MyColors.lightorange());
		buildIntestazione(app, cgScene, root, "gioco");
		attachEvent(cgScene, app, root, "specie", "regole");
		questionGioco(app, root, "Con quale regno preferisci iniziare?", "scegliSpecie");
		scegliSpecie();
	}

	public void scegliSpecie() throws SQLException {
		MyColors color = new MyColors();

		// Cerco tutte le specie
		for (CGClasse specie : listaClassi(cgScene)) {
			String nomeSpecie = specie.getSpecie().toLowerCase();
			String colore = specie.getColore();

			// Rettangolo delle specie
			MTRectangle specieButton = new MTRectangle(context, 500, 250);
			specieButton.setStrokeColor(MTColor.BLACK);
			color.setColor(colore);
			specieButton.setFillColor(color.getColor());

			// Nomi delle specie
			MTTextArea specieNome = new MTTextArea(context);
			IFont specieFont = specieGioco(context);
			specieNome.setFont(specieFont);
			specieNome.setText(specie.getSpecie());

			Utility.existsImage(nomeSpecie, "data/cg/");
			MTRectangle logoTexture = insertImage(context);

			// Prima riga
			if (count <= 3) {
				primaRigaSpecie(specieButton, specieNome, logoTexture);
			} else {
				secondaRigaSpecie(specieButton, specieNome, logoTexture);
			}

			proprietàSpecie(context, root, specie, specieButton, specieNome, logoTexture);

			specieButton.addGestureListener(TapProcessor.class, new gestureListener(specie));
			specieNome.addGestureListener(TapProcessor.class, new gestureListener(specie));
			logoTexture.addGestureListener(TapProcessor.class, new gestureListener(specie));

			count++;
		}
	}

	private void primaRigaSpecie(MTRectangle specieButton, MTTextArea specieNome, MTRectangle logoTexture) {
		specieButton.setPositionGlobal(new Vector3D(context.width / 6 + (i + 50), context.height / 3 + 120));
		specieNome.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 350, context.getAlignmentY() + 115));
		logoTexture.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 125, context.getAlignmentY() + 130));

		i = i + 600;
	}

	private void secondaRigaSpecie(MTRectangle specieButton, MTTextArea specieNome, MTRectangle logoTexture) {
		specieButton.setPositionGlobal(new Vector3D(context.width / 6 + (j + 50), context.height - 250));
		specieNome.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 350, context.getAlignmentY() + 115));
		logoTexture.setPositionRelativeToOther(specieButton,
				new Vector3D(context.getAlignmentX() + 125, context.getAlignmentY() + 130));

		j = j + 600;
	}

	/**
	 * Gesture per la specie
	 */
	public class gestureListener implements IGestureEventListener {
		private CGClasse specie;

		public gestureListener(CGClasse specie) {
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
						if (cgScene.getGame().equalsIgnoreCase("Giocatore singolo")) {
							cgScene.buildSinglePlayerScene(null);
						} else
							cgScene.buildMultiPlayerScene(null, null);
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
