package wwf.ClassificationGame.view.game;

import static wwf.ClassificationGame.util.Utility.*;
import java.sql.SQLException;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.sceneManager.Giocatore;
import wwf.ClassificationGame.util.MyColors;

public class FineGioco {
	private MTComponent root;
	private CGSceneManager cgScene;
	
	public FineGioco(CGSceneManager cgScene, MTApplication app, Giocatore g) throws SQLException {
		this.root = new MTComponent(app);
		this.cgScene = cgScene;
		
		this.cgScene.setClearColor(MyColors.lightorange());
		buildIntestazione(app, cgScene, root, "gioco");
		buildRegoleEFineGioco(cgScene, app, root, "fineGioco", g.getPunteggio());
		CGSceneManager.loadClassi();
		CGSceneManager.loadCategorie();
		CGSceneManager.loadEntita();
	}

	public MTComponent getRootComponent() {
		return root;
	}
}