/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Health
extends Module {
    private double healthBarWidth;
    private int width;
    private final Mode<String> modes = new Mode("Mode", new String[]{"Rect", "Rect2", "Minecraft", "Text"}, "Rect");
    public final Numbers<Double> xe = new Numbers<Double>("RectX", 0.0, 0.0, 1000.0, 0.5);
    public final Numbers<Double> ye = new Numbers<Double>("RectY", 0.0, 0.0, 1000.0, 0.5);
    float x;
    float y;

    public Health() {
        super("Health", ModuleType.Render);
        this.addValues(this.modes, this.xe, this.ye);
    }

    @Override
    public void onDisable() {
        this.y = 0.0f;
        this.x = 0.0f;
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (this.modes.is("Text")) {
            if (Minecraft.thePlayer.getHealth() >= 0.0f) {
                if (Minecraft.thePlayer.getHealth() < 10.0f) {
                    this.width = 3;
                }
            }
            if (Minecraft.thePlayer.getHealth() >= 10.0f) {
                if (Minecraft.thePlayer.getHealth() < 100.0f) {
                    this.width = 6;
                }
            }
            new ScaledResolution(mc);
            new ScaledResolution(mc);
            Client.instance.FontLoaders.Comfortaa18.drawStringWithShadow(String.valueOf(MathHelper.ceiling_float_int(Minecraft.thePlayer.getHealth())), ScaledResolution.getScaledWidth() / 2 - this.width, ScaledResolution.getScaledHeight() / 2 - 13, Minecraft.thePlayer.getHealth() <= 10.0f ? new Color(255, 0, 0).getRGB() : new Color(0, 255, 0).getRGB());
        }
        if (this.modes.is("Minecraft")) {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderHelper.enableGUIStandardItemLighting();
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
            GL11.glDisable((int)2929);
            GL11.glEnable((int)3042);
            GL11.glDepthMask((boolean)false);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            float maxHealth = Minecraft.thePlayer.getMaxHealth();
            int n = 0;
            while ((float)n < maxHealth / 2.0f) {
                Health.mc.ingameGUI.drawTexturedModalRect((float)(scaledResolution.getScaledWidth() / 2) + 1.0f - maxHealth / 2.0f * 10.0f / 2.0f + (float)(n * 10), (float)(scaledResolution.getScaledHeight() / 2 - 20 + 30), 16, 0, 9, 9);
                ++n;
            }
            float health = Minecraft.thePlayer.getHealth();
            int n2 = 0;
            while ((float)n2 < health / 2.0f) {
                Health.mc.ingameGUI.drawTexturedModalRect((float)(scaledResolution.getScaledWidth() / 2) + 1.0f - maxHealth / 2.0f * 10.0f / 2.0f + (float)(n2 * 10), (float)(scaledResolution.getScaledHeight() / 2 - 20 + 30), 52, 0, 9, 9);
                ++n2;
            }
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2929);
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            RenderHelper.disableStandardItemLighting();
        }
        if (this.modes.is("Rect") || this.modes.is("Rect2")) {
            ScaledResolution res = new ScaledResolution(mc);
            float scaledWidth = res.getScaledWidth();
            float scaledHeight = res.getScaledHeight();
            if (this.x != ((Double)this.xe.get()).floatValue()) {
                this.x = AnimationUtil.moveUD(this.x, scaledWidth / 3.0f - 50.0f + 69.0f - 115.0f - 15.0f + (float)((Double)this.xe.getValue()).intValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
            if (this.y != ((Double)this.ye.get()).floatValue()) {
                this.y = AnimationUtil.moveUD(this.y, scaledHeight / 2.0f + 80.0f - 63.5f - 25.0f - 5.0f - (float)((Double)this.ye.getValue()).intValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
        }
        if (this.modes.is("Rect") || this.modes.is("Rect2")) {
            float health = Minecraft.thePlayer.getHealth();
            double hpPercentage = Minecraft.thePlayer.isDead ? 0.0 : (double)(health / Minecraft.thePlayer.getMaxHealth());
            double hpWidth = 152.0 * hpPercentage;
            int healthColor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
            this.healthBarWidth = AnimationUtil.animate(hpWidth, this.healthBarWidth, (double)SimpleRender.processFPS(1000.0f, 0.013f));
            RenderUtil.drawGradientSideways(this.x + 40.0f, this.y + 21.0f, (double)this.x + 192.0, this.y + 27.0f, new Color(0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
            Gui.drawRect(0, 0, 0, 0, 0);
            if (this.modes.is("Rect")) {
                RenderUtil.drawGradientSideways(this.x + 40.0f, this.y + 21.0f, (double)(this.x + 40.0f) + this.healthBarWidth, this.y + 27.0f, healthColor / 2, healthColor);
            }
            if (this.modes.is("Rect2")) {
                RenderUtil.drawGradientSideways(this.x + 40.0f, this.y + 26.0f, (double)(this.x + 40.0f) + this.healthBarWidth, this.y + 27.0f, healthColor / 2, healthColor);
            }
            float fontX = 3.0f;
            Client.instance.FontLoaders.Comfortaa14.drawString(String.format("%.1f", hpPercentage * 100.0) + "%", this.x + 112.0f + 3.0f - (float)(Client.instance.FontLoaders.Comfortaa14.getStringWidth(String.format("%.1f", hpPercentage * 100.0) + "%") / 2), this.y + 23.0f, -1);
            Client.instance.FontLoaders.NovICON12.drawString("s", this.x + 96.0f + 3.0f - (float)(Client.instance.FontLoaders.NovICON10.getStringWidth("s") / 2), this.y + 24.0f, -1);
        }
    }
}

