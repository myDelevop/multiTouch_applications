package wwf.pictureBrowser.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import wwf.database.DBAccess;
import wwf.pictureBrowser.data.model.SocialNetwork;


/**
 * Implementazione dell'interfaccia SocialNetworkDao con mysql.
 */
public class SocialNetworkDaoImpl implements SocialNetworkDao {

	/** nome della tabella presente nel database. */
	private String tableName = "socialnetworks";

	/**
	 * Costrutture di default.
	 */
	public SocialNetworkDaoImpl() { }

	/**
	 * @see wwf.pictureBrowser.data.Dao.SocialNetworkDao#createSocialNetwork(
	 * wwf.pictureBrowser.data.model.SocialNetwork)
	 */
	@Override
    public final void createSocialNetwork(final SocialNetwork social)
            throws SQLException {
		Connection connection = DBAccess.getConnection();

		String id = getUniqueDifficoltaDomandeID(connection);
		
		StringBuffer sbInsert = new StringBuffer();
		sbInsert.append("INSERT INTO ");
		sbInsert.append(tableName);
		sbInsert.append(" (name, create_time)");
		sbInsert.append(" VALUES (");
		sbInsert.append("?, now()) ");
		PreparedStatement stmtInsert = null;
		try {
			stmtInsert = connection.prepareStatement(sbInsert.toString());
			stmtInsert.setString(1, social.getName());
			int rows = stmtInsert.executeUpdate();
			social.setId(Integer.parseInt(id));
			
			if (rows != 1) {
                throw new SQLException("executeUpdate return value: "
					+ rows);
            }
			
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				
				stmtInsert.close();	
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * @see wwf.pictureBrowser.data.Dao.SocialNetworkDao#updateSocialNetwork(
	 * wwf.pictureBrowser.data.model.SocialNetwork)
	 */
	@Override
    public final void updateSocialNetwork(final SocialNetwork social) 
            throws SQLException {
		Connection connection = DBAccess.getConnection();
		PreparedStatement stmtUpdate = null;
		
		StringBuffer sbUpdate = new StringBuffer();
		sbUpdate.append("UPDATE ");
		sbUpdate.append(tableName);
		sbUpdate.append(" SET ");
		sbUpdate.append(" name = ?, ");
		sbUpdate.append(" create_time = now() ");
		sbUpdate.append(" WHERE id = ?");
		
		try {
			stmtUpdate = connection.prepareStatement(sbUpdate.toString());
			stmtUpdate.setString(1, social.getName());
			stmtUpdate.setString(2, social.getId().toString());
			
			int rows = stmtUpdate.executeUpdate();
			if (rows != 1) {
                throw new SQLException("executeUpdate return value: "
					+ rows);
            }
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				stmtUpdate.close();	
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	/**
	 * @see wwf.pictureBrowser.data.Dao.SocialNetworkDao#deleteSocialNetwork(
	 * java.lang.Integer)
	 */
	@Override
    public final void deleteSocialNetwork(final Integer id) 
            throws SQLException {
		StringBuffer sbDelete = new StringBuffer();
		sbDelete.append("DELETE FROM ");
		sbDelete.append(tableName);
		sbDelete.append(" WHERE id = ?");
		
		Connection connection = DBAccess.getConnection();
		PreparedStatement stmtDelete = null;

		try {
			stmtDelete = connection.prepareStatement(sbDelete.toString());
			stmtDelete.setString(1, id.toString());
			
			int rows = stmtDelete.executeUpdate();
			if (rows != 1) {
                throw new SQLException("executeUpdate return value: "
					+ rows);
            }
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				stmtDelete.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * @see wwf.pictureBrowser.data.Dao.SocialNetworkDao#findSocialByID(
	 * java.lang.Integer)
	 */
	@Override
    public final SocialNetwork findSocialByID(final Integer id) 
            throws SQLException {
		SocialNetwork result = new SocialNetwork();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT id, name FROM ");
		sbSelect.append(tableName);
		sbSelect.append(" WHERE id = ?");
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			stmtSelect.setString(1, id.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<SocialNetwork> c = makeCollectionFromResultSet(rs);
			
			if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }
			
			Iterator<SocialNetwork> iter = c.iterator();
			result = iter.next();
			

		} catch (SQLException e) {
				throw e;
		} finally {
			try {
				stmtSelect.close();
			} catch (SQLException e) {
				throw e;
			}	
		}
		
		
		return result;
	}

	/**
	 * @see wwf.pictureBrowser.data.Dao.SocialNetworkDao#getAllSocialNetworks()
	 */
	@Override
    public final List<SocialNetwork> getAllSocialNetworks() 
            throws SQLException {
		List<SocialNetwork> socials = new ArrayList<SocialNetwork>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<SocialNetwork> c = makeCollectionFromResultSet(rs);
			
			
			for (SocialNetwork diff : c) {
				socials.add(diff);
			}

			

		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				stmtSelect.close();
			} catch (SQLException e) {
				throw e;
			}	
		}
		
		return socials;		

	}

	/**
	 * Restituisce una Collection di SocialNetwork da un ResultSet sql.
	 *
	 * @param rs resultSet sql
	 * @return restituisce i SocialNetwork presenti nel resultSet
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private Collection<SocialNetwork> makeCollectionFromResultSet(
	        final ResultSet rs) throws SQLException	{
		Collection<SocialNetwork> result = new ArrayList<SocialNetwork>();
		
		while (rs.next()) {
			String id = rs.getString("id"); 	
			String name = rs.getString("name"); 
			SocialNetwork d = new SocialNetwork(name);
			d.setId(Integer.parseInt(id));

			
			result.add(d); 	
		}
			
		return result;
	}
	
	/**
	 * Restituisce il nome della tabella presente nel db.
	 * @return nome tabella
	 * 
	 * */
	public final String getTableName() {
        return tableName;
    }

    /**
	 * Restituisce l'id successivo di auto_increment.
	 *
	 * @param conn connessione al database
	 * @return restituisce l'id richiesto
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private String getUniqueDifficoltaDomandeID(
	        final Connection conn) throws SQLException {

		String id = null;
		
		StringBuffer sbSelect = null;
		Statement stmtSelect = null;
		ResultSet rs = null;
		
		try {
			sbSelect = new StringBuffer();
			sbSelect.append("SELECT AUTO_INCREMENT ");
			sbSelect.append("FROM information_schema.tables ");
			sbSelect.append("WHERE table_schema = '");
			sbSelect.append(conn.getCatalog());
			sbSelect.append("' and table_Name = '");
			sbSelect.append(tableName);
			sbSelect.append("';");
			
			stmtSelect = conn.createStatement();
			rs = stmtSelect.executeQuery(sbSelect.toString());
			rs.next();
			id = rs.getString("AUTO_INCREMENT");
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				stmtSelect.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		return id;
	}
	
		
}
