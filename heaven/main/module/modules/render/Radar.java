/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.PlayerUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.gl.ScaleUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Radar
extends Module {
    private final Numbers<Double> scale = new Numbers<Double>("Scale", 2.0, 1.0, 5.0, 0.1);
    private final Numbers<Double> xe = new Numbers<Double>("X", 500.0, 1.0, 1920.0, 5.0);
    private final Numbers<Double> ye = new Numbers<Double>("Y", 2.0, 1.0, 1080.0, 5.0);
    private final Numbers<Double> size = new Numbers<Double>("Size", 125.0, 50.0, 500.0, 5.0);
    public final Option<Boolean> disOnChat = new Option<Boolean>("DisableOnChat", true);
    float x;
    float y;
    float sizes;

    public Radar() {
        super("Radar", new String[]{"minimap"}, ModuleType.Render);
        this.addValues(this.scale, this.xe, this.ye, this.size, this.disOnChat);
    }

    @Override
    public void onDisable() {
        this.sizes = 0.0f;
        this.y = 0.0f;
        this.x = 0.0f;
    }

    @EventHandler
    public void radar(EventRender2D e) {
        if (!((Boolean)this.disOnChat.get()).booleanValue() || !(Radar.mc.currentScreen instanceof GuiChat)) {
            if (this.x != ((Double)this.xe.get()).floatValue()) {
                this.x = AnimationUtil.moveUD(this.x, ((Double)this.xe.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
            if (this.y != ((Double)this.ye.get()).floatValue()) {
                this.y = AnimationUtil.moveUD(this.y, ((Double)this.ye.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
            if (this.sizes != ((Double)this.size.get()).floatValue()) {
                this.sizes = AnimationUtil.moveUD(this.sizes, ((Double)this.size.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
            this.renderRadar();
        }
    }

    public void renderRadar() {
        GL11.glPushMatrix();
        ScaleUtils.scale(mc);
        Gui.drawRect(this.x - 1.0f, this.y - 1.0f, this.x + this.sizes + 1.0f, this.y + this.sizes + 1.0f, new Color(0, 0, 0, 180).getRGB());
        Gui.drawRect(this.x, this.y, this.x + this.sizes, this.y + this.sizes, new Color(0, 0, 0, 180).getRGB());
        Gui.drawRect((double)(this.x + this.sizes / 2.0f) - 0.05 + 0.0, this.y, (double)(this.x + this.sizes / 2.0f) + 0.05 + 0.0, this.y + this.sizes, new Color(255, 255, 255, 80).getRGB());
        Gui.drawRect(this.x, (double)(this.y + this.sizes / 2.0f) - 0.05, this.x + this.sizes, (double)(this.y + this.sizes / 2.0f) + 0.05, new Color(255, 255, 255, 80).getRGB());
        for (Entity entity : Minecraft.theWorld.getLoadedEntityList()) {
            if (entity == Minecraft.thePlayer || !(entity instanceof EntityPlayer) || entity.isInvisible()) continue;
            float yawToEntity = RotationUtil.getYawToPoint(entity.posX, entity.posZ);
            float yawDiff = -(yawToEntity - Minecraft.thePlayer.rotationYaw + 180.0f);
            double yawDiffRad = Math.toRadians(yawDiff);
            double x = Math.abs(Minecraft.thePlayer.posX - entity.posX);
            double z = Math.abs(Minecraft.thePlayer.posZ - entity.posZ);
            double distance = Math.sqrt(x * x + z * z) / (Double)this.scale.get();
            int color = PlayerUtil.inTeam(entity, Minecraft.thePlayer) ? new Color(0, 231, 255, 255).getRGB() : (((EntityPlayer)entity).getHealth() < 10.0f ? new Color(255, 59, 59, 255).getRGB() : new Color(169, 255, 43, 255).getRGB());
            if (!(Math.sin(yawDiffRad) > 0.0 && Math.cos(yawDiffRad) < 0.0 && Math.sin(yawDiffRad) * distance < (double)(this.sizes / 2.0f) && -Math.cos(yawDiffRad) * distance < (double)(this.sizes / 2.0f) || Math.sin(yawDiffRad) < 0.0 && Math.cos(yawDiffRad) < 0.0 && -Math.sin(yawDiffRad) * distance < (double)(this.sizes / 2.0f) && -Math.cos(yawDiffRad) * distance < (double)(this.sizes / 2.0f) || Math.sin(yawDiffRad) > 0.0 && Math.cos(yawDiffRad) > 0.0 && Math.sin(yawDiffRad) * distance < (double)(this.sizes / 2.0f) && Math.cos(yawDiffRad) * distance < (double)(this.sizes / 2.0f)) && (!(Math.sin(yawDiffRad) < 0.0) || !(Math.cos(yawDiffRad) > 0.0) || !(-Math.sin(yawDiffRad) * distance < (double)(this.sizes / 2.0f)) || !(Math.cos(yawDiffRad) * distance < (double)(this.sizes / 2.0f)))) continue;
            RenderUtil.drawBorderedRect((double)(this.x + this.sizes / 2.0f) + Math.sin(yawDiffRad) * distance - 1.0, (double)(this.y + this.sizes / 2.0f) + Math.cos(yawDiffRad) * distance - 1.0, (double)(this.x + this.sizes / 2.0f) + Math.sin(yawDiffRad) * distance + 1.0, (double)(this.y + this.sizes / 2.0f) + Math.cos(yawDiffRad) * distance + 1.0, 0.1f, color, color);
        }
        GL11.glPopMatrix();
    }
}

