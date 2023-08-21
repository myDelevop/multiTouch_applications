/*
 * 
 */
package wwf.interactiveMap.sceneManager.score;

import java.io.Serializable;

/**
 * Giocatore composto da nome e punteggio.
 */
public class Player implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** Nome del giocatore in questione. */
    private String name;
    
    /** Punteggio ottenuto dal giocatore. */
    private float score;
    
    
    /**
     * Costruttore parametrico.
     *
     * @param name nome del giocatore
     * @param score punteggio da esso ottenuto
     */
    public Player(final String name, final float score) {
        super();
        this.name = name;
        this.score = score;
    }
    
    /**
     * Restituisce il nome del giocatore.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Setta il nome del giocatore.
     *
     * @param name nome da settare
     */
    public final void setName(final String name) {
        this.name = name;
    }
    
    /**
     * Restituisce il punteggio del giocatore.
     *
     * @return the score
     */
    public final float getScore() {
        return score;
    }
    
    /**
     * Setta il punteggio del giocatore.
     *
     * @param score punteggio da settare
     */
    public final void setScore(final float score) {
        this.score = score;
    }


    
}
