/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.oldFlux;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.oldFlux.Button;
import heaven.main.ui.gui.clickgui.oldFlux.Window;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class ValueButton {
    public Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public float y;
    public double opacity;
    private final Button parent;

    public ValueButton(Value value, int x, float y, Button parent) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        this.parent = parent;
        if (this.value instanceof Option) {
            this.change = (Boolean)this.value.getValue();
        } else if (this.value instanceof Mode) {
            this.name = String.valueOf(this.value.getValue());
        } else if (value instanceof Numbers) {
            Numbers v = (Numbers)value;
            this.name = this.name + v.getValue();
        }
        this.opacity = 0.0;
    }

    public void render(int mouseX, int mouseY, Limitation limitation, Window parent) {
        CFontRenderer mFont = Client.instance.FontLoaders.Comfortaa16;
        if (!this.custom) {
            double d = mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)mFont.getStringHeight() + 5.0f ? (this.opacity + 10.0 < 200.0 ? (this.opacity = this.opacity + 10.0) : 200.0) : (this.opacity - 6.0 > 0.0 ? (this.opacity = this.opacity - 6.0) : 0.0);
            this.opacity = d;
            Gui.drawRect((double)(this.x - 9), this.y - 4.0f, (double)(this.x - 9 + 88), this.y + 15.0f, new Color(255, 255, 255, (int)this.opacity).getRGB());
            if (this.change) {
                mFont.drawString(this.value.getName(), this.x - 5, this.y + 1.0f, parent.hudColor());
            }
            if (this.value instanceof Option) {
                this.change = (Boolean)this.value.getValue();
            } else if (this.value instanceof Mode) {
                this.name = String.valueOf(this.value.getValue());
            } else if (this.value instanceof Numbers) {
                Numbers v = (Numbers)this.value;
                this.name = String.valueOf(v.getValue());
                if (mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)mFont.getStringHeight() + 5.0f && Mouse.isButtonDown((int)0)) {
                    double min = ((Number)v.getMinimum()).doubleValue();
                    double max = ((Number)v.getMaximum()).doubleValue();
                    double inc = ((Number)v.getIncrement()).doubleValue();
                    double valAbs = (double)mouseX - ((double)this.x + 1.0);
                    double per = valAbs / 68.0;
                    per = Math.min(Math.max(0.0, per), 1.0);
                    double valRel = (max - min) * per;
                    double val = min + valRel;
                    val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    v.setValue(val);
                }
                double render = 68.0f * (((Number)v.getValue()).floatValue() - ((Number)v.getMinimum()).floatValue()) / (((Number)v.getMaximum()).floatValue() - ((Number)v.getMinimum()).floatValue());
                RenderUtil.circle((float)((double)this.x + render + 2.0) + 2.0f, this.y + 8.0f, 2.0f, new Color(240, 240, 240).getRGB());
                RenderUtil.drawRect(this.x - 5, this.y + (float)mFont.getStringHeight() + 2.0f, (float)((double)(this.x + 58) + 1.0), this.y + (float)mFont.getStringHeight() + 3.0f, new Color(191, 191, 191).getRGB());
                RenderUtil.drawRect(this.x - 5, this.y + (float)mFont.getStringHeight() + 2.0f, (float)((double)this.x + render + 1.0), this.y + (float)mFont.getStringHeight() + 3.0f, parent.hudColor());
            }
            Gui.drawRect(0.0, 0.0, 0.0, 0.0, 0);
            if (!this.change) {
                mFont.drawString(this.value.getName(), this.x - 5, this.y + 1.0f, -1);
            }
            if (this.value instanceof Mode) {
                Client.instance.FontLoaders.Comfortaa14.drawString(this.name, this.x + 80 - mFont.getStringWidth(this.name) + parent.allX, this.y + 2.0f, -1);
            } else {
                mFont.drawString(this.name, this.x + 76 - mFont.getStringWidth(this.name) + parent.allX, this.y + 1.0f, -1);
            }
        }
    }

    public void click(int mouseX, int mouseY) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)Client.instance.FontLoaders.Comfortaa16.getStringHeight() + 5.0f) {
            if (this.value instanceof Option) {
                Option m1;
                m1.setValue((Boolean)(m1 = (Option)this.value).getValue() == false);
                return;
            }
            if (this.value instanceof Mode) {
                int next;
                Mode m = (Mode)this.value;
                String current = (String)m.getValue();
                for (next = 0; next < m.getModes().length; ++next) {
                    if (!m.getModes()[next].equals(current)) continue;
                    ++next;
                    break;
                }
                this.value.setValue(m.getModes()[next == m.getModes().length ? 0 : next]);
            }
        }
    }
}

