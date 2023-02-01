/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.oldFlux;

import com.google.common.collect.Lists;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.oldFlux.Window;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class ClickUIOld
extends GuiScreen {
    public static ArrayList<Window> windows = Lists.newArrayList();
    public double opacity;
    public static int alpha = 255;
    public int scrollVelocity;
    public static boolean binding;

    public ClickUIOld() {
        this.allowUserInput = true;
        if (windows.isEmpty()) {
            int x2 = 5;
            for (ModuleType c2 : ModuleType.values()) {
                windows.add(new Window(c2, x2, 5, this));
                x2 += 95;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.opacity = this.opacity + 10.0 < 200.0 ? (this.opacity = this.opacity + 10.0) : 200.0;
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
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        windows.forEach(w2 -> w2.click(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}

