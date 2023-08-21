package wwf.pictureBrowser.data.dao;

import java.sql.SQLException;
import java.util.List;

import wwf.pictureBrowser.data.model.SocialNetwork;

/**
 * Interfaccia SocialNetworkDAO, i metodi permettono 
 * l'interazione con il database.
 */
public interface SocialNetworkDao {

	/**
	 * Inserisce nel database un social network.
	 *
	 * @param social
	 *            model che rappresenta il social network da inserire 
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void createSocialNetwork(SocialNetwork social) throws SQLException;
	
	/**
	 * Aggiorna nel database un social network.
	 *
	 * @param social
	 *            model che rappresenta il social network
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void updateSocialNetwork(SocialNetwork social) throws SQLException;
	
	/**
	 * Cancella un social network dal database.
	 *
	 * @param id
	 *            id del social network da cancellare
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	void deleteSocialNetwork(Integer id) throws SQLException;

	/**
	 * Dato un id, restituisce il model del social network presente nel db con
	 * quell'id.
	 *
	 * @param id
	 *            id del social network
	 * @return model del social network desiderato
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	SocialNetwork findSocialByID(Integer id) throws SQLException;
	
	/**
	 * Restituisce tutti i social network presenti nel database.
	 *
	 * @return lista di tutti i social network presenti nel database
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	List<SocialNetwork> getAllSocialNetworks() throws SQLException;

}
