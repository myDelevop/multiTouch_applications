package wwf.interactiveMap.data.dao;

import wwf.interactiveMap.data.model.Risposta;
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

/**
 * Implementazione dell'interfaccia RispostaDAO con mysql.
 */
public class RispostaDaoImpl implements RispostaDao {

	/** nome della tabella italiana presente nel database. */
	private String tableNameIt = "interactiveMap_risposte";
	
	/** nome della tabella italiana presente nel database. */
	private String tableNameEn = "interactiveMap_risposte_eng";
	
	/**
	 * Costrutture di default.
	 */
	public RispostaDaoImpl() { }

	/**
	 * @see wwf.cranium.data.dao.RispostaDAO#createRisposta(
	 * wwf.cranium.data.model.Risposta, boolean)
	 */
	
	@Override
    public final void createRisposta(final Risposta risposta,
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		Connection connection = DBAccess.getConnection();

		String id = getUniqueRispostaID(connection, isItalian);
		
		StringBuffer sbInsert = new StringBuffer();
		sbInsert.append("INSERT INTO ");
		sbInsert.append(tableName);
		sbInsert.append(" (testo_risposta, corretta, id_domanda, create_time)");
		sbInsert.append(" VALUES (");
		sbInsert.append("?, ?, ?, now()) ");
		
		PreparedStatement stmtInsert = null;
		try {
			stmtInsert = connection.prepareStatement(sbInsert.toString());
			stmtInsert.setString(1, risposta.getTestoRisposta());
			stmtInsert.setString(2, risposta.getCorretta());
			stmtInsert.setString(3, risposta.getDomandaId().toString());

			int rows = stmtInsert.executeUpdate();
			risposta.setId(Integer.parseInt(id));
			
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
	 * @see wwf.cranium.data.dao.RispostaDAO#updateRisposta(
	 * wwf.cranium.data.model.Risposta, boolean)
	 */
	@Override
    public final void updateRisposta(final Risposta risposta,
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
		sbUpdate.append(" testo_risposta = ?,");
		sbUpdate.append(" corretta = ?,");
		sbUpdate.append(" id_domanda = ?,");
	    sbUpdate.append(" create_time = now()");
		sbUpdate.append(" WHERE id = ?");
		
		try {
			stmtUpdate = connection.prepareStatement(sbUpdate.toString());
			stmtUpdate.setString(1, risposta.getTestoRisposta());
			stmtUpdate.setString(2, risposta.getCorretta());
			stmtUpdate.setString(3, risposta.getDomandaId().toString());
			stmtUpdate.setString(4, risposta.getId().toString());
			
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
	 * @see wwf.cranium.data.dao.RispostaDAO#deleteRisposta(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final void deleteRisposta(final Integer id,
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
	 * @see wwf.cranium.data.dao.RispostaDAO#findRispostaByID(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final Risposta findRispostaByID(final Integer id,
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		Risposta result = new Risposta();
		
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
			Collection<Risposta> c = makeCollectionFromResultSet(rs);
			
			if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }
			
			Iterator<Risposta> iter = c.iterator();
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
	 * @see wwf.cranium.data.dao.RispostaDAO#getAllRisposte(boolean)
	 */
	@Override
    public final List<Risposta> getAllRisposte(
            final boolean isItalian) throws SQLException {

		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		List<Risposta> risposte = new ArrayList<Risposta>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<Risposta> c = makeCollectionFromResultSet(rs);
			
			
			for (Risposta risp : c) {
				risposte.add(risp);
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
		
		return risposte;
	}

	
	/**
	 * @see wwf.cranium.data.dao.RispostaDAO#getRisposteByDomanda(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final List<Risposta> getRisposteByDomanda(final Integer domandaId, 
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

		List<Risposta> risposte = new ArrayList<Risposta>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		sbSelect.append(" WHERE id_domanda = ?");
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			stmtSelect.setString(1, domandaId.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<Risposta> c = makeCollectionFromResultSet(rs);
			
			
			for (Risposta risp : c) {
				risposte.add(risp);
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
		
		return risposte;
	}
	
	
	/**
	 * Restituisce una Collection di Risposte da un ResultSet sql.
	 *
	 * @param rs resultSet sql
	 * @return restituisce le DifficoltaDomande presenti nel resultSet
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private Collection<Risposta> makeCollectionFromResultSet(
	        final ResultSet rs) throws SQLException {
		Collection<Risposta> result = new ArrayList<Risposta>();

		while (rs.next()) {
			String id = rs.getString("id");
			String testoRisposta = rs.getString("testo_risposta");
			String corretta = rs.getString("corretta");
			String idDomanda = rs.getString("id_domanda");

			Risposta r = new Risposta(testoRisposta, corretta, 
			        Integer.parseInt(idDomanda));
			r.setId(Integer.parseInt(id));
			
			result.add(r);
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
	private String getUniqueRispostaID(final Connection conn,
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
			// select AUTO_INCREMENT from information_schema.tables where
			// table_schema='wwf' and tableName = 'cranium_giocatori';
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
     * Restituisce il nome della tabella in italiano.
     *
     * @return nome della tabella.
     * */
    public final String getTableNameIt() {
        return tableNameIt;
    }

    /**
     * Restituisce il nome della tabella in inglese.
     *
     * @return nome della tabella.
     * */
    public final String getTableNameEn() {
        return tableNameEn;
    }		

	
}
