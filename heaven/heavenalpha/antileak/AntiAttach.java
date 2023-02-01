/*
 * Decompiled with CFR 0.152.
 */
package heaven.heavenalpha.antileak;

import heaven.heavenalpha.antileak.AntiLeak;
import java.awt.Component;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;

public class AntiAttach {
    private boolean isPatched;

    public void vmCheck() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        for (String s : arguments) {
            if (!this.contains_(s, "Xbootclasspath")) continue;
            this.isPatched = true;
        }
        try {
            if (!("Xboot".contains("X") && "Xboo".contains("b") && "Classpath".contains("h"))) {
                this.isPatched = true;
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        if (this.isPatched) {
            try {
                this.windowMessage(null, "MinecraftVM Error", "Xbootclasspath");
                Minecraft.getMinecraft().shutdown();
                this.crashClient();
            }
            catch (Exception e) {
                e.printStackTrace();
                Minecraft.getMinecraft().shutdown();
                this.crashClient();
            }
        }
    }

    private boolean contains_(String s, String t) {
        char[] array1 = s.toCharArray();
        char[] array2 = t.toCharArray();
        boolean status = false;
        if (array2.length < array1.length) {
            for (int i = 0; i < array1.length; ++i) {
                int j;
                if (array1[i] != array2[0] || i + array2.length - 1 >= array1.length) continue;
                for (j = 0; j < array2.length && array1[i + j] == array2[j]; ++j) {
                }
                if (j != array2.length) continue;
                status = true;
                break;
            }
        }
        return status;
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
            new AntiLeak().publicResetAllCheck();
        }
    }

    private void crashClient() {
        new AntiLeak().publicResetAllCheck();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
    }
}

