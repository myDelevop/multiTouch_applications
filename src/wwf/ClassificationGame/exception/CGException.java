package wwf.ClassificationGame.exception;

/**
 * Classe che gestisce l'eccezione in caso di stringa già esistente
 */
@SuppressWarnings("serial")
public class CGException extends Exception {

	/**
	 * Istanziazione della eccezione.
	 */
	public CGException() {
	}

	/** In caso di eccezione, restituisce la stringa di errore */
	public String insertSpecie() {
		String message = ("Specie esistente all'interno del DB!");
		return message;
	}

	public String deleteSpecie() {
		String message = ("Specie non esistente all'interno del DB!");
		return message;
	}

	public String insertCategoria() {
		String message = ("Categoria esistente all'interno del DB!");
		return message;
	}

	public String deleteCategoria() {
		String message = ("Categoria non esistente all'interno del DB!");
		return message;
	}

	public String insertEntita() {
		String message = ("Entita' esistente all'interno del DB!");
		return message;
	}

	public String deleteEntita() {
		String message = ("Entita' non esistente all'interno del DB!");
		return message;
	}
}
