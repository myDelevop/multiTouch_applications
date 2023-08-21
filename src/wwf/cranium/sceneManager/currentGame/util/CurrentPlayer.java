package wwf.cranium.sceneManager.currentGame.util;

import java.io.Serializable;

/**
 * Livello di astrazione per modellare i giocatori, che si interpone tra 
 * il model e il livello massimo di astrazione.
 */
public class CurrentPlayer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** nome del giocatore. */
	private String nome;
	
	/** punteggio del giocatore (inizialmente azzerato). */
	private Integer punteggio = 0;

	/**
	 * Costruttore di default.
	 */
	public CurrentPlayer() {
	}

	/**
	 * Costruttore parametrico.
	 *
	 * @param nome nome del giocatore
	 * @param punteggio punteggio del giocatore
	 */
	public CurrentPlayer(final String nome, final Integer punteggio) {
		super();
		this.nome = nome;
		this.punteggio = punteggio;
	}

	/**
	 * restituisce il nome del giocatore in questione.
	 * @return nome del giocatore
	 */
	public final String getNome() {
		return nome;
	}

	/**
	 * setta il nome del giocatore in questione.
	 * @param nome nome del giocatore da settare
	 */
	public final void setNome(final String nome) {
		this.nome = nome;
	}

	/**
	 * restituisce il punteggio del giocatore in questione.
	 * @return punteggio del giocatore
	 */
	public final Integer getPunteggio() {
		return punteggio;
	}

	/**
	 * setta il punteggio del giocatore in questione.
	 * @param punteggio punteggio del giocatore da settare
	 */
	public final void setPunteggio(final Integer punteggio) {
		this.punteggio = punteggio;
	}

}
