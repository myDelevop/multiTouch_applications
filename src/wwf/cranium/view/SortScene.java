package wwf.cranium.view;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
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
 * Questa classe inizializza la componente {@link #root} di elementi grafici 
 * che consentono la visualizzazione della SortScene.
 */
public class SortScene {
	
    /** variabile destinata a contenere tutti gli elementi grafici 
     * della scena in questione.
     */
	private MTComponent root;
	
	/** Font bold 24 white. */
	private IFont bold24White;
	
	/** Font seguisb 32 white. */
	private IFont seguisb32White;


	/**
	 * Costruttore parametrico che provvede all'inizializzazione della 
	 * componente {@link #root}.
	 *
	 * @param craniumScene gestore delle schermate
	 * @param pa the pa
	 */
	public SortScene(final CraniumSceneManager craniumScene,
	        final MTApplication pa) {
		root = new MTComponent(pa);
		this.bold24White = Fonts.getInstance(pa).getSegoe50White();
		this.seguisb32White = Fonts.getInstance(pa).getSeguisb32White();

		MTBackgroundImage background = new MTBackgroundImage(pa, 
		        pa.loadImage(FilesPath.ROOT + "sea2.jpg"), false);
	    root.addChild(background);

		MTRectangle titleBar = new MTRectangle(pa, 0, 0, pa.width, 143);
		titleBar.setNoStroke(true);
		titleBar.setNoFill(true);
		titleBar.removeAllGestureEventListeners();
		titleBar.unregisterAllInputProcessors();

		MTTextArea text = new MTTextArea(pa, bold24White);
		text.setNoStroke(true);
		text.setNoFill(true);
		text.setText("COME VUOI ORDINARE LE DOMANDE?");
		text.removeAllGestureEventListeners();
		text.unregisterAllInputProcessors();
		text.setPositionGlobal(new Vector3D(pa.width / 2f, 62));
		titleBar.addChild(text);
		
		MTRectangle centralBar =
		        new MTRectangle(pa, 0, 143, pa.width, pa.height - 143);
		centralBar.setNoStroke(true);
		centralBar.setNoFill(true);
		centralBar.removeAllGestureEventListeners();
		centralBar.unregisterAllInputProcessors();

		MTRectangle randomButton = new MTRectangle(
		        pa, pa.width / 2 - 50, pa.height - 143 - 280);
		randomButton.setPositionGlobal(new Vector3D(
		        pa.width / 4, pa.height / 2 + (102 / 2)));
		randomButton.setNoStroke(true);
		randomButton.setFillColor(new MTColor(0f, 120f, 215f));
		randomButton.removeAllGestureEventListeners();
		randomButton.unregisterAllInputProcessors();
	   
		randomButton.registerInputProcessor(new TapProcessor(pa));
		randomButton.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
					craniumScene.setRandom(true);
					craniumScene.questionLoader();
				}
				return false;
			}
		});
	    
		MTTextArea textRandom = new MTTextArea(pa, seguisb32White);
		textRandom.setNoStroke(true);
		textRandom.setNoFill(true);
		textRandom.setText("Casuale");
		textRandom.removeAllGestureEventListeners();
		textRandom.unregisterAllInputProcessors();
		textRandom.registerInputProcessor(new TapProcessor(pa));
		textRandom.addGestureListener(TapProcessor.class, 
		        new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
					craniumScene.setRandom(true);
					craniumScene.questionLoader();
				}
				return false;
			}
		});
		textRandom.setPositionGlobal(new Vector3D(
		        randomButton.getWidthXY(TransformSpace.GLOBAL) / 2f, 
				randomButton.getHeightXY(TransformSpace.GLOBAL) / 2f));
		randomButton.addChild(textRandom);

		MTRectangle customButton = new MTRectangle(pa, 
		        pa.width / 2 - 50, pa.height - 143 - 280);
		customButton.setPositionGlobal(new Vector3D(
		        pa.width * 3 / 4, pa.height / 2 + (102 / 2)));
		customButton.setNoStroke(true);
		customButton.setFillColor(new MTColor(0f, 120f, 215f));
		customButton.removeAllGestureEventListeners();
		customButton.unregisterAllInputProcessors();

		customButton.registerInputProcessor(new TapProcessor(pa));
		customButton.addGestureListener(TapProcessor.class,
		        new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
					craniumScene.setRandom(false);
					craniumScene.questionLoader();
				}
				return false;
			}
		});

		MTTextArea textCustom = new MTTextArea(pa, seguisb32White);
		textCustom.setNoStroke(true);
		textCustom.setNoFill(true);
		textCustom.setText("Predefinita");
		textCustom.removeAllGestureEventListeners();
		textCustom.unregisterAllInputProcessors();
		
		textCustom.registerInputProcessor(new TapProcessor(pa));
		textCustom.addGestureListener(TapProcessor.class,
		        new IGestureEventListener() {	
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
					craniumScene.setRandom(false);
					craniumScene.questionLoader();
				}
				return false;
			}
		});
		
		textCustom.setPositionGlobal(new Vector3D(
		        customButton.getWidthXY(TransformSpace.GLOBAL) / 2f, 
				customButton.getHeightXY(TransformSpace.GLOBAL) / 2f));
		customButton.addChild(textCustom);
		
		centralBar.addChild(randomButton);
		centralBar.addChild(customButton);
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
