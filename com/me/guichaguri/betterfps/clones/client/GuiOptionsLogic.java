/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.clones.client;

import com.me.guichaguri.betterfps.gui.GuiBetterFpsConfig;
import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionsLogic
extends GuiOptions {
    @CopyMode(value=CopyMode.Mode.IGNORE)
    public GuiOptionsLogic(GuiScreen screen, GameSettings settings) {
        super(screen, settings);
    }

    @Override
    @CopyMode(value=CopyMode.Mode.APPEND)
    public void initGui() {
        int x_BF = width / 2 + 5;
        int y_BF = height / 6 + 24 - 8;
        int width_BF = 150;
        if (this.hasButtonInCoords_BF(x_BF, y_BF, 0, 2)) {
            x_BF = width / 2 - 155;
            if (this.hasButtonInCoords_BF(x_BF, y_BF, 0, 2)) {
                GuiButton language = null;
                for (GuiButton b : this.buttonList) {
                    if (b.id != 102) continue;
                    language = b;
                    break;
                }
                if (language == null) {
                    x_BF = 0;
                    y_BF = 0;
                    width_BF = 100;
                } else {
                    this.buttonList.remove(language);
                    this.buttonList.add(language);
                    x_BF = language.xPosition + language.width;
                    y_BF = language.yPosition;
                    width_BF -= language.width;
                }
            }
        }
        this.buttonList.add(new GuiButton(72109, x_BF, y_BF, width_BF, 20, "BetterFps Options"));
    }

    private boolean hasButtonInCoords_BF(int x, int y, int xRadius, int yRadius) {
        for (GuiButton b : this.buttonList) {
            if (b.xPosition > x + xRadius || b.yPosition > y + yRadius || b.xPosition < x - xRadius || b.yPosition < y - yRadius) continue;
            return true;
        }
        return false;
    }

    @Override
    @CopyMode(value=CopyMode.Mode.APPEND)
    protected void actionPerformed(GuiButton button) {
        if (button.id == 72109) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiBetterFpsConfig(this));
        }
    }
}

