/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.tileentity;

import heaven.main.Client;
import heaven.main.module.modules.render.ChestESP;
import java.awt.Color;
import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityChestRenderer
extends TileEntitySpecialRenderer<TileEntityChest> {
    private static final ResourceLocation textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
    private static final ResourceLocation textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
    private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
    private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
    private static final ResourceLocation textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
    private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    private final ModelChest simpleChest = new ModelChest();
    private final ModelChest largeChest = new ModelLargeChest();
    private boolean isChristmas;

    public TileEntityChestRenderer() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.isChristmas = true;
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        int i;
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        if (!te.hasWorldObj()) {
            i = 0;
        } else {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        }
        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            float f2;
            float f1;
            ModelChest modelchest;
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;
                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                } else if (this.isChristmas) {
                    this.bindTexture(textureChristmas);
                } else if (te.getChestType() == 1) {
                    this.bindTexture(textureTrapped);
                } else {
                    this.bindTexture(textureNormal);
                }
            } else {
                modelchest = this.largeChest;
                if (destroyStage >= 0) {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                } else if (this.isChristmas) {
                    this.bindTexture(textureChristmasDouble);
                } else if (te.getChestType() == 1) {
                    this.bindTexture(textureTrappedDouble);
                } else {
                    this.bindTexture(textureNormalDouble);
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            if (destroyStage < 0) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            GlStateManager.translate((float)x, (float)y + 1.0f, (float)z + 1.0f);
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            GlStateManager.translate(0.5f, 0.5f, 0.5f);
            int j = 0;
            if (i == 2) {
                j = 180;
            }
            if (i == 3) {
                j = 0;
            }
            if (i == 4) {
                j = 90;
            }
            if (i == 5) {
                j = -90;
            }
            if (i == 2 && te.adjacentChestXPos != null) {
                GlStateManager.translate(1.0f, 0.0f, 0.0f);
            }
            if (i == 5 && te.adjacentChestZPos != null) {
                GlStateManager.translate(0.0f, 0.0f, -1.0f);
            }
            GlStateManager.rotate(j, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, -0.5f);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            if (te.adjacentChestZNeg != null && (f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks) > f) {
                f = f1;
            }
            if (te.adjacentChestXNeg != null && (f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks) > f) {
                f = f2;
            }
            f = 1.0f - f;
            f = 1.0f - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * (float)Math.PI / 2.0f);
            if (Client.instance.getModuleManager().getModuleByClass(ChestESP.class).isEnabled() && te.hasWorldObj()) {
                ChestESP chestESP = new ChestESP();
                if (chestESP.mode.is("Outline") || chestESP.mode.is("Combined")) {
                    float[] hexColor = chestESP.getColorForTileEntity();
                    int color = chestESP.toRGBAHex(hexColor[0] / 255.0f, hexColor[1] / 255.0f, hexColor[2] / 255.0f, 1.0f);
                    modelchest.renderAll();
                    chestESP.pre3D();
                    modelchest.renderAll();
                    chestESP.setupStencil();
                    modelchest.renderAll();
                    modelchest.renderAll();
                    chestESP.setupStencil2();
                    chestESP.renderOutline(color);
                    modelchest.renderAll();
                    chestESP.post3D();
                } else if (chestESP.mode.is("Chams")) {
                    int sexyHidden = new Color(((Double)chestESP.r.get()).intValue(), ((Double)chestESP.g.get()).intValue(), ((Double)chestESP.b.get()).intValue()).getRGB();
                    GL11.glPushMatrix();
                    GL11.glDisable((int)3553);
                    GL11.glEnable((int)3042);
                    GL11.glDisable((int)2896);
                    GL11.glEnable((int)2960);
                    GL11.glBlendFunc((int)770, (int)771);
                    GlStateManager.disableDepth();
                    float[] hexColor = chestESP.getColorForTileEntity();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                    GL11.glColor4f((float)((float)(sexyHidden >> 16 & 0xFF) / 255.0f), (float)((float)(sexyHidden >> 8 & 0xFF) / 255.0f), (float)((float)(sexyHidden & 0xFF) / 255.0f), (float)255.0f);
                    modelchest.renderAll();
                    GlStateManager.enableDepth();
                    sexyHidden = new Color(((Double)chestESP.r.get()).intValue(), ((Double)chestESP.g.get()).intValue(), ((Double)chestESP.b.get()).intValue()).getRGB();
                    GL11.glColor4f((float)((float)(sexyHidden >> 16 & 0xFF) / 255.0f), (float)((float)(sexyHidden >> 8 & 0xFF) / 255.0f), (float)((float)(sexyHidden & 0xFF) / 255.0f), (float)255.0f);
                    modelchest.renderAll();
                    GL11.glEnable((int)2896);
                    GL11.glDisable((int)3042);
                    GL11.glEnable((int)3553);
                    GL11.glPopMatrix();
                } else if (chestESP.mode.is("Box")) {
                    GlStateManager.disableDepth();
                    modelchest.renderAll();
                    GlStateManager.enableDepth();
                } else {
                    modelchest.renderAll();
                }
            } else {
                modelchest.renderAll();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
}

