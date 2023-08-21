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

import wwf.cranium.data.model.Domanda;
import wwf.database.DBAccess;


/**
 * Implementazione dell'interfaccia DomandaDAO con mysql.
 */
public class DomandaDaoImpl implements DomandaDao {

	/** nome della tabella italiana presente nel database. */
	private String tableNameIt = "cranium_domande";
	
	/** nome della tabella inglese presente nel database. */
	private String tableNameEn = "cranium_domande_eng";
	

	/**
	 * Costrutture di default.
	 */
	public DomandaDaoImpl() { }

	/**
	 * @see wwf.cranium.data.dao.DomandaDAO#createDomanda(
	 * wwf.cranium.data.model.Domanda, boolean)
	 */
	@Override
    public final void createDomanda(final Domanda domanda, 
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		
		Connection connection = DBAccess.getConnection();
		
		String id = getUniqueDomandaID(connection, isItalian);


		StringBuffer sbInsert = new StringBuffer();
		sbInsert.append("INSERT INTO ");
		sbInsert.append(tableName);
		sbInsert.append(" (titolo, domanda, difficolta_id, tipologia_id, tempo"
				+ ", contenuto_multimediale, numero_domanda, create_time)");
		sbInsert.append(" VALUES (");
		sbInsert.append("?, ?, ?, ?, ?, ?, ?, now()) ");
		
		PreparedStatement stmtInsert = null;
		try {
			stmtInsert = connection.prepareStatement(sbInsert.toString());
			stmtInsert.setString(1, domanda.getTitolo());
			stmtInsert.setString(2, domanda.getDomanda());
			stmtInsert.setString(3, domanda.getDifficoltaId()
			        .toString());
			stmtInsert.setString(4, domanda.getTipologiaId()
			        .toString());
			stmtInsert.setString(5, domanda.getTempo()
			        .toString());
			stmtInsert.setString(6, domanda
			        .getContenutoMultimediale().toString());
			stmtInsert.setString(7, domanda.getNumeroDomanda()
			        .toString());

			int rows = stmtInsert.executeUpdate();
			domanda.setId(Integer.parseInt(id));
			
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
	 * @see wwf.cranium.data.dao.DomandaDAO#updateDomanda(
	 * wwf.cranium.data.model.Domanda, boolean)
	 */
	@Override
    public final void updateDomanda(final Domanda domanda,
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
		sbUpdate.append(" SET");
		sbUpdate.append(" titolo = ?, ");
		sbUpdate.append(" domanda = ?, ");
		sbUpdate.append(" difficolta_id = ?, ");
		sbUpdate.append(" tipologia_id = ?, ");
		sbUpdate.append(" tempo = ?, ");
		sbUpdate.append(" contenuto_multimediale = ?, ");
		sbUpdate.append(" numero_domanda = ?, ");
	      sbUpdate.append(" create_time = now() ");
		sbUpdate.append(" WHERE id = ?");
		
		try {
			stmtUpdate = connection.prepareStatement(sbUpdate.toString());
			stmtUpdate.setString(1, domanda.getTitolo());
			stmtUpdate.setString(2, domanda.getDomanda());
			stmtUpdate.setString(3, domanda.getDifficoltaId()
			        .toString());
			stmtUpdate.setString(4, domanda.getTipologiaId()
			        .toString());
			stmtUpdate.setString(5, domanda.getTempo()
			        .toString());
			stmtUpdate.setString(6, domanda
			        .getContenutoMultimediale().toString());
			stmtUpdate.setString(7, domanda.getNumeroDomanda()
			        .toString());
			stmtUpdate.setString(8, domanda.getId()
			        .toString());
			
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
	 * @see wwf.cranium.data.dao.DomandaDAO#deleteDomanda(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final void deleteDomanda(final Integer id,
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
	 * @see wwf.cranium.data.dao.DomandaDAO#findDomandaByID(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final Domanda findDomandaByID(final Integer id, 
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		
		Domanda result = new Domanda();
		
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
			Collection<Domanda> c = makeCollectionFromResultSet(rs);
			
			if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }
			
			Iterator<Domanda> iter = c.iterator();
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
	 * @see wwf.cranium.data.dao.DomandaDAO#getAllDomande(boolean)
	 */
	@Override
    public final List<Domanda> getAllDomande(
            final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		
		List<Domanda> domande = new ArrayList<Domanda>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			
			rs = stmtSelect.executeQuery();
			Collection<Domanda> c = makeCollectionFromResultSet(rs);
			
			
			for (Domanda domanda : c) {
                System.out.println(domanda.getTitolo());
				domande.add(domanda);
				
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
		
		return domande;	
	}

	
	/**
	 * @see wwf.cranium.data.dao.DomandaDAO#getDomandeByLevel(
	 * java.lang.Integer, boolean)
	 */
	@Override
    public final List<Domanda> getDomandeByLevel(final 
            Integer levelId, final boolean isItalian) throws SQLException {
		
		String tableName = "";
		if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }
		
		List<Domanda> domande = new ArrayList<Domanda>();
		
		Connection connection = DBAccess.getConnection();
		ResultSet rs = null;
		PreparedStatement stmtSelect = null;
		
		StringBuffer sbSelect = new StringBuffer();
		
		sbSelect.append("SELECT * FROM ");
		sbSelect.append(tableName);
		sbSelect.append(" WHERE difficolta_id = ?");
		sbSelect.append(" ORDER BY numero_domanda");

		
		try {
			stmtSelect = connection.prepareStatement(sbSelect.toString());
			stmtSelect.setString(1, levelId.toString());

			rs = stmtSelect.executeQuery();
			Collection<Domanda> c = makeCollectionFromResultSet(rs);
			
			
			for (Domanda domanda : c) {
                domande.add(domanda);			    
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
		
		return domande;	
	}

	
	/**
	 * Restituisce una Collection di Domande da un ResultSet sql.
	 *
	 * @param rs resultSet sql
	 * @return restituisce le Domande presenti nel resultSet
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private Collection<Domanda> makeCollectionFromResultSet(final ResultSet rs)
			throws SQLException {
			Collection<Domanda> result = new ArrayList<Domanda>();
		
			while (rs.next()) {
			    String id = rs.getString("id"); 	
				String titolo = rs.getString("titolo"); 
				String doman = rs.getString("domanda");
				String difficoltaId = rs
				        .getString("difficolta_id");
				String tipologiaId = rs
				        .getString("tipologia_id");
				String tempo = rs.getString("tempo");
				String contenutoMultimediale = 
				        rs.getString("contenuto_multimediale");
				String numeroDomanda = 
				        rs.getString("numero_domanda");
				Domanda d = new Domanda(titolo, doman,
						Integer.parseInt(difficoltaId),
						Integer.parseInt(tipologiaId),
						Integer.parseInt(tempo), 
						contenutoMultimediale, 
						Integer.parseInt(numeroDomanda));
				d.setId(Integer.parseInt(id));
				result.add(d); 	
			}
			
			return result;
		}

	/**
	 * Restituisce l'id successivo di auto_increment.
	 *
	 * @param conn connessione al database
	 * @param isItalian indica se la tabella da usare del db italiana o inglese
	 * @return restituisce l'id richiesto
	 * @throws SQLException lancia una SQLException in caso di errore
	 */
	private String getUniqueDomandaID(final 
	        Connection conn, final boolean isItalian) throws SQLException {
		
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
