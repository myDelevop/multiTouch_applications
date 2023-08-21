package wwf.pictureBrowser.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import wwf.pictureBrowser.data.dao.KeywordDaoImpl;
import wwf.pictureBrowser.data.model.Keyword;
import wwf.pictureBrowser.util.FilesPath;

/**
 * Livello massimo di astrazione per modellare le keywords (hashtags).
 * 
 */

public class CurrentKeywords implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** lista di keywords (hashtags). */
	private List<Keyword> allKeywords;
	
	/** Percorso dei file serializzati. */
	private String filePath;

	/**
	 * Il costruttore istanzia un oggetto con determinate caratteristiche 
	 * dal database. In assenza di connessione viene gestita un eccezione 
	 * la quale provvede alla deserializzazione dell'oggetto con le stesse
	 *  caratteristiche.
	 *  
	 *  @param updateStatus indica se è presente un aggiornamento nel db
	 */
	public CurrentKeywords(final boolean updateStatus) {
		this.filePath = FilesPath.SERIALIZABLE_PATH;
		
		try {
	          if (!updateStatus) {
                  throw new SQLException();	              
	          }
			inizializeKeywordsFromDB();
		} catch (SQLException e) {
			System.err.println("Database non aggiornato, "
			        + "carico da file locale");
			//e.printStackTrace();
			deserializeKeywords();
		}
	}

	/**
	 * Inizializza le keywords (hashtags) dal database.
	 *
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	private void inizializeKeywordsFromDB() throws SQLException {
		allKeywords = new KeywordDaoImpl().getAllKeywords();
	}
	
	/**
	 * Inizializza le keywords (hashtags) da file con deserializzazione.
	 *
	 */
	private void deserializeKeywords() {
		String fileName = "keywords.dat";
		File file = new File(filePath + fileName);
		
		if (file.exists()) {
			ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(new FileInputStream(
					        filePath + fileName));
					CurrentKeywords istance = (CurrentKeywords)
					        ois.readObject();
					this.allKeywords = istance.getAllKeywords();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();					
				}
		} else {
            SerializeObjects ser = new SerializeObjects();
            ser.run();
            try {
                ser.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deserializeKeywords();

    }
		
	}

	/**
	 * restituisce tutte le keywords (hashtags).
	 * @return lista delle keywords (hashtags)
	 */
	public final List<Keyword> getAllKeywords() {
		return allKeywords;
	}

	/**
	 * setta le keywords (hashtags).
	 * @param allKeywords lista di keywords (hashtags) da settare
	 */
	public final void setAllKeywords(final List<Keyword> allKeywords) {
		this.allKeywords = allKeywords;
	}
	
	   
}
