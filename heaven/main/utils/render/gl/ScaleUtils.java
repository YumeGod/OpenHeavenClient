/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.render.gl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ScaleUtils {
    public static int[] getScaledMouseCoordinates(Minecraft mc, int mouseX, int mouseY) {
        int x = mouseX;
        int y = mouseY;
        switch (mc.gameSettings.guiScale) {
            case 0: {
                x <<= 1;
                y <<= 1;
                break;
            }
            case 1: {
                x = (int)((double)x * 0.5);
                y = (int)((double)y * 0.5);
                break;
            }
            case 3: {
                x = (int)((double)x * 1.5);
                y = (int)((double)y * 1.5);
            }
        }
        return new int[]{x, y};
    }

    public static double[] getScaledMouseCoordinates(Minecraft mc, double mouseX, double mouseY) {
        double x = mouseX;
        double y = mouseY;
        switch (mc.gameSettings.guiScale) {
            case 0: {
                x *= 2.0;
                y *= 2.0;
                break;
            }
            case 1: {
                x *= 0.5;
                y *= 0.5;
                break;
            }
            case 3: {
                x *= 1.5;
                y *= 1.5;
            }
        }
        return new double[]{x, y};
    }

    public static void scale(Minecraft mc) {
        switch (mc.gameSettings.guiScale) {
            case 0: {
                GlStateManager.scale(0.5, 0.5, 0.5);
                break;
            }
            case 1: {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                break;
            }
            case 3: {
                GlStateManager.scale(0.6666666666666667, 0.6666666666666667, 0.6666666666666667);
            }
        }
    }
}

