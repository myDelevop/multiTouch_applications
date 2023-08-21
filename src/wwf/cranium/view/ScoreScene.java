package wwf.cranium.view;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.math.Vector3D;

import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.cranium.sceneManager.currentGame.util.QuestionPlayer;
import wwf.cranium.util.FilesPath;
import wwf.todelete.Fonts;


/**
 * Questa classe inizializza la componente {@link #root}
 *  di elementi grafici che consentono la visualizzazione della ScoreScene.
 */
public class ScoreScene {
	
	/** variabile destinata a contenere tutti gli elementi grafici della scena 
	 * in questione. */
	private MTComponent root;
	
	/** Font semi light 32 black. */
	private IFont semiLight32Black;
	
	/** Font bold 26 black. */
	private IFont bold26Black;

	/** The background. */
	private MTBackgroundImage background;
	
	/** The text. */
	private MTTextArea text;
	
	/** The text name players. */
	private MTTextArea textNamePlayers;
	
	/** The text score. */
	private MTTextArea textScore;
	
	/**
	 * Costruttore parametrico che provvede all'inizializzazione della 
	 * componente {@link #root}.
	 * 
	 * @param craniumScene gestore delle schermate
	 * @param pa the pa
	 * @param scoreData giocatori con rispettivi punteggi
	 * @param numPlayers numero di giocatori
	 */
	public ScoreScene(final CraniumSceneManager craniumScene, 
	        final MTApplication pa, final List<QuestionPlayer> scoreData, 
	        final int numPlayers) {

		this.root = new MTComponent(pa);
		this.bold26Black = Fonts.getInstance(pa).getBold26Black();
		this.semiLight32Black = Fonts.getInstance(pa).getSemiLight32Black();
		this.background = new MTBackgroundImage(pa,
		        pa.loadImage(FilesPath.ROOT + "sea2.jpg"), false);
		this.text = new MTTextArea(pa, bold26Black);
		
		this.textNamePlayers = new MTTextArea(pa, semiLight32Black);
		this.textScore = new MTTextArea(pa, semiLight32Black);


		

	
		text.setNoStroke(true);
		text.setNoFill(true);
		text.setText("CLASSIFICA");

		text.removeAllGestureEventListeners();
		text.unregisterAllInputProcessors();

		text.setPositionGlobal(new Vector3D(pa.width / 2f, 62f));


		
		textNamePlayers.setNoStroke(true);
		textNamePlayers.setNoFill(true);
		textScore.setNoStroke(true);
		textScore.setNoFill(true);
		
		Iterator<QuestionPlayer> iterator = scoreData.iterator();
		QuestionPlayer qp = null;
		
		List<QuestionPlayer> last = new LinkedList<QuestionPlayer>();
		if (last != null) {
		for (int i = 0; i < numPlayers; i++) {
			if (iterator.hasNext()) {
				qp = iterator.next();
				last.add(qp);
			}
		}


			Collections.sort(last);
			Collections.reverse(last);			
		}
	
		String namePlayer = "\n\n\n";
		String score = "\n\n\n";
		

		for (QuestionPlayer qpl : last) {
			namePlayer = namePlayer.concat(qpl.getPlayer().getNome());
			namePlayer = namePlayer.concat("\n");

			score = score.concat(qpl.getPlayer().getPunteggio().toString());
			score = score.concat("\n");
		}
		
		textNamePlayers.setText(namePlayer);
		textScore.setText(score);

		textNamePlayers.removeAllGestureEventListeners();
		textNamePlayers.unregisterAllInputProcessors();
		textScore.removeAllGestureEventListeners();
		textScore.unregisterAllInputProcessors();

		textNamePlayers.setPositionGlobal(new Vector3D(
		        pa.width / 2f - 70f, pa.height / 2f));
		textScore.setPositionGlobal(new Vector3D(
		        pa.width / 2f + 70f, pa.height / 2f));


		
		

	    root.addChild(background);

		root.addChild(text);
		root.addChild(textNamePlayers);
		root.addChild(textScore);

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




