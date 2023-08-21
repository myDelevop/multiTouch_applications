package wwf.interactiveMap.view.messages;


/**
 * Thread che permette la visualizzazione delle domande.
 */
public class QuestionThread extends Thread {
    
    /** The objects. */
    private QuestionDialogObjects objects;
    
    /**
     * Instantiates a new question thread.
     *
     * @param objects the objects
     */
    public QuestionThread(final QuestionDialogObjects objects) {
        this.objects = objects;
    }
    
    /** (non-Javadoc).
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {

        //objects.getTitleBar().setVisible(false);
        //objects.getQuestionPane().setVisible(false);
        
        this.interrupt();
    }
}