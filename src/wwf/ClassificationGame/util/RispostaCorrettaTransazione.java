package wwf.ClassificationGame.util;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

public class RispostaCorrettaTransazione {
	MTPolygon componente;
	String tipoAzione;

	public RispostaCorrettaTransazione(MTPolygon componente, String tipoAzione) {
		this.componente = componente;
		this.tipoAzione = tipoAzione;
		chiudiImmagine();
	}

	private void chiudiImmagine() {
		final MTComponent[] comps = new MTComponent[] { componente };
		MTPolygon referencePol = componente;

		for (int i = 0; i < comps.length; i++) {
			MTComponent comp = comps[i];
			if (comp instanceof MTPolygon) {
				MTPolygon poly = (MTPolygon) comp;
				if (referencePol == null) {
					referencePol = poly;
				}
			}
		}
		final MTPolygon referencePoly = referencePol;
		float width = referencePoly.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

		float from = 0;
		float to = 0;
		if (tipoAzione.compareTo("CHIUDI") == 0) {
			from = width;
			to = 1;
		} else if (tipoAzione.compareTo("APRI") == 0) {
			to = width;
			from = 1;
		}
		Animation closeAnim = new Animation("comp Fade", new MultiPurposeInterpolator(from, to, 2000, 0.5f, 0.8f, 1),
				referencePoly);
		closeAnim.addAnimationListener(new IAnimationListener() {
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getCurrentValue();
					resize(referencePoly, comps[0], currentVal, currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					if (tipoAzione.compareTo("CHIUDI") == 0) {
						comps[0].setVisible(false);
						for (int i = comps.length - 1; i > 0; i--) {
							MTComponent currentComp = comps[i];
							currentComp.destroy();
						}
						componente.destroy();
					}
					break;
				default:
					if (tipoAzione.compareTo("CHIUDI") == 0)
						componente.destroy();
					break;
				}// switch
			}// processanimation
		});// new IAnimationListener
		closeAnim.start();
	}

	protected void resize(MTPolygon referenceComp, MTComponent compToResize, float width, float height) {
		Vector3D centerPoint = getRefCompCenterRelParent(referenceComp);
		compToResize.scale(1 / referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT),
				(float) 1 / referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 1, centerPoint,
				TransformSpace.RELATIVE_TO_PARENT);
		compToResize.scale(width, width, 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
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
