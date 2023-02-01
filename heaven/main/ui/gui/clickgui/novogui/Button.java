/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.novogui;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.novogui.ValueButton;
import heaven.main.ui.gui.clickgui.novogui.Window;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class Button {
    public Module cheat;
    public Window parent;
    public int x;
    public float y;
    public int index;
    public int remander;
    public double opacity;
    public ArrayList<ValueButton> buttons = new ArrayList();
    public boolean expand;
    boolean hover;
    int smoothalpha;
    float animationsize;

    public Button(Module cheat, int x, int y) {
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 15;
        for (Value v : this.cheat.getValues()) {
            if (!v.isVisitable()) continue;
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15;
        }
    }

    public float processFPS(float fps, float defF, float defV) {
        return defV / (fps / defF);
    }

    public void render(int mouseX, int mouseY, Limitation limitation) {
        CFontRenderer font = Client.instance.FontLoaders.Comfortaa16;
        float y2 = this.y + 15.0f;
        this.buttons.clear();
        for (Value v : this.cheat.getValues()) {
            if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !v.isVisitable()) continue;
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15.0f;
        }
        if (this.index != 0) {
            int n;
            Minecraft.getMinecraft();
            if (Minecraft.getDebugFPS() == 0) {
                n = 1;
            } else {
                Minecraft.getMinecraft();
                n = Minecraft.getDebugFPS();
            }
            int FPS = n;
            Button b2 = this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15.0f + this.animationsize;
            if (b2.expand) {
                this.parent.buttonanim = true;
                this.animationsize = AnimationUtil.moveUD(this.animationsize, 15 * b2.buttons.size(), this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f));
            } else {
                this.parent.buttonanim = true;
                this.animationsize = AnimationUtil.moveUD(this.animationsize, 0.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f));
            }
        }
        if (this.parent.buttonanim) {
            this.parent.buttonanim = false;
        }
        int i = 0;
        float size = this.buttons.size();
        while ((float)i < size) {
            this.buttons.get((int)i).y = this.y + 17.0f + (float)(15 * i);
            this.buttons.get((int)i).x = this.x + 5;
            ++i;
        }
        this.smoothalphas();
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        limitation.cut();
        this.hover = mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)font.getStringHeight() + 4.0f;
        SimpleRender.drawRect(this.x - 5, this.y - 5.0f, this.x + 85 + this.parent.allX, this.y + (float)font.getStringHeight() + 5.0f, new Color(40, 40, 40).getRGB());
        SimpleRender.drawRect(this.x - 5, this.y - 5.0f - 1.0f, this.x + 85 + this.parent.allX, this.y + (float)font.getStringHeight() + 3.0f + 1.0f, this.hudcolorwithalpha());
        Client.instance.FontLoaders.simp17.drawStringWithShadow(this.cheat.getName(), this.x - 2, this.y - 1.0f, -1);
        Color Ranbow = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), this.smoothalpha), 70, 25);
        int n = ValueButton.valuebackcolor = (Boolean)HUD.Breathinglamp.get() != false ? Ranbow.getRGB() : new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
        if (!this.expand && size >= 1.0f) {
            Client.instance.FontLoaders.FLUXICON16.drawString("i", this.x + 75 + this.parent.allX, this.y + 1.0f, -1);
        } else if (size >= 1.0f) {
            Client.instance.FontLoaders.FLUXICON16.drawString("h", this.x + 75 + this.parent.allX, this.y + 1.0f, -1);
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.render(mouseX, mouseY, this.parent));
        }
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    private int hudcolorwithalpha() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), this.smoothalpha).getRGB();
    }

    private void smoothalphas() {
        int n;
        Minecraft.getMinecraft();
        if (Minecraft.getDebugFPS() == 0) {
            n = 1;
        } else {
            Minecraft.getMinecraft();
            n = Minecraft.getDebugFPS();
        }
        int FPS = n;
        this.smoothalpha = this.cheat.isEnabled() ? (int)AnimationUtil.moveUD(this.smoothalpha, 255.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f)) : (int)AnimationUtil.moveUD(this.smoothalpha, 0.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f));
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b -> b.key(typedChar, keyCode));
    }

    public void click(int mouseX, int mouseY, int button) {
        if (this.parent.drag) {
            return;
        }
        if (mouseX > this.x - 7 && mouseX < this.x + 85 + this.parent.allX && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)Client.instance.FontLoaders.simp17.getStringHeight()) {
            if (button == 0) {
                this.cheat.setEnabled(!this.cheat.isEnabled());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
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

