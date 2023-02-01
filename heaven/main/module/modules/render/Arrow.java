/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.render.RenderUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Arrow
extends Module {
    private final Option<Boolean> players = new Option<Boolean>("Player", true);
    private final Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    private final Option<Boolean> animals = new Option<Boolean>("Animals", false);
    private final Option<Boolean> invisibles = new Option<Boolean>("Invisible", false);
    private final Option<Boolean> renderPlayerName = new Option<Boolean>("RenderPlayerName", false);
    private final Numbers<Double> searchRange = new Numbers<Double>("SearchRange", 25.0, 0.0, 100.0, 0.1);
    private final Numbers<Double> round = new Numbers<Double>("Round", 25.0, 1.0, 200.0, 0.1);
    private final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false);
    private final Option<Boolean> distColor = new Option<Boolean>("DistanceColor", false, () -> (Boolean)this.rainbow.get() == false);
    private final Option<Boolean> Breathinglamp = new Option<Boolean>("Breathinglamp", false, () -> (Boolean)this.rainbow.get() == false);
    private final Option<Boolean> HUDColor = new Option<Boolean>("HUDColor", true, () -> (Boolean)this.rainbow.get() == false);
    private final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)this.HUDColor.get() == false);
    private final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)this.HUDColor.get() == false);
    private final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, () -> (Boolean)this.HUDColor.get() == false);
    private final Numbers<Double> alpha = new Numbers<Double>("Alpha", 255.0, 0.0, 255.0, 5.0);
    float rounds;
    boolean shouldStopOnViewing;

    public Arrow() {
        super("Arrows", new String[]{"osr"}, ModuleType.Render);
        this.addValues(this.players, this.mobs, this.animals, this.invisibles, this.renderPlayerName, this.searchRange, this.round, this.r, this.g, this.b, this.alpha, this.HUDColor, this.rainbow, this.Breathinglamp, this.distColor);
    }

    @Override
    public void onDisable() {
        this.rounds = 0.0f;
        this.shouldStopOnViewing = false;
    }

    @EventHandler
    public void on2D(EventRender2D e) {
        GlStateManager.pushMatrix();
        double size = 50.0;
        ScaledResolution sr = new ScaledResolution(mc);
        double playerOffsetX = Minecraft.thePlayer.posX;
        double playerOffSetZ = Minecraft.thePlayer.posZ;
        double cos = Math.cos((double)Minecraft.thePlayer.rotationYaw * (Math.PI / 180));
        double sin = Math.sin((double)Minecraft.thePlayer.rotationYaw * (Math.PI / 180));
        double loaddist = 0.2;
        float pTicks = e.getPartialTicks();
        if (this.rounds != ((Double)this.round.get()).floatValue()) {
            this.rounds = AnimationUtil.moveUD(this.rounds, ((Double)this.round.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        int i = 0;
        while (true) {
            block7: {
                double rotY;
                double var9;
                double pos2;
                double pos1;
                double rotX;
                double var7;
                Entity gay;
                block8: {
                    if (i >= Minecraft.theWorld.loadedEntityList.size()) break;
                    gay = (Entity)Minecraft.theWorld.loadedEntityList.get(i);
                    if (gay == Minecraft.thePlayer) break block7;
                    if (!((double)Minecraft.thePlayer.getDistanceToEntity(gay) <= (Double)this.searchRange.getValue())) break block7;
                    if (((Boolean)this.mobs.getValue()).booleanValue() && (gay instanceof EntityMob || gay instanceof EntitySlime || gay instanceof EntityVillager) || ((Boolean)this.animals.getValue()).booleanValue() && (gay instanceof EntityAnimal || gay instanceof EntitySquid)) break block8;
                    if (!((Boolean)this.players.getValue()).booleanValue() || !(gay instanceof EntityPlayer)) break block7;
                    if (gay == Minecraft.thePlayer) break block7;
                }
                if ((((Boolean)this.invisibles.getValue()).booleanValue() || !gay.isInvisible()) && (double)MathHelper.sqrt_double((var7 = 0.0 - (rotX = -((pos1 = (gay.posX + (gay.posX - gay.lastTickPosX) * (double)pTicks - playerOffsetX) * loaddist) * cos + (pos2 = (gay.posZ + (gay.posZ - gay.lastTickPosZ) * (double)pTicks - playerOffSetZ) * loaddist) * sin))) * var7 + (var9 = 0.0 - (rotY = -(pos2 * cos - pos1 * sin))) * var9) < 21.0) {
                    float angle = (float)(Math.atan2(rotY - 0.0, rotX - 0.0) * 180.0 / Math.PI);
                    double x = (double)this.rounds * Math.cos(Math.toRadians(angle)) + (double)((float)sr.getScaledWidth() / 2.0f - 24.5f) + 25.0;
                    double y = (double)this.rounds * Math.sin(Math.toRadians(angle)) + (double)((float)sr.getScaledHeight() / 2.0f - 25.2f) + 25.0;
                    if (((Boolean)this.renderPlayerName.getValue()).booleanValue()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(0.5, 0.5, 0.5);
                        Minecraft.fontRendererObj.drawStringWithShadow(gay.getName(), (float)x * 2.0f - (float)Minecraft.fontRendererObj.getStringWidth(gay.getName()) + 20.0f, (float)y * 2.0f - 20.0f, -1);
                        GlStateManager.popMatrix();
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, 0.0);
                    GlStateManager.rotate(angle, 0.0f, 0.0f, 1.0f);
                    GlStateManager.scale(1.5, 1.0, 1.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 3.2, 3.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 3.0, 3.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 2.5, 3.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 2.0, 3.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 1.5, 3.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 1.0, 3.0);
                    this.drawESPCircle(gay, 0.0, 0.0, 0.5, 3.0);
                    GlStateManager.popMatrix();
                }
            }
            ++i;
        }
        GlStateManager.popMatrix();
    }

    private static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    private static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    private void drawESPCircle(Entity target, double cx, double cy, double r, double n) {
        GL11.glPushMatrix();
        cx *= 2.0;
        cy *= 2.0;
        double b = 6.2831852 / n;
        double p = Math.cos(b);
        double s = Math.sin(b);
        double x = r * 2.0;
        double y = 0.0;
        Arrow.enableGL2D();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GlStateManager.resetColor();
        if (!((Boolean)this.rainbow.getValue()).booleanValue()) {
            if (!((Boolean)this.distColor.get()).booleanValue()) {
                Color Ranbowe;
                Color col;
                if (((Boolean)this.HUDColor.getValue()).booleanValue()) {
                    if (((Boolean)HUD.Breathinglamp.getValue()).booleanValue()) {
                        col = new Color(RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB());
                        Color cuscolor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
                        Ranbowe = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
                        GL11.glColor4f((float)((float)Ranbowe.getRed() / 255.0f), (float)((float)Ranbowe.getGreen() / 255.0f), (float)((float)Ranbowe.getBlue() / 255.0f), (float)((float)Ranbowe.getAlpha() / 255.0f));
                    } else {
                        GL11.glColor4f((float)((float)((Double)HUD.r.getValue()).intValue() / 255.0f), (float)((float)((Double)HUD.g.getValue()).intValue() / 255.0f), (float)((float)((Double)HUD.b.getValue()).intValue() / 255.0f), (float)((float)((Double)this.alpha.getValue()).intValue() / 255.0f));
                    }
                } else if (((Boolean)this.Breathinglamp.getValue()).booleanValue()) {
                    col = new Color(RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB());
                    Color cuscolor = new Color(((Double)this.r.getValue()).intValue(), ((Double)this.g.getValue()).intValue(), ((Double)this.b.getValue()).intValue());
                    Ranbowe = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
                    GL11.glColor4f((float)((float)Ranbowe.getRed() / 255.0f), (float)((float)Ranbowe.getGreen() / 255.0f), (float)((float)Ranbowe.getBlue() / 255.0f), (float)((float)Ranbowe.getAlpha() / 255.0f));
                } else {
                    GL11.glColor4f((float)((float)((Double)this.r.getValue()).intValue() / 255.0f), (float)((float)((Double)this.g.getValue()).intValue() / 255.0f), (float)((float)((Double)this.b.getValue()).intValue() / 255.0f), (float)((float)((Double)this.alpha.getValue()).intValue() / 255.0f));
                }
            } else {
                double[] arrd;
                float color = (float)Math.round(255.0 - Minecraft.thePlayer.getDistanceSqToEntity(target) * 255.0 / MathUtil.square((double)Arrow.mc.gameSettings.renderDistanceChunks * 2.5)) / 255.0f;
                if (FriendManager.isFriend(target.getName())) {
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
                GL11.glColor3d((double)arrd[0], (double)arrd[1], (double)arrd[2]);
            }
        } else {
            GL11.glColor4f((float)((float)ColorUtils.rainbow(1L, 1.0f).getRed() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getGreen() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getBlue() / 255.0f), (float)((float)((Double)this.alpha.getValue()).intValue() / 255.0f));
        }
        GL11.glBegin((int)2);
        for (double ii = 0.0; ii < n; ii += 1.0) {
            GL11.glVertex2d((double)(x + cx), (double)(y + cy));
            double t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        Arrow.disableGL2D();
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}

