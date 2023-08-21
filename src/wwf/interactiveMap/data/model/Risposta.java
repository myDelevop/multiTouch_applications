package wwf.interactiveMap.data.model;

/**
 * Model della risposta.
 */
public class Risposta {
    
    /** id della risposta. */
    private Integer id;
    
    /** testo della risposta. */
    private String testoRisposta;
    
    /** indica se la risposta è corretta. */
    private String corretta;
    
    /** id domanda della risposta in questione. */
    private Integer domandaId;

    
    /**
     * Costrutture di default.
     */
    public Risposta() { }
    
    /**
     * Costruttore parametrico.
     *
     * @param testoRisposta testo della risposta
     * @param corretta indica se la risosta è corretta
     * @param domandaId id della domanda in questione
     */
    public Risposta(final String testoRisposta, 
            final String corretta, final Integer domandaId) {
        super();
        this.testoRisposta = testoRisposta;
        this.corretta = corretta;
        this.domandaId = domandaId;
    }

    /**
     * restituisce l'id della tipologia.
     * @return id della tipologia
     */
    public final Integer getId() {
        return id;
    }

    /**
     * setta l'id della tipologia.
     * @param id id da settare
     */
    public final void setId(final Integer id) {
        this.id = id;
    }

    /**
     * restituisce il testo della risposta.
     * @return testo della risposta
     */
    public final String getTestoRisposta() {
        return testoRisposta;
    }

    /**
     * setta il testo della risposta.
     * @param testoRisposta testo della risposta da settare
     */
    public final void setTestoRisposta(final String testoRisposta) {
        this.testoRisposta = testoRisposta;
    }

    /**
     * restituisce l'esito della risposta.
     * @return esito risposta
     */
    public final String getCorretta() {
        return corretta;
    }

    /**
     * setta l'esito della risposta.
     * @param corretta esito da settare
     */
    public final void setCorretta(final String corretta) {
        this.corretta = corretta;
    }

    /**
     * restituisce l'id della domanda della risposta in questione.
     * @return id domanda della risposta in questione
     */
    public final Integer getDomandaId() {
        return domandaId;
    }

    /**
     * setta l'id domanda della risposta in quesitone.
     * @param domandaId id della domanda da settare
     */
    public final void setDomandaId(final Integer domandaId) {
        this.domandaId = domandaId;
    }


}