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

import wwf.cranium.data.model.Giocatore;
import wwf.database.DBAccess;


/**
 * Implementazione dell'interfaccia GiocatoreDAO con mysql.
 */
public class GiocatoreDaoImpl implements GiocatoreDao {

	/** nome della tabella italiana presente nel database. */
	private String tableNameIt = "cranium_giocatori";
	
	/** nome della tabella inglese presente nel database. */
	private String tableNameEn = "cranium_giocatori_eng";


	/**
	 * Costrutture di default.
	 */
	public GiocatoreDaoImpl() { }

	/**
	 * @see wwf.cranium.data.dao.GiocatoreDAO#createGiocatore(
	 * wwf.cranium.data.model.Giocatore, boolean)
	 */
	@Override
    public final void createGiocatore(final Giocatore giocatore, 
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		

		Connection connection = DBAccess.getConnection();

		String id = getUniqueGiocatoreID(connection, isItalian);
		
		StringBuffer sbInsert = new StringBuffer();
		sbInsert.append("INSERT INTO ");
		sbInsert.append(tableName);
		sbInsert.append(" (nome, create_time)");
		sbInsert.append(" VALUES (");
		sbInsert.append("?, now()) ");
		
		PreparedStatement stmtInsert = null;
		try {
			stmtInsert = connection.prepareStatement(sbInsert.toString());
			stmtInsert.setString(1, giocatore.getNome());

			int rows = stmtInsert.executeUpdate();
			giocatore.setId(Integer.parseInt(id));
			
			if (rows != 1) {
                throw new SQLException("executeUpdate return value: "
					+ rows);
            }
			
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			//throw e;
		} finally {
			try {
				stmtInsert.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * @see wwf.cranium.data.dao.GiocatoreDAO#updateGiocatore(
	 * wwf.cranium.data.model.Giocatore, boolean)
	 */
	@Override
    public final void updateGiocatore(final Giocatore giocatore, 
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
		sbUpdate.append(" nome = ?, ");
	    sbUpdate.append(" create_time = now() ");
		sbUpdate.append(" WHERE id = ?");
		
		try {
			stmtUpdate = connection.prepareStatement(sbUpdate.toString());
			stmtUpdate.setString(1, giocatore.getNome());
			stmtUpdate.setString(2, giocatore.getId().toString());
			
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
	 * @see wwf.cranium.data.dao.GiocatoreDAO#deleteGiocatore(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final void deleteGiocatore(final Integer id,
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
	 * @see wwf.cranium.data.dao.GiocatoreDAO#findGiocatoreByID(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final Giocatore findGiocatoreByID(final Integer id,
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		

		Giocatore result = new Giocatore();
		
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
			Collection<Giocatore> c = makeCollectionFromResultSet(rs);
			
			if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }
			
			Iterator<Giocatore> iter = c.iterator();
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
	 * @see wwf.cranium.data.dao.GiocatoreDAO#getAllPlayers(boolean)
	 */
	@Override
    public final List<Giocatore> getAllPlayers(
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		

		List<Giocatore> giocatori = new ArrayList<Giocatore>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<Giocatore> c = makeCollectionFromResultSet(rs);
			
			
			for (Giocatore gioc : c) {
			    giocatori.add(gioc);
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
		
		return giocatori;	
	}

	/**
	 * Restituisce una Collection di Giocatore da un ResultSet sql.
	 *
	 * @param rs resultSet sql
	 * @return restituisce i giocatori presenti nel resultSet
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private Collection<Giocatore> makeCollectionFromResultSet(
	        final ResultSet rs) throws SQLException {
		
		Collection<Giocatore> result = new ArrayList<Giocatore>();

		while (rs.next()) {
			String id = rs.getString("id");
			String nome = rs.getString("nome");

			Giocatore g = new Giocatore(nome);
			g.setId(Integer.parseInt(id));
			result.add(g);
		}

		return result;
	}
	
	/**
	 * Restituisce l'id successivo di auto_increment.
	 *
	 * @param conn connessione al database
	 * @param isItalian indica se la tabella da usare del db è 
	 * italiana o inglese
	 * @return restituisce l'id richiesto
	 * @throws SQLException lancia una SQLException in caso di errore
	 */	
	
	private String getUniqueGiocatoreID(final Connection conn,
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
