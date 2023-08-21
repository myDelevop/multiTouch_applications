package wwf.cranium.data.model;


/**
 * Model del giocatore.
 */
public class Giocatore {

	/** id del giocatore. */
	private Integer id;
	
	/** Nome del giocatore. */
	private String nome;
	
	/**
	 * Costrutture di default.
	 */
	public Giocatore() { }
	
	/**
	 * Costruttore parametrico.
	 *
	 * @param nome nome del giocatore
	 */
	public Giocatore(final String nome) {
		this.nome = nome;
	}

	/**
	 * restituisce l'id del giocatore.
	 * @return id del giocatore
	 */
	public final Integer getId() {
		return id;
	}

	/**
	 * setta l'id del giocatore.
	 * @param id id da settare
	 */
	public final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * restituisce il nome del giocatore.
	 * @return nome del giocatore
	 */
	public final String getNome() {
		return nome;
	}

	/**
	 * setta il nome del giocatore.
	 * @param nome nome del giocatore da settare
	 */
	public final void setNome(final String nome) {
		this.nome = nome;
	}
}
