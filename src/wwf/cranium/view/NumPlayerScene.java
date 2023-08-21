package wwf.cranium.view;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.cranium.util.FilesPath;
import wwf.todelete.Fonts;


/**
 * Questa classe inizializza la componente {@link #root} di elementi grafici che
 *  consentono la visualizzazione della NumPlayerScene.
 */
public class NumPlayerScene {
	
	/** variabile destinata a contenere tutti gli elementi grafici della scena 
	 * in questione. */
	private MTComponent root;
	
	/** Font bold 24 white. */
	private IFont bold24White;
	
	/** Font seguisb 36 white. */
	private IFont seguisb36White;

	/**
	 * Costruttore parametrico che provvede all'inizializzazione della 
	 * componente {@link #root}.
	 *
	 * @param craniumScene gestore delle schermate
	 * @param pa the pa
	 */
	public NumPlayerScene(final CraniumSceneManager craniumScene,
	        final MTApplication pa) {
		root = new MTComponent(pa);
		this.bold24White = Fonts.getInstance(pa).getSegoe50White();
		this.seguisb36White = Fonts.getInstance(pa).getSeguisb36White();
		
		MTBackgroundImage background = new MTBackgroundImage(pa, 
		        pa.loadImage(FilesPath.ROOT + "sea2.jpg"), false);
	    root.addChild(background);

		MTRectangle titleBar = new MTRectangle(pa, 0, 0, pa.width, 143);
		titleBar.setTexture(pa.loadImage(FilesPath.ROOT + "titleBar.jpg"));
		titleBar.setNoStroke(true);
		titleBar.setNoFill(true);

		titleBar.removeAllGestureEventListeners();
		titleBar.unregisterAllInputProcessors();

	    MTTextArea text = new MTTextArea(pa, bold24White);
	    text.setNoStroke(true);
		text.setNoFill(true);
	    text.setText("SELEZIONA IL NUMERO DI GIOCATORI");
	    text.removeAllGestureEventListeners();
	    text.unregisterAllInputProcessors();
		text.setPositionGlobal(new Vector3D(pa.width / 2f, 62));
	    titleBar.addChild(text);

		MTRectangle centralBar = new MTRectangle(pa, 0, 143,
		        pa.width, pa.height - 143);
		centralBar.setNoStroke(true);
		centralBar.setNoFill(true);
		centralBar.removeAllGestureEventListeners();
		centralBar.unregisterAllInputProcessors();
		
		/**
		 * Listener for buttons
		 * */
		class Listener implements IGestureEventListener {
			
		    /** number of players */
			private Integer numPlayers;

	         /** costruttore.
	          * @param numPlayers numero di giocatori */
			Listener(final Integer numPlayers) {
				this.numPlayers = numPlayers;
			}
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
					craniumScene.setNumPlayers(numPlayers);
					craniumScene.buildSelectLevelScene();
				}

				return false;
			}

		}

		MTRectangle numPlayer1 = new MTRectangle(pa, 
		        20, 143 + 20, 0, (pa.width - 80) / 3, 
		        (pa.height - (60 + 163)) / 2);
		numPlayer1.setFillColor(new MTColor(0f, 120f, 215f));
		numPlayer1.setNoStroke(true);

		MTRectangle numPlayer2 = new MTRectangle(pa, 
		        (pa.width - 80) / 3 + 40, 143 + 20, 0, 
		        (pa.width - 80) / 3, (pa.height - (60 + 163)) / 2);
		numPlayer2.setFillColor(new MTColor(0f, 120f, 215f)); // a=212
		numPlayer2.setNoStroke(true);
		
		MTRectangle numPlayer3 = new MTRectangle(pa, 
		        ((pa.width - 80) / 3) * 2 + 60, 
		        143 + 20, 0, (pa.width - 80) / 3,
		        (pa.height - (60 + 163)) / 2);
		numPlayer3.setFillColor(new MTColor(0f, 120f, 215f));
		numPlayer3.setNoStroke(true);
		
		MTRectangle numPlayer4 = new MTRectangle(pa, 
		        20, ((pa.height - (60 + 163)) / 2) + 143 + 40,
		        0, (pa.width - 80) / 3, (pa.height - (60 + 163)) / 2);
		numPlayer4.setFillColor(new MTColor(0f, 120f, 215f));
		numPlayer4.setNoStroke(true);
		
		MTRectangle numPlayer5 = new MTRectangle(pa,
		        (pa.width - 80) / 3 + 40, ((pa.height - (60 + 163)) / 2) 
		        + 143 + 40, 
		        0, (pa.width - 80) / 3, (pa.height - (60 + 163)) / 2);
		numPlayer5.setFillColor(new MTColor(0f, 120f, 215f));
		numPlayer5.setNoStroke(true);
		
		MTRectangle numPlayer6 = new MTRectangle(pa, 
		        ((pa.width - 80) / 3) * 2 + 60, ((pa.height 
		                - (60 + 163)) / 2) + 143 + 40, 0, 
		        (pa.width - 80) / 3, (pa.height - (60 + 163)) / 2);
		numPlayer6.setFillColor(new MTColor(0f, 120f, 215f));
		numPlayer6.setNoStroke(true);

	    MTTextArea text1 = new MTTextArea(pa, seguisb36White);
	    text1.setNoStroke(true);
	    text1.setNoFill(true);
	    text1.setText("1");	    
	    numPlayer1.addChild(text1);
	    text1.setPositionRelativeToParent(
	            new Vector3D(numPlayer1.getCenterPointLocal().x, 
	                    numPlayer1.getCenterPointLocal().y));

	    MTTextArea text2 = new MTTextArea(pa, seguisb36White);
	    text2.setNoStroke(true);
	    text2.setNoFill(true);
	    text2.setText("2");
	    numPlayer2.addChild(text2);
	    text2.setPositionRelativeToParent(
	            new Vector3D(numPlayer2.getCenterPointLocal().x, 
	                    numPlayer2.getCenterPointLocal().y));

	    MTTextArea text3 = new MTTextArea(pa, seguisb36White);
	    text3.setNoStroke(true);
	    text3.setNoFill(true);
	    text3.setText("3");
	    numPlayer3.addChild(text3);
	    text3.setPositionRelativeToParent(
	            new Vector3D(numPlayer3.getCenterPointLocal().x,
	                    numPlayer3.getCenterPointLocal().y));

	    MTTextArea text4 = new MTTextArea(pa, seguisb36White);
	    text4.setNoStroke(true);
	    text4.setNoFill(true);
	    text4.setText("4");
	    numPlayer4.addChild(text4);
	    text4.setPositionRelativeToParent(
	            new Vector3D(numPlayer4.getCenterPointLocal().x,
	                    numPlayer4.getCenterPointLocal().y));

	    MTTextArea text5 = new MTTextArea(pa, seguisb36White);
	    text5.setNoStroke(true);
	    text5.setNoFill(true);
	    text5.setText("5");
	    numPlayer5.addChild(text5);
	    text5.setPositionRelativeToParent(
	            new Vector3D(numPlayer5.getCenterPointLocal().x,
	                    numPlayer5.getCenterPointLocal().y));

	    MTTextArea text6 = new MTTextArea(pa, seguisb36White);
	    text6.setNoStroke(true);
	    text6.setNoFill(true);
	    text6.setText("6");
	    numPlayer6.addChild(text6);
	    text6.setPositionRelativeToParent(
	            new Vector3D(numPlayer6.getCenterPointLocal().x, 
	                    numPlayer6.getCenterPointLocal().y));

		numPlayer1.removeAllGestureEventListeners();
		numPlayer1.unregisterAllInputProcessors();
		numPlayer2.removeAllGestureEventListeners();
		numPlayer2.unregisterAllInputProcessors();
		numPlayer3.removeAllGestureEventListeners();
		numPlayer4.unregisterAllInputProcessors();
		numPlayer4.removeAllGestureEventListeners();
		numPlayer5.unregisterAllInputProcessors();
		numPlayer5.removeAllGestureEventListeners();
		numPlayer6.unregisterAllInputProcessors();
		numPlayer6.removeAllGestureEventListeners();

	    text1.removeAllGestureEventListeners();
	    text1.unregisterAllInputProcessors();
	    text2.removeAllGestureEventListeners();
	    text2.unregisterAllInputProcessors();
	    text3.removeAllGestureEventListeners();
	    text3.unregisterAllInputProcessors();
	    text4.removeAllGestureEventListeners();
	    text4.unregisterAllInputProcessors();
	    text5.removeAllGestureEventListeners();
	    text5.unregisterAllInputProcessors();
	    text6.removeAllGestureEventListeners();
	    text6.unregisterAllInputProcessors();
	    
	    numPlayer1.registerInputProcessor(new TapProcessor(pa));
	    numPlayer1.addGestureListener(TapProcessor.class, new Listener(1));
	    numPlayer2.registerInputProcessor(new TapProcessor(pa));
	    numPlayer2.addGestureListener(TapProcessor.class, new Listener(2));
	    numPlayer3.registerInputProcessor(new TapProcessor(pa));
	    numPlayer3.addGestureListener(TapProcessor.class, new Listener(3));
	    numPlayer4.registerInputProcessor(new TapProcessor(pa));
	    numPlayer4.addGestureListener(TapProcessor.class, new Listener(4));
	    numPlayer5.registerInputProcessor(new TapProcessor(pa));
	    numPlayer5.addGestureListener(TapProcessor.class, new Listener(5));
	    numPlayer6.registerInputProcessor(new TapProcessor(pa));
	    numPlayer6.addGestureListener(TapProcessor.class, new Listener(6));

	    text1.registerInputProcessor(new TapProcessor(pa));
	    text1.addGestureListener(TapProcessor.class, new Listener(1));
	    text2.registerInputProcessor(new TapProcessor(pa));
	    text2.addGestureListener(TapProcessor.class, new Listener(2));
	    text3.registerInputProcessor(new TapProcessor(pa));
	    text3.addGestureListener(TapProcessor.class, new Listener(3));
	    text4.registerInputProcessor(new TapProcessor(pa));
	    text4.addGestureListener(TapProcessor.class, new Listener(4));
	    text5.registerInputProcessor(new TapProcessor(pa));
	    text5.addGestureListener(TapProcessor.class, new Listener(5));
	    text6.registerInputProcessor(new TapProcessor(pa));
	    text6.addGestureListener(TapProcessor.class, new Listener(6));
		
		centralBar.addChild(numPlayer1);
		centralBar.addChild(numPlayer2);
		centralBar.addChild(numPlayer3);
		centralBar.addChild(numPlayer4);
		centralBar.addChild(numPlayer5);
		centralBar.addChild(numPlayer6);
	    
	    root.addChild(titleBar);
	    root.addChild(centralBar);
	}

	/**
	 * Restituisce la radice degli elementi grafici che costituiscono 
	 * la scena in questione.
	 *
	 * @return component root
	 */
	public final MTComponent getRootComponent() {
		return root;
	}


}
