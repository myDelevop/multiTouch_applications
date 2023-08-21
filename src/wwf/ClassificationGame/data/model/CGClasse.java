package wwf.ClassificationGame.data.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CGClasse implements Serializable {
	private Integer idSpecie;
	private String specie;
	private String colore;

	public CGClasse(Integer idSpecie, String specie, String colore) {
		this.idSpecie = idSpecie;
		this.specie = specie;
		this.colore = colore;
	}

	public CGClasse() {
	}

	public Integer getIdSpecie() {
		return idSpecie;
	}

	public void setIdSpecie(Integer idSpecie) {
		this.idSpecie = idSpecie;
	}

	public String getSpecie() {
		return specie;
	}

	public void setSpecie(String specie) {
		this.specie = specie;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	@Override
	public String toString() {
		return "Id: " + idSpecie + ", Specie: " + specie + ", Colore: " + colore;
	}

}