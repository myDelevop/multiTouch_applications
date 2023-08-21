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
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.exception.CGException;

public class CGCategoriaDAOImpl implements CGCategoriaDAO {

	public static void main(String[] args) throws SQLException {
		CGCategoriaDAOImpl categoriaImpl = new CGCategoriaDAOImpl();
		CGCategoria cat = new CGCategoria();
		
		// cat.setCategoria("Artropodi");
		// cat.setIdSpecie(2);
		// categoriaImpl.insertCategoria(cat);
		// categoriaImpl.updateCategoria(cat, 28);
		// categoriaImpl.deleteCategoria("Invertebrati");
		// categoriaImpl.cercaCategoriaByIdCategoria(20);
		// categoriaImpl.cercaCategorieByIdSpecie(2);
		// categoriaImpl.cercaAllCategorie(cat);
	}

	@Override
	public void insertCategoria(CGCategoria categoria) throws SQLException {
		String sql = ("INSERT INTO cgcategoria (categoria, id_specie) VALUES (?, ?)");

		Connection connection = ConnectionDatabase.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = null;
		PreparedStatement pstmtInsertCategoria = connection.prepareStatement(sql);

		try {
			// Verifico l'esistenza dei dati: se già presenti genero
			// l'eccezione!
			res = stmt.executeQuery("SELECT categoria FROM cgcategoria");

			while (res.next()) {
				if (res.getString("categoria").equalsIgnoreCase(categoria.getCategoria()))
					throw new CGException();
			}

			// Associamo al primo parametro la categoria
			pstmtInsertCategoria.setString(1, categoria.getCategoria());

			// Associamo al secondo parametro l'id della specie di appartenenza
			pstmtInsertCategoria.setInt(2, categoria.getIdSpecie());

			pstmtInsertCategoria.executeUpdate();

			System.out.println("Categoria inserita con successo!");

		} catch (CGException s) {
			System.out.println(s.insertCategoria());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtInsertCategoria.close();
			connection.close();
		}
	}

	@Override
	public void updateCategoria(CGCategoria categoria, Integer idCategoria) throws SQLException {
		StringBuffer sbUpdateCategoria = new StringBuffer();

		sbUpdateCategoria.append("UPDATE cgcategoria SET categoria = ?, ");
		sbUpdateCategoria.append("id_specie = ? ");
		sbUpdateCategoria.append("WHERE id_categoria = ?");

		Connection connection = ConnectionDatabase.getConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = null;
		PreparedStatement pstmtUpdateCategoria = connection.prepareStatement(sbUpdateCategoria.toString());

		try {
			// Verifico l'esistenza dei dati: se già presenti genero
			// l'eccezione!
			res = stmt.executeQuery("SELECT categoria FROM cgcategoria");

			while (res.next()) {
				if (res.getString("categoria").equalsIgnoreCase(categoria.getCategoria()))
					throw new CGException();
			}

			// Associamo al primo parametro la categoria
			pstmtUpdateCategoria.setString(1, categoria.getCategoria());

			// Associamo al secondo parametro l'id della specie
			pstmtUpdateCategoria.setInt(2, categoria.getIdSpecie());

			// Associamo al terzo parametro l'id della categoria
			pstmtUpdateCategoria.setInt(3, idCategoria);

			pstmtUpdateCategoria.executeUpdate();

			System.out.println("Categoria aggiornata con successo!");

		} catch (CGException s) {
			System.out.println(s.insertCategoria());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtUpdateCategoria.close();
			connection.close();
		}
	}

	@Override
	public void deleteCategoria(String categoria) throws SQLException {
		boolean flag = false;
		String sql = ("DELETE FROM cgcategoria WHERE categoria = ?");

		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtDeleteCategoria = connection.prepareStatement(sql);
		Statement stmt = connection.createStatement();
		ResultSet res = null;

		try {
			// Verifico l'esistenza dell' id della specie: se non esiste genera
			// l'eccezione!
			res = stmt.executeQuery("SELECT categoria FROM cgcategoria");

			while (res.next()) {
				if (res.getString("categoria").equalsIgnoreCase(categoria))
					flag = true;
			}

			if (flag == true) {
				pstmtDeleteCategoria = connection.prepareStatement(sql);

				// Associamo all'unico parametro l'id che vogliamo eliminare
				pstmtDeleteCategoria.setString(1, categoria);

				pstmtDeleteCategoria.executeUpdate();

				System.out.println("Categoria eliminata con successo!");
			} else
				throw new CGException();
		} catch (CGException s) {
			System.out.println(s.deleteCategoria());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			res.close();
			stmt.close();
			pstmtDeleteCategoria.close();
			connection.close();
		}
	}

