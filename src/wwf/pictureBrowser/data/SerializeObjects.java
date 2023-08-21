package wwf.pictureBrowser.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import wwf.pictureBrowser.util.FilesPath;

/**
 * La Classe SerializeObjects è un thread che provvede a caricare i social 
 * networks e gli hashtags dal database e li serializza in alcuni file. Gli 
 * oggetti potranno essere deserializzati per esempio, per visualizzare foto 
 * in assenza di connessione. 
 */
public class SerializeObjects extends Thread {
	
	/** Percorso dei file da serializzare. */
	private String filePath = FilesPath.SERIALIZABLE_PATH;

	/**
	 * Il thread crea oggetti di social network e keyword (hashtag) per ogni 
	 * combinazione possibile; dopo di che vengono richiamati metodi che 
	 * serializzano ogni singolo oggetto.
	 */
	@Override
    public final void run() {

		CurrentKeywords currentKeywords = new CurrentKeywords(true);
		serializeCurrentKeywords(currentKeywords);
		
		CurrentSocials currentSocials = new CurrentSocials(true);
		serializeCurrentSocials(currentSocials);
	}

	/**
	 * Serializza un oggetto di tipo CurrentKeywords che modella la 
	 * keywords(hashtag).
	 *
	 * @param currentKeywords oggetto da serializzare
	 */
	private void serializeCurrentKeywords(
	        final CurrentKeywords currentKeywords) {
		ObjectOutputStream output = null;
		String fileName = "keywords.dat";
		
		try {
			output = new ObjectOutputStream(new FileOutputStream(
			        filePath + fileName));
			output.writeObject(currentKeywords);
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
	 * Serializza un oggetto di tipo CurrentSocials che modella il 
	 * social network.
	 *
	 * @param currentSocials oggetto da serializzare
	 */
	private void serializeCurrentSocials(final 
	        CurrentSocials currentSocials) {
		ObjectOutputStream output = null;
		String fileName = "socials.dat";
		
		try {
			output = new ObjectOutputStream(
			        new FileOutputStream(filePath + fileName));
			output.writeObject(currentSocials);
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
