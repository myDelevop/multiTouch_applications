package wwf.ClassificationGame;

import java.sql.SQLException;

import org.mt4j.MTApplication;

import wwf.ClassificationGame.sceneManager.CGSceneManager;

public class StartCGExample extends MTApplication {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) {
		initialize();
	}

	@Override
	public void startUp() {
		try {
			this.addScene(new CGSceneManager(this, "Classification Game"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
