package wwf.cranium.util;

import org.mt4j.MTApplication;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

/**
 * Wrapper class to the minim framework.
 * 
 * For wav file formats For best results, do not use the mp3 file format.
 * 
 * @author Andrew McGlynn
 *
 */
public class AudioTrack {

    /** The minim. */
    private Minim minim;

    /** The player. */
    private AudioPlayer player;

    /** The meta data. */
    private AudioMetaData metaData;

    /**
     * Instantiates a new audio track.
     *
     * @param fileName
     *            file audio
     * @param mtapp
     *            current application
     */
    public AudioTrack(final String fileName, final MTApplication mtapp) {
        minim = new Minim(mtapp);
        player = minim.loadFile(fileName);
        metaData = player.getMetaData();
    }

    /**
     * Check if the track is playing.
     *
     * @return true if the track is playing
     */
    public final boolean isPlaying() {
        return player.isPlaying();
    }

    /**
     * Pause the audio track.
     */
    public final void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    /**
     * Play the audio track.
     */
    public final void play() {
        if (!player.isPlaying()) {
            player.play();
        }
    }

    /**
     * Rewind the track to the start.
     */
    public final void rewind() {
        player.rewind();
    }

    /**
     * Loop.
     */
    public final void loop() {
        player.loop();
    }

    /**
     * Change where the current position in time that the track is currently
     * playing.
     *
     * @param millis
     *            position
     */
    public final void cue(final int millis) {
        final int mill = millis;
        Thread cueThread = new Thread() {
            public void run() {
                player.cue(mill);
                try {
                    this.join();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Unable to join");
                }
            }
        };
        cueThread.start();
    }

    /**
     * Clean up the track, clearing resources.
     */
    public final void close() {
        player.close();
        minim.stop();
    }

    /**
     * Get the track artist .
     *
     * @return track artist
     */
    public final String getArtist() {
        return metaData.author();
    }

    /**
     * Get the name of the album.
     *
     * @return album name
     */
    public final String getAlbum() {
        return metaData.album();
    }

    /**
     * Get the track title.
     *
     * @return title
     */
    public final String getTitle() {
        return metaData.title();
    }

    /**
     * Get the length of the track in milliseconds.
     *
     * @return length of the track
     */
    public final int getLength() {
        return player.length();
    }

    /**
     * Get the position that the track is currently at in milliseconds.
     *
     * @return the current position of the track
     */
    public final int getPosition() {
        return player.position();
    }

    /**
     * Set the volume of the track.
     *
     * @param volume
     *            volume to set
     */
    public final void setVolume(final float volume) {
        if (player.isMuted()) {
            player.unmute();
        }
        player.setGain(volume);
    }

    /**
     * Mute the audio track.
     */
    public final void mute() {
        player.mute();
    }
}
