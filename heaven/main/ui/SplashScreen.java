/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui;

import heaven.main.Client;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.tenacity.animations.Animation;
import heaven.main.ui.tenacity.animations.impl.EaseInOutQuad;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SplashScreen {
    private static int currentProgress;
    private static ResourceLocation splash;
    private static Animation expandFade;
    private static Framebuffer framebuffer;

    public static void update() {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getLanguageManager() == null) {
            return;
        }
    }

    public static void setProgress(int givenProgress) {
        currentProgress = givenProgress;
        SplashScreen.update();
    }

    public static void init() {
        expandFade = new EaseInOutQuad(1000, 1.0);
    }

    public static void drawSplash() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = sr.getScaleFactor();
        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, sr.getScaledWidth(), sr.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        Gui.drawRect2(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(18, 18, 18).getRGB());
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable((int)3042);
        float textureWH = 39.0f;
        float x = (float)sr.getScaledWidth() / 2.0f;
        float y = (float)sr.getScaledHeight() / 2.0f;
        float stringWidth = Client.instance.FontLoaders.bold45.getStringWidth("Authenticating");
        Client.instance.FontLoaders.bold45.drawString("Authenticating", x - stringWidth / 2.0f, y, -1);
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.getMinecraft().updateDisplay();
    }
}

