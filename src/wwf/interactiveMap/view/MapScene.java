package wwf.interactiveMap.view;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.interactiveMap.data.SerializeObjects;
import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.interactiveMap.sceneManager.currentGame.CurrentDomanda;
import wwf.interactiveMap.util.FilesPath;
import wwf.interactiveMap.util.points.Propositions;
import wwf.interactiveMap.view.messages.MessageDialogObjects;
import wwf.interactiveMap.view.messages.MessageThread;
import wwf.interactiveMap.view.messages.QuestionDialogObjects;
import wwf.todelete.Fonts;


/**
 * Questa classe inizializza la componente {@link #root} di elementi grafici che
 * consentono la visualizzazione della QuestionScene.
 */
public class MapScene {

    /** The pa. */
    private MTApplication pa;

    /**
     * variabile destinata a contenere tutti gli elementi grafici della scena in
     * questione.
     */
    private MTComponent root;

    /** The sub level. */
    private MTRectangle subLevel;

    /** The upper level. */
    private MTRectangle upperLevel;

    /** Gestore delle schermate. */
    private InteractiveMapSceneManager mapScene;

    /** Font seguisb 32 white. */
    private IFont seguisb32White;

    /** Font seguisb 36 red. */
    private IFont seguisb36Red;

    /** The message objects. */
    private MessageDialogObjects messageObjects;

    /** The score button. */
    private MTRectangle scoreButton;
    
