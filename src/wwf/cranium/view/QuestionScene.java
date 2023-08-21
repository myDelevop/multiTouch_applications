package wwf.cranium.view;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.cranium.data.SerializeObjects;
import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.cranium.sceneManager.currentGame.CurrentGameData;
import wwf.cranium.sceneManager.currentGame.CurrentGameQuestions;
import wwf.cranium.sceneManager.currentGame.util.CurrentDomanda;
import wwf.cranium.sceneManager.currentGame.util.CurrentPlayer;
import wwf.cranium.sceneManager.currentGame.util.CurrentRisposta;
import wwf.cranium.sceneManager.currentGame.util.QuestionPlayer;
import wwf.cranium.util.AudioTrack;
import wwf.cranium.util.FilesPath;
import wwf.database.DBAccess;
import wwf.todelete.Fonts;


/**
 * Questa classe inizializza la componente {@link #root} di elementi grafici 
 * che consentono la visualizzazione della QuestionScene.
 */

public class QuestionScene {

	/** The pa. */
	private MTApplication pa;
	
	/** variabile destinata a contenere tutti gli elementi grafici della scena
	 *  in questione. */
	private MTComponent root;

	/**  Gestore delle schermate. */
	private CraniumSceneManager craniumScene;
	
	/** Lista di coppie question-player che modellano 
	 * l'i-sima domanda dell'i-simo giocatore. */
	private List<QuestionPlayer> qpList;
	
	/** The current QP. */
	private QuestionPlayer currentQP;
	
	/** The save data score. */
	private List<QuestionPlayer> saveDataScore;
	
	/** The current count. */
	private CountDown currentCount;

	
	/** The wrong answer. */
	private AudioTrack wrongAnswer;
	
	/** The correct answer. */
	private AudioTrack correctAnswer;
	
	/** The elapsed time. */
	private AudioTrack elapsedTime;
	
	/**
	 * The Class CountDown.
	 */
	class CountDown extends TimerTask {

		/** The count. */
		private Integer count;
		
		/** The all players. */
		private List<CurrentPlayer> allPlayers;
		
		/**
		 * Instantiates a new count down.
		 *
		 * @param count the count
		 * @param allPlayers the all players
		 */
		CountDown(final Integer count, final List<CurrentPlayer> allPlayers) {
			this.count = count;
			this.allPlayers = allPlayers;
		}

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
				
