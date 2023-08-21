/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 C.Ruff, Fraunhofer-Gesellschaft 
 * All rights reserved.
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
package wwf.cranium.sceneManager;

import java.awt.event.KeyEvent;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.widgets.progressBar.MTProgressBar;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;

import wwf.cranium.sceneManager.currentGame.CurrentGameData;
import wwf.cranium.sceneManager.currentGame.CurrentGameQuestions;
import wwf.cranium.sceneManager.currentGame.util.QuestionPlayer;
import wwf.cranium.util.AudioTrack;
import wwf.cranium.util.FilesPath;
import wwf.cranium.view.LevelScene;
import wwf.cranium.view.NumPlayerScene;
import wwf.cranium.view.QuestionScene;
import wwf.cranium.view.ScoreScene;
import wwf.cranium.view.SortScene;
import wwf.cranium.view.SplashScene;

/**
 * Questa classe gestisce le schermate dell'applicazione corrente. Ad ogni
 * schermata provvede anche di salvare i dati che saranno necessari nelle
 * schermate successive
 * 
 */
public class CraniumSceneManager extends AbstractScene {

    /** The mt application. */
    private MTApplication mtApplication;

    /** The canvas. */
    private MTCanvas canvas;

    /** The progress bar. */
    private MTProgressBar progressBar;

    /** The is italian. */
    private boolean isItalian;

    /** The num players. */
    private Integer numPlayers;

    /** The level id. */
    private Integer levelId;

    /** The is random. */
    private boolean isRandom;

    /** The current game questions. */
    private CurrentGameQuestions currentGameQuestions = null;

    /** The current game data. */
    private CurrentGameData currentGameData = null;

    /** The audio tap buttons. */
    private AudioTrack audioTapButtons;


    /** indica se c'è un aggiornamento nel dB. */
    private boolean isUpdateDB = false;

    /**
     * Costruttore parametrico, nel costruttore viene visualizzata la prima
     * schermata di splash.
     *
     * @param mtApplication
     *            the mt application
     * @param name
     *            the name
     */
    public CraniumSceneManager(final MTApplication mtApplication, 
            final String name) {
        super(mtApplication, name);
        this.mtApplication = mtApplication;


        this.progressBar = new MTProgressBar(mtApplication,
                mtApplication.createFont("arial", 18));

        this.progressBar.setDepthBufferDisabled(true);
        this.progressBar.setVisible(false);
        this.setClearColor(new MTColor(00f, 151f, 167f));

        this.isItalian = false; // default values
        this.numPlayers = 4;
        this.levelId = 2;
        this.isRandom = true;

        this.audioTapButtons = new AudioTrack(FilesPath.SOUNDS_PATH 
                + "tapButton.wav", mtApplication);
        this.canvas = getCanvas();
        this.canvas.addChild(progressBar);


        canvas.removeComponentChildren(canvas, 0);

        SplashScene sp = new SplashScene(this, mtApplication);

        canvas.addChild(sp.getRootComponent());
    }

    /**
     * Gets the audio tap buttons.
     *
     * @return the audio tap buttons
     */
    public final AudioTrack getAudioTapButtons() {
        return audioTapButtons;
    }

    /**
     * Questo metodo svuota il canvas e lo riempie di componenti relativi alla
     * view NumPlayerScene.
     */
    public final void buildNumPlayersScene() {
        canvas.removeComponentChildren(canvas, 0);
        NumPlayerScene np = new NumPlayerScene(this, mtApplication);
        canvas.addChild(np.getRootComponent());
    }

    /**
     * Questo metodo svuota il canvas e lo riempie di componenti relativi alla
     * view LevelScene.
     */
    public final void buildSelectLevelScene() {
        canvas.removeComponentChildren(canvas, 0);
        LevelScene lp = new LevelScene(this, mtApplication);
        canvas.addChild(lp.getRootComponent());
    }

    /**
     * Il metodo avvia un thread che carica le domande effettive del gioco e
     * visualizza la progress bar durante il caricamento, dopo di che viene
     * chiamato il metodo che permette la visualizzazione di QuestionScene.
     *
     * @see CraniumSceneManager#buildQuestionScene(ThreadLoadGame).
     */
    public final void questionLoader() {
        canvas.removeComponentChildren(canvas, 0);
        canvas.addChild(progressBar);

        ThreadLoadGame loader = new ThreadLoadGame(
                this, isItalian, numPlayers, levelId, isRandom, 300);
        progressBar.setProgressInfoProvider(loader);
        progressBar.setVisible(true);

        loader.start();

    }

    /**
     * Questo metodo svuota il canvas e lo riempie di componenti relativi alla
     * view QuestionScene.
     * 
     * Il metodo viene chiamato non appena le domande sono state caricate da un
     * thread
     *
     * @param thread            the thread
     * @see CraniumSceneManager#questionLoader()
     */
    public final void buildQuestionScene(final ThreadLoadGame thread) {

        // canvas.removeAllChildren();

        thread.setCurrent(1);
        if (currentGameQuestions == null) {
            currentGameQuestions = new CurrentGameQuestions(
                    levelId, numPlayers, isItalian, isRandom, isUpdateDB);
        }

        thread.setCurrent(2);
        if (currentGameData == null) {
            currentGameData = new CurrentGameData(
                    numPlayers, isItalian, isUpdateDB);
        }

        thread.setCurrent(3);
        QuestionScene lp = new QuestionScene(
                this, mtApplication, currentGameQuestions, currentGameData);
        thread.setCurrent(4);
        canvas.addChild(lp.getRootComponent());

    }

