package wwf.ClassificationGame.data.DAO;

import java.sql.SQLException;
import java.util.List;

import wwf.ClassificationGame.data.model.CGCategoria;

public interface CGCategoriaDAO {
	public void insertCategoria(CGCategoria categoria) throws SQLException;

	public void updateCategoria(CGCategoria categoria, Integer idCategoria) throws SQLException;

	public void deleteCategoria(String categoria) throws SQLException;

	// Query di un singolo oggetto
	public CGCategoria cercaCategoriaByIdCategoria(Integer idCategoria) throws SQLException;

	// Query di un singolo oggetto
	public CGCategoria cercaCategorieByIdSpecie(Integer idSpecie) throws SQLException;

	// Query di una lista
	public List<CGCategoria> cercaAllCategorie(CGCategoria categoria) throws SQLException;
}
