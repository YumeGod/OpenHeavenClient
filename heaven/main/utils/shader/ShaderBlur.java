/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.utils.shader;

import heaven.main.event.events.rendering.EventRender3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ShaderBlur {
    private static ShaderGroup blurShader;
    private static Minecraft mc;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static ResourceLocation shader;

    public static void initFboAndShader() {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(Minecraft.displayWidth, Minecraft.displayHeight);
            buffer = new Framebuffer(Minecraft.displayWidth, Minecraft.displayHeight, true);
            buffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void blurArea(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            ShaderBlur.initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        if (OpenGlHelper.isFramebufferEnabled()) {
            buffer.framebufferClear();
            GL11.glScissor((int)(x * factor), (int)(Minecraft.displayHeight - y * factor - height * factor), (int)(width * factor), (int)(height * factor));
            GL11.glEnable((int)3089);
            ShaderBlur.setShaderConfigs(intensity, blurWidth, blurHeight);
            buffer.bindFramebuffer(true);
            blurShader.loadShaderGroup(EventRender3D.ticks);
            mc.getFramebuffer().bindFramebuffer(true);
            GL11.glDisable((int)3089);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableBlend();
            GL11.glScalef((float)factor, (float)factor, (float)0.0f);
        }
    }

    public static void blurArea(float left, float f, float g, float h, float intensity) {
        intensity = Math.max(intensity, 1.0f);
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            ShaderBlur.initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        buffer.framebufferClear();
        GL11.glScissor((int)((int)(left * (float)factor)), (int)((int)((float)Minecraft.displayHeight - f * (float)factor - h * (float)factor)), (int)((int)(g * (float)factor)), (int)((int)h * factor));
        GL11.glEnable((int)3089);
        ShaderBlur.setShaderConfigs(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(EventRender3D.ticks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable((int)3089);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableBlend();
        RenderHelper.enableGUIStandardItemLighting();
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            ShaderBlur.initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor((int)(x * factor), (int)(Minecraft.displayHeight - y * factor - height * factor), (int)(width * factor), (int)(height * factor));
        GL11.glEnable((int)3089);
        ShaderBlur.setShaderConfigs(intensity, blurWidth, blurHeight);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(EventRender3D.ticks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable((int)3089);
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            ShaderBlur.initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glScissor((int)(x * factor), (int)(Minecraft.displayHeight - y * factor - height * factor), (int)(width * factor), (int)(height * factor));
        GL11.glEnable((int)3089);
        ShaderBlur.setShaderConfigs(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(EventRender3D.ticks);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable((int)3089);
    }

    static {
        mc = Minecraft.getMinecraft();
        shader = new ResourceLocation("shaders/post/blur.json");
    }
}

