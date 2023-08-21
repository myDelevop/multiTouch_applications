package wwf.ClassificationGame.sceneManager;

public class Giocatore {
	private String nome;
	private Integer punteggio;

	public Giocatore() {
	}

	public Giocatore(String nome, Integer punteggio) {
		super();
		this.nome = nome;
		this.punteggio = punteggio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(Integer punteggio) {
		this.punteggio = punteggio;
	}

}
