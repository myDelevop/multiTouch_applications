package wwf.ClassificationGame.view;

import static wwf.ClassificationGame.util.Utility.*;
import static wwf.ClassificationGame.util.MyFonts.descrizioneEntita;
import java.sql.SQLException;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.MTColors;

import processing.core.PImage;
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.util.FilesPath;
import wwf.ClassificationGame.util.MyColors;
import wwf.ClassificationGame.util.Utility;

public class Information extends AbstractScene {
	private MTComponent root;
	private MTApplication context;
	private CGSceneManager cgScene;
	private MTRectangle categoriaButton;
	MTTextArea categoriaNome;
	private PImage entimg;
	private MTRectangle entitaContainer;
	private MTRectangle entimgRect;
	private MTRectangle descriptionEntitaRect;
	private MTList descriptionArea;

	public Information(final CGSceneManager cgScene, MTApplication app) throws SQLException {
		super(app, "Information");
		this.context = app;
		this.root = new MTComponent(app);
		this.cgScene = cgScene;

		setSelectedColor(cgScene);
		buildIntestazione(app, cgScene, root, "normal");
		attachEvent(cgScene, app, root, "normal", "normal");
		buildCategorie();
	}

	private void setSelectedColor(CGSceneManager cgScene) {
		MyColors color = new MyColors();
		color.setColor(cgScene.getClasse().getColore());
		this.cgScene.setClearColor(color.getColor());
	}

