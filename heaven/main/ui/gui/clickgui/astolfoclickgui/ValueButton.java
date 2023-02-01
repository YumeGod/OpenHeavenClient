/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.astolfoclickgui;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Window;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ValueButton {
    public final Value value;
    public String name;
    public boolean custom;
    public int x;
    public int y;
    int staticColor;

    public ValueButton(Value value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof Mode) {
            this.name = String.valueOf(this.value.getValue());
        } else if (value instanceof Numbers) {
            Numbers v = (Numbers)value;
            this.name = this.name + (v.isInteger() ? (double)((Number)v.getValue()).intValue() : ((Number)v.getValue()).doubleValue());
        }
    }

    public void render(int mouseX, int mouseY, Limitation limitation, Window parent) {
        CFontRenderer mfont = Client.instance.FontLoaders.bold15;
        if (!this.custom) {
            Object v;
            if (this.value instanceof Mode) {
                this.name = String.valueOf(this.value.getValue());
            } else if (this.value instanceof Numbers) {
                v = (Numbers)this.value;
                this.name = String.valueOf(((Numbers)v).isInteger() ? (double)((Number)((Value)v).getValue()).intValue() : ((Number)((Value)v).getValue()).doubleValue());
                if (mouseX > this.x - 7 && mouseX < this.x + 85 + parent.allX && mouseY > this.y + Client.instance.FontLoaders.bold15.getStringHeight() - 10 && mouseY < this.y + mfont.getStringHeight() + 2 && Mouse.isButtonDown((int)0)) {
                    double min = ((Number)((Numbers)v).getMinimum()).doubleValue();
                    double max = ((Number)((Numbers)v).getMaximum()).doubleValue();
                    double inc = ((Number)((Numbers)v).getIncrement()).doubleValue();
                    double valAbs = (double)mouseX - ((double)this.x + 1.0);
                    double perc = valAbs / 68.0;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    double valRel = (max - min) * perc;
                    double val = min + valRel;
                    val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    ((Value)v).setValue(val);
                }
            }
            switch (parent.category.name()) {
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
            GL11.glEnable((int)3089);
            limitation.cut();
            Gui.drawRect(this.x - 10, this.y - 4, this.x + 80 + parent.allX, this.y + 11, new Color(20, 20, 20).getRGB());
            if (this.value instanceof Option) {
                mfont.drawString(this.value.getName(), this.x - 7, this.y + 2, (Boolean)this.value.getValue() != false ? new Color(255, 255, 255).getRGB() : new Color(108, 108, 108).getRGB());
            }
            if (this.value instanceof Mode) {
                mfont.drawString(this.value.getName(), this.x - 7, this.y + 3, new Color(255, 255, 255).getRGB());
                mfont.drawString(this.name, this.x + 77 + parent.allX - mfont.getStringWidth(this.name), this.y + 3, new Color(182, 182, 182).getRGB());
            }
            if (this.value instanceof Numbers) {
                v = (Numbers)this.value;
                double render = (82.0f + (float)parent.allX) * AnimationUtil.getAnimationState((((Number)((Value)v).getValue()).floatValue() - ((Number)((Numbers)v).getMinimum()).floatValue()) / (((Number)((Numbers)v).getMaximum()).floatValue() - ((Number)((Numbers)v).getMinimum()).floatValue()), 0.0f, 0.1f);
                Gui.drawRect((double)(this.x - 8), (double)(this.y + mfont.getStringHeight() + 2), (float)((double)(this.x - 4) + render), (double)(this.y + mfont.getStringHeight() - 9), this.staticColor);
                mfont.drawString(this.value.getName(), this.x - 7, this.y, new Color(255, 255, 255).getRGB());
                mfont.drawString(this.name, this.x + mfont.getStringWidth(this.value.getName()), this.y, -1);
            }
            GL11.glDisable((int)3089);
        }
    }

    public void key(char typedChar, int keyCode) {
    }

    public void click(int mouseX, int mouseY, int button, Window parent) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && mouseY > this.y - 6 && mouseY < this.y + Client.instance.FontLoaders.bold15.getStringHeight()) {
            if (this.value instanceof Option) {
                Option v;
                v.setValue((Boolean)(v = (Option)this.value).getValue() == false);
                return;
            }
            if (this.value instanceof Mode) {
                Mode m = (Mode)this.value;
                if (button == 0 || button == 1) {
                    List<String> options = Arrays.asList(m.getModes());
                    int index = options.indexOf(m.getValue());
                    index = button == 0 ? ++index : --index;
                    if (index >= options.size()) {
                        index = 0;
                    } else if (index < 0) {
                        index = options.size() - 1;
                    }
                    m.setValue(m.getModes()[index]);
                }
            }
        }
    }
}

