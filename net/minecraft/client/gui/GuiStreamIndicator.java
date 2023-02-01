/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiStreamIndicator {
    private static final ResourceLocation locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
    private final Minecraft mc;
    private final float streamAlpha = 1.0f;
    private final int streamAlphaDelta = 1;

    public GuiStreamIndicator(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void render(int p_152437_1_, int p_152437_2_) {
    }

    private void render(int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.65f + 0.35000002f * this.streamAlpha);
        GuiStreamIndicator guiStreamIndicator = this;
        guiStreamIndicator.mc.getTextureManager().bindTexture(locationStreamIndicator);
        float f = 150.0f;
        float f1 = 0.0f;
        float f2 = (float)p_152436_3_ * 0.015625f;
        float f3 = 1.0f;
        float f4 = (float)(p_152436_3_ + 16) * 0.015625f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16, f).tex(f1, f4).endVertex();
        worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_ + 16, f).tex(f3, f4).endVertex();
        worldrenderer.pos(p_152436_1_ - p_152436_4_, p_152436_2_, f).tex(f3, f2).endVertex();
        worldrenderer.pos(p_152436_1_ - 16 - p_152436_4_, p_152436_2_, f).tex(f1, f2).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

