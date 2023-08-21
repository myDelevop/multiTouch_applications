package wwf.interactiveMap.view;

import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import wwf.interactiveMap.data.model.DifficoltaDomande;
import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.interactiveMap.sceneManager.currentGame.CurrentDomanda;
import wwf.interactiveMap.sceneManager.currentGame.CurrentGameDifficolta;
import wwf.interactiveMap.sceneManager.currentGame.CurrentGameQuestions;
import wwf.interactiveMap.sceneManager.currentGame.Obstacle;
import wwf.interactiveMap.util.FilesPath;
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

    /** Font bold 24 black. */
	private IFont bold24Black;

	/** Font seguisb 32 white. */
	private IFont seguisb32White;
	
    /** The level selected. */
	private boolean levelSelected = false;

	/** Font difficolta. */
	private CurrentGameDifficolta difficolta;


	/**
	 * Costruttore parametrico che provvede all'inizializzazione 
	 * della componente {@link #root}.
	 *
	 * @param mapScene gestore delle schermate
	 * @param pa the pa
	 * @param playerName nome del giocatore.
	 */
	public LevelScene(final InteractiveMapSceneManager mapScene, 
			final MTApplication pa, final String playerName) {

		root = new MTComponent(pa);

		PImage image = pa.loadImage(FilesPath.IMAGES_PATH + "background2.png");
		MTImage layer = new MTImage(pa, image);
		layer.setPositionGlobal(new Vector3D(pa.width / 2, pa.height / 2));
		layer.setSizeXYGlobal(pa.width, pa.height);
		root.addChild(layer);

		this.bold24White = Fonts.getInstance(pa).getSegoe50White();
		this.bold24Black = Fonts.getInstance(pa).getBold26Black();
		this.seguisb32White = Fonts.getInstance(pa).getSeguisb32White();
		this.difficolta = new CurrentGameDifficolta(mapScene.isItalian(),
				mapScene.isUpdateDB(), pa);

		MTRectangle titleBar = new MTRectangle(pa, 0, 0, pa.width, 143f);
		titleBar.setNoFill(true);
		titleBar.setNoStroke(true);
		titleBar.removeAllGestureEventListeners();
		titleBar.unregisterAllInputProcessors();

		MTTextArea text = new MTTextArea(pa, bold24Black);
		text.setNoStroke(true);
		text.setNoFill(true);
		text.setText("SELEZIONA LIVELLO DI DIFFICOLTA'");
		text.removeAllGestureEventListeners();
		text.unregisterAllInputProcessors();
		text.setPositionGlobal(new Vector3D(pa.width / 2f, 75f));

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
						mapScene.getTapButton().rewind();
						mapScene.getTapButton().play();

						mapScene.setLevelId(diff.getId());
						CurrentGameQuestions currentGameQuestions 
						= new CurrentGameQuestions(pa, true, 
								true, mapScene.getLevelId(), playerName,
								mapScene.isUpdateDB());

						int i = 1;
						for (CurrentDomanda currentD 
						    : currentGameQuestions.getCurrentQuestions()) {
							currentD.setObstacleView(new Obstacle(pa, 
									(90f + 30f) * i, 
									150f, 
									150f, 
									90f,
									"obstacle.png"));
							i++;
						}
						mapScene.setCurrentGameQuestions(currentGameQuestions);
						mapScene.buildMapScene(true, true);
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
						mapScene.getTapButton().rewind();
						mapScene.getTapButton().play();

						mapScene.setLevelId(diff.getId());
						mapScene.setCurrentGameQuestions(
						       new CurrentGameQuestions(pa, true, true, mapScene
						     .getLevelId(), playerName, mapScene.isUpdateDB()));

						mapScene.buildMapScene(true, true);
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

