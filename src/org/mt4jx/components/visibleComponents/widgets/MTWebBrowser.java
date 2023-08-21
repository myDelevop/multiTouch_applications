package org.mt4jx.components.visibleComponents.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import com.badlogic.gdx.awesomium.Awesomium;
import com.badlogic.gdx.awesomium.JSArguments;
import com.badlogic.gdx.awesomium.WebView;
import com.badlogic.gdx.awesomium.WebViewListener;

public class MTWebBrowser extends MTRoundRectangle {
	
	private MTWebView mtWebView;
	private MTKeyboard keyboard;
	private MTTextField navbar;
	private BrowserInputKeyboardListener browserInput;
	private NavBarInputKeyboardListener navInput;
	private MTComponent clippedChildContainer;
	private String url = "";
	private boolean useKeyboard = true;
	private MTPolygon referencePoly;

	public MTWebBrowser(PApplet pApplet, int width, int height,String url) {
//		super(pApplet, 0, 0, 0, width, height);
		super(pApplet, 0,0,0, width, height, 25,25);
		this.url = url;
		clippedChildContainer = new MTComponent(pApplet);
//		clippedChildContainer.setChildClip(new Clip(pApplet, 0,0, width, height));
		MTRoundRectangle clipShape = new MTRoundRectangle(pApplet, 0,0,0, width, height, 25,25);
		clipShape.setNoStroke(true);
		clippedChildContainer.setChildClip(new Clip(clipShape));
		this.addChild(clippedChildContainer);
		
		
		int borderHorizontal = 25;
		int borderTop = 100;
		this.mtWebView = new MTWebView(pApplet, width-2*borderHorizontal, height - borderTop,url);
		clippedChildContainer.addChild(mtWebView);
		mtWebView.translate(new Vector3D(borderHorizontal, borderTop - borderHorizontal));
		
		float hOffset = 55;
		float vOffset = 22.5f;
		
		MTImageButton left = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "arrow_left_32x32.png"));
//		left.scale(2, 2, 1, left.getCenterPointLocal(), TransformSpace.LOCAL);
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getID() == TapEvent.TAPPED){
					mtWebView.getWebView().goToHistoryOffset(-1);
				}
			}
		});
		clippedChildContainer.addChild(left);
		left.setNoStroke(true);
		left.translate(new Vector3D(hOffset * 1, vOffset, 0));
		
		MTImageButton right = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "arrow_right_32x32.png"));
//		right.scale(2, 2, 1, right.getCenterPointLocal(), TransformSpace.LOCAL);
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getID() == TapEvent.TAPPED){
					mtWebView.getWebView().goToHistoryOffset(1);
				}
			}
		});
		clippedChildContainer.addChild(right);
		right.setNoStroke(true);
		right.translate(new Vector3D(hOffset * 2, vOffset, 0));
		
		MTImageButton reload = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "loop_32x32.png"));
//		reload.scale(2, 2, 1, reload.getCenterPointLocal(), TransformSpace.LOCAL);
		reload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getID() == TapEvent.TAPPED){
					mtWebView.getWebView().reload();
				}
			}
		});
		clippedChildContainer.addChild(reload);
		reload.setNoStroke(true);
		reload.translate(new Vector3D(hOffset * 3, vOffset, 0));
		
		
		MTImageButton stop = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "minus_alt_32x32.png"));
//		stop.scale(2, 2, 1, stop.getCenterPointLocal(), TransformSpace.LOCAL);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getID() == TapEvent.TAPPED){
					mtWebView.getWebView().stop();
				}
			}
		});
		clippedChildContainer.addChild(stop);
		stop.setNoStroke(true);
		stop.translate(new Vector3D(hOffset * 4, vOffset, 0));
		
		MTImageButton home = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "home_32x32.png"));
//		home.scale(2, 2, 1, home.getCenterPointLocal(), TransformSpace.LOCAL);
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getID() == TapEvent.TAPPED){
					mtWebView.loadURL("http://www.google.com", "", "", "");
				}
			}
		});
		clippedChildContainer.addChild(home);
		home.setNoStroke(true);
		home.translate(new Vector3D(hOffset * 5, vOffset, 0));
		
		MTImageButton close = new MTImageButton(pApplet, pApplet.loadImage(MT4jSettings.DEFAULT_IMAGES_PATH + "browser" + MTApplication.separator + "x_alt_32x32.png"));
