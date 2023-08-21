package wwf.ClassificationGame.util;

import static wwf.ClassificationGame.util.Utility.*;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4jx.util.MTColors;

import processing.core.PApplet;
import wwf.ClassificationGame.view.game.MTImageEntita;
import wwf.ClassificationGame.view.game.MultiPlayer;
import wwf.ClassificationGame.view.game.SinglePlayer;

public class RispostaCorretta extends Thread {
	private MTImageEntita root;
	MTApplication context;
	SinglePlayer singleplayer;
	MultiPlayer multiplayer;
	int durata = 200;
	PApplet applet;

	public RispostaCorretta(MTImageEntita root, PApplet applet, MultiPlayer multiplayer) {
		this.root = root;
		this.applet = applet;
		this.multiplayer = multiplayer;
		multiplayer.buildPopupRisposta("corretto");
		start();
	}

	public RispostaCorretta(MTImageEntita root, PApplet applet, SinglePlayer singleplayer) {
		this.root = root;
		this.applet = applet;
		this.singleplayer = singleplayer;
		singleplayer.buildPopupRisposta("corretto");
		start();
	}

	@Override
	public void run() {
		super.run();
		root.setLampeggioAttivo(true);
		while (durata-- > 0) {
			try {
				Thread.sleep(10);
				// Verde
				if (root instanceof MTPolygon) {
					((MTPolygon) root).setStrokeColor(MTColors.GREEN);
					durata--;
				}
				// Bianco
				if (root instanceof MTPolygon) {
					Thread.sleep(10);
					((MTPolygon) root).setStrokeColor(MTColors.WHITE);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (multiplayer.toString().toLowerCase().contains("multiplayer")) {
			multiplayer.rimuoviPopupRisposta();
		} else {
			singleplayer.rimuoviPopupRisposta();

			if (singleplayer.entitaIndovinate == singleplayer.entitaTotali) {
				buildSpecieSuccessiva(singleplayer.getContext(), singleplayer.getRootComponent(),
						singleplayer.getCgScene(), singleplayer.getG(), singleplayer.getPunteggioGiocatore());
			}
		}
		root.setLampeggioAttivo(false);
	}
}
