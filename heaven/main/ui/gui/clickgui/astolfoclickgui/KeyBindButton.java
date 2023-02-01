/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.astolfoclickgui;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.astolfoclickgui.ValueButton;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Window;
import heaven.main.ui.gui.clickgui.astolfoclickgui.asClickgui;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class KeyBindButton
extends ValueButton {
    public final Module cheat;
    public boolean bind;

    public KeyBindButton(Module cheat, int x, int y) {
        super(null, x, y);
        this.custom = true;
        this.bind = false;
        this.cheat = cheat;
    }

    @Override
    public void render(int mouseX, int mouseY, Limitation limitation, Window parent) {
        CFontRenderer mfont = Client.instance.FontLoaders.bold15;
        GL11.glEnable((int)3089);
        limitation.cut();
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, 0);
        Gui.drawRect(this.x - 10, this.y - 4, this.x + 80 + parent.x, this.y + 11, new Color(20, 20, 20).getRGB());
        mfont.drawString("Bind:", this.x - 7, this.y + 2, new Color(108, 108, 108).getRGB());
        mfont.drawString(Keyboard.getKeyName((int)this.cheat.getKey()), this.x + 77 - mfont.getStringWidth(Keyboard.getKeyName((int)this.cheat.getKey())) + parent.x, this.y + 2, new Color(108, 108, 108).getRGB());
        GL11.glDisable((int)3089);
    }

    @Override
    public void key(char typedChar, int keyCode) {
        if (this.bind) {
            this.cheat.setKey(keyCode);
            if (keyCode == 1) {
                this.cheat.setKey(0);
            }
            asClickgui.binding = false;
            this.bind = false;
        }
        super.key(typedChar, keyCode);
    }

    @Override
    public void click(int mouseX, int mouseY, int button, Window parent) {
        if (mouseX > this.x - 7 && mouseX < this.x + 85 + parent.allX && mouseY > this.y - 6 && mouseY < this.y + Client.instance.FontLoaders.Comfortaa18.getStringHeight() + 5 && button == 0) {
            asClickgui.binding = this.bind = !this.bind;
        }
        super.click(mouseX, mouseY, button, parent);
    }
}

