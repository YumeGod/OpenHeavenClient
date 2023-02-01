/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package heaven.main.ui.gui.clickgui.novogui;

import com.google.common.collect.Lists;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.novogui.Window;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class ClickyUI
extends GuiScreen {
    public static ArrayList<Window> windows = Lists.newArrayList();
    public int scrollVelocity;
    public static boolean binding;

    public ClickyUI() {
        if (windows.isEmpty()) {
            int x = 5;
            for (ModuleType c : ModuleType.values()) {
                windows.add(new Window(c, x, 5));
                x += 107;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, Display.getWidth(), Display.getHeight(), new Color(0, 0, 0, 100).getRGB());
        GlStateManager.pushMatrix();
        windows.forEach(w2 -> w2.render(mouseX, mouseY));
        GlStateManager.popMatrix();
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            this.scrollVelocity = wheel < 0 ? -120 : (wheel > 0 ? 130 : 0);
        }
        windows.forEach(w2 -> w2.mouseScroll(mouseX, mouseY, this.scrollVelocity));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        windows.forEach(w2 -> w2.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 && !binding) {
            this.mc.displayGuiScreen(null);
            return;
        }
        windows.forEach(w2 -> w2.key(typedChar, keyCode));
    }
}

