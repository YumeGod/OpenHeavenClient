/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.utils.AbuseUtil;
import heaven.main.value.Mode;

public class AutoL
extends Module {
    private static final String[] modes = new String[]{"English", "Chinese"};
    public static Mode<String> mode = new Mode("Language", modes, modes[0]);

    public AutoL() {
        super("AutoL", new String[]{"l"}, ModuleType.Misc);
        this.addValues(mode);
    }

    public static String getAutoLMessage(String PlayerName) {
        if (mode.is("English")) {
            return PlayerName + " L, " + (mode.is("English") ? AbuseUtil.getAbuseEnglish() : AbuseUtil.getAbuseChinese());
        }
        return PlayerName + "\u52a0Q\u7fa4142766499" + (mode.is("English") ? AbuseUtil.getAbuseEnglish() : AbuseUtil.getAbuseChinese());
    }
}

