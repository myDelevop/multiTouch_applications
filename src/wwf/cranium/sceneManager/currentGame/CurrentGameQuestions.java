package wwf.cranium.sceneManager.currentGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import wwf.cranium.data.SerializeObjects;
import wwf.cranium.data.dao.DifficoltaDomandeDaoImpl;
import wwf.cranium.data.dao.DomandaDaoImpl;
import wwf.cranium.data.dao.RispostaDaoImpl;
import wwf.cranium.data.dao.TipologiaDomandeDaoImpl;
import wwf.cranium.data.model.DifficoltaDomande;
import wwf.cranium.data.model.Domanda;
import wwf.cranium.data.model.Risposta;
import wwf.cranium.data.model.TipologiaDomande;
import wwf.cranium.sceneManager.currentGame.util.CurrentDomanda;
import wwf.cranium.sceneManager.currentGame.util.CurrentRisposta;
import wwf.cranium.util.FilesPath;

/**
 * Livello massimo di astrazione per modellare le domande.
 * 
 */
public class CurrentGameQuestions implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** lista di domande. */
	private List<CurrentDomanda> currentQuestions;
	
	/** Percorso dei file serializzati. */
	private String filePath;
	
	
	/** true se le domande in lingua italiana. */
	private transient boolean isItalian;
	
	/** numero di giocatori per le domande in questione. */
	private transient Integer numPlayers;
	
	/** il parametro indica se le domanda devono essere erogate in un ordine
	 * specifico oppure random. */
	private transient boolean isRandom;
	
	/** indica se è presente un aggiornamento nel db. */
	private transient boolean updateStatus = false;
	
	/** id del livello delle domande in questione. */
	private transient Integer levelId;
	
	/**
	 * Il costruttore istanzia un oggetto con le caratteristiche passate nei 
	 * parametri dal database. In assenza di connessione viene gestita un 
	 * eccezione la quale provvede alla deserializzazione dell'oggetto con le
	 *  stesse caratteristiche.
	 *
	 * @param levelId id del livello
	 * @param numPlayers numero dei giocatori
	 * @param isItalian indica se le domande sono in lingua italiana
	 * @param isRandom ordine delle domande, true se random
	 * @param updateStatus indica se è presente un aggiornamento nel db
	 */
	public CurrentGameQuestions(final Integer levelId, final Integer numPlayers,
	        final boolean isItalian, final boolean isRandom, 
	        final boolean updateStatus) {
		
		this.filePath = FilesPath.SERIALIZABLE_PATH;
		this.updateStatus = updateStatus;
		this.isItalian = isItalian;
		this.numPlayers = numPlayers;
		this.isRandom = isRandom;
		this.levelId = levelId;		
		
		try {
		    if (!updateStatus) {
                throw new SQLException();		        
		    }
			inizializeQuestionsFromDB();
		} catch (SQLException e) {
			e.printStackTrace();
			deserializeCurrentGame();
			
			this.isItalian = isItalian;
			this.numPlayers = numPlayers;
			this.isRandom = isRandom;
			this.levelId = levelId;		
		}
		
		
	}

	
	/**
	 * Inizializza le domande dal database.
	 * 
	 * @throws SQLException
	 *             lancia una SQLException in caso di errore
	 */
	public final void inizializeQuestionsFromDB() throws SQLException {
		
		currentQuestions = new LinkedList<CurrentDomanda>();
		DifficoltaDomande level = new DifficoltaDomandeDaoImpl()
				.findDifficoltaDomandeByID(levelId, isItalian);
		
		List<Risposta> allRisposte = 
		        new RispostaDaoImpl().getAllRisposte(isItalian);
		List<TipologiaDomande> allTipologie = 
		        new TipologiaDomandeDaoImpl().getAllTipologie(isItalian); 
		
		List<Domanda> questionsModel = new 
		        DomandaDaoImpl().getDomandeByLevel(level.getId(), isItalian);
		
		for (Domanda d:questionsModel) {
			
			List<Risposta> answersModel = new LinkedList<Risposta>();

			for (Risposta r:allRisposte) {
				if (r.getDomandaId().equals(d.getId())) {
                    answersModel.add(r);
                }
			}
			
			CurrentDomanda currentD = new CurrentDomanda();
			
			List<CurrentRisposta> currentRisp = 
			        new LinkedList<CurrentRisposta>();			
			
			for (Risposta a:answersModel) {
				CurrentRisposta currentR = new CurrentRisposta();
				currentR.setTesto(a.getTestoRisposta()); 
				if (!a.getCorretta().equalsIgnoreCase("no")) {
                    currentR.setCorretta(false);
                } else {
                    currentR.setCorretta(true);
                }
				
				currentRisp.add(currentR);
			}
			currentD.setTesto(d.getDomanda());
			currentD.setTitolo(d.getTitolo());

			currentD.setTempo(d.getTempo());

			for (TipologiaDomande tip:allTipologie) {
				if (d.getTipologiaId().equals(tip.getId())) {
					currentD.setTipologia(tip.getTipologia());
				}
			}
			
			currentD.setDifficolta(level.getLivelloDifficolta());
			if (!d.getContenutoMultimediale().equalsIgnoreCase("no")) {
                currentD.setWithImage(true);
            } else {
                currentD.setWithImage(false);
            }
			
			currentD.setRisposte(currentRisp);
			
			currentQuestions.add(currentD);
		}

		
	}
	
	
	/**
	 * Inizializza le domande da file con deserializzazione.
	 *
	 */
	private void deserializeCurrentGame() {

		
		String fileName = "questions";
		String difficoltaName = "";
		
		if (isItalian) {
			fileName = fileName.concat("ita");
			difficoltaName = "Medio"; //default value
		} else {
			fileName = fileName.concat("eng");
			difficoltaName = "medium";			
		}
		
		
		CurrentGameDifficolta difficolta = new CurrentGameDifficolta(isItalian,
		        updateStatus);
		
		for (DifficoltaDomande diff:difficolta.getAllDifficolta()) {
			if (levelId.equals(diff.getId())) {
                difficoltaName = diff.getLivelloDifficolta();
            }
		}
		
		fileName = fileName.concat(difficoltaName);
		fileName = fileName.concat(".dat");
		
			
		File file = new File(filePath + fileName);
		if (file.exists()) {
 			ObjectInputStream ois = null;
 			try {
 				ois = new ObjectInputStream(
 				        new FileInputStream(filePath + fileName));
 				CurrentGameQuestions istance = (CurrentGameQuestions) 
 				        ois.readObject();
 				this.setCurrentQuestions(istance.getCurrentQuestions());
 			} catch (FileNotFoundException e) {
 				e.printStackTrace();
 			} catch (IOException e) {
 				e.printStackTrace();
 			} catch (ClassNotFoundException e) {
 				e.printStackTrace();
 			}
 		} else {
 		    
            SerializeObjects ser = new SerializeObjects();
            ser.run();
            try {
                ser.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deserializeCurrentGame();
        
 		}
		
	}


	
	
	/**
	 * restituisce tutte le domande.
	 * @return lista delle domande correnti
	 */
	public final List<CurrentDomanda> getCurrentQuestions() {
		return currentQuestions;
	}


	/**
	 * setta le domande correnti.
	 * @param currentQuestions lista delle domande da settare
	 */
	public final void setCurrentQuestions(final 
	        List<CurrentDomanda> currentQuestions) {
		this.currentQuestions = currentQuestions;
	}


	/**
	 * restituisce true se le domande sono in lingua italiana.
	 * @return lingua 
	 */
	public final boolean isItalian() {
		return isItalian;
	}


	/**
	 * setta un valore che indica se la lingua delle domande è italiana.
	 * @param isItalian true se la lingua è italiana
	 */
	public final void setItalian(final boolean isItalian) {
		this.isItalian = isItalian;
	}


	/**
	 * restituisce il numero dei giocatori.
	 * @return numero dei giocatori
	 */
	public final int getNumPlayers() {
		return numPlayers;
	}


	/**
	 * setta il numero dei giocatori.
	 * @param numPlayers numero di giocatori da settare
	 */
	public final void setNumPlayers(final int numPlayers) {
		this.numPlayers = numPlayers;
	}


	/**
	 * restituisce un valore che indica se l'ordine di erogazione delle domande
	 * è random o definito a priori.
	 * @return true se l'ordine è random
	 */
	public final boolean isRandom() {
		return isRandom;
	}


	/**
	 * setta un valore che indica se l'ordine di erogazione delle domande
	 * è random o definito a priori.
	 * @param isRandom true se l'ordine è random
	 */
	public final void setRandom(final boolean isRandom) {
		this.isRandom = isRandom;
	}


	/**
	 * restituisce l'id del livello di difficolta.
	 * @return id del livello di difficolta
	 */
	public final Integer getLevelId() {
		return levelId;
	}

	/**
	 * setta l'id del livello di difficolta.
	 * @param levelId id del livello da settare
	 */
	public final void setLevelId(final Integer levelId) {
		this.levelId = levelId;
	}

}



