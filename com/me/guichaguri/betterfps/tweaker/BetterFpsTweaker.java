/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.tweaker;

import com.me.guichaguri.betterfps.BetterFpsHelper;
import com.me.guichaguri.betterfps.ITweaker;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BetterFpsTweaker
implements ITweaker {
    private List<String> args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = new ArrayList<String>(args);
        this.args.add("--version");
        this.args.add(profile);
        if (assetsDir != null) {
            this.args.add("--assetsDir");
            this.args.add(assetsDir.getAbsolutePath());
        }
        if (gameDir != null) {
            this.args.add("--gameDir");
            this.args.add(gameDir.getAbsolutePath());
        }
        BetterFpsHelper.MCDIR = gameDir;
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}

