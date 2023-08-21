package wwf.interactiveMap.data.model;

import java.io.Serializable;

/**
 * Model della tipologia di domande.
 */
public class DifficoltaDomande implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	

	/** id della difficolta delle domande. */
	private Integer id;
	
	/** Nome del livello di difficolta. */
	private String livelloDifficolta;
	
	/**
	 * Costrutture di default.
	 */
	public DifficoltaDomande() { }

	/**
	 * Costruttore parametrico.
	 *
	 * @param livelloDifficolta tipologia nome del livello di difficolta
	 */
	public DifficoltaDomande(final String livelloDifficolta) {
		super();
		this.livelloDifficolta = livelloDifficolta;
	}

	/**
	 * restituisce l'id della difficolta.
	 * @return id della difficolta
	 */
	public final Integer getId() {
		return id;
	}

	/**
	 * setta l'id della difficolta.
	 * @param id id da settare
	 */
	public final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * restituisce l'id della difficolta.
	 * @return id della difficolta
	 */
	public final String getLivelloDifficolta() {
		return livelloDifficolta;
	}

	/**
	 * setta il nome del livello di difficolta.
	 * @param livelloDifficolta nome del livello di difficolta
	 */
	public final void setLivelloDifficolta(final String livelloDifficolta) {
		this.livelloDifficolta = livelloDifficolta;
	}

	
}