//		close(2, 2, 1, stop.getCenterPointLocal(), TransformSpace.LOCAL);
		close.addActionListener(new CloseActionListener(new MTComponent[]{this}) );
		clippedChildContainer.addChild(close);
		close.setNoStroke(true);
		close.translate(new Vector3D(hOffset * 13.5f, vOffset, 0));
		close.scale(2f, 2f, 1f, close.getCenterPointGlobal());
		
		
		//Add a keyboard
		keyboard = new BKeyboard(pApplet);
		keyboard.setFillColor(new MTColor(80,80,80,130));
		keyboard.setStrokeColor(MTColor.BLACK);
		this.addChild(keyboard);
//		keyboard.translate(new Vector3D(50,height-height/4f));
		keyboard.setPositionRelativeToParent(getCenterPointLocal());
		keyboard.setVisible(false);
		browserInput = new BrowserInputKeyboardListener();
		keyboard.addTextInputListener(browserInput);

		
		//add we weblistener //INFO we can apparantly only set 1 listener overall
		getWebView().setListener(new WebViewListener() {
			@Override
			public void onWebViewCrashed() {}
			@Override
			public void onRequestMove(int x, int y) {}
			@Override
			public void onRequestDownload(String url) {			}
			@Override
			public void onReceiveTitle(String title, String frameName) {
			}
			@Override
			public void onPluginCrashed(String pluginName) {
			}
			@Override
			public void onOpenExternalLink(String url, String source) {
				getWebView().loadURL(url, "", "", "");
			}
			@Override
			public void onGetPageContents(String url, String contents) {
			}
			@Override
			public void onFinishLoading() {
			}
			@Override
			public void onDOMReady() {
			}
			@Override
			public void onChangeTooltip(String tooltip) {
			}
			@Override
			public void onChangeTargetURL(String url) {
			}
			@Override
			public void onChangeKeyboardFocus(boolean isFocused) {
				if (isFocused && useKeyboard){
					keyboard.setVisible(true);
					keyboard.removeTextInputListener(navInput);
					keyboard.addTextInputListener(browserInput);
				}
			}
			@Override
			public void onChangeCursor(int cursor) {
			}
			@Override
			public void onCallback(String objectName, String callbackName,	JSArguments args) {
			}
			@Override
			public void onBeginNavigation(String url, String frameName) {
				navbar.setText(url);
			}
			@Override
			public void onBeginLoading(String url, String frameName, int statusCode, String mimeType) {
			}
		});
		
		this.navInput = new NavBarInputKeyboardListener();
		
		navbar = new MTTextField(pApplet, 0, 0, 250, 25, FontManager.getInstance().getDefaultFont(pApplet));
		navbar.unregisterAllInputProcessors();
		navbar.removeAllGestureEventListeners();
		navbar.setText("http://www.google.com");
		navbar.translate(new Vector3D(hOffset*6, 25));
		clippedChildContainer.addChild(navbar);
		
		navbar.registerInputProcessor(new TapProcessor(pApplet));
		navbar.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped() && useKeyboard){
					keyboard.setVisible(true);
					navbar.setEnableCaret(true);
					keyboard.removeTextInputListener(browserInput);
					keyboard.addTextInputListener(navInput);
					//getWebView().unfocus();
				}
				return false;
			}
		});
		
		
		this.setDepthBufferDisabled(true); //To prevent z-fighting
		
	}
	
	
	private int getModifiers(String unicode) {
		int modifiers = 0;
		if(unicode.equals("shift")){
			modifiers |= Awesomium.AWE_MOD_SHIFT_KEY;
		}
		return modifiers;
	}
	
	public WebView getWebView(){
		return mtWebView.getWebView();
	}
	
	private class NavBarInputKeyboardListener implements ITextInputListener{
		@Override
		public void setText(String text) {
			navbar.setText(text);	
		}
		
		@Override
		public void removeLastCharacter() {
			navbar.removeLastCharacter();
		}

		@Override
		public void clear() {
			navbar.clear();
		}
		
		@Override
		public void appendText(String text) {
			navbar.appendText(text);	
		}
		
		@Override
		public void appendCharByUnicode(String unicode) {
			if (unicode.equals("\n")){
				navbar.setEnableCaret(false);
				getWebView().loadURL(navbar.getText(), "", "", "");
				keyboard.setVisible(false);
			}else{
				navbar.appendCharByUnicode(unicode);
			}
		}
	}
	
	private class BrowserInputKeyboardListener implements ITextInputListener{
		@Override
		public void setText(String text) {	}
		
		@Override
		public void removeLastCharacter() {
			if (isVisible()){
				getWebView().injectKeyDown(Awesomium.AWE_AK_BACK, getModifiers(""), false);
				getWebView().injectKeyUp(Awesomium.AWE_AK_BACK, getModifiers(""), false);
			}else{
				getWebView().goToHistoryOffset(-1);
			}
		}

		@Override
		public void clear() {	}
		
		@Override
		public void appendText(String text) {	}
		
		@Override
		public void appendCharByUnicode(String unicode) {
			//System.out.println(unicode);
			if(unicode.equals("shift")){
				getWebView().injectKeyDown(Awesomium.AWE_AK_SHIFT, getModifiers(unicode), false);
				getWebView().injectKeyUp(Awesomium.AWE_AK_SHIFT, getModifiers(unicode), false);
			}
			else if(unicode.equals("\n")){
				getWebView().injectKeyDown(Awesomium.AWE_AK_RETURN, getModifiers(unicode), false);
				getWebView().injectKeyUp(Awesomium.AWE_AK_RETURN, getModifiers(unicode), false);
				keyboard.setVisible(false);
			}
			else{
				char chr = unicode.charAt(0);
				//boolean upper = Character.isUpperCase(chr);
				getWebView().injectKeyTyped(chr);
			}
		}
	}
	
	private class BKeyboard extends MTKeyboard{
		public BKeyboard(PApplet pApplet) {
			super(pApplet);
		}
		
		@Override
		protected void onCloseButtonClicked() {
			this.setVisible(false);
		}
	}
	
	
	@Override
	public void setSizeLocal(float width, float height) {
		super.setSizeLocal(width, height);
		if (MT4jSettings.getInstance().isOpenGlMode() && clippedChildContainer.getChildClip() != null && clippedChildContainer.getChildClip().getClipShape() instanceof MTRoundRectangle){ 
			MTRoundRectangle clipRect = (MTRoundRectangle)clippedChildContainer.getChildClip().getClipShape();
			//clipRect.setVertices(Vertex.getDeepVertexArrayCopy(this.getVerticesLocal()));
			clipRect.setVertices(this.getVerticesLocal());
		}
	}


	public MTWebView getMtWebView() {
		return mtWebView;
	}


	public void setMtWebView(MTWebView mtWebView) {
		this.mtWebView = mtWebView;
	}


	public boolean isUseKeyboard() {
		return useKeyboard;
	}


	public void setUseKeyboard(boolean useKeyboard) {
		this.useKeyboard = useKeyboard;
	}
	
