/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.sound;

import heaven.main.utils.sound.MusicUtil;
import java.io.InputStream;
import sun.audio.AudioPlayer;

public class SoundManger {
    private InputStream inputStream;

    public void jelloEnableSound() {
        this.inputStream = MusicUtil.class.getResourceAsStream("/assets/minecraft/client/sound/enable.wav");
        AudioPlayer.player.start(this.inputStream);
    }

    public void jelloDisableSound() {
        this.inputStream = MusicUtil.class.getResourceAsStream("/assets/minecraft/client/sound/disable.wav");
        AudioPlayer.player.start(this.inputStream);
    }

    public void skeetAttackSounds() {
        this.inputStream = MusicUtil.class.getResourceAsStream("/assets/minecraft/client/audio/hitsound/skeet.wav");
        AudioPlayer.player.start(this.inputStream);
    }

    public static void main(String[] args) {
        new SoundManger().jelloEnableSound();
    }
}

