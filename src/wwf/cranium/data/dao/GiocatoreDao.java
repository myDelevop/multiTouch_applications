package wwf.cranium.data.dao;

import java.sql.SQLException;
import java.util.List;

import wwf.cranium.data.model.Giocatore;


/**
 * Interfaccia GiocatoreDAO, i metodi permettono l'interazione con il
 * database.
 */
public interface GiocatoreDao {

	/**
	 * Inserisce nel database un giocatore.
	 *
	 * @param giocatore
	 *            model che rappresenta il giocatore da inserire
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void createGiocatore(Giocatore giocatore,
	        boolean isItalian) throws SQLException;
	
	/**
	 * Aggiorna nel database un giocatore.
	 *
	 * @param giocatore
	 *            model che rappresenta il giocatore
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void updateGiocatore(Giocatore giocatore, 
	        boolean isItalian) throws SQLException;
	
	/**
	 * Cancella un giocatore dal database.
	 *
	 * @param id
	 *            id del giocatore da cancellare
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void deleteGiocatore(Integer id,
	        boolean isItalian) throws SQLException;
	
	/**
	 * Dato un id, restituisce il model del giocatore prensente nel db con
	 * quell'id.
	 *
	 * @param id
	 *            id del giocatore
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return model del giocatore desiderato
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	Giocatore findGiocatoreByID(Integer id,
	        boolean isItalian) throws SQLException;
	
	/**
	 * Restituisce tutti i giocatori presenti nel database.
	 *
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return lista di tutti i giocatori presenti nel database
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	List<Giocatore> getAllPlayers(
	        boolean isItalian) throws SQLException;

	// etc...
}
