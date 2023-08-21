package wwf.interactiveMap.view;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import wwf.interactiveMap.sceneManager.InteractiveMapSceneManager;
import wwf.interactiveMap.sceneManager.score.HistoricalPlayers;
import wwf.interactiveMap.sceneManager.score.Player;
import wwf.interactiveMap.util.FilesPath;
import wwf.todelete.Fonts;

// TODO: Auto-generated Javadoc
/**
 * Questa classe inizializza la componente {@link #root} di elementi grafici che
 * consentono la visualizzazione della ScoreScene.
 */
public class ScoreScene {

    /**
     * variabile destinata a contenere tutti gli elementi grafici della scena in
     * questione.
     */
    private MTComponent root;

    /** Font semi light 32 black. */
    private IFont semiLight32Black;

    /** Font bold 26 black. */
    private IFont bold26Black;

    /** The background. */
    private MTBackgroundImage background;

    /** The text. */
    private MTTextArea text;

    /** The text name players. */
    private MTTextArea textNamePlayers;

    /** The text score. */
    private MTTextArea textScore;

    /** The all players. */
    private HistoricalPlayers allPlayers;

    /**
     * Costruttore parametrico che provvede all'inizializzazione della
     * componente {@link #root}.
     *
     * @param mapScene the map scene
     * @param pa            the pa
     */
    public ScoreScene(final InteractiveMapSceneManager mapScene, 
            final MTApplication pa) {

        this.root = new MTComponent(pa);
        this.background = new MTBackgroundImage(pa, pa.loadImage(
                FilesPath.IMAGES_PATH + "background2.png"), false);

        this.bold26Black = Fonts.getInstance(pa).getBold26Black();

        root.addChild(background);

        int maxPlayers = 10;
        
        this.allPlayers = new HistoricalPlayers();

                if (this.allPlayers.getAllPlayers().size() >= maxPlayers) {
                    this.allPlayers.setAllPlayers(this.allPlayers
                            .getAllPlayers().subList(0, maxPlayers - 1));
                }
               this.allPlayers.insertPlayer(mapScene
                       .getCurrentGameQuestions().getCurrentPlayer());
               
               int numberOfPlayers = this.allPlayers.getAllPlayers().size();

        
        /// VIEW
        MTRectangle rect = new MTRectangle(pa, 0, 0, pa.width, pa.height);
        
        
        float centralPadding = 120f;

        MTRectangle centralLayer = new MTRectangle(pa, 
                pa.width / 3f, pa.height - 2 * centralPadding);
        
        root.addChild(rect);
        root.addChild(centralLayer);

        centralLayer.setAnchor(PositionAnchor.CENTER);
        centralLayer.setPositionGlobal(rect.getCenterPointGlobal());

        float padding = 20f;
        float height = (centralLayer.getHeightXY(TransformSpace.GLOBAL) 
                - padding * (numberOfPlayers + 1)) / numberOfPlayers;

        centralLayer.setAnchor(PositionAnchor.UPPER_LEFT);
        IFont font = Fonts.getInstance(pa).getBold26Black();


        MTRectangle layer;
        MTTextArea text;

        int i = 0;
        for (Player p : allPlayers.getAllPlayers()) {
             layer = new MTRectangle(pa, 
                    centralLayer.getPosition(TransformSpace.GLOBAL).x, 
                    centralLayer.getPosition(TransformSpace.GLOBAL).y 
                    + ((i + 1) * padding) + i * height,
                    centralLayer.getWidthXY(TransformSpace.GLOBAL),
                    height);
            text = new MTTextArea(pa, font);
            layer.setAnchor(PositionAnchor.UPPER_LEFT);

            layer.addChild(text);
            layer.setNoFill(true);
            text.setNoFill(true);
            String name = "player";
            if (!p.getName().equals("")) {                
                name = p.getName();
            }
            text.setText(name + "  " + p.getScore());
            text.removeAllGestureEventListeners();
            text.unregisterAllInputProcessors();
            layer.removeAllGestureEventListeners();
            layer.unregisterAllInputProcessors();
            text.setNoStroke(true);
            text.setAnchor(PositionAnchor.UPPER_LEFT);
            text.setPositionGlobal(new Vector3D(
                    layer.getPosition(TransformSpace.GLOBAL).x,
                    layer.getPosition(TransformSpace.GLOBAL).y));

            layer.setStrokeColor(MTColor.GREEN);
            layer.setNoStroke(true);

            root.addChild(layer);
            i++;
        }


        centralLayer.setVisible(false);
        centralLayer.setStrokeColor(MTColor.YELLOW);
        rect.setVisible(false);
        rect.setStrokeColor(MTColor.RED);

    }

    /**
     * Restituisce la radice degli elementi grafici che costituiscono la scena
     * in questione.
     * 
     * @return component root
     */
    public final MTComponent getRootComponent() {
        return root;
    }
}