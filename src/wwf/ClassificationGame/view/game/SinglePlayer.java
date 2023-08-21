package wwf.ClassificationGame.view.game;

import static wwf.ClassificationGame.util.MyFonts.*;
import static wwf.ClassificationGame.util.Utility.*;

import java.sql.SQLException;
import java.util.Collections;
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

import processing.core.PImage;
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.sceneManager.Giocatore;
import wwf.ClassificationGame.util.FilesPath;
import wwf.ClassificationGame.util.MyColors;
import wwf.ClassificationGame.util.Utility;

public class SinglePlayer {
	private MTComponent root;
	private MTApplication context;
	private CGSceneManager cgScene;

	// Categorie
	private MTRectangle categorieContainer;
	private MTRectangle categoriaTitle;
	private MTImage categoriaImmagine;
	private LinkedList<MTImage> categorieDomande = new LinkedList<MTImage>();
	private int categorieTotali = 0;
	private int spaceCategorie = 0;

	// Entit‡
	private MTRectangle entitaContainer;
	private MTImageEntita entitaImmagine;
	private MTTextArea entitaNome;
	public int entitaTotali = 0;
	private int spaceEntitaPrimaRiga = 0;
	private int spaceEntitaSecondaRiga = 0;

	// Giocatore
	protected MTTextArea giocatoreText;
	protected String nomeGiocatore = "Giocatore 1";
	protected int punteggioGiocatore;
	protected Giocatore g = new Giocatore(nomeGiocatore, punteggioGiocatore);
	public int entitaIndovinate = 0;

	// Popup risposta
	private MTRectangle infoRect;
	private MTTextArea titoloText;
	private MTTextArea domandaText;
	private MTRectangle imagePopup;
	private PImage loadImage;

	public SinglePlayer(CGSceneManager cgScene, MTApplication app, Giocatore g) throws SQLException {
		this.context = app;
		this.root = new MTComponent(app);
		this.cgScene = cgScene;

		this.cgScene.setClearColor(MyColors.lightorange());

		buildIntestazione(app, cgScene, root, "gioco");
		attachEvent(cgScene, app, root, "esciGioco", "esciGioco");
		questionGioco(context, root, "Inserisci ciascuna specie nella classe corretta", "gioco");
		caricaGiocatore(g);
		insertCategorieAndEntita();
	}

	public void insertCategorieAndEntita() throws SQLException {
		categorieContainer = new MTRectangle(context, context.width / 100, context.height / 1.5f, context.width - 30,
				context.height / 3);
		categorieContainer.setNoFill(true);

		entitaContainer = new MTRectangle(context, context.width / 300, context.height / 4 + 30, context.width - 20,
				context.height / 3 + 60);
		entitaContainer.setNoFill(true);

		Collections.shuffle(listaCategorie(cgScene));
		Collections.shuffle(listaEntita(cgScene));
		insertCategorie();
		insertEntita();
	}

