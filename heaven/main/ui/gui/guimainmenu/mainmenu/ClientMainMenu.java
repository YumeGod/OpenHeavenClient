/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.Display
 */
package heaven.main.ui.gui.guimainmenu.mainmenu;

import com.google.common.collect.Lists;
import heaven.heavenalpha.AAL;
import heaven.main.Client;
import heaven.main.ui.gui.alt.AltFileSaver;
import heaven.main.ui.gui.alt.GuiAltManager;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.guimainmenu.GuiGoodBye;
import heaven.main.ui.gui.guimainmenu.mainmenu.FlatButton;
import heaven.main.ui.gui.guimainmenu.mainmenu.FlatButtonNoBold;
import heaven.main.ui.gui.guimainmenu.mainmenu.ShaderButton;
import heaven.main.ui.gui.login.GuiLogin;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.shader.Shader;
import heaven.main.utils.shader.shaders.BackgroundShader;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

public class ClientMainMenu
extends GuiScreen {
    protected final List<GuiButton> guiMainButtonList = Lists.newArrayList();
    private boolean startAlpha;
    int alpha = 255;
    int byealpha;
    int textalpha;
    public static boolean useShader;
    boolean bye;
    float textY;
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    int impalpha;
    int impalpha2;

    private boolean titleCheck() {
        String[] info = new String[]{"M", "i", "n", "e", "c", "r", "a", "f", "t"};
        String indexCore = info[0] + info[1] + info[2] + info[3] + info[4] + info[5] + info[6] + info[7] + info[8];
        String[] numberInfo = new String[]{"1", ".", "8", ".", "9"};
        String numberIndexCore = numberInfo[0] + numberInfo[1] + numberInfo[2] + numberInfo[3] + numberInfo[4];
        return Display.getTitle().equals(indexCore + " " + numberIndexCore);
    }

    @Override
    public void initGui() {
        this.startAlpha = true;
        if (Client.instance.User.equals("User") || Client.instance.User.equals("12345") || Client.instance.User.equals("1234") || Client.instance.User.equals("null") || Client.instance.User.equals("Null")) {
            Minecraft.getMinecraft().shutdown();
        }
        if (Client.instance.User == null || Client.instance.User.isEmpty() || Client.instance.Users.isEmpty()) {
            Minecraft.getMinecraft().shutdown();
        } else if (Client.instance.User.equals("User") || Client.instance.User.equals("12345") || Client.instance.User.equals("1234") || Client.instance.User.equals("null") || Client.instance.User.equals("Null")) {
            Minecraft.getMinecraft().shutdown();
        }
        if (Client.instance.User == null || Client.instance.User.isEmpty()) {
            this.mc.shutdown();
        } else if (Client.instance.User.equals("User") || Client.instance.User.equals("12345") || Client.instance.User.equals("1234") || Client.instance.User.equals("null") || Client.instance.User.equals("Null")) {
            this.mc.shutdown();
        }
        AAL aal = new AAL();
        if (aal.clientIsLeakingOrInCracked) {
            Minecraft.getMinecraft().shutdown();
        }
        this.percent = 0.0f;
        this.lastPercent = 1.0f;
        this.percent2 = 0.0f;
        this.lastPercent2 = 1.0f;
        this.alpha = 255;
        this.guiMainButtonList.clear();
        this.textY = -15.0f;
        new ScaledResolution(this.mc);
        int h = ScaledResolution.getScaledHeight();
        new ScaledResolution(this.mc);
        int w = ScaledResolution.getScaledWidth();
        String singleplayer = "Single Player";
        String multiplayer = "Multi Player";
        this.guiMainButtonList.add(new FlatButton(0, w / 6, h / 2 + 12 - 56 - 1, 162, 16, "Single Player"));
        this.guiMainButtonList.add(new FlatButtonNoBold(1, w / 6, h / 2 + 12 + 20 - 56 + 20, 162, 16, "Multi Player"));
        this.guiMainButtonList.add(new ShaderButton(779, 8, h - 34, Client.instance.FontLoaders.bold16.getStringWidth("Mix:" + useShader), 20, "Mix:" + useShader));
        super.initGui();
    }

    public float smoothTrans(double current, double last) {
        int n;
        Minecraft.getMinecraft();
        if (Minecraft.getDebugFPS() == 0) {
            n = 1;
        } else {
            Minecraft.getMinecraft();
            n = Minecraft.getDebugFPS();
        }
        int FPS = n;
        return (float)(current + (last - current) / (double)(FPS / 10));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.GotAlpha();
        if (useShader) {
            ClientMainMenu.drawShaderBG();
        }
        this.percent = this.smoothTrans(this.percent, this.lastPercent);
        this.percent2 = this.smoothTrans(this.percent2, this.lastPercent2);
        if ((double)this.percent > 0.981) {
            GlStateManager.translate(width / 2, height / 2, 0.0f);
        } else {
            this.percent2 = this.smoothTrans(this.percent2, this.lastPercent2);
            GlStateManager.translate(width / 2, height / 2, 0.0f);
            GlStateManager.scale(this.percent2, this.percent2, 0.0f);
        }
        GlStateManager.translate(-ScaledResolution.getScaledWidth() / 2, -ScaledResolution.getScaledHeight() / 2, 0.0f);
        this.drawBackground(mouseX, mouseY);
        this.drawMainMenu();
        this.drawButton(mouseX, mouseY);
        if (this.startAlpha && this.alpha > 0) {
            this.bye = false;
            this.byealpha = 0;
            this.alpha = (int)AnimationUtil.moveUD(this.alpha, 0.0f, 0.2f, 0.15f);
        }
        if (this.bye) {
            if (Minecraft.theWorld == null) {
                this.byealpha = (int)AnimationUtil.moveUD(this.byealpha, 255.0f, 0.2f, 0.15f);
            }
        }
        if (this.bye && this.byealpha >= 251) {
            this.mc.displayGuiScreen(new GuiGoodBye());
        }
        SimpleRender.drawRect(0.0, 0.0, RenderUtil.width(), RenderUtil.height(), new Color(0, 0, 0, this.bye ? this.byealpha : this.alpha).getRGB());
    }

    public static ScaledResolution getres() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public static void drawShaderBG() {
        try {
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            BackgroundShader.BACKGROUND_SHADER.startShader();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
            worldRenderer.pos(0.0, ClientMainMenu.getres().getScaledHeight_double(), 0.0).endVertex();
            worldRenderer.pos(ClientMainMenu.getres().getScaledWidth_double(), ClientMainMenu.getres().getScaledHeight_double(), 0.0).endVertex();
            worldRenderer.pos(ClientMainMenu.getres().getScaledWidth_double(), 0.0, 0.0).endVertex();
            worldRenderer.pos(0.0, 0.0, 0.0).endVertex();
            tessellator.draw();
            Shader.stopShader();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawBackground(int mouseX, int mouseY) {
        if (!useShader) {
            RenderUtil.drawImage(new ResourceLocation("client/guimainmenu/mainmenu.png"), 0, 0, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
        }
        this.RightText(mouseX, mouseY);
        for (GuiButton guiButton : this.guiMainButtonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY);
        }
    }

    private void RightText(int mouseX, int mouseY) {
        boolean isOverSettings = mouseX > width - 100 && mouseX < width - 82 + 5 + Client.instance.FontLoaders.Comfortaa16.getStringWidth("Settings") && mouseY > 10 && mouseY < 22;
        boolean isOverAlt = mouseX > width - 180 && mouseX < width - 162 + 15 + Client.instance.FontLoaders.Comfortaa22.getStringWidth("AltManager") && mouseY > 10 && mouseY < 22;
        this.impalpha = (int)AnimationUtil.moveUD(this.impalpha, isOverSettings ? 229.0f : 178.0f, 0.2f, 0.15f);
        this.impalpha2 = (int)AnimationUtil.moveUD(this.impalpha2, isOverAlt ? 229.0f : 178.0f, 0.2f, 0.15f);
        Client.instance.FontLoaders.Comfortaa22.drawString("Settings", (double)(width - 82 + 5), 14.0, new Color(255, 255, 255, this.impalpha).getRGB());
        Client.instance.FontLoaders.Comfortaa22.drawString("AltManager", (double)(width - 162 + 15), 14.0, new Color(255, 255, 255, this.impalpha2).getRGB());
        RenderUtil.drawIcon(width - 27, 10.5f, 13, 13, new ResourceLocation("client/guimainmenu/close.png"));
    }

    public void drawButton(int mouseX, int mouseY) {
        for (GuiLabel guiLabel : this.labelList) {
            guiLabel.drawLabel(mouseX, mouseY);
        }
    }

    private void GotAlpha() {
        if (this.textalpha != 200) {
            this.textalpha = (int)AnimationUtil.moveUD(this.textalpha, 200.0f, 0.2f, 0.15f);
        }
    }

    public void drawMainMenu() {
        new ScaledResolution(this.mc);
        int h = ScaledResolution.getScaledHeight();
        new ScaledResolution(this.mc);
        int w = ScaledResolution.getScaledWidth();
        this.textY = AnimationUtil.moveUD(this.textY, ShaderButton.ioHover ? 25.0f : 0.0f, 0.11f, 0.1f);
        Client.instance.FontLoaders.bold16.drawString("Heaven " + Client.instance.version + " " + (Client.Beta && !Client.instance.User.isEmpty() ? "(BETA)" : "(Release)"), 8.0f, (float)(h - 12) - this.textY, this.TextColor());
        if (GuiLogin.checkDev) {
            Display.setTitle((String)(Client.instance.name + " | Dev"));
            String text2 = "Welcome," + Client.instance.User + " | Developer";
            Client.instance.FontLoaders.bold16.drawString(text2, w - Client.instance.FontLoaders.bold16.getStringWidth(text2) - 8, h - 12, this.TextColor());
        } else {
            String text2 = "Welcome," + Client.instance.User;
            Client.instance.FontLoaders.bold16.drawString(text2, w - Client.instance.FontLoaders.bold16.getStringWidth(text2) - 8, h - 12, this.TextColor());
        }
    }

    public int TextColor() {
        return new Color(233, 233, 233, this.textalpha).getRGB();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean isOverExit;
        boolean isOverSettings = mouseX > width - 100 && mouseX < width - 82 + Client.instance.FontLoaders.Comfortaa16.getStringWidth("Settings") && mouseY > 10 && mouseY < 22;
        boolean isOverAlt = mouseX > width - 180 && mouseX < width - 162 + Client.instance.FontLoaders.Comfortaa22.getStringWidth("AltManager") && mouseY > 10 && mouseY < 22;
        boolean bl = isOverExit = mouseX > width - 27 && mouseX < width - 27 + 13 && mouseY > 10 && mouseY < 24;
        if (mouseButton == 0 && isOverSettings) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
            AltFileSaver.saveAlts();
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && isOverAlt) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
            AltFileSaver.saveAlts();
            this.mc.displayGuiScreen(new GuiAltManager(this));
        }
        if (mouseButton == 0 && isOverExit) {
            Client.instance.saveConfig();
            this.bye = true;
        }
        if (mouseButton == 0) {
            for (GuiButton guibutton : this.guiMainButtonList) {
                if (!guibutton.mousePressed(this.mc, mouseX, mouseY)) continue;
                this.selectedButton = guibutton;
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformed(guibutton);
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 779) {
            useShader = !useShader;
            this.initGui();
        }
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        super.actionPerformed(button);
    }
}

