/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.oldFlux;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.ui.font.fontRenderer.UnicodeFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.oldFlux.ValueButton;
import heaven.main.ui.gui.clickgui.oldFlux.Window;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class Button {
    public Module cheat;
    public Window parent;
    public int x;
    public float y;
    public int index;
    public double opacity;
    public ArrayList<ValueButton> buttons = Lists.newArrayList();
    public boolean expand;
    float animationsize;
    public int arrow;
    boolean hover;

    public Button(Module cheat, int x2, int y2) {
        this.cheat = cheat;
        this.x = x2;
        this.y = y2;
        int y22 = y2 + 14;
        this.buttons.clear();
        for (Value v2 : this.cheat.getValues()) {
            if (!v2.isVisitable()) continue;
            this.buttons.add(new ValueButton(v2, this.x + 5, y22, this));
            y22 += 15;
        }
    }

    public void render(int mouseX, int mouseY, Limitation limitation) {
        float y2 = this.y + 14.0f;
        this.buttons.clear();
        for (Value v2 : this.cheat.getValues()) {
            if (!v2.isVisitable()) continue;
            this.buttons.add(new ValueButton(v2, this.x + 5, y2, this));
            y2 += 15.0f;
        }
        if (this.index != 0) {
            Button b22 = this.parent.buttons.get(this.index - 1);
            this.y = b22.y + 15.0f + this.animationsize;
            this.animationsize = b22.expand ? AnimationUtil.moveUD(this.animationsize, 15 * b22.buttons.size(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : AnimationUtil.moveUD(this.animationsize, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        int size = this.buttons.size();
        for (int i = 0; i < size; ++i) {
            this.buttons.get((int)i).y = this.y + 14.0f + (float)(15 * i);
            this.buttons.get((int)i).x = this.x + 5;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        UnicodeFontRenderer font = Client.instance.fontManager.arial17;
        if (this.parent.category.name().equals("Movement")) {
            SimpleRender.drawRect(this.x - 5 + 73, this.parent.y - 5 + 556 - (int)(this.parent.totalY + 22.0f - this.parent.translate.getY()), this.x + 85 - 18, (float)((double)(this.parent.y + font.getStringHeight()) + 3.8) - (float)((int)(this.parent.totalY + 22.0f - this.parent.translate.getY())) + 398.0f, new Color(233, 233, 233, 150).getRGB());
        }
        if (this.parent.category.name().equals("Combat")) {
            SimpleRender.drawRect(this.x - 5 + 73, this.parent.y - 5 + 408 - (int)(this.parent.totalY + 22.0f - this.parent.translate.getY()), this.x + 85 - 18, (float)((double)(this.parent.y + font.getStringHeight()) + 3.8) - (float)((int)(this.parent.totalY + 22.0f - this.parent.translate.getY() + 44.0f)) + 540.0f, new Color(233, 233, 233, 150).getRGB());
        }
        if (this.parent.category.name().equals("Player")) {
            SimpleRender.drawRect(this.x - 5 + 73, this.parent.y - 5 + 236 - (int)(this.parent.totalY + 22.0f - this.parent.translate.getY()), this.x + 85 - 18, (float)((double)(this.parent.y + font.getStringHeight()) + 3.8) - (float)((int)(this.parent.totalY + 22.0f - this.parent.translate.getY() + 44.0f)) + 470.0f, new Color(233, 233, 233, 150).getRGB());
        }
        if (this.parent.category.name().equals("Misc")) {
            SimpleRender.drawRect(this.x - 5 + 73, this.parent.y - 5 + 412 - (int)(this.parent.totalY + 22.0f - this.parent.translate.getY() + 106.0f), this.x + 85 - 18, (float)((double)(this.parent.y + font.getStringHeight()) + 3.8) - (float)((int)(this.parent.totalY + 22.0f - this.parent.translate.getY() + 64.0f)) + 236.0f, new Color(233, 233, 233, 150).getRGB());
        }
        if (this.parent.category.name().equals("Render")) {
            SimpleRender.drawRect(this.x - 5 + 73, this.parent.y - 5 + 1088 - (int)(this.parent.totalY + 22.0f - this.parent.translate.getY() + 191.0f), this.x + 85 - 18, (float)((double)(this.parent.y + font.getStringHeight()) + 3.8) - (float)((int)(this.parent.totalY + 22.0f - this.parent.translate.getY() - 312.0f)) + 294.0f, new Color(233, 233, 233, 150).getRGB());
        }
        limitation.cut();
        boolean bl = this.hover = mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)font.getStringHeight() + 4.0f;
        if (this.cheat.isEnabled()) {
            font.drawString(this.cheat.getName(), (float)(this.x + 10), this.y - 1.0f, this.parent.hudColor());
        } else {
            font.drawString(this.cheat.getName(), (float)(this.x + 10), this.y - 1.0f, -1);
        }
        limitation.cut();
        if (!this.expand && size >= 1) {
            Client.instance.FontLoaders.Comfortaa16.drawString(">", this.x, this.y + 2.0f, -1);
        } else if (size >= 1) {
            Client.instance.FontLoaders.Comfortaa16.drawString("v", this.x, this.y + 2.0f, -1);
        }
        if (this.expand) {
            this.buttons.forEach(b2 -> b2.render(mouseX, mouseY, limitation, this.parent));
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    public void click(int mouseX, int mouseY, int button) {
        if (this.parent.drag) {
            return;
        }
        if (mouseX > this.x - 7 && mouseX < this.x + 85 + this.parent.allX && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)Client.instance.FontLoaders.simp17.getStringHeight()) {
            if (button == 0) {
                this.cheat.setEnabledWithoutNotification(!this.cheat.isEnabled());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                boolean bl = this.expand = !this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b2 -> b2.click(mouseX, mouseY));
        }
    }

    public void setParent(Window parent) {
        this.parent = parent;
        for (int i2 = 0; i2 < this.parent.buttons.size(); ++i2) {
            if (this.parent.buttons.get(i2) != this) continue;
            this.index = i2;
            break;
        }
    }
}

