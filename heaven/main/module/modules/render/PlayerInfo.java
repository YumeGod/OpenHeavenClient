/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.render.color.ColorManager;
import heaven.main.utils.render.color.Colors;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;

public class PlayerInfo
extends Module {
    public PlayerInfo() {
        super("PlayerInfo", new String[]{"info"}, ModuleType.Render);
    }

    @EventHandler
    public void onRenderGui(EventRender2D event) {
        if (PlayerInfo.mc.gameSettings.showDebugInfo) {
            return;
        }
        float xOff = 77.5f;
        float yOff = 90.0f;
        float height = 50.0f;
        RenderUtil.rectangleBordered(77.5, 84.0, 187.5, 140.0, 0.5, Colors.getColor(0, 0), Colors.getColor(10, 255));
        RenderUtil.rectangleBordered(78.0, 84.5, 187.0, 139.5, 0.5, Colors.getColor(0, 0), Colors.getColor(48, 255));
        RenderUtil.rectangle(78.5, 85.0, 186.5, 139.0, Colors.getColor(17, 255));
        RenderUtil.rectangle(82.5, 84.0, 77.5f + (float)Client.instance.FontLoaders.Comfortaa11.getStringWidth("PlayerInfo") + 5.0f, 86.0, Colors.getColor(17, 255));
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        mc.getTextureManager().bindTexture(Minecraft.thePlayer.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(207, 227, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
        if (Minecraft.thePlayer.isWearing(EnumPlayerModelParts.HAT)) {
            Gui.drawScaledCustomSizeModalRect(77, 77, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
        }
        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
        Client.instance.FontLoaders.Comfortaa11.drawStringWithShadow("PlayerInfo", 82.5, 82.0, -1);
        Client.instance.FontLoaders.Comfortaa10.drawStringWithShadow(Minecraft.thePlayer.getName(), 97.5, 96.0, -1);
        Client.instance.FontLoaders.Comfortaa11.drawStringWithShadow(Minecraft.thePlayer.getHealth() + " HP", 142.5, 89.0, -1);
        RenderUtil.drawGradientRect(122.5, 96.0, 177.5, 100.0, new Color(140, 10, 10, 255).getRGB(), new Color(255, 50, 50, 255).getRGB());
        this.drawcheckbox("OnGround", 77.5f, 90.0f, 5.0f, 18.0f, 100.0f, 100.0f, Minecraft.thePlayer.onGround);
        GlStateManager.pushMatrix();
        EntityPlayerSP entityplayer = Minecraft.thePlayer;
        ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
        int split = 64;
        int y2 = 120;
        for (int index = 3; index >= 0; --index) {
            ItemStack armer = entityplayer.inventory.armorInventory[index];
            if (armer == null) continue;
            stuff.add(armer);
        }
        if (Minecraft.thePlayer.getCurrentEquippedItem() != null) {
            stuff.add(Minecraft.thePlayer.getCurrentEquippedItem());
        }
        for (ItemStack errything : stuff) {
            RenderUtil.rectangleBordered((double)split + 36.0, (double)y2 - 1.0, (double)split + 20.0, (double)y2 + 17.0, 1.0, new Color(0, 0, 0).getRGB(), new Color(255, 255, 255).getRGB());
            if (Minecraft.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 20;
            }
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            PlayerInfo.mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(errything, split, y2);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, errything, split, y2);
            PlayerInfo.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
        }
        GlStateManager.popMatrix();
    }

    public void drawcheckbox(String name, float xOff, float yOff, float x, float y, float p2, float p3, boolean enabled) {
        boolean hovering;
        GlStateManager.pushMatrix();
        String xd = name.charAt(0) + name.toLowerCase().substring(1);
        Client.instance.FontLoaders.Comfortaa11.drawStringWithShadow(xd, x + 7.5f + xOff, y + 1.0f + yOff, Colors.getColor(220, 255));
        RenderUtil.rectangle((double)(x + xOff) + 0.6, (double)(y + yOff) + 0.6, (double)(x + 6.0f + xOff) - 0.6, (double)(y + 6.0f + yOff) - 0.6, Colors.getColor(10, 255));
        RenderUtil.drawGradient(x + xOff + 1.0f, y + yOff + 1.0f, x + 6.0f + xOff - 1.0f, y + 6.0f + yOff - 1.0f, Colors.getColor(76), Colors.getColor(51, 255));
        boolean bl = hovering = p2 >= x + xOff && p3 >= y + yOff && p2 <= x + 35.0f + xOff && p3 <= y + 6.0f + yOff;
        if (enabled) {
            RenderUtil.drawGradient(x + xOff + 1.0f, y + yOff + 1.0f, x + xOff + 5.0f, y + yOff + 5.0f, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255), Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 120));
        }
        if (hovering && !enabled) {
            RenderUtil.rectangle(x + xOff + 1.0f, y + yOff + 1.0f, x + xOff + 5.0f, y + yOff + 5.0f, Colors.getColor(255, 40));
        }
        GlStateManager.popMatrix();
    }
}

