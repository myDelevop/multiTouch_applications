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
package wwf.interactiveMap.sceneManager;

import java.awt.event.KeyEvent;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTOverlayContainer;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.DefaultRotateAction;
import org.mt4j.input.gestureAction.DefaultScaleAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.util.MTColor;

import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.interactiveMap.sceneManager.currentGame.CurrentGameQuestions;
import wwf.interactiveMap.util.AudioTrack;
import wwf.interactiveMap.util.FilesPath;
import wwf.interactiveMap.util.WaterSceneBackground;
import wwf.interactiveMap.view.LevelScene;
import wwf.interactiveMap.view.MapScene;
import wwf.interactiveMap.view.ScoreScene;
import wwf.interactiveMap.view.SplashScene;

// TODO: Auto-generated Javadoc
/**
 * Questa classe gestisce le schermate dell'applicazione corrente. 
 * Ad ogni schermata provvede anche di salvare i dati che saranno 
 * necessari nelle schermate successive
 * 
 */
public class InteractiveMapSceneManager extends WaterSceneBackground {

    /** The mt application. */
    private MTApplication mtApplication;

    /** The canvas. */
    private MTCanvas canvas;

    /** The level id. */
    private Integer levelId;

    /** The water components. */
    private MTComponent[] waterComponents;
    
    /** The is italian. */
    private boolean isItalian;
    
    /** The is random. */
    private boolean isRandom;

    /** The current game questions. */
    private CurrentGameQuestions currentGameQuestions = null;
    
    /** The not clickable. */
    private AudioTrack notClickable;

    /** The tap button. */
    private AudioTrack tapButton;

    /** The is last answer. */
    private boolean isLastAnswer = false;

    /** The is update DB. */
    private boolean isUpdateDB = false;

    /**
     * Checks if is last answer.
     *
     * @return true, if is last answer
     */
    public final boolean isLastAnswer() {
        return isLastAnswer;
    }


    /**
     * Sets the last answer.
     *
     * @param isLastAnswer the new last answer
     */
    public final void setLastAnswer(final boolean isLastAnswer) {
        this.isLastAnswer = isLastAnswer;
    }


    /**
     * Costruttore parametrico, nel costruttore viene visualizzata la prima 
     * schermata di splash.
     *
     * @param mtApplication the mt application
     * @param name the name
     */
    public InteractiveMapSceneManager(final MTApplication mtApplication,
            final String name) {
        super(mtApplication, name);
        
        this.mtApplication = mtApplication;
        this.notClickable = new AudioTrack(FilesPath.SOUNDS_PATH 
                + "notClickable.wav", mtApplication);
        this.tapButton = new AudioTrack(FilesPath.SOUNDS_PATH 
                + "tapButton.wav", mtApplication);
        this.setClearColor(new MTColor(00f, 151f, 167f));

        this.isItalian = false; //default values
        this.isRandom = true;
        this.canvas = getCanvas();
        this.setItalian(true);

        
        
        IGestureEventListener[] list = canvas.getChildren()[0]
                .getGestureListeners(); 

        IGestureEventListener listener = null;

        for (IGestureEventListener l:list) {
            if (!(l instanceof DefaultDragAction)) {
                if (!(l instanceof DefaultScaleAction)) {
                    if (!(l instanceof DefaultRotateAction)) {
                        listener = l;
                    }   
                    
                }
                
            }
        }
        
        MTRectangle rect = new MTRectangle(mtApplication, 
                0,
                0,
                this.mtApplication.width,
                this.mtApplication.height);
        canvas.addChild(rect);
        rect.setNoFill(true);
        rect.removeAllGestureEventListeners();
        rect.unregisterAllInputProcessors();
        rect.registerInputProcessor(new MultipleDragProcessor(mtApplication));
        rect.addGestureListener(MultipleDragProcessor.class, listener);

        this.waterComponents = canvas.getChildren();

        canvas.removeComponentChildren(canvas, 0);
        
        SplashScene sp = new SplashScene(this, mtApplication);
        canvas.addChild(sp.getRootComponent());
    }
    

    /**
     * Builds the level scene.
     *
     * @param playerName the player name
     */
    public final void buildLevelScene(final String playerName) {
        canvas.removeComponentChildren(canvas, 0);
        LevelScene ls = new LevelScene(this, mtApplication, playerName);
        canvas.addChild(ls.getRootComponent());
    }


    /**
     * Builds the score scene.
     */
    public final void buildScoreScene() {
        canvas.removeComponentChildren(canvas, 0);
        ScoreScene ls = new ScoreScene(this, mtApplication);
        canvas.addChild(ls.getRootComponent());
    }

    /**
     * Questo metodo svuota il canvas e lo riempie di componenti relativi alla
     * view QuestionScene.
     * 
     * Il metodo viene chiamato non appena le domande sono state caricate da un 
     * thread
     *
     * @param isFirst the is first
     * @param check the check
     * @see CraniumSceneManager#questionLoader()
     */
    public final void buildMapScene(final boolean isFirst,
            final boolean check) {

        if (isFirst) {
            canvas.removeComponentChildren(canvas, 0);            
        } else {
            for (MTComponent c:canvas.getChildren()) {
                if (!(c instanceof MTOverlayContainer)) {
                    c.removeAllChildren();                    
                }
            }

        }
        
        canvas.addChildren(waterComponents);
        MapScene ms = new MapScene(this, mtApplication, check);
        canvas.addChild(ms.getRootComponent());
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
     * @param levelId the new level id
     */
    public final void setLevelId(final Integer levelId) {
        this.levelId = levelId;
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
     * Sets the current game questions.
     *
     * @param currentGameQuestions the new current game questions
     */
    public final void setCurrentGameQuestions(
            final CurrentGameQuestions currentGameQuestions) {
        this.currentGameQuestions = currentGameQuestions;
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
     * @param isItalian the new italian
     */
    public final void setItalian(final
            boolean isItalian) {
        this.isItalian = isItalian;
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
     * Gets the not clickable.
     *
     * @return the not clickable
     */
    public final AudioTrack getNotClickable() {
        return notClickable;
    }


    /**
     * Gets the tap button.
     *
     * @return the tap button
     */
    public final AudioTrack getTapButton() {
        return tapButton;
    }

    /** (non-Javadoc).
     * @see org.mt4j.sceneManagement.AbstractScene#onEnter()
     */
    public final void onEnter() {
        getMTApplication().registerKeyEvent(this);
    }

    /** (non-Javadoc).
     * @see org.mt4j.sceneManagement.AbstractScene#onLeave()
     */
    public final void onLeave() {
        getMTApplication().unregisterKeyEvent(this);
    }

    /** (non-Javadoc).
     * @see wwf.interactiveMap.util.WaterSceneBackground#keyEvent
     * (java.awt.event.KeyEvent)
     * @param e e
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