			if (count != 0) {
				count--;
				updateCountDown(count);
			} else {
				elapsedTime.pause();
				elapsedTime.rewind();

				textCountDown.setText("NRE");

				currentQP.getPlayer().setPunteggio(
						currentQP.getPlayer().getPunteggio() - 5);

				if (qpList.size() != 0) {
					this.cancel();
					drawStopTimeButton(currentQP, allPlayers);
				} else {
                    craniumScene.buildScoreScene(saveDataScore);
                }
				
				this.cancel();
			}
		}
	}


    /** The semi light 24 black. */
	private IFont semiLight24Black;
	
	/** The semi light 64 black. */
	private IFont semiLight64Black;
    
    /** The bold 26 black. */
	private IFont bold26Black;
    
    /** The questions font. */
	private IFont questionsFont;
    
    /** The bold time. */
	private IFont boldTime;

    /** The background. */
    private MTBackgroundImage background;
	
	/** The children. */
    private MTComponent[] children;
	
	/** The start button. */
	private MTEllipse startButton; // giocatore pronto per cominciare?
	
	/** The text start button. */
	private MTTextArea textStartButton;
	
	/** The lateral tab. */
	private MTRectangle lateralTab;
	
	/** The current player pane. */
	private MTRectangle currentPlayerPane;
	
	/** The current round sub pane. */
	private MTRectangle currentRoundSubPane;
	
	/** The text current round. */
	private MTTextArea textCurrentRound;
	
	/** The current player avatar. */
	private MTRectangle currentPlayerAvatar;
	
	/** The avatar. */
	private MTRectangle avatar;
	
	/** The current score sub pane. */
	private MTRectangle currentScoreSubPane;
	
	/** The text current score. */
	private MTTextArea textCurrentScore;
    
    /** The title players. */
	private MTRectangle titlePlayers;
    
    /** The title players text. */
    private MTTextArea titlePlayersText;
    
    /** The central scene. */
    private MTRectangle centralScene;
	
	/** The title bar. */
    private MTRectangle titleBar;
	
	/** The central title text. */
	private MTTextArea centralTitleText;
	
	/** The question pane. */
	private MTRectangle questionPane;
	
	/** The rect with image. */
	private MTRectangle rectWithImage;
	
	/** The cell with image. */
	private MTListCell cellWithImage;
	
	/** The question text with image. */
	private MTTextArea questionTextWithImage;
	
	/** The cell without image. */
	private MTListCell cellWithoutImage;
	
	/** The question text without image. */
	private MTTextArea questionTextWithoutImage;
    
    /** The timer. */
	private Timer timer;
	
	/** The bar. */
    private MTRectangle bar;
    
	/** The bar text. */
	private MTTextArea barText;
	
	/** The answers pane. */
	private MTRectangle answersPane;
	
	/** The answers. */
	private List<CurrentRisposta> answers;
	
	/** The r 1. */
	private CurrentRisposta r1;
	
	/** The r 2. */
	private CurrentRisposta r2;
    
    /** The answer text. */
	private MTTextArea answerText;
    
    /** The text count down. */
    private MTTextArea textCountDown;
	
	/** The stop time button. */
    private MTEllipse stopTimeButton; // tempo scaduto
	
	/** The text stop time button. */
	private MTTextArea textStopTimeButton;
	
	/** The result button. */
	private MTEllipse resultButton; // risposta corretta/errata
	
	/** The text result button. */
	private MTTextArea textResultButton;

	
	/**
	 * Instantiates a new question scene.
	 *
	 * @param craniumScene the cranium scene
	 * @param pa the pa
	 * @param currentGameQuestions the current game questions
	 * @param currentGameData the current game data
	 */
	public QuestionScene(final CraniumSceneManager craniumScene, 
	        final MTApplication pa,	final CurrentGameQuestions 
	        currentGameQuestions, final CurrentGameData currentGameData) {

		List<CurrentPlayer> allPlayers = currentGameData.getCurrentPlayers();
		List<CurrentDomanda> allQuestions = 
		        currentGameQuestions.getCurrentQuestions();
		

		if (currentGameQuestions.isRandom()) {
            Collections.shuffle(allQuestions);
        }

		SerializeObjects serializeManager = new SerializeObjects();
		serializeManager.start();

		
		this.pa = pa;
		this.root = new MTComponent(pa);

		this.craniumScene = craniumScene;
		this.craniumScene.setClearColor(new MTColor(00f, 151f, 167f));

		this.semiLight24Black = Fonts.getInstance(pa).getSemiLight32Black();
		this.semiLight64Black = Fonts.getInstance(pa).getSemiLight64Black();
		this.bold26Black = Fonts.getInstance(pa).getBold26Black();
		this.questionsFont = Fonts.getInstance(pa).getBold26Black();
		this.boldTime = Fonts.getInstance(pa).getBoldTime();
		
		this.children = root.getChildren();
		this.startButton = new MTEllipse(pa, 
		        new Vector3D(500f, 500f), 370f, 218f);
		this.stopTimeButton = new MTEllipse(pa, 
		        new Vector3D(500f, 500f), 370f, 218f);
		this.resultButton = new MTEllipse(pa, 
		        new Vector3D(500f, 500f), 670f, 318f);
		this.textStartButton = new MTTextArea(pa, semiLight24Black);
		this.textStopTimeButton = new MTTextArea(pa, semiLight24Black);
		this.textResultButton = new MTTextArea(pa, semiLight64Black);
		this.lateralTab = new MTRectangle(pa, 0f, 0f, pa.width / 5f, pa.height);
		this.currentPlayerPane = new MTRectangle(pa, 0f, 
		        pa.height * 7f / 11f, pa.width / 5f, pa.height * 4f / 11f);
		this.currentRoundSubPane = new MTRectangle(pa, 0f, 
		        pa.height * 7f / 11f, pa.width / 5f, (pa.height * 4f / 11f)
		        * 2f / 7f);
		this.textCurrentRound = new MTTextArea(pa, semiLight24Black);
		this.currentPlayerAvatar = new MTRectangle(pa, 0f,
				pa.height * 7f / 11f 
				+ currentPlayerPane.getHeightXY(TransformSpace.GLOBAL) * 2f 
				/ 7f, pa.width / 5f, (pa.height * 4f / 11f) * 4f / 7f);
		this.avatar = new MTRectangle(pa, 
		        pa.width / 5f / 2f, (pa.height * 4f / 11f) * 4f / 7f - 40f);
		this.currentScoreSubPane = new MTRectangle(pa, 0f,
	    		pa.height * 7f / 11f + currentPlayerPane.getHeightXY(
	    		        TransformSpace.GLOBAL) * 6f / 7f,
	    		pa.width / 5f, (pa.height * 4f / 11f) * 1f / 7f);
		this.textCurrentScore = new MTTextArea(pa, semiLight24Black);
		this.titlePlayers = new MTRectangle(pa, 0f, 0f,  
		        pa.width / 5f, pa.height * 1f / 11f);
		this.titlePlayersText = new MTTextArea(pa, semiLight24Black);
		this.centralScene = new MTRectangle(pa, pa.width / 5f,
		        0f, pa.width - pa.width / 5f, pa.height);
		this.titleBar = new MTRectangle(pa, pa.width / 5f, 0f, 
		        pa.width - pa.width / 5f, pa.height * 1f / 11f);
		this.centralTitleText = new MTTextArea(pa, bold26Black);
		this.questionPane = new MTRectangle(pa, pa.width / 5f, pa.height * 1f 
		        / 11f, pa.width - pa.width / 5f, pa.height * 4f / 11f);
		this.timer = new Timer();
		this.rectWithImage = new MTRectangle(pa,
				pa.width / 5f + pa.width * 4f / 5f / 2f + 5f, 
				pa.height * 1f / 11f + 5f, 
				pa.width - (pa.width / 5f + pa.width * 4f / 5f / 2f) - 10f, 
				pa.height * 4f / 11f - 10f);
		this.cellWithImage = new MTListCell(pa,
				pa.width - (pa.width / 5f + pa.width * 4f / 5f / 2f) - 10f,
				pa.height * 6f / 11f + 325f);
		this.questionTextWithImage = new MTTextArea(pa, 
				0f,
				0f,
				pa.width - (pa.width / 5f + pa.width * 4f / 5f / 2f) - 10f, 
				pa.height * 6f / 11f + 325f);
		this.cellWithoutImage = new MTListCell(pa,
				pa.width - pa.width / 5f - 10f, 
				pa.height * 6f / 11f + 25f);
		this.questionTextWithoutImage = new MTTextArea(pa, 
				0f,
				0f,
				pa.width - pa.width / 5f - 10f, 
				pa.height * 6f / 11f + 25f);
		this.bar = new MTRectangle(pa, pa.width / 5f, 
		        pa.height * 5f / 11f, pa.width - pa.width / 5f, 
		        pa.height * 1f / 11f);
		this.barText = new MTTextArea(pa, semiLight24Black);
		this.answersPane = new MTRectangle(pa,
				pa.width / 5f,
				pa.height * 6f / 11f,
				pa.width - pa.width / 5f,
				pa.height * 5f / 11f);
		this.r1 = new CurrentRisposta("SI", true);
		this.r2 = new CurrentRisposta("NO", false);
		this.answerText = new MTTextArea(pa, semiLight24Black);


	    this.textCountDown = new MTTextArea(pa, boldTime);


	 	this.wrongAnswer = new AudioTrack(
	 	        FilesPath.SOUNDS_PATH + "wrong.mp3", pa);
	 	this.correctAnswer = new AudioTrack(
	 	        FilesPath.SOUNDS_PATH + "correct.mp3", pa);
	 	this.elapsedTime = new AudioTrack(
	 	        FilesPath.SOUNDS_PATH + "elapsedTime.wav", pa);

	    try {
			DBAccess.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Manager manager = new Manager(allQuestions, allPlayers);

		this.qpList = manager.getList();
		this.saveDataScore = new LinkedList<QuestionPlayer>();
		
		for (QuestionPlayer qp:qpList) {
			saveDataScore.add(qp);
		}



		if (qpList.size() != 0) {
			QuestionPlayer qp = qpList.get(0);
			currentQP = qp;
			qpList.remove(qp);
			
			drawButton(qp, allPlayers);
			
		} else {
            craniumScene.buildScoreScene(saveDataScore);
        }
	}
	
	/**
	 * Draw button.
	 *
	 * @param qp the qp
	 * @param allPlayers the all players
	 */
	private void drawButton(final QuestionPlayer qp,
	        final List<CurrentPlayer> allPlayers) {

		startButton.setPositionGlobal(new Vector3D(pa.width / 2f,
		        pa.height / 2f));

		startButton.setNoStroke(true);
		startButton.setFillColor(new MTColor(0f, 120f, 215f, 198f));
		startButton.setName("button");
	    
	    for (MTComponent c:children) {
	    	if (!c.getName().equalsIgnoreCase("button")) {
	    		c.setVisible(false);	
	    	}
	    }


	    textStartButton.setNoStroke(true);
	    textStartButton.setNoFill(true);
	    textStartButton.setText(qp.getPlayer().getNome().toUpperCase() 
	    			+ ", PRONTO PER COMINCIARE?");
	    textStartButton.removeAllGestureEventListeners();
	    textStartButton.unregisterAllInputProcessors();


	    startButton.addChild(textStartButton);
	    textStartButton.setPositionRelativeToParent(new Vector3D(
	            startButton.getCenterPointLocal().x, 
	            startButton.getCenterPointLocal().y));


	    
	    textStartButton.removeAllGestureEventListeners();
	    textStartButton.unregisterAllInputProcessors();

	    startButton.removeAllGestureEventListeners();
	    startButton.unregisterAllInputProcessors();
	    

	    textStartButton.registerInputProcessor(new TapProcessor(pa));
	    textStartButton.addGestureListener(TapProcessor.class, 
	            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
						removeChildren();
						drawCentralScene(qp, allPlayers);
						drawLateralBar(qp, allPlayers);
					}

					return false;
				}
			});

		    
	    startButton.registerInputProcessor(new TapProcessor(pa));
	    startButton.addGestureListener(TapProcessor.class, 
	            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
						removeChildren(); 
						drawLateralBar(qp, allPlayers);
						drawCentralScene(qp, allPlayers);
						
					}

					return false;
				}
			});

	    root.addChild(startButton);
	}
	

	/**
	 * Draw lateral bar.
	 *
	 * @param qp the qp
	 * @param allPlayers the all players
	 */
	private void drawLateralBar(final QuestionPlayer qp, 
	        final List<CurrentPlayer> allPlayers) {
	    lateralTab.removeAllGestureEventListeners();
	    lateralTab.unregisterAllInputProcessors();
	    lateralTab.setNoStroke(false);
	    lateralTab.setStrokeWeight(3f);
	    lateralTab.setStrokeColor(new MTColor(00f, 151f, 167f));
	    lateralTab.setFillColor(new MTColor(00f, 188f, 212f));
	    
	    currentPlayerPane.removeAllGestureEventListeners();
	    currentPlayerPane.unregisterAllInputProcessors();
	    currentPlayerPane.setNoStroke(false);
	    currentPlayerPane.setStrokeWeight(3f);
	    currentPlayerPane.setStrokeColor(new MTColor(00f, 151f, 167f));
	    currentPlayerPane.setFillColor(new MTColor(178f, 235f, 242f));
	    currentPlayerPane.addChild(currentRoundSubPane);
	    
	    currentRoundSubPane.removeAllGestureEventListeners();
	    currentRoundSubPane.unregisterAllInputProcessors();
	    currentRoundSubPane.setNoStroke(true);
	    currentRoundSubPane.setNoFill(true);
	    textCurrentRound.setNoStroke(true);
	    textCurrentRound.setNoFill(true);
	    
	    if (qp.getQuestion().getTipologia().equalsIgnoreCase("diretta") 
	            || qp.getQuestion().getTipologia().equalsIgnoreCase("direct")) {
            textCurrentRound.setText(qp.getPlayer().getNome() 
                    + "\n è il tuo turno!");
        } else {
            textCurrentRound.setText(qp.getPlayer().getNome() 
                    + "\n si sta esibendo!");
        }
	    textCurrentRound.removeAllGestureEventListeners();
	    textCurrentRound.unregisterAllInputProcessors();
	    
	    currentRoundSubPane.addChild(textCurrentRound);
	    textCurrentRound.setPositionRelativeToParent(new Vector3D(
	            currentRoundSubPane.getPosition(TransformSpace.LOCAL).x, 
	            currentRoundSubPane.getCenterPointLocal().y));

		currentPlayerPane.addChild(currentPlayerAvatar);
		currentPlayerAvatar.removeAllGestureEventListeners();
		currentPlayerAvatar.unregisterAllInputProcessors();
		currentPlayerAvatar.setNoStroke(true);
		currentPlayerAvatar.setNoFill(true);
		currentPlayerAvatar.setFillColor(MTColor.GRAY);

		File file = new File(FilesPath.AVATARS_PATH);
		String[] imageFile = file.list(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return (name.equalsIgnoreCase(qp.getPlayer().getNome() 
				        + ".png"))
					 || (name.equalsIgnoreCase(qp.getPlayer().getNome() 
					    + ".jpg"));
			}
		});
		if (imageFile.length != 0) {
            avatar.setTexture(pa.loadImage(FilesPath.AVATARS_PATH 
                    + imageFile[0]));
        } else {
            avatar.setTexture(pa.loadImage(FilesPath.AVATARS_PATH 
                    + "default.png"));
        } 
		avatar.setNoStroke(true);

		avatar.removeAllGestureEventListeners();
		avatar.unregisterAllInputProcessors();
	    
		currentPlayerAvatar.addChild(avatar);
		avatar.setPositionRelativeToParent(new Vector3D(
		        currentPlayerAvatar.getPosition(TransformSpace.LOCAL).x,
		        currentPlayerAvatar.getPosition(TransformSpace.LOCAL).y));

	    currentPlayerPane.addChild(currentScoreSubPane);
	    currentScoreSubPane.removeAllGestureEventListeners();
	    currentScoreSubPane.unregisterAllInputProcessors();
	    currentScoreSubPane.setNoStroke(true);
	    currentScoreSubPane.setNoFill(true);
	    currentScoreSubPane.setFillColor(MTColor.GRAY);
	    
	    textCurrentScore.setNoStroke(true);
	    textCurrentScore.setNoFill(true);
	    textCurrentScore.setText("Score: " + qp.getPlayer().getPunteggio());
	    textCurrentScore.removeAllGestureEventListeners();
	    textCurrentScore.unregisterAllInputProcessors();
	    
	    currentScoreSubPane.addChild(textCurrentScore);
	    textCurrentScore.setPositionRelativeToParent(new Vector3D(
	            currentScoreSubPane.getPosition(TransformSpace.LOCAL).x, 
	            currentScoreSubPane.getCenterPointLocal().y));
	    
	    titlePlayers.setNoStroke(true);
	    titlePlayers.setFillColor(new MTColor(0, 0, 0, 0));
	    titlePlayers.removeAllGestureEventListeners();
	    titlePlayers.unregisterAllInputProcessors();
	    titlePlayersText.setNoFill(true);
	    titlePlayersText.setNoStroke(true);
	    titlePlayersText.setStrokeColor(MTColor.AQUA);
	    titlePlayersText.setText("GIOCATORI");
	    titlePlayersText.removeAllGestureEventListeners();
	    titlePlayersText.unregisterAllInputProcessors();
	    
	    titlePlayers.addChild(titlePlayersText);
	    titlePlayersText.setPositionRelativeToParent(new Vector3D(
	            titlePlayers.getPosition(TransformSpace.LOCAL).x,
	            titlePlayers.getCenterPointLocal().y));

	    titlePlayersText.translate(new Vector3D(
	    		-(titlePlayersText.getPosition(TransformSpace.GLOBAL).x) 
	    		+ titlePlayersText.getWidthXY(TransformSpace.GLOBAL) / 2f + 3f,
	    		0f,
	    		0f));
    	lateralTab.addChild(titlePlayers);

        float posX = 0;
        float posY = 0;
    	MTRectangle iPlayer;
    	MTTextArea iName;
    	
    	for (CurrentPlayer p : allPlayers) {
		
	    	posY += pa.height * 1f / 11f;
	    	iPlayer = new MTRectangle(pa, posX + 1f, posY, pa.width / 5f 
	    	        - 4f, pa.height * 1f / 11f);
	    	iPlayer.setStrokeWeight(2f);

	    	iPlayer.setNoStroke(true);
	    	
	    	iPlayer.setFillColor(new MTColor(0, 0, 0, 0));
	    	iPlayer.removeAllGestureEventListeners();
	    	iPlayer.unregisterAllInputProcessors();
	    	
	    	iPlayer.setNoFill(true);
	    	iPlayer.setNoStroke(false);
	    	iPlayer.setStrokeColor(MTColor.AQUA);

	    	iName = new MTTextArea(pa, semiLight24Black);
	    	iName.setNoStroke(true);
	    	iName.setNoFill(true);
	    	iName.setText(p.getNome() 
	    	        + " (" + p.getPunteggio() + ")");
	    	iName.removeAllGestureEventListeners();
	    	iName.unregisterAllInputProcessors();
		    
	    	iPlayer.addChild(iName);
		    iName.setPositionRelativeToParent(new Vector3D(
		            iPlayer.getPosition(TransformSpace.LOCAL).x, 
		            iPlayer.getCenterPointLocal().y));
		    
		    iName.translate(new Vector3D(
		    		-(iName.getPosition(TransformSpace.GLOBAL).x) 
		    		+ iName.getWidthXY(TransformSpace.GLOBAL) / 2f + 3f,
		    		0f,
		    		0f));

		    lateralTab.addChild(iPlayer);    		
    	}

	    lateralTab.addChild(currentPlayerPane);
		root.addChild(lateralTab);		
	}
	
	/**
	 * Draw central scene.
	 *
	 * @param qp the qp
	 * @param allPlayers the all players
	 */
	private void drawCentralScene(final QuestionPlayer qp, 
	        final List<CurrentPlayer> allPlayers) {

		centralScene.removeAllGestureEventListeners();
		centralScene.unregisterAllInputProcessors();
		centralScene.setNoStroke(true);
		centralScene.setNoFill(true);

		centralScene.addChild(titleBar);
		titleBar.setNoStroke(true);
		titleBar.setNoFill(true);
		titleBar.removeAllGestureEventListeners();
		titleBar.unregisterAllInputProcessors();
		
		centralTitleText.setNoStroke(true);
		centralTitleText.setNoFill(true);
		centralTitleText.setText(qp.getQuestion().getTitolo().toUpperCase());
		centralTitleText.removeAllGestureEventListeners();
		centralTitleText.unregisterAllInputProcessors();
		centralTitleText.setPositionGlobal(new Vector3D(
		        pa.width / 2f + (pa.width / 5f / 2f), 31f));

		titleBar.addChild(centralTitleText);

		textCountDown.setNoStroke(true);
		textCountDown.setNoFill(true);
		textCountDown.removeAllGestureEventListeners();
		textCountDown.unregisterAllInputProcessors();
 		

	    textCountDown.setNoStroke(true);
	    textCountDown.setNoFill(true);
	    textCountDown.removeAllGestureEventListeners();
	    textCountDown.unregisterAllInputProcessors();
	    textCountDown.setText("1");	    
	    bar.addChild(textCountDown);
	    textCountDown.setPositionGlobal(new Vector3D(pa.width - 80f, 
	            bar.getCenterPointLocal().y));

		boolean withImage = qp.getQuestion().isWithImage();

		centralScene.addChild(questionPane);
		questionPane.setNoStroke(true);
		questionPane.setNoFill(true);
		questionPane.removeAllGestureEventListeners();
		questionPane.unregisterAllInputProcessors();
		
		
		System.out.println(withImage);
		questionPane.removeAllChildren();
		
		if (withImage) {

			cellWithImage.setNoFill(true);
			cellWithImage.setNoStroke(true);
			questionPane.addChild(rectWithImage);
			rectWithImage.removeAllGestureEventListeners();
			rectWithImage.unregisterAllInputProcessors();

			MTList aList = new MTList(pa,
					pa.width / 5f + 5f, 
					pa.height * 1f / 11f + 5f, 
					pa.width - (pa.width / 5f + pa.width * 4f / 5f / 2f) - 10f, 
					pa.height * 4f / 11f - 10f);
			
				questionPane.addChild(aList);


				questionTextWithImage.setFont(questionsFont);
				questionTextWithImage.setNoStroke(true);
				questionTextWithImage.setNoFill(false);

				File file = new File(FilesPath.QUESTIONS_IMAGES_PATH);
				String[] imageFile = file.list(new FilenameFilter() {
					
					@Override
					public boolean accept(final File dir, final String name) {
						return (name.equalsIgnoreCase(qp.getQuestion()
						        .getTitolo() + ".png"))
							 || (name.equalsIgnoreCase(qp.getQuestion()
							         .getTitolo() + ".jpg"));
					}
				});

				if (imageFile.length != 0) {
                    rectWithImage.setTexture(pa.loadImage(
                            FilesPath.QUESTIONS_IMAGES_PATH + imageFile[0]));
                } else {
                    rectWithImage.setTexture(pa.loadImage(
                            FilesPath.QUESTIONS_IMAGES_PATH + "nodata.png"));
                }

				
				
				questionTextWithImage.setTexture(pa.loadImage(
				        FilesPath.ROOT + "textBackground.jpg"));
				questionTextWithImage.setText(qp.getQuestion().getTesto());

				questionTextWithImage.removeAllGestureEventListeners();
				questionTextWithImage.unregisterAllInputProcessors();
				
				cellWithImage.addChild(questionTextWithImage);
				aList.scrollY(questionTextWithImage.
				        getFont().getFontAbsoluteHeight());
				aList.addListElement(cellWithImage);	
				aList.setNoFill(true);
				aList.setNoStroke(true);
		} else {
			MTList aList = new MTList(pa,
					pa.width / 5f + 5f, 
					pa.height * 1f / 11f + 5f, 
					pa.width - pa.width / 5f - 10f, 
					pa.height * 4f / 11f - 10f);
				questionPane.addChild(aList);

				questionTextWithoutImage.setFont(questionsFont);
				questionTextWithoutImage.setNoStroke(true);
				questionTextWithoutImage.setNoFill(false);
				questionTextWithoutImage.setTexture(pa.loadImage(
				        FilesPath.ROOT + "textBackground.jpg"));
				questionTextWithoutImage.setText(qp.getQuestion().getTesto());

				questionTextWithoutImage.removeAllGestureEventListeners();
				questionTextWithoutImage.unregisterAllInputProcessors();
				
				cellWithoutImage.setNoFill(true);
				cellWithoutImage.setNoStroke(true);
				cellWithoutImage.addChild(questionTextWithoutImage);
				aList.scrollY(questionTextWithoutImage.getFont()
				        .getFontAbsoluteHeight());
				aList.addListElement(cellWithoutImage);		
				aList.setNoFill(true);
				aList.setNoStroke(true);

		}


		centralScene.addChild(bar);
		bar.setNoStroke(true);
		bar.setNoFill(true);
		bar.removeAllGestureEventListeners();
		bar.unregisterAllInputProcessors();
		barText.setNoStroke(true);
		barText.setNoFill(true);
		
		if (qp.getQuestion().getTipologia().
		        equalsIgnoreCase("diretta") || qp.getQuestion().
		        getTipologia().equalsIgnoreCase("direct")) {
            barText.setText("SCEGLI UNA TRA LE SEGUENTI RISPOSTE");
        } else {
            barText.setText("L'ESERCIZIO E' STATO SVOLTO CORRETTAMENTE?");
        }
			
		
		barText.removeAllGestureEventListeners();
		barText.unregisterAllInputProcessors();
		barText.setPositionGlobal(new Vector3D(
		        pa.width / 2f + (pa.width / 5f / 2f),
				pa.height * 5f / 11f + pa.height * 1f / 11f / 2f));

		bar.addChild(barText);
		
		
		
		centralScene.addChild(answersPane);
		answersPane.setNoFill(true);
		answersPane.setNoStroke(false);
		answersPane.removeAllGestureEventListeners();
		answersPane.unregisterAllInputProcessors();
		
		if (qp.getQuestion().getTipologia().equalsIgnoreCase("diretta") 
		        || qp.getQuestion().getTipologia().equalsIgnoreCase("direct")) {
            answers = qp.getQuestion().getRisposte();
        } else {
			answers = new LinkedList<CurrentRisposta>();
			
			answers.add(r1);
			answers.add(r2);
		}

		
		
		
		MTRectangle cell;
		

		Integer numRows = 2;

		if (answers.size() >= 9) {
		    numRows = 3;
		} else if (answers.size() >= 3) {
		    numRows = 2;
		}
		
		Integer numColumns = ((Double) Math.ceil(answers.size() 
		        / numRows.doubleValue())).intValue();
		float padding = 20f;

		
		switch(answers.size()) {
		case 2: 
		    numRows = 1;
		    numColumns = 2;
			padding = 60f;
			break;
        default:
            break;
		}

		
		float widthPane = answersPane.getWidthXY(TransformSpace.LOCAL);
		float heightPane = answersPane.getHeightXY(TransformSpace.LOCAL);
		
		
		float width = (widthPane / numColumns)
		        - ((padding * 2 + padding * (numColumns - 1)) 
				        / numColumns);
		
		float height = (heightPane / numRows) 
		        - ((padding * 2f + padding * (numRows - 1)) 
				        / numRows);

		float startX = (width / 2f 
				+ padding + pa.width / 5f) 
				- (width + padding); // increase this in for
		float startY = height / 2f 
				+ padding + pa.height * 6f / 11f;
		
		int i = 1;
		
		float x = startX;
		float y = startY;
		
		for (final CurrentRisposta answer:answers) {
			
			if (i <= numColumns) {
				x += width + padding;
				i++;
			} else {
				i = 2;
				x = startX;
				x += width + padding;
				y += height + padding;
			}
			
			
			cell = new MTRectangle(pa, width, height);
			cell.setName("answer");

			cell.setPositionGlobal(new Vector3D(x, y));


			cell.setFillColor(new MTColor(0f, 120f, 215f, 212f));
			cell.setNoStroke(true);


		    MTTextArea text1;
		    MTTextArea text2;
		    
		    if (answer.getTesto().length() <= 50) {
		    	text1 = new MTTextArea(pa, semiLight24Black);


				
		    	text1.setName("text");
			    text1.setText(answer.getTesto());

			    text1.setNoStroke(true);
			    text1.setNoFill(true);
			    
			    text1.removeAllGestureEventListeners();
			    text1.unregisterAllInputProcessors();
			    
			    text1.registerInputProcessor(new TapProcessor(pa));
			    text1.addGestureListener(TapProcessor.class, 
			            new IGestureEventListener() {
					
					@Override
					public boolean processGestureEvent(final 
					        MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.getTapID() == TapEvent.TAPPED) {
							currentCount.cancel();
							elapsedTime.pause();
							drawResultButton(answer, qp, allPlayers);
						}
						return false;
					}
				});


				cell.addChild(text1);

				text1.setPositionGlobal(new Vector3D(
						cell.getPosition(TransformSpace.GLOBAL).x,
						cell.getPosition(TransformSpace.GLOBAL).y));

		    	
		    } else {
		    	text2 = new MTTextArea(pa,
		    			x,
		    	   		cell.getPosition(TransformSpace.GLOBAL).y,
			    		width - 1.3f, 
			    		height - 1.3f);

			    //titleBar.addChild(text2);

			    text2.setFont(semiLight24Black);
			    text2.setName("text");
			    text2.setNoStroke(true);
			    text2.setNoFill(true);
			    text2.setText(answer.getTesto());
			    
	
			    text2.removeAllGestureEventListeners();
			    text2.unregisterAllInputProcessors();
	
	
			    text2.registerInputProcessor(new TapProcessor(pa));
			    text2.addGestureListener(TapProcessor.class, 
			            new IGestureEventListener() {
					
					@Override
					public boolean processGestureEvent(final 
					        MTGestureEvent ge) {
						TapEvent te = (TapEvent) ge;
						if (te.getTapID() == TapEvent.TAPPED) {
							currentCount.cancel();
							elapsedTime.pause();
							drawResultButton(answer, qp, allPlayers);
						}
						return false;
					}
				});

				cell.addChild(text2);

				text2.setPositionRelativeToParent(new Vector3D(
						 text2.getWidthXY(TransformSpace.GLOBAL) / 2,
						 text2.getHeightXY(TransformSpace.GLOBAL) / 2));

		    
		    }
		    cell.removeAllGestureEventListeners();
		    cell.unregisterAllInputProcessors();
		    cell.registerInputProcessor(new TapProcessor(pa));
		    cell.addGestureListener(TapProcessor.class, 
		            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						currentCount.cancel();
						elapsedTime.pause();
						drawResultButton(answer, qp, allPlayers);
					}
					return false;
				}
			});

		    answersPane.addChild(cell);

		}
		
		List<MTRectangle> allAnswers = new LinkedList<MTRectangle>();
		
		for (MTComponent c : answersPane.getChildren()) {
			if (c instanceof MTRectangle && c.getName().
			        equalsIgnoreCase("answer")) {
                allAnswers.add((MTRectangle) c);
            }
		}
		

	    currentCount = new CountDown(qp.getQuestion().getTempo(), allPlayers);
	    timer.schedule(currentCount, 1000, 1000);

		root.addChild(centralScene);

	}


	/**
	 * Gets the root component.
	 *
	 * @return the root component
	 */
	public final MTComponent getRootComponent() {
		return root;
	}

	
	/**
	 * Removes the children.
	 */
	private void removeChildren() {
	//font    
//		children.remo;
		startButton.removeAllChildren();
		textStartButton.removeAllChildren();
		stopTimeButton.removeAllChildren();
		textStopTimeButton.removeAllChildren();
		resultButton.removeAllChildren();
		textResultButton.removeAllChildren();

		lateralTab.removeAllChildren();
		currentPlayerPane.removeAllChildren();
		currentRoundSubPane.removeAllChildren();
		textCurrentRound.removeAllChildren();
		currentPlayerAvatar.removeAllChildren();
		avatar.removeAllChildren();
		currentScoreSubPane.removeAllChildren();
		textCurrentScore.removeAllChildren();
	    titlePlayers.removeAllChildren();
	    titlePlayersText.removeAllChildren();
	    centralScene.removeAllChildren();
		titleBar.removeAllChildren();
		centralTitleText.removeAllChildren();
		questionPane.removeAllChildren();
		rectWithImage.removeAllChildren();
		cellWithImage.removeAllChildren();
		questionTextWithImage.removeAllChildren();
		cellWithoutImage.removeAllChildren();
		questionTextWithoutImage.removeAllChildren();
		bar.removeAllChildren();
		barText.removeAllChildren();
		answersPane.removeAllChildren();
	    answerText.removeAllChildren();
	    textCountDown.removeAllChildren();
	    root.removeAllChildren();
	    
	}

	
	/**
	 * Update count down.
	 *
	 * @param count the count
	 */
	private void updateCountDown(final Integer count) {
		if (count <= 10) {
			if (!elapsedTime.isPlaying()) {
				elapsedTime.loop();
			}
			textCountDown.getFont().setFillColor(MTColor.RED);
		} else {
            textCountDown.getFont().setFillColor(MTColor.BLACK);
        }

		textCountDown.setText(count.toString());
	}

	/**
	 * Draw stop time button.
	 *
	 * @param qp the qp
	 * @param allPlayers the all players
	 */
	private void drawStopTimeButton(final QuestionPlayer qp, 
	        final List<CurrentPlayer> allPlayers) {
		removeChildren();
		
		startButton.setPositionGlobal(new Vector3D(pa.width / 2f,
		        pa.height / 2f));

		startButton.setNoStroke(true);
		startButton.setFillColor(new MTColor(0f, 120f, 215f, 198f));

		textStartButton.setNoFill(true);
		textStartButton.setNoStroke(true);
		textStartButton.setText("TEMPO SCADUTO, MI DISPIACE HAI PERSO 5 PUNTI");

		startButton.addChild(textStartButton);

		textStartButton.setPositionRelativeToParent(
		        new Vector3D(startButton.getCenterPointLocal().x, 
		                startButton.getCenterPointLocal().y));
		
		textStartButton.removeAllGestureEventListeners();
	    textStartButton.unregisterAllInputProcessors();

	    startButton.removeAllGestureEventListeners();
	    startButton.unregisterAllInputProcessors();
	    
	    
	    textStartButton.registerInputProcessor(new TapProcessor(pa));
	    textStartButton.addGestureListener(TapProcessor.class,
	            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						
						
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
							
						if (qpList.size() != 0) {
							QuestionPlayer newQp = qpList.get(0);
							currentQP = newQp;
							qpList.remove(newQp);
							
							removeChildren();
							if (qp.getQuestion().getTempo() != 0) {
                                drawButton(newQp, allPlayers);
                            } else {
								
								drawCentralScene(newQp, allPlayers);
								drawLateralBar(newQp, allPlayers);

							}
								
						} else {
                            craniumScene.buildScoreScene(saveDataScore);
                        }

						}

					

					return false;
				}
			});

		    
	    startButton.registerInputProcessor(new TapProcessor(pa));
	    startButton.addGestureListener(TapProcessor.class,
	            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
						
					if (qpList.size() != 0) {
						QuestionPlayer newQp = qpList.get(0);
						currentQP = newQp;
						qpList.remove(newQp);
						
						removeChildren();
						if (qp.getQuestion().getTempo() != 0) {
                            drawButton(newQp, allPlayers);
                        } else {
							
							drawCentralScene(newQp, allPlayers);
							drawLateralBar(newQp, allPlayers);

						}
							
					} else {
                        craniumScene.buildScoreScene(saveDataScore);
                    }

					}

					return false;
				}
			});
	     

		
		root.addChild(startButton);
		
	}
	
	/**
	 * Draw result button.
	 *
	 * @param answer the answer
	 * @param qp the qp
	 * @param allPlayers the all players
	 */
	private void drawResultButton(final CurrentRisposta answer, 
	        final QuestionPlayer qp, final List<CurrentPlayer> allPlayers) {
		
		removeChildren();
		elapsedTime.pause();
		
		resultButton.setPositionGlobal(new Vector3D(
		        pa.width / 2f, pa.height / 2f));
		resultButton.setNoStroke(true);
		resultButton.setFillColor(new MTColor(0f, 120f, 215f, 198f));


		if (answer.isCorretta()) {
			correctAnswer.rewind();
			correctAnswer.play();
			background = new MTBackgroundImage(pa, 
			        pa.loadImage(FilesPath.ROOT + "correct.jpg"), false);
			textResultButton.setText("Complimenti " + qp.getPlayer().getNome() 
			        + ", risposta esatta!\nhai accumulato altri 10 punti.");
		} else {		
			wrongAnswer.rewind();
			wrongAnswer.play();
			background = new MTBackgroundImage(pa, pa.loadImage(
			        FilesPath.ROOT + "wrong.jpg"), false);
			textResultButton.setText("Peccato " + qp.getPlayer().getNome() 
			        + ". risposta sbagliata!\nhai perso 5 punti");
		}

		textResultButton.setNoStroke(true);
		textResultButton.setNoFill(true);
		resultButton.addChild(textResultButton);
		textResultButton.setPositionRelativeToParent(new Vector3D(
		        resultButton.getCenterPointLocal().x, 
		        resultButton.getCenterPointLocal().y));
		
		textResultButton.removeAllGestureEventListeners();
		textResultButton.unregisterAllInputProcessors();
		resultButton.removeAllGestureEventListeners();
		resultButton.unregisterAllInputProcessors();
	    
	    textResultButton.registerInputProcessor(new TapProcessor(pa));
	    textResultButton.addGestureListener(TapProcessor.class, 
	            new IGestureEventListener() {		
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						
						currentCount.cancel();
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
						//manager.getList().remove(qp);
						if (answer.isCorretta()) {
							qp.getPlayer().setPunteggio(qp.getPlayer()
							        .getPunteggio() + 10);
						} else {
							qp.getPlayer().setPunteggio(qp.getPlayer()
							        .getPunteggio() - 5);
						}
						
						if (qpList.size() != 0) {
							QuestionPlayer newQp = qpList.get(0);
							currentQP = newQp;
							qpList.remove(newQp);
							
							removeChildren();
							if (qp.getQuestion().getTempo() != 0) {
                                drawButton(newQp, allPlayers);
                            } else {
								
								drawCentralScene(newQp, allPlayers);
								drawLateralBar(newQp, allPlayers);

							}
								
						} else {
                            craniumScene.buildScoreScene(saveDataScore);
                        }
					}
					return false;
				}
			});

	    resultButton.registerInputProcessor(new TapProcessor(pa));
	    resultButton.addGestureListener(TapProcessor.class, 
	            new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(final MTGestureEvent ge) {
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) {
						currentCount.cancel();
						craniumScene.getAudioTapButtons().rewind();
						craniumScene.getAudioTapButtons().play();
						//manager.getList().remove(qp);
						if (answer.isCorretta()) {
							qp.getPlayer().setPunteggio(
							        qp.getPlayer().getPunteggio() + 10);
						} else {
							qp.getPlayer().setPunteggio(
							        qp.getPlayer().getPunteggio() - 5);
						}
						
						if (qpList.size() != 0) {
							QuestionPlayer newQp = qpList.get(0);
							currentQP = newQp;
							qpList.remove(newQp);
							
							removeChildren();
							if (qp.getQuestion().getTempo() != 0) {
                                drawButton(newQp, allPlayers);
                            } else {
								drawCentralScene(newQp, allPlayers);
								drawLateralBar(newQp, allPlayers);

							}
								
						} else {
                            craniumScene.buildScoreScene(saveDataScore);
                        }
					}

					return false;
				}
			});
	     

		root.addChild(background);
		root.addChild(resultButton);
		
	}
	

}


