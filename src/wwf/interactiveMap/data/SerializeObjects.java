package wwf.interactiveMap.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.mt4j.MTApplication;
import wwf.interactiveMap.data.model.DifficoltaDomande;
import wwf.interactiveMap.sceneManager.currentGame.CurrentGameDifficolta;
import wwf.interactiveMap.sceneManager.currentGame.CurrentGameQuestions;
import wwf.interactiveMap.util.FilesPath;

/**
 * La Classe SerializeObjects è un thread che provvede a caricare le domande,
 * con rispettive riposte e li serializza in alcuni file. Gli oggetti potranno
 * essere deserializzati per esempio, per giocare in assenza di connessione.
 */

public class SerializeObjects extends Thread {

    /** Percorso dei file da serializzare. */
    private String filePath = FilesPath.SERIALIZABLE_PATH;

    /** The pa. */
    private MTApplication pa;

    /**
     * Instantiates a new serialize objects.
     *
     * @param pa the pa
     */
    public SerializeObjects(final MTApplication pa) {
        super();
        this.pa = pa;
    }


    /**
     * Il thread crea oggetti di domande e risposte; dopo di che vengono
     * richiamati metodi che serializzano ogni singolo oggetto.
     * 
     */
    @Override
    public final void run() {

        CurrentGameDifficolta currentDifficolta;

        currentDifficolta = new CurrentGameDifficolta(
                true, true, pa); // isItalian
        serializeCurrentDifficolta(currentDifficolta, "ita");
        currentDifficolta = new CurrentGameDifficolta(false, true, pa);
        serializeCurrentDifficolta(currentDifficolta, "eng");

        CurrentGameQuestions currentQuestions;

        currentDifficolta = new CurrentGameDifficolta(true, true, pa);
        for (DifficoltaDomande diff:currentDifficolta.getAllDifficolta()) {
            currentQuestions = new CurrentGameQuestions(pa, true, true, diff.
                    getId(), "playerITA", true);
              serializeCurrentGame(currentQuestions, 
                 "ita", diff.getLivelloDifficolta());
        }
        

        currentDifficolta = new CurrentGameDifficolta(false, true, pa);
        for (DifficoltaDomande diff:currentDifficolta.getAllDifficolta()) {
             currentQuestions = new CurrentGameQuestions(pa, false, true, diff.
                 getId(), "playerENG", true);
             serializeCurrentGame(currentQuestions, 
               "eng", diff.getLivelloDifficolta());
        }
        
    }

    
    /**
     * Serializza un oggetto di tipo CurrentGameDifficolta che modella le 
     * difficolta per le domande.
     *
     * @param c oggetto da serializzare
     * @param language indica la lingua del gioco
     */
    public final void serializeCurrentDifficolta(
            final CurrentGameDifficolta c, final String language) {
        ObjectOutputStream output = null;
        String fileName = "difficolta" + language + ".dat";
        
        try {
            output = new ObjectOutputStream(
                    new FileOutputStream(filePath + fileName));
            output.writeObject(c);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    
    /**
     * Serializza un oggetto di tipo CurrentGameQuestions che modella le
     *  domande.
     *
     * @param c oggetto da serializzare
     * @param language indica la lingua del gioco
     * @param difficolta indica il livello di difficolta per le domande da 
     * serializzare
     */
    public final void serializeCurrentGame(final CurrentGameQuestions c,
            final String language, final String difficolta) {

        String fileName = "questions" + language + difficolta + ".dat";

        
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(
                    filePath + fileName));
            output.writeObject(c);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
    }
    
}
