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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Projectiles
extends Module {
    private MovingObjectPosition blockCollision;

    public Projectiles() {
        super("Projectiles", new String[]{"Projectiles"}, ModuleType.Render);
    }

    @EventHandler
    public void arrowESP(EventRender3D event) {
        EntityPlayerSP player = Minecraft.thePlayer;
        ItemStack stack = player.inventory.getCurrentItem();
        if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
            int item = Item.getIdFromItem(Minecraft.thePlayer.getHeldItem().getItem());
            if (item == 261 || item == 368 || item == 332 || item == 344) {
                AxisAlignedBB aim;
                double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)Projectiles.mc.timer.renderPartialTicks - Math.cos(Math.toRadians(player.rotationYaw)) * (double)0.16f;
                double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)Projectiles.mc.timer.renderPartialTicks + (double)player.getEyeHeight() - 0.1;
                double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)Projectiles.mc.timer.renderPartialTicks - Math.sin(Math.toRadians(player.rotationYaw)) * (double)0.16f;
                double itemBow = stack.getItem() instanceof ItemBow ? 1.0 : (double)0.4f;
                double yaw = Math.toRadians(player.rotationYaw);
                double pitch = Math.toRadians(player.rotationPitch);
                double trajectoryX = -Math.sin(yaw) * Math.cos(pitch) * itemBow;
                double trajectoryY = -Math.sin(pitch) * itemBow;
                double trajectoryZ = Math.cos(yaw) * Math.cos(pitch) * itemBow;
                double trajectory = Math.sqrt(trajectoryX * trajectoryX + trajectoryY * trajectoryY + trajectoryZ * trajectoryZ);
                trajectoryX /= trajectory;
                trajectoryY /= trajectory;
                trajectoryZ /= trajectory;
                if (stack.getItem() instanceof ItemBow) {
                    float bowPower = (float)(72000 - player.getItemInUseCount()) / 20.0f;
                    if ((bowPower = (bowPower * bowPower + bowPower * 2.0f) / 3.0f) > 1.0f) {
                        bowPower = 1.0f;
                    }
                    trajectoryX *= (double)(bowPower *= 3.0f);
                    trajectoryY *= (double)bowPower;
                    trajectoryZ *= (double)bowPower;
                } else {
                    trajectoryX *= 1.5;
                    trajectoryY *= 1.5;
                    trajectoryZ *= 1.5;
                }
                GL11.glPushMatrix();
                GL11.glDisable((int)3553);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glLineWidth((float)2.0f);
                double gravity = stack.getItem() instanceof ItemBow ? 0.05 : 0.03;
                GL11.glColor4f((float)0.0f, (float)1.0f, (float)0.2f, (float)0.5f);
                GL11.glBegin((int)3);
                for (int i = 0; i < 2000; ++i) {
                    GL11.glVertex3d((double)(posX - RenderManager.renderPosX), (double)(posY - RenderManager.renderPosY), (double)(posZ - RenderManager.renderPosZ));
                    posX += trajectoryX * 0.1;
                    posY += trajectoryY * 0.1;
                    posZ += trajectoryZ * 0.1;
                    trajectoryX *= 0.999;
                    trajectoryY *= 0.999;
                    trajectoryZ *= 0.999;
                    trajectoryY -= gravity * 0.1;
                    Vec3 vec = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
                    this.blockCollision = Minecraft.theWorld.rayTraceBlocks(vec, new Vec3(posX, posY, posZ));
                    for (Entity o : Minecraft.theWorld.getLoadedEntityList()) {
                        if (!(o instanceof EntityLivingBase) || o instanceof EntityPlayerSP) continue;
                        EntityLivingBase entity = (EntityLivingBase)o;
                        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(0.3, 0.3, 0.3);
                        MovingObjectPosition entityCollision = entityBoundingBox.calculateIntercept(vec, new Vec3(posX, posY, posZ));
                        if (entityCollision != null) {
                            this.blockCollision = entityCollision;
                        }
                        if (entityCollision != null) {
                            GL11.glColor4f((float)1.0f, (float)0.0f, (float)0.2f, (float)0.5f);
                        }
                        if (entityCollision == null) continue;
                        this.blockCollision = entityCollision;
                    }
                    if (this.blockCollision != null) break;
                }
                GL11.glEnd();
                double renderX = posX - RenderManager.renderPosX;
                double renderY = posY - RenderManager.renderPosY;
                double renderZ = posZ - RenderManager.renderPosZ;
                GL11.glPushMatrix();
                GL11.glTranslated((double)(renderX - 0.5), (double)(renderY - 0.5), (double)(renderZ - 0.5));
                switch (this.blockCollision.sideHit.getIndex()) {
                    case 2: 
                    case 3: {
                        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                        aim = new AxisAlignedBB(0.0, 0.5, -1.0, 1.0, 0.45, 0.0);
                        break;
                    }
                    case 4: 
                    case 5: {
                        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                        aim = new AxisAlignedBB(0.0, -0.5, 0.0, 1.0, -0.45, 1.0);
                        break;
                    }
                    default: {
                        aim = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 0.45, 1.0);
                    }
                }
                Projectiles.drawBox(aim);
                RenderGlobal.drawSelectionBoundingBox(aim);
                GL11.glPopMatrix();
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)2848);
                GL11.glPopMatrix();
            }
        }
        for (Object object : Minecraft.theWorld.loadedEntityList) {
            if (!(object instanceof EntityArrow)) continue;
            EntityArrow arrow = (EntityArrow)object;
            if (arrow.inGround) continue;
            double posX = arrow.posX;
            double posY = arrow.posY;
            double posZ = arrow.posZ;
            double motionX = arrow.motionX;
            double motionY = arrow.motionY;
            double motionZ = arrow.motionZ;
            boolean hasLanded2 = false;
            Projectiles.enableRender3D();
            this.setColor(3196666);
            GL11.glLineWidth((float)2.0f);
            GL11.glBegin((int)3);
            for (int limit2 = 0; !hasLanded2 && limit2 < 300; ++limit2) {
                Vec3 posBefore2 = new Vec3(posX, posY, posZ);
                Vec3 posAfter2 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                MovingObjectPosition landingPosition2 = Minecraft.theWorld.rayTraceBlocks(posBefore2, posAfter2, false, true, false);
                if (landingPosition2 != null) {
                    hasLanded2 = true;
                }
                BlockPos var20 = new BlockPos(posX += motionX, posY += motionY, posZ += motionZ);
                Block var21 = Minecraft.theWorld.getBlockState(var20).getBlock();
                if (var21.getMaterial() == Material.water) {
                    motionX *= 0.6;
                    motionY *= 0.6;
                    motionZ *= 0.6;
                } else {
                    motionX *= 0.99;
                    motionY *= 0.99;
                    motionZ *= 0.99;
                }
                motionY -= (double)0.05f;
                GL11.glVertex3d((double)(posX - RenderManager.renderPosX), (double)(posY - RenderManager.renderPosY), (double)(posZ - RenderManager.renderPosZ));
            }
            GL11.glEnd();
            Projectiles.disableRender3D();
        }
    }

    private static void drawBox(AxisAlignedBB bb) {
        GL11.glBegin((int)7);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glEnd();
    }

    public void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 0xFF) / 255.0f;
        float red = (float)(colorHex >> 16 & 0xFF) / 255.0f;
        float green = (float)(colorHex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(colorHex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)(alpha == 0.0f ? 1.0f : alpha));
    }

    private static void enableRender3D() {
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)1.0f);
    }

    private static void disableRender3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

