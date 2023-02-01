/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.astolfoclickgui;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.astolfoclickgui.KeyBindButton;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.astolfoclickgui.ValueButton;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Window;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Button {
    public final Module cheat;
    public Window parent;
    public int x;
    public int y;
    public int arrow;
    public int index;
    public int remander;
    public double opacity;
    public final ArrayList<ValueButton> buttons = Lists.newArrayList();
    public boolean expand;
    int staticColor;

    public Button(Module cheat, int x, int y) {
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 15;
        for (Value v : cheat.getValues()) {
            if (!v.isVisitable()) continue;
            this.buttons.add(new ValueButton(v, x + 5, y2));
            y2 += 15;
        }
        this.buttons.add(new KeyBindButton(cheat, x + 5, y2 - 5));
    }

    public void render(int mouseX, int mouseY, Limitation limitation) {
        CFontRenderer font = Client.instance.FontLoaders.Comfortaa17;
        CFontRenderer mfont = Client.instance.FontLoaders.bold15;
        int y2 = this.y + 15;
        this.buttons.clear();
        for (Value v : this.cheat.getValues()) {
            if (!v.isVisitable()) continue;
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15;
        }
        if (this.index != 0) {
            Button b2 = this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15 + (b2.expand ? 15 * b2.buttons.size() : 0);
        }
        for (int i = 0; i < this.buttons.size(); ++i) {
            this.buttons.get((int)i).y = this.y + 14 + 15 * i;
            this.buttons.get((int)i).x = this.x + 5;
        }
        switch (this.parent.category.name()) {
            case "Combat": {
                this.staticColor = new Color(231, 76, 60).getRGB();
                break;
            }
            case "Render": {
                this.staticColor = new Color(54, 1, 205).getRGB();
                break;
            }
            case "Movement": {
                this.staticColor = new Color(45, 203, 113).getRGB();
                break;
            }
            case "Player": {
                this.staticColor = new Color(141, 68, 173).getRGB();
                break;
            }
            case "World": {
                this.staticColor = new Color(38, 154, 255).getRGB();
                break;
            }
            case "Misc": {
                this.staticColor = new Color(102, 101, 101).getRGB();
            }
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        Limitation.doGlScissor(this.x - 5, this.y - 5, 90 + this.parent.allX, font.getStringHeight() + 5);
        limitation.cut();
        Gui.drawRect(this.x - 5, this.y - 5, this.x + 85 + this.parent.allX, this.y + 5 + font.getStringHeight(), new Color(39, 39, 39).getRGB());
        if (this.cheat.isEnabled()) {
            limitation.cut();
            Gui.drawRect(this.x - 4, this.y - 5, this.x + 84 + this.parent.allX, this.y + 10, this.staticColor);
        }
        limitation.cut();
        mfont.drawString(this.cheat.getName().toLowerCase(), this.x + 81 + this.parent.allX - mfont.getStringWidth(this.cheat.getName().toLowerCase()), this.y, new Color(220, 220, 220).getRGB());
        if (mouseX > this.x - 7 && mouseX < this.x + 85 + this.parent.allX && mouseY > this.y - 6 && mouseY < this.y + mfont.getStringHeight()) {
            Gui.drawRect(this.x - 4, this.y - 5, this.x + 84 + this.parent.allX, this.y + 10, new Color(233, 233, 233, 30).getRGB());
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        if (this.expand) {
            this.buttons.forEach(component -> component.render(mouseX, mouseY, limitation, this.parent));
        }
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b -> b.key(typedChar, keyCode));
    }

    public void click(int mouseX, int mouseY, int button) {
        if (this.parent.drag) {
            return;
        }
        if (mouseX > this.x - 7 && mouseX < this.x + 85 + this.parent.allX && mouseY > this.y - 6 && mouseY < this.y + Client.instance.FontLoaders.bold16.getStringHeight()) {
            if (button == 0) {
                this.cheat.setEnabled(!this.cheat.isEnabled());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                boolean bl = this.expand = !this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.click(mouseX, mouseY, button, this.parent));
        }
    }

    public void setParent(Window parent) {
        this.parent = parent;
        for (int i2 = 0; i2 < this.parent.buttons.size(); ++i2) {
            if (this.parent.buttons.get(i2) != this) continue;
            this.index = i2;
            this.remander = this.parent.buttons.size() - i2;
            break;
        }
    }
}

