/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.utils.shader;

import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.timer.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class BlurBuffer {
    private static ShaderGroup blurShader;
    private static final Minecraft mc;
    private static Framebuffer buffer;
    private static float lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static ResourceLocation shader;
    private static TimeHelper updateTimer;

    public static void initFboAndShader() {
        try {
            buffer = new Framebuffer(Minecraft.displayWidth, Minecraft.displayHeight, true);
            buffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), buffer, shader);
            blurShader.createBindFramebuffers(Minecraft.displayWidth, Minecraft.displayHeight);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void blurArea(float x, float y, float width, float height, boolean setupOverlay) {
        ScaledResolution scale = new ScaledResolution(mc);
        float factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            BlurBuffer.initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glEnable((int)3089);
        RenderUtil.doGlScissor(x, y, width, height);
        GL11.glPushMatrix();
        buffer.framebufferRenderExt(Minecraft.displayWidth, Minecraft.displayHeight, true);
        GL11.glPopMatrix();
        GL11.glDisable((int)3089);
        if (setupOverlay) {
            BlurBuffer.mc.entityRenderer.setupOverlayRendering();
        }
        GlStateManager.enableDepth();
    }

    public static void blurRoundArea(float x, float y, float width, float height, float roundRadius, boolean setupOverlay) {
        ScaledResolution scale = new ScaledResolution(mc);
        float factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            BlurBuffer.initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glEnable((int)3089);
        RenderUtil.doGlScissor(x, y, width, height);
        GL11.glPushMatrix();
        buffer.framebufferRenderExt(Minecraft.displayWidth, Minecraft.displayHeight, true);
        GL11.glPopMatrix();
        GL11.glDisable((int)3089);
        if (setupOverlay) {
            BlurBuffer.mc.entityRenderer.setupOverlayRendering();
        }
        GlStateManager.enableDepth();
    }

    public static void updateBlurBuffer(boolean setupOverlay) {
        if (updateTimer.delay(16.666666f, true) && blurShader != null) {
            mc.getFramebuffer().unbindFramebuffer();
            BlurBuffer.setShaderConfigs(50.0f, 0.0f, 1.0f);
            buffer.bindFramebuffer(true);
            mc.getFramebuffer().framebufferRenderExt(Minecraft.displayWidth, Minecraft.displayHeight, true);
            if (OpenGlHelper.shadersSupported) {
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                blurShader.loadShaderGroup(BlurBuffer.mc.timer.renderPartialTicks);
                GlStateManager.popMatrix();
            }
            buffer.unbindFramebuffer();
            mc.getFramebuffer().bindFramebuffer(true);
            if (mc.getFramebuffer() != null && BlurBuffer.mc.getFramebuffer().depthBuffer > -1) {
                BlurBuffer.setupFBO(mc.getFramebuffer());
                BlurBuffer.mc.getFramebuffer().depthBuffer = -1;
            }
            if (setupOverlay) {
                BlurBuffer.mc.entityRenderer.setupOverlayRendering();
            }
        }
    }

    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.displayWidth, (int)Minecraft.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencil_depth_buffer_ID);
    }

    static {
        mc = Minecraft.getMinecraft();
        shader = new ResourceLocation("shaders/post/blur.json");
        updateTimer = new TimeHelper();
    }
}

