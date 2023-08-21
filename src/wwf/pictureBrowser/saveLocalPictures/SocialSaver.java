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
package wwf.pictureBrowser.saveLocalPictures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.mt4j.MTApplication;
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
import wwf.pictureBrowser.PictureBrowser;
import wwf.pictureBrowser.data.CurrentKeywords;
import wwf.pictureBrowser.data.CurrentSocials;
import wwf.pictureBrowser.data.model.Keyword;
import wwf.pictureBrowser.data.model.SocialNetwork;
import wwf.pictureBrowser.util.FilesPath;


/**
 * Questo thread provvede al caricamento di foto da vari social networks 
 * le quali vengono immadiatamente salvate su disco per consentire la 
 * visualizzazione delle stesse foto anche in assenza di connessione.
 * 
 */
public class SocialSaver extends Thread {

	/** The use places for geo search. */
	private boolean usePlacesForGeoSearch;

	/** The mt application. */
	private MTApplication mtApplication;


	/** The search parameters. */
	private SearchParameters searchParameters;


	/** numero di foto che devono essere caricate per ogni social network. */
	private int fotoCount;


	/** The search page offset. */
	private int searchPageOffset;


	/** The flickr key. */
	private String flickrKey;


	/** The flickr secret. */
	private String flickrSecret;


	/** The f. */
	private Flickr f;


	/** lista delle foto inizializzate. */
	private List<Photo> photos;


	/** The pa. */
	private PApplet pa;

	/** The picture browser. */
	private PictureBrowser pictureBrowser;

	/** The keywords. */
	private CurrentKeywords keywords;
	
	/** The socials. */
	private CurrentSocials socials;



	/**
	 * Costruttore parametrico.
	 *
	 * @param pa applicazione in questione
	 * @param pictureBrowser riferimento all'applicazione principale
	 */
	public SocialSaver(final PApplet pa, final PictureBrowser pictureBrowser) {

		this.pictureBrowser = pictureBrowser;
		this.keywords = new CurrentKeywords(pictureBrowser.isUpdateDB());
		this.socials = new CurrentSocials(pictureBrowser.isUpdateDB());
	}


	/**
	 * Istanzia l'oggetto {@link #f} usando le apiKey presenti nel file di 
	 * configurazione e istanzia anche {@link #searchParameters} usando gli 
	 * hashtag desiderati.
	 *
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void buildFlickrParameter() {
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
				}
			}
			properties.load(in);

			flickrKey = properties.getProperty("FlickrApiKey", " ");
			flickrSecret = properties.getProperty("FlickrSecret", " ");
		} catch (Exception e) {
			System.err.println("Error while loading FlickrApiKey.txt file.");
			e.printStackTrace();

		}


		searchParameters = new SearchParameters();




		f = new Flickr(flickrKey, flickrSecret, new REST());
		//f.getAuthInterface();
		RequestContext.getRequestContext();

		this.fotoCount = 5;
		this.searchPageOffset = 0;
		this.photos = new ArrayList<Photo>();

		this.usePlacesForGeoSearch = true;
	}


	/**
	 * Il metodo run chiama i metodi che provvederanno al caricamento delle 
	 * foto dai social network specificati.
	 * 
	 */
	@Override
    public final void run() {
		System.out.println("thread start");
		
		
		loadFlickrPictures(); 
		loadTwitterPictures();
		loadFacebookPicturesLocal();
		loadInstagramPicturesLocal();

		System.out.println("thread end");
	}


