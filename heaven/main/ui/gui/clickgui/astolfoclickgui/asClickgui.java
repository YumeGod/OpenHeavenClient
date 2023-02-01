/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package heaven.main.ui.gui.clickgui.astolfoclickgui;

import com.google.common.collect.Lists;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Window;
import heaven.main.ui.gui.guimainmenu.ColorCreator;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class asClickgui
extends GuiScreen {
    public static final ArrayList<Window> windows = Lists.newArrayList();
    public int scrollVelocity;
    public static boolean binding;

    public asClickgui() {
        if (windows.isEmpty()) {
            int x = 5;
            for (ModuleType c : ModuleType.values()) {
                windows.add(new Window(c, x, 5));
                x += 115;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (((Boolean)ClickGui.Streamer.getValue()).booleanValue()) {
            this.drawGradientRect(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), ColorCreator.createRainbowFromOffset(-6000, 5));
        }
        Gui.drawRect(0, 0, Display.getWidth(), Display.getHeight(), new Color(0, 0, 0, 150).getRGB());
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
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 && !binding) {
            this.mc.displayGuiScreen(null);
            return;
        }
        windows.forEach(w -> w.key(typedChar, keyCode));
    }

    @Override
    public void initGui() {
        if (((Boolean)ClickGui.Blur.getValue()).booleanValue() && OpenGlHelper.shadersSupported) {
            if (Minecraft.thePlayer != null) {
                if (this.mc.entityRenderer.theShaderGroup != null) {
                    this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
                }
                this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
    }

    @Override
    public void onGuiClosed() {
        this.mc.entityRenderer.stopUseShader();
        this.mc.entityRenderer.isShaderActive();
    }
}

