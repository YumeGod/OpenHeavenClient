/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.login.Alt;
import heaven.main.ui.gui.login.AltLoginThread;
import heaven.main.ui.gui.login.AltManager;
import heaven.main.ui.gui.login.GuiAltManager;
import heaven.main.ui.simplecore.SimpleRender;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected
extends GuiScreen {
    private final String reason;
    private final IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private boolean isTrueRec;
    private String watchdogString = "";

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(997, 5, 8, 98, 20, "AltManager"));
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
        this.buttonList.add(new GuiButton(7723, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 22, "Reconnect"));
        this.buttonList.add(new GuiButton(7724, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 44, "Random Alt and Reconnect"));
        this.buttonList.add(new GuiButton(7725, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 66, "Random Name and Reconnect"));
        this.watchdogString = "";
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 997) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }
        if (button.id == 7723) {
            try {
                GuiMultiplayer gui = (GuiMultiplayer)this.parentScreen;
                gui.connectToSelected();
            }
            catch (Exception gui) {
                // empty catch block
            }
        }
        if (button.id == 7724) {
            Alt randomAlt = AltManager.alts.get(new Random().nextInt(AltManager.alts.size()));
            String user1 = randomAlt.getUsername();
            String pass1 = randomAlt.getPassword();
            GuiAltManager.loginThread = new AltLoginThread(user1, pass1);
            GuiAltManager.loginThread.start();
            try {
                GuiMultiplayer gui = (GuiMultiplayer)this.parentScreen;
                gui.connectToSelected();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (button.id == 7725) {
            try {
                String var0 = SimpleRender.abcdefg();
                AltLoginThread thread = new AltLoginThread(var0, "");
                thread.start();
                GuiMultiplayer gui = (GuiMultiplayer)this.parentScreen;
                gui.connectToSelected();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GuiDisconnected.drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.field_175353_i / 2 - (this.fontRendererObj.FONT_HEIGHT << 1), 0xAAAAAA);
        int i = height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            for (String s : this.multilineMessage) {
                GuiDisconnected.drawCenteredString(this.fontRendererObj, s, width / 2, i, 0xFFFFFF);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        if (!this.watchdogString.isEmpty()) {
            GuiDisconnected.drawCenteredString(this.fontRendererObj, "[LanderHekoer]Ban" + this.watchdogString, width / 2, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT + 88 + 30, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

