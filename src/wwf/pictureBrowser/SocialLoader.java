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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.progressBar.AbstractProgressThread;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.places.Place;
import com.flickr4java.flickr.places.PlacesInterface;
import com.flickr4java.flickr.places.PlacesList;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.auth.AccessToken;
import processing.core.PApplet;
import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import wwf.pictureBrowser.data.CurrentKeywords;
import wwf.pictureBrowser.data.CurrentSocials;
import wwf.pictureBrowser.data.model.Keyword;
import wwf.pictureBrowser.data.model.SocialNetwork;
import wwf.pictureBrowser.util.FilesPath;


/**
 * Questo thread provvede al caricamento di foto da vari social networks 
 * in modo che potranno essere subito visualizzate in un secondo momento, 
 * lo stato del caricamento è mostrato da una progress bar.
 */
public class SocialLoader extends AbstractProgressThread {
	
	/** The use places for geo search. */
	private boolean usePlacesForGeoSearch;

	/** The mt application. */
	private MTApplication mtApplication;

	/** The search parameters. */
	private SearchParameters searchParameters;


	/** The foto count. */
	private int fotoCount;


	/** The search page offset. */
	private int searchPageOffset;


	/** The flickr key. */
	private String flickrKey;


	/** The flickr secret. */
	private String flickrSecret;


	/** The f. */
	private Flickr f;


	/** The photos. */
	private List<Photo> photos;

	/** The pa. */
	private PApplet pa;

	/** The picture browser. */
	private PictureBrowser pictureBrowser;

	/** The keywords. */
	private CurrentKeywords keywords;
	
	/** The socials. */
	private CurrentSocials socials;
	
	/** The current foto. */
	private Integer currentFoto;




	/**
	 * Costruttore parametrico .
	 *
	 * @param pa applicazione corrente
	 * @param sleepTime numero di secondi di attesa per il thread
	 * @param pictureBrowser riferimento alla classe principale 
	 * dell' applicazione
	 * @param fotoCount numero di foto che dveono essere caricate
	 */
	public SocialLoader(final PApplet pa, final long sleepTime, 
	        final PictureBrowser pictureBrowser, final int fotoCount) {
		
		super(sleepTime);

		this.pictureBrowser = pictureBrowser;
		this.keywords = new CurrentKeywords(pictureBrowser.isUpdateDB());
		this.socials = new CurrentSocials(pictureBrowser.isUpdateDB());
		this.fotoCount = fotoCount;
		this.currentFoto = new Integer(0);
	}
	

	/**
	 * Istanzia l'oggetto {@link #f} usando le apiKey presenti 
	 * nel file di configurazione e istanzia anche {@link #searchParameters} 
	 * usando gli hashtag desiderati.
	 *
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void buildFlickrParameter() throws Exception {
		Properties properties = new Properties();

		try {
			InputStream in = null;
			try {
				in = new FileInputStream(FilesPath.APIKEYS_PATH
				        + "FlickrApiKey.txt");
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
			}

			if (in == null) {
				try {
					in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(FilesPath.APIKEYS_PATH
							        + "FlickrApiKey.txt");
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
					throw e;
				}
			}
			properties.load(in);

			flickrKey = properties.getProperty("FlickrApiKey", " ");
			flickrSecret = properties.getProperty("FlickrSecret", " ");
		} catch (Exception e) {
			System.err.println("Error while loading FlickrApiKey.txt file.");
			e.printStackTrace();
			throw e;
		}


		searchParameters = new SearchParameters();


		List<String> hashTags = new ArrayList<String>();

		for (Keyword key:keywords.getAllKeywords()) {
			if (getSocialNetworkByID(key.getSocialId())
			        .equalsIgnoreCase("flickr")) {
				hashTags.add(key.getKeyword());
				hashTags.add("#" + key.getKeyword());
			}
		}

		String[] arrayTags = new String[hashTags.size()];
		arrayTags = hashTags.toArray(arrayTags);

		searchParameters.setTags(arrayTags);
		//searchParameters.setText("olimpiadi");
		//sp.setTags(new String[]{t.getText()});
		searchParameters.setSort(SearchParameters.RELEVANCE);

		System.out.println("Flickr search for: \"" + "WWF" + "\"");

		f = new Flickr(flickrKey, flickrSecret, new REST());
		//f.getAuthInterface();
		RequestContext.getRequestContext();

		this.searchPageOffset = 0;
		this.photos = new ArrayList<Photo>();

		this.usePlacesForGeoSearch = true;
	}


	/**
	 * Il metodo run chiama i metodi che provvederanno al caricamento 
	 * delle foto dai social network specificati.
	 * 
	 */
	@Override
    public final void run() {

		pictureBrowser.getProgressBar().setVisible(true);

		float numSocials = 4;
		
		this.setTarget(fotoCount * numSocials);

		loadInstagramPicturesLocal(); // using ifttt
		loadFacebookPicturesLocal(); // using ifttt
		try {
			loadFlickrPictures(); 			
		} catch (Exception e) {
			e.printStackTrace();
			loadLocalPictures(FilesPath.OFFLINE_FLICKR_FOTOS_PATH, "flickr");
		}
		try {
			loadTwitterPictures();
		} catch (Exception e) {
			e.printStackTrace();
			loadLocalPictures(FilesPath.OFFLINE_TWITTER_FOTOS_PATH, "twitter");
		}
		pictureBrowser.getProgressBar().setVisible(false);
	}


