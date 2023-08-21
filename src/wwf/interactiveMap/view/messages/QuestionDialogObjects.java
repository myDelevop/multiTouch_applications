package wwf.interactiveMap.view.messages;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.interactiveMap.sceneManager.currentGame.CurrentDomanda;
import wwf.interactiveMap.sceneManager.currentGame.CurrentRisposta;
import wwf.interactiveMap.util.AudioTrack;
import wwf.interactiveMap.util.FilesPath;
import wwf.todelete.Fonts;


/**
 * La classe visualizza in un dialogo le domande da mostrare
 * all'utente.
 */
public class QuestionDialogObjects {
    
    /** The pa. */
    private MTApplication pa;
    
    /**  Gestore delle schermate. */
    private InteractiveMapSceneManager mapScene;
    
    
    /** variabile destinata a contenere tutti gli elementi grafici 
     * della scena in questione. */
    private MTRectangle upperLayer;     
    
    /** The sub level. */
    private MTRectangle subLevel;
    
    /** The current domanda. */
    private CurrentDomanda currentDomanda;

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

    /** The answers pane. */
    private MTRectangle answersPane;
    
    /** The answer text. */
    private MTTextArea answerText;
    
    /** The wrong answer. */
    private AudioTrack wrongAnswer;

    /** The correct answer. */
    private AudioTrack correctAnswer;
    
    /** The semi light 24 black. */
    private IFont semiLight24Black;
    
    /** The bold 26 black. */
    private IFont bold26Black;
    
    /** The questions font. */
    private IFont questionsFont;
    
    /** The bold time. */
    private IFont boldTime;
    
    /** The score count. */
    private int scoreCount = 0;

    
    /**
     * Instantiates a new question dialog objects.
     *
     * @param pa the pa
     * @param upperLayer the upper layer
     * @param subLevel the sub level
     * @param mapScene the map scene
     * @param currentDomanda the current domanda
     */
    public QuestionDialogObjects(final MTApplication pa, 
            final MTRectangle upperLayer,
            final MTRectangle subLevel,
            final InteractiveMapSceneManager mapScene, 
            final CurrentDomanda currentDomanda) {

        this.semiLight24Black = Fonts.getInstance(pa).getSemiLight32Black();
        this.bold26Black = Fonts.getInstance(pa).getBold26Black();
        this.questionsFont = Fonts.getInstance(pa).getBold26Black();
        this.boldTime = Fonts.getInstance(pa).getBoldTime();

        
        float startX = upperLayer.getPosition(TransformSpace.GLOBAL).x;
        float startY = upperLayer.getPosition(TransformSpace.GLOBAL).y;
        float widthParent = upperLayer.getWidthXY(TransformSpace.GLOBAL);
        float heightParent = upperLayer.getHeightXY(TransformSpace.GLOBAL);
        
        this.pa = pa;
        this.mapScene = mapScene;
        this.currentDomanda = currentDomanda;
        this.upperLayer = upperLayer;
        this.subLevel = subLevel;

        
        this.upperLayer.setVisible(true);
        this.subLevel.setVisible(true);
        
        this.currentDomanda.setVisualized(true);
        upperLayer.removeAllChildren();
        
        this.wrongAnswer = new AudioTrack(
                FilesPath.SOUNDS_PATH + "wrong.wav", pa);
        this.correctAnswer = new AudioTrack(
                FilesPath.SOUNDS_PATH + "correct.wav", pa);

        

        this.titleBar = new MTRectangle(pa, 
                startX, 
                startY, 
                widthParent, 
                heightParent * 2f / 11f);

        this.centralTitleText = new MTTextArea(pa, bold26Black);
        
        this.questionPane = new MTRectangle(pa, 
                startX, 
                startY + (heightParent * 2f / 11f), 
                widthParent, 
                heightParent * 4f / 11f);

        this.rectWithImage = new MTRectangle(pa,
         
                startX + (widthParent / 2f + 5f), 
                startY + (heightParent * 2f / 11f + 5f), 
                widthParent - (0f + widthParent / 2f + 5f) - 10f, 
                heightParent * 4f / 11f - 10f);

        
        this.cellWithImage = new MTListCell(pa,
                widthParent - (0f + widthParent / 2f + 5f) - 10f, 
                heightParent * 6f / 11f + 325f);
        
        this.questionTextWithImage = new MTTextArea(pa, 
                0f,
                0f,
                widthParent - (0f + widthParent / 2f + 5f) - 10f, 
                heightParent * 6f / 11f + 325f);

        this.cellWithoutImage = new MTListCell(pa,
                widthParent - 0f - 10f, 
                heightParent * 6f / 11f + 25f);
        this.questionTextWithoutImage = new MTTextArea(pa, 
                0f,
                0f,
                widthParent - 0f - 10f, 
                heightParent * 6f / 11f + 25f);

        
        this.answersPane = new MTRectangle(pa,
                startX,
                startY + (heightParent * 6f / 11f),
                widthParent - 0f,
                heightParent * 5f / 11f);
        this.answerText = new MTTextArea(pa, semiLight24Black);
        
        viewQuestion(startX, startY, widthParent, heightParent);
       
    }
    
    
    /**
     * Gets the title bar.
     *
     * @return the title bar
     */
    public final MTRectangle getTitleBar() {
        return titleBar;
    }


