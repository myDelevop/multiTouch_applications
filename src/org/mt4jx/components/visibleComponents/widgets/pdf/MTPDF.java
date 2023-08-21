/***********************************************************************
 *   MT4j Extension: PDF
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License (LGPL)
 *   as published by the Free Software Foundation, either version 3
 *   of the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the LGPL
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package org.mt4jx.components.visibleComponents.widgets.pdf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author Uwe Laufs
 *
 */
public class MTPDF extends MTRectangle {
	private File pdf;
	private PApplet pApplet;
	private int pageNumber;
	private MTPolygon referencePoly;
	MTApplication mtApplication;
	/**
	 * limit in pixels for pdf rendering result to avoid memory problems
	 */
	private int sizeLimitX=1280;
	private boolean autoUpdate = true;
	public MTPDF(MTApplication mtApplication, File pdf){
		this(mtApplication, pdf, 1);
	}

	public MTPDF(PApplet pApplet, File pdf, int pageNumber){
		super(pApplet,0,0);
		this.mtApplication = (MTApplication) pApplet;
		referencePoly = this;
		this.pdf = pdf;
		this.pApplet = pApplet;
		this.pageNumber = pageNumber;

		PImage img= null;
		try {
			img = new PImage(PDFRenderer.loadImage(pdf, 1d, pageNumber));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.setWidthLocal(img.width);
		this.setHeightLocal(img.height);
		this.setTexture(img);

		final PApplet pa = pApplet;
		this.addGestureListener(ScaleProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge instanceof ScaleEvent && autoUpdate){
					switch (ge.getId()) {
					case ScaleEvent.GESTURE_ENDED:
						((MTApplication)pa).invokeLater(
								new Thread(){
									public void run(){
										updateTexture();
									}
								}
						);
						updateTexture();
						break;
					default:
						break;
					}
				}
				return false;
			}
		});

		//setDisplayCloseButton(true);
	}
	public boolean isAutoUpdate() {
		return autoUpdate;
	}
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	private synchronized void updateTexture(){
		// round to full pixels
		int width = Math.round(this.getWidthXYGlobal());
		int height = Math.round(this.getHeightXYGlobal());
		int textureWidth,textureHeight;
		if(width>this.sizeLimitX){
			double ratio = ((double)width)/((double)height);
			textureWidth = sizeLimitX;
			textureHeight = (int)Math.round((double)sizeLimitX*(double)ratio);
		}else{
			textureWidth = width;
			textureHeight = height;
		}
		// resize to full pixels
		setSizeXYGlobal(width, height);
		PImage img= null;
		try {
			// use double image size as texture (better while scaling up) ?
			img = new PImage(PDFRenderer.loadImage(pdf, textureWidth, textureHeight, this.pageNumber));
			setTexture(img);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setDisplayCloseButton(boolean dispClose){
		if (dispClose){
			PImage closeTexture=null;
			try {
				//System.out.println(mtApplication.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +"closeButton64.png"));
				closeTexture = mtApplication.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
				"close.png");
			} catch (Exception e) {
				e.printStackTrace();
			}

			MTImageButton keybCloseSvg = new MTImageButton(mtApplication, closeTexture);
			keybCloseSvg.addActionListener(new CloseActionListener(this) );
			keybCloseSvg.setName("closeButton");
			keybCloseSvg.setNoStroke(true);
			this.addChild(keybCloseSvg);
			keybCloseSvg.setSizeXYGlobal(100, 100);
			//keybCloseSvg.setSizeXYRelativeToParent(0.15f, 0.15f);
			keybCloseSvg.setPositionRelativeToParent(new Vector3D(getWidthXYGlobal(), -0.08f));
		}else{
			//Remove svg button and destroy child display lists
			MTComponent[] childs = this.getChildren();
			for (int i = 0; i < childs.length; i++) {
				MTComponent component = childs[i];
				if (component.getName().equals("closeButton")) {
					MTSvgButton svgButton = (MTSvgButton) component;
					svgButton.destroy();
				}
			}
		}
	}

	private class CloseActionListener implements ActionListener{
		/** The comps. */

		/** The reference poly for resizing the button. */
		private MTPolygon referencePoly;

		/**
		 * Instantiates a new close action listener.
		 * 
		 * @param comps the comps
		 */
		public CloseActionListener(MTPolygon comps) {
			super();
			referencePoly = comps;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			switch (arg0.getID()) {
			case TapEvent.BUTTON_CLICKED:
				//Get the first polygon type out of the array
				closePanel(referencePoly);
				break;
			default:
				break;
			}//switch aeID
		}

		/**
		 * Resize.
		 * 
		 * @param referenceComp the reference comp
		 * @param compToResize the comp to resize
		 * @param width the width
		 * @param height the height
		 */

		/**
		 * Gets the ref comp center local.
		 * 
		 * @param shape the shape
		 * 
		 * @return the ref comp center local
		 */

	}//Class closebutton actionlistener

	public void closePanel(final MTPolygon comps){
		referencePoly = comps;
		float width = referencePoly.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

		Animation closeAnim = new Animation("comp Fade", new MultiPurposeInterpolator(width, 1, 1200, 0.5f, 0.8f, 1), referencePoly);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					resize(referencePoly, referencePoly, currentVal, currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					destroy();
					//System.out.println("destroyed: " + getName());
					break;	
				default:
					destroy();
					break;
				}//switch
			}//processanimation
		});//new IAnimationListener
		closeAnim.start(); 
	}

	protected void resize(MTPolygon referenceComp, MTComponent compToResize, float width, float height){ 
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
}
