package wwf.cranium.data.model;

/**
 * Model della tipologia di domande.
 */
public class TipologiaDomande {
	
	/** id tipologia domande. */
	private Integer id;
	
	/** Nome della tipologia. */
	private String tipologia;

	/**
	 * Costrutture di default.
	 */
	public TipologiaDomande() { }
	
	/**
	 * Costruttore parametrico.
	 *
	 * @param tipologia nome della tipologia di domande
	 */
	public TipologiaDomande(final String tipologia) {
		super();
		this.tipologia = tipologia;
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
	 * restituisce il nome della tipologia.
	 * @return nome della tipologia
	 */
	public final String getTipologia() {
		return tipologia;
	}

	/**
	 * setta il nome della tipologia.
	 *
	 * @param tipologia nome della tipologia
	 */
	public final void setTipologia(final String tipologia) {
		this.tipologia = tipologia;
	}


}
