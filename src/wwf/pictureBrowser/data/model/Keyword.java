package wwf.pictureBrowser.data.model;

import java.io.Serializable;

/**
 * Model del keyword (hashtag).
 */
public class Keyword implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id del keyword (hashtag). */
	private Integer id;
	
	/** parola chiave del keyword (hashtag). */
	private String keyword;
	
	/** id del socialnetwork cui l'hashtag è associato. */
	private Integer socialId;


	/**
	 * Costrutture di default.
	 */
	public Keyword() { }


	/**
	 * Costruttore parametrico.
	 *
	 * @param keyword parola chiave del keyword (hashtag)
	 * @param socialId id del socialnewtork cui ò'hashtag è associato
	 */
	public Keyword(final String keyword, final Integer socialId) {
		super();
		this.keyword = keyword;
		this.socialId = socialId;
	}


	/**
	 * restituisce l'id del keyword (hashtag).
	 * @return id del keyword (hashtag)
	 */
	public final Integer getId() {
		return id;
	}

	
	/**
	 * setta l'id del keyword (hashtag).
	 * @param id id da settare
	 */
	public final void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * restituisce la parola chiave del keyword (hashtag).
	 * @return parola chiave del keyword (hashtag)
	 */
	public final String getKeyword() {
		return keyword;
	}


	/**
	 * setta la parola chiave del keyword (hashtag).
	 * @param keyword parola chiave del keyword (hashtag)
	 */
	public final void setKeyword(final String keyword) {
		this.keyword = keyword;
	}


	/**
	 * restituisce l'id del social network cui il keyword (hashtag) è associato.
	 * @return id del social network.
	 */
	public final Integer getSocialId() {
		return socialId;
	}


	/**
	 * setta l'id del social network cui il keyword (hashtag) è associato.
	 * @param socialId id del social network da settare.
	 */
	public final void setSocialId(final Integer socialId) {
		this.socialId = socialId;
	}

	
}
