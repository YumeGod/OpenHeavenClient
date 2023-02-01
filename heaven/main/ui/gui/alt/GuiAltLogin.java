/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.ui.gui.alt;

import heaven.main.ui.gui.alt.Alt;
import heaven.main.ui.gui.alt.AltEnum;
import heaven.main.ui.gui.alt.AltFileSaver;
import heaven.main.ui.gui.alt.AltLoginThread;
import heaven.main.ui.gui.alt.Get_Microsoft_base;
import heaven.main.ui.gui.alt.GuiAltManager;
import heaven.main.ui.gui.alt.LoginOrAdd;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public class GuiAltLogin
extends GuiScreen
implements LoginOrAdd {
    private GuiTextField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    private GuiTextField combined;
    private final AltEnum altEnum;
    private String ssss;

    public GuiAltLogin(GuiScreen previousScreen, AltEnum altEnum) {
        this.previousScreen = previousScreen;
        this.altEnum = altEnum;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.onLogin();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                AltFileSaver.saveAlts();
                break;
            }
            case 2: {
                String data;
                try {
                    data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                }
                catch (Exception var4) {
                    return;
                }
                if (!data.contains(":")) break;
                String[] credentials = data.split(":");
                this.username.setText(credentials[0]);
                this.password.setText(credentials[1]);
                break;
            }
            case 1145: {
                this.username.setText(GuiAltLogin.getRandomString(7));
                this.password.setText("");
            }
            case 45: {
                Get_Microsoft_base.OpenExplorer.open_Microsoft_Url();
            }
        }
    }

    @Override
    public void onLogin() {
        switch (this.altEnum) {
            case LOGIN: {
                if (this.combined.getText().isEmpty()) {
                    this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                } else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
                    String data = this.combined.getText().split(":")[0];
                    String p = this.combined.getText().split(":")[1];
                    this.thread = new AltLoginThread(data.replaceAll(" ", ""), p.replaceAll(" ", ""));
                } else {
                    this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                }
                this.thread.start();
                break;
            }
            case ADD: {
                this.ssss = (Object)((Object)EnumChatFormatting.YELLOW) + "Adding....";
                new Thread(() -> {
                    String pa;
                    String a;
                    if (this.combined.getText().isEmpty()) {
                        a = this.username.getText();
                        pa = this.password.getText();
                    } else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
                        String data1 = this.combined.getText().split(":")[0];
                        String p = this.combined.getText().split(":")[1];
                        a = data1.replaceAll(" ", "");
                        pa = p.replaceAll(" ", "");
                    } else {
                        a = this.username.getText();
                        pa = this.password.getText();
                    }
                    if (pa.isEmpty()) {
                        GuiAltManager.getAlts().add(new Alt(a, "NULL_PASSWORD", a));
                        this.ssss = (Object)((Object)EnumChatFormatting.GREEN) + "Added successfully" + a;
                    } else {
                        Session s = AltLoginThread.createSession(a, pa);
                        if (s == null) {
                            this.ssss = (Object)((Object)EnumChatFormatting.RED) + "Add failed!";
                        } else {
                            GuiAltManager.getAlts().add(new Alt(a, pa, s.getProfile().getName()));
                            this.ssss = (Object)((Object)EnumChatFormatting.GREEN) + "Added successfully! " + s.getProfile().getName();
                        }
                    }
                }, "Add Alt Thread").start();
            }
        }
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        new ScaledResolution(this.mc);
        this.drawDefaultBackground();
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.combined.drawTextBox();
        Minecraft.fontRendererObj.drawCenteredString("Alt Login", width / 2, 20.0f, -1);
        if (this.altEnum == AltEnum.LOGIN) {
            Minecraft.fontRendererObj.drawCenteredString(this.thread == null ? "Waiting..." : this.thread.getStatus(), width / 2, 29.0f, -1);
        } else if (this.altEnum == AltEnum.ADD) {
            Minecraft.fontRendererObj.drawCenteredString(this.ssss == null ? "Waiting..." : this.ssss, width / 2, 29.0f, -1);
        }
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            Minecraft.fontRendererObj.drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66.0f, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            Minecraft.fontRendererObj.drawStringWithShadow("Password", width / 2 - 96, 106.0f, -7829368);
        }
        if (this.combined.getText().isEmpty() && !this.combined.isFocused()) {
            Minecraft.fontRendererObj.drawStringWithShadow("Email:Password", width / 2 - 96, 146.0f, -7829368);
        }
        super.drawScreen(x, y, z);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(45, width / 2 - 100, var3 + 72 + 12 + 48 + 24, "Microsoft Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiButton(1145, width / 2 - 100, var3 + 72 + 12 + 48, "Random User Name"));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 12 - 24, "Import user:pass"));
        this.username = new GuiTextField(1, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new GuiTextField(11, Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.combined = new GuiTextField(var3, Minecraft.fontRendererObj, width / 2 - 100, 140, 200, 20);
        this.username.setFocused(true);
        this.username.setMaxStringLength(200);
        this.password.setMaxStringLength(200);
        this.combined.setMaxStringLength(200);
        Keyboard.enableRepeatEvents((boolean)true);
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException var4) {
            var4.printStackTrace();
        }
        if (character == '\t' && (this.username.isFocused() || this.combined.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
            this.combined.setFocused(!this.combined.isFocused());
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
        this.combined.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
        this.combined.mouseClicked(x, y, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
        this.combined.updateCursorCounter();
    }
}

