/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.login;

import heaven.main.ui.gui.login.Alt;
import java.util.ArrayList;
import java.util.List;

public class AltManager {
    public static List<Alt> alts;
    static Alt lastAlt;

    public static void init() {
        AltManager.setupAlts();
    }

    public static Alt getLastAlt() {
        return lastAlt;
    }

    public static void setLastAlt(Alt alt) {
        lastAlt = alt;
    }

    public static void setupAlts() {
        alts = new ArrayList<Alt>();
    }

    public static List<Alt> getAlts() {
        return alts;
    }
}

