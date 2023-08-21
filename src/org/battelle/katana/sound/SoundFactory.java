package org.battelle.katana.sound;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.mt4j.MTApplication;



/**
 * SoundFactory creates threads for sound files that will play in the background when started
 * 
 * @author Alex
 * 
 */
public class SoundFactory {
  /** Singleton creator */
  private static SoundFactory instance = new SoundFactory();
  /** Random number generator */
  private Random random;
  /** String to sounds folder */
  private final String SOUNDPATH = "resources" + MTApplication.separator + "sounds" + MTApplication.separator;
  /** String path to hits sounds */
  private final String HITSPATH = SOUNDPATH + "hits" + MTApplication.separator;
  /** String path to miss sounds */
  private final String MISSPATH = SOUNDPATH + "misses" + MTApplication.separator;
  /** String path to miss sounds */
  private final String QUAKEPATH = SOUNDPATH + "quake" + MTApplication.separator;
  /** String path to miss sounds */
  private final String POWERUPPATH = SOUNDPATH + "powerups" + MTApplication.separator;
  /** String path to miss sounds */
  private final String FXPATH = SOUNDPATH + "fx" + MTApplication.separator;
  /** ArrayList to store hit sounds */
  private ArrayList<byte[]> hitList = new ArrayList<byte[]>();
  /** ArrayList to store miss sounds */
  private ArrayList<byte[]> missList = new ArrayList<byte[]>();
  /** ArrayList to store quake sounds */
  private ArrayList<byte[]> quakeList = new ArrayList<byte[]>();
  /** ArrayList to store powerup sounds */
  private ArrayList<byte[]> powerupList = new ArrayList<byte[]>();
  /** ArrayList to store effects */
  private ArrayList<byte[]> fxList = new ArrayList<byte[]>();

  /**
   * Default constructor which pre-generates Players for each sound file
   */
  public SoundFactory() {
    random = new Random();
    
    try {
      // Load all hit sounds
      for (File file : new File(HITSPATH).listFiles()) {
        if (!file.getName().equals("info")) {
          hitList.add(generatePlayer(file));
        }
      }
    }
    catch (Exception e) {
      System.err.println("WARNING: Did not load hit sounds");
      e.printStackTrace();
    }

    // Load all miss sounds
    try {
      for (File file : new File(MISSPATH).listFiles()) {
        if (!file.getName().equals("info")) {
          missList.add(generatePlayer(file));
        }
      }
    }
    catch (Exception e) {
      System.err.println("WARNING: Did not load miss sounds");
    }

    // Load quake sounds
    try {
      quakeList.add(generatePlayer(new File(QUAKEPATH + "quake_dominating.wav")));
      quakeList.add(generatePlayer(new File(QUAKEPATH + "quake_unstoppable.wav")));
      quakeList.add(generatePlayer(new File(QUAKEPATH + "quake_godlike.wav")));
      quakeList.add(generatePlayer(new File(QUAKEPATH + "quake_wickedsick.wav")));
      quakeList.add(generatePlayer(new File(QUAKEPATH + "quake_ludicrous.wav")));

    }
    catch (Exception e) {
      System.err.println("WARNING: Did not load quake sounds");
    }

    // Load powerup sounds
    try {
      powerupList.add(generatePlayer(new File(POWERUPPATH + "multiplier.wav")));
      powerupList.add(generatePlayer(new File(POWERUPPATH + "frenzy.wav")));
      powerupList.add(generatePlayer(new File(POWERUPPATH + "timewarp.wav")));
    }
    catch (Exception e) {
      System.err.println("WARNING: Did not load powerup sounds");
    }

    // Load misc sounds
    try {
      fxList.add(generatePlayer(new File(FXPATH + "drum_loop.wav")));
      fxList.add(generatePlayer(new File(FXPATH + "explosion.wav")));
      fxList.add(generatePlayer(new File(FXPATH + "score.wav")));
      fxList.add(generatePlayer(new File(FXPATH + "game_over.wav")));
      fxList.add(generatePlayer(new File(FXPATH + "score.wav")));
      fxList.add(generatePlayer(new File(FXPATH + "extralife.wav")));
    }
    catch (Exception e) {
      System.err.println("WARNING: Did not load misc sound effects");
    }
  }

  /**
   * Generates a thread that will play an mp3 sound file when started
   * 
   * @param sound Enum value for the desired sound file
   * @return A thread that will play desired sound upon start()
   */
  public synchronized Thread generateSound(SoundType sound) {
    Sound returnSound = new Sound();

    // Fetch respective Player and pass it to returnSound
    try {
      switch (sound) {
        case MISS:
          returnSound = new Sound(missList.get(random.nextInt(missList.size())));
          break;
        case HIT:
          returnSound = new Sound(hitList.get(random.nextInt(hitList.size())));
          break;
        case BOMB:
          returnSound = new Sound(fxList.get(1));
          break;
        case BGMUSIC:
          returnSound = new Sound(fxList.get(0));
          returnSound.setLoop(true);
          break;
        case GAMEOVER:
          returnSound = new Sound(fxList.get(3));
          break;
        case WICKEDSICK:
          returnSound = new Sound(quakeList.get(3));
          break;
        case GODLIKE:
          returnSound = new Sound(quakeList.get(2));
          break;
        case DOMINATING:
          returnSound = new Sound(quakeList.get(0));
          break;
        case UNSTOPPABLE:
          returnSound = new Sound(quakeList.get(1));
          break;
        case SCORE:
          returnSound = new Sound(fxList.get(4));
          break;
        case TIMEWARP:
          returnSound = new Sound(powerupList.get(2));
          break;
        case FRENZY:
          returnSound = new Sound(powerupList.get(1));
          break;
        case MULTIPLIER:
          returnSound = new Sound(powerupList.get(0));
          break;
        case LUDICROUS:
          returnSound = new Sound(quakeList.get(4));
          break;
        case EXTRALIFE:
          returnSound = new Sound(fxList.get(5));
          break;
        default:
          System.err.println("ERROR: Invalid sound passed to SoundFactory");
          break;
      }
      return new Thread(returnSound);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Creates a new player from the mp3 file specified at the path passed to the method
   * 
   * @param path The location where the desired mp3 file is located
   * @return A new Player from the file path
   */
  private byte[] generatePlayer(File soundFile) {
    try {
      // Check length of sound file
      long length = soundFile.length();

      // Byte[] to hold data in sound file
      byte[] returnArray = new byte[(int) length];

      // Try to open relevant streams and create a new Player from the
      // path
      FileInputStream fis = new FileInputStream(soundFile);

      // Read bytes from input stream
      int offset = 0;
      int numRead = 0;
      while (offset < returnArray.length && (numRead = fis.read(returnArray, offset, Math.min(returnArray.length - offset, 512 * 1024))) >= 0) {
        offset += numRead;
      }

      // Verify complete read
      if (offset < returnArray.length) {
        throw new IOException("Could not completely read file: " + soundFile.getName());
      }

      // Close file input stream
      fis.close();

      return returnArray;
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    // Return null if stream opening fails
    return null;
  }

  public static synchronized SoundFactory getInstance() {
    return instance;
  }
}