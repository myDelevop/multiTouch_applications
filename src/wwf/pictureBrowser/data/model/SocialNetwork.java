package wwf.pictureBrowser.data.model;

import java.io.Serializable;

/**
 * Model del social network.
 */
public class SocialNetwork implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** id del social network. */
	private Integer id;
	
	/** nome del social network name. */
	private String name;

	/**
	 * Costrutture di default.
	 */
	public SocialNetwork() { }

	
	/**
	 * Costruttore parametrico.
	 *
	 * @param name nome del social network.
	 */
	public SocialNetwork(final String name) {
		super();
		this.name = name;
	}


	/**
	 * restituisce l'id del social network.
	 * @return id del social network
	 */
	public final Integer getId() {
		return id;
	}

	/**
	 * setta l'id del social network.
	 * @param id id da settare
	 */
	public final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * restituisce il nome del social network.
	 * @return nome del social network
	 */
	public final String getName() {
		return name;
	}

	/**
	 * setta il nome del social network.
	 * @param name nome social network da settare
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	

}
