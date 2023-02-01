/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.login;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.login.GuiLogin;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiFuck
extends GuiScreen {
    private static long startTime = 0L;
    int alpha = 255;
    private float currentX;
    private float currentY;
    static final double Anitext = -10.0;
    private boolean i;

    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis();
        }
        new ScaledResolution(this.mc);
        int h = ScaledResolution.getScaledHeight();
        new ScaledResolution(this.mc);
        int w = ScaledResolution.getScaledWidth();
        ScaledResolution sr = new ScaledResolution(this.mc);
        float xDiff = ((float)(mouseX - h / 2) - this.currentX) / (float)sr.getScaleFactor();
        float yDiff = ((float)(mouseY - w / 2) - this.currentY) / (float)sr.getScaleFactor();
        this.currentX += xDiff * 0.3f;
        this.currentY += yDiff * 0.3f;
        CFontRenderer fontwel2 = Client.instance.FontLoaders.Comfortaa24;
        CFontRenderer fontwel = Client.instance.FontLoaders.Comfortaa36;
        RenderUtil.drawImage(new ResourceLocation("client/guimainmenu/BG.mc"), -30, -30, ScaledResolution.getScaledWidth() + 60, ScaledResolution.getScaledHeight() + 60);
        fontwel.drawCenteredString("Your ID as bad,please again! ", (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f - 3.0f - -10.0f, new Color(255, 255, 255).getRGB());
        fontwel2.drawCenteredString(Client.instance.Users, (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f + (float)fontwel.getStringHeight() + 15.0f - -10.0f, new Color(255, 255, 255).getRGB());
        Gui.drawRect(0, 0, RenderUtil.width(), RenderUtil.height(), new Color(0, 0, 0, this.alpha).getRGB());
        if (this.i) {
            if (this.alpha < 255) {
                this.alpha += 15;
            }
            if (this.alpha >= 255) {
                this.mc.displayGuiScreen(new GuiLogin());
            }
        } else {
            if (this.alpha > 0 && startTime + 2000L <= System.currentTimeMillis()) {
                this.alpha -= 15;
            }
            if (this.alpha <= 0 && startTime + 10000L <= System.currentTimeMillis()) {
                this.i = true;
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.i = true;
    }
}