	/**
	 * Questo metodo carica le foto da flickr usando le apikey del file 
	 * di configurazione.
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadFlickrPictures() throws Exception {

		int loadedFotosCount = 0;
		
		buildFlickrParameter();

		if (this.getSearchParameters() != null) {


			boolean isGeoSearch = this.isGeoSearch(this.getSearchParameters());
			PhotoList<Photo> fotoList = this.getSearchedFotoList(
			        this.getSearchParameters(), this.getFotoLoadCount(), 
			        this.getSearchPageOffset(), isGeoSearch);

			if (fotoList != null && fotoList.size() > 0) {
				for (int i = 0; i < fotoCount; i++) {

					try {
						Thread.sleep(this.getSleepTime()); 
					} catch (InterruptedException e) {
						e.printStackTrace();
						this.setFinished(true);
						throw e;
					}

					Photo foto = (Photo) fotoList.get(i);
					String fotoName = foto.getTitle();
					String fotoUrl = foto.getMediumUrl();

					this.setCurrentAction("Loading flickr foto: " + fotoName);

					this.addPictureToCanvas(fotoUrl, fotoName 
					        + foto.getDescription(),
							"flickr");

					this.setCurrent(currentFoto++);
					loadedFotosCount++;
				}
			} else {
				System.err.println("Foto list returned null or list is empty!");
				this.setFinished(true);
			} //if sp != null
		} else {
			System.err.println(
			        "No search parameters for flickr search specified!");
			//Fire event that all loaded
			this.setFinished(true);
		}
		//		this.setFinished(true);
		
		for (int i = 0; i < fotoCount - loadedFotosCount; i++) {
			Thread.sleep(this.getSleepTime());
			this.setCurrent(currentFoto++);	
		}

	}


	/**
	 * Istanzia l'oggetto di twitter usando le apiKey presenti nel file di 
	 * configurazione e carica le foto da twitter con gli hashtag desiderati.
	 *
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadTwitterPictures() throws Exception {

		int loadedFotosCount = 0;

		Twitter twitter;
		String consumerKey = "";
		String consumerSecret = "";
		String accessToken = "";
		String accessTokenSecret = "";

		Properties properties = new Properties();

		try {
			InputStream in = null;
			try {
				in = new FileInputStream(FilesPath.APIKEYS_PATH 
				        + "twitterApiKey.txt");
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
			}
			if (in == null) {
				try {
					in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(FilesPath.APIKEYS_PATH 
							        + "twitterApiKey.txt");
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
					throw e;
				}
			}
			properties.load(in);

			consumerKey = properties.getProperty("consumerKey", " ");
			consumerSecret = properties.getProperty("consumerSecret", " ");
			accessToken = properties.getProperty("accessToken", " ");
			accessTokenSecret = properties.getProperty("accessTokenSecret",
			        " ");

		} catch (Exception e) {
			System.err.println("Error while loading twitterApiKey.txt file.");
			e.printStackTrace();
			throw e;
		}

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(consumerKey)
		.setOAuthConsumerSecret(consumerSecret)
		.setOAuthAccessToken(accessToken)
		.setOAuthAccessTokenSecret(accessTokenSecret);

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();

		List<String> hashTags = new ArrayList<String>();

		for(Keyword key:keywords.getAllKeywords()) {
			if(getSocialNetworkByID(key.getSocialId())
			        .equalsIgnoreCase("WWF")) {
                hashTags.add(key.getKeyword());
            }
		}


		Iterator<String> iterator = hashTags.iterator();

		String searchString = "(";

		while (iterator.hasNext()) {
			String s = iterator.next();
			searchString = searchString.concat(s.toLowerCase() + " OR ");
			searchString = searchString.concat("#" + s.toLowerCase());
			if (iterator.hasNext()) {
				searchString = searchString.concat(" OR ");				
			}
		}
		searchString = searchString.concat(") AND filter:images");



		Query query = new Query(searchString);
		query.setResultType(Query.MIXED); // get the recent tweets
		query.count(5);
		QueryResult result = null;

		try {
			result = twitter.search(query);
		} catch (TwitterException e1) {
			e1.printStackTrace();
			throw e1;
		}

		System.out.println("Numero di tweet:" + result.getTweets().size());

		for (int i = 0; i < result.getTweets().size() && i<20; i++) {
			try {
				Status tweetById = twitter.showStatus(
				        result.getTweets().get(i).getId());
				tweetById.getUser().getScreenName();
				tweetById.getId();
				String description = tweetById.getText();
				MediaEntity[] medias = tweetById.getExtendedMediaEntities();

				for (MediaEntity m : medias) {
					
					String fotoUrl = m.getMediaURLHttps();

					this.setCurrentAction("Loading twitter foto: "
					        + description);
					this.setCurrent(currentFoto++);
					loadedFotosCount++;

					this.addPictureToCanvas(fotoUrl, description, "twitter");
				}
			} catch (TwitterException e) {
				System.err.print("Failed to search tweets: " + e.getMessage());
				throw e;
			}
		}
		
		for(int i = 0; i < fotoCount - loadedFotosCount; i++) {
			Thread.sleep(this.getSleepTime());
			this.setCurrent(currentFoto++);	
		}
	}

	/**
	 * Istanzia l'oggetto di facebook usando le apiKey presenti nel file 
	 * di configurazione e carica le foto dal profilo di facebook.
	 *
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadFacebookPictures() {
		
		int loadedFotosCount = 0;

		Facebook facebook;


		String appId = "";
		String appSecret = "";
		String accessToken = "";
		Properties properties = new Properties();

		InputStream in = null;
		try {
			in = new FileInputStream(FilesPath.APIKEYS_PATH 
			        + "facebookApiKey.txt");
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}

		if (in == null) {
			try {
				in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(FilesPath.APIKEYS_PATH 
						        + "facebookApiKey.txt");
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		appId = properties.getProperty("appId", " ");
		appSecret = properties.getProperty("appSecret", " ");
		accessToken = properties.getProperty("accessToken", " ");


		facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(appId, appSecret);
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));


		List<facebook4j.Photo> facebookFotos = 
		        new ArrayList<facebook4j.Photo>();


		Reading reading = new Reading();
		reading.limit(fotoCount);

		try {
			facebookFotos = facebook.getPhotos(reading);
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for (int i=0; i<facebookFotos.size(); i++) {
			facebook4j.Photo photo = (facebook4j.Photo) facebookFotos.get(i);

			String fotoUrl = photo.getPicture().toString();

			String description = photo.getName();

			if (description == null) {
                description = "***No description for this photo***";
            }

			this.setCurrentAction("Loading facebook foto: " + description);
			this.addPictureToCanvas(fotoUrl, description, "facebook");

			this.setCurrent(currentFoto++);
			loadedFotosCount++;
		}


		for(int i = 0; i < fotoCount - loadedFotosCount; i++) {
			try {
				Thread.sleep(this.getSleepTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.setCurrent(currentFoto++);			
		}
	}



	/**
	 * Carica le foto presenti nella cartella locale di facebook (usando ifttt)
	 * @throws Exception lancia una Exception in caso di errore.
	 */
	private void loadFacebookPicturesLocal() {
		loadLocalPictures(FilesPath.FACEBOOK_FOTOS_PATH, "facebook");
	}

