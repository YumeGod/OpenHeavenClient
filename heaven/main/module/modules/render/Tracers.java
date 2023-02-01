/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.render.RenderUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers
extends Module {
    private static final Mode<String> colormode = new Mode("Color", new String[]{"DistanceHealth", "Custom"}, "DistanceHealth");
    private static final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false);
    private static final Option<Boolean> Breathinglamp = new Option<Boolean>("Breathinglamp", false);
    private static final Option<Boolean> HUDColor = new Option<Boolean>("HUDColor", true);
    private static final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0);
    private static final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0);
    private static final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0);

    public Tracers() {
        super("Tracers", ModuleType.Render);
        this.addValues(colormode, r, g, b, HUDColor, rainbow, Breathinglamp);
    }

    @EventHandler
    private static void on3DRender(EventRender3D e) {
        for (Entity o : Minecraft.theWorld.loadedEntityList) {
            double[] arrd;
            if (!o.isEntityAlive() || !(o instanceof EntityPlayer)) continue;
            if (o == Minecraft.thePlayer) continue;
            double posX = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double)e.getPartialTicks() - RenderManager.renderPosX;
            double posY = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double)e.getPartialTicks() - RenderManager.renderPosY;
            double posZ = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double)e.getPartialTicks() - RenderManager.renderPosZ;
            boolean old = Tracers.mc.gameSettings.viewBobbing;
            RenderUtil.startDrawing();
            Tracers.mc.gameSettings.viewBobbing = false;
            Tracers.mc.entityRenderer.setupCameraTransform(Tracers.mc.timer.renderPartialTicks, 2);
            Tracers.mc.gameSettings.viewBobbing = old;
            float color = (float)Math.round(255.0 - Minecraft.thePlayer.getDistanceSqToEntity(o) * 255.0 / MathUtil.square((double)Tracers.mc.gameSettings.renderDistanceChunks * 2.5)) / 255.0f;
            if (FriendManager.isFriend(o.getName())) {
                double[] arrd2 = new double[3];
                arrd2[0] = 0.0;
                arrd2[1] = 1.0;
                arrd = arrd2;
                arrd2[2] = 1.0;
            } else {
                double[] arrd3 = new double[3];
                arrd3[0] = color;
                arrd3[1] = 1.0f - color;
                arrd = arrd3;
                arrd3[2] = 0.0;
            }
            Tracers.drawLine(arrd, posX, posY, posZ);
            RenderUtil.stopDrawing();
        }
    }

    private static void drawLine(double[] color, double x, double y, double z) {
        GL11.glEnable((int)2848);
        if (colormode.isCurrentMode("DistanceHealth")) {
            if (color.length >= 4) {
                if (color[3] <= 0.1) {
                    return;
                }
                GL11.glColor4d((double)color[0], (double)color[1], (double)color[2], (double)color[3]);
            } else {
                GL11.glColor3d((double)color[0], (double)color[1], (double)color[2]);
            }
        } else if (!((Boolean)rainbow.getValue()).booleanValue()) {
            if (((Boolean)HUDColor.getValue()).booleanValue()) {
                if (((Boolean)HUD.Breathinglamp.getValue()).booleanValue()) {
                    Color col = new Color(RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB());
                    Color cuscolor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
                    Color Ranbowe = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
                    GL11.glColor4f((float)((float)Ranbowe.getRed() / 255.0f), (float)((float)Ranbowe.getGreen() / 255.0f), (float)((float)Ranbowe.getBlue() / 255.0f), (float)((float)Ranbowe.getAlpha() / 255.0f));
                } else {
                    GL11.glColor4f((float)((float)((Double)HUD.r.getValue()).intValue() / 255.0f), (float)((float)((Double)HUD.g.getValue()).intValue() / 255.0f), (float)((float)((Double)HUD.b.getValue()).intValue() / 255.0f), (float)255.0f);
                }
            } else if (((Boolean)Breathinglamp.getValue()).booleanValue()) {
                Color col = new Color(RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB());
                Color cuscolor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
                Color Ranbowe = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
                GL11.glColor4f((float)((float)Ranbowe.getRed() / 255.0f), (float)((float)Ranbowe.getGreen() / 255.0f), (float)((float)Ranbowe.getBlue() / 255.0f), (float)((float)Ranbowe.getAlpha() / 255.0f));
            } else {
                GL11.glColor4f((float)((float)((Double)r.getValue()).intValue() / 255.0f), (float)((float)((Double)g.getValue()).intValue() / 255.0f), (float)((float)((Double)b.getValue()).intValue() / 255.0f), (float)255.0f);
            }
        } else {
            GL11.glColor4f((float)((float)ColorUtils.rainbow(1L, 1.0f).getRed() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getGreen() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getBlue() / 255.0f), (float)255.0f);
        }
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)0.0, (double)Minecraft.thePlayer.getEyeHeight(), (double)0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }
}

