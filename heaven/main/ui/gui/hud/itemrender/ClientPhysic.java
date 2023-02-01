/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.hud.itemrender;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClientPhysic {
    public static final Random random = new Random();
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final RenderItem renderItem = mc.getRenderItem();
    public static long tick;
    public static double rotation;
    public static final ResourceLocation RES_ITEM_GLINT;

    public static void doRenderItemPhysic(Entity par1Entity, double x, double y, double z) {
        EntityItem item;
        ItemStack itemstack;
        rotation = (double)(System.nanoTime() - tick) / 3000000.0;
        if (!ClientPhysic.mc.inGameHasFocus) {
            rotation = 0.0;
        }
        if ((itemstack = (item = (EntityItem)par1Entity).getEntityItem()).getItem() != null) {
            random.setSeed(187L);
            ClientPhysic.mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            ClientPhysic.mc.getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(itemstack);
            int i = ClientPhysic.func_177077_a(item, x, y - (double)0.1f, z, ibakedmodel);
            BlockPos pos = new BlockPos(item);
            if (item.rotationPitch > 360.0f) {
                item.rotationPitch = 0.0f;
            }
            item.getAir();
            if (item.getPosition() != null) {
                if (item.onGround) {
                    if (item.rotationPitch != 0.0f && item.rotationPitch != 90.0f && item.rotationPitch != 180.0f && item.rotationPitch != 270.0f) {
                        double Abstand0 = ClientPhysic.formPositiv(item.rotationPitch);
                        double Abstand90 = ClientPhysic.formPositiv(item.rotationPitch - 90.0f);
                        double Abstand180 = ClientPhysic.formPositiv(item.rotationPitch - 180.0f);
                        double Abstand270 = ClientPhysic.formPositiv(item.rotationPitch - 270.0f);
                        if (Abstand0 <= Abstand90 && Abstand0 <= Abstand180 && Abstand0 <= Abstand270) {
                            item.rotationPitch = item.rotationPitch < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                        }
                        if (Abstand90 < Abstand0 && Abstand90 <= Abstand180 && Abstand90 <= Abstand270) {
                            item.rotationPitch = item.rotationPitch - 90.0f < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                        }
                        if (Abstand180 < Abstand90 && Abstand180 < Abstand0 && Abstand180 <= Abstand270) {
                            item.rotationPitch = item.rotationPitch - 180.0f < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                        }
                        if (Abstand270 < Abstand90 && Abstand270 < Abstand180 && Abstand270 < Abstand0) {
                            item.rotationPitch = item.rotationPitch - 270.0f < 0.0f ? (float)((double)item.rotationPitch + rotation) : (float)((double)item.rotationPitch - rotation);
                        }
                    }
                } else {
                    BlockPos posUp = new BlockPos(item);
                    posUp.add(0.0, 0.2f, 0.0);
                    Material m1 = item.worldObj.getBlockState(posUp).getBlock().getMaterial();
                    Material m2 = item.worldObj.getBlockState(pos).getBlock().getMaterial();
                    boolean m3 = item.isInsideOfMaterial(Material.water);
                    boolean m4 = item.isInWater();
                    item.rotationPitch = m3 | m1 == Material.water | m2 == Material.water | m4 ? (float)((double)item.rotationPitch + rotation / 4.0) : (float)((double)item.rotationPitch + rotation * 2.0);
                }
            }
            GL11.glRotatef((float)item.rotationYaw, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(item.rotationPitch + 90.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.isAmbientOcclusion()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.7f, 0.7f, 0.7f);
                    renderItem.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.pushMatrix();
                if (j > 0 && ClientPhysic.shouldSpreadItems()) {
                    GlStateManager.translate(0.0f, 0.0f, 0.046875f * (float)j);
                }
                renderItem.renderItem(itemstack, ibakedmodel);
                if (!ClientPhysic.shouldSpreadItems()) {
                    GlStateManager.translate(0.0f, 0.0f, 0.046875f);
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            ClientPhysic.mc.getRenderManager().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            ClientPhysic.mc.getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
        }
    }

    public static int func_177077_a(EntityItem item, double x, double y, double z, IBakedModel p_177077_9_) {
        ItemStack itemstack = item.getEntityItem();
        Item item2 = itemstack.getItem();
        if (item2 == null) {
            return 0;
        }
        boolean flag = p_177077_9_.isAmbientOcclusion();
        int i = ClientPhysic.getModelCount(itemstack);
        float f2 = 0.0f;
        GlStateManager.translate((float)x, (float)y + f2 + 0.25f, (float)z);
        float f3 = 0.0f;
        if (flag || ClientPhysic.mc.getRenderManager().renderEngine != null && ClientPhysic.mc.gameSettings.fancyGraphics) {
            GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
        }
        if (!flag) {
            f3 = -0.0f * (float)(i - 1) * 0.5f;
            float f4 = -0.0f * (float)(i - 1) * 0.5f;
            float f5 = -0.046875f * (float)(i - 1) * 0.5f;
            GlStateManager.translate(f3, f4, f5);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }

    public static int getModelCount(ItemStack stack) {
        int b0 = 1;
        if (stack.animationsToGo > 48) {
            b0 = 5;
        } else if (stack.animationsToGo > 32) {
            b0 = 4;
        } else if (stack.animationsToGo > 16) {
            b0 = 3;
        } else if (stack.animationsToGo > 1) {
            b0 = 2;
        }
        return b0;
    }

    public static boolean shouldSpreadItems() {
        return true;
    }

    public static double formPositiv(float rotationPitch) {
        if (rotationPitch > 0.0f) {
            return rotationPitch;
        }
        return -rotationPitch;
    }

    static {
        RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    }
}

