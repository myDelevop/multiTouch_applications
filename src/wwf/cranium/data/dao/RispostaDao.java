package wwf.cranium.data.dao;

import java.sql.SQLException;
import java.util.List;
import wwf.cranium.data.model.Risposta;


/**
 * Interfaccia RispsotaDAO, i metodi permettono l'interazione con il
 * database.
 */
public interface RispostaDao {

	/**
	 * Inserisce nel database una risposta.
	 *
	 * @param risposta
	 *            model che rappresenta la risposta da inserire
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void createRisposta(Risposta risposta,
	        boolean isItalian) throws SQLException;
	
	/**
	 * Aggiorna nel database una risposta.
	 *
	 * @param risposta
	 *            model che rappresenta la risposta
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void updateRisposta(Risposta risposta, 
	        boolean isItalian) throws SQLException;
	
	/**
	 * Cancella una risposta dal database.
	 *
	 * @param id
	 *            id della risposta da cancellare
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void deleteRisposta(Integer id, 
	        boolean isItalian) throws SQLException;
	
	/**
	 * Dato un id, restituisce il model della risposta prensente nel db con
	 * quell'id.
	 *
	 * @param id
	 *            id della risposta
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return model della risposta desiderata
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	Risposta findRispostaByID(Integer id,
	        boolean isItalian) throws SQLException;
	
	/**
	 * Restituisce tutte le risposte presenti nel database.
	 *
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return lista di tutte le risposte presenti nel database
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	List<Risposta> getAllRisposte(
	        boolean isItalian) throws SQLException;
	
	/**
	 * Restituisce tutte le risposte presenti nel database
	 *  di una determinata domanda.
	 *
	 * @param domandaId id della domanda di cui si vogliono le risposte
	 * @param isItalian
	 *            indica se la tabella da usare del db è italiana o inglese
	 * @return lista di tutte le risposte presenti nel database di 
	 *         quella domanda 
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	List<Risposta> getRisposteByDomanda(Integer domandaId,
	        boolean isItalian) throws SQLException;

	// etc...
}
