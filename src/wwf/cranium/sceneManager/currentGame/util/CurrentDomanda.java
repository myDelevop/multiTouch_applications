package wwf.cranium.sceneManager.currentGame.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Livello di astrazione per modellare le domande, che si interpone tra 
 * il model e il livello massimo di astrazione.
 * 
 */
public class CurrentDomanda implements Serializable {
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** titolo della domanda. */
	private String titolo;
	
	/** testo della domanda. */
	private String testo;
	
	/** lista di tutte le risposte associate alla domanda in questione. */
	private List<CurrentRisposta> risposte = new LinkedList<CurrentRisposta>();
	
	/** tempo a disposizione per rispondere alla domanda in questione. */
	private int tempo;
	
	/** difficolta della domanda in questione. */
	private String difficolta;
	
	/** tipologia della domanda in questione. */
	private String tipologia;
	
	/** indica se alla domanda in questione è associato un contenuto 
	 * multimediale. */
	private boolean withImage;

	/**
	 * Costruttore di default.
	 */
	public CurrentDomanda() { }
	

	/**
	 * Costruttore parametrico.
	 *
	 * @param titolo titolo della domanda in questione
	 * @param testo testo della domanda in questione
	 * @param risposte lista delle risposte associate alla domanda in questione
	 * @param tempo tempo a disposizione per rispondere alla domanda
	 * @param difficolta difficoltà della domanda in questione
	 * @param tipologia tipologia della domanda in questione
	 * @param withImage indica se alla domanda in questione è 
	 * associato un contenuto multimediale
	 */
	public CurrentDomanda(final String titolo, final String testo, 
	        final LinkedList<CurrentRisposta> risposte, 
			final int tempo, final String difficolta, 
			final String tipologia, final boolean withImage) {
		super();
		this.titolo = titolo;
		this.testo = testo;
		this.risposte = risposte;
		this.tempo = tempo;
		this.difficolta = difficolta;
		this.tipologia = tipologia;
		this.withImage = withImage;
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
	 * restituisce il tempo a disposizione per rispondere alla domanda.
	 * @return tempo a disposizione
	 */
	public final int getTempo() {
		return tempo;
	}

	/**
	 * setta il tempo a disposizione per rispondere alla domanda in questione.
	 * @param tempo tempo a disposizione da settare
	 */
	public final void setTempo(final int tempo) {
		this.tempo = tempo;
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
	 * restituisce la tipologia associata alla domanda in questione.
	 * @return tipologia della domanda
	 */
	public final String getTipologia() {
		return tipologia;
	}

	/**
	 * setta la tipologia della domanda in questione.
	 * @param tipologia tipologia della domanda da settare
	 */
	public final void setTipologia(final String tipologia) {
		this.tipologia = tipologia;
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
	
	
}
