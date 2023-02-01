/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.antileak;

import heaven.heavenalpha.AAL;
import heaven.heavenalpha.antileak.AntiLeak;
import heaven.heavenalpha.base64.Base64Obf;
import heaven.heavenalpha.web.HWIDUtil;
import heaven.heavenalpha.web.WebUtils;
import heaven.main.Client;
import net.minecraft.client.Minecraft;

public class HWIDSwitcher {
    public void HWIDCheck() {
        new AAL().clientIsLeakingOrInCracked = false;
    }

    private void crashClient() {
        try {
            if (!this.getWebInfo(Client.strHWID).contains(HWIDUtil.getClientHWID(false))) {
                this.crashClient();
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        new AntiLeak().publicResetAllCheck();
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

