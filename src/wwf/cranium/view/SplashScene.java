package wwf.cranium.view;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.cranium.data.dao.DifficoltaDomandeDaoImpl;
import wwf.cranium.data.dao.DomandaDaoImpl;
import wwf.cranium.data.dao.GiocatoreDaoImpl;
import wwf.cranium.data.dao.RispostaDaoImpl;
import wwf.cranium.data.dao.TipologiaDomandeDaoImpl;
import wwf.cranium.sceneManager.CraniumSceneManager;
import wwf.cranium.util.FilesPath;
import wwf.database.DBAccess;
import wwf.cranium.data.SerializeObjects;
import wwf.todelete.Fonts;


// TODO: Auto-generated Javadoc
/**
 * Questa classe inizializza la componente {@link #root} di elementi grafici che
 *  consentono la visualizzazione della SplashScene.
 */
public class SplashScene {
	
    
	/** variabile destinata a contenere tutti gli elementi grafici della scena 
	 * in questione. */
	private MTComponent root;
	
	/** Font semi light 24. */
	private IFont semiLight24;
	
	/**
	 * Costruttore parametrico che provvede all'inizializzazione 
	 * della componente {@link #root}.
	 *
	 * @param craniumScene gestore delle schermate
	 * @param pa the pa
	 */
	public SplashScene(final CraniumSceneManager craniumScene, 
	        final MTApplication pa) {

		root = new MTComponent(pa);
		this.semiLight24 = Fonts.getInstance(pa).getSeguisb36White();
		
		MTBackgroundImage background = new MTBackgroundImage(
		        pa, pa.loadImage(FilesPath.ROOT + "sea.jpg"), false);
	    root.addChild(background);
		
		MTRectangle titleBar = new MTRectangle(pa, 0, 0, pa.width, 143);
		titleBar.setNoStroke(true);
		titleBar.setNoFill(true);
		titleBar.setFillColor(new MTColor(0, 0, 0, 0));
		titleBar.removeAllGestureEventListeners();
		titleBar.unregisterAllInputProcessors();

		
		
	    MTTextArea text = new MTTextArea(pa, 
	    		pa.width / 4 - (pa.width / 2 - 50) / 2,
	    		7,
	    		pa.width * 3 / 4 + (pa.width / 2 - 50) / 2, 
	    		250);



	    text.setFont(semiLight24);
	    text.setNoStroke(true);
		text.setNoFill(true);
	    text.setText("Gioca a CraniumWWF: sfida il computer o i tuoi amici "
	            + "rispondendo a domande sulla flora e la fauna della riserva "
	            + "di Torre Guaceto e libera l'attivista che c'è in te!");


	    text.removeAllGestureEventListeners();
	    text.unregisterAllInputProcessors();


	    titleBar.addChild(text);


	    

		MTRectangle centralBar = new MTRectangle(
		        pa, 0, 143f, pa.width, pa.height - 143f);
		centralBar.setNoFill(true);
		centralBar.setNoStroke(true);
		centralBar.removeAllGestureEventListeners();
		centralBar.unregisterAllInputProcessors();

		MTRectangle englishButton = new MTRectangle(
		        pa, pa.width / 2 - 50, pa.height - 143 - 280);
		englishButton.setPositionGlobal(new Vector3D(
		        pa.width / 4, pa.height / 2 
		        + (semiLight24.getOriginalFontSize())));
		englishButton.setNoStroke(true);
		englishButton.setTexture(pa.loadImage(FilesPath.ROOT + "en.png"));
		englishButton.removeAllGestureEventListeners();
		englishButton.unregisterAllInputProcessors();
	   
	    englishButton.registerInputProcessor(new TapProcessor(pa));
	    englishButton.addGestureListener(TapProcessor.class, 
	            new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
					try {
                        checkUpdate(craniumScene, false);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
					craniumScene.setItalian(false);
					craniumScene.buildNumPlayersScene();
				}
				return false;
			}
		});
	    
		MTRectangle italianButton = new MTRectangle(pa, 
		        pa.width / 2 - 50, pa.height - 143 - 280);
		italianButton.setPositionGlobal(new Vector3D(pa.width * 3 / 4,
		        pa.height / 2 + (semiLight24.getOriginalFontSize())));
		italianButton.setNoStroke(true);
		italianButton.setTexture(pa.loadImage(FilesPath.ROOT + "it.png"));
		italianButton.removeAllGestureEventListeners();
		italianButton.unregisterAllInputProcessors();
	    

		italianButton.registerInputProcessor(new TapProcessor(pa));
		italianButton.addGestureListener(TapProcessor.class,
		        new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(final MTGestureEvent ge) {
				TapEvent te = (TapEvent) ge;
				if (te.getTapID() == TapEvent.TAPPED) {
					craniumScene.getAudioTapButtons().rewind();
					craniumScene.getAudioTapButtons().play();
	                   try {
	                        checkUpdate(craniumScene, true);
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                    }
					craniumScene.setItalian(true);
					craniumScene.buildNumPlayersScene();
				}
				return false;
			}
		});
	    
	    
	    MTComponent upperLayer = new MTComponent(pa);
	    upperLayer.addChild(englishButton);
	    upperLayer.addChild(italianButton);




		root.addChild(titleBar);
		root.addChild(centralBar);
		root.addChild(upperLayer);
		
		
	}
	
	
    /**
     * Questo metodo controlla se nel database ci sono delle modifiche; in tal
     * caso le domande vengono caricate direttamente dal database, altrimenti
     * poichè non ci sono aggiornamenti, le domande vengono deserializzate da 
     * file per una maggiore efficienza.
     *
     * @param craniumScene the cranium scene
     * @param isItalian the is italian
     * @throws SQLException the SQL exception
     */
    private void checkUpdate(final CraniumSceneManager craniumScene,
            final boolean isItalian) throws SQLException {
        Connection connection = DBAccess.getConnection();
        Statement stmt = connection.createStatement();

        List<Date> dates = new ArrayList<Date>();
        List<String> tables = new ArrayList<String>();

        DifficoltaDomandeDaoImpl diffDao = new DifficoltaDomandeDaoImpl();
        DomandaDaoImpl domDao = new DomandaDaoImpl();
        GiocatoreDaoImpl giocDao = new GiocatoreDaoImpl();
        RispostaDaoImpl rispDao = new RispostaDaoImpl();
        TipologiaDomandeDaoImpl tipDao = new TipologiaDomandeDaoImpl();
        
        String fileName = "";

        if (isItalian) {
            tables.add(diffDao.getTableNameIt());
            tables.add(domDao.getTableNameIt());
            tables.add(giocDao.getTableNameIt());
            tables.add(rispDao.getTableNameIt());
            tables.add(tipDao.getTableNameIt());
            fileName = "mostRecentDataIt.dat";
        } else {
            tables.add(diffDao.getTableNameEn());
            tables.add(domDao.getTableNameEn());
            tables.add(giocDao.getTableNameEn());
            tables.add(rispDao.getTableNameEn());
            tables.add(tipDao.getTableNameEn());
            fileName = "mostRecentDataEn.dat";
        }

        ResultSet rs = null;

        String query = "SELECT MAX(time) FROM (";

        Iterator<String> it = tables.iterator();
        while (it.hasNext()) {
            String tableName = it.next();

            query = query.concat(" SELECT ");
            query = query.concat(tableName);
            query = query.concat(".create_time as time");
            query = query.concat(" FROM ");
            query = query.concat(tableName);
            if (it.hasNext()) {                
                query = query.concat(" UNION ");
            }
        }
        query = query.concat(") foo;");

        rs = stmt.executeQuery(query);
        while (rs.next()) {
            if (rs.getTimestamp(1) != null) {
                Date data = new Date(rs.getTimestamp(1).getTime());
                dates.add(data);
            }
        }


        Date mostRecentDB = dates.get(0);
        if (dates.size() > 0) {
            mostRecentDB = Collections.max(dates); // avrà sempre un solo
            // elemento            
        }
        System.out.println("Data remota " + mostRecentDB);
        
        Date mostRecentLocal = null;

        File file = new File(FilesPath.SERIALIZABLE_PATH + fileName);

        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(
                        FilesPath.SERIALIZABLE_PATH + fileName));
                mostRecentLocal = (Date) ois.readObject();
                System.out.println("File locale " + mostRecentLocal.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
        	mostRecentLocal = new Date(2000);
            FileOutputStream out;
			try {
				out = new FileOutputStream(
				        FilesPath.SERIALIZABLE_PATH + fileName);
				ObjectOutputStream oout = new ObjectOutputStream(out);

	            // write something in the file
	            oout.writeObject(mostRecentLocal);
	            // close the stream
	            oout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }

        if (mostRecentLocal.compareTo(mostRecentDB) < 0) {
            ObjectOutputStream output = null;

            try {
                SerializeObjects serialize = new SerializeObjects();
                serialize.run();
                output = new ObjectOutputStream(new FileOutputStream(
                        FilesPath.SERIALIZABLE_PATH + fileName));
                output.writeObject(mostRecentDB);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //craniumScene.setUpdateDB(true);
            
        }
    }


	/**
	 * Restituisce la radice degli elementi grafici che costituiscono 
	 * la scena in questione.
	 *
	 * @return component root
	 */
	public final MTComponent getRootComponent() {
		return root;
	}
	
	
/*	public static void main(String[] args) {
        ObjectOutputStream output = null;
        
        Date mostRecentDB = new Date(200);

        String fileName = "mostRecentDataEn.dat";

        try {
            output = new ObjectOutputStream(new FileOutputStream(
            FilesPath.SERIALIZABLE_PATH + fileName));
            output.writeObject(mostRecentDB);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
