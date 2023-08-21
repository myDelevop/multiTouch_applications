package wwf.ClassificationGame.util;

import static wwf.ClassificationGame.util.MyFonts.*;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.MTColors;

import processing.core.PImage;
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.data.model.CGClasse;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.sceneManager.Giocatore;

public class Utility {
	public static void lockAll(MTComponent c) {
		c.setGestureAllowance(DragProcessor.class, false);
		c.setGestureAllowance(ScaleProcessor.class, false);
		c.setGestureAllowance(ZoomProcessor.class, false);
		c.setGestureAllowance(RotateProcessor.class, false);
	}

	public static void lockStrokeFillText(MTTextArea ta) {
		ta.setGestureAllowance(DragProcessor.class, false);
		ta.setGestureAllowance(ScaleProcessor.class, false);
		ta.setGestureAllowance(ZoomProcessor.class, false);
		ta.setGestureAllowance(RotateProcessor.class, false);

		ta.setNoStroke(true);
		ta.setNoFill(true);
	}

	public static void lockStrokeRectangle(MTRectangle r) {
		r.setGestureAllowance(DragProcessor.class, false);
		r.setGestureAllowance(ScaleProcessor.class, false);
		r.setGestureAllowance(ZoomProcessor.class, false);
		r.setGestureAllowance(RotateProcessor.class, false);

		r.setNoStroke(true);
	}

	private static String nomeImmagine;

