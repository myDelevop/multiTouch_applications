package wwf.interactiveMap.view.messages;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;


/**
 * Questa classe gestisce il feedback nel momento in cui l'utente
 * clicca su un ostacolo sbloccato. In particolare visualizza un 
 * messaggio.
 */
public class MessageDialogObjects {
    
    /** Applicazione corrente. */
    private MTApplication pa;
    
    /** Componente principale. */
    private MTComponent root;
    
    /** The title bar. */
    private MTRectangle titleBar;
    
    /** TextArea in cui verrà visualizzato il testo. */
    private MTTextArea text;
    
    /**
     * Costruttore parametrico.
     *
     * @param pa the pa
     * @param root the root
     * @param font the font
     */
    public MessageDialogObjects(final MTApplication pa,
            final MTComponent root, final IFont font) {
        
        this.pa = pa;
        this.root = root;
        
        titleBar = new MTRectangle(pa, 
                0, 
                0, this.pa.width, 143f);
        titleBar.setNoStroke(true);
        titleBar.setNoFill(true);
        titleBar.removeAllGestureEventListeners();
        titleBar.unregisterAllInputProcessors();

        text = new MTTextArea(this.pa, font);

        text.setNoStroke(true);
        text.setNoFill(true);
        text.setText("Sblocca prima i livelli precedenti");
        text.setFillColor(MTColor.BLACK);

        text.removeAllGestureEventListeners();
        text.unregisterAllInputProcessors();

        titleBar.addChild(text);
        this.root.addChild(titleBar);
        
        titleBar.setVisible(false);

    }

    /**
     * Restituisce la titleBar.
     *
     * @return the title bar
     */
    public final MTRectangle getTitleBar() {
        return titleBar;
    }


    
}