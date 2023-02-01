/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonSyntaxException
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.utils.shader;

import com.google.gson.JsonSyntaxException;
import heaven.main.ui.gui.clickgui.RenderUtil;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final ResourceLocation resourceLocation = new ResourceLocation("rise/shader/blur.json");
    private ShaderGroup shaderGroup;
    private Framebuffer framebuffer;
    private int lastFactor;
    private int lastWidth;
    private int lastHeight;

    public void init() {
        try {
            this.shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), this.resourceLocation);
            this.shaderGroup.createBindFramebuffers(Minecraft.displayWidth, Minecraft.displayHeight);
            this.framebuffer = this.shaderGroup.mainFramebuffer;
        }
        catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setValues(int strength) {
        this.shaderGroup.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.getShaders().get(2).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.getShaders().get(3).getShaderManager().getShaderUniform("Radius").set(strength);
    }

    public final void blur(int blurStrength) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        if (this.sizeHasChanged(scaleFactor, width, height) || this.framebuffer == null || this.shaderGroup == null) {
            this.init();
        }
        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;
        this.setValues(blurStrength);
        this.framebuffer.bindFramebuffer(true);
        this.shaderGroup.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }

    public final void blur(double x, double y, double areaWidth, double areaHeight, int blurStrength) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        if (this.sizeHasChanged(scaleFactor, width, height) || this.framebuffer == null || this.shaderGroup == null) {
            this.init();
        }
        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;
        GL11.glEnable((int)3089);
        RenderUtil.scissor(x, y, areaWidth, areaHeight);
        this.framebuffer.bindFramebuffer(true);
        this.shaderGroup.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        this.setValues(blurStrength);
        mc.getFramebuffer().bindFramebuffer(false);
        GL11.glDisable((int)3089);
    }

    private boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return this.lastFactor != scaleFactor || this.lastWidth != width || this.lastHeight != height;
    }
}