	@Override
	public CGCategoria cercaCategoriaByIdCategoria(Integer idCategoria) throws SQLException {
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectCategoria = null;
		ResultSet res = null;

		CGCategoria result = new CGCategoria();

		String sql = ("SELECT * FROM cgcategoria WHERE id_categoria = ?");

		try {
			pstmtSelectCategoria = connection.prepareStatement(sql);

			// Associamo all'unico parametro l'id della categoria
			pstmtSelectCategoria.setInt(1, idCategoria);

			res = pstmtSelectCategoria.executeQuery();

			// Vado a memorizzare nella Collection i dati del ResultSet
			Collection<CGCategoria> c = collectionResultSet(res);

			// Itero sulla collezione e restituirò tutti i dati se esiste l'id
			Iterator<CGCategoria> iter = c.iterator();
			result = iter.next();

			// System.out.println(result.toString() + "\n");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectCategoria.close();
			connection.close();
		}

		return result;
	}

	@Override
	public CGCategoria cercaCategorieByIdSpecie(Integer idSpecie) throws SQLException {
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectSpecie = null;
		ResultSet res = null;

		CGCategoria result = new CGCategoria();

		String sql = ("SELECT * FROM cgcategoria WHERE id_specie = ?");

		try {
			pstmtSelectSpecie = connection.prepareStatement(sql);

			// Associamo all'unico parametro l'id della specie
			pstmtSelectSpecie.setInt(1, idSpecie);

			res = pstmtSelectSpecie.executeQuery();

			// Vado a memorizzare nella collection i dati del ResultSet
			Collection<CGCategoria> c = collectionResultSet(res);

			// Itero sulla collezione e restituisco tutti i parametri se esiste
			// l'id
			Iterator<CGCategoria> iter = c.iterator();
			result = iter.next();

			// System.out.println("Categorie presenti nella specie: ");
			// for (CGCategoria specie : c)
			// System.out.println(specie.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectSpecie.close();
			connection.close();
		}

		return result;
	}

	@Override
	public List<CGCategoria> cercaAllCategorie(CGCategoria categoria) throws SQLException {
		List<CGCategoria> listaCategorie = new ArrayList<CGCategoria>();
		Connection connection = ConnectionDatabase.getConnection();
		PreparedStatement pstmtSelectAllCategorie = null;
		ResultSet res = null;

		String sql = ("SELECT * FROM cgcategoria");

		try {
			pstmtSelectAllCategorie = connection.prepareStatement(sql.toString());

			res = pstmtSelectAllCategorie.executeQuery();

			Collection<CGCategoria> c = collectionResultSet(res);

			for (CGCategoria categoria1 : c)
				listaCategorie.add(categoria1);

			//System.out.println("Numero di categorie: " + listaCategorie.size() + "\n");
			//System.out.println("Categorie presenti: \n" + listaCategorie.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmtSelectAllCategorie.close();
			connection.close();
		}
		return listaCategorie;
	}

	private Collection<CGCategoria> collectionResultSet(final ResultSet res) throws SQLException {
		Collection<CGCategoria> result = new java.util.ArrayList<CGCategoria>();

		// Inserisco nella Collection tutte le categorie che trovo nella tabella
		// del DB
		while (res.next()) {
			Integer idCategoria = res.getInt("id_categoria");
			String nome = res.getString("categoria");
			Integer idSpecie = res.getInt("id_specie");
			CGCategoria categoria = new CGCategoria(idCategoria, nome, idSpecie);
			result.add(categoria);
		}
		return result;
	}
}
