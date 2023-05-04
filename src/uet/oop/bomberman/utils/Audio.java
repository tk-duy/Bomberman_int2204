package uet.oop.bomberman.utils;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Audio {
    public static Clip menuMusic;
    public static Clip gameMusic;

    public static void playMenuMusic() {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/MenuMusic.wav"));
            menuMusic = AudioSystem.getClip();
            menuMusic.open(in);
            menuMusic = setVolume(menuMusic,0.8f);
            menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("something went wrong when try to play music");
            e.printStackTrace();
        }
    }

    public static void playGameMusic() {
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/GameSong.wav"));
            gameMusic = AudioSystem.getClip();
            gameMusic.open(in);
            gameMusic = setVolume(gameMusic,0.8f);
            gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("something went wrong when try to play music");
            e.printStackTrace();
        }
    }

    public static void stopMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    public static void playMenuMove(){
        try
        {
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/MenuMove.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void playEntityDie(){
        try{
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/entinydie.wav"));
            Clip tmp = AudioSystem.getClip();
            tmp.open(in);
            Clip clip = setVolume(tmp,0.5f);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void playBombDrop(){
        try{
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/BombDrop.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void playBombExplode(){
        try{
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/BombExplode.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void playVictory(){
        try{
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/StageComplete.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static void bomberDie(){
        try{
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/BomberDie.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void takeItem(){
        try{
            AudioInputStream in = AudioSystem.getAudioInputStream(new File("res/musics/TakeItem.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        }
        catch(Exception e){e.printStackTrace();}
    }

    /**
     *
     * @param clip the clip which you want to modify its volume.
     * @param volume the desire volume range from 0 to 1.
     *        0 means no sound
     *        1 means maximum volume.
     * @return the modified clip.
     */
    public static Clip setVolume(Clip clip, float volume) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
        return clip;
    }
}
