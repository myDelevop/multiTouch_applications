package wwf.cranium.data.model;


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
	
	/** id difficolta della domanda in questione. */
	private Integer difficoltaId;
	
	/** id tipologia della domanda in quesione. */
	private Integer tipologiaId;
	
	/** tempo a disposizione per rispondere alla domanda in questione. */
	private Integer tempo;
	
	/** Indica se è presente un contenuto multimediale. */
	private String contenutoMultimediale;
	
	/** numero che rappresenta l'ordine di erogazione delle domande. */
	private Integer numeroDomanda;

	/**
	 * Costrutture di default.
	 */
	public Domanda() { }

	/**
	 * Costruttore parametrico.
	 *
	 * @param titolo titolo della domanda
	 * @param domanda testo della domanda
	 * @param difficoltaId id difficolta della domanda in questione
	 * @param tipologiaId id tipologia della domanda in questione
	 * @param tempo tempo a disposizione per rispondere alla domanda in
	 *  questione
	 * @param contenutoMultimediale indica se è presente un contenuto 
	 * multimediale
	 * @param numeroDomanda numero che rappresenta l'ordine di erogazione
	 *  delle domande
	 */
	public Domanda(final String titolo, final String domanda, 
	        final Integer difficoltaId, final Integer tipologiaId,
			final Integer tempo, final String contenutoMultimediale,
			final Integer numeroDomanda) {
		super();
		this.titolo = titolo;
		this.domanda = domanda;
		this.difficoltaId = difficoltaId;
		this.tipologiaId = tipologiaId;
		this.tempo = tempo;
		this.contenutoMultimediale = contenutoMultimediale;
		this.numeroDomanda = numeroDomanda;
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
	 * restituisce l'id della tipologia della domanda in questione.
	 * @return id tipologia
	 */
	public final Integer getTipologiaId() {
		return tipologiaId;
	}
	
	/**
	 * setta l'id tipologia della domanda in questione.
	 * @param tipologiaId tipologia id da settare
	 */
	public final void setTipologiaId(final Integer tipologiaId) {
		this.tipologiaId = tipologiaId;
	}

	/**
	 * restituisce il tempo a disposizione per rispondere alla domanda
	 *  in questione.
	 * @return tempo a disposizione per rispondere
	 */
	public final Integer getTempo() {
		return tempo;
	}

	/**
	 * setta il tempo a disposizione per rispondere alla domanda in questione.
	 * @param tempo tempo a disposizione per rispondere
	 */
	public final void setTempo(final Integer tempo) {
		this.tempo = tempo;
	}
}
