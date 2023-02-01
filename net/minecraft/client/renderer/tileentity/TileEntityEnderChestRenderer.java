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
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityEnderChestRenderer
extends TileEntitySpecialRenderer<TileEntityEnderChest> {
    private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
    private final ModelChest field_147521_c = new ModelChest();

    @Override
    public void renderTileEntityAt(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        int i = 0;
        if (te.hasWorldObj()) {
            i = te.getBlockMetadata();
        }
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(ENDER_CHEST_TEXTURE);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
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
        GlStateManager.rotate(j, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0f - f;
        f = 1.0f - f * f * f;
        this.field_147521_c.chestLid.rotateAngleX = -(f * (float)Math.PI / 2.0f);
        if (Client.instance.getModuleManager().getModuleByClass(ChestESP.class).isEnabled() && te.hasWorldObj()) {
            ChestESP chestESP = new ChestESP();
            if (chestESP.mode.is("Outline")) {
                float[] hexColor = chestESP.getColorForTileEntity();
                int color = chestESP.toRGBAHex(hexColor[0] / 255.0f, hexColor[1] / 255.0f, hexColor[2] / 255.0f, 1.0f);
                this.field_147521_c.renderAll();
                chestESP.pre3D();
                this.field_147521_c.renderAll();
                chestESP.setupStencilFirst();
                this.field_147521_c.renderAll();
                chestESP.setupStencilSecond();
                chestESP.renderOutline(color);
                this.field_147521_c.renderAll();
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
                this.field_147521_c.renderAll();
                GlStateManager.enableDepth();
                sexyHidden = new Color(((Double)chestESP.r.get()).intValue(), ((Double)chestESP.g.get()).intValue(), ((Double)chestESP.b.get()).intValue()).getRGB();
                GL11.glColor4f((float)((float)(sexyHidden >> 16 & 0xFF) / 255.0f), (float)((float)(sexyHidden >> 8 & 0xFF) / 255.0f), (float)((float)(sexyHidden & 0xFF) / 255.0f), (float)255.0f);
                this.field_147521_c.renderAll();
                GL11.glEnable((int)2896);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
            } else {
                this.field_147521_c.renderAll();
            }
        } else {
            this.field_147521_c.renderAll();
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