	// specie: "data/cg/"
	// categoria: "data/cg/game/categorie/"
	// entita: "data/cg/information/"
	public static void existsImage(String nome, String percorso) {
		nomeImmagine = null;
		File cartellaImmagini = new File(percorso);
		File files[] = cartellaImmagini.listFiles();

		for (File f : files) {
			if (f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".png")
					|| f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".jpg")
					|| f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".jpeg")
					|| f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".bmp")) {
				nomeImmagine = f.getName();
			}
		}
	}

	public static void existsImageGame(String nome, String percorso) {
		nomeImmagine = "bianco.jpg";
		File cartellaImmagini = new File(percorso);
		File files[] = cartellaImmagini.listFiles();

		for (File f : files) {
			if (f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".png")
					|| f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".jpg")
					|| f.getName().toLowerCase().equalsIgnoreCase(nome.toLowerCase() + ".bmp")) {
				nomeImmagine = f.getName();
			}
		}
	}

	public static String getExistsImage() {
		return nomeImmagine;
	}

	/**
	 * Inserisci immagine di default se all'interno del gioco non Ë stata
	 * specificata nessuna immagine per la categoria
	 */
	public static MTRectangle insertImage(MTApplication context) {
		PImage logo = new PImage();
		if (getExistsImage() == null) {
			logo = context.loadImage(FilesPath.rootPath + "default.png");
		} else {
			logo = context.loadImage(FilesPath.rootPath + Utility.getExistsImage());
		}
		logo.resize(190, 170);
		MTRectangle logoTexture = new MTRectangle(context, logo);
		return logoTexture;
	}

	public static String pulisciNomeImmagine(String immagine) {
		String nomePulito = new String("");
		nomePulito = immagine;

		nomePulito = nomePulito.replace(" ", "");
		for (int i = 0; i < 10; i++)
			nomePulito = nomePulito.replace(" ", "");
		immagine = nomePulito;

		return immagine;
	}

	/**
	 * Intestazione del programma
	 */
	public static void buildIntestazione(MTApplication context, final CGSceneManager cgScene, final MTComponent root,
			String type) {
		MTTextArea titleText = new MTTextArea(context, context.width / 3, 0, context.width, context.height);
		MTTextArea subtitleText = null;
		IFont titleFont = titoloSplashScene(context);
		IFont subtitleFont = sottotitoloSplashScene(context);
		IFont titleGiocoFont = titoloGioco(context);
		IFont subtitleGiocoFont = sottotitoloGioco(context);

		titleText.setText("Torre Guaceto");

		switch (type) {
		case "splash":
			MTBackgroundImage background = new MTBackgroundImage(context,
					context.loadImage(FilesPath.rootPath + "torreguaceto.jpg"), false);
			root.addChild(background);
			titleText.setFont(titleFont);
			subtitleText = new MTTextArea(context, titleText.getCenterPointGlobal().x / 2 - 60, 105, context.width,
					context.height);
			subtitleText.setFont(subtitleFont);
			subtitleText.setText("Classification Game");
			break;

		case "normal":
			titleFont = FontManager.getInstance().createFont(context, FilesPath.rootFont + "berlinsansfb.ttf", 110,
					MyColors.orange());
			titleText.setFont(titleFont);
			subtitleText = new MTTextArea(context, titleText.getCenterPointGlobal().x / 2, 108, context.width,
					context.height);
			subtitleText.setFont(subtitleFont);
			subtitleText.setText("\t\t" + cgScene.getClasse().getSpecie());
			break;

		case "gioco":
			titleText.setFont(titleGiocoFont);
			subtitleText = new MTTextArea(context, titleText.getCenterPointGlobal().x / 2 - 50, 108, context.width,
					context.height);
			subtitleText.setFont(subtitleGiocoFont);
			subtitleText.setText(cgScene.getGame());
			break;
		}

		PImage logotg = context.loadImage(FilesPath.rootPath + "logoguaceto.png");
		MTRectangle logotgRect = new MTRectangle(context, logotg);
		logotgRect.setPositionGlobal(new Vector3D(context.width * 0.26f, context.height * 0.075f));

		PImage logowwf = context.loadImage(FilesPath.rootPath + "WWF.png");
		MTRectangle logowwfRect = new MTRectangle(context, logowwf);
		logowwfRect.setPositionGlobal(new Vector3D(context.width * 0.75f, context.height * 0.09f));

		Utility.lockStrokeFillText(titleText);
		Utility.lockStrokeFillText(subtitleText);
		Utility.lockStrokeRectangle(logotgRect);
		Utility.lockStrokeRectangle(logowwfRect);

		root.addChild(titleText);
		root.addChild(subtitleText);
		root.addChild(logotgRect);
		root.addChild(logowwfRect);
	}

	/**
	 * Gestione delle interazioni coi bottoni
	 */
	public static void attachEvent(final CGSceneManager cgScene, final MTApplication context, final MTComponent root,
			final String typeBack, final String typeHome) {
		PImage loadBack = context.loadImage(FilesPath.rootPath + "back.png");
		final MTRectangle backRect = new MTRectangle(context, loadBack);
		backRect.setPositionGlobal(new Vector3D(context.width * 0.10f, context.height * 0.080f));

		PImage loadHome = context.loadImage(FilesPath.rootPath + "home.png");
		final MTRectangle homeRect = new MTRectangle(context, loadHome);
		homeRect.setPositionGlobal(new Vector3D(context.width * 0.93f, context.height * 0.088f));

		lockStrokeRectangle(backRect);
		lockStrokeRectangle(homeRect);

		root.addChild(backRect);
		root.addChild(homeRect);

		backRect.registerInputProcessor(new TapProcessor(context));
		backRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					try {
						if ("normal".equals(typeBack)) {
							homeBack(cgScene, root);
						} else if ("regole".equals(typeBack)) {
							homeBack1(cgScene, root);
						} else if ("specie".equals(typeBack)) {
							homeBackRegole(cgScene, root);
						} else if ("esciGioco".equals(typeBack)) {
							buildPopupEsciGioco(cgScene, context, root, backRect);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});

		homeRect.registerInputProcessor(new TapProcessor(context));
		homeRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					try {
						if ("normal".equals(typeHome)) {
							homeBack(cgScene, root);
						} else if ("regole".equals(typeHome)) {
							homeBack1(cgScene, root);
						} else if ("esciGioco".equals(typeHome)) {
							cgScene.setExit(homeRect);
							buildPopupEsciGioco(cgScene, context, root, homeRect);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});
	}

	/**
	 * Inserimento della domanda nella scelta della specie e inserimento della
	 * domanda nel gioco
	 */
	public static void questionGioco(MTApplication context, MTComponent root, String text, String type) {
		MTTextArea domandaSpecieText = new MTTextArea(context);
		if ("scegliSpecie".equals(type)) {
			domandaSpecieText.setPositionGlobal(new Vector3D(context.width / 3 + 55, context.height / 4 - 40));
		} else if ("gioco".equals(type)) {
			domandaSpecieText.setPositionGlobal(new Vector3D(context.width / 4 + 105, context.height / 4 - 70));
		} else if ("multigioco".equals(type)) {
			domandaSpecieText.setPositionGlobal(new Vector3D(context.width / 4 + 105, context.height / 4 - 80));
		}
		IFont domandaFont = domandaGioco(context);
		domandaSpecieText.setFont(domandaFont);
		domandaSpecieText.setText(text);
		lockStrokeFillText(domandaSpecieText);
		root.addChild(domandaSpecieText);
	}

	/**
	 * Popup utilizzato per confermare l'uscita dal gioco
	 */
	public static void buildPopupEsciGioco(final CGSceneManager cgScene, MTApplication context, final MTComponent root,
			final MTRectangle homeRect) {
		final MTRectangle infoRect = new MTRectangle(context, context.getAlignmentX() + 475,
				context.getAlignmentY() + 350, context.width / 1.95f, context.height - 700);
		infoRect.setFillColor(MyColors.lightyellow());
		infoRect.setStrokeColor(MTColor.BLACK);

		MTTextArea titoloText = new MTTextArea(context);
		titoloText.setPositionRelativeToOther(infoRect,
				new Vector3D(infoRect.getCenterPointGlobal().x - 80, infoRect.getCenterPointGlobal().y / 2 + 100));
		IFont titoloFont;

		MTTextArea domandaText = new MTTextArea(context);
		domandaText.setPositionRelativeToOther(infoRect,
				new Vector3D(titoloText.getCenterPointGlobal().x - 150, titoloText.getCenterPointGlobal().y + 75));
		IFont domandaFont = testoPopup(context);
		domandaText.setFont(domandaFont);

		PImage close = context.loadImage(FilesPath.rootGame + "close.png");
		MTRectangle closeRect = new MTRectangle(context, close);
		closeRect.setPositionGlobal(new Vector3D(context.width * 0.75f, context.height * 0.33f));

		titoloFont = titoloPopupRosso(context);
		titoloText.setFont(titoloFont);
		titoloText.setText("Attenzione");

		domandaText.setText("Vuoi veramente uscire dal gioco?");

		PImage tartaruga = context.loadImage(FilesPath.rootGame + "tartaruga.png");
		MTRectangle imagePopup = new MTRectangle(context, tartaruga);
		imagePopup.setPositionRelativeToOther(infoRect,
				new Vector3D(context.width * 0.30f, context.height * 0.57f - 3));

		PImage annulla = context.loadImage(FilesPath.rootGame + "annulla.png");
		MTRectangle annullaRect = new MTRectangle(context, annulla);
		annullaRect.setPositionRelativeToOther(infoRect, new Vector3D(context.width * 0.63f, context.height * 0.60f));

		PImage ok = context.loadImage(FilesPath.rootGame + "ok.png");
		MTRectangle okRect = new MTRectangle(context, ok);
		okRect.setPositionRelativeToOther(infoRect, new Vector3D(context.width * 0.72f, context.height * 0.60f));

		proprietaPopupEsciGioco(context, root, infoRect, closeRect, titoloText, domandaText, imagePopup, annullaRect,
				okRect);

		closeRect.registerInputProcessor(new TapProcessor(context));
		annullaRect.registerInputProcessor(new TapProcessor(context));

		closeRect.registerInputProcessor(new TapProcessor(context));
		closeRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					removeEsciGioco(root, infoRect);
				}
				return true;
			}
		});

		annullaRect.registerInputProcessor(new TapProcessor(context));
		annullaRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					removeEsciGioco(root, infoRect);
				}
				return true;
			}
		});

		okRect.registerInputProcessor(new TapProcessor(context));
		okRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					try {
						cgScene.setClasse(null);
						CGSceneManager.loadClassi();
						CGSceneManager.loadCategorie();
						CGSceneManager.loadEntita();
						root.removeAllChildren();
						root.destroy();
						System.gc();
						if (cgScene.getExit() == homeRect) {
							cgScene.setGame(null);
							cgScene.buildSplashScreen();
						} else
							cgScene.buildScegliSpecieScene();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});
	}

	/**
	 * Schermata che descrive le regole del gioco e per visualizzare alla fine
	 * del gioco il punteggio ottenuto
	 */
	public static void buildRegoleEFineGioco(final CGSceneManager cgScene, MTApplication context,
			final MTComponent root, final String type, Integer punteggio) {
		MTRectangle regoleRect = new MTRectangle(context, context.width * 0.10f, context.getAlignmentY() + 250,
				context.width / 1.25f, context.height - 350);
		regoleRect.setFillColor(MTColors.WHITE);
		regoleRect.setStrokeColor(MTColor.BLACK);

		MTRectangle logoRagazzoRect = null;
		MTRectangle logoFumettoRect = null;

		PImage logoFumetto = context.loadImage(FilesPath.rootGame + "fumetto.png");
		logoFumettoRect = new MTRectangle(context, logoFumetto);

		MTTextArea titoloRegole = new MTTextArea(context);
		MTTextArea regoleText = new MTTextArea(context);
		MTTextArea fineregoleText = new MTTextArea(context);

		if ("regoleGioco".equals(type)) {
			IFont titleRegoleFont = titoloRegole(context);
			titoloRegole.setFont(titleRegoleFont);
			IFont corpoRegoleFont = corpoRegole(context);
			regoleText.setFont(corpoRegoleFont);
			IFont fineRegoleFont = fineRegole(context);
			fineregoleText.setFont(fineRegoleFont);
			PImage logoRagazzo = context.loadImage(FilesPath.rootGame + "ragazzoregole.png");
			logoRagazzoRect = new MTRectangle(context, logoRagazzo);
			logoRagazzoRect.setPositionRelativeToOther(regoleRect,
					new Vector3D(context.width / 6 + 40, context.height - 350));
			logoFumettoRect.setPositionRelativeToOther(regoleRect,
					new Vector3D(context.width / 2 + 102, context.height / 2 + 21));
			titoloRegole.setPositionRelativeToOther(logoFumettoRect, new Vector3D(
					logoFumettoRect.getCenterPointGlobal().x / 2 + 60, logoFumettoRect.getCenterPointGlobal().y / 15));
			titoloRegole.setText("Regole del gioco");
			regoleText.setPositionRelativeToOther(logoFumettoRect, new Vector3D(
					logoFumettoRect.getCenterPointGlobal().x / 4 + 10, logoFumettoRect.getCenterPointGlobal().y / 4));
			regoleText.setText("\t\t" + "Per ciascun regno ci saranno le immagini di alcune specie" + "\n"
					+ "\t\t\t\t\t" + "che dovrai trascinare nei contenitori corretti." + "\n\n\n" + "\t\t\t\t"
					+ "Ogni risposta corretta ti far‡ guadagnare 100 punti." + "\n\n" + "\t\t\t\t\t  "
					+ "Ogni risposta errata ti far‡ perdere 30 punti.");
			fineregoleText.setPositionRelativeToOther(logoFumettoRect, new Vector3D(
					logoFumettoRect.getCenterPointGlobal().x - 250, logoFumettoRect.getCenterPointGlobal().y - 90));
			fineregoleText.setText("BUON DIVERTIMENTO...");
		} else if ("fineGioco".equals(type)) {
			IFont titleRegoleFont = titoloFine(context);
			titoloRegole.setFont(titleRegoleFont);
			IFont fineRegoleFont = testoFine(context);
			regoleText.setFont(fineRegoleFont);
			PImage logoRagazzo = context.loadImage(FilesPath.rootGame + "ragazzofine.png");
			logoRagazzoRect = new MTRectangle(context, logoRagazzo);
			logoRagazzoRect.setPositionRelativeToOther(regoleRect,
					new Vector3D(context.width / 6 + 40, context.height - 325));
			logoFumetto.resize(1250, 520);
			logoFumettoRect = new MTRectangle(context, logoFumetto);
			logoFumettoRect.setPositionRelativeToOther(regoleRect,
					new Vector3D(context.width / 2 + 138, context.height / 2 + 21));

			titoloRegole.setPositionRelativeToOther(logoFumettoRect, new Vector3D(
					logoFumettoRect.getCenterPointGlobal().x / 2 + 75, logoFumettoRect.getCenterPointGlobal().y / 12));
			titoloRegole.setText("Riepilogo");

			regoleText.setPositionRelativeToOther(logoFumettoRect, new Vector3D(
					logoFumettoRect.getCenterPointGlobal().x / 4 + 110, logoFumettoRect.getCenterPointGlobal().y / 4));
			regoleText.setText("\t\t" + "Complimenti: hai completato il gioco!" + "\n\n" + "\t\t\t\t\t"
					+ "Hai totalizzato " + punteggio + " punti!");
		}

		PImage logook = context.loadImage(FilesPath.rootGame + "ok.png");
		MTRectangle logookRect = new MTRectangle(context, logook);
		logookRect.setPositionRelativeToOther(regoleRect, new Vector3D(context.width * 0.85f, context.height * 0.83f));

		logookRect.registerInputProcessor(new TapProcessor(context));
		logookRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					try {
						if ("regoleGioco".equals(type)) {
							root.removeAllChildren();
							root.destroy();
							System.gc();
							cgScene.buildScegliSpecieScene();
						} else if ("fineGioco".equals(type)) {
							cgScene.setClasse(null);
							root.removeAllChildren();
							root.destroy();
							System.gc();
							cgScene.buildSplashScreen();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});

		propriet‡Regole(root, regoleRect, logoRagazzoRect, logoFumettoRect, titoloRegole, regoleText, fineregoleText,
				logookRect);
	}

	/**
	 * Gestione delle specie non ancora giocate
	 */
	public static void buildSpecieSuccessiva(MTApplication context, final MTComponent root,
			final CGSceneManager cgScene, final Giocatore g, final int punteggioGiocatore) {
		listaClassi(cgScene).remove(cgScene.getClasse());

		if (listaClassi(cgScene).size() == 0) {
			cgScene.setClasse(null);
			try {
				g.setPunteggio(punteggioGiocatore);
				root.removeAllChildren();
				root.destroy();
				System.gc();
				cgScene.buildFineGioco(g);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			MTRectangle infoRect = new MTRectangle(context, context.getAlignmentX() + 475,
					context.getAlignmentY() + 350, context.width / 1.95f, context.height - 700);
			infoRect.setFillColor(MyColors.lightyellow());
			infoRect.setStrokeColor(MTColor.BLACK);

			IFont titoloFont = titoloPopupVerde(context);
			MTTextArea titoloText = new MTTextArea(context);
			titoloText.setFont(titoloFont);
			titoloText.setText("Regno completato");
			titoloText.setPositionRelativeToOther(infoRect,
					new Vector3D(infoRect.getCenterPointGlobal().x + 20, infoRect.getCenterPointGlobal().y / 2 + 120));

			MTTextArea domandaText = new MTTextArea(context);
			domandaText.setPositionRelativeToOther(infoRect,
					new Vector3D(titoloText.getCenterPointGlobal().x - 380, titoloText.getCenterPointGlobal().y + 75));
			IFont testoFont = testoPopup(context);
			domandaText.setFont(testoFont);
			domandaText.setText("Preparati, adesso passerai al regno successivo!");

			PImage loadImage = context.loadImage(FilesPath.rootGame + "cane.png");
			MTRectangle imagePopup = new MTRectangle(context, loadImage);
			imagePopup.setPositionRelativeToOther(infoRect,
					new Vector3D(context.width * 0.315f, context.height * 0.57f - 3));
			PImage logook = context.loadImage(FilesPath.rootGame + "ok.png");

			MTRectangle logookRect = new MTRectangle(context, logook);
			logookRect.setPositionRelativeToOther(infoRect,
					new Vector3D(context.width * 0.70f, context.height * 0.60f));

			logookRect.registerInputProcessor(new TapProcessor(context));
			logookRect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						CGClasse specieSuccessiva;

						Iterator<CGClasse> specie = listaClassi(cgScene).iterator();
						while (specie.hasNext()) {
							specieSuccessiva = (CGClasse) specie.next();
							cgScene.setClasse(specieSuccessiva);
						}
						try {
							g.setPunteggio(punteggioGiocatore);
							root.removeAllChildren();
							root.destroy();
							System.gc();
							cgScene.buildSinglePlayerScene(g);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					return true;
				}
			});

			proprietaSpecieSuccessiva(root, infoRect, titoloText, domandaText, imagePopup, logookRect);
		}
	}

	public class gestureListenerRiprendi implements IGestureEventListener {
		private MTComponent root;
		private MTRectangle questionRect;

		public gestureListenerRiprendi(MTComponent root, MTRectangle questionRect) {
			this.root = root;
			this.questionRect = questionRect;
		}

		public boolean processGestureEvent(MTGestureEvent ge) {
			if (ge instanceof TapEvent) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					removeEsciGioco(root, questionRect);
				}
			}
			return true;
		}
	}

	private static void removeEsciGioco(final MTComponent root, MTRectangle questionRect) {
		root.removeChild(questionRect);
		System.gc();
	}

	private static void homeBack(final CGSceneManager cgScene, final MTComponent root) throws SQLException {
		cgScene.setCategoria(null);
		cgScene.setEntita(null);
		root.removeAllChildren();
		root.destroy();
		System.gc();
		cgScene.buildSplashScreen();
	}

	private static void homeBack1(final CGSceneManager cgScene, final MTComponent root) throws SQLException {
		cgScene.setGame(null);
		root.removeAllChildren();
		root.destroy();
		System.gc();
		cgScene.buildSplashScreen();
	}

	private static void homeBackRegole(final CGSceneManager cgScene, final MTComponent root) throws SQLException {
		root.removeAllChildren();
		root.destroy();
		System.gc();
		cgScene.buildGameRegoleScene();
	}

	public static void propriet‡Specie(MTApplication context, MTComponent root, CGClasse specie,
			MTRectangle specieButton, MTTextArea specieNome, MTRectangle logoTexture) {
		lockAll(specieButton);
		lockStrokeRectangle(logoTexture);
		lockStrokeFillText(specieNome);

		root.addChild(specieButton);
		root.addChild(logoTexture);
		root.addChild(specieNome);

		specieButton.registerInputProcessor(new TapProcessor(context));
		specieNome.registerInputProcessor(new TapProcessor(context));
		logoTexture.registerInputProcessor(new TapProcessor(context));
	}

	public static void propriet‡Categoria(MTApplication context, MTComponent root, MTRectangle categoriaButton,
			MTTextArea categoriaNome) {
		lockAll(categoriaButton);
		lockStrokeFillText(categoriaNome);

		categoriaButton.registerInputProcessor(new TapProcessor(context));
		categoriaNome.registerInputProcessor(new TapProcessor(context));

		root.addChild(categoriaButton);
		root.addChild(categoriaNome);
	}

	public static void propriet‡Giocatore(MTApplication context, MTComponent root, MTRectangle singleButton,
			MTTextArea singleText, MTRectangle singleTexture) {
		lockAll(singleButton);
		lockStrokeRectangle(singleTexture);
		lockStrokeFillText(singleText);

		singleButton.registerInputProcessor(new TapProcessor(context));
		singleText.registerInputProcessor(new TapProcessor(context));
		singleTexture.registerInputProcessor(new TapProcessor(context));

		root.addChild(singleButton);
		root.addChild(singleTexture);
		root.addChild(singleText);
	}

	public static void propriet‡Regole(MTComponent root, MTRectangle regoleRect, MTRectangle logoRagazzoRect,
			MTRectangle logoFumettoRect, MTTextArea titoloRegole, MTTextArea regoleText, MTTextArea fineregoleText,
			MTRectangle logookRect) {
		lockAll(regoleRect);
		lockStrokeRectangle(logoRagazzoRect);
		lockStrokeRectangle(logoFumettoRect);
		lockStrokeFillText(titoloRegole);
		lockStrokeFillText(regoleText);
		lockStrokeFillText(fineregoleText);
		lockStrokeRectangle(logookRect);

		root.addChild(regoleRect);
		root.addChild(logoRagazzoRect);
		root.addChild(logoFumettoRect);
		root.addChild(titoloRegole);
		root.addChild(regoleText);
		root.addChild(fineregoleText);
		root.addChild(logookRect);
	}

	public static void propriet‡CategoriaGioco(MTRectangle categorieContainer, MTRectangle categoriaTitle,
			MTTextArea categoriaNome, MTImage categoriaImmagine) {
		lockAll(categoriaTitle);
		lockStrokeFillText(categoriaNome);
		lockAll(categoriaImmagine);

		categoriaTitle.setStrokeColor(MTColors.WHITE);
		categoriaTitle.setFillColor(MyColors.azzurro());

		categoriaImmagine.setNoFill(true);
		categoriaImmagine.setNoStroke(true);

		categorieContainer.addChild(categoriaTitle);
		categorieContainer.addChild(categoriaNome);
		categorieContainer.addChild(categoriaImmagine);
	}

	public static void propriet‡EntitaGioco(MTApplication context, MTRectangle entitaContainer,
			MTRectangle entitaImmagine, MTTextArea entitaNome) {
		lockAll(entitaImmagine);
		lockAll(entitaNome);

		entitaImmagine.setNoFill(true);
		entitaImmagine.setStrokeWeight(8);
		entitaImmagine.setGestureAllowance(DragProcessor.class, true);
		entitaImmagine.setGestureAllowance(RotateProcessor.class, true);
		entitaImmagine.setGestureAllowance(ScaleProcessor.class, true);
		entitaImmagine.addGestureListener(DragProcessor.class, new InertiaDragAction());

		entitaNome.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 26, MTColor.WHITE));
		entitaNome.setFillColor(MTColors.RED);
		entitaNome.setStrokeColor(MTColors.BLACK);

		entitaContainer.addChild(entitaImmagine);
		entitaImmagine.addChild(entitaNome);
	}

	private static void proprietaPopupEsciGioco(MTApplication context, final MTComponent root,
			final MTRectangle infoRect, MTRectangle closeRect, MTTextArea titoloText, MTTextArea domandaText,
			MTRectangle tartarugaRect, MTRectangle annullaRect, MTRectangle okRect) {
		lockAll(infoRect);
		lockStrokeRectangle(closeRect);
		lockStrokeFillText(titoloText);
		lockStrokeFillText(domandaText);
		lockStrokeRectangle(tartarugaRect);
		lockStrokeRectangle(annullaRect);
		lockStrokeRectangle(okRect);

		root.addChild(infoRect);
		infoRect.addChild(closeRect);
		infoRect.addChild(titoloText);
		infoRect.addChild(domandaText);
		infoRect.addChild(tartarugaRect);
		infoRect.addChild(annullaRect);
		infoRect.addChild(okRect);
	}

	public static void proprietaPopupCorrettoSbagliato(final MTComponent root, MTRectangle infoRect,
			MTTextArea titoloText, MTTextArea domandaText, MTRectangle imagePopup) {
		lockAll(infoRect);
		lockStrokeRectangle(imagePopup);
		lockStrokeFillText(titoloText);
		lockStrokeFillText(domandaText);

		root.addChild(infoRect);
		root.addChild(imagePopup);
		root.addChild(titoloText);
		root.addChild(domandaText);
	}

	public static void proprietaSpecieSuccessiva(final MTComponent root, MTRectangle infoRect, MTTextArea titoloText,
			MTTextArea domandaText, MTRectangle imagePopup, MTRectangle logookRect) {
		lockAll(infoRect);
		lockStrokeRectangle(imagePopup);
		lockStrokeFillText(titoloText);
		lockStrokeFillText(domandaText);
		lockStrokeRectangle(logookRect);

		root.addChild(infoRect);
		root.addChild(imagePopup);
		root.addChild(titoloText);
		root.addChild(domandaText);
		root.addChild(logookRect);
	}

	public static void proprietaMultiPlayer(final MTComponent root, MTRectangle containerCategorieSuper,
			MTRectangle containerG1, MTRectangle containerG2) {
		containerCategorieSuper.setNoFill(true);
		containerCategorieSuper.setNoStroke(true);

		containerG1.setFillColor(MyColors.grigioverde());
		containerG1.setStrokeColor(MTColors.WHITE);

		containerG2.setFillColor(MyColors.lavanda());
		containerG2.setStrokeColor(MTColors.WHITE);

		lockAll(containerCategorieSuper);
		lockAll(containerG1);
		lockAll(containerG2);

		root.addChild(containerCategorieSuper);
		root.addChild(containerG1);
		root.addChild(containerG2);
	}

	public static void proprietaCategorieSuper(MTRectangle containerCategorieSuper, MTRectangle categoriaTitleSuper,
			MTTextArea categoriaNomeSuper, MTImage categoriaImmagineSuper) {
		lockAll(categoriaTitleSuper);
		lockStrokeFillText(categoriaNomeSuper);
		lockAll(categoriaImmagineSuper);

		categoriaTitleSuper.setStrokeColor(MTColors.WHITE);
		categoriaTitleSuper.setFillColor(MyColors.azzurro());

		categoriaNomeSuper.setNoFill(true);
		categoriaNomeSuper.setNoStroke(true);

		categoriaImmagineSuper.setNoFill(true);
		categoriaImmagineSuper.setNoStroke(true);

		containerCategorieSuper.addChild(categoriaTitleSuper);
		containerCategorieSuper.addChild(categoriaNomeSuper);
		containerCategorieSuper.addChild(categoriaImmagineSuper);
	}

	public static void proprietaCategorieGiocatore1(MTRectangle categorieContainerG1,
			MTRectangle categoriaTitleG1, MTTextArea categoriaNomeG1,
			MTImage categoriaImmagineG1) {
		lockAll(categoriaTitleG1);
		lockStrokeFillText(categoriaNomeG1);
		lockAll(categoriaImmagineG1);

		categoriaTitleG1.setStrokeColor(MTColors.WHITE);
		categoriaTitleG1.setFillColor(MyColors.azzurro());

		categoriaNomeG1.setNoFill(true);
		categoriaNomeG1.setNoStroke(true);

		categoriaImmagineG1.setNoFill(true);
		categoriaImmagineG1.setNoStroke(true);

		categorieContainerG1.addChild(categoriaTitleG1);
		categorieContainerG1.addChild(categoriaNomeG1);
		categorieContainerG1.addChild(categoriaImmagineG1);
	}

	public static void proprietaCategorieGiocatore2(MTRectangle categorieContainerG2,
			MTRectangle categoriaTitleG2, MTTextArea categoriaNomeG2,
			MTImage categoriaImmagineG2) {
		lockAll(categoriaTitleG2);
		lockStrokeFillText(categoriaNomeG2);
		lockAll(categoriaImmagineG2);

		categoriaTitleG2.setStrokeColor(MTColors.WHITE);
		categoriaTitleG2.setFillColor(MyColors.azzurro());

		categoriaNomeG2.setNoFill(true);
		categoriaNomeG2.setNoStroke(true);

		categoriaImmagineG2.setNoFill(true);
		categoriaImmagineG2.setNoStroke(true);

		categorieContainerG2.addChild(categoriaTitleG2);
		categorieContainerG2.addChild(categoriaNomeG2);
		categorieContainerG2.addChild(categoriaImmagineG2);
	}

	public static void proprietaEntitaGiocatore1(MTApplication context, MTRectangle entitaContainerG1,
			MTTextArea entitaNomeG1, MTRectangle entitaImmagineG1) {
		lockAll(entitaImmagineG1);
		lockAll(entitaNomeG1);

		entitaImmagineG1.setNoFill(true);
		entitaImmagineG1.setStrokeWeight(5);
		entitaImmagineG1.setGestureAllowance(DragProcessor.class, true);
		entitaImmagineG1.setGestureAllowance(RotateProcessor.class, true);
		entitaImmagineG1.setGestureAllowance(ScaleProcessor.class, true);
		entitaImmagineG1.addGestureListener(DragProcessor.class, new InertiaDragAction());

		entitaNomeG1.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 22, MTColor.WHITE));
		entitaNomeG1.setFillColor(MTColors.RED);
		entitaNomeG1.setStrokeColor(MTColors.BLACK);

		entitaContainerG1.addChild(entitaImmagineG1);
		entitaImmagineG1.addChild(entitaNomeG1);
	}
	
	public static void proprietaEntitaGiocatore2(MTApplication context, MTRectangle entitaContainerG2,
			MTTextArea entitaNomeG2, MTRectangle entitaImmagineG2) {
		lockAll(entitaImmagineG2);
		lockAll(entitaNomeG2);

		entitaImmagineG2.setNoFill(true);
		entitaImmagineG2.setStrokeWeight(5);
		entitaImmagineG2.setGestureAllowance(DragProcessor.class, true);
		entitaImmagineG2.setGestureAllowance(RotateProcessor.class, true);
		entitaImmagineG2.setGestureAllowance(ScaleProcessor.class, true);
		entitaImmagineG2.addGestureListener(DragProcessor.class, new InertiaDragAction());

		entitaNomeG2.setFont(FontManager.getInstance().createFont(context, "arial.ttf", 22, MTColor.WHITE));
		entitaNomeG2.setFillColor(MTColors.RED);
		entitaNomeG2.setStrokeColor(MTColors.BLACK);

		entitaContainerG2.addChild(entitaImmagineG2);
		entitaImmagineG2.addChild(entitaNomeG2);
	}

	public static List<CGClasse> listaClassi(CGSceneManager cgScene) {
		return cgScene.getListaClassi();
	}

	public static List<CGCategoria> listaCategorie(CGSceneManager cgScene) {
		return cgScene.getListaCategorie();
	}

	public static List<CGEntita> listaEntita(CGSceneManager cgScene) {
		return cgScene.getListaEntita();
	}

}