package wwf.ClassificationGame.view.game;

import static wwf.ClassificationGame.util.Utility.*;
import java.sql.SQLException;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import wwf.ClassificationGame.sceneManager.CGSceneManager;
import wwf.ClassificationGame.util.MyColors;

public class GameRegole {
	private MTComponent root;
	private CGSceneManager cgScene;

	public GameRegole(CGSceneManager cgScene, MTApplication app) throws SQLException {
		this.root = new MTComponent(app);
		this.cgScene = cgScene;

		this.cgScene.setClearColor(MyColors.lightorange());
		buildIntestazione(app, cgScene, root, "gioco");
		attachEvent(cgScene, app, root, "regole", "regole");
		buildRegoleEFineGioco(cgScene, app, root, "regoleGioco", null);
	}

	public MTComponent getRootComponent() {
		return root;
	}
}