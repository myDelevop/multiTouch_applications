package wwf.interactiveMap.sceneManager.score;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wwf.interactiveMap.util.FilesPath;

// TODO: Auto-generated Javadoc
/**
 * Lista dei giocatori storici.
 */
public class HistoricalPlayers implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** Lista di giocatori. */
    private List<Player> allPlayers;

    /**
     * Costruttore di default. Se il file esiste l'oggetto viene deserializzato,
     * altrimenti crea una lista vuota e serializza con il giocatore corrente.
     */
    public HistoricalPlayers() {
        String fileName = FilesPath.SERIALIZABLE_SCORE_PATH + "allPlayers.dat";
        File file = new File(fileName);

        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(fileName));
                HistoricalPlayers istance = (HistoricalPlayers) 
                        ois.readObject();
                this.setAllPlayers(istance.getAllPlayers());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            allPlayers = new ArrayList<Player>();
        }

    }

    /**
     * Insert player.
     *
     * @param p the p
     */
    public final void insertPlayer(final Player p) {
        this.allPlayers.add(p);
        
        ObjectOutputStream output = null;
        String fileName = FilesPath.SERIALIZABLE_SCORE_PATH + "allPlayers.dat";
        
        try {
            output = new ObjectOutputStream(
                    new FileOutputStream(fileName));
            output.writeObject(this);
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
     * Gets the all players.
     *
     * @return the all players
     */
    public final List<Player> getAllPlayers() {
        return allPlayers;
    }

    /**
     * Sets the all players.
     *
     * @param allPlayers the new all players
     */
    public final void setAllPlayers(final List<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }

}
