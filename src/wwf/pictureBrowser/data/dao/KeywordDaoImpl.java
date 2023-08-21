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
import wwf.pictureBrowser.data.model.Keyword;

/**
 * Implementazione dell'interfaccia DifficoltaDomandeDao con mysql.
 */
public class KeywordDaoImpl implements KeywordDao {
	

    /** nome della tabella presente nel database. */
	private String tableName = "socialnetworkkeywords";

	/**
	 * Costrutture di default.
	 */
	public KeywordDaoImpl() { }

	/**
	 * @see wwf.pictureBrowser.data.Dao.KeywordDao#createKeyword(
	 * wwf.pictureBrowser.data.model.Keyword).
	 */
	@Override
    public final void createKeyword(final Keyword keyword) throws SQLException {
		
		Connection connection = DBAccess.getConnection();

		String id = getUniqueDifficoltaDomandeID(connection);
		
		StringBuffer sbInsert = new StringBuffer();
		sbInsert.append("INSERT INTO ");
		sbInsert.append(tableName);
		sbInsert.append(" (keyword, social_id, create_time)");
		sbInsert.append(" VALUES (");
		sbInsert.append("?, ?, now()) ");
		PreparedStatement stmtInsert = null;
		try {
			stmtInsert = connection.prepareStatement(sbInsert.toString());
			stmtInsert.setString(1, keyword.getKeyword());
			stmtInsert.setString(2, keyword.getSocialId().toString());
			int rows = stmtInsert.executeUpdate();
			keyword.setId(Integer.parseInt(id));
			
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
	 * @see wwf.pictureBrowser.data.Dao.KeywordDao#updateKeyword(
	 * wwf.pictureBrowser.data.model.Keyword)
	 */
	@Override
    public final void updateKeyword(final Keyword social) throws SQLException {
		Connection connection = DBAccess.getConnection();
		PreparedStatement stmtUpdate = null;
		
		StringBuffer sbUpdate = new StringBuffer();
		sbUpdate.append("UPDATE ");
		sbUpdate.append(tableName);
		sbUpdate.append(" SET ");
		sbUpdate.append(" keyword = ?, ");
		sbUpdate.append(" social_id = ?, ");
		sbUpdate.append(" create_time = now()");
		sbUpdate.append(" WHERE id = ?");
		
		try {
			stmtUpdate = connection.prepareStatement(sbUpdate.toString());
			stmtUpdate.setString(1, social.getKeyword());
			stmtUpdate.setString(2, social.getSocialId().toString());
			stmtUpdate.setString(3, social.getId().toString());
			
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
	 * @see wwf.pictureBrowser.data.Dao.KeywordDao#deleteKeyword(
	 * java.lang.Integer)
	 */
	@Override
    public final void deleteKeyword(final Integer id) throws SQLException {
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
	 * @see wwf.pictureBrowser.data.Dao.KeywordDao#findKeywordByID(
	 * java.lang.Integer).
	 */
	@Override
    public final Keyword findKeywordByID(final Integer id) throws SQLException {
		Keyword result = new Keyword();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT id, keyword, social_id FROM ");
		sbSelect.append(tableName);
		sbSelect.append(" WHERE id = ?");
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			stmtSelect.setString(1, id.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<Keyword> c = makeCollectionFromResultSet(rs);
			
			if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }
			
			Iterator<Keyword> iter = c.iterator();
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
	 * @see wwf.pictureBrowser.data.Dao.KeywordDao#getAllKeywords()
	 */
	@Override
    public final List<Keyword> getAllKeywords() throws SQLException {
		List<Keyword> keywords = new ArrayList<Keyword>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<Keyword> c = makeCollectionFromResultSet(rs);
			
			
			for (Keyword diff : c) {
				keywords.add(diff);
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
		
		return keywords;		
	}

	/**
	 * Restituisce una Collection di Keyword (hashtag) da un ResultSet sql.
	 *
	 * @param rs resultSet sql
	 * @return restituisce le Keywords (hashtags) presenti nel resultSet
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private Collection<Keyword> makeCollectionFromResultSet(final ResultSet rs)
			throws SQLException	{
		Collection<Keyword> result = new ArrayList<Keyword>();
		
		while (rs.next()) {
			String id = rs.getString("id"); 	
			String keyword = rs.getString("keyword"); 
			String socialid = rs.getString("social_id");
			Keyword d = new Keyword(keyword, 
					Integer.parseInt(socialid));
			d.setId(Integer.parseInt(id));
			result.add(d); 	
		}
			
		return result;
	}
	
	/**
	 * Restituisce l'id successivo di auto_increment.
	 *
	 * @param conn connessione al database
	 * @return restituisce l'id richiesto
	 * @throws SQLException lancia una SQLException in caso di errore
	*/
	private String getUniqueDifficoltaDomandeID(final Connection conn) 
	        throws SQLException {

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
	
    /**
     * Restituisce il nome della tabella presente nel db.
     * @return nome tabella
     * 
     * */
    public final String getTableName() {
        return tableName;
    }

}
