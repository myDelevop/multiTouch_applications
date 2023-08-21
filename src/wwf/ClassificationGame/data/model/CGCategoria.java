package wwf.ClassificationGame.data.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CGCategoria implements Serializable{
	private Integer idCategoria;
	private String categoria;
	private Integer idSpecie;

	public CGCategoria() {
	}

	public CGCategoria(Integer idCategoria, String categoria, Integer idSpecie) {
		super();
		this.idCategoria = idCategoria;
		this.categoria = categoria;
		this.idSpecie = idSpecie;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Integer getIdSpecie() {
		return idSpecie;
	}

	public void setIdSpecie(Integer idSpecie) {
		this.idSpecie = idSpecie;
	}

	@Override
	public String toString() {
		return "Id Categoria: " + idCategoria + ", Categoria : " + categoria + "\n";
	}

}
