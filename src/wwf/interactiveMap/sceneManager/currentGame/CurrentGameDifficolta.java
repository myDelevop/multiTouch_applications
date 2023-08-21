package wwf.interactiveMap.sceneManager.currentGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.mt4j.MTApplication;

import wwf.interactiveMap.data.SerializeObjects;
import wwf.interactiveMap.data.dao.DifficoltaDomandeDaoImpl;
import wwf.interactiveMap.data.model.DifficoltaDomande;
import wwf.interactiveMap.util.FilesPath;

/**
 * Livello massimo di astrazione per modellare i livelli di difficolta.
 * 
 */
public class CurrentGameDifficolta implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** lista dei livelli di difficolta. */
	private List<DifficoltaDomande> allDifficolta;
	
	/** Percorso dei file serializzati. */
	private String filePath;

	/** Applicazione corrente. */
	private transient MTApplication pa;
	/**
	 * Il costruttore istanzia un oggetto con le caratteristiche passate nei 
	 * parametri dal database. In assenza di connessione viene gestita un 
	 * eccezione la quale provvede alla deserializzazione dell'oggetto con le 
	 * stesse caratteristiche.
	 * 
	 * @param isItalian indica se la lingua è italina
	 * @param updateStatus indica se c'è un aggiornamento.
	 * @param pa applicazione corrente
	 */
	public CurrentGameDifficolta(final boolean isItalian,
	        final boolean updateStatus, final MTApplication pa) {
		
	    this.pa = pa;
		this.filePath = FilesPath.SERIALIZABLE_PATH;
		
		try {
	          if (!updateStatus) {
	                 throw new SQLException();    
	          }
	          inizializeDifficoltaFromDB(isItalian);
		} catch (SQLException e) {
			//e.printStackTrace();
			deserializeCurrentGameDifficolta(isItalian);
			
		}

	}

	/**
	 * Inizializza i livelli di difficolta dal database.
	 *
	 * @param isItalian indica se la lingua è italiana
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	private void inizializeDifficoltaFromDB(final boolean isItalian) 
	        throws SQLException {
		this.allDifficolta = new 
		        DifficoltaDomandeDaoImpl().getAllDifficoltaDomande(isItalian);
	}
	
	/**
	 * Inizializza i livelli di difficolta da file con deserializzazione.
	 *
	 * @param isItalian indica se la lingua è italiana
	 */
	private void deserializeCurrentGameDifficolta(final boolean isItalian) {

		String fileName = "difficolta";
		if (isItalian) {
            fileName = fileName.concat("ita");
        } else {
            fileName = fileName.concat("eng");
        }

		fileName = fileName.concat(".dat");
		
		File file = new File(filePath + fileName);
		if (file.exists()) {
 			ObjectInputStream ois = null;
 			try {
 				ois = new ObjectInputStream(
 				        new FileInputStream(filePath + fileName));
 				CurrentGameDifficolta istance = 
 				        (CurrentGameDifficolta) ois.readObject();
 				this.setAllDifficolta(istance.getAllDifficolta());
 			} catch (FileNotFoundException e) {
 				e.printStackTrace();
 			} catch (IOException e) {
 				e.printStackTrace();
 			} catch (ClassNotFoundException e) {
 				e.printStackTrace();
 			}
 		} else {
 		    SerializeObjects ser = new SerializeObjects(pa);
 		    ser.run();
 		    try {
                ser.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
 		   deserializeCurrentGameDifficolta(isItalian);
 		}

	}

	/**
	 * restituisce tutti i  livelli di difficolta.
	 * @return lista dei livelli di difficolta
	 */
	public final List<DifficoltaDomande> getAllDifficolta() {
		return allDifficolta;
	}

	/**
	 * setta i livelli di difficolta.
	 * @param allDifficolta lista dei livelli di difficolta da settare
	 */
	public final void setAllDifficolta(final
	        List<DifficoltaDomande> allDifficolta) {
		this.allDifficolta = allDifficolta;
	}

}
