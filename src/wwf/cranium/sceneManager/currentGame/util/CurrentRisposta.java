package wwf.cranium.sceneManager.currentGame.util;

import java.io.Serializable;


/**
 * Livello di astrazione per modellare le risposte, che si interpone 
 * tra il model e il livello massimo di astrazione.
 * 
 */
public class CurrentRisposta implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** Testo della risposta. */
	private String testo;	
	
	/** indica se la risposta è corretta. */
	private boolean corretta;

	
	/**
	 * Costruttore di default.
	 */
	public CurrentRisposta() { }
	
	/**
	 * Costruttore parametrico.
	 *
	 * @param testo testo della riposta
	 * @param corretta indica se la risposta è corretta
	 */
	public CurrentRisposta(final String testo, final boolean corretta) {
		super();
		this.testo = testo;
		this.corretta = corretta;
	}

	/**
	 * restituisce il testo della riposta.
	 * @return testo della risposta
	 */
	public final String getTesto() {
		return testo;
	}

	/**
	 * setta il testo della risposta.
	 * @param testo testo della risposta da settare
	 */
	public final void setTesto(final String testo) {
		this.testo = testo;
	}

	/**
	 * restituisce l'esito della risposta.
	 * @return true se la risposta è corretta
	 */
	public final boolean isCorretta() {
		return corretta;
	}

	/**
	 * setta l'esito della risposta.
	 * @param corretta indica se la risposta è corretta
	 */
	public final void setCorretta(final boolean corretta) {
		this.corretta = corretta;
	}

	
}
