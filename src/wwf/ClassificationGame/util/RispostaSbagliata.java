package wwf.ClassificationGame.util;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.util.MTColors;

import processing.core.PApplet;
import wwf.ClassificationGame.view.game.MTImageEntita;
import wwf.ClassificationGame.view.game.MultiPlayer;
import wwf.ClassificationGame.view.game.SinglePlayer;

public class RispostaSbagliata extends Thread {
	private MTImageEntita root;
	MTApplication context;
	SinglePlayer singleplayer;
	MultiPlayer multiplayer;
	int durata = 200;
	PApplet applet;

	public RispostaSbagliata(MTImageEntita root, PApplet applet, MultiPlayer multiplayer) {
		this.root = root;
		this.applet = applet;
		this.multiplayer = multiplayer;
		multiplayer.buildPopupRisposta("sbagliato");
		start();
	}

	public RispostaSbagliata(MTImageEntita root, PApplet applet, SinglePlayer singleplayer) {
		this.root = root;
		this.applet = applet;
		this.singleplayer = singleplayer;
		singleplayer.buildPopupRisposta("sbagliato");
		start();
	}

	@Override
	public void run() {
		super.run();
		root.setLampeggioAttivo(true);
		while (durata-- > 0) {
			try {
				Thread.sleep(10);
				// Rosso
				if (root instanceof MTPolygon) {
					((MTPolygon) root).setStrokeColor(MTColors.RED);
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
			Vector3D centerPoint = getRefCompCenterRelParent(root);
			float x = centerPoint.x;
			((MTPolygon) root).setPositionGlobal(new Vector3D(x, 650));
		} else {
			singleplayer.rimuoviPopupRisposta();
			Vector3D centerPoint = getRefCompCenterRelParent(root);
			float x = centerPoint.x;
			((MTPolygon) root).setPositionGlobal(new Vector3D(x, 500));
		}

		root.setLampeggioAttivo(false);
	}

	protected Vector3D getRefCompCenterRelParent(AbstractShape shape) {
		Vector3D centerPoint;
		if (shape.isBoundingShapeSet()) {
			centerPoint = shape.getBoundingShape().getCenterPointLocal();
			centerPoint.transform(shape.getLocalMatrix());
		} else {
			Vector3D localObjCenter = shape.getCenterPointGlobal();
			localObjCenter.transform(shape.getGlobalInverseMatrix());
			localObjCenter.transform(shape.getLocalMatrix());
			centerPoint = localObjCenter;
		}
		return centerPoint;
	}
}
