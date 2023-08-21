package wwf.pictureBrowser.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import wwf.pictureBrowser.data.dao.SocialNetworkDaoImpl;
import wwf.pictureBrowser.data.model.SocialNetwork;
import wwf.pictureBrowser.util.FilesPath;

/**
 * Livello massimo di astrazione per modellare i social networks.
 * 
 */
public class CurrentSocials implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** lista di social networks. */
	private List<SocialNetwork> allSocials;
	
	/** Percorso dei file serializzati. */
	private String filePath;


	/**
	 * Il costruttore istanzia un oggetto con determinate caratteristiche 
	 * dal database. In assenza di connessione viene gestita un eccezione
	 * la quale provvede alla,deserializzazione dell'oggetto con le stesse
	 * caratteristiche.
	 *
	 * @param updateStatus indica se c'è un aggiornamento nel db
	 */
	public CurrentSocials(final boolean updateStatus) {
		this.filePath = FilesPath.SERIALIZABLE_PATH;
		
		try {
		    if (!updateStatus) {
                throw new SQLException();		        
		    }
			inizializeSocialsFromDB();
		} catch (SQLException e) {
			System.out.println("Database non aggiornato, "
			        + "carico da file locale");
			//e.printStackTrace();
			deserializeSocials();
		}
	}

	/**
	 * Inizializza i social network dal database.
	 *
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	private void inizializeSocialsFromDB() throws SQLException {
		allSocials = new SocialNetworkDaoImpl().getAllSocialNetworks();
	}
	
	/**
	 * Inizializza i social network da file con deserializzazione.
	 *
	 */
	private void deserializeSocials() {
		String fileName = "socials.dat";
		File file = new File(filePath + fileName);
		
		if (file.exists()) {
			ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(new FileInputStream(
					        filePath + fileName));
					CurrentSocials istance = (CurrentSocials) ois.readObject();
					this.allSocials = istance.getAllSocials();
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
	            deserializeSocials();

		}
	}

	/**
	 * restituisce tutti i social network.
	 * @return lista dei social network
	 */
	public final List<SocialNetwork> getAllSocials() {
		return allSocials;
	}

	/**
	 * setta i social network correnti.
	 * @param allSocials lista di social network da settare
	 */
	public final void setAllSocials(final List<SocialNetwork> allSocials) {
		this.allSocials = allSocials;
	}
	
	
}
