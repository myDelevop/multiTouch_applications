package wwf.cranium.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import wwf.cranium.data.model.DifficoltaDomande;
import wwf.cranium.sceneManager.currentGame.CurrentGameData;
import wwf.cranium.sceneManager.currentGame.CurrentGameDifficolta;
import wwf.cranium.sceneManager.currentGame.CurrentGameQuestions;
import wwf.cranium.util.FilesPath;

/**
 * La Classe SerializeObjects è un thread che provvede a caricare le domande,
 *  i giocatori e le difficolta dal database e li serializza in alcuni file. 
 *  Gli oggetti potranno essere deserializzati per esempio, per giocare in 
 *  assenza di connessione. 
 */
public class SerializeObjects extends Thread {

	/** Percorso dei file da serializzare. */
	private String filePath = FilesPath.SERIALIZABLE_PATH;


	/**
	 * Il thread crea oggetti di domande,giocatori e difficolta per ogni 
	 * combinazione possibile; dopo di che vengono richiamati metodi che 
	 * serializzano ogni singolo oggetto.
	 * 
	 */
	@Override
    public final void run() {

		try {
			CurrentGameData currentData;

			currentData = new CurrentGameData(6, true, true);
			serializeCurrentGameData(currentData, "ita");

			currentData = new CurrentGameData(6, false, true);
			serializeCurrentGameData(currentData, "eng");
			
			CurrentGameDifficolta currentDifficolta;
			
			currentDifficolta = new CurrentGameDifficolta(true, true);
			serializeCurrentDifficolta(currentDifficolta, "ita");
			currentDifficolta = new CurrentGameDifficolta(false, true);
			serializeCurrentDifficolta(currentDifficolta, "eng");

			CurrentGameQuestions currentQuestions;
			
			currentDifficolta = new CurrentGameDifficolta(true, true);
			for (DifficoltaDomande diff:currentDifficolta.getAllDifficolta()) {
				currentQuestions = new CurrentGameQuestions(diff.
				        getId(), 1, true, false, true);
				serializeCurrentGame(currentQuestions, 
				        "ita", diff.getLivelloDifficolta());
			}
			
			currentDifficolta = new CurrentGameDifficolta(false, true);
			for (DifficoltaDomande diff:currentDifficolta.getAllDifficolta()) {
				currentQuestions = 
				        new CurrentGameQuestions(diff.getId(), 1, 
				                false, false, true);
				serializeCurrentGame(currentQuestions, "eng", 
				        diff.getLivelloDifficolta());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		

	}
	
	/**
	 * Serializza un oggetto di tipo CurrentGameData che modella i giocatori.
	 *
	 * @param c oggetto da serializzare
	 * @param language indica la lingua del gioco
	 */
	public final void serializeCurrentGameData(final CurrentGameData c, 
	        final String language) {
		ObjectOutputStream output = null;
		String fileName = "players" + language + ".dat";
		
		try {
			output = new ObjectOutputStream(
			        new FileOutputStream(filePath + fileName));
			output.writeObject(c);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


	/**
	 * Serializza un oggetto di tipo CurrentGameQuestions che modella le
	 *  domande.
	 *
	 * @param c oggetto da serializzare
	 * @param language indica la lingua del gioco
	 * @param difficolta indica il livello di difficolta per le domande da 
	 * serializzare
	 */
	public final void serializeCurrentGame(final CurrentGameQuestions c,
	        final String language, final String difficolta) {

		String fileName = "questions" + language + difficolta + ".dat";
 		
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(
			        filePath + fileName));
			output.writeObject(c);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}


	/**
	 * Serializza un oggetto di tipo CurrentGameDifficolta che modella le 
	 * difficolta per le domande.
	 *
	 * @param c oggetto da serializzare
	 * @param language indica la lingua del gioco
	 */
	public final void serializeCurrentDifficolta(
	        final CurrentGameDifficolta c, final String language) {
		ObjectOutputStream output = null;
		String fileName = "difficolta" + language + ".dat";
		
		try {
			output = new ObjectOutputStream(
			        new FileOutputStream(filePath + fileName));
			output.writeObject(c);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}


