package wwf.cranium.sceneManager.currentGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import wwf.cranium.data.SerializeObjects;
import wwf.cranium.data.dao.GiocatoreDaoImpl;
import wwf.cranium.data.model.Giocatore;
import wwf.cranium.sceneManager.currentGame.util.CurrentPlayer;
import wwf.cranium.util.FilesPath;

/**
 * Livello massimo di astrazione per modellare i giocatori.
 * 
 */
public class CurrentGameData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Lista di giocatori. */
	private List<CurrentPlayer> currentPlayers;
	
	/** Percorso dei file serializzati. */
	private String filePath;

	/**
	 * Il costruttore istanzia un oggetto con le caratteristiche passate nei
	 * parametri dal database. In assenza di connessione viene gestita un 
	 * eccezione la quale provvede alla deserializzazione dell'oggetto con le 
	 * stesse caratteristiche.
	 *
	 * @param numPlayers numero di giocatori che si vogliono istanziare
	 * @param isItalian indica se la lingua è italina
	 * @param updateStatus  indica se è presente un aggiornamento nel db
	 */
	public CurrentGameData(final Integer numPlayers, final boolean isItalian,
	        final boolean updateStatus) {
		
		this.filePath = FilesPath.SERIALIZABLE_PATH;

		try {
		    if (!updateStatus) {
                throw new SQLException();		        
		    }
			inizializePlayersFromDB(numPlayers, isItalian);
		} catch (SQLException e) {
			e.printStackTrace();
			deserializeCurrentGame(numPlayers, isItalian);
			
		}

	}

	/**
	 * Inizializza i giocatori dal database.
	 *
	 * @param numPlayers numero di giocatori
	 * @param isItalian indica se la lingua è italiana
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	public final void inizializePlayersFromDB(final Integer numPlayers, 
	        final boolean isItalian) throws SQLException {

		currentPlayers = new ArrayList<CurrentPlayer>();

		
		List<CurrentPlayer> buffer = new LinkedList<CurrentPlayer>();

		int cont = 1;

		List<Giocatore> playersModel = 
		        new GiocatoreDaoImpl().getAllPlayers(isItalian);

		for (Giocatore g : playersModel) {
			CurrentPlayer currentP = new CurrentPlayer();

			currentP.setNome(g.getNome());
			currentP.setPunteggio(0);
			if (cont <= numPlayers) {
				buffer.add(currentP);
				cont++;
			}
		}
		
		
		
		
		int count = buffer.size();

		if (count <= 5) {
			for (int i = count + 1; i <= numPlayers; i++) {
				CurrentPlayer extra = new CurrentPlayer();
				extra.setNome("Player " + i);
				extra.setPunteggio(0);
				buffer.add(extra);
			}
		}
		
		currentPlayers.addAll(buffer.subList(0, numPlayers));



		
		
	}

	/**
	 * Inizializza i giocatori da file con deserializzazione.
	 *
	 * @param numPlayers numero di giocatori
	 * @param isItalian indica se la lingua è italiana
	 */
	private void deserializeCurrentGame(final Integer numPlayers,
	        final boolean isItalian) {

		String fileName = "players";

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
				CurrentGameData istance = (CurrentGameData) ois.readObject();

				List<CurrentPlayer> currents = new LinkedList<CurrentPlayer>();

				List<CurrentPlayer> buffer = istance.getCurrentPlayers();

				int count = buffer.size();

				if (count <= 5) {
					for (int i = count + 1; i <= numPlayers; i++) {
						CurrentPlayer extra = new CurrentPlayer();
						extra.setNome("Player " + i);
						extra.setPunteggio(0);
						buffer.add(extra);
					}
				}
				
				currents.addAll(buffer.subList(0, numPlayers));


				this.setCurrentPlayers(currents);
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
            deserializeCurrentGame(numPlayers, isItalian);
		}

	}

	/**
	 * restituisce tutti i giocatori.
	 * @return lista dei giocatori
	 */
	public final List<CurrentPlayer> getCurrentPlayers() {
		return currentPlayers;
	}

	/**
	 * setta i giocatori correnti.
	 * @param currentPlayers lista di giocatori da settare
	 */
	public final void setCurrentPlayers(
	        final List<CurrentPlayer> currentPlayers) {
		this.currentPlayers = currentPlayers;
	}
}
