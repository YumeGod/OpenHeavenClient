/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.RLEEvent;
import heaven.main.event.events.world.RenderArmEvent;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Chams
extends Module {
    private static final String[] modes = new String[]{"Custom", "Rainbow", "Health", "Team"};
    private final TimerUtil pulseTimer = new TimerUtil();
    private boolean pulsing;
    private float nope;
    private final Option<Boolean> coloredValue = new Option<Boolean>("Colored", true);
    private final Option<Boolean> pulseValue = new Option<Boolean>("Pulse", false);
    private final Option<Boolean> handValue = new Option<Boolean>("Hand", true);
    private final Option<Boolean> fillValue = new Option<Boolean>("Fill", true);
    private final Numbers<Number> redValue = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 5.0);
    private final Numbers<Number> greenValue = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 5.0);
    private final Numbers<Number> blueValue = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0);
    private final Numbers<Number> alphaValue = new Numbers<Double>("Alpha", 35.0, 0.0, 255.0, 5.0);
    private final Mode<String> colorModeValue = new Mode("ColorMode", modes, modes[0]);

    public Chams() {
        super("Chams", new String[]{"Chams"}, ModuleType.Render);
        this.addValues(this.colorModeValue, this.coloredValue, this.pulseValue, this.handValue, this.fillValue, this.redValue, this.greenValue, this.blueValue, this.alphaValue);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.pulseTimer.reset();
        this.pulsing = false;
        this.nope = 0.0f;
    }

    @EventHandler
    void onRenderModel(RenderArmEvent event) {
        if (!((Boolean)this.handValue.getValue()).booleanValue()) {
            return;
        }
        try {
            if (event.getEntity() == Minecraft.thePlayer && Chams.mc.gameSettings.thirdPersonView == 0) {
                if (event.isPre()) {
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.disableLighting();
                    GlStateManager.disableTexture2D();
                    Chams.mc.entityRenderer.disableLightmap();
                    Color color = this.getColor2();
                    switch ((String)this.colorModeValue.getValue()) {
                        case "Rainbow": {
                            Color rain = ColorUtils.getRainbow(10.0f, 1.0f, 1.0f);
                            color = new Color(rain.getRed(), rain.getGreen(), rain.getBlue(), ((Number)this.alphaValue.getValue()).intValue());
                            break;
                        }
                        case "Health": {
                            Color blend = ColorUtils.getBlendColor(Minecraft.thePlayer.getHealth(), Minecraft.thePlayer.getMaxHealth());
                            color = new Color(blend.getRed(), blend.getGreen(), blend.getBlue(), ((Number)this.alphaValue.getValue()).intValue());
                            break;
                        }
                        case "Team": {
                            String text = Minecraft.thePlayer.getDisplayName().getFormattedText();
                            if (Character.toLowerCase(text.charAt(0)) == '\u00a7') {
                                char oneMore = Character.toLowerCase(text.charAt(1));
                                int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                                if (colorCode >= 16) break;
                                try {
                                    int newColor = Minecraft.fontRendererObj.colorCode[colorCode];
                                    color = new Color(newColor >> 16, newColor >> 8 & 0xFF, newColor & 0xFF, ((Number)this.alphaValue.getValue()).intValue());
                                }
                                catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                                break;
                            }
                            color = new Color(255, 255, 255, ((Number)this.alphaValue.getValue()).intValue());
                            break;
                        }
                    }
                    GlStateManager.color(0.003921569f * (float)color.getRed(), 0.003921569f * (float)color.getGreen(), 0.003921569f * (float)color.getBlue(), 0.003921569f * (float)color.getAlpha());
                }
                if (event.isPost()) {
                    GlStateManager.resetColor();
                    Chams.mc.entityRenderer.enableLightmap();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    GlStateManager.disableBlend();
                    GlStateManager.enableTexture2D();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventHandler
    private void onRLE(RLEEvent e) {
        if (e.getEntity() instanceof EntityPlayer && e.isPre()) {
            if (((Boolean)this.coloredValue.getValue()).booleanValue()) {
                e.setCancelled(true);
                try {
                    Render render = mc.getRenderManager().getEntityRenderObject(e.getEntity());
                    if (render != null && Chams.mc.getRenderManager().renderEngine != null && render instanceof RendererLivingEntity) {
                        RendererLivingEntity rendererLivingEntity = (RendererLivingEntity)render;
                        GlStateManager.pushMatrix();
                        GlStateManager.disableTexture2D();
                        GlStateManager.disableAlpha();
                        Chams.mc.entityRenderer.disableLightmap();
                        GlStateManager.enableBlend();
                        if (((Boolean)this.fillValue.getValue()).booleanValue()) {
                            GlStateManager.disableLighting();
                        }
                        Color color = this.getColor2();
                        switch ((String)this.colorModeValue.getValue()) {
                            case "Rainbow": {
                                Color rain = ColorUtils.getRainbow(10.0f, 1.0f, 1.0f);
                                color = new Color(rain.getRed(), rain.getGreen(), rain.getBlue(), ((Number)this.alphaValue.getValue()).intValue());
                            }
                            case "Health": {
                                Color blend = ColorUtils.getBlendColor(e.getEntity().getHealth(), e.getEntity().getMaxHealth());
                                color = new Color(blend.getRed(), blend.getGreen(), blend.getBlue(), ((Number)this.alphaValue.getValue()).intValue());
                            }
                            case "Team": {
                                String text = e.getEntity().getDisplayName().getFormattedText();
                                if (Character.toLowerCase(text.charAt(0)) == '\u00a7') {
                                    char oneMore = Character.toLowerCase(text.charAt(1));
                                    int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
                                    if (colorCode >= 16) break;
                                    try {
                                        int newColor = Minecraft.fontRendererObj.colorCode[colorCode];
                                        color = new Color(newColor >> 16, newColor >> 8 & 0xFF, newColor & 0xFF, ((Number)this.alphaValue.getValue()).intValue());
                                    }
                                    catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
                                    break;
                                }
                                color = new Color(255, 255, 255, ((Number)this.alphaValue.getValue()).intValue());
                            }
                        }
                        if (((Boolean)this.pulseValue.getValue()).booleanValue() && this.pulseTimer.hasReached(15.0)) {
                            if (this.pulsing && this.nope >= 0.5f) {
                                this.pulsing = false;
                            }
                            if (!this.pulsing && this.nope <= 0.0f) {
                                this.pulsing = true;
                            }
                            float f = this.nope = this.pulsing ? this.nope + 0.02f : this.nope - 0.015f;
                            if (this.nope > 1.0f) {
                                this.nope = 1.0f;
                            } else if (this.nope < 0.0f) {
                                this.nope = 0.0f;
                            }
                            this.pulseTimer.reset();
                        }
                        GlStateManager.color(0.003921569f * (float)color.getRed(), 0.003921569f * (float)color.getGreen(), 0.003921569f * (float)color.getBlue(), (Boolean)this.pulseValue.getValue() != false ? this.nope : 0.003921569f * (float)color.getAlpha());
                        GlStateManager.disableDepth();
                        rendererLivingEntity.renderModel(e.getEntity(), e.getLimbSwing(), e.getLimbSwingAmount(), e.getAgeInTicks(), e.getRotationYawHead(), e.getRotationPitch(), e.getOffset());
                        GlStateManager.enableDepth();
                        rendererLivingEntity.renderModel(e.getEntity(), e.getLimbSwing(), e.getLimbSwingAmount(), e.getAgeInTicks(), e.getRotationYawHead(), e.getRotationPitch(), e.getOffset());
                        GlStateManager.resetColor();
                        if (((Boolean)this.fillValue.getValue()).booleanValue()) {
                            GlStateManager.enableLighting();
                        }
                        GlStateManager.disableBlend();
                        Chams.mc.entityRenderer.enableLightmap();
                        GlStateManager.enableAlpha();
                        GlStateManager.enableTexture2D();
                        GlStateManager.popMatrix();
                        rendererLivingEntity.renderLayers(e.getEntity(), e.getLimbSwing(), e.getLimbSwingAmount(), e.getTick(), e.getAgeInTicks(), e.getRotationYawHead(), e.getRotationPitch(), e.getOffset());
                        GlStateManager.popMatrix();
                    }
                }
                catch (Exception exception) {}
            } else {
                GL11.glEnable((int)32823);
                GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);
            }
        } else if (!((Boolean)this.coloredValue.getValue()).booleanValue() && e.getEntity() instanceof EntityPlayer && e.isPost()) {
            GL11.glDisable((int)32823);
            GL11.glPolygonOffset((float)1.0f, (float)1100000.0f);
        }
    }

    public Color getColor2() {
        return new Color(((Number)this.redValue.getValue()).intValue(), ((Number)this.greenValue.getValue()).intValue(), ((Number)this.blueValue.getValue()).intValue(), ((Number)this.alphaValue.getValue()).intValue());
    }
}

