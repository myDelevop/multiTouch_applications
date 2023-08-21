package wwf.ClassificationGame.data.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import wwf.ClassificationGame.ConnectionDatabase;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.exception.CGException;

public class CGEntitaDAOImpl implements CGEntitaDAO {

	public static void main(String[] args) throws SQLException {
		CGEntitaDAOImpl entitaImpl = new CGEntitaDAOImpl();
		CGEntita ent = new CGEntita();

		// ent.setEntita("Farfalla");
		// ent.setDescrizione("Hanno 2 ");
		// ent.setDomanda("no");
		// ent.setIdCategoria(2);
		// entitaImpl.insertEntita(ent);
		// entitaImpl.updateEntita(ent, 64);
		// entitaImpl.deleteEntita(64);
		// entitaImpl.cercaEntitaByIdEntita(63);
		// entitaImpl.cercaEntitaByIdCategoria(20);
		// entitaImpl.cercaEntitaByDomanda(1);
		// entitaImpl.cercaAllEntita(ent);
	}

	@Override
	public void insertEntita(CGEntita entita) throws SQLException {
		String sql = ("INSERT INTO cgentita (entita, descrizione, domanda, id_categoria) VALUES (?, ?, ?, ?)");

		Connection connection = ConnectionDatabase.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = null;
		PreparedStatement pstmtInsertEntita = connection.prepareStatement(sql);

		try {
			// Verifico l'esistenza dei dati: se già presenti genero
			// l'eccezione!
			res = stmt.executeQuery("SELECT entita FROM cgentita");

			while (res.next()) {
				if (res.getString("entita").equalsIgnoreCase(entita.getEntita()))
					throw new CGException();
			}

			// Associamo al primo parametro l'entità
			pstmtInsertEntita.setString(1, entita.getEntita());

			// Associamo al secondo parametro la descrizione
			pstmtInsertEntita.setString(2, entita.getDescrizione());

			// Associamo al terzo parametro la domanda
			pstmtInsertEntita.setString(3, entita.getDomanda());

			// Associamo al quarto parametro l'id categoria
			pstmtInsertEntita.setInt(4, entita.getIdCategoria());

			pstmtInsertEntita.executeUpdate();

			System.out.println("Entita' inserita con successo!");

		} catch (CGException s) {
			System.out.println(s.insertEntita());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtInsertEntita.close();
			connection.close();
		}

	}

	@Override
	public void updateEntita(CGEntita entita, Integer idEntita) throws SQLException {
		StringBuffer sbUpdateEntita = new StringBuffer();

		sbUpdateEntita.append("UPDATE cgentita SET entita = ?, ");
		sbUpdateEntita.append("descrizione = ?, ");
		sbUpdateEntita.append("domanda = ?, ");
		sbUpdateEntita.append("id_categoria = ? ");
		sbUpdateEntita.append("WHERE id_entita = ?");

		Connection connection = ConnectionDatabase.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = null;
		PreparedStatement pstmtUpdateEntita = connection.prepareStatement(sbUpdateEntita.toString());

		try {
			// Verifico l'esistenza dei dati: se già presenti genero
			// l'eccezione!
			res = stmt.executeQuery("SELECT entita FROM cgentita");

			while (res.next()) {
				if (res.getString("entita").equalsIgnoreCase(entita.getEntita()))
					throw new CGException();
			}

			// Associamo al primo parametro l'entità
			pstmtUpdateEntita.setString(1, entita.getEntita());

			// Associamo al secondo parametro la descrizione
			pstmtUpdateEntita.setString(2, entita.getDescrizione());

			// Associamo al terzo parametro la domanda
			pstmtUpdateEntita.setString(3, entita.getDomanda());

			// Associamo al quarto parametro l'id categoria
			pstmtUpdateEntita.setInt(4, entita.getIdCategoria());

			// Associamo al quarto parametro l'id categoria
			pstmtUpdateEntita.setInt(4, entita.getIdCategoria());

			// Associamo al quinto parametro l'id entità
			pstmtUpdateEntita.setInt(4, idEntita);

			pstmtUpdateEntita.executeUpdate();

			System.out.println("Entita' aggiornata con successo!");

		} catch (CGException s) {
			System.out.println(s.insertEntita());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtUpdateEntita.close();
			connection.close();
		}
	}

	@Override
	public void deleteEntita(String entita) throws SQLException {
		boolean flag = false;
		String sql = ("DELETE FROM cgentita WHERE id_entita = ?");

		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtDeleteEntita = connection.prepareStatement(sql);
		Statement stmt = connection.createStatement();
		ResultSet res = null;

		try {
			// Verifico l'esistenza dell' id della specie: se non esiste genera
			// l'eccezione!
			res = stmt.executeQuery("SELECT entita FROM cgentita");

			while (res.next()) {
				if (res.getString("entita").equalsIgnoreCase(entita))
					flag = true;
			}

			if (flag == true) {
				pstmtDeleteEntita = connection.prepareStatement(sql);

				// Associamo all'unico parametro l'id che vogliamo eliminare
				pstmtDeleteEntita.setString(1, entita);

				pstmtDeleteEntita.executeUpdate();

				System.out.println("Entita' eliminata con successo!");
			} else
				throw new CGException();
		} catch (CGException s) {
			System.out.println(s.deleteEntita());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtDeleteEntita.close();
			connection.close();
		}
	}

