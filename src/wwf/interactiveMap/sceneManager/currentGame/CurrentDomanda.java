package wwf.interactiveMap.sceneManager.currentGame;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Livello di astrazione per modellare le domande, che si interpone 
 * tra il model e il livello massimo di astrazione.
 * 
 */
public class CurrentDomanda implements Serializable {
    
    
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** lista di tutte le risposte associate alla domanda in questione. */
    private List<CurrentRisposta> risposte = new LinkedList<CurrentRisposta>();
    
    /** ostacolo della domanda. */
    private transient Obstacle obstacle;

    /** titolo della domanda. */
    private String titolo;
    
    /** testo della domanda. */
    private String testo;    
    
    /** indica la descrizione della domanda da visualizzare
     * non appena l'utente risponde alla domanda. */
    private String descrizioneTestuale;
    
    /**  settato a true se la descrizione deve essere affiancata da un'immagine,. */
    private boolean descrizioneWithImage;
    
    /** indica se alla domanda in questione è associato un contenuto 
     * multimediale. */
    private boolean withImage;
    
    /** difficolta della domanda in questione. */
    private String difficolta;

    /** indica se la domanda è stata visualizzata. */
    private boolean isVisualized = false;

    /**
     * Costruttore di default.
     */
    public CurrentDomanda() { }

    /**
     * Costruttore parametrico.
     *
     * @param risposte lista delle risposte associate alla domanda in questione
     * @param obstacle ostacolo
     * @param titolo titolo della domanda in questione
     * @param testo testo della domanda in questione
     * @param descrizioneTestuale descrizione della domanda
     * @param descrizioneWithImage descrizione + immagine
     * @param withImage indica se alla domanda in questione è
     * associato un contenuto multimediale
     */
    public CurrentDomanda(
            final LinkedList<CurrentRisposta> risposte,
            final Obstacle obstacle, final String titolo, 
            final String testo, final String descrizioneTestuale,
            final boolean descrizioneWithImage, 
            final boolean withImage) {
        super();
        this.risposte = risposte;
        this.obstacle = obstacle;
        this.titolo = titolo;
        this.testo = testo;
        this.descrizioneTestuale = descrizioneTestuale;
        this.descrizioneWithImage = descrizioneWithImage;
        this.withImage = withImage;
    }

    /**
     * restituisce tutte le risposte associate alla domanda.
     * @return lista di risposte associate alla domanda
     */
    public final List<CurrentRisposta> getRisposte() {
        return risposte;
    }

    /**
     * setta le risposte associate alla domanda in questione.
     * @param risposte lista di risposte associte alla domanda
     */
    public final void setRisposte(final List<CurrentRisposta> risposte) {
        this.risposte = risposte;
    }

    
    /**
     * restituisce la componente da visualizzare nella mappa.
     * @return componente da visualizzare
     */
    public final Obstacle getObstacleView() {
        return obstacle;
    }

    /**
     * setta l'ostacolo da visualizzare nella mappa.
     * @param obstacle per la relativa domanda
     */
    public final void setObstacleView(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    
    /**
     * restituisce il titolo della domanda.
     * @return titolo della domanda
     */
    public final String getTitolo() {
        return titolo;
    }

    /**
     * setta il titolo della domanda.
     * @param titolo titolo della domanda da settare
     */
    public final void setTitolo(final String titolo) {
        this.titolo = titolo;
    }

    /**
     * restituisce il testo della domanda.
     * @return testo della domanda
     */
    public final String getTesto() {
        return testo;
    }

    /**
     * setta il testo della domanda.
     * @param testo testo della domanda da settare
     */
    public final void setTesto(final String testo) {
        this.testo = testo;
    }

    
    /**
     * Restituisce la descrizione testuale della domanda.
     *
     * @return descrizione testuale
     */
    public String getDescrizioneTestuale() {
        return descrizioneTestuale;
    }

    /**
     * Setta la descrizione testuale della domanda.
     *
     * @param descrizione_testuale descrizione testuale
     */
    public void setDescrizioneTestuale(String descrizioneTestuale) {
        this.descrizioneTestuale = descrizioneTestuale;
    }

    /**
     * indica se un'immagine deve essere affiancata alla descrizione.
     *
     * @return true, descrizione + immagine
     */
    public boolean isDescrizioneWithImage() {
        return descrizioneWithImage;
    }

    /**
     * Sets the descrizione with image.
     *
     * @param descrizione_withImage the new descrizione with image
     */
    public void setDescrizioneWithImage(boolean descrizioneWithImage) {
        this.descrizioneWithImage = descrizioneWithImage;
    }

    /**
     * restituisce true se alla domanda è associato un contenuto multimediale.
     * @return true se all domanda è associato un contenuto multimediale
     */
    public final boolean isWithImage() {
        return withImage;
    }

    /**
     * setta un parametro che indica se alla domanda in questione è associato o 
     * meno un contenuto multimediale.
     * @param withImage indica se la domanda è con contenuto multimediale
     */
    public final void setWithImage(final boolean withImage) {
        this.withImage = withImage;
    }
    
    
    /**
     * restituisce il livello di difficolta della domanda.
     * @return livello di difficolta della domanda
     */
    public final String getDifficolta() {
        return difficolta;
    }

    /**
     * setta il livello di difficolta della domanda in questione.
     * @param difficolta livello difficolta della domanda da settare
     */
    public final void setDifficolta(final String difficolta) {
        this.difficolta = difficolta;
    }

    /**
     * true se la domanda è stata già visualizzata.
     * @return true se la domanda è stata già visualizzata.
     * */
    public final boolean isVisualized() {
        return isVisualized;
    }

    /**
     * Setta isVisualized.
     * @param isVisualized true se la domanda è stata già visualizzata.
     * */
    public final void setVisualized(final boolean isVisualized) {
        this.isVisualized = isVisualized;
    }
    
    
}