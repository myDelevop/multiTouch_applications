package wwf.interactiveMap.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.MultipleDragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import wwf.database.DBAccess;
import wwf.interactiveMap.data.SerializeObjects;
import wwf.interactiveMap.data.dao.DifficoltaDomandeDaoImpl;
import wwf.interactiveMap.data.dao.DomandaDaoImpl;
import wwf.interactiveMap.data.dao.RispostaDaoImpl;
import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.interactiveMap.util.FilesPath;
import wwf.todelete.Fonts;

// TODO: Auto-generated Javadoc
/**
 * Questa classe inizializza la componente {@link #root} di elementi grafici che
 *  consentono la visualizzazione della SplashScene.
 */
public class SplashScene {
    
    /** the pa*/
    private MTApplication pa;

    /** variabile destinata a contenere tutti gli elementi grafici della scena 
     * in questione. */
    private MTComponent root;
    
    /** Font bold 24 white. */
    private IFont bold24White;
    
    /** The bold 24 black. */
    private IFont bold24Black;


    /**
     * Costruttore parametrico che provvede all'inizializzazione 
     * della componente {@link #root}.
     *
     * @param mapScene the map scene
     * @param pa the pa
     */
    public SplashScene(final InteractiveMapSceneManager mapScene, 
            final MTApplication pa) {
        
        this.pa = pa;
        root = new MTComponent(pa);
        

        this.bold24White = Fonts.getInstance(pa).getSegoe50White();
        bold24Black = Fonts.getInstance(pa).getBold26Black();

        PImage image = pa.loadImage(FilesPath.IMAGES_PATH + "background2.png");
        MTImage layer = new MTImage(pa, image);
        layer.setPositionGlobal(new Vector3D(pa.width / 2, pa.height / 2));
        layer.setSizeXYGlobal(pa.width, pa.height);
        //MTRectangle layer = new MTRectangle(pa,0,0,pa.width,pa.height);
        //layer.setFillColor(new MTColor(00f, 151f, 167f));
        root.addChild(layer);
        
        MTRectangle titleBar = new MTRectangle(pa, 0, 0, pa.width, 143f);
        titleBar.setNoFill(true);
        titleBar.setNoStroke(true);
        titleBar.removeAllGestureEventListeners();
        titleBar.unregisterAllInputProcessors();
        titleBar.registerInputProcessor(new MultipleDragProcessor(pa));

        MTTextArea text = new MTTextArea(pa, bold24Black);
        text.setNoStroke(true);
        text.setNoFill(true);
        text.setText("INSERISCI IL TUO NOME PRIMA DI COMINCIARE");
        text.removeAllGestureEventListeners();
        text.unregisterAllInputProcessors();
        text.setPositionGlobal(new Vector3D(pa.width / 2f, 75f));

        titleBar.addChild(text);

        MTRectangle centralBar = new MTRectangle(pa, 
                0, 143, pa.width, pa.height - 143);
        centralBar.setNoFill(true);
        centralBar.setNoStroke(true);
        centralBar.removeAllGestureEventListeners();
        centralBar.unregisterAllInputProcessors();
        
        
        MTKeyboard keyb = new MTKeyboard(pa);
        keyb.setPositionGlobal(new Vector3D(pa.width / 2f, pa.height / 2f));
        keyb.setFillColor(new MTColor(30f, 30f, 30f, 210f));
        keyb.setStrokeColor(new MTColor(0, 0, 0, 255f));
        keyb.setWidthXYGlobal(pa.width * 0.5f);
        keyb.removeAllGestureEventListeners();
        
        final MTTextArea t = new MTTextArea(pa, FontManager.getInstance()
                .createFont(pa, "arial.ttf", 50, MTColor.BLACK)); 
        t.setExpandDirection(ExpandDirection.UP);
        t.setStrokeColor(new MTColor(0, 0, 0, 255f));
        t.setFillColor(new MTColor(205f, 200f, 177f, 255f));
        t.unregisterAllInputProcessors();
        //t.setEnableCaret(true);
        t.snapToKeyboard(keyb);
        keyb.addTextInputListener(t);


        MTImageButton button = new MTImageButton(pa, 
                pa.loadImage(FilesPath.IMAGES_PATH + "okButton.png"));
        button.setNoStroke(true);
        button.scale(0.4f, 0.4f, 1, new Vector3D(940f, 215f, 0),
                TransformSpace.LOCAL);
        button.translate(new Vector3D(0, 15, 0));
        button.setBoundsBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);


        button.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (e.getSource() instanceof MTComponent) {
                    switch (e.getID()) {
                    case TapEvent.TAPPED:
                        
                        mapScene.getTapButton().rewind();
                        mapScene.getTapButton().play();
                        
                        try {
                            checkUpdate(mapScene, true);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        mapScene.setItalian(true);
                        mapScene.buildLevelScene(t.getText());

                        t.clear();
                        break;
                        
                    default:
                        break;
                    }
                }
            }
        });
        keyb.addChild(button);

        centralBar.addChild(keyb);
        root.addChild(titleBar);
        root.addChild(centralBar);

    }

    /**
     * Check update.
     *
     * @param mapScene the map scene
     * @param isItalian the is italian
     * @throws SQLException the SQL exception
     */
    private void checkUpdate(final InteractiveMapSceneManager mapScene, 
            final boolean isItalian) throws SQLException {
        Connection connection = DBAccess.getConnection();
        Statement stmt = connection.createStatement();

        List<Date> dates = new ArrayList<Date>();
        List<String> tables = new ArrayList<String>();

        DifficoltaDomandeDaoImpl diffDao = new DifficoltaDomandeDaoImpl();
        DomandaDaoImpl domDao = new DomandaDaoImpl();
        RispostaDaoImpl rispDao = new RispostaDaoImpl();

        String fileName = "";

        if (isItalian) {
            tables.add(diffDao.getTableNameIt());
            tables.add(domDao.getTableNameIt());
            tables.add(rispDao.getTableNameIt());
            fileName = "mostRecentDataIt.dat";
        } else {
            tables.add(diffDao.getTableNameEn());
            tables.add(domDao.getTableNameEn());
            tables.add(rispDao.getTableNameEn());
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
            mostRecentDB = Collections.max(dates); // avr� sempre un solo
            // elemento            
        }
        Date mostRecentLocal = null;
        File file = new File(FilesPath.SERIALIZABLE_PATH + fileName);

        if (file.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(
                        FilesPath.SERIALIZABLE_PATH + fileName));
                mostRecentLocal = (Date) ois.readObject();
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
                out = new FileOutputStream(FilesPath.SERIALIZABLE_PATH 
                        + fileName);
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
                SerializeObjects serialize = new SerializeObjects(pa);
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
            //mapScene.setUpdateDB(true);
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
    
    

}