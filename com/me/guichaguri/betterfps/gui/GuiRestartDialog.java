/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiRestartDialog
extends GuiScreen {
    private GuiScreen parent = null;
    private final String[] message = new String[]{"You need to restart your game to apply some changes", "Do you want restart now?"};

    public GuiRestartDialog(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, width / 2 - 205, height - 27, I18n.format("gui.yes", new Object[0])));
        this.buttonList.add(new GuiButton(2, width / 2 + 5, height - 27, I18n.format("gui.no", new Object[0])));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int centerX = width / 2;
        int centerY = height / 2;
        int i = 0;
        for (String msg : this.message) {
            GuiRestartDialog.drawCenteredString(this.fontRendererObj, msg, centerX, centerY - (this.message.length - i) * this.fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
            ++i;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 1) {
            this.mc.shutdown();
        } else if (button.id == 2) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
}

