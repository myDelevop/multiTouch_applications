package wwf.cranium.view;

import java.util.List;

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

import wwf.cranium.data.model.DifficoltaDomande;
import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.cranium.sceneManager.currentGame.CurrentGameDifficolta;
import wwf.cranium.util.FilesPath;
import wwf.todelete.Fonts;

/**
 * Questa classe inizializza la componente {@link #root} di elementi
 * grafici che consentono la visualizzazione della LevelScene.
 */
public class LevelScene {
	
	/** variabile destinata a contenere tutti gli elementi grafici 
	 * della scena in questione. */
	private MTComponent root;
	
	/** Font bold 24 white. */
	private IFont bold24White;
	
	/** Font seguisb 32 white. */
	private IFont seguisb32White;
	
	/** Font difficolta. */
	private CurrentGameDifficolta difficolta;
	

	/**
	 * Costruttore parametrico che provvede all'inizializzazione 
	 * della componente {@link #root}.
	 *
	 * @param craniumScene gestore delle schermate
	 * @param pa the pa
	 */
	public LevelScene(final CraniumSceneManager craniumScene, 
	        final MTApplication pa) {

		root = new MTComponent(pa);
		this.bold24White = Fonts.getInstance(pa).getSegoe50White();
		this.seguisb32White = Fonts.getInstance(pa).getSeguisb32White();
		this.difficolta = new CurrentGameDifficolta(craniumScene.isItalian(),
		        craniumScene.isUpdateDB());
		
		MTBackgroundImage background = new MTBackgroundImage(pa, 
		        pa.loadImage(FilesPath.ROOT + "sea2.jpg"), false);
	    root.addChild(background);

		MTRectangle titleBar = new MTRectangle(pa, 0, 0, pa.width, 143);
		titleBar.setNoFill(true);
		titleBar.setNoStroke(true);
		titleBar.removeAllGestureEventListeners();
		titleBar.unregisterAllInputProcessors();

		MTTextArea text = new MTTextArea(pa, bold24White);
		text.setNoStroke(true);
		text.setNoFill(true);
		text.setText("SELEZIONA LIVELLO DI DIFFICOLTA'");
		text.removeAllGestureEventListeners();
		text.unregisterAllInputProcessors();
		text.setPositionGlobal(new Vector3D(pa.width / 2f, 62));

		titleBar.addChild(text);
		
		MTRectangle centralBar = new MTRectangle(pa, 
		        0, 143, pa.width, pa.height - 143);
		centralBar.setNoFill(true);
		centralBar.setNoStroke(true);
		centralBar.removeAllGestureEventListeners();
		centralBar.unregisterAllInputProcessors();

		MTRectangle container = new MTRectangle(pa,
				pa.width / 2f - (pa.width / 2f / 2f), 
				143f - 15f,
				pa.width / 2f,
				(((pa.height - (80f + 143f)) / 3f) * 3f) + 40f);
		
		container.removeAllGestureEventListeners();
		container.unregisterAllInputProcessors();

		container.setNoFill(true);
		container.setNoStroke(true);

		MTRectangle component;
		List<DifficoltaDomande> allDifficolta = difficolta.getAllDifficolta();

		float y = 143f - 15f;
		for (final DifficoltaDomande diff : allDifficolta) {
			component  = new MTRectangle(pa,
					pa.width / 2f - (pa.width / 2f / 2f),				
					y,
					pa.width / 2f,
					(container.getHeightXY(TransformSpace.GLOBAL) 
					        - (20f * (allDifficolta.size() - 1))) 
					/ allDifficolta.size()
					
					
					);
			
			y += (container.getHeightXY(TransformSpace.GLOBAL) 
			        - (20f * (allDifficolta.size() - 1))) 
					/ allDifficolta.size();
			y += 20f;
  
			component.setFillColor(new MTColor(0f, 120f, 215f, 212f));
			component.setNoStroke(true);

			
		    MTTextArea levelText = new MTTextArea(pa, seguisb32White);
		    levelText.setNoStroke(true);
		    levelText.setNoFill(true);
		    levelText.setText(diff.getLivelloDifficolta());
		    component.addChild(levelText);

		    levelText.setPositionRelativeToParent(new Vector3D(
		            component.getCenterPointLocal().x, 
		            component.getCenterPointLocal().y));

		    component.removeAllGestureEventListeners();
		    component.unregisterAllInputProcessors();
		    levelText.removeAllGestureEventListeners();
		    levelText.unregisterAllInputProcessors();
		   
		    component.registerInputProcessor(new TapProcessor(pa));
		    component.addGestureListener(TapProcessor.class, 
		            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
						craniumScene.setLevelId(diff.getId());
						craniumScene.buildSortScene();
					}
					return false;
				}
			});

		    levelText.registerInputProcessor(new TapProcessor(pa));
		    levelText.addGestureListener(TapProcessor.class,
		            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
						craniumScene.setLevelId(diff.getId());
						craniumScene.buildSortScene();
					}
					return false;
				}
			});

		    container.addChild(component);
		}


		centralBar.addChild(container);

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

