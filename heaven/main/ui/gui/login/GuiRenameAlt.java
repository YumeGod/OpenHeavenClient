/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.login;

import heaven.main.ui.gui.login.GuiAltManager;
import heaven.main.ui.gui.login.GuiPasswordField;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiRenameAlt
extends GuiScreen {
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private String status = "\u00a7eWaiting...";
    private GuiPasswordField pwField;

    public GuiRenameAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 0: {
                this.manager.selectedAlt.setMask(this.nameField.getText());
                if (!this.pwField.getText().isEmpty()) {
                    this.manager.selectedAlt.setPassword(this.pwField.getText());
                }
                this.status = "\u00a7aEdited!";
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        Minecraft.fontRendererObj.drawCenteredString("Edit Alt", (float)width / 2.0f, 10.0f, -1);
        Minecraft.fontRendererObj.drawCenteredString(this.status, (float)width / 2.0f, 20.0f, -1);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty()) {
            Minecraft.fontRendererObj.drawStringWithShadow("New E-Mail", (float)width / 2.0f - 96.0f, 66.0f, -7829368);
        }
        if (this.pwField.getText().isEmpty()) {
            Minecraft.fontRendererObj.drawStringWithShadow("New Password", (float)width / 2.0f - 96.0f, 106.0f, -7829368);
        }
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Finish"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.eventButton, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.pwField = new GuiPasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.nameField.setText(this.manager.selectedAlt.getUsername());
        if (!this.manager.selectedAlt.getUsername().isEmpty()) {
            this.pwField.setText(this.manager.selectedAlt.getPassword());
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.pwField.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.nameField.mouseClicked(par1, par2, par3);
        this.pwField.mouseClicked(par1, par2, par3);
    }
}

