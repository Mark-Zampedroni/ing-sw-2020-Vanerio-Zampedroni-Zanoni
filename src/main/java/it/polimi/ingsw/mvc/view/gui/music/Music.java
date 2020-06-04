package it.polimi.ingsw.mvc.view.gui.music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Music {

    static Clip clipper;

    public static void playMusic() {

        try {
            File music = new File("src/main/resources/music/musicname.wav");
            AudioInputStream audio = AudioSystem.getAudioInputStream(music);
            clipper = AudioSystem.getClip();
            //System.out.println("Playing background music...");
            clipper.open(audio);
            clipper.start();
            clipper.loop(Clip.LOOP_CONTINUOUSLY);

        }
        catch (Exception e) {
            //System.out.println("ERROR [MUSIC]");
            e.printStackTrace();
        }
    }

        public static void turnOffMusic(){
            clipper.close();
        }

}
