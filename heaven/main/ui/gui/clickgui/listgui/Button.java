/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.listgui;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.listgui.ValueButton;
import heaven.main.ui.gui.clickgui.listgui.Window;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class Button {
    public final Module cheat;
    public Window parent;
    public int x;
    public float y;
    public int index;
    public final ArrayList<ValueButton> buttons = new ArrayList();
    public boolean expand;
    boolean hover;
    float x2;
    int smoothalpha;
    float animationsize;
    public int remander;

    public Button(Module cheat, int x, int y, int buttonAlpha) {
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 15;
        this.smoothalpha = buttonAlpha;
        for (Value v : this.cheat.getValues()) {
            if (!v.isVisitable()) continue;
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15;
        }
    }

    public void render(int mouseX, int mouseY, Limitation limitation) {
        CFontRenderer font = Client.instance.FontLoaders.Comfortaa18;
        float y2 = this.y + 15.0f;
        this.buttons.clear();
        for (Value v : this.cheat.getValues()) {
            if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !v.isVisitable()) continue;
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15.0f;
        }
        if (this.index != 0) {
            Button b2 = this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15.0f + this.animationsize;
            this.animationsize = b2.expand ? AnimationUtil.moveUD(this.animationsize, 15 * b2.buttons.size(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : AnimationUtil.moveUD(this.animationsize, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        int size = this.buttons.size();
        for (int i = 0; i < size; ++i) {
            this.buttons.get((int)i).y = this.y + 14.0f + (float)(15 * i);
            this.buttons.get((int)i).x = this.x + 5;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        limitation.cut();
        this.ModFontAnim();
        this.smoothalphas();
        if (!this.cheat.isEnabled()) {
            SimpleRender.drawRect(this.x - 5, this.y - 5.0f, this.x + 85, this.y + (float)font.getStringHeight() + 3.8f, new Color(40, 40, 40).getRGB());
        }
        SimpleRender.drawRect(this.x - 5, this.y - 5.0f, this.x + 85, this.y + (float)font.getStringHeight() + 3.8f, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), this.smoothalpha).getRGB());
        this.hover = mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)font.getStringHeight() + 4.0f;
        this.ModFontAnim();
        Client.instance.FontLoaders.regular15.drawString(this.cheat.getName(), (float)this.x + this.x2, this.y, new Color(255, 255, 255).getRGB());
        if (!this.expand && size >= 1) {
            Client.instance.FontLoaders.FLUXICON16.drawString("h", this.x + 73, this.y + 1.0f, new Color(255, 255, 255).getRGB());
        } else if (size >= 1) {
            Client.instance.FontLoaders.FLUXICON16.drawString("i", this.x + 73, this.y + 1.0f, new Color(255, 255, 255).getRGB());
        }
        if (this.expand) {
            this.buttons.forEach(component -> component.render(this.parent, mouseX, mouseY, limitation));
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    private void smoothalphas() {
        this.smoothalpha = this.cheat.isEnabled() ? (int)AnimationUtil.moveUD(this.smoothalpha, 255.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : (int)AnimationUtil.moveUD(this.smoothalpha, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
    }

    private void ModFontAnim() {
        this.x2 = this.hover ? AnimationUtil.moveUD(this.x2, 5.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : AnimationUtil.moveUD(this.x2, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
    }

    public void click(int mouseX, int mouseY, int button) {
        if (this.parent.drag) {
            return;
        }
        if (this.hover) {
            if (button == 0) {
                this.cheat.setEnabled(!this.cheat.isEnabled());
            }
            if (button == 1 && !this.buttons.isEmpty() && this.parent.expand >= (float)(this.parent.height / 2)) {
                boolean bl = this.expand = !this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.click(mouseX, mouseY, button));
        }
    }

    public void setParent(Window parent) {
        this.parent = parent;
        for (int i = 0; i < this.parent.buttons.size(); ++i) {
            if (this.parent.buttons.get(i) != this) continue;
            this.index = i;
            this.remander = this.parent.buttons.size() - i;
            break;
        }
    }
}

