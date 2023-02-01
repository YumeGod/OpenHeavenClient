/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.me.guichaguri.betterfps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterFps {
    public static final Logger log = LogManager.getLogger((String)"BetterFps");
    public static boolean isClient = false;
    public static int TNT_TICKS = 0;
    public static int MAX_TNT_TICKS = 100;
    public static int TICKABLE_RADIUS_POS = 1;
    public static int TICKABLE_RADIUS_NEG = -1;

    public static void serverStart() {
    }

    public static void worldTick() {
        TNT_TICKS = 0;
    }

    public static boolean isTickable(int dX, int dY) {
        return dX == 0 && dY == 0;
    }
}

