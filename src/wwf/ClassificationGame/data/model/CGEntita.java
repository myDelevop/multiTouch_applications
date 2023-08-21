package wwf.ClassificationGame.data.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CGEntita implements Serializable {
	private Integer idEntita;
	private String entita;
	private String descrizione;
	private String domanda;
	private Integer idCategoria;

	public CGEntita() {
	}

	public CGEntita(Integer idEntita, String entita, String descrizione, String domanda, Integer idCategoria) {
		super();
		this.idEntita = idEntita;
		this.entita = entita;
		this.descrizione = descrizione;
		this.domanda = domanda;
		this.idCategoria = idCategoria;
	}

	public Integer getIdEntita() {
		return idEntita;
	}

	public void setIdEntita(Integer idEntita) {
		this.idEntita = idEntita;
	}

	public String getEntita() {
		return entita;
	}

	public void setEntita(String entita) {
		this.entita = entita;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDomanda() {
		return domanda;
	}

	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	@Override
	public String toString() {
		return "Id Entita: " + idEntita + ", Entita : " + entita + ", Descrizione : " + descrizione + ", Domanda : "
				+ domanda + "\n";
	}

}
