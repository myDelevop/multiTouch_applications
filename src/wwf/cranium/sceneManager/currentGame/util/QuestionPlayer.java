package wwf.cranium.sceneManager.currentGame.util;

/**
 * Questa classe descrive una coppia giocatore-domanda che indica il momento
 *  in cui un determinato giocatore sta rispondendo ad una determiata domanda 
 *  nel corso del gioco.
 * 
 */
public class QuestionPlayer implements Comparable<QuestionPlayer> {
	
	/** Domanda. */
	private CurrentDomanda question;
	
	/** Giocatore. */
	private CurrentPlayer player;

	/**
	 * Costruttore parametrico.
	 *
	 * @param question domanda
	 * @param player giocatore
	 */
	public QuestionPlayer(final CurrentDomanda question,
	        final CurrentPlayer player) {
		this.question = question;
		this.player = player;
	}
	
	
	/**
	 * restituisce la domanda.
	 * @return domanda
	 */
	public final CurrentDomanda getQuestion() {
		return question;
	}

	/**
	 * setta la domanda.
	 * @param question domanda da settare
	 */
	public final void setQuestion(final CurrentDomanda question) {
		this.question = question;
	}

	/**
	 * restituisce il giocatore.
	 * @return giocatore
	 */
	public final CurrentPlayer getPlayer() {
		return player;
	}

	/**
	 * setta il giocatore.
	 * @param player giocatore da settare
	 */
	public final void setPlayer(final CurrentPlayer player) {
		this.player = player;
	}


	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
    public final int compareTo(final QuestionPlayer o) {
		
		if (this.getPlayer().getPunteggio() < o.getPlayer().getPunteggio()) {
            return -1;
        } else {
            return 1;
        }
	}	
	

} 