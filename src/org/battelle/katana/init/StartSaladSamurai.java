package org.battelle.katana.init;
import java.util.ArrayList;

import org.battelle.katana.scenes.MenuScene;
import org.battelle.katana.sound.SoundFactory;
import org.battelle.katana.sound.SoundType;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.mesh.MTCube;
import org.mt4j.components.visibleComponents.shapes.mesh.MTSphere;
import org.mt4j.input.inputSources.MacTrackpadSource;
import org.mt4j.util.MTColor;

/**
 * Starts the Salad Samurai Game
 */
public class StartSaladSamurai extends MTApplication {

	private static final long serialVersionUID = -3546196759589208526L;
	private SoundFactory soundFactory = SoundFactory.getInstance();

	public static void main(String[] args) {
		initialize();
	}

	@Override
	public void startUp() {
		String osName = System.getProperty("os.name");
		if (!osName.toLowerCase().contains("windows")) {
			getInputManager().registerInputSource(new MacTrackpadSource(this));
		}
		
		soundFactory.generateSound(SoundType.BGMUSIC).start();
		ArrayList<MTComponent> fruitList = new ArrayList<MTComponent>();
		
		fruitList.add(new MTSphere(this, "sphereOne", 20, 20, 80));
		fruitList.add(new MTSphere(this, "sphereOne", 20, 20, 90));
		fruitList.add(new MTSphere(this, "sphereOne", 20, 20, 100));
		MTSphere bomb = new MTSphere (this, "bomb", 20, 20, 60);
		MTCube powerup = new MTCube (this, 100);
		MTColor textColor = new MTColor(MTColor.WHITE);
		MTColor trailColor = new MTColor(MTColor.TEAL);
		addScene(new MenuScene(this, "Menu Scene", fruitList, bomb, powerup, textColor, trailColor));
	}

}