    /**
     * Questo metodo svuota il canvas e lo riempie di componenti relativi alla
     * view SortScene.
     */
    public final void buildSortScene() {
        canvas.removeComponentChildren(canvas, 0);
        SortScene sp = new SortScene(this, mtApplication);
        canvas.addChild(sp.getRootComponent());
    }

    /**
     * Questo metodo svuota il canvas e lo riempie di componenti relativi alla
     * view ScoreScene, necessita di dati per visualizzare la classifica dei
     * giocatori.
     * 
     * @param saveDataScore
     *            punteggio dei giocatori
     */
    public final void buildScoreScene(
            final List<QuestionPlayer> saveDataScore) {
        canvas.removeComponentChildren(canvas, 0);
        ScoreScene sp = new ScoreScene(
                this, mtApplication, saveDataScore, numPlayers);
        canvas.addChild(sp.getRootComponent());
    }


    /**
     * Gets the mt application.
     *
     * @return the mt application
     */
    public final MTApplication getMtApplication() {
        return mtApplication;
    }

    /**
     * Sets the mt application.
     *
     * @param mtApplication
     *            the new mt application
     */
    public final void setMtApplication(final MTApplication mtApplication) {
        this.mtApplication = mtApplication;
    }

    /**
     * Checks if is italian.
     *
     * @return true, if is italian
     */
    public final boolean isItalian() {
        return isItalian;
    }

    /**
     * Sets the italian.
     *
     * @param isItalian
     *            the new italian
     */
    public final void setItalian(final boolean isItalian) {
        this.isItalian = isItalian;
    }

    /**
     * Gets the num players.
     *
     * @return the num players
     */
    public final Integer getNumPlayers() {
        return numPlayers;
    }

    /**
     * Sets the num players.
     *
     * @param numPlayers
     *            the new num players
     */
    public final void setNumPlayers(final Integer numPlayers) {
        this.numPlayers = numPlayers;
    }

    /**
     * Gets the level id.
     *
     * @return the level id
     */
    public final Integer getLevelId() {
        return levelId;
    }

    /**
     * Sets the level id.
     *
     * @param levelId
     *            the new level id
     */
    public final void setLevelId(final Integer levelId) {
        this.levelId = levelId;
    }

    /**
     * Checks if is random.
     *
     * @return true, if is random
     */
    public final boolean isRandom() {
        return isRandom;
    }

    /**
     * Sets the random.
     *
     * @param isRandom
     *            the new random
     */
    public final void setRandom(final boolean isRandom) {
        this.isRandom = isRandom;
    }

    /**
     * Gets the current game questions.
     *
     * @return the current game questions
     */
    public final CurrentGameQuestions getCurrentGameQuestions() {
        return currentGameQuestions;
    }

    /**
     * Gets the current game data.
     *
     * @return the current game data
     */
    public final CurrentGameData getCurrentGameData() {
        return currentGameData;
    }

    /**
     * Sets the current game questions.
     *
     * @param currentGameQuestions
     *            the new current game questions
     */
    public final void setCurrentGameQuestions(
            final CurrentGameQuestions currentGameQuestions) {
        this.currentGameQuestions = currentGameQuestions;
    }

    /**
     * Sets the current game data.
     *
     * @param currentGameData
     *            the new current game data
     */
    public final void setCurrentGameData(
            final CurrentGameData currentGameData) {
        this.currentGameData = currentGameData;
    }


    /**
     * Checks if is update DB.
     *
     * @return true, if is update DB
     */
    public final boolean isUpdateDB() {
        return isUpdateDB;
    }

    /**
     * Sets the update DB.
     *
     * @param isUpdateDB the new update DB
     */
    public final void setUpdateDB(final boolean isUpdateDB) {
        this.isUpdateDB = isUpdateDB;
    }

    /**
     * On enter.
     *
     * @see org.mt4j.sceneManagement.AbstractScene#onEnter()
     */
    public final void onEnter() {
        getMTApplication().registerKeyEvent(this);
    }

    /**
     * On leave.
     *
     * @see org.mt4j.sceneManagement.AbstractScene#onLeave()
     */
    public final void onLeave() {
        getMTApplication().unregisterKeyEvent(this);
    }

    /**
     * Key event.
     *
     * @param e
     *            the e
     */
    public final void keyEvent(final KeyEvent e) {
        int evtID = e.getID();
        if (evtID != KeyEvent.KEY_PRESSED) {
            return;
        }
        switch (e.getKeyCode()) {
        case KeyEvent.VK_F:
            System.out.println("FPS: " + mtApplication.frameRate);
            break;
        default:
            break;
        }
    }

}
