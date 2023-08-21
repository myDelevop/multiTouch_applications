package wwf.interactiveMap.util.points;

import org.mt4j.MTApplication;


/**
 * Classe che gestisce le proporzioni dell'immagine originale.
 */
public class Propositions {

    /** The pa. */
    private MTApplication pa;
    
    
    /**
     * Instantiates a new propositions.
     *
     * @param pa the pa
     */
    public Propositions(final MTApplication pa) {
        super();
        this.pa = pa;
    }

    /**
     * Restituisce la X in proporzione alla dimensione originale.
     *
     * @param value the value
     * @return the scaled X
     */
    public final Float getScaledX(final Float value) {
        return value * pa.width / 1200f;
    }
    
    /**
     * Restituisce la Y in proporzione alla dimensione originale.
     *
     * @param value the value
     * @return the scaled Y
     */
    public final Float getScaledY(final Float value) {
        return value * pa.height / 665f;
    }
    
}
