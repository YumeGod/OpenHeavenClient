/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ScaledResolution {
    private final double scaledWidthD;
    private final double scaledHeightD;
    private static int scaledWidth;
    private static int scaledHeight;
    private int scaleFactor;

    public ScaledResolution(Minecraft p_i46445_1_) {
        scaledWidth = Minecraft.displayWidth;
        scaledHeight = Minecraft.displayHeight;
        this.scaleFactor = 1;
        boolean flag = p_i46445_1_.isUnicode();
        int i = p_i46445_1_.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i) {
            if (scaledWidth / (this.scaleFactor + 1) < 320) break;
            if (scaledHeight / (this.scaleFactor + 1) < 240) break;
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = (double)scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = (double)scaledHeight / (double)this.scaleFactor;
        scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }

    public static int getScaledWidth() {
        return scaledWidth;
    }

    public static int getScaledHeight() {
        return scaledHeight;
    }

    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }

    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }
}