	/**
	 * Carica le foto presenti nella cartella locale di instagram (usando ifttt)
	 * @throws Exception lancia una Exception in caso di errore.
	 */
	private void loadInstagramPicturesLocal() {
		loadLocalPictures(FilesPath.INSTAGRAM_FOTOS_PATH, "instagram");
	}


	/**
	 * Aggiunge nel canvas una foto.
	 *
	 * @param fotoUrl url della foto
	 * @param description descrizione della foto
	 * @param socialNetwork nome del socialnetwork cui la foto è stata caricata
	 */
	protected void addPictureToCanvas(final String fotoUrl, 
	        final String description, final String socialNetwork) { }


	/**
	 * Gets the searched foto list.
	 *
	 * @param sp the sp
	 * @param n the n
	 * @param startPage the start page
	 * @param isGeoSearch the is geo search
	 * @return the searched foto list
	 */
	public final PhotoList<Photo> getSearchedFotoList(
	        final SearchParameters sp, final int n, final int startPage, 
	        final boolean isGeoSearch) {
		PhotoList<Photo> photoList = null;
		PhotosInterface photoInterface = f.getPhotosInterface();

		try {
			if (isGeoSearch && sp.getLongitude() != null 
			        && sp.getLatitude() != null 
			        && usePlacesForGeoSearch) {

				PlacesInterface p = f.getPlacesInterface();
				PlacesList<Place> placesList = null;

				placesList = p.findByLatLon(Double.parseDouble(
				        sp.getLatitude()), Double.parseDouble(sp.getLongitude()),
						sp.getAccuracy());

				sp.setLatitude(null);
				sp.setLongitude(null);

				System.out.println("Places found: " + placesList.size());
				for (int i = 0; i < placesList.size(); i++) {
					Place place = (Place) placesList.get(i);
					String placeID = place.getPlaceId();

					sp.setPlaceId(placeID);
					sp.setWoeId(place.getWoeId());

				}
			}
			HashSet<String> keywords = new HashSet<String>();
			keywords.add("WWF");
			
			photoList = photoInterface.getRecent(keywords, n, startPage);

			if (isGeoSearch && photoList != null) {
				System.out.println("Found " + photoList.size() + " fotos.");

				if (photoList.size() <= 0) {
					System.out.println("Found no fotos, "
					        + "reducing accuracy and trying again.");
					int a = sp.getAccuracy();

					for (int i = 1; i < 6; i++) {
						a--;
						if (a < 1) {
							a++;
							break;
						}
					}

					System.out.println("Using new accuracy: " + a);
					sp.setAccuracy(a);
					photoList = photoInterface.search(sp, n, startPage);
					if (photoList != null) {
                        System.out.println("Found " 
                                + photoList.size() + " fotos.");
                    }
				}
			}

			if (isGeoSearch) {
				if (photoList != null) {
					GeoInterface g = photoInterface.getGeoInterface();

					// Go through all found fotos
					for (int i = 0; i < photoList.size(); i++) {
						Photo foto = (Photo) photoList.get(i);
						// Add to result list
						photos.add(foto);

						String id = foto.getId();
						try {
							GeoData loc = g.getLocation(id);
							if (loc != null) {
								foto.setGeoData(loc);
							}
						} catch (Exception e) {
							System.err.println(
							        "Error fetching geodata for foto");
							e.printStackTrace();
						}
					}
				}
			} else {
				// Add to result list
				if (photoList != null) {
					for (int i = 0; i < photoList.size(); i++) {
						photos.add((Photo) photoList.get(i));
					}
				}
			}

			// Geht nur mit eigenen fotos!?
			// photos.getWithGeoData(arg0, arg1, arg2, arg3, arg4, arg5, arg6,
			// arg7, arg8);

			return photoList;
		} catch (FlickrException e) {
			System.out.println("ERRORE NEL RITROVAMENTO DELLE FOTO DI FLICKR");
		}
		return photoList;
	}