    /**
     * Gets the question pane.
     *
     * @return the question pane
     */
    public final MTRectangle getQuestionPane() {
        return questionPane;
    }


    /**
     * Sets the current domanda.
     *
     * @param currentDomanda the new current domanda
     */
    public final void setCurrentDomanda(
            final CurrentDomanda currentDomanda) {
        this.currentDomanda = currentDomanda;
    }
 
    
    /**
     * View question.
     *
     * @param startX the start X
     * @param startY the start Y
     * @param widthParent the width parent
     * @param heightParent the height parent
     */
    private final void viewQuestion(final float startX,
            final float startY, final float widthParent, 
            final float heightParent) {

        upperLayer.addChild(titleBar);
        titleBar.setNoStroke(false);
        titleBar.setNoFill(true);
        titleBar.setFillColor(MTColor.RED);
        titleBar.removeAllGestureEventListeners();
        titleBar.unregisterAllInputProcessors();

        centralTitleText.setNoStroke(true);
        centralTitleText.setNoFill(true);
        
        if(currentDomanda != null) {
            centralTitleText.setText(currentDomanda.getTitolo().toUpperCase());            
        }
        centralTitleText.removeAllGestureEventListeners();
        centralTitleText.unregisterAllInputProcessors();
        centralTitleText.setPositionGlobal(new Vector3D(
                startX + widthParent / 2f, 
                startY + titleBar.getHeightXY(TransformSpace.GLOBAL)/2f));

        titleBar.addChild(centralTitleText);


        boolean withImage = false;
        // TODO controllare valore non esatto
        if(currentDomanda!=null) {
            withImage = currentDomanda.isWithImage();            
        }
        

        upperLayer.addChild(questionPane);
        questionPane.setNoStroke(false);
        questionPane.setNoFill(true);
        questionPane.setFillColor(MTColor.YELLOW);
        questionPane.removeAllGestureEventListeners();
        questionPane.unregisterAllInputProcessors();
        
        
        questionPane.removeAllChildren();
        
        if (withImage) {

            questionPane.addChild(rectWithImage);


            rectWithImage.setNoStroke(true);
            rectWithImage.removeAllGestureEventListeners();
            rectWithImage.unregisterAllInputProcessors();

            cellWithImage.setNoFill(true);
            cellWithImage.setNoStroke(true);
            cellWithImage.setFillColor(MTColor.BLUE);
            rectWithImage.removeAllGestureEventListeners();
            rectWithImage.unregisterAllInputProcessors();

            
            MTList aList = new MTList(pa,
                    startX + 5f, 
                    startY + (heightParent * 2f / 11f + 5f), 
                    widthParent - (widthParent / 2f) - 10f, 
                    heightParent * 4f / 11f - 10f);
            
                questionPane.addChild(aList);


                questionTextWithImage.setFont(questionsFont);
                questionTextWithImage.setNoStroke(true);
                questionTextWithImage.setNoFill(false);

                File file = new File(FilesPath.QUESTIONS_IMAGES_PATH);
                
                if(currentDomanda != null) {
                    String[] imageFile = file.list(new FilenameFilter() {
                        
                        @Override
                        public boolean accept(final File dir,
                                final String name) {
                            return (name.equalsIgnoreCase(currentDomanda
                                    .getTitolo() + ".png"))
                                 || (name.equalsIgnoreCase(currentDomanda
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

                }



                
                
                questionTextWithImage.setTexture(pa.loadImage(
                        FilesPath.ROOT + "textBackground.jpg"));


                if(currentDomanda != null) {
                    questionTextWithImage.setText(currentDomanda.getTesto());
                }
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
                    startX + 5f, 
                    startY + (heightParent * 2f / 11f + 5f), 
                    widthParent - 10f, 
                    heightParent * 4f / 11f - 10f);
                questionPane.addChild(aList);

                questionTextWithoutImage.setFont(questionsFont);
                questionTextWithoutImage.setNoStroke(true);
                questionTextWithoutImage.setNoFill(false);
                questionTextWithoutImage.setTexture(pa.loadImage(
                        FilesPath.ROOT + "textBackground.jpg"));
                if(currentDomanda != null) {
                    questionTextWithoutImage.setText(currentDomanda.getTesto());
                } 
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



        

        upperLayer.addChild(answersPane);
        answersPane.setNoFill(true);
        answersPane.setFillColor(MTColor.GREEN);
        answersPane.setNoStroke(false);
        answersPane.removeAllGestureEventListeners();
        answersPane.unregisterAllInputProcessors();
  

        if(currentDomanda != null) {
            final List<CurrentRisposta> answers = currentDomanda.getRisposte();

            
            
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

            float answersX = startX + ((width / 2f 
                    + padding + 0f) 
                    - (width + padding)); // increase this in for
            float answersY = startY + (height / 2f 
                    + padding + heightParent * 6f / 11f);
            
            int i = 1;
            
            float x = answersX;
            float y = answersY;
            
            for (final CurrentRisposta answer:answers) {
                
                if (i <= numColumns) {
                    x += width + padding;
                    i++;
                } else {
                    i = 2;
                    x = answersX;
                    x += width + padding;
                    y += height + padding;
                }
                
                
                final MTRectangle cell = new MTRectangle(pa, width, height);
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
                                if(answer.isCorretta()) {
                                    correctAnswer.rewind();
                                    correctAnswer.play();
                                }
                                scoreAnswer(answer, cell, answers.size());
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
                                if(answer.isCorretta()) {
                                    correctAnswer.rewind();
                                    correctAnswer.play();
                                }
                                scoreAnswer(answer, cell, answers.size());
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
                    public boolean processGestureEvent(
                            final MTGestureEvent ge) {
                        TapEvent te = (TapEvent) ge;
                        if (te.getTapID() == TapEvent.TAPPED) {
                            if(answer.isCorretta()) {
                                correctAnswer.rewind();
                                correctAnswer.play();
                            }
                            scoreAnswer(answer, cell, answers.size());
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

        }


        upperLayer.setNoFill(false);
        upperLayer.setFillColor(new MTColor(00f, 151f, 167f));


    }
    
    /**
     * Score answer.
     *
     * @param answer the answer
     * @param cell the cell
     * @param numOfAnswers the num of answers
     */
    private void scoreAnswer(final CurrentRisposta answer, 
            final MTRectangle cell, final int numOfAnswers) {
        
        if (answer.isCorretta()) {

            upperLayer.setAnchor(PositionAnchor.LOWER_RIGHT);
            MTImageButton button = new MTImageButton(pa, 
                    pa.loadImage(FilesPath.IMAGES_PATH + "okButton.png"));
            button.setAnchor(PositionAnchor.LOWER_RIGHT);
            button.setNoStroke(true);
            button.scale(0.4f, 0.4f, 1, new Vector3D(940f, 215f, 0),
                    TransformSpace.LOCAL);
            button.translate(new Vector3D(0, 0, 0));
            button.setBoundsBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);

            button.setPositionGlobal(new Vector3D(
                    upperLayer.getPosition(TransformSpace.LOCAL).x,
                    upperLayer.getPosition(TransformSpace.LOCAL).y));
            upperLayer.addChild(button);
            for(MTComponent c: upperLayer.getChildren()) {
                c.setVisible(false);
            }
            button.setVisible(true);

            upperLayer.setAnchor(PositionAnchor.UPPER_LEFT);

            float startX = upperLayer.getPosition(TransformSpace.GLOBAL).x;
            float startY = upperLayer.getPosition(TransformSpace.GLOBAL).y;
            float widthParent = upperLayer.getWidthXY(TransformSpace.GLOBAL);
            float heightParent = upperLayer.getHeightXY(TransformSpace.GLOBAL);
            
            if(currentDomanda.isDescrizioneWithImage()) {
                MTRectangle rect;
                MTListCell cellWithImage;
                MTTextArea descriptionTextWithImage;

                
                rect = new MTRectangle(pa,
                        
                        startX + (widthParent / 2f + 5f), 
                        startY + (5f), 
                        widthParent - (0f + widthParent / 2f + 5f) - 10f, 
                        heightParent * 4f / 5f);
                
                cellWithImage = new MTListCell(pa,
                        widthParent - (0f + widthParent / 2f + 5f) - 10f, 
                        heightParent * 4f / 5f + 325f);
                
                descriptionTextWithImage = new MTTextArea(pa, 
                        0f,
                        0f,
                        widthParent - (0f + widthParent / 2f + 5f) - 10f, 
                        heightParent * 4f / 5f + 325f);
                
                
                upperLayer.addChild(rect);
                
                rect.setNoStroke(true);                 
                rect.removeAllGestureEventListeners();
                rect.unregisterAllInputProcessors();
                cellWithImage.setNoFill(true);
                cellWithImage.setNoStroke(true);

                
                MTList aList1 = new MTList(pa,
                        startX + 5f, 
                        startY + 5f, 
                        widthParent - 10f, 
                        heightParent * 4f / 5f - 10f);
                
                upperLayer.addChild(aList1);


                    descriptionTextWithImage.setFont(questionsFont);
                    descriptionTextWithImage.setNoStroke(true);
                    descriptionTextWithImage.setNoFill(false);

                    File file = new File(FilesPath.QUESTIONS_DESCRIPTIONS_PATH);
                    
                    if(currentDomanda != null) {
                        String[] imageFile = file.list(new FilenameFilter() {
                            
                            @Override
                            public boolean accept(final File dir,
                                    final String name) {
                                return (name.equalsIgnoreCase(currentDomanda
                                        .getTitolo() + ".png"))
                                     || (name.equalsIgnoreCase(currentDomanda
                                             .getTitolo() + ".jpg"));
                            }
                            
                            
                        });
                        if (imageFile.length != 0) {
                            rect.setTexture(pa.loadImage(
                                   FilesPath.QUESTIONS_DESCRIPTIONS_PATH + imageFile[0]));
                        } else {
                            rect.setTexture(pa.loadImage(
                                   FilesPath.QUESTIONS_DESCRIPTIONS_PATH + "nodata.png"));
                        }
                    }
                    
                    descriptionTextWithImage.setTexture(pa.loadImage(
                            FilesPath.ROOT + "textBackground.jpg"));


                    if(currentDomanda != null) {
                        descriptionTextWithImage.setText(currentDomanda.getDescrizioneTestuale());
                    }
                    descriptionTextWithImage.removeAllGestureEventListeners();
                    descriptionTextWithImage.unregisterAllInputProcessors();
                    
                    cellWithImage.addChild(descriptionTextWithImage);
                    aList1.scrollY(descriptionTextWithImage.
                            getFont().getFontAbsoluteHeight());
                    aList1.addListElement(cellWithImage);    
                    aList1.setNoFill(true);
                    aList1.setNoStroke(true);
                    
                //cellWithImage = 
                
            } else {
                MTListCell cellWithoutImage;
                MTTextArea descriptionTextWithoutImage;
                
                cellWithoutImage = new MTListCell(pa,
                        widthParent - 0f - 10f, 
                        heightParent * 4f / 5f + 100f);
                descriptionTextWithoutImage = new MTTextArea(pa, 
                        0f,
                        0f,
                        widthParent - 0f - 10f, 
                        heightParent * 4f / 5f + 100f);
                
                descriptionTextWithoutImage.setFont(questionsFont);
                descriptionTextWithoutImage.setNoStroke(true);
                descriptionTextWithoutImage.setNoFill(false);
                descriptionTextWithoutImage.setTexture(pa.loadImage(
                        FilesPath.ROOT + "textBackground.jpg"));
                if(currentDomanda != null) {
                    descriptionTextWithoutImage.setText(currentDomanda.getDescrizioneTestuale());
                } 
                descriptionTextWithoutImage.removeAllGestureEventListeners();
                descriptionTextWithoutImage.unregisterAllInputProcessors();
                
                cellWithoutImage.setNoFill(true);
                cellWithoutImage.setNoStroke(true);
                cellWithoutImage.addChild(descriptionTextWithoutImage);


                MTList aList = new MTList(pa,
                        startX + 5f, 
                        startY + 5f,
                        widthParent - 10f, 
                        heightParent * 4f / 5f - 10f);
                    upperLayer.addChild(aList);

                aList.scrollY(descriptionTextWithoutImage.getFont()
                        .getFontAbsoluteHeight());
                aList.addListElement(cellWithoutImage);     
                aList.setNoFill(true);
                aList.setNoStroke(true);


            }
            
            button.addGestureListener(TapProcessor.class, 
                    new IGestureEventListener() {
                
                @Override
                public boolean processGestureEvent(
                        final MTGestureEvent ge) {
                    TapEvent te = (TapEvent) ge;
                    if (te.getTapID() == TapEvent.TAPPED) {
                        float score = 0;

                        
                                    if(scoreCount == 0) {
                          score = 10;                
                        }
                        else {
                           score = (float) numOfAnswers / ((float) (scoreCount + 1));
                        }
                        
                         mapScene.getCurrentGameQuestions().getCurrentPlayer().setScore(
                              mapScene.getCurrentGameQuestions().getCurrentPlayer()
                              .getScore() + score);

                                    currentDomanda.getObstacleView().setUnLockStatus();
                        
                                    boolean isUnlocked = true;
                        for (final CurrentDomanda dom : mapScene.getCurrentGameQuestions()
                              .getCurrentQuestions()) {
                          if (dom.getObstacleView().isLock() && !dom.isVisualized()) {
                              isUnlocked = false;
                          }
                        } 
                        upperLayer.getParent().removeChild(upperLayer);
                        upperLayer.removeAllChildren();
                         mapScene.buildMapScene(false, 
                              !isUnlocked);                             

                    }
                    return false;
                }
            });
            
        } else {
            scoreCount++;
            wrongAnswer.rewind();
            wrongAnswer.play();
            cell.setVisible(false);
        }
    }
}
