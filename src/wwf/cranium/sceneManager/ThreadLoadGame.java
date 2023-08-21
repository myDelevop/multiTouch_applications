package wwf.cranium.sceneManager;

import org.mt4j.components.visibleComponents.widgets.progressBar.AbstractProgressThread;
import wwf.cranium.sceneManager.currentGame.CurrentGameData;
import wwf.cranium.sceneManager.currentGame.CurrentGameQuestions;

/**
 * Questa classe avvia un thread che carica le domande del gioco corrente.
 */
public class ThreadLoadGame extends AbstractProgressThread {

	
	/** inidca se il gioco è in lungua italiana. */
	private boolean isItalian;
	
	/** numero di giocatori per la partita corrente. */
	private Integer numPlayers;
	
	/** id del livello di difficolta della partita corrente. */
	private Integer levelID;
	
	/** indica se l'ordine di erogazione delle domande è random o meno. */
	private boolean isRandom;
	
	/** riferimento al gestore di schermate. */
	private CraniumSceneManager craniumManager;
	
	/** domande che devono essere inizializzate. */
	private CurrentGameQuestions currentGameQuestions = null;
	
	/** giocatori che deveno essere inizializzati. */
	private CurrentGameData currentGameData = null;

	/**
	 * Costruttore parametrico.
	 *
	 * @param craniumManager riferimento al gestore di schermate
	 * @param isItalian indica se si deve caricare il gioco in lingua italiana
	 * @param numPlayers numero di giocatori 
	 * @param levelID id del livello di difficolta
	 * @param isRandom indica se l'ordine di erogazione delle domande è random 
	 * @param sleepTime the sleep time
	 */
	public ThreadLoadGame(final CraniumSceneManager craniumManager, 
	        final boolean isItalian, final Integer numPlayers, 
			final Integer levelID, final boolean isRandom, 
			final long sleepTime) {
		super(sleepTime);

		this.craniumManager = craniumManager;
		this.isItalian = isItalian;
		this.numPlayers = numPlayers;
		this.levelID = levelID;
		this.isRandom = isRandom;
	}

	/**
	 */
	@Override
    public final void run() {
		
		this.setTarget(4);


		this.setCurrentAction("Caricamento domande...");

		

		craniumManager.buildQuestionScene(this);
	}


}
