package wwf.ClassificationGame.view.game;

import static wwf.ClassificationGame.util.MyFonts.testoPopup;
import static wwf.ClassificationGame.util.MyFonts.titoloPopupRosso;
import static wwf.ClassificationGame.util.MyFonts.titoloPopupVerde;
import static wwf.ClassificationGame.util.Utility.*;

import java.sql.SQLException;
import java.util.LinkedList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.MTColors;

import processing.core.PImage;
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.sceneManager.Giocatore;
import wwf.ClassificationGame.util.FilesPath;
import wwf.ClassificationGame.util.MyColors;
import wwf.ClassificationGame.util.Utility;

public class MultiPlayer {
	private MTComponent root;
	private MTApplication context;
	private CGSceneManager cgScene;

	private MTRectangle containerCategorieSuper;
	private MTRectangle containerG1;
	private MTRectangle containerG2;
	private LinkedList<MTImage> categorieDomande = new LinkedList<MTImage>();

	// Categorie
	private LinkedList<CGCategoria> categorieGioco = new LinkedList<CGCategoria>();
	private MTRectangle categoriaTitleSuper;
	private MTImage categoriaImmagineSuper;
	private MTTextArea categoriaNomeSuper;

	// Categorie Giocatore 1
	private MTRectangle categorieContainerG1;
	private MTRectangle categoriaTitleG1;
	private MTImage categoriaImmagineG1;
	private MTTextArea categoriaNomeG1;
	private int spaceCategorieG1 = 0;

	// Entita Giocatore 1
	private MTRectangle entitaContainerG1;
	private MTImageEntita entitaImmagineG1;
	private MTTextArea entitaNomeG1;
	public int entitaTotaliG1 = 0;
	private int spaceEntitaPrimaRigaG1 = 0;
	private int spaceEntitaSecondaRigaG1 = 0;

	// Categorie Giocatore 2
	private MTRectangle categorieContainerG2;
	private MTRectangle categoriaTitleG2;
	private MTImage categoriaImmagineG2;
	private MTTextArea categoriaNomeG2;
	private int spaceCategorieG2 = 0;

	// Entita Giocatore 2
	private MTRectangle entitaContainerG2;
	private MTImageEntita entitaImmagineG2;
	private MTTextArea entitaNomeG2;
	public int entitaTotaliG2 = 0;
	private int spaceEntitaPrimaRigaG2 = 0;
	private int spaceEntitaSecondaRigaG2 = 0;

	// Giocatore 1
	protected MTTextArea giocatoreTextG1;
	protected String nomeG1 = "Giocatore 1";
	protected int punteggioG1;
	protected Giocatore g1 = new Giocatore(nomeG1, punteggioG1);

	// Giocatore 2
	protected MTTextArea giocatoreTextG2;
	protected String nomeG2 = "Giocatore 2";
	protected int punteggioG2;
	protected Giocatore g2 = new Giocatore(nomeG2, punteggioG2);
	public int entitaIndovinate = 0;

	// Popup Risposta
	private MTRectangle infoRect;
	private MTTextArea titoloText;
	private MTTextArea domandaText;
	private MTRectangle imagePopup;
	private PImage loadImage;

	public MultiPlayer(CGSceneManager cgScene, MTApplication app, Giocatore g1, Giocatore g2) throws SQLException {
		this.context = app;
		this.root = new MTComponent(app);
		this.cgScene = cgScene;

		this.cgScene.setClearColor(MyColors.lightorange());

		buildIntestazione(app, cgScene, root, "gioco");
		attachEvent(cgScene, app, root, "esciGioco", "esciGioco");
		questionGioco(context, root, "Inserite ciascuna specie nella classe corretta", "multigioco");
		insertCategorieAndEntita();
		caricaGiocatori(g1, g2);
	}

