/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import heaven.main.Client;
import heaven.main.ui.gui.guimainmenu.mainmenu.ClientMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;

public class GuiIngameMenu
extends GuiScreen {
    @Override
    public void initGui() {
        this.buttonList.clear();
        int i = -16;
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));
        if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton)this.buttonList.get((int)0)).displayString = I18n.format("menu.disconnect", new Object[0]);
        }
        this.buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + i, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
        GuiButton guibutton = new GuiButton(7, width / 2 + 2, height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan", new Object[0]));
        this.buttonList.add(guibutton);
        this.buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + i, 98, 20, I18n.format("gui.stats", new Object[0])));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 1: {
                boolean flag = this.mc.isIntegratedServerRunning();
                button.enabled = false;
                Minecraft.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                if (flag) {
                    this.mc.displayGuiScreen(new ClientMainMenu());
                    break;
                }
                this.mc.displayGuiScreen(new GuiMultiplayer(new ClientMainMenu()));
            }
            default: {
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            }
            case 5: {
                this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
                break;
            }
            case 7: {
                this.mc.displayGuiScreen(new GuiShareToLan(this));
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Client.instance.saveConfig();
        this.drawDefaultBackground();
        GuiIngameMenu.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), width / 2, 40, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

