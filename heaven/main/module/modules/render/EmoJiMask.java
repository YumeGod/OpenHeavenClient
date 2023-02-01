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
import heaven.main.ui.gui.clickgui.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EmoJiMask
extends Module {
    public EmoJiMask() {
        super("EmoJiMask", new String[]{"mask"}, ModuleType.Render);
    }

    @EventHandler
    public void onEmoji(EventRender3D event) {
        for (EntityPlayer entity : Minecraft.theWorld.playerEntities) {
            if (entity == Minecraft.thePlayer) continue;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            float partialTicks = EmoJiMask.mc.timer.renderPartialTicks;
            double n = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            mc.getRenderManager();
            double x = n - RenderManager.renderPosX;
            double n2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            mc.getRenderManager();
            double y = n2 - RenderManager.renderPosY;
            double n3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            mc.getRenderManager();
            double z = n3 - RenderManager.renderPosZ;
            float SCALE = 0.035f;
            GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f), (float)z);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glScalef((float)(-SCALE), (float)(-SCALE), (float)(-(SCALE / 2.0f)));
            double xLeft = -20.0;
            double yUp = 27.0;
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GlStateManager.disableBlend();
            GL11.glDisable((int)3042);
            RenderUtil.drawImage(new ResourceLocation("client/emoji.png"), -11, 7, 25, 25);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }
}

