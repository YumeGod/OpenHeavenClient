/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.render;

import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.shader.ShaderUtil;
import heaven.main.utils.shader.Utils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class GradientUtil
implements Utils {
    private static final ShaderUtil gradientMaskShader = new ShaderUtil("Tenacity/Shaders/gradientMask.frag");
    private static final ShaderUtil gradientShader = new ShaderUtil("Tenacity/Shaders/gradient.frag");

    public static void drawGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        gradientShader.init();
        float[] fArray = new float[2];
        fArray[0] = x * (float)sr.getScaleFactor();
        Minecraft.getMinecraft();
        fArray[1] = (float)Minecraft.displayHeight - height * (float)sr.getScaleFactor() - y * (float)sr.getScaleFactor();
        gradientShader.setUniformf("location", fArray);
        gradientShader.setUniformf("rectSize", width * (float)sr.getScaleFactor(), height * (float)sr.getScaleFactor());
        gradientShader.setUniformf("alpha", alpha);
        gradientShader.setUniformf("color1", (float)bottomLeft.getRed() / 255.0f, (float)bottomLeft.getGreen() / 255.0f, (float)bottomLeft.getBlue() / 255.0f);
        gradientShader.setUniformf("color2", (float)topLeft.getRed() / 255.0f, (float)topLeft.getGreen() / 255.0f, (float)topLeft.getBlue() / 255.0f);
        gradientShader.setUniformf("color3", (float)bottomRight.getRed() / 255.0f, (float)bottomRight.getGreen() / 255.0f, (float)bottomRight.getBlue() / 255.0f);
        gradientShader.setUniformf("color4", (float)topRight.getRed() / 255.0f, (float)topRight.getGreen() / 255.0f, (float)topRight.getBlue() / 255.0f);
        ShaderUtil.drawQuads(x, y, width, height);
        gradientShader.unload();
        GlStateManager.disableBlend();
    }

    public static void drawGradientLR(float x, float y, float width, float height, float alpha, Color left, Color right) {
        GradientUtil.drawGradient(x, y, width, height, alpha, left, left, right, right);
    }

    public static void drawGradientTB(float x, float y, float width, float height, float alpha, Color top, Color bottom) {
        GradientUtil.drawGradient(x, y, width, height, alpha, bottom, top, bottom, top);
    }

    public static void applyGradientHorizontal(float x, float y, float width, float height, float alpha, Color left, Color right, Runnable content) {
        GradientUtil.applyGradient(x, y, width, height, alpha, left, left, right, right, content);
    }

    public static void applyGradientVertical(float x, float y, float width, float height, float alpha, Color top, Color bottom, Runnable content) {
        GradientUtil.applyGradient(x, y, width, height, alpha, bottom, top, bottom, top, content);
    }

    public static void applyGradient(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight, Runnable content) {
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        gradientMaskShader.init();
        ScaledResolution sr = new ScaledResolution(mc);
        float[] fArray = new float[2];
        fArray[0] = x * (float)sr.getScaleFactor();
        Minecraft.getMinecraft();
        fArray[1] = (float)Minecraft.displayHeight - height * (float)sr.getScaleFactor() - y * (float)sr.getScaleFactor();
        gradientMaskShader.setUniformf("location", fArray);
        gradientMaskShader.setUniformf("rectSize", width * (float)sr.getScaleFactor(), height * (float)sr.getScaleFactor());
        gradientMaskShader.setUniformf("alpha", alpha);
        gradientMaskShader.setUniformi("tex", 0);
        gradientMaskShader.setUniformf("color1", (float)bottomLeft.getRed() / 255.0f, (float)bottomLeft.getGreen() / 255.0f, (float)bottomLeft.getBlue() / 255.0f);
        gradientMaskShader.setUniformf("color2", (float)topLeft.getRed() / 255.0f, (float)topLeft.getGreen() / 255.0f, (float)topLeft.getBlue() / 255.0f);
        gradientMaskShader.setUniformf("color3", (float)bottomRight.getRed() / 255.0f, (float)bottomRight.getGreen() / 255.0f, (float)bottomRight.getBlue() / 255.0f);
        gradientMaskShader.setUniformf("color4", (float)topRight.getRed() / 255.0f, (float)topRight.getGreen() / 255.0f, (float)topRight.getBlue() / 255.0f);
        content.run();
        gradientMaskShader.unload();
        GlStateManager.disableBlend();
    }
}

