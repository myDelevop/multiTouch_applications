package wwf.ClassificationGame.data.DAO;

import java.sql.SQLException;
import java.util.List;

import wwf.ClassificationGame.data.model.CGEntita;

public interface CGEntitaDAO {
	public void insertEntita(CGEntita entita) throws SQLException;

	public void updateEntita(CGEntita entita, Integer idEntita) throws SQLException;

	public void deleteEntita(String entita) throws SQLException;

	// Query per una singola entit�
	public CGEntita cercaEntitaByIdEntita(Integer idEntita) throws SQLException;

	// Query che ricerca per una categoria tutte le entit�
	public CGEntita cercaEntitaByIdCategoria(Integer idCategoria) throws SQLException;

	// Query che ricerca tutte le entit� che hanno delle domande
	public CGEntita cercaEntitaByDomanda1(Integer idSpecie) throws SQLException;

	// Query di una lista
	public List<CGEntita> cercaAllEntita(CGEntita entita) throws SQLException;
}
