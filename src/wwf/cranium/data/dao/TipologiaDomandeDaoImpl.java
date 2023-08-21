package wwf.cranium.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import wwf.cranium.data.model.TipologiaDomande;
import wwf.database.DBAccess;

// TODO: Auto-generated Javadoc
/**
 * Implementazione dell'interfaccia TipologiaDomandeDAO con mysql.
 */
public class TipologiaDomandeDaoImpl implements TipologiaDomandeDao {

	/** nome della tabella italiana presente nel database. */
	private String tableNameIt = "cranium_tipologia_domande";
	
	/** nome della tabella inglese presente nel database. */
	private String tableNameEn = "cranium_tipologia_domande_eng";
	
	/**
	 * Costrutture di default.
	 */
	public TipologiaDomandeDaoImpl() { }

	/**
	 * Creates the tipologia domande.
	 *
	 * @param tipDomande the tip domande
	 * @param isItalian the is italian
	 * @throws SQLException the SQL exception
	 * @see wwf.cranium.data.dao.TipologiaDomandeDAO#createTipologiaDomande(
	 * wwf.cranium.data.model.TipologiaDomande, boolean)
	 */
	@Override
    public final void createTipologiaDomande(final TipologiaDomande tipDomande,
            final boolean isItalian) throws SQLException {

		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		Connection connection = DBAccess.getConnection();

		String id = getUniqueTipDomandeID(connection, isItalian);
		
		StringBuffer sbInsert = new StringBuffer();
		sbInsert.append("INSERT INTO ");
		sbInsert.append(tableName);
		sbInsert.append(" (tipologia, create_time)");
		sbInsert.append(" VALUES (");
		sbInsert.append("?, now()) ");
		
		PreparedStatement stmtInsert = null;
		try {
			stmtInsert = connection.prepareStatement(sbInsert.toString());
			stmtInsert.setString(1, tipDomande.getTipologia());

			int rows = stmtInsert.executeUpdate();
			tipDomande.setId(Integer.parseInt(id));
			
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
	 * Update tipologia domande.
	 *
	 * @param tipDomanda the tip domanda
	 * @param isItalian the is italian
	 * @throws SQLException the SQL exception
	 * @see wwf.cranium.data.dao.TipologiaDomandeDAO#updateTipologiaDomande(
	 * wwf.cranium.data.model.TipologiaDomande, boolean)
	 */
	@Override
    public final void updateTipologiaDomande(final TipologiaDomande tipDomanda,
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		Connection connection = DBAccess.getConnection();
		PreparedStatement stmtUpdate = null;
		
		StringBuffer sbUpdate = new StringBuffer();
		sbUpdate.append("UPDATE ");
		sbUpdate.append(tableName);
		sbUpdate.append(" SET ");
		sbUpdate.append(" tipologia = ?, ");
	    sbUpdate.append(" create_time = now() ");
		sbUpdate.append(" WHERE id = ?");
		
		try {
			stmtUpdate = connection.prepareStatement(sbUpdate.toString());
			stmtUpdate.setString(1, tipDomanda.getTipologia());
			stmtUpdate.setString(2, tipDomanda.getId().toString());
			
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
	 * Delete tipologia domande.
	 *
	 * @param id the id
	 * @param isItalian the is italian
	 * @throws SQLException the SQL exception
	 * @see wwf.cranium.data.dao.TipologiaDomandeDAO#deleteTipologiaDomande(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final void deleteTipologiaDomande(final Integer id,
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		Connection connection = DBAccess.getConnection();
		PreparedStatement stmtDelete = null;
		
		StringBuffer sbDelete = new StringBuffer();
		sbDelete.append("DELETE FROM ");
		sbDelete.append(tableName);
		sbDelete.append(" WHERE id = ?");
		
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
	 * Find tipologia domande by ID.
	 *
	 * @param id the id
	 * @param isItalian the is italian
	 * @return the tipologia domande
	 * @throws SQLException the SQL exception
	 * @see wwf.cranium.data.dao.TipologiaDomandeDAO#findTipologiaDomandeByID(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final TipologiaDomande findTipologiaDomandeByID(final Integer id, 
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		TipologiaDomande result = new TipologiaDomande();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		sbSelect.append(" WHERE id = ?");
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			stmtSelect.setString(1, id.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<TipologiaDomande> c = makeCollectionFromResultSet(rs);
			
			if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }
			
			Iterator<TipologiaDomande> iter = c.iterator();
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
	 * Gets the all tipologie.
	 *
	 * @param isItalian the is italian
	 * @return the all tipologie
	 * @throws SQLException the SQL exception
	 * @see wwf.cranium.data.dao.TipologiaDomandeDAO#getAllTipologie(boolean)
	 */
	@Override
    public final List<TipologiaDomande> getAllTipologie(
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }


		List<TipologiaDomande> tipologie = new ArrayList<TipologiaDomande>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<TipologiaDomande> c = makeCollectionFromResultSet(rs);
			
			
			for (TipologiaDomande tip : c) {
				tipologie.add(tip);
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
		
		return tipologie;	
	}
	
	
	/**
	 * Restituisce una Collection di TipologiaDomande da un ResultSet sql.
	 *
	 * @param rs resultSet sql
	 * @return restituisce le TipologiaDomande presenti nel resultSet
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private Collection<TipologiaDomande> makeCollectionFromResultSet(
	        final ResultSet rs) throws SQLException {
		
		Collection<TipologiaDomande> result = new ArrayList<TipologiaDomande>();

		while (rs.next()) {
			String id = rs.getString("id");
			String tipologia = rs.getString("tipologia");

			TipologiaDomande t = new TipologiaDomande(tipologia);
			t.setId(Integer.parseInt(id));
			result.add(t);
		}

		return result;
	}
	
	
	/**
	 * Restituisce l'id successivo di auto_increment.
	 *
	 * @param conn connessione al database
	 * @param isItalian indica se la tabella da usare del db è italiana 
	 * o inglese
	 * @return restituisce l'id richiesto
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private String getUniqueTipDomandeID(final Connection conn,
	        final boolean isItalian) throws SQLException {

		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

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
     * Restituisce il nome della tabella italiana.
     *
     * @return nome della tabella presente nel db
     */
    public final String getTableNameIt() {
        return tableNameIt;
    }

    /**
     * Restituisce il nome della tabella inglese.
     *
     * @return nome della tabella presente nel db
     */
    public final String getTableNameEn() {
        return tableNameEn;
    }
	 
	
	
}
