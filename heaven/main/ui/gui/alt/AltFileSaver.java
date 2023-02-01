/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.alt;

import heaven.main.dev.FileUtils;
import heaven.main.management.FileManager;
import heaven.main.ui.gui.alt.Alt;
import heaven.main.ui.gui.alt.GuiAltManager;
import heaven.main.ui.gui.alt.saveSystem.FileWrite;
import java.io.FileInputStream;
import net.minecraft.util.StringUtils;

public class AltFileSaver {
    public static void saveAlts() {
        StringBuilder altBuilder = new StringBuilder();
        for (Alt alt : GuiAltManager.getAlts()) {
            String a = StringUtils.isNullOrEmpty(alt.getAccount()) ? "NULL_ACCOUNT" : alt.getAccount();
            String p = StringUtils.isNullOrEmpty(alt.getPassword()) ? "NULL_PASSWORD" : alt.getPassword();
            String u = StringUtils.isNullOrEmpty(alt.getUserName()) ? "NULL_USERNAME" : alt.getUserName();
            altBuilder.append(a).append(":").append(p).append(":").append(u).append("\n");
        }
        FileWrite.writeStringTo(altBuilder.toString(), String.valueOf(FileManager.dir), "alts.txt");
    }

    public static void readAlts() {
        try {
            for (String str : FileUtils.readLine(new FileInputStream(FileManager.dir + "alts.txt"))) {
                String account = str.split(":")[0];
                String password = str.split(":")[1];
                String userName = str.split(":")[2];
                GuiAltManager.getAlts().add(new Alt(account, password, userName));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

