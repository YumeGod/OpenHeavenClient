/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package heaven.heavenalpha.antileak;

import heaven.heavenalpha.antileak.AntiLeak;
import heaven.main.Client;
import java.awt.Component;
import java.util.Objects;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

public class TitleCheck {
    public void titleCheck() {
        boolean numberA = true;
        boolean numberB = true;
        if (this.nameCheck(2)) {
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdown();
            this.crash("e");
        }
        if (this.nameCheck(1364)) {
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdown();
            this.crash("ae");
        }
        String[] info = new String[]{"M", "i", "n", "e", "c", "r", "a", "f", "t", "o"};
        String indexCore = info[0] + info[1] + info[2] + info[3] + info[4] + info[5] + info[6] + info[7] + info[8];
        String[] numberInfo = new String[]{"1", ".", "8", ".", "9"};
        String numberIndexCore = numberInfo[0] + numberInfo[1] + numberInfo[2] + numberInfo[3] + numberInfo[4];
        if (!Display.getTitle().equals(!Client.Beta ? indexCore + " " + numberIndexCore : Client.instance.nowTitle)) {
            this.msg(null, info[3] + info[5] + info[5] + info[9] + info[5], "ID: " + numberInfo[0] + numberInfo[2] + numberInfo[4]);
            this.crash("df");
            Minecraft.getMinecraft().shutdown();
        }
    }

    private void msg(Component bool, String text, String text2) {
        if (bool == null) {
            if (text != null || text2 != null) {
                if (Objects.requireNonNull(text).isEmpty() || text2.isEmpty()) {
                    this.crash("fb");
                }
                JOptionPane.showInputDialog(null, text, text2);
            } else {
                this.crash("febb");
            }
        } else {
            this.crash("h");
            new AntiLeak().publicResetAllCheck();
        }
    }

    private boolean nameCheck(int stage) {
        if (stage == 1) {
            String[] getBigSkidderName = new String[]{"M", "a", "r", "g", "e", "l", "e", "4", "5", "6", "d"};
            if (Display.getTitle().contains(getBigSkidderName[0] + getBigSkidderName[1] + getBigSkidderName[2] + getBigSkidderName[3] + getBigSkidderName[4] + getBigSkidderName[5] + getBigSkidderName[6])) {
                return true;
            }
            String[] getCrackName = new String[]{"C", "r", "a", "c", "k", "e", "d", "4", "5", "6", "d"};
            if (Display.getTitle().contains(getCrackName[0] + getCrackName[1] + getCrackName[2] + getCrackName[3] + getCrackName[4] + getCrackName[5] + getCrackName[6])) {
                return true;
            }
            String[] getSkidderName = new String[]{"h", "o", "3", "4", "5", "6", "d"};
            if (Display.getTitle().contains(getSkidderName[0] + getSkidderName[1] + getSkidderName[2])) {
                return true;
            }
            if (Display.getTitle().contains(getCrackName[0] + getCrackName[1] + getCrackName[2] + getCrackName[3] + getCrackName[4])) {
                return true;
            }
        } else {
            String[] getBigSkidderName = new String[]{"M", "a", "r", "g", "e", "l", "e", "4", "5", "6", "d"};
            if (Display.getTitle().contains(getBigSkidderName[0] + getBigSkidderName[1] + getBigSkidderName[2] + getBigSkidderName[3] + getBigSkidderName[4] + getBigSkidderName[5] + getBigSkidderName[6])) {
                return true;
            }
            String[] getCrackName = new String[]{"C", "r", "a", "c", "k", "e", "d", "4", "5", "6", "d"};
            if (Display.getTitle().contains(getCrackName[0] + getCrackName[1] + getCrackName[2] + getCrackName[3] + getCrackName[4] + getCrackName[5] + getCrackName[6])) {
                return true;
            }
            String[] getSkidderName = new String[]{"h", "o", "3", "4", "5", "6", "d"};
            if (Display.getTitle().contains(getSkidderName[0] + getSkidderName[1] + getSkidderName[2])) {
                return true;
            }
            if (Display.getTitle().contains(getCrackName[0] + getCrackName[1] + getCrackName[2] + getCrackName[3] + getCrackName[4])) {
                return true;
            }
        }
        return false;
    }

    private void crash(String fakeText) {
        this.msg(null, fakeText, "Some client dog want ck use and id -> 430");
        new AntiLeak().publicResetAllCheck();
        if (fakeText.isEmpty()) {
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdown();
            Runtime.getRuntime().gc();
            Runtime.getRuntime().gc();
        } else {
            Minecraft.getMinecraft().shutdown();
            Minecraft.getMinecraft().shutdown();
            Runtime.getRuntime().gc();
        }
    }
}

