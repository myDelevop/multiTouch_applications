package wwf.ClassificationGame.view.game;

import java.util.Iterator;
import java.util.LinkedList;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.sceneManager.Giocatore;
import wwf.ClassificationGame.util.RispostaSbagliata;
import wwf.ClassificationGame.util.RispostaCorretta;
import wwf.ClassificationGame.util.RispostaCorrettaTransazione;

public class MTRectangleEntita extends MTRectangle {
	private MTApplication context;
	boolean indovinato = false;
	boolean sbagliato = false;
	private SinglePlayer singleplayer;
	private MultiPlayer multiplayer;
	private Giocatore g1;
	private Giocatore g2;
	private LinkedList<MTImage> categorie = new LinkedList<>();
	private LinkedList<MTRectangleEntita> entitaDomande = new LinkedList<>();
	private boolean lampeggioAttivo = false;
	int i = 0;
	private String typeEntita;
	private boolean contained = true;

	public MTRectangleEntita(PApplet pApplet, PImage texture, MultiPlayer multiplayer, Giocatore g1, Giocatore g2,
			MTApplication app, CGSceneManager cgScene) {
		super(pApplet, texture);
		this.multiplayer = multiplayer;
		new MTComponent(app);
		this.g1 = g1;
		this.g2 = g2;
	}

	public MTRectangleEntita(PApplet pApplet, PImage texture, SinglePlayer singleplayer, Giocatore g1,
			MTApplication app, CGSceneManager cgScene) {
		super(pApplet, texture);
		this.singleplayer = singleplayer;
		new MTComponent(app);
		this.g1 = g1;
	}

	public void associaEntitaACategoria(LinkedList<MTImage> categorieDomande, String typeGame) {
		this.categorie = categorieDomande;
	}

	public void eliminaEntitaIndovinata(LinkedList<MTRectangleEntita> entitaDomande) {
		this.entitaDomande = entitaDomande;
	}

	@Override
	public void translate(Vector3D dirVect) {
		Iterator<MTImage> categorieIt = categorie.iterator();

		Vector3D futurePoint = new Vector3D(this.getCenterPointGlobal().getX() + dirVect.x,
				this.getCenterPointGlobal().getY() + dirVect.y);
		try {
			contained = ((MTComponent) this.getParent().getParent()).containsPoint(futurePoint, this);
		} catch (NullPointerException e) {
		}

		if (contained)
			super.translate(dirVect);

		// check if the image is inside the proper category
		while (categorieIt.hasNext()) {
			MTImage cgCategoria = (MTImage) categorieIt.next();
			if (cgCategoria.containsPointGlobal(this.getCenterPointGlobal())) {
				if (this.getName().contains(cgCategoria.getName())) {
					typeEntita = this.getName();
					entitaIndovinata();
				} else {
					typeEntita = this.getName();
					entitaNonIndovinata();
				}
			}
		}
		sbagliato = false;
	}

	private void entitaIndovinata() {
		if (!indovinato && !lampeggioAttivo) {
			new RispostaCorrettaTransazione(this, "CHIUDI");
			if (typeEntita.contains("G1")) {
				g1.setNome(multiplayer.nomeG1);
				multiplayer.punteggioG1 = multiplayer.punteggioG1 + 100;
				g1.setPunteggio(multiplayer.punteggioG1);
				multiplayer.giocatoreTextG1.setText("Nome : " + g1.getNome() + "\nPunteggio : " + g1.getPunteggio());
				multiplayer.entitaIndovinateMulti = multiplayer.entitaIndovinateMulti + 1;
				new RispostaCorretta(this, context, multiplayer, "multiplayer", typeEntita, entitaDomande,
						this.getName());
			} else if (typeEntita.contains("G2")) {
				g2.setNome(multiplayer.nomeG2);
				multiplayer.punteggioG2 = multiplayer.punteggioG2 + 100;
				g2.setPunteggio(multiplayer.punteggioG2);
				multiplayer.giocatoreTextG2.setText("Nome : " + g2.getNome() + "\nPunteggio : " + g2.getPunteggio());
				multiplayer.entitaIndovinateMulti = multiplayer.entitaIndovinateMulti + 1;
				new RispostaCorretta(this, context, multiplayer, "multiplayer", typeEntita, entitaDomande,
						this.getName());
			} else {
				g1.setNome(singleplayer.nomeG1);
				singleplayer.punteggioG1 = singleplayer.punteggioG1 + 100;
				g1.setPunteggio(singleplayer.punteggioG1);
				singleplayer.giocatoreText.setText("Nome : " + g1.getNome() + "\nPunteggio : " + g1.getPunteggio());
				singleplayer.entitaIndovinate = singleplayer.entitaIndovinate + 1;
				new RispostaCorretta(this, context, singleplayer, "singleplayer");
			}
			indovinato = true;
		}
	}

	private void entitaNonIndovinata() {
		if (!sbagliato && !lampeggioAttivo) {
			if (typeEntita.contains("G1")) {
				g1.setNome(multiplayer.nomeG1);
				multiplayer.punteggioG1 = multiplayer.punteggioG1 - 30;
				g1.setPunteggio(multiplayer.punteggioG1);
				multiplayer.giocatoreTextG1.setText("Nome : " + g1.getNome() + "\nPunteggio : " + g1.getPunteggio());
				new RispostaSbagliata(this, context, multiplayer, "multiplayer", typeEntita);
			} else if (typeEntita.contains("G2")) {
				g2.setNome(multiplayer.nomeG2);
				multiplayer.punteggioG2 = multiplayer.punteggioG2 - 30;
				g2.setPunteggio(multiplayer.punteggioG2);
				multiplayer.giocatoreTextG2.setText("Nome : " + g2.getNome() + "\nPunteggio : " + g2.getPunteggio());
				new RispostaSbagliata(this, context, multiplayer, "multiplayer", typeEntita);
			} else {
				g1.setNome(singleplayer.nomeG1);
				singleplayer.punteggioG1 = singleplayer.punteggioG1 - 30;
				g1.setPunteggio(singleplayer.punteggioG1);
				singleplayer.giocatoreText
						.setText("Nome : " + g1.getNome() + "\n" + "Punteggio : " + g1.getPunteggio());
				new RispostaSbagliata(this, context, singleplayer, "singleplayer");
			}
			sbagliato = true;
		}
	}

	public boolean isLampeggioAttivo() {
		return lampeggioAttivo;
	}

	public void setLampeggioAttivo(boolean lampeggioAttivo) {
		this.lampeggioAttivo = lampeggioAttivo;
	}

}
