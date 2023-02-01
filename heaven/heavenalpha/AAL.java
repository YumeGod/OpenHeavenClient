/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha;

import heaven.heavenalpha.AALAPI;
import heaven.heavenalpha.base64.Base64Obf;
import heaven.heavenalpha.web.HWIDUtil;
import heaven.heavenalpha.web.WebUtils;
import heaven.main.Client;
import heaven.main.ui.gui.login.GuiLogin;
import java.awt.Component;
import java.util.Objects;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;

public class AAL {
    public static boolean otherSeverNotGitee = true;
    public boolean clientIsLeakingOrInCracked;

    public final void antialpha() throws NoSuchFieldException {
        if (AALAPI.obfuscateCheck()) {
            Minecraft.getMinecraft().shutdown();
        }
        if (!Minecraft.getMinecraft().alphaantileak) {
            this.Logins();
        } else {
            this.windowMessage(null, "HWIDError", "ID: 189");
            this.crashClient();
        }
    }

    public final void Logins() {
        try {
            if (this.getWebInfo(Client.strHWID).contains(HWIDUtil.getClientHWID(false))) {
                this.clientIsLeakingOrInCracked = false;
                GuiLogin.i = true;
                GuiLogin.j = true;
                GuiLogin.login = true;
                Runtime.getRuntime().gc();
                Client.instance.fontManager.init();
            } else {
                this.clientIsLeakingOrInCracked = false;
                GuiLogin.i = true;
                GuiLogin.j = true;
                GuiLogin.login = true;
                Runtime.getRuntime().gc();
                Client.instance.fontManager.init();
            }
        }
        catch (Exception exception) {
            this.clientIsLeakingOrInCracked = false;
            GuiLogin.i = true;
            GuiLogin.j = true;
            GuiLogin.login = true;
            Runtime.getRuntime().gc();
            Client.instance.fontManager.init();
        }
    }

    private void windowMessage(Component bool, String text, String text2) {
        if (bool == null) {
            if (text != null || text2 != null) {
                if (Objects.requireNonNull(text).isEmpty() || text2.isEmpty()) {
                    this.crashClient();
                }
                JOptionPane.showInputDialog(null, text, text2);
            } else {
                this.crashClient();
            }
        } else {
            this.crashClient();
            this.clientIsLeakingOrInCracked = true;
        }
    }

    private void crashClient() {
        this.clientIsLeakingOrInCracked = true;
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
    }

    private String getWebInfo(String url) {
        return WebUtils.get(Base64Obf.obfTurnIO(url));
    }
}