	private void insertCategorie() {
		for (CGCategoria categoria : listaCategorie(cgScene)) {
			if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie())) {
				categorieTotali++;
			}
		}

		for (CGCategoria categoria : listaCategorie(cgScene)) {
			if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie())) {
				categoriaTitle = new MTRectangle(context, 250, 60);

				existsImageGame(categoria.getCategoria().toLowerCase(), "data/cg/game/categorie/");
				categoriaImmagine = new MTImage(context, context.loadImage(FilesPath.rootCategorie + getExistsImage()));
				categoriaImmagine.scale(0.895f, 1.0f, 1f, categoriaImmagine.getCenterPointGlobal());

				if (categorieTotali == 3) {
					categoriaTitle.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + 75 + (spaceCategorie), context.height / 1.39f));
					categoriaImmagine.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + 75 + (spaceCategorie), context.height / 1.148f));
					spaceCategorie = spaceCategorie + 720;
				} else if (categorieTotali == 4) {
					categoriaTitle.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + 50 + (spaceCategorie), context.height / 1.39f));
					categoriaImmagine.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + 50 + (spaceCategorie), context.height / 1.148f));
					spaceCategorie = spaceCategorie + 500;
				} else if (categorieTotali == 5) {
					categoriaTitle.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + 25 + (spaceCategorie), context.height / 1.39f));
					categoriaImmagine.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + 25 + (spaceCategorie), context.height / 1.148f));
					spaceCategorie = spaceCategorie + 392;
				} else {
					categoriaTitle.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + (spaceCategorie), context.height / 1.39f));
					categoriaImmagine.setPositionRelativeToOther(categorieContainer,
							new Vector3D(context.width / 13 + (spaceCategorie), context.height / 1.148f));
					spaceCategorie = spaceCategorie + 325;
				}

				MTTextArea categoriaNome = new MTTextArea(context);
				categoriaNome.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 36, MTColor.WHITE));
				categoriaNome.setText(categoria.getCategoria());
				categoriaNome.setPositionRelativeToOther(categorieContainer,
						new Vector3D(categoriaTitle.getCenterPointGlobal().x, categoriaTitle.getCenterPointGlobal().y));

				categoriaImmagine.setName(pulisciNomeImmagine(categoria.getCategoria()));
				categorieDomande.add(categoriaImmagine);
				System.out.println(categoriaImmagine.getName());

				propriet‡CategoriaGioco(categorieContainer, categoriaTitle, categoriaNome, categoriaImmagine);
			}
			lockStrokeRectangle(categorieContainer);
			root.addChild(categorieContainer);
		}
	}

	private void insertEntita() {
		for (CGEntita entita : listaEntita(cgScene)) {
			for (CGCategoria categoria : listaCategorie(cgScene)) {
				if (categoria.getIdSpecie().equals(cgScene.getClasse().getIdSpecie()) && entitaTotali < 6
						&& entita.getDomanda().equalsIgnoreCase("si")
						&& entita.getIdCategoria().equals(categoria.getIdCategoria())) {
					entitaNome = new MTTextArea(context);
					entitaNome.setText(entita.getEntita());

					existsImage(entita.getEntita().toLowerCase(), "data/cg/information/");
					PImage loadEntita = context.loadImage(FilesPath.rootInformation + Utility.getExistsImage());
					loadEntita.resize(220, 250);
					entitaImmagine = new MTImageEntita(context, loadEntita, this, g, context, cgScene);

					entitaImmagine.setName(pulisciNomeImmagine(categoria.getCategoria()));
					entitaImmagine.associaEntitaACategoria(categorieDomande, "singleplayer");

					if (entitaTotali < 3) {
						entitaImmagine.setPositionRelativeToOther(entitaContainer,
								new Vector3D(context.width / 14 + (spaceEntitaPrimaRiga), context.height / 3 + 20));
						entitaNome.setPositionRelativeToOther(entitaImmagine, new Vector3D(
								context.width / 27 + (-spaceEntitaPrimaRiga) - 10, context.height / 28 - 5));
						spaceEntitaPrimaRiga = spaceEntitaPrimaRiga + 640;
					} else {
						entitaImmagine.setPositionRelativeToOther(entitaContainer, new Vector3D(
								context.width / 5 + 60 + (spaceEntitaSecondaRiga), context.height / 2 + 10));
						entitaNome.setPositionRelativeToOther(entitaImmagine, new Vector3D(
								-context.width / 8 + (-spaceEntitaSecondaRiga) - 10, context.height / 50 - 157));
						spaceEntitaSecondaRiga = spaceEntitaSecondaRiga + 640;
					}

					propriet‡EntitaGioco(context, entitaContainer, entitaImmagine, entitaNome);
					entitaTotali++;
				}
			}
			lockStrokeRectangle(entitaContainer);
			root.addChild(entitaContainer);
		}
	}

	public void caricaGiocatore(Giocatore g) {
		if (g == null) {
			g = new Giocatore(nomeGiocatore, punteggioGiocatore);
			g.setNome(nomeGiocatore);
			g.setPunteggio(punteggioGiocatore);
		} else
			nomeGiocatore = g.getNome();
		punteggioGiocatore = g.getPunteggio();

		giocatoreText = new MTTextArea(context);
		giocatoreText.setPositionGlobal(new Vector3D(context.width * 0.852f, context.height * 0.22f));
		giocatoreText.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 32, MTColor.WHITE));
		giocatoreText.setText("Nome : " + g.getNome() + "\n" + "Punteggio : " + g.getPunteggio());
		lockStrokeFillText(giocatoreText);

		root.addChild(giocatoreText);
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
			domandaText.setText("Molto bene, continua cosÏ!");
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
			domandaText.setText("Riprova, la prossima volta andr‡ meglio!");
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

	public MTComponent getRoot() {
		return root;
	}

	public void setRoot(MTComponent root) {
		this.root = root;
	}

	public MTApplication getContext() {
		return context;
	}

	public void setContext(MTApplication context) {
		this.context = context;
	}

	public CGSceneManager getCgScene() {
		return cgScene;
	}

	public void setCgScene(CGSceneManager cgScene) {
		this.cgScene = cgScene;
	}

	public int getPunteggioGiocatore() {
		return punteggioGiocatore;
	}

	public void setPunteggioGiocatore(int punteggioGiocatore) {
		this.punteggioGiocatore = punteggioGiocatore;
	}

	public Giocatore getG() {
		return g;
	}

	public void setG(Giocatore g) {
		this.g = g;
	}

}