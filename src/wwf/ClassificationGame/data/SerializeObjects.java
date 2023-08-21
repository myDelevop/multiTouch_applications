package wwf.ClassificationGame.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wwf.ClassificationGame.data.DAO.CGCategoriaDAOImpl;
import wwf.ClassificationGame.data.DAO.CGClasseDAOImpl;
import wwf.ClassificationGame.data.DAO.CGEntitaDAOImpl;
import wwf.ClassificationGame.data.model.CGCategoria;
import wwf.ClassificationGame.data.model.CGClasse;
import wwf.ClassificationGame.data.model.CGEntita;
import wwf.ClassificationGame.util.FilesPath;

@SuppressWarnings("serial")
public class SerializeObjects extends Thread implements Serializable {
	private List<CGClasse> listaClassi;
	private List<CGCategoria> listaCategorie;
	private List<CGEntita> listaEntita;

	public void run() {
		try {
			inizializeDataFromDB();
		} catch (SQLException e) {
			e.printStackTrace();
			deserializeData();
		}
	}

	private void inizializeDataFromDB() throws SQLException {
		CGClasse specie = new CGClasse();
		CGCategoria category = new CGCategoria();
		CGEntita entity = new CGEntita();

		listaClassi = new ArrayList<CGClasse>();
		List<CGClasse> listaClassiDB = new CGClasseDAOImpl().cercaAllClassi(specie);

		listaCategorie = new ArrayList<CGCategoria>();
		List<CGCategoria> listaCategorieDB = new CGCategoriaDAOImpl().cercaAllCategorie(category);

		listaEntita = new ArrayList<CGEntita>();
		List<CGEntita> listaEntitaDB = new CGEntitaDAOImpl().cercaAllEntita(entity);

		ObjectOutputStream outputStream = null;
		File file = new File(FilesPath.rootPath + "data.dat");

		for (CGClasse classe : listaClassiDB) {
			int idSpecie = classe.getIdSpecie();
			String specie1 = classe.getSpecie();
			String colore = classe.getColore();

			specie.setIdSpecie(idSpecie);
			specie.setSpecie(specie1);
			specie.setColore(colore);

			CGClasse c = new CGClasse(idSpecie, specie1, colore);
			listaClassi.add(c);
		}

		for (CGCategoria categoria : listaCategorieDB) {
			int idCategoria = categoria.getIdCategoria();
			String categoria1 = categoria.getCategoria();
			int idSpecie = categoria.getIdSpecie();

			category.setIdCategoria(idCategoria);
			category.setCategoria(categoria1);
			category.setIdSpecie(idSpecie);

			CGCategoria c = new CGCategoria(idCategoria, categoria1, idSpecie);
			listaCategorie.add(c);
		}

		for (CGEntita entita : listaEntitaDB) {
			int idEntita = entita.getIdEntita();
			String entita1 = entita.getEntita();
			String descrizione = entita.getDescrizione();
			String domanda = entita.getDomanda();
			int idCategoria = entita.getIdCategoria();

			entity.setIdEntita(idEntita);
			entity.setEntita(entita1);
			entity.setDescrizione(descrizione);
			entity.setDomanda(domanda);
			entity.setIdCategoria(idCategoria);

			CGEntita e = new CGEntita(idEntita, entita1, descrizione, domanda, idCategoria);
			listaEntita.add(e);
		}
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(listaClassi);
			outputStream.writeObject(listaCategorie);
			outputStream.writeObject(listaEntita);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deserializeData() {
		File file = new File(FilesPath.rootPath + "data.dat");

		if (file.exists()) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(file));
				@SuppressWarnings("unchecked")
				ArrayList<CGClasse> istance = (ArrayList<CGClasse>) ois.readObject();
				this.setListaClassi(istance);
				@SuppressWarnings("unchecked")
				ArrayList<CGCategoria> istance1 = (ArrayList<CGCategoria>) ois.readObject();
				this.setListaCategorie(istance1);
				@SuppressWarnings("unchecked")
				ArrayList<CGEntita> istance2 = (ArrayList<CGEntita>) ois.readObject();
				this.setListaEntita(istance2);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public List<CGClasse> getListaClassi() {
		return listaClassi;
	}

	public void setListaClassi(List<CGClasse> listaClassi) {
		this.listaClassi = listaClassi;
	}

	public List<CGCategoria> getListaCategorie() {
		return listaCategorie;
	}

	public void setListaCategorie(List<CGCategoria> listaCategorie) {
		this.listaCategorie = listaCategorie;
	}

	public List<CGEntita> getListaEntita() {
		return listaEntita;
	}

	public void setListaEntita(List<CGEntita> listaEntita) {
		this.listaEntita = listaEntita;
	}

	public static void main(String[] args) throws SQLException {
		SerializeObjects d = new SerializeObjects();
		d.inizializeDataFromDB();
		d.deserializeData();
	}

}