	/**
	 * Questo metodo carica le foto da flickr usando le apikey del file 
	 * di configurazione.
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadFlickrPictures() {

		buildFlickrParameter();

		//sp.setMachineTags(new String[]{"geo:locality=\"san francisco\""});
		List<String> hashTags = new ArrayList<String>();

		for (Keyword key:keywords.getAllKeywords()) {
			if (getSocialNetworkByID(key.getSocialId())
			        .equalsIgnoreCase("flickr")) {
				hashTags.add(key.getKeyword());
                hashTags.add("#" + key.getKeyword());
			}
		}

		for (String s:hashTags) {
			String[] arrayTags = {s, "#" + s};
			searchParameters.setTags(arrayTags);
			searchParameters.setSort(SearchParameters.RELEVANCE);

			if (this.getSearchParameters() != null) {
				this.setFotoLoadCount(5);

				boolean isGeoSearch = this.isGeoSearch(
				        this.getSearchParameters());
				PhotoList<Photo> fotoList = this.getSearchedFotoList(
				        this.getSearchParameters(), this.getFotoLoadCount(), 
				        this.getSearchPageOffset(), isGeoSearch);

				if (fotoList != null && fotoList.size() > 0) {

					for (int i = 0; i < fotoList.size(); i++) {

						Photo foto = (Photo) fotoList.get(i);
						String fotoName = foto.getTitle();
						String fotoUrl = foto.getMediumUrl();

						try {
							this.saveLocalPicture(foto.getId(), 
							        fotoUrl, fotoName + foto.getDescription(),
									"flickr", s);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					System.err.println(
					        "Foto list returned null or list is empty!");
				}
			} else {
				System.err.println(
				        "No search parameters for flickr search specified!");
			}
		}
	}

	/**
	 * Istanzia l'oggetto di twitter usando le apiKey presenti nel file di 
	 * configurazione e carica le foto da twitter con gli hashtag desiderati.
	 *
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadTwitterPictures() {

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

		for (Keyword key:keywords.getAllKeywords()) {
			if (getSocialNetworkByID(key.getSocialId())
			        .equalsIgnoreCase("twitter")) {
                hashTags.add(key.getKeyword());
            }
		}

		Iterator<String> iterator = hashTags.iterator();

		while (iterator.hasNext()) {
			String s = iterator.next();

			String searchString = "(";
			searchString = searchString.concat(s.toLowerCase() + " OR ");
			searchString = searchString.concat("#" + s.toLowerCase());
			searchString = searchString.concat(") AND filter:images");

			Query query = new Query(searchString);
			query.setResultType(Query.MIXED); // get the recent tweets
			query.count(5);
			QueryResult result = null;

			try {
				result = twitter.search(query);
			} catch (TwitterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("Numero di tweet:" + result.getTweets().size());

			for (int i = 0; i < result.getTweets().size(); i++) {
				try {
					Status tweetById = twitter.showStatus(
					        result.getTweets().get(i).getId());
					tweetById.getUser().getScreenName();
					tweetById.getId();
					String description = tweetById.getText();
					MediaEntity[] medias = tweetById.getExtendedMediaEntities();

					for (MediaEntity m : medias) {
						String fotoUrl = m.getMediaURLHttps();


						try {
							this.saveLocalPicture(Long.toString(m.getId()),
							        fotoUrl, description, "twitter", s);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (TwitterException e) {
					System.err.print("Failed to search tweets: " 
				+ e.getMessage());
					return;
				}
			}

		}
	}

	/**
	 * Istanzia l'oggetto di facebook usando le apiKey presenti nel file di 
	 * configurazione e carica le foto dal profilo di facebook.
	 *
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadFacebookPictures() {
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
			// TODO Auto-generated catch block
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
		reading.includeHidden(true);

		try {
			facebookFotos = facebook.getPhotos(reading);
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		for (int i = 0; i < facebookFotos.size(); i++) {
			facebook4j.Photo photo = (facebook4j.Photo) facebookFotos.get(i);

			String fotoUrl = photo.getPicture().toString();

			String description = photo.getName();

			if (description == null) {
                description = "***No description for this photo***";
            }

			try {
				this.saveLocalPicture(photo.getId(), 
				        fotoUrl, description, "facebook", null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


		/**
		 * Carica le foto presenti nella cartella locale di facebook 
		 * (usando ifttt).
		 * @throws Exception lancia una Exception in caso di errore
		 */
	private void loadFacebookPicturesLocal() {
		loadLocalPictures(FilesPath.FACEBOOK_FOTOS_PATH, "facebook");
	}

	/**
	 * Carica le foto presenti nella cartella locale di instagram 
	 * (usando ifttt).
	 * @throws Exception lancia una Exception in caso di errore
	 */
	private void loadInstagramPicturesLocal() {
		loadLocalPictures(FilesPath.INSTAGRAM_FOTOS_PATH, "instagram");
	}


