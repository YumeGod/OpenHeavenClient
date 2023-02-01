/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import heaven.main.event.events.drakApi.LoadWorldEvent;
import heaven.main.management.EventManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;

public class GuiDownloadTerrain
extends GuiScreen {
    private final NetHandlerPlayClient netHandlerPlayClient;
    private int progress;
    private final CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    public GuiDownloadTerrain(NetHandlerPlayClient netHandler) {
        this.netHandlerPlayClient = netHandler;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
    }

    @Override
    public void initGui() {
        EventManager.call(new LoadWorldEvent());
        this.buttonList.clear();
    }

    @Override
    public void updateScreen() {
        ++this.progress;
        if (this.progress % 20 == 0) {
            this.netHandlerPlayClient.addToSendQueue(new C00PacketKeepAlive());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.customLoadingScreen != null) {
            this.customLoadingScreen.drawBackground(width, height);
        } else {
            this.drawBackground(0);
        }
        GuiDownloadTerrain.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), width / 2, height / 2 - 50, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

