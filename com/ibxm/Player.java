/*
 * Decompiled with CFR 0.152.
 */
package com.ibxm;

import com.ibxm.FastTracker2;
import com.ibxm.IBXM;
import com.ibxm.Module;
import com.ibxm.ProTracker;
import com.ibxm.ScreamTracker3;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Player {
    private Thread play_thread;
    final IBXM ibxm = new IBXM(48000);
    private Module module;
    int song_duration;
    int play_position;
    boolean running;
    boolean loop = true;
    final byte[] output_buffer;
    final SourceDataLine output_line = AudioSystem.getSourceDataLine(new AudioFormat(48000.0f, 16, 2, true, true));

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java com.ibxm.Player <module file>");
            System.exit(0);
        }
        FileInputStream file_input_stream = new FileInputStream(args[0]);
        Player player = new Player();
        player.set_module(Player.load_module(file_input_stream));
        file_input_stream.close();
        player.play();
    }

    public static Module load_module(InputStream input) throws IllegalArgumentException, IOException {
        DataInputStream data_input_stream = new DataInputStream(input);
        byte[] xm_header = new byte[60];
        data_input_stream.readFully(xm_header);
        if (FastTracker2.is_xm(xm_header)) {
            return FastTracker2.load_xm(xm_header, data_input_stream);
        }
        byte[] s3m_header = new byte[96];
        System.arraycopy(xm_header, 0, s3m_header, 0, 60);
        data_input_stream.readFully(s3m_header, 60, 36);
        if (ScreamTracker3.is_s3m(s3m_header)) {
            return ScreamTracker3.load_s3m(s3m_header, data_input_stream);
        }
        byte[] mod_header = new byte[1084];
        System.arraycopy(s3m_header, 0, mod_header, 0, 96);
        data_input_stream.readFully(mod_header, 96, 988);
        return ProTracker.load_mod(mod_header, data_input_stream);
    }

    public Player() throws LineUnavailableException {
        this.output_buffer = new byte[4096];
    }

    public void set_module(Module m) {
        if (m != null) {
            this.module = m;
        }
        this.stop();
        this.ibxm.set_module(this.module);
        this.song_duration = this.ibxm.calculate_song_duration();
    }

    public void set_loop(boolean loop) {
        this.loop = loop;
    }

    public void play() {
        this.stop();
        this.play_thread = new Thread(new Driver());
        this.play_thread.start();
    }

    public void stop() {
        this.running = false;
        if (this.play_thread != null) {
            try {
                this.play_thread.join();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    private class Driver
    implements Runnable {
        Driver() {
        }

        @Override
        public void run() {
            if (Player.this.running) {
                return;
            }
            try {
                Player.this.output_line.open();
                Player.this.output_line.start();
                Player.this.play_position = 0;
                Player.this.running = true;
                while (Player.this.running) {
                    int frames = Player.this.song_duration - Player.this.play_position;
                    if (frames > 1024) {
                        frames = 1024;
                    }
                    Player.this.ibxm.get_audio(Player.this.output_buffer, frames);
                    Player.this.output_line.write(Player.this.output_buffer, 0, frames << 2);
                    Player.this.play_position += frames;
                    if (Player.this.play_position < Player.this.song_duration) continue;
                    Player.this.play_position = 0;
                    if (Player.this.loop) continue;
                    Player.this.running = false;
                }
                Player.this.output_line.drain();
                Player.this.output_line.close();
            }
            catch (LineUnavailableException lue) {
                lue.printStackTrace();
            }
        }
    }
}

