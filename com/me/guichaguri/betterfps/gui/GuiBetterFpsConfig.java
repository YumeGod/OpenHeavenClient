/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.me.guichaguri.betterfps.gui;

import com.me.guichaguri.betterfps.BetterFpsConfig;
import com.me.guichaguri.betterfps.BetterFpsHelper;
import com.me.guichaguri.betterfps.gui.GuiCycleButton;
import com.me.guichaguri.betterfps.gui.GuiRestartDialog;
import com.me.guichaguri.betterfps.tweaker.BetterFpsTweaker;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Util;
import org.lwjgl.input.Mouse;

public class GuiBetterFpsConfig
extends GuiScreen {
    private GuiScreen parent = null;

    public GuiBetterFpsConfig() {
    }

    public GuiBetterFpsConfig(GuiScreen parent) {
        this.parent = parent;
    }

    private List<GuiButton> initButtons() {
        ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();
        BetterFpsConfig config = BetterFpsConfig.getConfig();
        buttons.add(new AlgorithmButton(2, "Algorithm", BetterFpsHelper.displayHelpers, config.algorithm, new String[]{"The algorithm of sine & cosine methods", "\u00a7cRequires restarting to take effect", "", "\u00a7eShift-click com.me to test algorithms \u00a77(This will take a few seconds)", "", "\u00a7aMore information soon"}));
        buttons.add(new GuiCycleButton.GuiBooleanButton(3, "Update Checker", config.updateChecker, new String[]{"Whether will check for updates on startup", "It's highly recommended enabling this option", "", "Default: On"}));
        buttons.add(new GuiCycleButton.GuiBooleanButton(4, "Preallocate Memory", config.preallocateMemory, new String[]{"Whether will preallocate 10MB on startup.", "\u00a7cRequires restarting to take effect", "", "Default in Vanilla: On", "Default in BetterFps: Off", "", "Note: This allocation will only be cleaned once the memory is almost full"}));
        buttons.add(new GuiCycleButton.GuiBooleanButton(5, "Fast Box Render", config.fastBoxRender, new String[]{"Whether will only render the exterior of boxes.", "\u00a7cRequires restarting to take effect", "", "Default in Vanilla: Off", "Default in BetterFps: On"}));
        buttons.add(new GuiCycleButton.GuiBooleanButton(6, "Fog", config.fog, new String[]{"Whether will render the fog.", "\u00a7cRequires restarting to take effect", "", "Default: On"}));
        buttons.add(new GuiCycleButton.GuiBooleanButton(7, "Fast Hopper", config.fastHopper, new String[]{"Whether will improve the hopper.", "\u00a7cRequires restarting to take effect", "", "Default in Vanilla: Off", "Default in BetterFps: On"}));
        buttons.add(new GuiCycleButton.GuiBooleanButton(8, "Fast Beacon", config.fastBeacon, new String[]{"Whether will improve the beacon.", "\u00a7cRequires restarting to take effect", "", "Default in Vanilla: Off", "Default in BetterFps: On"}));
        return buttons;
    }

    @Override
    public void initGui() {
        int x1 = width / 2 - 155;
        int x2 = width / 2 + 5;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(-1, x1, height - 27, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(-2, x2, height - 27, 150, 20, I18n.format("gui.cancel", new Object[0])));
        List<GuiButton> buttons = this.initButtons();
        int y = 25;
        int lastId = 0;
        for (GuiButton button : buttons) {
            boolean first = button.id % 2 != 0;
            boolean large = button.id - 1 != lastId;
            button.xPosition = first || large ? x1 : x2;
            button.yPosition = y;
            button.width = large ? 310 : 150;
            button.height = 20;
            this.buttonList.add(button);
            if (!first || large) {
                y += 25;
            }
            lastId = button.id;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (mouseY < this.fontRendererObj.FONT_HEIGHT + 14) {
            if (Mouse.isButtonDown((int)1)) {
                GuiBetterFpsConfig.drawCenteredString(this.fontRendererObj, "This is not a button", width / 2, 7, 0xC0C0C0);
            } else {
                GuiBetterFpsConfig.drawCenteredString(this.fontRendererObj, "Hold right-click on a button for information", width / 2, 7, 0xC0C0C0);
            }
        } else {
            GuiBetterFpsConfig.drawCenteredString(this.fontRendererObj, "BetterFps Options", width / 2, 7, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (Mouse.isButtonDown((int)1)) {
            for (GuiButton button : this.buttonList) {
                if (!(button instanceof GuiCycleButton) || !button.isMouseOver()) continue;
                int y = mouseY + 5;
                String[] help = ((GuiCycleButton)button).getHelpText();
                int fontHeight = this.fontRendererObj.FONT_HEIGHT;
                int i = 0;
                this.drawGradientRect(0, y, Minecraft.displayWidth, y + fontHeight * help.length + 10, -1072689136, -804253680);
                for (String h : help) {
                    if (!h.isEmpty()) {
                        this.fontRendererObj.drawString(h, 5, y + i * fontHeight + 5, 0xFFFFFF);
                    }
                    ++i;
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button instanceof GuiCycleButton) {
            ((GuiCycleButton)button).actionPerformed();
        } else if (button.id == -1) {
            boolean restart = false;
            BetterFpsConfig config = BetterFpsConfig.getConfig();
            GuiCycleButton algorithmButton = this.getCycleButton(2);
            String algorithm = (String)algorithmButton.getSelectedValue();
            if (!algorithm.equals(config.algorithm)) {
                restart = true;
            }
            config.algorithm = algorithm;
            GuiCycleButton updateButton = this.getCycleButton(3);
            config.updateChecker = (Boolean)updateButton.getSelectedValue();
            GuiCycleButton preallocateButton = this.getCycleButton(4);
            boolean preallocate = (Boolean)preallocateButton.getSelectedValue();
            if (preallocate != config.preallocateMemory) {
                restart = true;
            }
            config.preallocateMemory = preallocate;
            GuiCycleButton boxRenderButton = this.getCycleButton(5);
            boolean boxRender = (Boolean)boxRenderButton.getSelectedValue();
            if (boxRender != config.fastBoxRender) {
                restart = true;
            }
            config.fastBoxRender = boxRender;
            GuiCycleButton fogButton = this.getCycleButton(6);
            boolean fog = (Boolean)fogButton.getSelectedValue();
            if (fog != config.fog) {
                restart = true;
            }
            config.fog = fog;
            GuiCycleButton hopperButton = this.getCycleButton(7);
            boolean fastHopper = (Boolean)hopperButton.getSelectedValue();
            if (fastHopper != config.fastHopper) {
                restart = true;
            }
            config.fastHopper = fastHopper;
            GuiCycleButton beaconButton = this.getCycleButton(8);
            boolean fastBeacon = (Boolean)beaconButton.getSelectedValue();
            if (fastBeacon != config.fastBeacon) {
                restart = true;
            }
            config.fastBeacon = fastBeacon;
            BetterFpsHelper.saveConfig();
            this.mc.displayGuiScreen(restart ? new GuiRestartDialog(this.parent) : this.parent);
        } else if (button.id == -2) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    private GuiCycleButton getCycleButton(int id) {
        for (GuiButton button : this.buttonList) {
            if (button.id != id) continue;
            return (GuiCycleButton)button;
        }
        return null;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    private static class AlgorithmButton
    extends GuiCycleButton {
        Process process = null;

        public <T> AlgorithmButton(int buttonId, String title, HashMap<T, String> values, T defaultValue, String[] helpLines) {
            super(buttonId, title, values, defaultValue, helpLines);
        }

        private String getJavaDir() {
            String separator = System.getProperty("file.separator");
            String path = System.getProperty("java.home") + separator + "bin" + separator;
            if (Util.getOSType() == Util.EnumOS.WINDOWS && new File(path + "javaw.exe").isFile()) {
                return path + "javaw.exe";
            }
            return path + "java";
        }

        private boolean isRunning() {
            try {
                this.process.exitValue();
                return false;
            }
            catch (Exception ex) {
                return true;
            }
        }

        private void updateAlgorithm() {
            if (this.process != null && !this.isRunning()) {
                try {
                    String line;
                    BufferedReader in = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
                    block2: while ((line = in.readLine()) != null) {
                        if (!BetterFpsHelper.helpers.containsKey(line)) continue;
                        for (int i = 0; i < this.keys.size(); ++i) {
                            if (!this.keys.get(i).equals(line)) continue;
                            this.key = i;
                            this.updateTitle();
                            continue block2;
                        }
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                this.process = null;
            }
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            this.updateAlgorithm();
            super.drawButton(mc, mouseX, mouseY);
        }

        @Override
        public boolean shiftClick() {
            if (this.process != null && this.isRunning()) {
                return true;
            }
            ArrayList<String> args = new ArrayList<String>();
            args.add(this.getJavaDir());
            args.add("-Dtester=" + Minecraft.getMinecraft().mcDataDir.getAbsolutePath());
            args.add("-cp");
            args.add(BetterFpsTweaker.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            args.add("com.me.guichaguri.betterfps.installer.BetterFpsInstaller");
            try {
                this.process = new ProcessBuilder(args).start();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
    }
}

