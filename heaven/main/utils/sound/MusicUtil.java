/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicUtil {
    public static synchronized void playSound(final String url, final float gain) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(MusicUtil.class.getResourceAsStream("/assets/minecraft/client/sound/enable.wav"));
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(gain);
                    clip.start();
                }
                catch (Exception e) {
                    System.err.println(e.getMessage() + " " + url);
                }
            }
        }).start();
    }
}

