/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.alt;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.alt.Alt;
import heaven.main.ui.gui.alt.GuiAltManager;
import heaven.main.ui.gui.alt.RightClickMenu;
import heaven.main.ui.gui.clickgui.CSGOClickGui;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.login.AltLoginThread;
import java.awt.Color;
import org.lwjgl.input.Mouse;

public class AltManagerRightClickMenu
extends RightClickMenu {
    private final Alt alt;
    private boolean leftClickDown;

    public AltManagerRightClickMenu(int x, int y, Alt alt) {
        super(x, y);
        this.alt = alt;
    }

    @Override
    public void onOpen() {
        this.buttons.add(new RightClickMenu.RightClickMenuButton("\u767b\u5f55", () -> {
            GuiAltManager.displayingMenu = null;
            String cachePassword = this.alt.getPassword().equals("NULL_PASSWORD") ? "" : this.alt.getPassword();
            GuiAltManager.altLoginThread = new AltLoginThread(this.alt.getUserName(), cachePassword);
            GuiAltManager.altLoginThread.start();
        }));
        this.buttons.add(new RightClickMenu.RightClickMenuButton("\u5220\u9664", () -> {
            try {
                GuiAltManager.getAlts().remove(this.alt);
                GuiAltManager.selectedAlt = null;
                GuiAltManager.displayingMenu = null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }));
        this.buttons.add(new RightClickMenu.RightClickMenuButton("\u53d6\u6d88", () -> {
            GuiAltManager.displayingMenu = null;
        }));
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RenderUtil.resetColor();
        RenderUtil.drawRect(this.x, this.y, this.x + 100, (float)(this.y + 25) + (float)this.buttons.size() * 10.0f, -1);
        if (!CSGOClickGui.isHovered(this.x, this.y, this.x + 100, (float)(this.y + 25) + (float)this.buttons.size() * 10.0f, mouseX, mouseY) && (Mouse.isButtonDown((int)0) || Mouse.isButtonDown((int)1))) {
            GuiAltManager.displayingMenu = null;
        }
        this.alt.loadHead();
        RenderUtil.drawImage(this.alt.getHead(), this.x + 1, this.y + 1, 15, 15);
        Client.instance.FontLoaders.Comfortaa16.drawString(this.alt.getUserName(), this.x + 20, this.y + 3, new Color(0, 0, 0).getRGB());
        float buttonY = this.y + 20;
        for (RightClickMenu.RightClickMenuButton button : this.buttons) {
            Client.instance.FontLoaders.Comfortaa16.drawString(button.getButtonText(), this.x + 2, (int)buttonY, new Color(0, 0, 0).getRGB());
            CFontRenderer cfr_ignored_0 = Client.instance.FontLoaders.Comfortaa16;
            if (CSGOClickGui.isHovered(this.x, buttonY, this.x + 100, buttonY + 0.0f, mouseX, mouseY)) {
                CFontRenderer cfr_ignored_1 = Client.instance.FontLoaders.Comfortaa16;
                RenderUtil.drawRect(this.x, buttonY, this.x + 100, buttonY + 0.0f, new Color(0, 0, 0, 50).getRGB());
                if (Mouse.isButtonDown((int)0) && !this.leftClickDown) {
                    button.onPress();
                    this.leftClickDown = true;
                }
            }
            buttonY += 12.0f;
        }
        if (!Mouse.isButtonDown((int)0)) {
            this.leftClickDown = false;
        }
    }
}

