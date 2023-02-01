/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.guimainmenu;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.login.GuiLogin;
import heaven.main.utils.timer.TimerUtil;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class GuiGoodBye
extends GuiScreen {
    private long startTime;
    int alpha = 0;
    final TimerUtil timer = new TimerUtil();
    final String[] bye2 = new String[]{"See you next time", "Good Bye", "Look forward to your next back"};
    final String[] bye;
    final Random r;
    final String bye2r;
    final String byer;
    private float currentX;
    private float currentY;
    final int textalpha;
    final double Anitext;

    public GuiGoodBye() {
        this.bye = new String[]{Client.instance.User};
        this.r = new Random();
        this.bye2r = this.bye2[this.r.nextInt(this.bye2.length)];
        this.byer = this.bye[this.r.nextInt(this.bye.length)];
        this.textalpha = 255;
        this.Anitext = 0.0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        new ScaledResolution(this.mc);
        int h = ScaledResolution.getScaledHeight();
        new ScaledResolution(this.mc);
        int w = ScaledResolution.getScaledWidth();
        float xDiff = ((float)(mouseX - h / 2) - this.currentX) / (float)sr.getScaleFactor();
        float yDiff = ((float)(mouseY - w / 2) - this.currentY) / (float)sr.getScaleFactor();
        this.currentX += xDiff * 0.3f;
        this.currentY += yDiff * 0.3f;
        if (this.startTime == 0L) {
            this.startTime = System.currentTimeMillis();
        }
        CFontRenderer font = Client.instance.FontLoaders.Comfortaa18;
        CFontRenderer font2 = Client.instance.FontLoaders.Comfortaa34;
        RenderUtil.drawImage(new ResourceLocation("client/guimainmenu/BG.mc"), 0, 0, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
        font.drawCenteredStringWithShadow(this.bye2r, (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f - 3.0f - (float)this.Anitext, new Color(255, 255, 255).getRGB());
        if (GuiLogin.checkDev) {
            font2.drawCenteredStringWithShadow(this.byer + " | Developer", (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f + (float)font2.getStringHeight() - (float)this.Anitext, new Color(255, 255, 255).getRGB());
        } else {
            font2.drawCenteredStringWithShadow(this.byer, (float)ScaledResolution.getScaledWidth() / 2.0f, (float)ScaledResolution.getScaledHeight() / 2.0f + (float)font2.getStringHeight() - (float)this.Anitext, new Color(255, 255, 255).getRGB());
        }
        Gui.drawRect(-100, -100, ScaledResolution.getScaledWidth() + 100, ScaledResolution.getScaledHeight() + 100, new Color(0, 0, 0, this.alpha).getRGB());
        if (this.startTime + 2000L <= System.currentTimeMillis()) {
            if (this.alpha != 255) {
                ++this.alpha;
            }
            if (this.alpha != 255) {
                ++this.alpha;
            }
            if (this.alpha != 255) {
                ++this.alpha;
            }
        }
        if (this.alpha >= 255 && this.startTime + 3000L <= System.currentTimeMillis()) {
            this.mc.shutdown();
        }
    }
}

