/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.render.color.Colors;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryHUD
extends Module {
    private final String[] UIs = new String[]{"ColorShadow", "Simple", "ItemSimple"};
    private final Mode<String> mode = new Mode("UI", this.UIs, this.UIs[0]);
    private final Numbers<Double> xe = new Numbers<Double>("X", 0.0, 0.0, 1000.0, 1.0);
    private final Numbers<Double> ye = new Numbers<Double>("Y", 0.0, 0.0, 1000.0, 1.0);
    float x2;
    float y2;

    public InventoryHUD() {
        super("InventoryHUD", new String[]{"invhud"}, ModuleType.Render);
        this.addValues(this.mode, this.xe, this.ye);
    }

    @Override
    public void onDisable() {
        this.y2 = 0.0f;
        this.x2 = 0.0f;
    }

    @EventHandler
    public void on2D(EventRender2D e) {
        if (InventoryHUD.mc.gameSettings.showDebugInfo) {
            return;
        }
        if (this.mode.is("Simple") || this.mode.is("ColorShadow")) {
            float x = 3.0f;
            float y = 201.0f;
            if (this.x2 != ((Double)this.xe.get()).floatValue()) {
                this.x2 = AnimationUtil.moveUD(this.x2, ((Double)this.xe.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
            if (this.y2 != ((Double)this.ye.get()).floatValue()) {
                this.y2 = AnimationUtil.moveUD(this.y2, ((Double)this.ye.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            }
            if (this.mode.is("Simple")) {
                RenderUtil.drawRect(2.0f + this.x2, 365.0f - this.y2, 163.0f + this.x2, 366.0f - this.y2, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB());
                RenderUtil.drawRect(2.0f + this.x2, 366.0f - this.y2, 163.0f + this.x2, 434.0f - this.y2, new Color(0, 0, 0, 180).getRGB());
            }
            if (this.mode.is("ColorShadow")) {
                RenderUtil.drawGradientSideways(2.0f + this.x2, 369.0f - this.y2 - 4.0f, 163.0f + this.x2, 379.0f - this.y2, new Color(((Double)HUD.r.getValue()).intValue() / 2, ((Double)HUD.g.getValue()).intValue() / 2, ((Double)HUD.b.getValue()).intValue() / 2).getRGB(), new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB());
                RenderUtil.drawGradientSidewaysV(2.0f + this.x2, 379.0f - this.y2, 163.0f + this.x2, 434.0f - this.y2, new Color(23, 25, 24, 200).getRGB(), new Color(23, 25, 24, 120).getRGB());
                RenderUtil.drawGradientSideways(2.0f + this.x2, 369.0f - this.y2 - 4.0f, 163.0f + this.x2 - 145.0f, 379.0f - this.y2, new Color(0, 0, 0, 100).getRGB(), new Color(0, 0, 0, 100).getRGB());
            }
            if (this.mode.is("Simple")) {
                Client.instance.FontLoaders.bold16.drawString("Inventory", 5.0f + this.x2, 270.0f - this.y2 + 2.0f + 100.0f, new Color(255, 255, 255).getRGB());
            }
            if (this.mode.is("ColorShadow")) {
                Client.instance.FontLoaders.Comfortaa16.drawString("Inventory HUD", 5.0f + this.x2 + 16.0f, 270.0f - this.y2 + 100.0f, -1);
                Client.instance.FontLoaders.FLUXICON21.drawString("c", 5.0f + this.x2, 270.0f - this.y2 + 100.0f, -1);
            }
            if (this.emptyInventory()) {
                if (this.mode.is("ColorShadow")) {
                    Client.instance.FontLoaders.Comfortaa16.drawCenteredString("Your inventory is empty", 81.5f + this.x2, 301.5f - this.y2 + 100.0f, new Color(255, 255, 255).getRGB());
                }
                if (this.mode.is("Simple")) {
                    Client.instance.FontLoaders.Comfortaa16.drawCenteredString("Empty ", 81.5f + this.x2, 301.5f - this.y2 + 100.0f, new Color(0, 0, 0, 150).getRGB());
                }
                return;
            }
            int i = 0;
            while (true) {
                if (i >= Minecraft.thePlayer.inventory.mainInventory.length) break;
                if (i >= 9) {
                    ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enableLighting();
                        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int)(x + this.x2), (int)(y + 79.0f - this.y2 + 100.0f));
                        GlStateManager.popMatrix();
                        mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, stack, (int)(x + this.x2), (int)(y + 79.0f - this.y2 + 100.0f));
                        GlStateManager.disableLighting();
                    }
                    if (x < 130.0f) {
                        x += 18.0f;
                    } else {
                        x = 2.0f;
                        y += 18.0f;
                    }
                }
                ++i;
            }
        }
        if (this.mode.is("ItemSimple")) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((Double)this.xe.getValue(), (Double)this.ye.getValue(), 0.0);
            RenderUtil.drawBorderedRect(0.0, 1.0, 180.0, -58.0, 1.0f, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 130).getRGB());
            RenderHelper.enableGUIStandardItemLighting();
            InventoryHUD.renderArmor();
            int x2 = 1;
            int x3 = 1;
            int x4 = 1;
            for (int i2 = 27; i2 < 36; ++i2) {
                this.renderItem(i2, 1 + x2, -16, Minecraft.thePlayer);
                x2 += 20;
            }
            for (int i3 = 18; i3 < 27; ++i3) {
                this.renderItem(i3, 1 + x3, -36, Minecraft.thePlayer);
                x3 += 20;
            }
            for (int i4 = 9; i4 < 18; ++i4) {
                this.renderItem(i4, 1 + x4, -56, Minecraft.thePlayer);
                x4 += 20;
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
        if (this.mode.is("RectAngle")) {
            Color fillWithOpacity = new Color(180, 180, 180, 180);
            Color outlineWithOpacity = new Color(250, 200, 200, 255);
            RenderUtil.drawRect(((Double)this.xe.getValue()).intValue() + 1, ((Double)this.ye.getValue()).intValue() + 1, ((Double)this.xe.getValue()).intValue() + 161, ((Double)this.ye.getValue()).intValue() + 55, fillWithOpacity.getRGB());
            RenderUtil.drawRect(((Double)this.xe.getValue()).intValue(), ((Double)this.ye.getValue()).intValue(), ((Double)this.xe.getValue()).intValue() + 162, ((Double)this.ye.getValue()).intValue() + 1, outlineWithOpacity.getRGB());
            RenderUtil.drawRect(((Double)this.xe.getValue()).intValue(), ((Double)this.ye.getValue()).intValue() + 55, ((Double)this.xe.getValue()).intValue() + 162, ((Double)this.ye.getValue()).intValue() + 56, outlineWithOpacity.getRGB());
            RenderUtil.drawRect(((Double)this.xe.getValue()).intValue(), ((Double)this.ye.getValue()).intValue(), ((Double)this.xe.getValue()).intValue() + 1, ((Double)this.ye.getValue()).intValue() + 56, outlineWithOpacity.getRGB());
            RenderUtil.drawRect(((Double)this.xe.getValue()).intValue() + 161, ((Double)this.ye.getValue()).intValue(), ((Double)this.xe.getValue()).intValue() + 162, ((Double)this.ye.getValue()).intValue() + 56, outlineWithOpacity.getRGB());
            float boxWidth = 165.0f;
            RenderUtil.rectangleBordered((double)(((Double)this.xe.getValue()).intValue() - 3) - 0.5, (double)(((Double)this.ye.getValue()).intValue() - 3) - 0.3, (double)((float)((Double)this.xe.getValue()).intValue() + 165.0f) + 0.5, (double)(((Double)this.ye.getValue()).intValue() + 59) + 0.3, 0.5, Colors.getColor(60), Colors.getColor(10));
            RenderUtil.rectangleBordered((double)(((Double)this.xe.getValue()).intValue() - 3) + 0.5, (double)(((Double)this.ye.getValue()).intValue() - 3) + 0.6, (double)((float)((Double)this.xe.getValue()).intValue() + 165.0f) - 0.5, (double)(((Double)this.ye.getValue()).intValue() + 59) - 0.6, 1.3, Colors.getColor(60), Colors.getColor(40));
            RenderUtil.rectangleBordered((double)(((Double)this.xe.getValue()).intValue() - 3) + 2.5, (double)(((Double)this.ye.getValue()).intValue() - 3) + 2.5, (double)((float)((Double)this.xe.getValue()).intValue() + 165.0f) - 2.5, (double)(((Double)this.ye.getValue()).intValue() + 59) - 2.5, 0.5, Colors.getColor(22), Colors.getColor(12));
            RenderUtil.drawGradientSideways(((Double)this.xe.getValue()).intValue(), (double)((Double)this.ye.getValue()).intValue() - 0.5, (double)((float)((Double)this.xe.getValue()).intValue() + 165.0f) - 2.5, ((Double)this.ye.getValue()).intValue(), Colors.getColor(55, 177, 218), Colors.getColor(204, 77, 198));
            RenderUtil.rectangle(((Double)this.xe.getValue()).intValue(), ((Double)this.ye.getValue()).intValue(), (double)((float)((Double)this.xe.getValue()).intValue() + 165.0f) - 2.5, ((Double)this.ye.getValue()).intValue(), Colors.getColor(0, 110));
            RenderUtil.rectangleBordered(((Double)this.xe.getValue()).intValue(), ((Double)this.ye.getValue()).intValue(), ((Double)this.xe.getValue()).intValue() + 162, ((Double)this.ye.getValue()).intValue() + 56, 0.3, Colors.getColor(48), Colors.getColor(10));
            RenderUtil.rectangle(((Double)this.xe.getValue()).intValue() + 1, ((Double)this.ye.getValue()).intValue() + 1, ((Double)this.xe.getValue()).intValue() + 161, ((Double)this.ye.getValue()).intValue() + 55, Colors.getColor(17));
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            ItemStack[] items = Minecraft.thePlayer.inventory.mainInventory;
            int size = items.length;
            for (int item = 9; item < size; ++item) {
                int slotX = ((Double)this.xe.getValue()).intValue() + item % 9 * 18;
                int slotY = ((Double)this.ye.getValue()).intValue() + 2 + (item / 9 - 1) * 18;
                mc.getRenderItem().renderItemAndEffectIntoGUI(items[item], slotX + 1, slotY);
                mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, items[item], slotX, slotY);
            }
            RenderHelper.disableStandardItemLighting();
            InventoryHUD.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.popMatrix();
        }
    }

    private static void renderArmor() {
        int protectionenchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, Minecraft.thePlayer.getEquipmentInSlot(4));
        int protectionenchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, Minecraft.thePlayer.getEquipmentInSlot(3));
        int protectionenchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, Minecraft.thePlayer.getEquipmentInSlot(2));
        int protectionenchantmentLevel1 = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, Minecraft.thePlayer.getEquipmentInSlot(1));
        int unbreakingenchantmentLevel44 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, Minecraft.thePlayer.getEquipmentInSlot(4));
        int unbreakingenchantmentLevel33 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, Minecraft.thePlayer.getEquipmentInSlot(3));
        int unbreakingenchantmentLevel22 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, Minecraft.thePlayer.getEquipmentInSlot(2));
        int unbreakingenchantmentLevel11 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, Minecraft.thePlayer.getEquipmentInSlot(1));
        int sharpnessenchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, Minecraft.thePlayer.getEquipmentInSlot(0));
        int fireAspectenchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, Minecraft.thePlayer.getEquipmentInSlot(0));
        if (Minecraft.thePlayer.getEquipmentInSlot(4) != null) {
            RenderUtil.drawRect(0.0, 3.0, 20.0, 30.0, new Color(0, 0, 0, 150).getRGB());
            mc.getRenderItem().renderItemAndEffectIntoGUI(Minecraft.thePlayer.getEquipmentInSlot(4), 2, 10);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, Minecraft.thePlayer.getEquipmentInSlot(4), 2, 10);
            if (protectionenchantmentLevel4 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Pro\u00a72" + protectionenchantmentLevel4, 3.0, 10.0, -1);
            }
            if (unbreakingenchantmentLevel44 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Unb\u00a72" + unbreakingenchantmentLevel44, 3.0, 5.0, -1);
            }
        }
        if (Minecraft.thePlayer.getEquipmentInSlot(3) != null) {
            RenderUtil.drawRect(20.0, 3.0, 40.0, 30.0, new Color(0, 0, 0, 150).getRGB());
            if (protectionenchantmentLevel3 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Pro\u00a72" + protectionenchantmentLevel3, 23.0, 9.0, -1);
            }
            if (unbreakingenchantmentLevel33 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Unb\u00a72" + unbreakingenchantmentLevel33, 23.0, 4.0, -1);
            }
            mc.getRenderItem().renderItemAndEffectIntoGUI(Minecraft.thePlayer.getEquipmentInSlot(3), 22, 10);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, Minecraft.thePlayer.getEquipmentInSlot(3), 42, 10);
        }
        if (Minecraft.thePlayer.getEquipmentInSlot(2) != null) {
            RenderUtil.drawRect(40.0, 3.0, 60.0, 30.0, new Color(0, 0, 0, 150).getRGB());
            mc.getRenderItem().renderItemAndEffectIntoGUI(Minecraft.thePlayer.getEquipmentInSlot(2), 42, 10);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, Minecraft.thePlayer.getEquipmentInSlot(2), 42, 10);
            if (protectionenchantmentLevel2 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Pro\u00a72" + protectionenchantmentLevel2, 43.0, 9.0, -1);
            }
            if (unbreakingenchantmentLevel22 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Unb\u00a72" + unbreakingenchantmentLevel22, 43.0, 4.0, -1);
            }
        }
        if (Minecraft.thePlayer.getEquipmentInSlot(1) != null) {
            RenderUtil.drawRect(60.0, 3.0, 80.0, 30.0, new Color(0, 0, 0, 150).getRGB());
            mc.getRenderItem().renderItemAndEffectIntoGUI(Minecraft.thePlayer.getEquipmentInSlot(1), 62, 10);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, Minecraft.thePlayer.getEquipmentInSlot(1), 62, 10);
            if (protectionenchantmentLevel1 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Pro\u00a72" + protectionenchantmentLevel1, 63.0, 10.0, -1);
            }
            if (unbreakingenchantmentLevel11 > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Unb\u00a72" + unbreakingenchantmentLevel11, 63.0, 5.0, -1);
            }
        }
        if (Minecraft.thePlayer.getEquipmentInSlot(0) != null) {
            RenderUtil.drawRect(83.0, 3.0, 103.0, 30.0, new Color(0, 0, 0, 150).getRGB());
            mc.getRenderItem().renderItemAndEffectIntoGUI(Minecraft.thePlayer.getEquipmentInSlot(0), 85, 10);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, Minecraft.thePlayer.getEquipmentInSlot(0), 85, 10);
            if (sharpnessenchantmentLevel > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Sha\u00a72" + sharpnessenchantmentLevel, 86.0, 10.0, -1);
            }
            if (fireAspectenchantmentLevel > 0) {
                Client.instance.FontLoaders.Comfortaa14.drawStringWithShadow("Fire\u00a72" + fireAspectenchantmentLevel, 86.0, 5.0, -1);
            }
        }
    }

    private void renderItem(int i, int x, int y, EntityPlayer player) {
        ItemStack itemstack = player.inventory.mainInventory[i];
        if (itemstack != null) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemstack, x, y);
            mc.getRenderItem().renderItemOverlays(Minecraft.fontRendererObj, itemstack, x - 1, y - 1);
        }
    }

    private boolean emptyInventory() {
        for (int i = 0; i < 45; ++i) {
            if (i < 9) continue;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            return false;
        }
        return true;
    }
}