	public void insertCategorieAndEntita() throws SQLException {
		containerCategorieSuper = new MTRectangle(context, context.width / 120, context.height / 4.6f,
				context.width / 1.015f, context.height / 5.5f);

		containerG1 = new MTRectangle(context, context.width / 120, context.height / 2.47f, context.width / 2.05f,
				context.height / 1.70f);

		categorieContainerG1 = new MTRectangle(context, context.width / 2.05f, context.height / 3.95f);
		categorieContainerG1.setPositionRelativeToOther(containerG1,
				new Vector3D(context.width / 3.96f, context.height * 0.867f));
		categorieContainerG1.setNoFill(true);
		categorieContainerG1.setStrokeColor(MTColors.WHITE);

		entitaContainerG1 = new MTRectangle(context, context.width / 2.05f, context.height / 2.96f);
		entitaContainerG1.setPositionRelativeToOther(containerG1,
				new Vector3D(context.width / 3.96f, context.height / 1.745f));
		entitaContainerG1.setNoFill(true);
		entitaContainerG1.setStrokeColor(MTColors.WHITE);

		containerG2 = new MTRectangle(context, context.width / 1.98f, context.height / 2.47f, context.width / 2.05f,
				context.height / 1.70f);

		categorieContainerG2 = new MTRectangle(context, context.width / 2.05f, context.height / 3.95f);
		categorieContainerG2.setPositionRelativeToOther(containerG2,
				new Vector3D(context.width / 1.335f, context.height * 0.867f));
		categorieContainerG2.setNoFill(true);
		categorieContainerG2.setStrokeColor(MTColors.WHITE);

		entitaContainerG2 = new MTRectangle(context, context.width / 2.05f, context.height / 2.96f);
		entitaContainerG2.setPositionRelativeToOther(containerG2,
				new Vector3D(context.width * 0.749f, context.height / 1.745f));
		entitaContainerG2.setNoFill(true);
		entitaContainerG2.setStrokeColor(MTColors.WHITE);

		proprietaMultiPlayer(root, containerCategorieSuper, containerG1, containerG2);

		insertCategorie();
		insertEntita();
	}