    /**
     * Costruttore parametrico che provvede all'inizializzazione della
     * componente {@link #root}.
     *
     * @param mapScene the map scene
     * @param pa            the pa
     * @param isFirst the is first
     */
    public MapScene(final InteractiveMapSceneManager mapScene,
            final MTApplication pa, final boolean isFirst) {

        float padding = 60f;

        root = new MTComponent(pa);
        
        this.subLevel = new MTRectangle(pa, 0, 0, pa.width, pa.height);
        this.upperLevel = new MTRectangle(pa, padding, padding, pa.width 
                - padding * 2f, pa.height - padding * 2f);

        this.upperLevel.setAnchor(PositionAnchor.UPPER_LEFT);
        this.upperLevel.setStrokeColor(MTColor.BLACK);
        this.seguisb32White = Fonts.getInstance(pa).getSeguisb32White();
        this.seguisb36Red = Fonts.getInstance(pa).getSeguisb36Red();
        this.mapScene = mapScene;
        this.pa = pa;

        List<CurrentDomanda> allQuestions 
        = mapScene.getCurrentGameQuestions().getCurrentQuestions();

        int numberOfQuestions = allQuestions.size();
        
        if(numberOfQuestions >= 12) {
            allQuestions = allQuestions.subList(0, 12);
            numberOfQuestions = 12;
        } else if(numberOfQuestions >= 9) {
            allQuestions = allQuestions.subList(0, 9);
            numberOfQuestions = 9;
        } else if(numberOfQuestions >= 6) {
            allQuestions = allQuestions.subList(0, 6);
            numberOfQuestions = 6;
        } 

        if(numberOfQuestions < 6) {
            MTTextArea text = new MTTextArea(pa, seguisb36Red);
            text.setNoStroke(true);
            text.setNoFill(true);
            text.setText("INSERISCI ALMENO 6 DOMANDE NEL DATABASE");
            text.removeAllGestureEventListeners();
            text.unregisterAllInputProcessors();
            text.setPositionGlobal(new Vector3D(pa.width / 2f, 120f));

            root.addChild(text);
            return;
        }

        MTBackgroundImage background;

        if (isFirst) {
            background = new MTBackgroundImage(pa, 
            pa.loadImage(FilesPath.IMAGES_PATH + "Mappa Gioco Musa_inizio.png"), false);            
        } else {
            background = new MTBackgroundImage(pa, pa.loadImage(
            FilesPath.IMAGES_PATH + "Mappa Gioco Musa_fine.png"), false);            
        }

        Propositions prop = new Propositions(pa);
        CurrentDomanda dom;

        // SET POSITION
        switch (numberOfQuestions) {
        case 6: 
            dom = allQuestions.get(0);
            dom.getObstacleView().setPosition(prop.getScaledX(82.5f),
                    prop.getScaledY(601.5f));
            dom = allQuestions.get(1);
            dom.getObstacleView().setPosition(prop.getScaledX(409.5f),
                    prop.getScaledY(361.5f));
            dom = allQuestions.get(2);
            dom.getObstacleView().setPosition(prop.getScaledX(702.0f),
                    prop.getScaledY(207.0f));
            dom = allQuestions.get(3);
            dom.getObstacleView().setPosition(prop.getScaledX(775.5f),
                    prop.getScaledY(352.5f));
            dom = allQuestions.get(4);
            dom.getObstacleView().setPosition(prop.getScaledX(789.0f),
                    prop.getScaledY(207.0f));
            dom = allQuestions.get(5);
            dom.getObstacleView().setPosition(prop.getScaledX(817.5f),
                    prop.getScaledY(141.0f));

            break;

        case 9:
            dom = allQuestions.get(0);
            dom.getObstacleView().setPosition(prop.getScaledX(82.5f),
                    prop.getScaledY(610.5f));
            dom = allQuestions.get(1);
            dom.getObstacleView().setPosition(prop.getScaledX(223.5f),
                    prop.getScaledY(549f));
            dom = allQuestions.get(2);
            dom.getObstacleView().setPosition(prop.getScaledX(409.5f),
                    prop.getScaledY(361.5f));
            dom = allQuestions.get(3);
            dom.getObstacleView().setPosition(prop.getScaledX(591.0f),
                    prop.getScaledY(265.5f));
            dom = allQuestions.get(4);
            dom.getObstacleView().setPosition(prop.getScaledX(702.0f),
                    prop.getScaledY(207.0f));
            dom = allQuestions.get(5);
            dom.getObstacleView().setPosition(prop.getScaledX(775.5f),
                    prop.getScaledY(352.5f));
            dom = allQuestions.get(6);
            dom.getObstacleView().setPosition(prop.getScaledX(789.0f),
                    prop.getScaledY(273.0f));
            dom = allQuestions.get(7);
            dom.getObstacleView().setPosition(prop.getScaledX(789.0f),
                    prop.getScaledY(207.0f));
            dom = allQuestions.get(8);
            dom.getObstacleView().setPosition(prop.getScaledX(817.5f),
                    prop.getScaledY(141.0f));

            break;

        case 12:
            dom = allQuestions.get(0);
            dom.getObstacleView().setPosition(prop.getScaledX(82.5f),
                    prop.getScaledY(610.5f));
            dom = allQuestions.get(1);
            dom.getObstacleView().setPosition(prop.getScaledX(223.5f),
                    prop.getScaledY(549.0f));
            dom = allQuestions.get(2);
            dom.getObstacleView().setPosition(prop.getScaledX(358.5f),
                    prop.getScaledY(424.5f));
            dom = allQuestions.get(3);
            dom.getObstacleView().setPosition(prop.getScaledX(409.5f),
                    prop.getScaledY(361.5f));
            dom = allQuestions.get(4);
            dom.getObstacleView().setPosition(prop.getScaledX(498.0f),
                    prop.getScaledY(295.5f));
            dom = allQuestions.get(5);
            dom.getObstacleView().setPosition(prop.getScaledX(591.0f),
                    prop.getScaledY(265.5f));
            dom = allQuestions.get(6);
            dom.getObstacleView().setPosition(prop.getScaledX(702.0f),
                    prop.getScaledY(207.0f));
            dom = allQuestions.get(7);
            dom.getObstacleView().setPosition(prop.getScaledX(760.5f),
                    prop.getScaledY(465.0f));
            dom = allQuestions.get(8);
            dom.getObstacleView().setPosition(prop.getScaledX(775.5f),
                    prop.getScaledY(352.5f));
            dom = allQuestions.get(9);
            dom.getObstacleView().setPosition(prop.getScaledX(789.0f),
                    prop.getScaledY(273.0f));
            dom = allQuestions.get(10);
            dom.getObstacleView().setPosition(prop.getScaledX(789.0f),
                    prop.getScaledY(207.0f));
            dom = allQuestions.get(11);
            dom.getObstacleView().setPosition(prop.getScaledX(817.5f),
                    prop.getScaledY(141.0f));
            break;

        default:
            break;
        }


        root.addChild(background);

        this.messageObjects = new MessageDialogObjects(
                pa, root, seguisb32White);

        MTComponent o = null;
        List<CurrentDomanda> previousList = null;

        for (final CurrentDomanda d : allQuestions) {
            previousList = new ArrayList<CurrentDomanda>();
            o = d.getObstacleView().getObstacle();
            root.addChild(o);

            for (final CurrentDomanda temp : allQuestions) {
                if (temp == d) {
                    break;
                }
                previousList.add(temp);
            }

            boolean checkPrevious = true;
            for (CurrentDomanda temp : previousList) {
                if (temp.getObstacleView().isLock()) {
                    checkPrevious = false;
                    break;
                }
            }

            if (d.getObstacleView().isLock() && checkPrevious) {
               ((MTRectangle) o.getChildByName("upperLayer")).setNoFill(true);
               ((MTRectangle) o.getChildByName("upperLayer")).setNoStroke(true);

                for (MTComponent c : o.getChildren()) {
                    c.unregisterAllInputProcessors();
                    c.removeAllGestureEventListeners();
                    c.registerInputProcessor(new TapProcessor(pa));
                    c.addGestureListener(TapProcessor.class,
                            new IGestureEventListener() {
                        @Override
                        public boolean processGestureEvent(
                                final MTGestureEvent ge) {
                            TapEvent te = (TapEvent) ge;
                            if (te.getTapID() == TapEvent.TAPPED) {
                                mapScene.getTapButton().rewind();
                                mapScene.getTapButton().play();

                                QuestionDialogObjects o 
                                = new QuestionDialogObjects(pa, upperLevel, 
                                        subLevel, mapScene, d);
                            }
                            return false;
                        }
                    });
                }
            } else if (d.getObstacleView().isLock()) {
                for (MTComponent c : o.getChildren()) {
                    c.unregisterAllInputProcessors();
                    c.removeAllGestureEventListeners();
                    c.registerInputProcessor(new TapProcessor(pa));
                    c.addGestureListener(TapProcessor.class, 
                            new IGestureEventListener() {
                        @Override
                        public boolean processGestureEvent(
                                final MTGestureEvent ge) {
                            TapEvent te = (TapEvent) ge;
                            if (te.getTapID() == TapEvent.TAPPED) {
                                MessageThread dialogThread 
                                = new MessageThread(messageObjects);

                                mapScene.getNotClickable().rewind();
                                mapScene.getNotClickable().play();
                                dialogThread.start();
                            }
                            return false;
                        }
                    });
                }
            }
        }

        this.subLevel.unregisterAllInputProcessors();
        this.subLevel.removeAllGestureEventListeners();
        this.subLevel.setNoStroke(true);
        this.subLevel.setFillColor(new MTColor(45f, 45f, 45f, 180f));
        this.subLevel.setVisible(false);

        this.upperLevel.setVisible(false);

        root.addChild(subLevel);
        root.addChild(upperLevel);

        System.out.println("2 " + isFirst);

        if (!isFirst) {
            
            scoreButton = new MTRectangle(pa, pa.loadImage(FilesPath.IMAGES_PATH
                    + "okButton.png"));
            scoreButton.setVisible(true);
            scoreButton.setNoStroke(true);
            scoreButton.setAnchor(PositionAnchor.LOWER_RIGHT);
            scoreButton.setPositionGlobal(new Vector3D(pa.width, pa.height));
            
            scoreButton.unregisterAllInputProcessors();
            scoreButton.removeAllGestureEventListeners();
            scoreButton.registerInputProcessor(new TapProcessor(pa));
            scoreButton.addGestureListener(TapProcessor.class, 
                    new IGestureEventListener() {
                @Override
                public boolean processGestureEvent(
                        final MTGestureEvent ge) {
                    TapEvent te = (TapEvent) ge;
                    if (te.getTapID() == TapEvent.TAPPED) {
                        mapScene.getTapButton().rewind();
                        mapScene.getTapButton().play();

                        root.removeAllChildren();
                        mapScene.buildScoreScene();

                    }
                    return false;
                }
            });

            root.addChild(scoreButton);
            
            SerializeObjects s = new SerializeObjects(pa);
            s.start();

        }

        

    }

    /**
     * Restituisce la radice degli elementi grafici che costituiscono la scena
     * in questione.
     *
     * @return component root
     */
    public final MTComponent getRootComponent() {
        return root;
    }

}
