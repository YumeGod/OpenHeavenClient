/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.login;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.guimainmenu.mainmenu.ClientMainMenu;
import heaven.main.ui.gui.login.GuiLogin;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiWelcome
extends GuiScreen {
    private static long startTime = 0L;
    private int alpha = 255;
    private static final double Anitext = 10.0;
    private boolean i;
    private final String bye2r;

    private void ShowSystemNotification(String title, String text, TrayIcon.MessageType type, Long delay) throws AWTException {
        if (SystemTray.isSupported()) {
            TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("icon.png"), title);
            trayIcon.setImageAutoSize(true);
            SystemTray.getSystemTray().add(trayIcon);
            trayIcon.displayMessage(title, text, type);
            new Thread(() -> {
                try {
                    Thread.sleep(delay);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                SystemTray.getSystemTray().remove(trayIcon);
            }).start();
        }
    }

    @Override
    public void initGui() {
        new GuiLogin().Logins();
        if (Client.instance.Users.equals("ho3")) {
            Minecraft.getMinecraft().shutdown();
        }
        try {
            if (GuiLogin.checkDev) {
                this.ShowSystemNotification("Welcome to " + Client.instance.name, "Logged in as " + Client.instance.Users + " developer", TrayIcon.MessageType.INFO, 3000L);
            } else {
                this.ShowSystemNotification("Welcome to " + Client.instance.name, "Logged in as " + Client.instance.Users, TrayIcon.MessageType.INFO, 3000L);
            }
        }
        catch (AWTException aWTException) {
            // empty catch block
        }
    }

    public GuiWelcome() {
        String[] bye2 = new String[]{"Welcome to ", "Hello, use ", "Hey bro, Nice "};
        Random r = new Random();
        this.bye2r = bye2[r.nextInt(bye2.length)];
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis();
        }
        CFontRenderer fontwel2 = Client.instance.FontLoaders.regular24;
        CFontRenderer fontwel = Client.instance.FontLoaders.regular36;
        RenderUtil.drawImage(new ResourceLocation("client/guimainmenu/BG.mc"), -30, -30, ScaledResolution.getScaledWidth() + 60, ScaledResolution.getScaledHeight() + 60);
        fontwel.drawCenteredString(this.bye2r + Client.instance.name, (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f - 3.0f - 10.0f, new Color(255, 255, 255).getRGB());
        if (GuiLogin.checkDev) {
            fontwel2.drawCenteredString(Client.instance.Users + " | Developer", (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f + (float)fontwel.getStringHeight() + 15.0f - 10.0f, new Color(255, 255, 255).getRGB());
        } else {
            fontwel2.drawCenteredString(Client.instance.Users, (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f + (float)fontwel.getStringHeight() + 15.0f - 10.0f, new Color(255, 255, 255).getRGB());
        }
        Gui.drawRect(0, 0, RenderUtil.width(), RenderUtil.height(), new Color(0, 0, 0, this.alpha).getRGB());
        if (this.i) {
            if (this.alpha < 255) {
                this.alpha += 15;
            }
            if (this.alpha >= 255) {
                this.mc.displayGuiScreen(new ClientMainMenu());
            }
        } else {
            if (this.alpha > 0 && startTime + 1000L <= System.currentTimeMillis()) {
                this.alpha -= 15;
            }
            if (this.alpha <= 0 && startTime + 5000L <= System.currentTimeMillis()) {
                this.i = true;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.i = true;
    }
}