	@Override
	public CGEntita cercaEntitaByIdEntita(Integer idEntita) throws SQLException {
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectEntita = null;
		ResultSet res = null;

		CGEntita result = new CGEntita();

		String sql = ("SELECT * FROM cgentita WHERE id_entita = ?");

		try {
			pstmtSelectEntita = connection.prepareStatement(sql);

			// Associamo all'unico parametro l'id dell'entita
			pstmtSelectEntita.setInt(1, idEntita);

			res = pstmtSelectEntita.executeQuery();

			// Vado a memorizzare nella Collection i dati del ResultSet
			Collection<CGEntita> e = collectionResultSet(res);

			// Itero sulla collezione e restituirò tutti i dati se esiste l'id
			Iterator<CGEntita> iter = e.iterator();
			result = iter.next();

			System.out.println("Entita presenti con domande: ");
			for (CGEntita categoria : e)
				System.out.println(categoria.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectEntita.close();
			connection.close();
		}

		return result;
	}

	@Override
	public CGEntita cercaEntitaByIdCategoria(Integer idCategoria) throws SQLException {
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectCategoria = null;
		ResultSet res = null;

		CGEntita result = new CGEntita();

		String sql = ("SELECT * FROM cgentita WHERE id_categoria = ?");

		try {
			pstmtSelectCategoria = connection.prepareStatement(sql);

			// Associamo all'unico parametro l'id della categoria
			pstmtSelectCategoria.setInt(1, idCategoria);

			res = pstmtSelectCategoria.executeQuery();

			// Vado a memorizzare nella collection i dati del ResultSet
			Collection<CGEntita> e = collectionResultSet(res);

			// Itero sulla collezione e restituisco tutti i parametri se esiste
			// l'id
			Iterator<CGEntita> iter = e.iterator();
			result = iter.next();

			System.out.println("Entita presenti nella categoria: ");
			for (CGEntita categoria : e)
				System.out.println(categoria.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectCategoria.close();
			connection.close();
		}

		return result;
	}

	@Override
	public CGEntita cercaEntitaByDomanda1(Integer idSpecie) throws SQLException {
		StringBuffer sbDomandaEntita = new StringBuffer();

		sbDomandaEntita.append("SELECT * ");
		sbDomandaEntita.append("FROM (cgentita inner join cgcategoria) INNER JOIN cgclasse ");
		sbDomandaEntita.append("ON (cgentita.id_categoria = cgcategoria.id_categoria) ");
		sbDomandaEntita.append("AND cgcategoria.id_specie = cgclasse.id_specie ");
		sbDomandaEntita.append("WHERE domanda = 'si' ");
		sbDomandaEntita.append("AND cgcategoria.id_specie = ?");

		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectAllEntitaDomande = null;
		ResultSet res = null;

		CGEntita result = new CGEntita();

		try {
			pstmtSelectAllEntitaDomande = connection.prepareStatement(sbDomandaEntita.toString());

			// Associamo l'id della specie
			pstmtSelectAllEntitaDomande.setInt(1, idSpecie);

			res = pstmtSelectAllEntitaDomande.executeQuery();

			Collection<CGEntita> c = collectionResultSet(res);

			// Itero sulla collezione e restituisco tutti i parametri se esiste
			// l'id
			Iterator<CGEntita> iter = c.iterator();
			result = iter.next();

			System.out.println("Entita presenti nella specie con domanda: ");
			for (CGEntita entita1 : c)
				System.out.println(entita1.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectAllEntitaDomande.close();
			connection.close();
		}
		return result;
	}

	@Override
	public List<CGEntita> cercaAllEntita(CGEntita entita) throws SQLException {
		List<CGEntita> listaEntita = new ArrayList<CGEntita>();
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectAllEntita = null;
		ResultSet res = null;

		String sql = ("SELECT * FROM cgentita");

		try {
			pstmtSelectAllEntita = connection.prepareStatement(sql.toString());

			res = pstmtSelectAllEntita.executeQuery();

			Collection<CGEntita> c = collectionResultSet(res);

			for (CGEntita entita1 : c)
				listaEntita.add(entita1);

			// System.out.println("Numero di entita: " + listaEntita.size() +
			// "\n");
			// System.out.println("Entita presenti: \n" +
			// listaEntita.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectAllEntita.close();
			connection.close();
		}
		return listaEntita;
	}

	private Collection<CGEntita> collectionResultSet(final ResultSet res) throws SQLException {
		Collection<CGEntita> result = new java.util.ArrayList<CGEntita>();

		// Inserisco nella Collection tutte le entita che trovo nella tabella
		// del DB
		while (res.next()) {
			Integer idEntita = res.getInt("id_entita");
			String nome = res.getString("entita");
			String descrizione = res.getString("descrizione");
			String domanda = res.getString("domanda");
			Integer idCategoria = res.getInt("id_categoria");
			CGEntita entita = new CGEntita(idEntita, nome, descrizione, domanda, idCategoria);
			result.add(entita);
		}
		return result;
	}

}
