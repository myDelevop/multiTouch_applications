package wwf.interactiveMap.data.dao;


import java.sql.SQLException;
import java.util.List;

import wwf.interactiveMap.data.model.Domanda;

/**
 * Interfaccia DomandaDAO, i metodi permettono l'interazione con il
 * database.
 */
public interface DomandaDao {

	/**
	 * Inserisce nel database una domanda.
	 *
	 * @param domanda
	 *            model che rappresenta la domanda da inserire
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void createDomanda(Domanda domanda, boolean isItalian) throws SQLException;
	
	/**
	 * Aggiorna nel database una domanda.
	 *
	 * @param domanda
	 *            model che rappresenta la domanda
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void updateDomanda(Domanda domanda, boolean isItalian) throws SQLException;
	
	/**
	 * Cancella una domanda dal database.
	 *
	 * @param id
	 *            id della domanda da cancellare
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void deleteDomanda(Integer id, boolean isItalian) throws SQLException;
	
	
	/**
	 * Dato un id, restituisce il model della domanda prensente nel db con
	 * quell'id.
	 *
	 * @param id
	 *            id della domanda
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return model della domanda desiderata
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	Domanda findDomandaByID(Integer id, boolean isItalian) throws SQLException;
	
	/**
	 * Restituisce tutte le domande presenti nel database.
	 *
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return lista di tutte le domande presenti nel database
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	List<Domanda> getAllDomande(boolean isItalian) throws SQLException;
	
	
	/**
     * Restituisce tutte le domande presenti nel database di un determinato 
     * livello.
     *
     * @param levelId livello difficoltà desiderato
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @return lista di tutte le domande presenti nel database di quel livello
     * @throws SQLException
     *             lancia una SQLException in caso di errore
     */
    List<Domanda> getDomandeByLevel(Integer levelId, boolean isItalian)
            throws SQLException;
	// etc...
}
