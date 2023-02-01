/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.astolfoclickgui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class Limitation {
    public final int startX;
    public final int startY;
    public final int endX;
    public final int endY;

    public Limitation(int x1, int y1, int x2, int y2) {
        this.startX = x1;
        this.startY = y1;
        this.endX = x2;
        this.endY = y2;
    }

    public void cut() {
        Limitation.doGlScissor(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY);
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k) {
            if (Minecraft.displayWidth / (scaleFactor + 1) < 320) break;
            if (Minecraft.displayHeight / (scaleFactor + 1) < 240) break;
            ++scaleFactor;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(Minecraft.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }
}

