/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package heaven.main.ui.gui.clickgui.listgui;

import com.google.common.collect.Lists;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.listgui.Window;
import heaven.main.ui.gui.guimainmenu.ColorCreator;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.TranslateUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class ClickyUI
extends GuiScreen {
    public static final ArrayList<Window> windows = Lists.newArrayList();
    public int scrollVelocity;
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public boolean close;
    int shadowanim;
    public TranslateUtil translate;

    public void init() {
        this.translate = new TranslateUtil(0.0f, 0.0f);
    }

    public ClickyUI() {
        if (windows.isEmpty()) {
            int x = 50;
            for (ModuleType c : ModuleType.values()) {
                windows.add(new Window(c, x, 5));
                x += 105;
            }
        }
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
        Gui.drawRect(0, 0, Display.getWidth(), Display.getHeight(), new Color(0, 0, 0, this.shadowanim).getRGB());
        this.shadowanim = !this.close ? (int)AnimationUtil.moveUD(this.shadowanim, 160.0f, SimpleRender.processFPS(1000.0f, 0.016f), SimpleRender.processFPS(1000.0f, 0.014f)) : (int)AnimationUtil.moveUD(this.shadowanim, 0.0f, SimpleRender.processFPS(1000.0f, 0.016f), SimpleRender.processFPS(1000.0f, 0.014f));
        this.percent = this.smoothTrans(this.percent, this.lastPercent);
        this.percent2 = this.smoothTrans(this.percent2, this.lastPercent2);
        if (!this.close) {
            if ((double)this.percent > 0.981) {
                GlStateManager.translate(width / 2, height / 2, 0.0f);
            } else {
                this.percent2 = this.smoothTrans(this.percent2, this.lastPercent2);
                GlStateManager.translate(width / 2, height / 2, 0.0f);
                GlStateManager.scale(this.percent2, this.percent2, 0.0f);
            }
        } else {
            GlStateManager.translate(width / 2, height / 2, 0.0f);
            GlStateManager.scale(this.percent, this.percent, this.percent);
        }
        GlStateManager.translate(-ScaledResolution.getScaledWidth() / 2, -ScaledResolution.getScaledHeight() / 2, 0.0f);
        if (this.close) {
            this.percent = this.smoothTrans(this.percent, -1.0);
        }
        if (this.percent <= 0.0f && this.shadowanim <= 30 && this.close) {
            this.mc.currentScreen = null;
            this.mc.mouseHelper.grabMouseCursor();
            this.mc.inGameHasFocus = true;
        }
        if (((Boolean)ClickGui.Streamer.getValue()).booleanValue()) {
            this.drawGradientRect(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), ColorCreator.createRainbowFromOffset(-6000, 5));
        }
        GlStateManager.pushMatrix();
        windows.forEach(w -> w.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 120 : 0);
        }
        windows.forEach(w -> w.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        windows.forEach(w -> w.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.percent = this.percent2;
            this.close = true;
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.percent = 0.0f;
        this.lastPercent = 1.0f;
        this.percent2 = 0.0f;
        this.lastPercent2 = 1.0f;
        this.shadowanim = 0;
        if (((Boolean)ClickGui.Blur.getValue()).booleanValue() && OpenGlHelper.shadersSupported) {
            if (Minecraft.thePlayer != null) {
                if (this.mc.entityRenderer.theShaderGroup != null) {
                    this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
                }
                this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
    }
}

