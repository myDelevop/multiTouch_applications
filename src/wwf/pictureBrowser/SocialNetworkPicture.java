package wwf.pictureBrowser;

import java.io.IOException;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;
import wwf.pictureBrowser.util.FilesPath;
import wwf.pictureBrowser.util.ImageResizer;

/**
 * classe che rappresenta gli oggetti delle foto da visualizzare, ogni foto 
 * infatti, deve avere una descrizione e un socialnetwork di appartenenza.
 */
public class SocialNetworkPicture extends MTImageCustomized {
	
	/** descrizione della foto. */
	private String description = "";
	
	/** Nome del social network. */
	private String socialNetwork = "";
	
	/** The p applet. */
	private PApplet pApplet;
	
	/**
	 * Costruttore parametrico.Viene anche inserita una iconcina che identifica 
	 * il particolare social network.
	 *
	 * @param pApplet the applet
	 * @param texture immagine da visualizzare
	 * @param socialNetwork nome del social network
	 */
	public SocialNetworkPicture(final PApplet pApplet, final PImage texture, 
	        final String socialNetwork) {
		super(pApplet, texture);
		this.pApplet = pApplet;
		this.socialNetwork = socialNetwork;
		
		MTRectangle infoSocial; 
		
		String facebookImage = "";
        String instagramImage = "";
        String twitterImage = "";
        String flickrImage = "";
		
        try {
            facebookImage = ImageResizer.resize(FilesPath.IMAGES_PATH 
                    + "facebook.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            instagramImage = ImageResizer.resize(FilesPath.IMAGES_PATH 
                    + "instagram.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            twitterImage = ImageResizer.resize(FilesPath.IMAGES_PATH 
                    + "twitter.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            flickrImage = ImageResizer.resize(FilesPath.IMAGES_PATH 
                    + "flickr.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

		
		if (socialNetwork.equalsIgnoreCase("facebook")) {
			infoSocial = new MTRectangle(pApplet,
					pApplet.loadImage(facebookImage));		
		} else if (socialNetwork.equalsIgnoreCase("instagram")) {
			infoSocial = new MTRectangle(pApplet,
					pApplet.loadImage(instagramImage));		
		} else if ((socialNetwork.equalsIgnoreCase("twitter"))) {
			infoSocial = new MTRectangle(pApplet,
					pApplet.loadImage(twitterImage));		
		} else if (socialNetwork.equalsIgnoreCase("flickr")) {
			infoSocial = new MTRectangle(pApplet,
					pApplet.loadImage(flickrImage));		
		} else {
			infoSocial = new MTRectangle(pApplet, 0, 0, 0, 0);		
		}
		infoSocial.setWidthLocal(160f);
		infoSocial.setHeightLocal(160f);
		


		infoSocial.setPositionRelativeToParent(new Vector3D(
				this.getWidthXYGlobal()
				- (infoSocial.getWidthXY(TransformSpace.GLOBAL) / 2f),
				this.getHeightXYGlobal() 
				- (infoSocial.getHeightXY(TransformSpace.GLOBAL) / 2f)));
		infoSocial.setNoFill(false);
		infoSocial.setNoStroke(true);
		
		infoSocial.unregisterAllInputProcessors();
		infoSocial.removeAllGestureEventListeners();
		
		this.addChild(infoSocial);

	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}
}
