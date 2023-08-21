package wwf.interactiveMap.data.model;


// TODO: Auto-generated Javadoc
/**
 * Model della tipologia di domande.
 */
public class Domanda {

    /** id della domanda. */
    private Integer id;
    
    /** titolo della domanda. */
    private String titolo;
    
    /** testo della domanda. */
    private String domanda;
    
    /** descrizione finale della domanda. */
    private String descrizione;
    
    /** indica se la descrizione dovra essere affiancata da un'immagine. */
    private String descrizione_withImage;

    /** id difficolta della domanda in questione. */
    private Integer difficoltaId;
    
    
    /** numero che rappresenta l'ordine di erogazione delle domande. */
    private Integer numeroDomanda;

    /** Indica se è presente un contenuto multimediale. */
    private String contenutoMultimediale;

    /**
     * Costrutture di default.
     */
    public Domanda() { }

    /**
     * Costruttore parametrico.
     *
     * @param titolo titolo della domanda
     * @param domanda testo della domanda
     * @param descrizione descrizione della domanda
     * @param descrizione_withImage indica se la descrizione con immagine
     * @param difficoltaId id difficolta della domanda in questione
     *  questione
     * @param contenutoMultimediale indica se è presente un contenuto 
     * multimediale
     * @param numeroDomanda numero che rappresenta l'ordine di erogazione
     *  delle domande
     */
    public Domanda(final String titolo, final String domanda, 
            final String descrizione,  final String 
            descrizione_withImage, final Integer difficoltaId,
            final String contenutoMultimediale, final Integer numeroDomanda) {
        super();
        this.titolo = titolo;
        this.difficoltaId = difficoltaId;
        this.domanda = domanda;
        this.descrizione = descrizione;
        this.descrizione_withImage = descrizione_withImage;
        this.contenutoMultimediale = contenutoMultimediale;
        this.numeroDomanda = numeroDomanda;
    }

    /**
     * restituisce il numero che indica l'ordine di erogazione delle domande.
     * @return numero ordine di erogazione delle domande
     */
    public final Integer getNumeroDomanda() {
        return numeroDomanda;
    }

    /**
     * setta il numero che indica l'ordine di erogazione delle domande.
     * @param numeroDomanda numero ordine di erogazione delle domande
     */
    public final void setNumeroDomanda(final Integer numeroDomanda) {
        this.numeroDomanda = numeroDomanda;
    }

    /**
     * restituisce l'id della domanda.
     * @return id della domanda
     */
    public final Integer getId() {
        return id;
    }

    /**
     * setta l'id della domanda.
     * @param id id da settare
     */
    public final void setId(final Integer id) {
        this.id = id;
    }

    /**
     * restituisce il titolo della domanda.
     * @return titolo domanda
     */
    public final String getTitolo() {
        return titolo;
    }

    /**
     * setta il titolo della domanda.
     * @param titolo titolo da settare
     */
    public final void setTitolo(final String titolo) {
        this.titolo = titolo;
    }

    /**
     * restituisce il testo della domanda.
     * @return testo della domanda
     */
    public final String getDomanda() {
        return domanda;
    }

    /**
     * setta il testo della domanda.
     * @param domanda testo della domanda da settare
     */
    public final void setDomanda(final String domanda) {
        this.domanda = domanda;
    }

    
    /**
     * Restituisce la descrizione della domanda.
     *
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione della domanda.
     *
     * @param descrizione della domanda
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * indica se la descrizione deve essere affiancata da un immagine.
     *
     * @return true se descrizione con immagine
     */
    public String getDescrizione_withImage() {
        return descrizione_withImage;
    }

    /**
     * setta il parametro booleano.
     *
     * @param descrizione_withImage true se descrizione + immagine
     */
    public void setDescrizione_withImage(String descrizione_withImage) {
        this.descrizione_withImage = descrizione_withImage;
    }

    
    /**
     * restituisce l'id della difficolta della domanda in questione.
     * @return id difficolta
     */
    public final Integer getDifficoltaId() {
        return difficoltaId;
    }

    /**
     * setta l'id di difficolta della domanda in questione.
     * @param difficoltaId id difficolta da settare
     */
    public final void setDifficoltaId(final Integer difficoltaId) {
        this.difficoltaId = difficoltaId;
    }

    /**
     * restituisce un numero che indica se la domanda ha un 
     * contenuto multimediale.
     * @return numero che indica se la domanda ha un contenuto
     *  multimediale
     */
    public final String getContenutoMultimediale() {
        return contenutoMultimediale;
    }

    /**
     * setta il numero che indica se la domanda ha un contenuto multimediale.
     * @param contenutoMultimediale numero contenuto multimediale
     */
    public final void setContenutoMultimediale(final String 
            contenutoMultimediale) {
        this.contenutoMultimediale = contenutoMultimediale;
    }

}
