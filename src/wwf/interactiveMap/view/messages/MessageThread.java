package wwf.interactiveMap.view.messages;


/**
 * Thread che visualizza il messaggio.
 */
public class MessageThread extends Thread {
    
    /** The objects. */
    private MessageDialogObjects objects;
    
    /**
     * Instantiates a new message thread.
     *
     * @param objects the objects
     */
    public MessageThread(final MessageDialogObjects objects) {
        this.objects = objects;
    }
    
    /** (non-Javadoc).
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {

        objects.getTitleBar().setVisible(true);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        objects.getTitleBar().setVisible(false);
        
        this.interrupt();
    }
}