/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps;

import com.me.guichaguri.betterfps.BetterFpsHelper;
import java.lang.reflect.Field;

public class BetterFpsConfig {
    protected static BetterFpsConfig instance = null;
    public String algorithm = "rivens-half";
    public boolean updateChecker = true;
    public boolean preallocateMemory = false;
    public boolean fastBoxRender = true;
    public boolean fog = true;
    public boolean fastHopper = true;
    public boolean fastBeacon = true;

    public static BetterFpsConfig getConfig() {
        if (instance == null) {
            BetterFpsHelper.loadConfig();
        }
        return instance;
    }

    public static Object getValue(String key) {
        if (instance == null) {
            BetterFpsHelper.loadConfig();
        }
        try {
            Field f = BetterFpsConfig.class.getDeclaredField(key);
            return f.get(instance);
        }
        catch (Exception ex) {
            return null;
        }
    }
}

