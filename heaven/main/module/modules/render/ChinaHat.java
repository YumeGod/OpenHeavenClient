/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.utils.render.RenderUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

public class ChinaHat
extends Module {
    private static final Mode<String> mode = new Mode("Mode", new String[]{"Old", "New"}, "New");
    private static final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false, () -> mode.isCurrentMode("Old"));
    private static final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, () -> mode.isCurrentMode("Old"), () -> (Boolean)rainbow.getValue() == false);
    private static final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, () -> mode.isCurrentMode("Old"), () -> (Boolean)rainbow.getValue() == false);
    private static final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, () -> mode.isCurrentMode("Old"), () -> (Boolean)rainbow.getValue() == false);
    private static final Numbers<Double> alpha = new Numbers<Double>("Alpha", 255.0, 0.0, 255.0, 5.0, () -> mode.isCurrentMode("Old"));
    private static final Numbers<Double> pointsValue = new Numbers<Double>("Point", 30.0, 3.0, 180.0, 1.0, () -> mode.isCurrentMode("New"));
    private static final Numbers<Double> sizeValue = new Numbers<Double>("Size", 0.5, 0.0, 2.0, 0.1, () -> mode.isCurrentMode("New"));
    private static final Option<Boolean> HUDColor = new Option<Boolean>("HUDColor", true, () -> mode.isCurrentMode("Old"));
    private static final Option<Boolean> renderInFirstPerson = new Option<Boolean>("RenderInFirstPerson", false);
    private static final Mode<String> ColorMode = new Mode("ColorMode", new String[]{"Astolfo", "Rainbow"}, "New", () -> mode.isCurrentMode("New"));
    private static final double[][] pointsCache = new double[181][2];
    static int lastPoints;
    static double lastSize;
    private static final Color[] rainbows;
    private static final Color[] astolfo;

    public ChinaHat() {
        super("ChinaHat", ModuleType.Render);
        this.addValues(mode, ColorMode, r, g, b, alpha, HUDColor, rainbow, renderInFirstPerson, pointsValue, sizeValue);
    }

    @EventHandler
    private void renderHud(EventRender3D event) {
        if (mode.isCurrentMode("New")) {
            if (lastSize != (Double)sizeValue.getValue() || (double)lastPoints != (Double)pointsValue.getValue()) {
                lastSize = (Double)sizeValue.getValue();
                lastPoints = ((Double)pointsValue.getValue()).intValue();
                ChinaHat.genPoints(lastPoints, lastSize);
            }
            for (EntityPlayer entity : Minecraft.theWorld.playerEntities) {
                if (!FriendManager.isFriend(entity.getName())) {
                    if (entity != Minecraft.thePlayer) continue;
                }
                this.drawHat(event, entity);
            }
        }
        if (mode.isCurrentMode("Old")) {
            if (ChinaHat.mc.gameSettings.thirdPersonView == 0 && ((Boolean)renderInFirstPerson.getValue()).booleanValue()) {
                return;
            }
            GL11.glPushMatrix();
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            if (!((Boolean)rainbow.getValue()).booleanValue()) {
                if (((Boolean)HUDColor.getValue()).booleanValue()) {
                    GL11.glColor4f((float)((float)((Double)HUD.r.getValue()).intValue() / 255.0f), (float)((float)((Double)HUD.g.getValue()).intValue() / 255.0f), (float)((float)((Double)HUD.b.getValue()).intValue() / 255.0f), (float)((float)((Double)alpha.getValue()).intValue() / 255.0f));
                } else {
                    GL11.glColor4f((float)((float)((Double)r.getValue()).intValue() / 255.0f), (float)((float)((Double)g.getValue()).intValue() / 255.0f), (float)((float)((Double)b.getValue()).intValue() / 255.0f), (float)((float)((Double)alpha.getValue()).intValue() / 255.0f));
                }
            } else {
                GL11.glColor4f((float)((float)ColorUtils.rainbow(1L, 1.0f).getRed() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getGreen() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getBlue() / 255.0f), (float)((float)((Double)alpha.getValue()).intValue() / 255.0f));
            }
            GL11.glTranslatef((float)0.0f, (float)(Minecraft.thePlayer.height + 0.4f), (float)0.0f);
            GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            Cylinder shaft = new Cylinder();
            shaft.setDrawStyle(100012);
            shaft.setDrawStyle(100012);
            shaft.draw(0.0f, 0.7f, 0.3f, 30, 1);
            GlStateManager.resetColor();
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    private void drawHat(EventRender3D e, EntityLivingBase entity) {
        boolean isPlayerSP = entity.isEntityEqual(Minecraft.thePlayer);
        if (ChinaHat.mc.gameSettings.thirdPersonView == 0 && isPlayerSP && !((Boolean)renderInFirstPerson.getValue()).booleanValue()) {
            return;
        }
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2884);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glShadeModel((int)7425);
        GL11.glEnable((int)3042);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderManager renderManager = mc.getRenderManager();
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)e.getPartialTicks() - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)e.getPartialTicks() - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)e.getPartialTicks() - RenderManager.renderPosZ;
        Color[] colors = new Color[181];
        for (int i = 0; i < colors.length; ++i) {
            colors[i] = ColorMode.isCurrentMode("Rainbow") ? this.fadeBetween(rainbows, 6000.0, (double)i * (6000.0 / (Double)pointsValue.getValue())) : this.fadeBetween(astolfo, 2000.0, (double)i * (2000.0 / (Double)pointsValue.getValue()));
        }
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)(y + 1.9), (double)z);
        if (entity.isSneaking()) {
            GL11.glTranslated((double)0.0, (double)-0.2, (double)0.0);
        }
        GL11.glRotatef((float)((float)RenderUtil.interpolate(isPlayerSP ? (double)Minecraft.thePlayer.prevRotationYaw : (double)entity.prevRotationYaw, isPlayerSP ? (double)Minecraft.thePlayer.rotationYaw : (double)entity.rotationYaw, e.getPartialTicks())), (float)0.0f, (float)-1.0f, (float)0.0f);
        float interpolate = (float)RenderUtil.interpolate(isPlayerSP ? (double)Minecraft.thePlayer.prevRotationPitch : (double)entity.prevRotationPitch, isPlayerSP ? (double)Minecraft.thePlayer.rotationPitch : (double)entity.rotationPitch, e.getPartialTicks());
        GL11.glRotatef((float)(interpolate / 3.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glTranslated((double)0.0, (double)0.0, (double)(interpolate / 270.0f));
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)2.0f);
        GL11.glBegin((int)2);
        ChinaHat.drawCircle(((Double)pointsValue.getValue()).intValue() - 1, colors, 255);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glBegin((int)6);
        GL11.glVertex3d((double)0.0, (double)((Double)sizeValue.getValue() / 2.0), (double)0.0);
        ChinaHat.drawCircle(((Double)pointsValue.getValue()).intValue(), colors, 85);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glDisable((int)3042);
        GL11.glDepthMask((boolean)true);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3553);
    }

    private static void drawCircle(int points, Color[] colors, int alpha) {
        for (int i = 0; i <= points; ++i) {
            double[] point = pointsCache[i];
            Color clr = colors[i];
            GL11.glColor4f((float)((float)clr.getRed() / 255.0f), (float)((float)clr.getGreen() / 255.0f), (float)((float)clr.getBlue() / 255.0f), (float)((float)alpha / 255.0f));
            GL11.glVertex3d((double)point[0], (double)0.0, (double)point[1]);
        }
    }

    private static void genPoints(int points, double size) {
        for (int i = 0; i <= points; ++i) {
            double cos = size * StrictMath.cos((double)i * Math.PI * 2.0 / (double)points);
            double sin = size * StrictMath.sin((double)i * Math.PI * 2.0 / (double)points);
            ChinaHat.pointsCache[i][0] = cos;
            ChinaHat.pointsCache[i][1] = sin;
        }
    }

    public Color fadeBetween(Color[] table, double progress) {
        int i = table.length;
        if (progress == 1.0) {
            return table[0];
        }
        if (progress == 0.0) {
            return table[i - 1];
        }
        double d = Math.max(0.0, (1.0 - progress) * (double)(i - 1));
        int j = (int)d;
        return this.fadeBetween(table[j], table[j + 1], d - (double)j);
    }

    public Color fadeBetween(Color start, Color end, double progress) {
        if (progress > 1.0) {
            progress = 1.0 - progress % 1.0;
        }
        return this.gradient(start, end, progress);
    }

    public Color gradient(Color start, Color end, double progress) {
        double invert = 1.0 - progress;
        return new Color((int)((double)start.getRed() * invert + (double)end.getRed() * progress), (int)((double)start.getGreen() * invert + (double)end.getGreen() * progress), (int)((double)start.getBlue() * invert + (double)end.getBlue() * progress), (int)((double)start.getAlpha() * invert + (double)end.getAlpha() * progress));
    }

    public Color fadeBetween(Color[] table, double speed, double offset) {
        return this.fadeBetween(table, ((double)System.currentTimeMillis() + offset) % speed / speed);
    }

    static {
        rainbows = new Color[]{new Color(30, 250, 215), new Color(0, 200, 255), new Color(50, 100, 255), new Color(100, 50, 255), new Color(255, 50, 240), new Color(255, 0, 0), new Color(255, 150, 0), new Color(255, 255, 0), new Color(0, 255, 0), new Color(80, 240, 155)};
        astolfo = new Color[]{new Color(252, 106, 140), new Color(252, 106, 213), new Color(218, 106, 252), new Color(145, 106, 252), new Color(106, 140, 252), new Color(106, 213, 252), new Color(106, 213, 252), new Color(106, 140, 252), new Color(145, 106, 252), new Color(218, 106, 252), new Color(252, 106, 213), new Color(252, 106, 140)};
    }
}

