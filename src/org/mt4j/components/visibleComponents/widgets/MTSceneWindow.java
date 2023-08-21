/***********************************************************************
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package org.mt4j.components.visibleComponents.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.batik.css.engine.value.css2.SrcManager;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import wwf.mtShell.MTRoundRectangleCustom;

/**
 * The Class MTSceneWindow. Can be used to run and display a scene in a window
 * from within another scene.
 * 
 * @author Christopher Ruff
 */
public class MTSceneWindow 
//extends MTRectangle {
extends MTRoundRectangleCustom {

	/** The scene texture. */
	private MTSceneTexture sceneTexture;

	/** The maximize button image. */
	private static PImage maximizeButtonImage;

	/** The close button image. */
	private static PImage closeButtonImage;

	ThreadTimeCount resetTime;

	MTRoundRectangle mtRoundRectangle;

	//Destroying the sceneTexture -> destroy SceneWindow -> destroy scene
	//Destroy this -> destroy scene -> child sceneTexture.destroy() 


	/**
	 * Instantiates a new mT scene window.
	 *
	 * @param scene the scene
	 * @param borderWidth the border width
	 * @param borderHeight the border height
	 * @param applet the applet
	 * @deprecated constructor will deleted! Please , use the constructor with the PApplet instance as the first parameter.
	 */
	public MTSceneWindow(final Iscene scene, float borderWidth, float borderHeight, MTApplication applet) {
		this(applet, scene, borderWidth, borderHeight);
	}

	/**
	 * Instantiates a new mT scene window.
	 *
	 * @param scene the scene
	 * @param borderWidth the border width
	 * @param borderHeight the border height
	 * @param applet the applet
	 * @param fboWidth the fbo width
	 * @param fboHeight the fbo height
	 * @deprecated constructor will deleted! Please , use the constructor with the PApplet instance as the first parameter.
	 */
	public MTSceneWindow(final Iscene scene, float borderWidth, float borderHeight, final MTApplication applet, int fboWidth, int fboHeight) {
		this(applet, scene, borderWidth, borderHeight, fboWidth, fboHeight);
	}

	/**
	 * Instantiates a new mT scene window.
	 * @param applet the applet
	 * @param scene the scene
	 * @param borderWidth the border width
	 * @param borderHeight the border height
	 */
	public MTSceneWindow(MTApplication applet, final Iscene scene, float borderWidth, float borderHeight) {
		this(applet, scene, borderWidth, borderHeight, Math.round(MT4jSettings.getInstance().getWindowWidth() * 0.6f), Math.round(MT4jSettings.getInstance().getWindowHeight() * 0.6f));
	}

	public void createResetTimerThread(){
		resetTime = new ThreadTimeCount();
		resetTime.start();
		this.addInputListener(new IMTInputEventListener() {
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				//resetTime.getCloseAnim().stop();
				//resetTime.resetTimer();
				return false;
			}
		});
	}

	/**
	 * Instantiates a new mT scene window.
	 * @param applet the applet
	 * @param scene the scene
	 * @param borderWidth the border width
	 * @param borderHeight the border height
	 * @param fboWidth the fbo width
	 * @param fboHeight the fbo height
	 */
	public MTSceneWindow(final MTApplication applet, final Iscene scene, float borderWidth, float borderHeight, int fboWidth, int fboHeight) {
		//		super(0-borderWidth, 0-borderHeight, applet.width+2*borderWidth, applet.height+2*borderHeight, applet);
		super(applet, 0-borderWidth, 0-borderHeight, 0, MT4jSettings.getInstance().getWindowWidth()+2*borderWidth, MT4jSettings.getInstance().getWindowHeight()+2*borderHeight, 30, 30);
		mtRoundRectangle = this;
		this.setStrokeColor(new MTColor(0,0,0));

		sceneTexture = new MTSceneTexture(applet,0, 0, fboWidth, fboHeight, scene); 
		sceneTexture.setStrokeColor(new MTColor(0,0,0));
		this.addChild(sceneTexture);
		addListnerToAllChild(sceneTexture.getScene().getCanvas());

		//Add the scene to the scene list in the Application
		//FIXME add the scene later to the MTApplication because if we add the scene 
		//before any other scene is added it becomes the active scene which we dont want
		if (applet.getSceneCount() == 0){
			applet.invokeLater(new Runnable() {
				public void run() {
					applet.addScene(sceneTexture.getScene());
				}
			});
		}else{
			applet.addScene(sceneTexture.getScene());	
		}


		sceneTexture.addStateChangeListener(StateChange.COMPONENT_DESTROYED, new StateChangeListener() {
			public void stateChanged(StateChangeEvent evt) {
				destroy();
			}
		});


		if (closeButtonImage == null){
			closeButtonImage = applet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
					//			"close_32.png")
					//			"126182-simple-black-square-icon-alphanumeric-circled-x3_cr.png"
					//			"124241-matte-white-square-icon-alphanumeric-circled-x3_cr.png"
					//			"124241-matte-white-square-icon-alphanumeric-circled-x3128.png"
					"closeButton64.png"
			);
		}
		MTImageButton closeButton = new MTImageButton(applet, closeButtonImage);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent r) {
				switch (r.getID()) {
				case TapEvent.TAPPED:
					close();
					System.gc();
					//					destroy();
					break;
				default:
					break;
				}
			}
		});
		this.addChild(closeButton);
		closeButton.setNoStroke(true);
		//		closeButton.setSizeXYRelativeToParent(borderWidth - borderWidth/20, borderWidth - borderWidth/20);
		closeButton.setSizeXYRelativeToParent(borderWidth - borderWidth/30, borderWidth - borderWidth/30);
		//		closeButton.setSizeXYRelativeToParent(borderWidth -0.5f, borderWidth-0.5f);
		closeButton.setPositionRelativeToParent(new Vector3D( (applet.width+ (borderWidth /2f)), borderHeight - 5));

		if (maximizeButtonImage == null){
			maximizeButtonImage = applet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
					//			"window_app_blank_32.png")
					//			"127941-simple-black-square-icon-symbols-shapes-maximize-button_cr.png"
					"maximizeButton64.png"
			);
		}
		MTImageButton maximizeButton = new MTImageButton(applet, maximizeButtonImage);
		maximizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent r) {
				switch (r.getID()) {
				case TapEvent.TAPPED:
					maximize();
					break;
				default:
					break;
				}
			}
		});
		this.addChild(maximizeButton);
		maximizeButton.setNoStroke(true);
		//		maximizeButton.setSizeXYRelativeToParent(borderWidth - borderWidth/10, borderWidth - borderWidth/10);
		maximizeButton.setSizeXYRelativeToParent(borderWidth - borderWidth/30, borderWidth - borderWidth/30);
		//		maximizeButton.setPositionRelativeToParent(new Vector3D( (applet.width+2*borderWidth)-maximizeButton.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), closeButton.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + 40));
		//		maximizeButton.setPositionRelativeToParent(new Vector3D( (applet.width+ (borderWidth /2f)), borderHeight + closeButton.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + 15));
		maximizeButton.setPositionRelativeToParent(new Vector3D( (applet.width+ (borderWidth /2f)), applet.height - closeButton.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f));

	}

	private void addListnerToAllChild(MTComponent component){
		MTComponent[] c = component.getChildren();
		component.addInputListener(new IMTInputEventListener() {	
			@Override
			public boolean processInputEvent(MTInputEvent inEvt) {
				try {
					resetTime.resetTimer();
					((AniAnimation)resetTime.getCloseAnim()).reset(resetTime);
				} catch (NullPointerException e) {
					// TODO: handle exception
				}
				
				return false;
			}
		});
		for (int i = 0 ; i < c.length ; i++){
			final MTComponent child = c[i];
			if (child.getChildCount()>0){
				addListnerToAllChild(child);
			}
		}
	}

	public void close(){
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		//		IAnimation closeAnim = new Animation("Window Fade", new MultiPurposeInterpolator(width, 1, 350, 0.2f, 0.5f, 1), this);
		IAnimation closeAnim = new AniAnimation(width, 1, 350, AniAnimation.SINE_IN, this);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
				//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					setWidthXYRelativeToParent(currentVal);
					rotateZ(getCenterPointRelativeToParent(), -ae.getDelta()*0.3f);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}

	/**
	 * Gets the scene texture.
	 * 
	 * @return the scene texture
	 */
	public MTSceneTexture getSceneTexture(){
		return sceneTexture;
	}

	/**
	 * Maximize.
	 */
	public void maximize(){
		sceneTexture.maximize();
	}

	/**
	 * Restore.
	 */
	public void restore(){
		sceneTexture.restore();
	}

	/* (non-Javadoc)
	 * @see org.mt4j.components.visibleComponents.shapes.MTPolygon#destroyComponent()
	 */
	@Override
	protected void destroyComponent() {
		super.destroyComponent();

		//FIXME gets called twice if we destroy this component (because we listen to sceneTexture destroy())
		//-> but not tragic

		//Destroy the scene used for the window because we dont destroy it 
		//in MTSceneTexture
		sceneTexture.getScene().destroy(); 
	}
	private class ThreadTimeCount extends Thread{

		IAnimation closeAnim;
		int sec = 0;
		int startSec = 15; //modify this value to set the number of seconds to wait before to close the application
		int threadDuration = 10;
		Integer i = 0;

		public ThreadTimeCount() {
			super();
			
		}

		@Override
		public void run() {
			/*try {
				for ( i = startSec; i > -threadDuration; i--) {
					System.out.println("Secondo" + i);
					Thread.sleep(1000);
					if(i==1){
						float width = mtRoundRectangle.getWidthXY(TransformSpace.GLOBAL);
						//		IAnimation closeAnim = new Animation("Window Fade", new MultiPurposeInterpolator(width, 1, 350, 0.2f, 0.5f, 1), this);
						closeAnim = new AniAnimation(width, 1, 10000, AniAnimation.SINE_IN, mtRoundRectangle);
						closeAnim.addAnimationListener(new IAnimationListener(){
							public void processAnimationEvent(AnimationEvent ae) {
								//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
								switch (ae.getId()) {
								case AnimationEvent.ANIMATION_STARTED:
								case AnimationEvent.ANIMATION_UPDATED:
									float currentVal = ae.getAnimation().getValue();
									mtRoundRectangle.setWidthXYGlobal(currentVal);
									//rotateZ(getCenterPointRelativeToParent(),0f);
									break;
								case AnimationEvent.ANIMATION_ENDED:
									setVisible(false);
									//TO DO add destroy method
									mtRoundRectangle.destroy();;
									break;	
								default:
									break;
								}//switch
							}//processanimation

						});
						closeAnim.start();
						//resetTimer();
					}
				}

			}
			catch (InterruptedException e) {
				System.out.println("Thread figlio interrotto");
			}*/
		}

		//closeAnim.start();


		public int getSec() {
			return sec;
		}

		public void setSec(int sec) {
			this.sec = sec;
		}

		public int getStartSec() {
			return startSec;
		}

		public void setStartSec(int startSec) {
			this.startSec = startSec;
		}

		public void resetTimer(){
			if (i==0){
				//System.out.println("Riparte thread");
				((AniAnimation)resetTime.getCloseAnim()).reset(resetTime);
				//this.resume();
				//System.out.println("La i ha valore " + i);
			}
			i = startSec;

		}

		public IAnimation getCloseAnim() {
			return closeAnim;
		}

		public void setCloseAnim(IAnimation closeAnim) {
			this.closeAnim = closeAnim;
		}
	}


}
