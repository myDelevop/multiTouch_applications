/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 C.Ruff, 
 * Fraunhofer-Gesellschaft All rights reserved.
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
package wwf.pictureBrowser.saveLocalPictures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import wwf.pictureBrowser.PictureBrowser;
import wwf.pictureBrowser.SocialNetworkPicture;
import wwf.pictureBrowser.util.FilesPath;


/**
 * Classe SocialNetworkPicturesSaver, la quale provvede alla inizializzazione 
 * di tutte le foto che dovranno essere salvate in locale.
 */
public class SocialNetworkPicturesSaver extends SocialSaver {

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
	 * @param pa the pa
	 * @param pictureBrowser riferimento alla classe principale 
	 * della applicazione corrente.
	 */
	public SocialNetworkPicturesSaver(final PApplet pa, 
	        final PictureBrowser pictureBrowser) {

		super(pa, pictureBrowser);

		this.pa = pa;
		mtFotos = new ArrayList<SocialNetworkPicture>();

		this.getHighResolution = true;
		this.pictureBrowser = pictureBrowser;
	}


	/**
  	 * @see wwf.pictureBrowser.saveLocalPictures.SocialSaver#saveLocalPicture(
  	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
  	 *  java.lang.String)
	 */
	@Override
    protected final void saveLocalPicture(final String id,
            final String fileURL, final String description,
            final String socialNetwork, final String hashTag) 
			        throws IOException {
	    
		String saveDir = "";
		if (socialNetwork.equalsIgnoreCase("facebook")) {
			saveDir = FilesPath.OFFLINE_FACEBOOK_FOTOS_PATH;			
		} else if (socialNetwork.equalsIgnoreCase("instagram")) {
			saveDir = FilesPath.OFFLINE_INSTAGRAM_FOTOS_PATH;
		} else if (socialNetwork.equalsIgnoreCase("twitter")) {
			saveDir = FilesPath.OFFLINE_TWITTER_FOTOS_PATH;
		} else if (socialNetwork.equalsIgnoreCase("flickr")) {
			saveDir = FilesPath.OFFLINE_FLICKR_FOTOS_PATH;			
		}
		
		String file = "";
		if (id != null) {
            file = file.concat(id);
        } else if (description != null) {
            file = file.concat(description);
        } else {
            file = "default";
        }

		File f = new File(saveDir + file + ".jpg");
		if (!f.exists()) {
			int bufferSize = 4096;
			URL url = new URL(fileURL);
	        HttpURLConnection httpConn = (HttpURLConnection) url.
	                openConnection();
	        int responseCode = httpConn.getResponseCode();
	 
	        // always check HTTP response code first
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            String fileName = "";
	            String disposition = httpConn.getHeaderField(
	                    "Content-Disposition");
	            String contentType = httpConn.getContentType();
	            int contentLength = httpConn.getContentLength();
	 
	            if (disposition != null) {
	                // extracts file name from header field
	                int index = disposition.indexOf("filename=");
	                if (index > 0) {
	                    fileName = disposition.substring(index + 10,
	                            disposition.length() - 1);
	                }
	            } else {
	                // extracts file name from URL
	                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
	                        fileURL.length());
	            }
	 
	            System.out.println("Content-Type = " + contentType);
	            System.out.println("Content-Disposition = " + disposition);
	            System.out.println("Content-Length = " + contentLength);
	            System.out.println("fileName = " + fileName);
	 
	            // opens input stream from the HTTP connection
	            InputStream inputStream = httpConn.getInputStream();
	            String saveFilePath = saveDir + File.separator + file + ".jpg";
	             
	            // opens an output stream to save into file
	            FileOutputStream outputStream =
	                    new FileOutputStream(saveFilePath);
	 
	            int bytesRead = -1;
	            byte[] buffer = new byte[bufferSize];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	 
	            outputStream.close();
	            inputStream.close();
	 
	            System.out.println("File downloaded");
	        } else {
	            System.out.println("No file to download."
	                    + " Server replied HTTP code: " + responseCode);
	        }
	        httpConn.disconnect();
		} else {
			f.setLastModified(System.currentTimeMillis());
		}
	
	}


	/**
	 * @see wwf.pictureBrowser.saveLocalPictures.
	 * SocialSaver#saveLocalPictureOffline(java.io.File, java.lang.String,
	 *  java.lang.String)
	 */
	@Override
    protected final void saveLocalPictureOffline(final File file,
            final String socialNetwork, final String hashTag)  
                    throws IOException  {
		
		System.out.println(">>>> i'm here...");
		String fileName = file.getName();
		String path2 = "";
		
		if (socialNetwork.equalsIgnoreCase("facebook")) {
			path2 = FilesPath.OFFLINE_FACEBOOK_FOTOS_PATH;			
		} else if (socialNetwork.equalsIgnoreCase("instagram")) {
			path2 = FilesPath.OFFLINE_INSTAGRAM_FOTOS_PATH;
		} else if (socialNetwork.equalsIgnoreCase("twitter")) {
			path2 = FilesPath.OFFLINE_TWITTER_FOTOS_PATH;
		} else if (socialNetwork.equalsIgnoreCase("flickr")) {
			path2 = FilesPath.OFFLINE_FLICKR_FOTOS_PATH;			
		}
		File exist = new File(path2 + fileName);
		if (!exist.exists()) {
			File in = file;
			File out = new File(path2 + fileName);
			
			FileInputStream fin = new FileInputStream(in);
			FileOutputStream fout = new FileOutputStream(out);
			FileChannel inChannel = fin.getChannel();
		        FileChannel outChannel = fout.getChannel();
		        try {
		            inChannel.transferTo(0, inChannel.size(),
		                    outChannel);
		        } catch (IOException e) {
		            throw e;
		        } finally {
		        	fin.close();
		        	fout.close();
		        	
		            if (inChannel != null) {
		            	
		            	inChannel.close();
		            }
		            if (outChannel != null) {
		            	outChannel.close();
		            }
		        }
		} else {
			exist.setLastModified(System.currentTimeMillis());
		}
        System.out.println("<<<<<'im not here");
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