	private void buildCategorie() throws SQLException {
		int i = 0;

		for (CGCategoria categoria : listaCategorie(cgScene)) {
			if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie())) {
				categoriaButton = new MTRectangle(context, 280, 90);
				categoriaButton.setPositionGlobal(new Vector3D(context.width / 9 + (i), context.height / 5 + 30));
				categoriaButton.setStrokeColor(MTColor.BLACK);

				if (cgScene.getCategoria() == null) {
					setCategory(categoria, true);
				} else {
					if (categoria.getCategoria().equalsIgnoreCase(cgScene.getCategoria().getCategoria())) {
						setCategory(categoria, false);
					}
				}

				categoriaNome = new MTTextArea(context);
				categoriaNome.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 36, MTColor.BLACK));
				categoriaNome.setText(categoria.getCategoria());
				categoriaNome.setPositionRelativeToParent(new Vector3D(categoriaButton.getCenterPointGlobal().x,
						categoriaButton.getCenterPointGlobal().y));

				propriet‡Categoria(context, root, categoriaButton, categoriaNome);

				categoriaButton.addGestureListener(TapProcessor.class, new gestureListenerCategoria(categoria));
				categoriaNome.addGestureListener(TapProcessor.class, new gestureListenerCategoria(categoria));

				i = i + 300;
			}
		}
	}

	private void buildEntita() throws SQLException {
		entitaContainer = new MTRectangle(context, context.width / 20 - 3, context.height / 4 + 30, context.width - 140,
				context.height - 320);

		descriptionEntitaRect = new MTRectangle(context, context.width - 139, 300);
		descriptionEntitaRect.setPositionRelativeToOther(entitaContainer,
				new Vector3D(context.width / 2 + 4, context.height - 180));
		descriptionEntitaRect.setStrokeColor(MTColor.BLACK);
		descriptionEntitaRect.setFillColor(MyColors.lightgray());
		Utility.lockAll(descriptionEntitaRect);
		entitaContainer.addChild(descriptionEntitaRect);

		int j = 0;

		for (CGEntita entita : listaEntita(cgScene)) {
			if (entita.getIdCategoria().equals(cgScene.getCategoria().getIdCategoria())) {
				// Inserimento immagine entit‡
				existsImage(entita.getEntita().toLowerCase(), "data/cg/information/");
				entimg = context.loadImage(FilesPath.rootInformation + getExistsImage());
				entimg.resize(280, 320);
				entimgRect = new MTRectangle(context, entimg);
				entimgRect.setPositionRelativeToOther(entitaContainer,
						new Vector3D(context.width / 9 + (j + 10), context.height / 2 - 40));

				// Nessuna entit‡ scelta, se Ë stata scelta coloro l'entit‡
				// scelta e le altre rimaranno senza bordo
				if (cgScene.getEntita() == null) {
					setEntita(entita, true);
				} else if (entita.getEntita().equalsIgnoreCase(cgScene.getEntita().getEntita())) {
					setEntita(entita, false);
				} else
					Utility.lockStrokeRectangle(entimgRect);

				// Propriet‡
				entitaContainer.addChild(entimgRect);

				entimgRect.registerInputProcessor(new TapProcessor(context));
				entimgRect.addGestureListener(TapProcessor.class, new gestureListenerEntita(entita));

				j = j + 370;
			}
		}
		Utility.lockStrokeRectangle(entitaContainer);
		entitaContainer.setNoFill(true);

		root.addChild(entitaContainer);
	}

	private void setCategory(CGCategoria categoria, boolean isDefault) throws SQLException {
		if (isDefault) {
			cgScene.setCategoria(categoria);
			categoriaButton.setFillColor(MyColors.ocra());
			buildEntita();
		} else {
			categoriaButton.setFillColor(MyColors.ocra());
		}

	}

	private void setEntita(CGEntita entita, boolean isDefault) throws SQLException {
		if (isDefault) {
			cgScene.setEntita(entita);
			buildEntita();
		} else {
			// Bordo entit‡ selezionata
			entimgRect.setStrokeColor(MyColors.ocra());
			entimgRect.setStrokeWeight(5);
			Utility.lockAll(entimgRect);

			// Nome entit‡
			MTTextArea entitaNome = new MTTextArea(context);
			entitaNome.setPositionRelativeToParent(new Vector3D(27, 19));
			entitaNome.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 31, MTColor.BLACK));
			entitaNome.setText("Nome: " + entita.getEntita());
			Utility.lockStrokeFillText(entitaNome);
			descriptionEntitaRect.addChild(entitaNome);

			buildDescriptionEntita(entita);
		}

	}

	private void buildDescriptionEntita(CGEntita entita) {
		IFont descrizioneFont = descrizioneEntita(context);

		descriptionArea = new MTList(context, 0, 48, 1781, 252);
		descriptionArea.setFillColor(MyColors.lightgray());
		descriptionArea.setStrokeColor(MTColors.BLACK);
		descriptionEntitaRect.addChild(descriptionArea);

		MTListCell cellDescription = new MTListCell(context, 1778, 350);
		descriptionArea.addListElement(cellDescription);
		cellDescription.setFillColor(MyColors.lightgray());
		cellDescription.setNoStroke(true);

		MTTextArea descriptionText = new MTTextArea(context, 0, 0, 795.0f, 200.0f);
		descriptionText.setFillColor(MyColors.lightgray());
		descriptionText.setFont(descrizioneFont);
		descriptionText.setText("Descrizione: " + entita.getDescrizione());

		cellDescription.addChild(descriptionText);

		descriptionText.setPositionRelativeToParent(new Vector3D(888, 225));
		descriptionText.setWidthXYGlobal(cellDescription.getWidthXY(TransformSpace.GLOBAL) * 2);
		descriptionText.setHeightXYGlobal(cellDescription.getWidthXY(TransformSpace.GLOBAL) / 4);

		descriptionText.setNoStroke(true);
	}

	public class gestureListenerCategoria implements IGestureEventListener {
		CGCategoria categoria;

		public gestureListenerCategoria(CGCategoria categoria) {
			super();
			this.categoria = categoria;
		}

		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			if (te.getTapID() == TapEvent.TAPPED) {
				cgScene.setCategoria(categoria);
				try {
					removeCategorie();
					buildCategorie();

					if (entimgRect == null) {
						buildEntita();
					} else {
						cgScene.setEntita(null);
						removeEntita();
						buildEntita();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	public class gestureListenerEntita implements IGestureEventListener {
		CGEntita entita;

		public gestureListenerEntita(CGEntita entita) {
			super();
			this.entita = entita;
		}

		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent) ge;
			if (te.getTapID() == TapEvent.TAPPED) {
				cgScene.setEntita(entita);
				try {
					removeEntita();
					buildEntita();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	private void removeCategorie() {
		root.removeChild(categoriaButton);
		root.removeChild(categoriaNome);
		System.gc();
	}

	private void removeEntita() {
		root.removeChild(entitaContainer);
		System.gc();
	}

	public MTComponent getRootComponent() {
		return root;
	}

}