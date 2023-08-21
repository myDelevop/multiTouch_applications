package wwf.interactiveMap.sceneManager.currentGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;

import wwf.interactiveMap.data.SerializeObjects;
import wwf.interactiveMap.data.dao.DifficoltaDomandeDaoImpl;
import wwf.interactiveMap.data.dao.DomandaDaoImpl;
import wwf.interactiveMap.data.dao.RispostaDaoImpl;
import wwf.interactiveMap.data.model.DifficoltaDomande;
import wwf.interactiveMap.data.model.Domanda;
import wwf.interactiveMap.data.model.Risposta;
import wwf.interactiveMap.sceneManager.score.Player;
import wwf.interactiveMap.util.FilesPath;

// TODO: Auto-generated Javadoc
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

    /**
     * il parametro indica se le domanda devono essere erogate in un ordine
     * specifico oppure random.
     */
    private transient boolean isRandom;

    /** true se le domande in lingua italiana. */
    private transient boolean isItalian;

    /** id del livello delle domande in questione. */
    private transient Integer levelId;

    /** informazioni riguardo al player e punteggio. */
    private Player currentPlayer;

    /** indica se c'è un aggiornamento. */
    private transient boolean updateStatus = false;

    /** applicazione corrente. */
    private transient MTApplication pa;

    /**
     * Il costruttore istanzia un oggetto con le caratteristiche passate nei
     * parametri dal database. In assenza di connessione viene gestita un
     * eccezione la quale provvede alla deserializzazione dell'oggetto con le
     * stesse caratteristiche.
     *
     * @param pa            applicazione corrente
     * @param isItalian            indica se le domande sono in lingua italiana
     * @param isRandom            ordine delle domande, true se random
     * @param levelId            id del livello
     * @param playerName nome del giocatore corrente
     * @param updateStatus the update status
     */
    public CurrentGameQuestions(final MTApplication pa,
            final boolean isItalian, final boolean isRandom,
            final Integer levelId, final String playerName,
            final boolean updateStatus) {

        this.filePath = FilesPath.SERIALIZABLE_PATH;

        this.isItalian = isItalian;
        this.isRandom = isRandom;
        this.updateStatus = updateStatus;
        this.levelId = levelId;     
        this.currentPlayer = new Player(playerName, 0);
        this.pa = pa;

        try {
            if(!updateStatus) {
                throw new SQLException();
            }
            inizializeQuestionsFromDB();
        } catch (SQLException e) {
           // e.printStackTrace();
            deserializeCurrentGame();

            this.isItalian = isItalian;
            this.isRandom = isRandom;
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
        
        List<Risposta> answers = new RispostaDaoImpl()
                .getAllRisposte(isItalian);
        
        List<Domanda> questions = 
                new DomandaDaoImpl().
                getDomandeByLevel(levelId, isItalian);


        for (Domanda d : questions) {
            List<Risposta> answersModel = new LinkedList<Risposta>();
            for (Risposta r : answers) {
                if (r.getDomandaId().equals(d.getId())) {
                    answersModel.add(r);
                }
            }

            CurrentDomanda currentD = new CurrentDomanda();
            List<CurrentRisposta> currentRisp
                    = new LinkedList<CurrentRisposta>();
            for (Risposta a : answersModel) {
                CurrentRisposta currentR = new CurrentRisposta();
                currentR.setTesto(a.getTestoRisposta());
                if (!a.getCorretta().equalsIgnoreCase("no")) {
                    currentR.setCorretta(true);
                } else {
                    currentR.setCorretta(false);
                }
                currentRisp.add(currentR);
            }

            currentD.setTesto(d.getDomanda());
            currentD.setTitolo(d.getTitolo());
            currentD.setDescrizioneTestuale(d.getDescrizione());
            if (!d.getDescrizione_withImage().equalsIgnoreCase("no")) {
                currentD.setDescrizioneWithImage(true);
            } else {
                currentD.setDescrizioneWithImage(false);
            }            
            currentD.setRisposte(currentRisp);
            currentD.setDifficolta(level.getLivelloDifficolta());
            
            if (!d.getContenutoMultimediale().equalsIgnoreCase("no")) {
                currentD.setWithImage(true);
            } else {
                currentD.setWithImage(false);
            }

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
                updateStatus, pa);
        
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
            SerializeObjects ser = new SerializeObjects(pa);
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
     * 
     * @return lista delle domande correnti
     */
    public final List<CurrentDomanda> getCurrentQuestions() {
        return currentQuestions;
    }

    /**
     * setta le domande correnti.
     * 
     * @param currentQuestions
     *            lista delle domande da settare
     */
    public final void setCurrentQuestions(
            final List<CurrentDomanda> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }


    /**
     * restituisce true se le domande sono in lingua italiana.
     * 
     * @return lingua
     */
    public final boolean isItalian() {
        return isItalian;
    }

    /**
     * setta un valore che indica se la lingua delle domande è italiana.
     * 
     * @param isItalian
     *            true se la lingua è italiana
     */
    public final void setItalian(final boolean isItalian) {
        this.isItalian = isItalian;
    }

    /**
     * restituisce un valore che indica se l'ordine di erogazione delle domande
     * è random o definito a priori.
     * 
     * @return true se l'ordine è random
     */
    public final boolean isRandom() {
        return isRandom;
    }

    /**
     * setta un valore che indica se l'ordine di erogazione delle domande è
     * random o definito a priori.
     * 
     * @param isRandom
     *            true se l'ordine è random
     */
    public final void setRandom(final boolean isRandom) {
        this.isRandom = isRandom;
    }



    /**
     * Gets the current player.
     *
     * @return the current player
     */
    public final Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player.
     *
     * @param currentPlayer the new current player
     */
    public final void setCurrentPlayer(final Player currentPlayer) {
        this.currentPlayer = currentPlayer;
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
     *
     * @param levelId the new level id
     */
    public final void setLevelId(final Integer levelId) {
        this.levelId = levelId;
    }
    
}