/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IProgressUpdate;
import net.optifine.CustomLoadingScreen;
import net.optifine.CustomLoadingScreens;

public class GuiScreenWorking
extends GuiScreen
implements IProgressUpdate {
    private String field_146591_a = "";
    private String field_146589_f = "";
    private int progress;
    private boolean doneWorking;
    private final CustomLoadingScreen customLoadingScreen = CustomLoadingScreens.getCustomLoadingScreen();

    @Override
    public void displaySavingString(String message) {
        this.resetProgressAndMessage(message);
    }

    @Override
    public void resetProgressAndMessage(String message) {
        this.field_146591_a = message;
        this.displayLoadingString("Working...");
    }

    @Override
    public void displayLoadingString(String message) {
        this.field_146589_f = message;
        this.progress = 0;
    }

    @Override
    public void setLoadingProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void setDoneWorking() {
        this.doneWorking = true;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        block3: {
            if (this.doneWorking) break block3;
            if (this.customLoadingScreen == null) ** GOTO lbl-1000
            if (Minecraft.theWorld == null) {
                this.customLoadingScreen.drawBackground(GuiScreenWorking.width, GuiScreenWorking.height);
            } else lbl-1000:
            // 2 sources

            {
                this.drawDefaultBackground();
            }
            if (this.progress > 0) {
                GuiScreenWorking.drawCenteredString(this.fontRendererObj, this.field_146591_a, GuiScreenWorking.width / 2, 70, 0xFFFFFF);
                GuiScreenWorking.drawCenteredString(this.fontRendererObj, this.field_146589_f + " " + this.progress + "%", GuiScreenWorking.width / 2, 90, 0xFFFFFF);
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}

