/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.antileak;

import antiskidderobfuscator.NativeMethod;
import heaven.heavenalpha.antileak.HWIDSwitcher;
import heaven.heavenalpha.antileak.TitleCheck;
import heaven.heavenalpha.base64.Base64Obf;
import heaven.heavenalpha.web.HWIDUtil;
import heaven.heavenalpha.web.WebUtils;
import heaven.main.Client;
import java.awt.Component;
import java.util.Objects;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;

public class AntiLeak {
    private boolean hwidCheck;
    private boolean userCheck;
    private boolean vmCheck;

    public void startAntiLeakModule() {
        if (!this.hwidCheck) {
            this.startCheckHWID();
            this.hwidCheck = true;
        }
        if (!this.vmCheck) {
            this.vmCheck = true;
        }
        if (!this.userCheck) {
            // empty if block
        }
        new HWIDSwitcher().HWIDCheck();
        new TitleCheck().titleCheck();
        System.out.println("finished Antileak cheak startAntiLeakModule");
    }

    @NativeMethod
    private void usercheck() {
        if (this.getBadUserNames()) {
            this.crashClient();
            Minecraft.getMinecraft().shutdown();
        } else {
            boolean checkHWIDIsOK = false;
            try {
                if (this.getWebInfo(Client.strHWID).contains(HWIDUtil.getClientHWID(false))) {
                    checkHWIDIsOK = true;
                    this.userCheck = true;
                } else {
                    this.windowMessage(null, "HWIDError", HWIDUtil.getClientHWID(false));
                    this.userCheck = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public final boolean getBadUserNames() {
        return Client.instance.User.contains("Cracker") || Client.instance.User.contains("cracker") || Client.instance.User.contains("1234") || Client.instance.User.contains("123") || Client.instance.User.contains("1") || Client.instance.User.contains("UltraPanda") || Client.instance.User.contains("ho3") || Client.instance.User.isEmpty();
    }

    private String getWebInfo(String url) {
        return WebUtils.get(Base64Obf.obfTurnIO(url));
    }

    private void startCheckHWID() {
        boolean checkHWIDIsOK = true;
        this.hwidCheck = true;
        boolean checkVersionIsOK = true;
        this.hwidCheck = true;
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
            this.resetAllCheck();
        }
    }

    private void crashClient() {
        this.windowMessage(null, "Warning", "Some dog people client window, send this to client OP and kill u using computer");
        this.resetAllCheck();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
    }

    public final void publicResetAllCheck() {
        this.resetAllCheck();
    }

    private void resetAllCheck() {
        this.hwidCheck = true;
        this.vmCheck = true;
        this.userCheck = true;
    }
}

