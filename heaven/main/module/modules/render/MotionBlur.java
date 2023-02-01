/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.display.DisplayFrameEvent;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.shader.motionblur.MotionBlurResourceManager;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MotionBlur
extends Module {
    private final Mode<String> type = new Mode("Mode", new String[]{"GL", "Shader"}, "GL");
    public final Numbers<Double> multiplier = new Numbers<Double>("FrameMultiplier", 0.5, 0.05, 0.99, 0.01);
    double lastvalue;
    private Map domainResourceManagers;

    public MotionBlur() {
        super("MotionBlur", ModuleType.Render);
        this.addValues(this.type, this.multiplier);
    }

    @EventHandler
    public void onClientTick(DisplayFrameEvent event) {
        if (this.type.is("GL")) {
            float n = ((Double)this.multiplier.getValue()).floatValue();
            GL11.glAccum((int)259, (float)n);
            GL11.glAccum((int)256, (float)(1.0f - n));
            GL11.glAccum((int)258, (float)1.0f);
        }
    }

    @Override
    public void onDisable() {
        if (this.type.is("Shader")) {
            MotionBlur.mc.entityRenderer.stopUseShader();
        }
    }

    @Override
    public void onEnable() {
        if (this.type.is("Shader")) {
            if (this.domainResourceManagers == null) {
                this.domainResourceManagers = ((SimpleReloadableResourceManager)MotionBlur.mc.getResourceManager()).domainResourceManagers;
            }
            if (!this.domainResourceManagers.containsKey("motionblur")) {
                this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
            }
            if (this.isFastRenderEnabled()) {
                Helper.sendMessage("Please don't open <MotionBlur> with <FastRender>");
                this.setEnabled(false);
                return;
            }
            this.lastvalue = ((Double)this.multiplier.getValue()).intValue();
            this.applyShader();
        }
    }

    public boolean isFastRenderEnabled() {
        return MotionBlur.mc.gameSettings.ofFastRender;
    }

    public void applyShader() {
        if (this.type.is("Shader")) {
            MotionBlur.mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        }
    }

    @EventHandler
    public void onClientTick(EventTick event) {
        if (this.type.is("Shader")) {
            if (Minecraft.thePlayer != null) {
                if (Minecraft.theWorld != null) {
                    if (!Minecraft.getMinecraft().entityRenderer.isShaderActive() || this.lastvalue != (Double)this.multiplier.getValue()) {
                        if (Minecraft.theWorld != null && !this.isFastRenderEnabled()) {
                            this.lastvalue = (Double)this.multiplier.getValue();
                            this.applyShader();
                        }
                    }
                    if (this.domainResourceManagers == null) {
                        this.domainResourceManagers = ((SimpleReloadableResourceManager)MotionBlur.mc.getResourceManager()).domainResourceManagers;
                    }
                    if (!this.domainResourceManagers.containsKey("motionblur")) {
                        this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
                    }
                    if (this.isFastRenderEnabled()) {
                        Helper.sendMessage("Please don't open <MotionBlur> with <FastRender>");
                        this.setEnabled(false);
                    }
                }
            }
        }
    }
}

