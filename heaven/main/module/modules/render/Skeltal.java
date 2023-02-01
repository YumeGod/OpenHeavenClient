/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Skeltal
extends Module {
    private static final Map<EntityPlayer, float[][]> modelRotations = new HashMap<EntityPlayer, float[][]>();
    private final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false);
    private final Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0, () -> (Boolean)this.rainbow.get() == false);
    private final Numbers<Double> g = new Numbers<Double>("Green", 0.0, 0.0, 255.0, 1.0, () -> (Boolean)this.rainbow.get() == false);
    private final Numbers<Double> b = new Numbers<Double>("Blue", 0.0, 0.0, 255.0, 1.0, () -> (Boolean)this.rainbow.get() == false);
    private final Numbers<Double> a = new Numbers<Double>("Alpha", 255.0, 0.0, 255.0, 1.0, () -> (Boolean)this.rainbow.get() == false);

    public Skeltal() {
        super("Skeltal", ModuleType.Render);
        this.addValues(this.r, this.g, this.b, this.a, this.rainbow);
    }

    @EventHandler
    public void onRender(EventRender3D e) {
        GlStateManager.enableBlend();
        GL11.glEnable((int)2848);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        GL11.glHint((int)3154, (int)4354);
        GlStateManager.depthMask(false);
        GL11.glDisable((int)2848);
        GlStateManager.disableLighting();
        modelRotations.keySet().removeIf(player -> !Minecraft.theWorld.playerEntities.contains(player) || !player.isEntityAlive());
        Minecraft.theWorld.playerEntities.forEach(player -> {
            float[][] modelRotations;
            if (player != Minecraft.thePlayer && !player.isInvisible() && (modelRotations = modelRotations.get(player)) != null) {
                GL11.glPushMatrix();
                GL11.glLineWidth((float)1.0f);
                if (((Boolean)this.rainbow.get()).booleanValue()) {
                    GL11.glColor4f((float)(ColorUtils.rainbow(1L, 1.0f).getRed() / 255), (float)(ColorUtils.rainbow(1L, 1.0f).getGreen() / 255), (float)(ColorUtils.rainbow(1L, 1.0f).getBlue() / 255), (float)(((Double)this.a.get()).intValue() / 255));
                } else {
                    GL11.glColor4f((float)(((Double)this.r.get()).intValue() / 255), (float)(((Double)this.g.get()).intValue() / 255), (float)(((Double)this.b.get()).intValue() / 255), (float)(((Double)this.a.get()).intValue() / 255));
                }
                float part = Minecraft.getMinecraft().timer.renderPartialTicks;
                double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)part;
                double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)part;
                double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)part;
                Vec3 interp = new Vec3(interpX, interpY, interpZ);
                double d = interp.xCoord;
                mc.getRenderManager();
                double x = d - RenderManager.renderPosX;
                double d2 = interp.yCoord;
                mc.getRenderManager();
                double y = d2 - RenderManager.renderPosY;
                double d3 = interp.zCoord;
                mc.getRenderManager();
                double z = d3 - RenderManager.renderPosZ;
                GL11.glTranslated((double)x, (double)y, (double)z);
                float bodyYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * Skeltal.mc.timer.renderPartialTicks;
                GL11.glRotatef((float)(-bodyYawOffset), (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glTranslated((double)0.0, (double)0.0, (double)(player.isSneaking() ? -0.235 : 0.0));
                float legHeight = player.isSneaking() ? 0.6f : 0.75f;
                GL11.glPushMatrix();
                GL11.glTranslated((double)-0.125, (double)legHeight, (double)0.0);
                if (modelRotations[3][0] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[3][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (modelRotations[3][1] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[3][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (modelRotations[3][2] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[3][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.125, (double)legHeight, (double)0.0);
                if (modelRotations[4][0] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[4][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (modelRotations[4][1] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[4][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (modelRotations[4][2] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[4][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.0, (double)(-legHeight), (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glTranslated((double)0.0, (double)0.0, (double)(player.isSneaking() ? 0.25 : 0.0));
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.0, (double)(player.isSneaking() ? -0.05 : 0.0), (double)(player.isSneaking() ? -0.01725 : 0.0));
                GL11.glPushMatrix();
                GL11.glTranslated((double)-0.375, (double)((double)legHeight + 0.55), (double)0.0);
                if (modelRotations[1][0] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[1][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (modelRotations[1][1] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[1][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (modelRotations[1][2] != 0.0f) {
                    GL11.glRotatef((float)(-modelRotations[1][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.375, (double)((double)legHeight + 0.55), (double)0.0);
                if (modelRotations[2][0] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[2][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (modelRotations[2][1] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[2][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (modelRotations[2][2] != 0.0f) {
                    GL11.glRotatef((float)(-modelRotations[2][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glRotatef((float)(bodyYawOffset - player.rotationYawHead), (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.0, (double)((double)legHeight + 0.55), (double)0.0);
                if (modelRotations[0][0] != 0.0f) {
                    GL11.glRotatef((float)(modelRotations[0][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glRotatef((float)(player.isSneaking() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glTranslated((double)0.0, (double)(player.isSneaking() ? -0.16175 : 0.0), (double)(player.isSneaking() ? -0.48025 : 0.0));
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.0, (double)legHeight, (double)0.0);
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.0, (double)legHeight, (double)0.0);
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glTranslated((double)0.0, (double)((double)legHeight + 0.55), (double)0.0);
                GL11.glBegin((int)3);
                GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
                GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
                GL11.glEnd();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
            }
        });
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glDisable((int)2848);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
    }

    public static void updateModel(EntityPlayer player, ModelPlayer model) {
        modelRotations.put(player, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
    }
}

