/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 C.Ruff, Fraunhofer-Gesellschaft 
 * All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package wwf.pictureBrowser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import wwf.pictureBrowser.util.ImageResizer;


/**
 * Classe SocialNetworkPicturesLoader, la quale provvede alla inizializzazione 
 * di tutte le foto che dovranno essere caricate nell'applicazione.
 */
public class SocialNetworkPicturesLoader extends SocialLoader {


	/** Foto da inizializzare. */
	private List<SocialNetworkPicture> mtFotos;


	/** The pa. */
	private PApplet pa;

	/** The get high resolution. */
	private boolean getHighResolution;

	/** Riferimento all'applicazione principale. */
	private PictureBrowser pictureBrowser;

	/**
	 * Costruttore parametrico.
	 *
	 * @param pa applicazione in questione
	 * @param sleepTime tempo di attesa per il thread
	 * @param pictureBrowser riferimento alla classe principale della 
	 *        applicazione corrente
	 * @param fotoCount numero di foto che devono essere caricate
	 */
	public SocialNetworkPicturesLoader(final PApplet pa, final long sleepTime,
	        final PictureBrowser pictureBrowser,
			final int fotoCount) {

		super(pa, sleepTime, pictureBrowser, fotoCount);

		this.pa = pa;
		mtFotos = new ArrayList<SocialNetworkPicture>();

		this.getHighResolution = true;
		this.pictureBrowser = pictureBrowser;
	}


	/**
	 * @see wwf.pictureBrowser.SocialLoader#addPictureToCanvas(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
    protected final void addPictureToCanvas(final String fotoUrl, 
            final String description, final String socialNetwork) {
	    
        String imagePath = "";
        try {
            imagePath = ImageResizer.resize(fotoUrl);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    PImage image = pa.loadImage(imagePath);
	    
		SocialNetworkPicture photo = new SocialNetworkPicture(pa,
		        image, socialNetwork);
		// Create image object
		photo.setName(fotoUrl);
		photo.setDescription(description);
		//photo.buildTextArea();
		pictureBrowser.addPhotoToCanvas(photo);
	}


	/**
	 * Restituisce le foto.
	 *
	 * @return lista di foto
	 */
	public final SocialNetworkPicture[] getMtFotos() {
		return this.mtFotos.toArray(
		        new SocialNetworkPicture[this.mtFotos.size()]);
	}
	/**
	 * Checks if is gets the high resolution.
	 *
	 * @return true, if is gets the high resolution
	 */
	public final boolean isGetHighResolution() {
		return getHighResolution;
	}

	/**
	 * Sets the gets the high resolution.
	 *
	 * @param getHighResolution the new gets the high resolution
	 */
	public final void setGetHighResolution(final boolean getHighResolution) {
		this.getHighResolution = getHighResolution;
	}
}