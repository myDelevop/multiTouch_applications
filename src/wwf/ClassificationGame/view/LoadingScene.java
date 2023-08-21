package wwf.ClassificationGame.view;

import static wwf.ClassificationGame.util.Utility.buildIntestazione;
import static wwf.ClassificationGame.sceneManager.CGSceneManager.*;

import java.sql.SQLException;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.widgets.progressBar.MTProgressBar;

import wwf.ClassificationGame.sceneManager.CGSceneManager;

public class LoadingScene {
	private MTComponent root;
	private MTApplication context;
	private CGSceneManager cgScene;

	public LoadingScene(CGSceneManager cgScene, MTApplication app) throws SQLException {
		root = new MTComponent(app);
		this.context = app;
		this.cgScene = cgScene;

		buildIntestazione(app, cgScene, root, "splash");

		//inizialiteData(app);
	}

	public void inizialiteData(MTApplication app) throws SQLException {
		// Load from classpath
		MTProgressBar progressBar = new MTProgressBar(app, app.createFont("arial", 35));

		progressBar.setDepthBufferDisabled(true);
		progressBar.setPickable(false);

		root.addChild(progressBar);
		for (int i = 0; i < 5; i++) {
			switch (i) {
			case 1:
				loadClassi();
				break;
			case 2:
				loadCategorie();
				break;
			case 3:
				loadEntita();
				break;
			case 4:
				progressBar.setVisible(false);
				break;
			}
			System.out.println(i);
		}
	}

	public MTComponent getRootComponent() {
		return root;
	}
}
