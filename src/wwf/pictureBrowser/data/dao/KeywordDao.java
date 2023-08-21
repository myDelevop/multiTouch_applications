package wwf.pictureBrowser.data.dao;

import java.sql.SQLException;
import java.util.List;

import wwf.pictureBrowser.data.model.Keyword;


/**
 * Interfaccia KeywordDAO, i metodi permettono l'interazione 
 * con i database.
 */
public interface KeywordDao {

	/**
	 * Inserisce nel database una keyword.
	 *
	 * @param keyword
	 *            model che rappresenta la parola chiave (hashtag) da inserire 
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void createKeyword(Keyword keyword) throws SQLException;
	
	/**
	 * Aggiorna nel database una parola chiave(hashtag).
	 *
	 * @param keyword
	 *            model che rappresenta la parola chiave da aggiornare
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */	
	void updateKeyword(Keyword keyword) throws SQLException;
	
	/**
	 * Cancella una parola chiave (hashtag) dal database.
	 *
	 * @param id
	 *            id della parola chiave (hashtag) da cancellare
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void deleteKeyword(Integer id) throws SQLException;

	/**
	 * Dato un id, restituisce il model della parola chiave (hashtag) nel db con
	 * quell'id.
	 *
	 * @param id
	 *            id della parola chiave (hashtag)
	 * @return model della parola chiave (hashtag) desiderata
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	Keyword findKeywordByID(Integer id) throws SQLException;
	
	/**
	 * Restituisce tutte le parola chiave (hashtag) presenti nel database.
	 *
	 * @return lista di tutte le parola chiave (hashtag) presenti nel database
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */	
	List<Keyword> getAllKeywords() throws SQLException;
}
