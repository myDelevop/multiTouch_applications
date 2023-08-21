package wwf.cranium.data.dao;

import java.sql.SQLException;
import java.util.List;

import wwf.cranium.data.model.TipologiaDomande;

/**
 * Interfaccia TipologiaDomandeDAO, i metodi permettono l'interazione con il
 * database.
 */
public interface TipologiaDomandeDao {

	/**
	 * Inserisce nel database una tipologia di domanda.
	 *
	 * @param tipDomande
	 *            model che rappresenta la tipologia di domanda da inserire
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void createTipologiaDomande(TipologiaDomande tipDomande, 
	        boolean isItalian) throws SQLException;
	
	/**
	 * Aggiorna nel database una tipologia di domanda.
	 *
	 * @param tipDomande
	 *            model che rappresenta la tipologia domanda
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void updateTipologiaDomande(TipologiaDomande tipDomande,
	        boolean isItalian) throws SQLException;
	
	/**
	 * Cancella una tipologia di domanda dal database.
	 *
	 * @param id
	 *            id della tipologia di domanda da cancellare
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void deleteTipologiaDomande(Integer id, 
	        boolean isItalian) throws SQLException;
	
	/**
	 * Dato un id, restituisce il model della tipologia di domanda prensente
	 *  nel db con quell'id.
	 *
	 * @param id
	 *            id della tipologia di domanda
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return model della domanda desiderata
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */	
	
	TipologiaDomande findTipologiaDomandeByID(Integer id, 
	         boolean isItalian) throws SQLException;
	
	 /**
	  * Restituisce tutte le tipologie di domande presenti nel database.
	  *
	  * @param isItalian
	  *            indica se la tabella da usare del db è italiana o inglese
	  * @return lista di tutte le tipologie di domande presenti nel database
	  * @throws SQLException
	  *             lancia una SQLException in caso di errore
	  */	
	 List<TipologiaDomande> getAllTipologie(boolean isItalian)
	         throws SQLException;
	// etc...
	
}
