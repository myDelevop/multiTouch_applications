package org.battelle.katana.sound;
import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

/**
 * Sound implements Runnable in order to be passed to a thread
 * In order to execute properly, a valid player must be specified
 * 
 * @author Alex
 *
 */
public class Sound implements Runnable
{
  /**
   * Clip plays a sound specified by parent
   */
  private Clip clip;

  /**
   * Boolean value to specify whether the sound is to loop infinitely
   */
  private boolean loop;

  /**
   * Default constructor, sets the player to play once
   */
  public Sound()
  {
    this.loop = false;
  }

  /**
   * Constructs a Sound from player, defaults to play sound once
   * 
   * @param player The player that corresponds to a sound to be played
   * @throws JavaLayerException Throws error if player cannot read byte[]
   */
  public Sound(byte[] soundData)
  {
    Clip newClip = null;
    try {
      newClip = AudioSystem.getClip();
      newClip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(soundData)));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    this.clip = newClip;
    this.loop = false;
  }

  /**
   * Implements Runnable's run(), called by the thread to which the Runnable belongs 
   */
  public void run()
  {
    class AudioListener implements LineListener {
      private boolean done = false;
      @Override public synchronized void update(LineEvent event) {
        Type eventType = event.getType();
        if (eventType == Type.STOP || eventType == Type.CLOSE) {
          done = true;
          notifyAll();
        }
      }
      public synchronized void waitUntilDone() throws InterruptedException {
        while (!done) { wait(); }
      }
    }

    AudioListener listener = new AudioListener();

    try { 
      clip.addLineListener(listener);
      clip.open();
      //System.out.println(loop);
      if (loop) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
      }
      else
      {
        clip.start();
      }
      listener.waitUntilDone();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      clip.close();
    }
  }

  /**
   * Basic mutator for loop variable, must be set for background music
   * 
   * @param loop Desired value for Sound's private loop variable
   */
  public void setLoop(Boolean loop)
  {
    this.loop = loop;
  }
}