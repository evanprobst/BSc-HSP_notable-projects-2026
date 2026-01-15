import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * The MusicPlayer class provides static methods to play, pause, resume, and stop looping background music
 * for the application using Java's javax.sound.sampled API.
 * 
 * This class manages a single audio clip at a time and supports tracking the playback position
 * to allow resuming from where the music was paused.
 * <p>
 * Note: Only supports file formats supported by AudioSystem.getAudioInputStream(),
 * such as WAV files.</p>
 * 
 * @author group55
 * @version 1.0
 */
public class MusicPlayer {

    /** The audio clip currently being played. */
    private static Clip clip;

    /** The position (in microseconds) of the clip when paused. */
    private static long clipPosition = 0;

    /** The audio stream used to read the sound file. */
    private static AudioInputStream audioStream;

    /** The audio file currently being played. */
    private static File audioFile;

    /** Tracks whether the music is currently paused. */
    private static boolean isPasued = false;

    /**
     * Sets the paused state of the music.
     * 
     * @param isPasued true to indicate the music is paused, false if not
     */
    public static boolean isPasued() {
        return isPasued;
    }

    /**
     * Plays music from the specified file path. If a clip is already playing, it stops and replaces it.
     * The music will loop continuously.
     * 
     * @param filePath the path to the audio file to play (e.g., "sounds/normal_state_music.wav")
     */
    public static void setPasued(boolean isPasued) {
        MusicPlayer.isPasued = isPasued;
    }

    public static void playMusic(String filePath) {
        try {
            if (clip != null) {
                clip.stop();
                clip.close();
            }

            audioFile = new File(filePath);
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    /**
     * Stops the currently playing music and resets the clip position.
     */
    public static void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
        clipPosition = 0;
    }


    /**
     * Pauses the currently playing music and saves its current position.
     */
    public static void pauseMusic() {
        if (clip != null) {
            clipPosition = clip.getMicrosecondPosition();
            clip.stop();
            isPasued = true;
        }
    }


    /**
     * Resumes the music from the last paused position, if the clip is not already playing.
     */
    public static void resumeMusic() {
        try {
            if (clip != null && !clip.isRunning()) {
                clip.close(); // Must reopen to reinitialize properly
                audioStream = AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.setMicrosecondPosition(clipPosition);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                isPasued = false;
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
}
