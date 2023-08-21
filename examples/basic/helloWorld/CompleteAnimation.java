package basic.helloWorld;

import javax.swing.plaf.metal.MetalTreeUI;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

public class CompleteAnimation {
	Vector3D startPoint = new Vector3D(0, 0);
	Vector3D endPoint = new Vector3D(0, 0);
	Animation moveAnimation = null;
	MTRectangle component;
	float finalWidth;
	float startHeight;
	boolean destroyAfterAnimation = false;

	public CompleteAnimation(MTRectangle comp, long duration, float accellerationEndTime, float decelleratioEndTime, int loopCount){
		this.component = comp;
		MultiPurposeInterpolator interpolator = new MultiPurposeInterpolator(0, 100, duration, accellerationEndTime, decelleratioEndTime, loopCount);
		moveAnimation = new Animation("move animation", interpolator, this, 0);
		finalWidth = comp.getWidthXY(TransformSpace.GLOBAL);
		startPoint = component.getPosition(TransformSpace.GLOBAL);
	//	System.out.println("La lunghezza di partenza e' " + comp.getWidthXY(TransformSpace.GLOBAL));
	}

	public void start(){
		final float offsetX = (startPoint.getX() - endPoint.getX() ) / 100 ;
		final float offsetY = (startPoint.getY() - endPoint.getY() ) / 100;
		final float offsetWidth = (finalWidth - component.getWidthXY(TransformSpace.GLOBAL))/100;

		//final float offsetHeight = finalHeigth - component.getHeightXY(TransformSpace.GLOBAL);

		moveAnimation.addAnimationListener(new IAnimationListener() {
			@Override
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getDelta();
					if(startPoint.getX() != 0  || endPoint.getX()!=0 || startPoint.getY()!=0 || endPoint.getY()!=0){
						component.setPositionGlobal(new Vector3D(component.getCenterPointGlobal().getX() - offsetX * currentVal, component.getCenterPointGlobal().getY() - offsetY * currentVal));
					}
					if (offsetWidth != 0)
						component.setWidthXYGlobal(component.getWidthXY(TransformSpace.GLOBAL) + offsetWidth * currentVal);
				//	System.out.println("Grandezza componente " + component.getWidthXY(TransformSpace.GLOBAL));
					//	resize(component, component, currentVal, currentVal);
					//	drag(button,currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
						if(destroyAfterAnimation)
							component.destroy();
					break;	
				default:

					break;
				}
			}
		});
		moveAnimation.start();
	}

	protected void resize(MTPolygon referenceComp,MTComponent compToResize, float width, float height){ 
		Vector3D centerPoint = getRefCompCenterRelParent(referenceComp);
		compToResize.scale(1/referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), (float)1/referenceComp.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
		compToResize.scale(width, width, 1, centerPoint, TransformSpace.RELATIVE_TO_PARENT);
	}

	protected Vector3D getRefCompCenterRelParent(AbstractShape shape){
		Vector3D centerPoint;
		if (shape.hasBounds()){
			centerPoint = shape.getBounds().getCenterPointLocal();
			centerPoint.transform(shape.getLocalMatrix()); //macht den punkt in self space
		}else{
			Vector3D localObjCenter = shape.getCenterPointGlobal();
			localObjCenter.transform(shape.getGlobalInverseMatrix()); //to localobj space
			localObjCenter.transform(shape.getLocalMatrix()); //to parent relative space
			centerPoint = localObjCenter;
		}
		return centerPoint;
	}

	public Vector3D getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector3D startPoint) {
		this.startPoint = startPoint;
	}

	public Vector3D getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector3D endPoint) {
		this.endPoint = endPoint;
	}

	public float getFinalWidth() {
		return finalWidth;
	}

	public void setFinalWidth(float finalWidth) {
		this.finalWidth = finalWidth;
	}

	public float getStartHeight() {
		return startHeight;
	}

	public void setStartHeight(float startHeight) {
		this.startHeight = startHeight;
	}

	public boolean isDestroyAfterAnimation() {
		return destroyAfterAnimation;
	}

	public void setDestroyAfterAnimation(boolean destroyAfterAnimation) {
		this.destroyAfterAnimation = destroyAfterAnimation;
	}


}