	/**
	 * Checks if is geo search.
	 *
	 * @param sp the sp
	 * @return true, if is geo search
	 */
	private boolean isGeoSearch(final SearchParameters sp) {
		return ((sp.getLatitude() != null // TODO is it null when not set?
				&& !sp.getLatitude().equalsIgnoreCase(""))
				|| (sp.getLongitude() != null && !sp.getLongitude()
				.equalsIgnoreCase(""))
				|| (sp.getBBox() != null && sp.getBBox().length > 0)
				|| (sp.getWoeId() != null && !sp.getWoeId()
				.equalsIgnoreCase(""))
				|| (sp.getPlaceId() != null && !sp.getPlaceId()
				.equalsIgnoreCase("")) || sp.getRadius() != -1);
	}


	/**
	 * Questo metodo carica le foto da una qualsiasi cartella locale, 
	 * il cui percorso viene passato come parametro
	 * @param path percorso della cartella cui si vogliono caricare le foto
	 * @param socialNetwork nome del social network
	 */
	private void loadLocalPictures(final String path, 
	        final String socialNetwork) {


	    int numOfRecentFiles = 20;

	    
		int loadedFotosCount = 0;

		File file = new File(path);
		List<File> files = new LinkedList<File>();
		File[] f_list = file.listFiles();
		FileNameExtensionFilter image_filter =
		        new FileNameExtensionFilter("JPEG file", "jpeg", "jpg");

		for (File f : f_list) {
			if (f.isFile()) {
				if (image_filter.accept(f)) {
					System.out.println("Path " + f);
					files.add(f);
				}
			}
		}

		File[] fotoFiles = new File[files.size()]; 
		fotoFiles = files.toArray(fotoFiles);
		Arrays.sort(fotoFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
		List<File> recentFiles = new ArrayList<File>(Arrays.asList(fotoFiles));
		if(recentFiles.size()>numOfRecentFiles)
		    recentFiles = recentFiles.subList(0, numOfRecentFiles);
		Iterator<File> it = recentFiles.iterator();
		
		while (it.hasNext()) {
			File fotoFile = (File) it.next();

			String fotoUrl = fotoFile.toString();
			String description = fotoFile.getName();

			if (description == null) {
                description = "***No description for this photo***";
            }

			if (socialNetwork.equalsIgnoreCase("facebook")) {
                this.setCurrentAction("Loading facebook foto: " + description);
            } else if (socialNetwork.equalsIgnoreCase("instagram")) {
                this.setCurrentAction("Loading instagram foto: " + description);
            }
				
			this.addPictureToCanvas(fotoUrl, description, socialNetwork);

			this.setCurrent(currentFoto++);
			loadedFotosCount++;
		}
		
		for (int i = 0; i < fotoCount - loadedFotosCount; i++) {
			try {
				Thread.sleep(this.getSleepTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setCurrent(currentFoto++);	
		}

	}

	/**
	 * Gets the social network by ID.
	 *
	 * @param id the id
	 * @return the social network by ID
	 */
	private String getSocialNetworkByID(final Integer id) {
		String sn = "";
		for (SocialNetwork social:socials.getAllSocials()) {
			if (social.getId().equals(id)) {
                sn = social.getName();
            }
		}
		return sn;
	}


	/**
	 * Gets the foto urls.
	 *
	 * @param list the list
	 * @return the foto urls
	 */
	private String[] getFotoUrls(final PhotoList<Photo> list) {
		// Grab all the image paths and store in String array
		String[] smallURLs = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Photo p = (Photo) list.get(i);
			// smallURLs[i] = p.getSmallUrl();
			// smallURLs[i] = p.getUrl();
			smallURLs[i] = p.getMediumUrl();
		}
		return smallURLs;
	}


	/**
	 * Gets the foto load count.
	 *
	 * @return the foto load count
	 */
	public final int getFotoLoadCount() {
		return fotoCount;
	}


	/**
	 * Gets the search parameters.
	 *
	 * @return the search parameters
	 */
	public final SearchParameters getSearchParameters() {
		return searchParameters;
	}


	/**
	 * Sets the search parameters.
	 *
	 * @param searchParameters the new search parameters
	 */
	public final void setSearchParameters(
	        final SearchParameters searchParameters) {
		this.searchParameters = searchParameters;
	}


	/**
	 * Gets the search page offset.
	 *
	 * @return the search page offset
	 */
	private int getSearchPageOffset() {
		return this.searchPageOffset;
	}

	/**
	 * Sets the search page offset.
	 *
	 * @param searchPageOffset the new search page offset
	 */
	public final void setSearchPageOffset(final int searchPageOffset) {
		this.searchPageOffset = searchPageOffset;
	}


	/**
	 * Gets the flickr key.
	 *
	 * @return the flickr key
	 */
	public final String getFlickrKey() {
		return flickrKey;
	}


	/**
	 * Sets the flickr key.
	 *
	 * @param flickrKey the new flickr key
	 */
	public final void setFlickrKey(final String flickrKey) {
		this.flickrKey = flickrKey;
	}


	/**
	 * Gets the flickr secret.
	 *
	 * @return the flickr secret
	 */
	public final String getFlickrSecret() {
		return flickrSecret;
	}



	/**
	 * Sets the flickr secret.
	 *
	 * @param flickrSecret the new flickr secret
	 */
	public final void setFlickrSecret(final String flickrSecret) {
		this.flickrSecret = flickrSecret;
	}

	/**
	 * Gets the photos.
	 *
	 * @return the photos
	 */
	public final Photo[] getPhotos() {
		return this.photos.toArray(new Photo[this.photos.size()]);
	}


	/**
	 * Checks if is use places for geo search.
	 *
	 * @return true, if is use places for geo search
	 */
	public final boolean isUsePlacesForGeoSearch() {
		return usePlacesForGeoSearch;
	}


	/**
	 * Sets the use places for geo search.
	 *
	 * @param usePlacesForGeoSearch the new use places for geo search
	 */
	public final void setUsePlacesForGeoSearch(final boolean 
	        usePlacesForGeoSearch) {
		this.usePlacesForGeoSearch = usePlacesForGeoSearch;
	}
}