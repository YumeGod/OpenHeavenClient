/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.achievement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;

public class GuiAchievement
extends Gui {
    private static final ResourceLocation achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    private final Minecraft mc;
    private int width;
    private int height;
    private String achievementTitle;
    private String achievementDescription;
    private Achievement theAchievement;
    private long notificationTime;
    private final RenderItem renderItem;
    private boolean permanentNotification;

    public GuiAchievement(Minecraft mc) {
        this.mc = mc;
        this.renderItem = mc.getRenderItem();
    }

    public void displayAchievement(Achievement ach) {
        this.achievementTitle = I18n.format("achievement.get", new Object[0]);
        this.achievementDescription = ach.getStatName().getUnformattedText();
        this.notificationTime = Minecraft.getSystemTime();
        this.theAchievement = ach;
        this.permanentNotification = false;
    }

    public void displayUnformattedAchievement(Achievement achievementIn) {
        this.achievementTitle = achievementIn.getStatName().getUnformattedText();
        this.achievementDescription = achievementIn.getDescription();
        this.notificationTime = Minecraft.getSystemTime() + 2500L;
        this.theAchievement = achievementIn;
        this.permanentNotification = true;
    }

    private void updateAchievementWindowScale() {
        GlStateManager.viewport(0, 0, Minecraft.displayWidth, Minecraft.displayHeight);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        this.width = Minecraft.displayWidth;
        this.height = Minecraft.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, this.width, this.height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
    }

    public void updateAchievementWindow() {
        if (this.theAchievement != null && this.notificationTime != 0L) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer != null) {
                double d0 = (double)(Minecraft.getSystemTime() - this.notificationTime) / 3000.0;
                if (!this.permanentNotification) {
                    if (d0 < 0.0 || d0 > 1.0) {
                        this.notificationTime = 0L;
                        return;
                    }
                } else if (d0 > 0.5) {
                    d0 = 0.5;
                }
                this.updateAchievementWindowScale();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                double d1 = d0 * 2.0;
                if (d1 > 1.0) {
                    d1 = 2.0 - d1;
                }
                d1 *= 4.0;
                if ((d1 = 1.0 - d1) < 0.0) {
                    d1 = 0.0;
                }
                d1 *= d1;
                d1 *= d1;
                int i = this.width - 160;
                int j = -((int)(d1 * 36.0));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableTexture2D();
                GuiAchievement guiAchievement = this;
                guiAchievement.mc.getTextureManager().bindTexture(achievementBg);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(i, j, 96, 202, 160, 32);
                if (this.permanentNotification) {
                    Minecraft.fontRendererObj.drawSplitString(this.achievementDescription, i + 30, j + 7, 120, -1);
                } else {
                    Minecraft.fontRendererObj.drawString(this.achievementTitle, i + 30, j + 7, -256);
                    Minecraft.fontRendererObj.drawString(this.achievementDescription, i + 30, j + 18, -1);
                }
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableColorMaterial();
                GlStateManager.enableLighting();
                this.renderItem.renderItemAndEffectIntoGUI(this.theAchievement.theItemStack, i + 8, j + 8);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
            }
        }
    }

    public void clearAchievements() {
        this.theAchievement = null;
        this.notificationTime = 0L;
    }
}