//	@Override
//	public void setWidthLocal(float width) {
//		super.setWidthLocal(width);
//		if (MT4jSettings.getInstance().isOpenGlMode() && clippedChildContainer.getChildClip() != null && clippedChildContainer.getChildClip().getClipShape() instanceof MTRectangle){ 
//			MTRectangle clipRect = (MTRectangle)clippedChildContainer.getChildClip().getClipShape();
//			//clipRect.setVertices(Vertex.getDeepVertexArrayCopy(this.getVerticesLocal()));
//			clipRect.setVertices(this.getVerticesLocal());
//		}
//	}
//	
//	@Override
//	public void setHeightLocal(float height) {
//		super.setHeightLocal(height);
//		if (MT4jSettings.getInstance().isOpenGlMode() && clippedChildContainer.getChildClip() != null && clippedChildContainer.getChildClip().getClipShape() instanceof MTRectangle){ 
//			MTRectangle clipRect = (MTRectangle)clippedChildContainer.getChildClip().getClipShape();
//			//clipRect.setVertices(Vertex.getDeepVertexArrayCopy(this.getVerticesLocal()));
//			clipRect.setVertices(this.getVerticesLocal());
//		}
//	}
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
	
	public void closePanel(final MTComponent[] comps){
		for (int i = 0; i < comps.length; i++) { //TODO this is stupid.. redo this whole thing
			MTComponent comp = comps[i];
			if (comp instanceof MTPolygon) {
				MTPolygon poly = (MTPolygon) comp;
				referencePoly = poly;
			}
		}
		float width = referencePoly.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);

		Animation closeAnim = new Animation("comp Fade", new MultiPurposeInterpolator(width, 1, 1200, 0.5f, 0.8f, 1), referencePoly);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					resize(referencePoly, comps[0], currentVal, currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					comps[0].setVisible(false);
					for (int i = comps.length-1; i >0 ; i--) {
						MTComponent currentComp =  comps[i];
						//Call destroy which fires a destroy state change event
						currentComp.destroy();

						//System.out.println("destroyed: " + currentComp.getName());
					}
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
	
	private class CloseActionListener implements ActionListener{
		/** The comps. */
		public MTComponent[] comps;

		/** The reference poly for resizing the button. */
		private MTPolygon referencePoly;

		/**
		 * Instantiates a new close action listener.
		 * 
		 * @param comps the comps
		 */
		public CloseActionListener(MTComponent[] comps) {
			super();
			this.comps = comps;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent arg0) {
			switch (arg0.getID()) {
			case TapEvent.BUTTON_CLICKED:
				//Get the first polygon type out of the array
				closePanel(comps);
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

}
