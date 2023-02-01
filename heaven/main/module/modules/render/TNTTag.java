/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.font.CFontRenderer;
import java.awt.Color;
import java.text.DecimalFormat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;

public class TNTTag
extends Module {
    public TNTTag() {
        super("TNTTag", ModuleType.Render);
    }

    public static void renderTag(RenderTNTPrimed renderTNTPrimed, EntityTNTPrimed tntPrimed, double x, double y, double z, float partialTicks) {
        if (tntPrimed.fuse < 1) {
            return;
        }
        double d0 = tntPrimed.getDistanceSqToEntity(renderTNTPrimed.getRenderManager().livingPlayer);
        if (d0 <= 4096.0) {
            float number = ((float)tntPrimed.fuse - partialTicks) / 20.0f;
            String str = new DecimalFormat("0.00").format(number);
            CFontRenderer fontrenderer = Client.instance.FontLoaders.Comfortaa20;
            float f = 1.6f;
            float f1 = 0.016666668f * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + tntPrimed.height + 0.5f, (float)z);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            renderTNTPrimed.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            int xMultiplier = 1;
            if (mc != null && TNTTag.mc.gameSettings != null && TNTTag.mc.gameSettings.thirdPersonView == 2) {
                xMultiplier = -1;
            }
            renderTNTPrimed.getRenderManager();
            GlStateManager.rotate(RenderManager.playerViewX * (float)xMultiplier, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;
            int j = fontrenderer.getStringWidth(str) / 2;
            float green = Math.min((float)tntPrimed.fuse / 80.0f, 1.0f);
            Color color = new Color(1.0f - green, green, 0.0f);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(-j - 1, -1 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(-j - 1, 8 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j + 1, 8 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            worldrenderer.pos(j + 1, -1 + i, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i - 3, color.getRGB());
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
}