	/**
	 * Salva una foto in locale.
	 *
	 * @param id id della foto
	 * @param fotoUrl url della foto
	 * @param description descrizione della foto
	 * @param socialNetwork nome del social network cui la foto proviene
	 * @param hashTag hashtag della foto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void saveLocalPicture(final String id, 
	        final String fotoUrl, final String description,
	        final String socialNetwork, final String hashTag) 
			        throws IOException { }

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
	        final SearchParameters sp, final int n, 
	        final int startPage, final boolean isGeoSearch) {

	    PhotoList<Photo> photoList = null;
		PhotosInterface photoInterface = f.getPhotosInterface();

		try {

			if (isGeoSearch && sp.getLongitude() 
			        != null && sp.getLatitude() 
			        != null && usePlacesForGeoSearch) {

				PlacesInterface p = f.getPlacesInterface();
				PlacesList<Place> placesList = null;

				placesList = p.findByLatLon(Double.parseDouble(
				        sp.getLatitude()),
				        Double.parseDouble(sp.getLongitude()),
						sp.getAccuracy());

				sp.setLatitude(null);
				sp.setLongitude(null);

				System.out.println("Places found: " + placesList.size());
				for (int i = 0; i < placesList.size(); i++) {
					Place place = (Place) placesList.get(i);
					String placeID = place.getPlaceId();
					// System.out.println("Place ID: " + placeID);

					sp.setPlaceId(placeID);
					sp.setWoeId(place.getWoeId());
				}
			}
			photoList = photoInterface.search(sp, n, startPage);


			if (isGeoSearch && photoList != null) {
				System.out.println("Found " + photoList.size() + " fotos.");

				if (photoList.size() <= 0) {
					System.out.println("Found no fotos,"
					        + " reducing accuracy and trying again.");
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

			return photoList;
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return photoList;
	}



	/**
	 * Questo metodo carica le foto da una qualsiasi cartella locale, 
	 * il cui percorso viene passato come parametro.
	 * 
	 * @param path percorso della cartella cui si vogliono caricare le foto
	 * @param socialNetwork nome del social network
	 */
	
	private void loadLocalPictures(final String path,
	        final String socialNetwork) {
		try {
			File file = new File(path);
			// final File files[] = file.listFiles();
			List<File> fotoFiles = new LinkedList<File>();

			File[] fList = file.listFiles();
			FileNameExtensionFilter imageFilter = 
			        new FileNameExtensionFilter("JPEG file", "jpeg", "jpg");

			for (File ff:fList) {
				if (ff.isFile()) {
					if (imageFilter.accept(ff)) {
						fotoFiles.add(ff);
					}
				}
			}

			Iterator<File> it = fotoFiles.iterator();
			while (it.hasNext()) {
				File fotoFile = (File) it.next();
				String description = fotoFile.getName();

				if (description == null) {
                    description = "***No description for this photo***";
                }

				if (fotoFile.isFile()) {
					try {
						this.saveLocalPictureOffline(fotoFile, 
						        socialNetwork, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Queste foto sono caricate da iftt quindi sono presenti in una cartella 
	 * locale. Il metodo non scarica la foto da internet ma effettua 
	 * semplicemente un copia incolla.
	 *
	 * @param file file da salvare
	 * @param socialNetwork nome del social network cui la foto proviene
	 * @param hashTag hashtag della foto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void saveLocalPictureOffline(final File file,
	        final String socialNetwork, final String hashTag) 
	                throws IOException { }

	/**
	 * Checks if is geo search.
	 *
	 * @param sp the sp
	 * @return true, if is geo search
	 */
	private boolean isGeoSearch(final SearchParameters sp) {
		return ((sp.getLatitude() != null
				&& !sp.getLatitude().equalsIgnoreCase(""))
				|| (sp.getLongitude() != null 
				&& !sp.getLongitude().equalsIgnoreCase(""))
				|| (sp.getBBox() != null
				&& sp.getBBox().length > 0)
				|| (sp.getWoeId() != null 
				&& !sp.getWoeId().equalsIgnoreCase(""))
				|| (sp.getPlaceId() != null 
				&& !sp.getPlaceId().equalsIgnoreCase("")) 
				|| sp.getRadius() != -1);
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
	 * Sets the foto load count.
	 *
	 * @param fotosCount the new foto load count
	 */
	public final void setFotoLoadCount(final int fotosCount) {
		this.fotoCount = fotosCount;
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
	public final void setSearchParameters(final SearchParameters
	        searchParameters) {
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