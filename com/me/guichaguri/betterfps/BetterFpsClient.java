/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps;

import com.me.guichaguri.betterfps.BetterFps;
import com.me.guichaguri.betterfps.BetterFpsConfig;
import com.me.guichaguri.betterfps.BetterFpsHelper;
import com.me.guichaguri.betterfps.gui.GuiBetterFpsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class BetterFpsClient {
    protected static Minecraft mc;
    private static final KeyBinding MENU_KEY;

    public static void start(Minecraft minecraft) {
        mc = minecraft;
        BetterFps.isClient = true;
        if (BetterFpsConfig.instance == null) {
            BetterFpsHelper.loadConfig();
        }
        BetterFpsHelper.init();
    }

    public static void worldLoad() {
    }

    public static void keyEvent(int key) {
        if (MENU_KEY.getKeyCode() == key) {
            mc.displayGuiScreen(new GuiBetterFpsConfig(BetterFpsClient.mc.currentScreen));
        }
    }

    static {
        MENU_KEY = new KeyBinding("BetterFps", 88, "key.categories.misc");
    }
}

