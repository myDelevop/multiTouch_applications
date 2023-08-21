package wwf.ClassificationGame.exception;

/**
 * Classe che gestisce l'eccezione in caso di colore già presente
 */
@SuppressWarnings("serial")
public class ColorException extends Exception {

	/**
	 * Istanziazione della eccezione.
	 */
	public ColorException() {
	}

	/** In caso di eccezione, restituisce la stringa di errore */
	public String insertColor() {
		String message = ("Colore presente all'interno del DB!");
		return message;
	}
}
