package wwf.ClassificationGame.data.DAO;

import java.sql.SQLException;
import java.util.List;

import wwf.ClassificationGame.data.model.CGClasse;

public interface CGClasseDAO {
	public void insertClasse(CGClasse classe) throws SQLException;

	public void updateClasse(CGClasse classe, Integer idSpecie) throws SQLException;

	public void deleteClasse(String specie) throws SQLException;

	// Query di un singolo oggetto
	public CGClasse cercaClasseBySpecie(String specie) throws SQLException;

	// Query di una lista
	public List<CGClasse> cercaAllClassi(CGClasse classe) throws SQLException;
}