	private void insertCategorie() {
		int i = 0;
		for (CGCategoria cat : listaCategorie(cgScene)) {
			if (cat.getIdSpecie().equals(cgScene.getClasse().getIdSpecie())) {
				categoriaTitleSuper = new MTRectangle(context, 280, 45);
				categoriaTitleSuper
						.setPositionGlobal(new Vector3D(context.width / 12 - 4 + (i), context.height / 4 - 11));

				categoriaNomeSuper = new MTTextArea(context);
				categoriaNomeSuper
						.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 28, MTColor.WHITE));
				categoriaNomeSuper.setText(cat.getCategoria());
				categoriaNomeSuper.setPositionRelativeToParent(new Vector3D(
						categoriaTitleSuper.getCenterPointGlobal().x, categoriaTitleSuper.getCenterPointGlobal().y));

				existsImageGame(cat.getCategoria().toLowerCase(), "data/cg/game/categorie/");
				categoriaImmagineSuper = new MTImage(context,
						context.loadImage(FilesPath.rootCategorie + getExistsImage()));
				categoriaImmagineSuper.scale(1, 0.56f, 1f, categoriaImmagineSuper.getCenterPointGlobal());

				categoriaImmagineSuper.setPositionRelativeToOther(categoriaTitleSuper,
						new Vector3D(context.width / 14 + 3, context.height / 9));

				proprietaCategorieSuper(containerCategorieSuper, categoriaTitleSuper, categoriaNomeSuper,
						categoriaImmagineSuper);

				i = i + 322;
			}
		}

		for (CGCategoria categoria : listaCategorie(cgScene)) {
			if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie()) && categorieGioco.size() < 4) {
				categorieGioco.add(categoria);
			}
		}

		for (CGCategoria categoria : categorieGioco) {
			// Giocatore 1
			categoriaTitleG1 = new MTRectangle(context, 201, 50);

			existsImageGame(categoria.getCategoria().toLowerCase(), "data/cg/game/categorie/");
			categoriaImmagineG1 = new MTImage(context, context.loadImage(FilesPath.rootCategorie + getExistsImage()));
			categoriaImmagineG1.scale(0.721f, 0.80f, 1f, categoriaImmagineG1.getCenterPointGlobal());

			categoriaTitleG1.setPositionRelativeToOther(categorieContainerG1,
					new Vector3D(context.width / 22 + (spaceCategorieG1), -context.height / 1.40f));
			categoriaImmagineG1.setPositionRelativeToOther(categorieContainerG1,
					new Vector3D(context.width / 22 + (spaceCategorieG1), -context.height / 1.692f));

			categoriaNomeG1 = new MTTextArea(context);
			categoriaNomeG1.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 28, MTColor.WHITE));
			categoriaNomeG1.setText(categoria.getCategoria());
			categoriaNomeG1.setPositionRelativeToOther(containerG1,
					new Vector3D(categoriaTitleG1.getCenterPointGlobal().x, categoriaTitleG1.getCenterPointGlobal().y));

			categoriaImmagineG1.setName(pulisciNomeImmagine(categoria.getCategoria()) + "G1");
			categorieDomande.add(categoriaImmagineG1);

			proprietaCategorieGiocatore1(categorieContainerG1, categoriaTitleG1, categoriaNomeG1, categoriaImmagineG1);

			// Giocatore2
			categoriaTitleG2 = new MTRectangle(context, 201, 50);

			categoriaImmagineG2 = new MTImage(context, context.loadImage(FilesPath.rootCategorie + getExistsImage()));
			categoriaImmagineG2.scale(0.721f, 0.80f, 1f, categoriaImmagineG2.getCenterPointGlobal());

			categoriaTitleG2.setPositionRelativeToOther(categorieContainerG2,
					new Vector3D(-context.width * 0.451f + (spaceCategorieG2), -context.height / 1.40f));
			categoriaImmagineG2.setPositionRelativeToOther(categorieContainerG2,
					new Vector3D(-context.width * 0.451f + (spaceCategorieG2), -context.height / 1.692f));

			categoriaNomeG2 = new MTTextArea(context);
			categoriaNomeG2.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 28, MTColor.WHITE));
			categoriaNomeG2.setText(categoria.getCategoria());
			categoriaNomeG2.setPositionRelativeToOther(containerG2,
					new Vector3D(categoriaTitleG2.getCenterPointGlobal().x, categoriaTitleG2.getCenterPointGlobal().y));

			categoriaImmagineG2.setName(pulisciNomeImmagine(categoria.getCategoria()) + "G2");
			categorieDomande.add(categoriaImmagineG2);

			proprietaCategorieGiocatore2(categorieContainerG2, categoriaTitleG2, categoriaNomeG2, categoriaImmagineG2);

			spaceCategorieG1 = spaceCategorieG1 + 242;
			spaceCategorieG2 = spaceCategorieG2 + 242;
		}
		lockStrokeRectangle(containerCategorieSuper);
		lockStrokeRectangle(categorieContainerG1);
		lockStrokeRectangle(categorieContainerG2);

		containerG1.addChild(categorieContainerG1);
		containerG2.addChild(categorieContainerG2);
	}

	// private void insertCategorie() {
	// int i = 0;
	// for (CGCategoria categoria : listaCategorie(cgScene)) {
	// if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie())) {
	// categoriaTitleSuper = new MTRectangle(context, 280, 45);
	// categoriaTitleSuper
	// .setPositionGlobal(new Vector3D(context.width / 12 - 4 + (i),
	// context.height / 4 - 11));
	//
	// categoriaNomeSuper = new MTTextArea(context);
	// categoriaNomeSuper
	// .setFont(FontManager.getInstance().createFont(context, "arial.ttf", 28,
	// MTColor.WHITE));
	// categoriaNomeSuper.setText(categoria.getCategoria());
	// categoriaNomeSuper.setPositionRelativeToParent(new Vector3D(
	// categoriaTitleSuper.getCenterPointGlobal().x,
	// categoriaTitleSuper.getCenterPointGlobal().y));
	//
	// existsImageGame(categoria.getCategoria().toLowerCase(),
	// "data/cg/game/categorie/");
	// categoriaImmagineSuper = new MTImage(context,
	// context.loadImage(FilesPath.rootCategorie + getExistsImage()));
	// categoriaImmagineSuper.scale(1, 0.56f, 1f,
	// categoriaImmagineSuper.getCenterPointGlobal());
	//
	// categoriaImmagineSuper.setPositionRelativeToOther(categoriaTitleSuper,
	// new Vector3D(context.width / 14 + 3, context.height / 9));
	//
	// proprietaCategorieSuper(containerCategorieSuper, categoriaTitleSuper,
	// categoriaNomeSuper,
	// categoriaImmagineSuper);
	//
	// i = i + 322;
	//
	// // Giocatore 1
	// categoriaTitleG1 = new MTRectangle(context, 201, 50);
	//
	// existsImageGame(categoria.getCategoria().toLowerCase(),
	// "data/cg/game/categorie/");
	// categoriaImmagineG1 = new MTImage(context,
	// context.loadImage(FilesPath.rootCategorie + getExistsImage()));
	// categoriaImmagineG1.scale(0.721f, 0.80f, 1f,
	// categoriaImmagineG1.getCenterPointGlobal());
	//
	// categoriaTitleG1.setPositionRelativeToOther(categorieContainerG1,
	// new Vector3D(context.width / 22 + (spaceCategorieG1), -context.height /
	// 1.40f));
	// categoriaImmagineG1.setPositionRelativeToOther(categorieContainerG1,
	// new Vector3D(context.width / 22 + (spaceCategorieG1), -context.height /
	// 1.692f));
	//
	// categoriaNomeG1 = new MTTextArea(context);
	// categoriaNomeG1.setFont(FontManager.getInstance().createFont(context,
	// "arial.ttf", 28, MTColor.WHITE));
	// categoriaNomeG1.setText(categoria.getCategoria());
	// categoriaNomeG1.setPositionRelativeToOther(containerG1, new Vector3D(
	// categoriaTitleG1.getCenterPointGlobal().x,
	// categoriaTitleG1.getCenterPointGlobal().y));
	//
	// categoriaImmagineG1.setName(pulisciNomeImmagine(categoria.getCategoria())
	// + "G1");
	// categorieDomandeG1.add(categoriaImmagineG1);
	//
	// proprietaCategorieGiocatore1(categorieContainerG1, categoriaTitleG1,
	// categoriaNomeG1,
	// categoriaImmagineG1);
	//
	// // Giocatore2
	// categoriaTitleG2 = new MTRectangle(context, 201, 50);
	//
	// categoriaImmagineG2 = new MTImage(context,
	// context.loadImage(FilesPath.rootCategorie + getExistsImage()));
	// categoriaImmagineG2.scale(0.721f, 0.80f, 1f,
	// categoriaImmagineG2.getCenterPointGlobal());
	//
	// categoriaTitleG2.setPositionRelativeToOther(categorieContainerG2,
	// new Vector3D(-context.width * 0.451f + (spaceCategorieG2),
	// -context.height / 1.40f));
	// categoriaImmagineG2.setPositionRelativeToOther(categorieContainerG2,
	// new Vector3D(-context.width * 0.451f + (spaceCategorieG2),
	// -context.height / 1.692f));
	//
	// categoriaNomeG2 = new MTTextArea(context);
	// categoriaNomeG2.setFont(FontManager.getInstance().createFont(context,
	// "arial.ttf", 28, MTColor.WHITE));
	// categoriaNomeG2.setText(categoria.getCategoria());
	// categoriaNomeG2.setPositionRelativeToOther(containerG2, new Vector3D(
	// categoriaTitleG2.getCenterPointGlobal().x,
	// categoriaTitleG2.getCenterPointGlobal().y));
	//
	// categoriaImmagineG2.setName(pulisciNomeImmagine(categoria.getCategoria())
	// + "G2");
	// categorieDomandeG2.add(categoriaImmagineG2);
	//
	// proprietaCategorieGiocatore2(categorieContainerG2, categoriaTitleG2,
	// categoriaNomeG2,
	// categoriaImmagineG2);
	//
	// spaceCategorieG1 = spaceCategorieG1 + 242;
	// spaceCategorieG2 = spaceCategorieG2 + 242;
	// }
	// }
	// lockStrokeRectangle(containerCategorieSuper);
	// lockStrokeRectangle(categorieContainerG1);
	// // lockStrokeRectangle(categorieContainerG2);
	//
	// containerG1.addChild(categorieContainerG1);
	// containerG2.addChild(categorieContainerG2);
	// }

	private void insertEntita() {
		for (CGEntita entita : listaEntita(cgScene)) {
			// for (CGCategoria categoria : listaCategorie(cgScene)) {
			for (CGCategoria categoria : categorieGioco) {
				if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie()) && entitaTotaliG1 < 5
						&& entita.getDomanda().equalsIgnoreCase("si")
						&& entita.getIdCategoria().equals(categoria.getIdCategoria())) {
					entitaNomeG1 = new MTTextArea(context);
					entitaNomeG1.setText(entita.getEntita());

					existsImage(entita.getEntita().toLowerCase(), "data/cg/information/");
					PImage loadEntita = context.loadImage(FilesPath.rootInformation + Utility.getExistsImage());
					loadEntita.resize(150, 180);
					entitaImmagineG1 = new MTImageEntita(context, loadEntita, this, g1, null, context, cgScene);
					entitaImmagineG1.setStrokeColor(new MTColor(0, 0, 0, 0));
					entitaImmagineG1.setName(pulisciNomeImmagine(categoria.getCategoria() + "G1"));
					entitaImmagineG1.associaEntitaACategoria(categorieDomande, "multiplayer");

					if (entitaTotaliG1 < 2) {
						entitaImmagineG1.setPositionRelativeToOther(entitaContainerG1,
								new Vector3D(context.width / 7.3f + (spaceEntitaPrimaRigaG1), -context.height / 3.25f));
						entitaNomeG1.setPositionRelativeToOther(entitaImmagineG1,
								new Vector3D(-context.width / 13 + (-spaceEntitaPrimaRigaG1), context.height / 5.6f));
						spaceEntitaPrimaRigaG1 = spaceEntitaPrimaRigaG1 + 380;
					} else {
						entitaImmagineG1.setPositionRelativeToOther(entitaContainerG1,
								new Vector3D(context.width / 26.8f + (spaceEntitaSecondaRigaG1), -context.height / 5));
						entitaNomeG1.setPositionRelativeToOther(entitaImmagineG1,
								new Vector3D(context.width / 43 + (-spaceEntitaSecondaRigaG1), context.height / 14));
						spaceEntitaSecondaRigaG1 = spaceEntitaSecondaRigaG1 + 380;
					}

					proprietaEntitaGiocatore1(context, entitaContainerG1, entitaNomeG1, entitaImmagineG1);

					entitaNomeG2 = new MTTextArea(context);
					entitaNomeG2.setText(entita.getEntita());

					PImage loadEntita1 = context.loadImage(FilesPath.rootInformation + Utility.getExistsImage());
					loadEntita1.resize(150, 180);
					entitaImmagineG2 = new MTImageEntita(context, loadEntita1, this, null, g2, context, cgScene);

					entitaImmagineG2.setName(pulisciNomeImmagine(categoria.getCategoria() + "G2"));
					entitaImmagineG2.associaEntitaACategoria(categorieDomande, "multiplayer");

					if (entitaTotaliG2 < 2) {
						entitaImmagineG2.setPositionRelativeToOther(entitaContainerG2, new Vector3D(
								-context.width / 2.78f + (spaceEntitaPrimaRigaG2), -context.height / 3.25f));
						entitaNomeG2.setPositionRelativeToOther(entitaImmagineG2, new Vector3D(
								-context.width / 13.15f + (-spaceEntitaPrimaRigaG2), context.height / 5.6f));
						spaceEntitaPrimaRigaG2 = spaceEntitaPrimaRigaG2 + 380;
					} else {
						entitaImmagineG2.setPositionRelativeToOther(entitaContainerG2,
								new Vector3D(-context.width / 2.18f + (spaceEntitaSecondaRigaG2), -context.height / 5));
						entitaNomeG2.setPositionRelativeToOther(entitaImmagineG2,
								new Vector3D(context.width / 43.9f + (-spaceEntitaSecondaRigaG2), context.height / 14));
						spaceEntitaSecondaRigaG2 = spaceEntitaSecondaRigaG2 + 380;
					}

					proprietaEntitaGiocatore2(context, entitaContainerG2, entitaNomeG2, entitaImmagineG2);

					entitaTotaliG1++;
					entitaTotaliG2++;
				}
			}
			lockStrokeRectangle(entitaContainerG1);
			containerG1.addChild(entitaContainerG1);

			lockStrokeRectangle(entitaContainerG2);
			containerG2.addChild(entitaContainerG2);
		}
	}

	public void caricaGiocatori(Giocatore g1, Giocatore g2) {
		if (g1 == null && g2 == null) {
			g1 = new Giocatore(nomeG1, punteggioG1);
			g1.setNome(nomeG1);
			g1.setPunteggio(punteggioG1);

			g2 = new Giocatore(nomeG2, punteggioG2);
			g2.setNome(nomeG2);
			g2.setPunteggio(punteggioG2);
		} else
			nomeG1 = g1.getNome();
		punteggioG1 = g1.getPunteggio();
		nomeG2 = g2.getNome();
		punteggioG2 = g2.getPunteggio();

		giocatoreTextG1 = new MTTextArea(context);
		giocatoreTextG1.setPositionRelativeToOther(entitaContainerG1,
				new Vector3D(context.width / 2.75f, -context.height / 2.55f));
		giocatoreTextG1.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 26, MTColor.WHITE));
		giocatoreTextG1.setText("Nome : " + g1.getNome() + "\n" + "Punteggio : " + g1.getPunteggio());

		giocatoreTextG2 = new MTTextArea(context);
		giocatoreTextG2.setPositionRelativeToOther(entitaContainerG2,
				new Vector3D(-context.width / 7.55f, -context.height / 2.55f));
		giocatoreTextG2.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 26, MTColor.WHITE));
		giocatoreTextG2.setText("Nome : " + g2.getNome() + "\n" + "Punteggio : " + g2.getPunteggio());

		lockStrokeFillText(giocatoreTextG1);
		lockStrokeFillText(giocatoreTextG2);

		entitaContainerG1.addChild(giocatoreTextG1);
		entitaContainerG2.addChild(giocatoreTextG2);
	}

	public void buildPopupRisposta(String typePopup) {
		infoRect = new MTRectangle(context, context.getAlignmentX() + 475, context.getAlignmentY() + 350,
				context.width / 1.95f, context.height - 700);
		infoRect.setFillColor(MyColors.lightyellow());
		infoRect.setStrokeColor(MTColor.BLACK);
		titoloText = new MTTextArea(context);
		domandaText = new MTTextArea(context);
		IFont testoFont = testoPopup(context);

		if ("corretto".equals(typePopup)) {
			IFont titoloFont = titoloPopupVerde(context);
			titoloText.setFont(titoloFont);
			titoloText.setText("Risposta esatta");
			titoloText.setPositionRelativeToOther(infoRect,
					new Vector3D(infoRect.getCenterPointGlobal().x + 20, infoRect.getCenterPointGlobal().y / 2 + 120));
			domandaText.setPositionRelativeToOther(infoRect,
					new Vector3D(titoloText.getCenterPointGlobal().x - 220, titoloText.getCenterPointGlobal().y + 75));
			domandaText.setFont(testoFont);
			domandaText.setText("Molto bene, continua così!");
			loadImage = context.loadImage(FilesPath.rootGame + "scimmia.png");
			imagePopup = new MTRectangle(context, loadImage);
			imagePopup.setPositionRelativeToOther(infoRect,
					new Vector3D(context.width * 0.315f, context.height * 0.55f));
		} else if ("sbagliato".equals(typePopup)) {
			IFont titoloFont = titoloPopupRosso(context);
			titoloText.setFont(titoloFont);
			titoloText.setText("Risposta sbagliata");
			titoloText.setPositionRelativeToOther(infoRect,
					new Vector3D(infoRect.getCenterPointGlobal().x + 20, infoRect.getCenterPointGlobal().y / 2 + 120));
			domandaText.setPositionRelativeToOther(infoRect,
					new Vector3D(titoloText.getCenterPointGlobal().x - 310, titoloText.getCenterPointGlobal().y + 75));

			domandaText.setFont(testoFont);
			domandaText.setText("Riprova, la prossima volta andrà meglio!");
			loadImage = context.loadImage(FilesPath.rootGame + "gatto.png");
			imagePopup = new MTRectangle(context, loadImage);
			imagePopup.setPositionRelativeToOther(infoRect,
					new Vector3D(context.width * 0.315f, context.height * 0.57f - 3));
		}
		proprietaPopupCorrettoSbagliato(root, infoRect, titoloText, domandaText, imagePopup);
	}

	public void rimuoviPopupRisposta() {
		root.removeChild(infoRect);
		root.removeChild(imagePopup);
		root.removeChild(titoloText);
		root.removeChild(domandaText);
	}

	public MTComponent getRootComponent() {
		return root;
	}
}
