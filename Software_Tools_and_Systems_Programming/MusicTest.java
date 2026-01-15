import org.junit.*;
import static org.junit.Assert.*;

public class MusicTest {

    private static final String TEST_AUDIO_FILE = "sounds/normal_state_music.wav";

    @Before
    public void setUp() { // Before all, stop music, set to unpasued
        MusicPlayer.stopMusic();
        MusicPlayer.setPasued(false);
    }

    @After
    public void tearDown() { // After all, Stop music
        MusicPlayer.stopMusic();
    }

    @Test
    public void testPlayMusic() { // Test if music plays
        MusicPlayer.playMusic(TEST_AUDIO_FILE);
        assertFalse(MusicPlayer.isPasued());
    }

    @Test
    public void testStopMusic() { // Test if music fully stops
        MusicPlayer.playMusic(TEST_AUDIO_FILE);
        MusicPlayer.stopMusic();
        assertFalse(MusicPlayer.isPasued());
    }

    @Test
    public void testPauseMusic() { // test if music pauses
        MusicPlayer.playMusic(TEST_AUDIO_FILE);
        MusicPlayer.pauseMusic();
        assertTrue(MusicPlayer.isPasued());
    }

    @Test
    public void testResumeMusic() { // test if music resumes
        MusicPlayer.playMusic(TEST_AUDIO_FILE);
        MusicPlayer.pauseMusic();
        assertTrue(MusicPlayer.isPasued());

        MusicPlayer.resumeMusic();
        assertFalse(MusicPlayer.isPasued());
    }

    @Test
    public void testIsPausedGetterAndSetter() { // test music paused getters and setters
        MusicPlayer.setPasued(true);
        assertTrue(MusicPlayer.isPasued());

        MusicPlayer.setPasued(false);
        assertFalse(MusicPlayer.isPasued());
    }
    public static void main(String[] args) {
        MusicTest test = new MusicTest();
            //testPlayMusic
            test.setUp();
            test.testPlayMusic();
            test.tearDown();
            //testStopMusic
            test.setUp();
            test.testStopMusic();
            test.tearDown();
            //testPauseMusic
            test.setUp();
            test.testPauseMusic();
            test.tearDown();
            //testResumeMusic
            test.setUp();
            test.testResumeMusic();
            test.tearDown();
            //testIsPausedGetterAndSetter
            test.setUp();
            test.testIsPausedGetterAndSetter();
            test.tearDown();

            System.out.println("All automated Music tests passed.");
        
    }
}
