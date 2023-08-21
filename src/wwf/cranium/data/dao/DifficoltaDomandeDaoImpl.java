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

import wwf.cranium.data.model.DifficoltaDomande;
import wwf.database.DBAccess;

/**
 * Implementazione dell'interfaccia DifficoltaDomandeDAO con mysql.
 */
public class DifficoltaDomandeDaoImpl implements DifficoltaDomandeDao {

    /** nome della tabella italiana presente nel database. */
    private String tableNameIt = "cranium_difficolta_domande";

    /** nome della tabella inglese presente nel database. */
    private String tableNameEn = "cranium_difficolta_domande_eng";

    /**
     * Costrutture di default.
     */
    public DifficoltaDomandeDaoImpl() {
    }

    /**
     * @see wwf.cranium.data.dao.DifficoltaDomandeDAO
     * #createDifficoltaDomande(wwf.cranium.data.model.DifficoltaDomande,
     *      boolean)
     */
    @Override
    public final void createDifficoltaDomande(final DifficoltaDomande
            difficoltaDom, final boolean isItalian) throws SQLException {

        String tableName = "";
        if (isItalian) {
            tableName = tableNameIt;            
        } else {
            tableName = tableNameEn;            
        }

        Connection connection = DBAccess.getConnection();

        String id = getUniqueDifficoltaDomandeID(connection, isItalian);

        StringBuffer sbInsert = new StringBuffer();
        sbInsert.append("INSERT INTO ");
        sbInsert.append(tableName);
        sbInsert.append(" (livello_difficolta, create_time)");
        sbInsert.append(" VALUES (");
        sbInsert.append("?, now()) ");
        PreparedStatement stmtInsert = null;
        try {
            stmtInsert = connection.prepareStatement(sbInsert.toString());
            stmtInsert.setString(1, difficoltaDom.getLivelloDifficolta());
            int rows = stmtInsert.executeUpdate();
            difficoltaDom.setId(Integer.parseInt(id));

            if (rows != 1) {                
                throw new SQLException("executeUpdate return value: " + rows);
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
     * @see wwf.cranium.data.dao.DifficoltaDomandeDAO#updateDifficoltaDomande
     * (wwf.cranium.data.model.DifficoltaDomande, boolean)
     */
    @Override
    public final void updateDifficoltaDomande(final DifficoltaDomande 
            difficoltaDom, final boolean isItalian) throws SQLException {

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
        sbUpdate.append(" livello_difficolta = ?, ");
        sbUpdate.append(" create_time = now() ");
        sbUpdate.append(" WHERE id = ?");

        try {
            stmtUpdate = connection.prepareStatement(sbUpdate.toString());
            stmtUpdate.setString(1, difficoltaDom.getLivelloDifficolta());
            stmtUpdate.setString(2, difficoltaDom.getId().toString());

            int rows = stmtUpdate.executeUpdate();
            if (rows != 1) {
                throw new SQLException("executeUpdate return value: " + rows);
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
     * @see wwf.cranium.data.dao.DifficoltaDomandeDAO#deleteDifficoltaDomande
     * (java.lang.Integer, boolean)
     */
    @Override
    public final void deleteDifficoltaDomande(final Integer id, 
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
                throw new SQLException("executeUpdate return value: " + rows);
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
     * @see wwf.cranium.data.dao.DifficoltaDomandeDAO#findDifficoltaDomandeByID
     * (java.lang.Integer, boolean)
     */
    @Override
    public final DifficoltaDomande findDifficoltaDomandeByID(
            final Integer id, final boolean isItalian) throws SQLException {

        String tableName = "";
        if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

        DifficoltaDomande result = new DifficoltaDomande();

        Connection connection = DBAccess.getConnection();
        ResultSet rs = null;
        PreparedStatement stmtSelect = null;

        StringBuffer sbSelect = new StringBuffer();

        sbSelect.append("SELECT id, livello_difficolta FROM ");
        sbSelect.append(tableName);
        sbSelect.append(" WHERE id = ?");

        try {
            stmtSelect = connection.prepareStatement(sbSelect.toString());
            stmtSelect.setString(1, id.toString());

            rs = stmtSelect.executeQuery();
            Collection<DifficoltaDomande> c = makeCollectionFromResultSet(rs);

            if (c.size() != 1) {
                throw new SQLException("id = " + id);
            }

            Iterator<DifficoltaDomande> iter = c.iterator();
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
     * @see wwf.cranium.data.dao.DifficoltaDomandeDAO#getAllDifficoltaDomande
     * (boolean)
     */
    @Override
    public final List<DifficoltaDomande> getAllDifficoltaDomande(
            final boolean isItalian) throws SQLException {

        String tableName = "";
        if (isItalian) {
            tableName = tableNameIt;
        } else {
            tableName = tableNameEn;
        }

        List<DifficoltaDomande> diffDomande = 
                new ArrayList<DifficoltaDomande>();

        Connection connection = DBAccess.getConnection();
        ResultSet rs = null;
        PreparedStatement stmtSelect = null;

        StringBuffer sbSelect = new StringBuffer();

        sbSelect.append("SELECT * FROM ");
        sbSelect.append(tableName);

        try {
            stmtSelect = connection.prepareStatement(sbSelect.toString());

            rs = stmtSelect.executeQuery();
            Collection<DifficoltaDomande> c = makeCollectionFromResultSet(rs);

            for (DifficoltaDomande diff : c) {
                diffDomande.add(diff);                
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

        return diffDomande;
    }

    /**
     * Restituisce una Collection di DifficoltaDomande da un ResultSet sql.
     *
     * @param rs
     *            resultSet sql
     * @return restituisce le DifficoltaDomande presenti nel resultSet
     * @throws SQLException lancia
     *             una SQLException in caso di errore
     */
    private Collection<DifficoltaDomande> makeCollectionFromResultSet(
            final ResultSet rs) throws SQLException {
        
        Collection<DifficoltaDomande> result =
                new java.util.ArrayList<DifficoltaDomande>();
        while (rs.next()) {
            String id = rs.getString("id");
            String livelloDifficolta = rs.getString("livello_difficolta");
            DifficoltaDomande d = new DifficoltaDomande(livelloDifficolta);
            d.setId(Integer.parseInt(id));
            result.add(d);
        }

        return result;
    }

    /**
     * Restituisce l'id successivo di auto_increment.
     *
     * @param conn
     *            connessione al database
     * @param isItalian
     *            indica se la tabella da usare del db è italiana o inglese
     * @return restituisce l'id richiesto
     * @throws SQLException lancia
     *             una SQLException in caso di errore
     */
    private String getUniqueDifficoltaDomandeID(final Connection conn,
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
            // table_schema='wwf' and table_name = 'cranium_giocatori';
            sbSelect.append("SELECT AUTO_INCREMENT ");
            sbSelect.append("FROM information_schema.tables ");
            sbSelect.append("WHERE table_schema = '");
            sbSelect.append(conn.getCatalog());
            sbSelect.append("' and table_name = '");
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