/**
 * 
 * @author rock_
 *
 */
class Manager {
    
	/** lista di coppie giocatore-domanda. */
	private List<QuestionPlayer> list;
	
    /**
     * Costruttore parametrico.
     * @param currentQuestions domande
     * @param allPlayers giocatori
     */
	Manager(final List<CurrentDomanda> currentQuestions, 
	        final List<CurrentPlayer> allPlayers) {

		Iterator<CurrentDomanda> iter = currentQuestions.iterator();
		while (iter.hasNext()) {
			CurrentDomanda d = iter.next();
			if (d.getRisposte().size() <= 1 && (d.getTipologia().
			        equalsIgnoreCase("diretta") 
			        || d.getTipologia().equalsIgnoreCase("direct"))) {
                iter.remove();
            }
		}
		
		Integer numberOfQuestions = (((Double) Math.floor(((Integer) 
		        currentQuestions.size()).doubleValue() 
		        / ((Integer) allPlayers.size()).doubleValue())).intValue()) 
		        * allPlayers.size();

		List<CurrentDomanda> questions = currentQuestions
		        .subList(0, numberOfQuestions);

		list = new LinkedList<QuestionPlayer>();
		
		while (questions.iterator().hasNext()) {
			for (CurrentPlayer p:allPlayers) {
				if (questions.iterator().hasNext()) {
					list.add(new QuestionPlayer(questions.get(0), p));
					questions.remove(0);
					
				}
			}
		}

	}

	/**
	 * 
	 * @return lista di coppie giocatore-domanda
	 */
	public List<QuestionPlayer> getList() {
		return list;
	}


	/**
	 * @return stringa che descrive l'oggetto
	 */
	public String toString() {
	
		String string = "";
		for (QuestionPlayer qp:list) {
			string = ((Integer) (qp.getQuestion().getTempo()))
			        .toString();
		}

		return string;

	}
	
}


