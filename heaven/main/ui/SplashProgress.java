/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

public class SplashProgress {
    public static int PROGRESS;
    private static String CURRENT;
    private static ResourceLocation splash;

    public void update() {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().getLanguageManager() != null) {
            Minecraft.getMinecraft();
            this.drawSplash(Minecraft.getTextureManager());
        }
    }

    public void setProgress(int givenProgress, String givenText) {
        PROGRESS = givenProgress;
        CURRENT = givenText;
        this.update();
    }

    public void drawSplash(TextureManager tm) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = scaledResolution.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(scaledResolution.getScaledWidth() * scaleFactor, scaledResolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        if (splash == null) {
            splash = new ResourceLocation("client/guimainmenu/mainmenu.png");
        }
        tm.bindTexture(splash);
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, 1920, 1080, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 1920.0f, 1080.0f);
        this.drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledResolution.getScaledWidth() * scaleFactor, scaledResolution.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.getMinecraft().updateDisplay();
    }

    private void drawProgress() {
        if (Minecraft.getMinecraft().gameSettings != null) {
            Minecraft.getMinecraft();
            if (Minecraft.getTextureManager() != null) {
                CFontRenderer ufr = Client.instance.FontLoaders.bold22;
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                double nProgress = PROGRESS;
                double calc = nProgress / 11.0 * (double)sr.getScaledWidth();
                Gui.drawRect(0, sr.getScaledHeight() - 35, sr.getScaledWidth(), sr.getScaledHeight(), new Color(49, 49, 49, 152).getRGB());
                GlStateManager.resetColor();
                this.resetTextureState();
                ufr.drawString(CURRENT, 20.0f, sr.getScaledHeight() - 20, -1);
                String step = PROGRESS + "/" + 11;
                ufr.drawString(step, sr.getScaledWidth() - 20 - ufr.getStringWidth(step), sr.getScaledHeight() - 20, -505290241);
                GlStateManager.resetColor();
                this.resetTextureState();
                RenderUtil.drawGradientSideways(0.0, sr.getScaledHeight() - 2, (int)calc, sr.getScaledHeight(), new Color(134, 82, 218, 192).getRGB(), new Color(118, 68, 195, 190).getRGB());
                Gui.drawRect(0, sr.getScaledHeight() - 2, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0, 0, 0, 10).getRGB());
            }
        }
    }

    private void resetTextureState() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = -1;
    }

    static {
        CURRENT = "";
    }
}

