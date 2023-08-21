package wwf.cranium.data.dao;

import wwf.cranium.data.model.DifficoltaDomande;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DifficoltaDomandeDAO, i metodi permettono l'interazione con il
 * database.
 */
public interface DifficoltaDomandeDao {

    /**
     * Inserisce nel database una difficoltà per le domande.
     * 
     * @param difficoltaDom
     *            model che rappresenta la difficoltà da inserire
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @throws SQLException
     *             lancia una SQLException in caso di errore
     */

    void createDifficoltaDomande(DifficoltaDomande difficoltaDom,
            boolean isItalian) throws SQLException;

    /**
     * Aggiorna nel database una difficoltà per le domande.
     *
     * @param difficoltaDom
     *            model che rappresenta la difficoltà
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @throws SQLException
     *             lancia una SQLException in caso di errore
     */
    
    void updateDifficoltaDomande(DifficoltaDomande difficoltaDom, 
            boolean isItalian) throws SQLException;

    /**
     * Cancella una difficoltà per le domande dal database.
     *
     * @param id
     *            id della difficolta da cancellare
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @throws SQLException
     *             lancia una SQLException in caso di errore
     */
    
    void deleteDifficoltaDomande(Integer id,
            boolean isItalian) throws SQLException;

    /**
     * Dato un id, restituisce il model della difficoltà prensente nel db con
     * quell'id.
     *
     * @param id
     *            id della domanda
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @return model della difficolta desiderata
     * @throws SQLException
     *             lancia una SQLException in caso di errore
     */
    DifficoltaDomande findDifficoltaDomandeByID(Integer id, 
            boolean isItalian) throws SQLException;

    /**
     * Restituisce tutte le difficolta di domande presenti nel database.
     *
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @return lista di tutte le difficolta di domande presenti nel database
     * @throws SQLException
     *             lancia una SQLException in caso di errore
     */
    List<DifficoltaDomande> getAllDifficoltaDomande(boolean 
            isItalian) throws SQLException;
    // etc...
}
