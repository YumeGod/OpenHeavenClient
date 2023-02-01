/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.login;

import heaven.main.management.FileManager;
import heaven.main.ui.gui.login.Alt;
import heaven.main.ui.gui.login.AltLoginThread;
import heaven.main.ui.gui.login.AltManager;
import heaven.main.ui.gui.login.GuiAddAlt;
import heaven.main.ui.gui.login.GuiAltLogin;
import heaven.main.ui.gui.login.GuiRenameAlt;
import heaven.main.utils.render.RenderUtil;
import java.io.IOException;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager
extends GuiScreen {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    public static AltLoginThread loginThread;
    private int offset;
    public Alt selectedAlt;
    private String status = "\u00a7eWaiting...";

    public GuiAltManager() {
        FileManager.saveAlts();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (loginThread == null) {
                    mc.displayGuiScreen(null);
                    break;
                }
                if (!loginThread.getStatus().equals("Logging in...") && !loginThread.getStatus().equals("Do not hit back! Logging in...")) {
                    mc.displayGuiScreen(null);
                    break;
                }
                loginThread.setStatus("Do not hit back! Logging in...");
                break;
            }
            case 1: {
                String user = this.selectedAlt.getUsername();
                String pass = this.selectedAlt.getPassword();
                loginThread = new AltLoginThread(user, pass);
                loginThread.start();
                break;
            }
            case 2: {
                if (loginThread != null) {
                    loginThread = null;
                }
                AltManager.getAlts().remove(this.selectedAlt);
                this.status = "\u00a7cRemoved.";
                this.selectedAlt = null;
                FileManager.saveAlts();
                break;
            }
            case 3: {
                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                Alt randomAlt = AltManager.alts.get(new Random().nextInt(AltManager.alts.size()));
                String user1 = randomAlt.getUsername();
                String pass1 = randomAlt.getPassword();
                loginThread = new AltLoginThread(user1, pass1);
                loginThread.start();
                break;
            }
            case 6: {
                mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            }
            case 7: {
                Alt lastAlt = AltManager.lastAlt;
                if (lastAlt == null) {
                    if (loginThread == null) {
                        this.status = "?cThere is no last used alt!";
                        break;
                    }
                    loginThread.setStatus("?cThere is no last used alt!");
                    break;
                }
                String user2 = lastAlt.getUsername();
                String pass2 = lastAlt.getPassword();
                loginThread = new AltLoginThread(user2, pass2);
                loginThread.start();
                break;
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        this.drawDefaultBackground();
        Minecraft.fontRendererObj.drawStringWithShadow(GuiAltManager.mc.session.getUsername(), 10.0f, 10.0f, -7829368);
        Minecraft.fontRendererObj.drawCenteredString("Account Manager - " + AltManager.getAlts().size() + " alts", (float)width / 2.0f, 10.0f, -1);
        Minecraft.fontRendererObj.drawCenteredString(loginThread == null ? this.status : loginThread.getStatus(), (float)width / 2.0f, 20.0f, -1);
        GL11.glPushMatrix();
        GuiAltManager.prepareScissorBox(0.0f, 33.0f, width, height - 50);
        GL11.glEnable((int)3089);
        int y = 38;
        for (Alt alt : AltManager.getAlts()) {
            if (!this.isAltInArea(y)) continue;
            String name = alt.getMask().isEmpty() ? alt.getUsername() : alt.getMask();
            StringBuilder pass = new StringBuilder();
            if (alt.getPassword().isEmpty()) {
                pass = new StringBuilder("\u00a7cCracked");
            } else {
                for (char ignored : alt.getPassword().toCharArray()) {
                    pass.append("*");
                }
            }
            if (alt == this.selectedAlt) {
                if (GuiAltManager.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown((int)0)) {
                    RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f, -16777216, -2142943931);
                } else if (GuiAltManager.isMouseOverAlt(par1, par2, y - this.offset)) {
                    RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f, -16777216, -2142088622);
                } else {
                    RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f, -16777216, -2144259791);
                }
            } else if (GuiAltManager.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown((int)0)) {
                RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f, -16777216, -2146101995);
            } else if (GuiAltManager.isMouseOverAlt(par1, par2, y - this.offset)) {
                RenderUtil.drawBorderedRect(52.0f, y - this.offset - 4, width - 52, y - this.offset + 20, 1.0f, -16777216, -2145180893);
            }
            Minecraft.fontRendererObj.drawCenteredString(name, (float)width / 2.0f, y - this.offset, -1);
            Minecraft.fontRendererObj.drawCenteredString(pass.toString(), (float)width / 2.0f, y - this.offset + 10, 0x555555);
            y += 26;
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
            this.rename.enabled = false;
        } else {
            this.login.enabled = true;
            this.remove.enabled = true;
            this.rename.enabled = true;
        }
        if (Keyboard.isKeyDown((int)200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        } else if (Keyboard.isKeyDown((int)208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 + 4 + 76, height - 24, 75, 20, "Back"));
        this.login = new GuiButton(1, width / 2 - 154, height - 48, 70, 20, "Login");
        this.buttonList.add(this.login);
        this.remove = new GuiButton(2, width / 2 - 74, height - 24, 70, 20, "Remove");
        this.buttonList.add(this.remove);
        this.buttonList.add(new GuiButton(3, width / 2 + 4 + 76, height - 48, 75, 20, "Add"));
        this.buttonList.add(new GuiButton(4, width / 2 - 74, height - 48, 70, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(5, width / 2 + 4, height - 48, 70, 20, "Random"));
        this.rename = new GuiButton(6, width / 2 + 4, height - 24, 70, 20, "Edit");
        this.buttonList.add(this.rename);
        this.buttonList.add(new GuiButton(7, width / 2 - 154, height - 24, 70, 20, "Last Alt"));
        this.login.enabled = false;
        this.remove.enabled = false;
    }

    private boolean isAltInArea(int y) {
        return y - this.offset <= height - 50;
    }

    private static boolean isMouseOverAlt(int x, int y, int y1) {
        return x >= 52 && y >= y1 - 4 && x <= width - 52 && y <= y1 + 20 && y >= 33 && x <= width && y <= height - 50;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y = 38 - this.offset;
        for (Alt alt : AltManager.getAlts()) {
            if (GuiAltManager.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed((GuiButton)this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt;
            }
            y += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        int factor = new ScaledResolution(mc).getScaleFactor();
        new ScaledResolution(mc);
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)ScaledResolution.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }
}

