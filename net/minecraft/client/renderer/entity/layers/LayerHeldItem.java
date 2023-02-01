/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import heaven.main.Client;
import heaven.main.module.modules.render.Animations;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItem
implements LayerRenderer<EntityLivingBase> {
    private final RendererLivingEntity<?> livingEntityRenderer;

    public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        block8: {
            itemstack = entitylivingbaseIn.getHeldItem();
            if (itemstack == null) break block8;
            GlStateManager.pushMatrix();
            if (this.livingEntityRenderer.getMainModel().isChild) {
                f = 0.5f;
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                GlStateManager.scale(f, f, f);
            }
            Minecraft.getMinecraft();
            if (entitylivingbaseIn != Minecraft.thePlayer) ** GOTO lbl-1000
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.isBlocking() && Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled() && ((Boolean)Animations.betterThirdPersonBlock.getValue()).booleanValue()) {
                if (entitylivingbaseIn.isSneaking()) {
                    ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
                    GlStateManager.translate(-0.58f, 0.3f, -0.2f);
                    GlStateManager.rotate(-24390.0f, 137290.0f, -2009900.0f, -2054900.0f);
                } else {
                    ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0325f);
                    GlStateManager.translate(-0.48f, 0.2f, -0.2f);
                    GlStateManager.rotate(-24390.0f, 137290.0f, -2009900.0f, -2054900.0f);
                }
            } else lbl-1000:
            // 2 sources

            {
                ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
            }
            GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).fishEntity != null) {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }
            item = itemstack.getItem();
            minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                f1 = 0.375f;
                GlStateManager.scale(-f1, -f1, f1);
            }
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.203125f, 0.0f);
            }
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

