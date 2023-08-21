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
import wwf.ClassificationGame.data.model.CGClasse;
import wwf.ClassificationGame.exception.ColorException;
import wwf.ClassificationGame.exception.CGException;

public class CGClasseDAOImpl implements CGClasseDAO {

	public static void main(String[] args) throws SQLException {
		CGClasseDAOImpl classeImpl = new CGClasseDAOImpl();
		CGClasse clas = new CGClasse();
		// clas.setSpecie("A");
		// clas.setColore("viola");
		// classeImpl.insertClasse(clas);
		// classeImpl.updateClasse(clas, 22);
		// classeImpl.deleteClasse("A");
		// classeImpl.cercaClasseById(2);
		// classeImpl.cercaAllClassi(clas);
	}

	@Override
	public void insertClasse(CGClasse classe) throws SQLException {
		String sql = ("INSERT INTO cgclasse (specie, colore) VALUES (?, ?)");

		Connection connection = ConnectionDatabase.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = null;
		PreparedStatement pstmtInsertClasse = connection.prepareStatement(sql);

		try {
			// Verifico l'esistenza dei dati: se già presenti genero
			// l'eccezione!
			res = stmt.executeQuery("SELECT specie, colore FROM cgclasse");

			while (res.next()) {
				if (res.getString("specie").equalsIgnoreCase(classe.getSpecie()))
					throw new CGException();
				if (res.getString("colore").equalsIgnoreCase(classe.getColore()))
					throw new ColorException();
			}

			// Associamo al primo parametro la specie
			pstmtInsertClasse.setString(1, classe.getSpecie());
			// Associamo al secondo parametro il colore associato alla specie
			pstmtInsertClasse.setString(2, classe.getColore());

			pstmtInsertClasse.executeUpdate();

			System.out.println("Specie inserita con successo!");

		} catch (CGException s) {
			System.out.println(s.insertSpecie());
		} catch (ColorException c) {
			System.out.println(c.insertColor());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtInsertClasse.close();
			connection.close();
		}
	}

	@Override
	public void updateClasse(CGClasse classe, Integer idSpecie) throws SQLException {
		StringBuffer sbUpdateClasse = new StringBuffer();

		sbUpdateClasse.append("UPDATE cgclasse SET specie = ? , ");
		sbUpdateClasse.append("colore = ? ");
		sbUpdateClasse.append("WHERE id_specie = ?");

		Connection connection = ConnectionDatabase.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = null;
		PreparedStatement pstmtUpdateClasse = connection.prepareStatement(sbUpdateClasse.toString());

		try {
			// Verifico l'esistenza dei dati: se già presenti genero
			// l'eccezione!
			res = stmt.executeQuery("SELECT specie, colore FROM cgclasse");

			while (res.next()) {
				if (res.getString("specie").equalsIgnoreCase(classe.getSpecie()))
					throw new CGException();
			}

			// Associamo al primo parametro la specie
			pstmtUpdateClasse.setString(1, classe.getSpecie());

			// Associamo al secondo parametro il colore associato alla specie
			pstmtUpdateClasse.setString(2, classe.getColore());

			// Associamo al terzo parametro l'id che vogliamo aggiornare
			pstmtUpdateClasse.setInt(3, idSpecie);

			pstmtUpdateClasse.executeUpdate();

			System.out.println("Specie aggiornata con successo!");

		} catch (CGException s) {
			System.out.println(s.insertSpecie());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtUpdateClasse.close();
			connection.close();
		}
	}

	@Override
	public void deleteClasse(String specie) throws SQLException {
		boolean flag = false;
		String sql = ("DELETE FROM cgclasse WHERE specie = ?");

		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtDeleteClasse = connection.prepareStatement(sql);
		Statement stmt = connection.createStatement();
		ResultSet res = null;

		try {
			// Verifico l'esistenza dell' id della specie: se non esiste genera
			// l'eccezione!
			res = stmt.executeQuery("SELECT specie FROM cgclasse");

			while (res.next()) {
				if (res.getString("specie").equalsIgnoreCase(specie))
					flag = true;
			}

			if (flag == true) {
				pstmtDeleteClasse = connection.prepareStatement(sql);

				// Associamo all'unico parametro l'id che vogliamo eliminare
				pstmtDeleteClasse.setString(1, specie);

				pstmtDeleteClasse.executeUpdate();

				System.out.println("Specie eliminata con successo!");
			} else
				throw new CGException();
		} catch (CGException s) {
			System.out.println(s.deleteSpecie());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtDeleteClasse.close();
			connection.close();
		}
	}

	@Override
	public CGClasse cercaClasseBySpecie(String specie) throws SQLException {
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectClasse = null;
		ResultSet res = null;

		CGClasse result = new CGClasse();

		String sql = ("SELECT * FROM cgclasse WHERE specie = ?");

		try {
			pstmtSelectClasse = connection.prepareStatement(sql);

			// Associamo all'unico parametro l'id
			pstmtSelectClasse.setString(1, specie);

			res = pstmtSelectClasse.executeQuery();

			// Vado a memorizzare nella Collection i dati del ResultSet
			Collection<CGClasse> c = collectionResultSet(res);

			// Itero sulla collezione e restituirò tutti i dati se esiste
			// l'id
			Iterator<CGClasse> iter = c.iterator();
			result = iter.next();

			// System.out.println(result.toString() + "\n");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			pstmtSelectClasse.close();
			connection.close();
		}
		return result;
	}

	@Override
	public List<CGClasse> cercaAllClassi(CGClasse classe) throws SQLException {
		List<CGClasse> listaClassi = new ArrayList<CGClasse>();
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectAllClassi = null;
		ResultSet res = null;

		String sql = ("SELECT * FROM cgclasse");

		try {
			pstmtSelectAllClassi = connection.prepareStatement(sql.toString());

			res = pstmtSelectAllClassi.executeQuery();

			Collection<CGClasse> c = collectionResultSet(res);

			for (CGClasse classi : c)
				listaClassi.add(classi);

			/*System.out.println("Numero di classi presenti: " + listaClassi.size() + "\n");
			System.out.println("Classi presenti: \n" + listaClassi.toString());*/

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectAllClassi.close();
			connection.close();
		}
		return listaClassi;
	}

	private Collection<CGClasse> collectionResultSet(final ResultSet res) throws SQLException {
		Collection<CGClasse> result = new java.util.ArrayList<CGClasse>();

		// Inserisco nella Collection tutte le classi che trovo nella tabella
		// del DB
		while (res.next()) {
			Integer idSpecie = res.getInt("id_specie");
			String nome = res.getString("specie");
			String colore = res.getString("colore");
			CGClasse classe = new CGClasse(idSpecie, nome, colore);
			result.add(classe);
		}
		return result;
	}

}
