package wwf.ClassificationGame.view.game;

import java.util.Iterator;
import java.util.LinkedList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.sceneManager.Giocatore;
import wwf.ClassificationGame.util.RispostaSbagliata;
import wwf.ClassificationGame.util.RispostaCorretta;
import wwf.ClassificationGame.util.RispostaCorrettaTransazione;

public class MTImageEntita extends MTImage {
	private MTApplication context;
	private MTImage categoriaAssociata;
	boolean indovinato = false;
	boolean sbagliato = false;
	private SinglePlayer singleplayer;
	private MultiPlayer multiplayer;
	private Giocatore g;
	private Giocatore g1;
	private Giocatore g2;
	private LinkedList<MTImage> categorie = new LinkedList<>();
	private boolean lampeggioAttivo = false;
	private MTImageEntita istanza;
	int i = 0;
	private String typeEntita;

	public MTImageEntita(PApplet pApplet, PImage texture, MultiPlayer multiplayer, Giocatore g1, Giocatore g2,
			MTApplication app, CGSceneManager cgScene) {
		super(pApplet, texture);
		this.multiplayer = multiplayer;
		new MTComponent(app);
		this.g1 = g1;
		this.g2 = g2;
		istanza = this;
		this.addGestureListener(DragProcessor.class, listnerButton);
		this.addGestureListener(ScaleProcessor.class, listnerButton);
		this.addGestureListener(RotateProcessor.class, listnerButton);
	}

	public MTImageEntita(PApplet pApplet, PImage texture, SinglePlayer singleplayer, Giocatore g, MTApplication app,
			CGSceneManager cgScene) {
		super(pApplet, texture);
		this.singleplayer = singleplayer;
		new MTComponent(app);
		this.g = g;
		istanza = this;
		this.addGestureListener(DragProcessor.class, listnerButton);
		this.addGestureListener(ScaleProcessor.class, listnerButton);
		this.addGestureListener(RotateProcessor.class, listnerButton);
	}

	public void associaEntitaACategoria(LinkedList<MTImage> categorieDomande, String typeGame) {
		this.categorie = categorieDomande;
	}

	public void translate(Vector3D dirVect) {
		super.translate(dirVect);
		Iterator<MTImage> categorieIt = categorie.iterator();
		while (categorieIt.hasNext()) {
			MTImage cgCategoria = (MTImage) categorieIt.next();
			if (cgCategoria.containsPointGlobal(this.getCenterPointGlobal())) {
//				System.out.println(cgCategoria.getName() + "-----" + this.getName());
				if (cgCategoria.getName().equalsIgnoreCase(this.getName())) {
					typeEntita = this.getName();
					entitaIndovinata();
				}
				else {
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
				new RispostaCorretta(this, context, multiplayer);
			} else if (typeEntita.contains("G2")) {
				g2.setNome(multiplayer.nomeG2);
				multiplayer.punteggioG2 = multiplayer.punteggioG2 + 100;
				g2.setPunteggio(multiplayer.punteggioG2);
				multiplayer.giocatoreTextG2.setText("Nome : " + g2.getNome() + "\nPunteggio : " + g2.getPunteggio());
				new RispostaCorretta(this, context, multiplayer);
			} else {
				g.setNome(singleplayer.nomeGiocatore);
				singleplayer.punteggioGiocatore = singleplayer.punteggioGiocatore + 100;
				g.setPunteggio(singleplayer.punteggioGiocatore);
				singleplayer.giocatoreText.setText("Nome : " + g.getNome() + "\nPunteggio : " + g.getPunteggio());
				singleplayer.entitaIndovinate = singleplayer.entitaIndovinate + 1;
				new RispostaCorretta(this, context, singleplayer);
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
				new RispostaSbagliata(this, context, multiplayer);
			} else if (typeEntita.contains("G2")) {
				g2.setNome(multiplayer.nomeG2);
				multiplayer.punteggioG2 = multiplayer.punteggioG2 - 30;
				g2.setPunteggio(multiplayer.punteggioG2);
				multiplayer.giocatoreTextG2.setText("Nome : " + g2.getNome() + "\nPunteggio : " + g2.getPunteggio());
				new RispostaSbagliata(this, context, multiplayer);
			} else {
				new RispostaSbagliata(this, context, singleplayer);
				g.setNome(singleplayer.nomeGiocatore);
				singleplayer.punteggioGiocatore = singleplayer.punteggioGiocatore - 30;
				g.setPunteggio(singleplayer.punteggioGiocatore);
				singleplayer.giocatoreText.setText("Nome : " + g.getNome() + "\n" + "Punteggio : " + g.getPunteggio());
			}
			sbagliato = true;
		}
	}

	public MTImage getCategoriaAssociata() {
		return categoriaAssociata;
	}

	public void setCategoriaAssociata(MTImage categoriaAssociata) {
		this.categoriaAssociata = categoriaAssociata;
	}

	public boolean isLampeggioAttivo() {
		return lampeggioAttivo;
	}

	public void setLampeggioAttivo(boolean lampeggioAttivo) {
		this.lampeggioAttivo = lampeggioAttivo;
	}

	IGestureEventListener listnerButton = new IGestureEventListener() {
		@Override
		public boolean processGestureEvent(MTGestureEvent ge) {
			if (MTGestureEvent.GESTURE_ENDED == ge.getId()) {
				istanza.setSelected(false);
			} else {
				istanza.setSelected(true);
			}
			return false;
		}
	};
}
