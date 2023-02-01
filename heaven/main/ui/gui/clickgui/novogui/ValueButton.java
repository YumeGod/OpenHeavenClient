/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.novogui;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.clickgui.novogui.Window;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class ValueButton {
    public final Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public float y;
    public static int valuebackcolor;

    public ValueButton(Value value, int x, float y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof Option) {
            this.change = (Boolean)this.value.getValue();
        } else if (this.value instanceof Mode) {
            this.name = String.valueOf(this.value.getValue());
        } else if (value instanceof Numbers) {
            Numbers v = (Numbers)value;
            this.name = this.name + (v.isInteger() ? (double)((Number)v.getValue()).intValue() : ((Number)v.getValue()).doubleValue());
        }
    }

    public void render(int mouseX, int mouseY, Window parent) {
        CFontRenderer font = Client.instance.FontLoaders.simp16;
        Gui.drawRect((double)(this.x - 10), this.y - 7.0f, (double)(this.x + 80 + parent.allX), this.y + 11.0f, new Color(40, 40, 40).getRGB());
        if (this.value instanceof Option) {
            this.change = (Boolean)this.value.getValue();
        } else if (this.value instanceof Mode) {
            this.name = String.valueOf(this.value.getValue()).toUpperCase();
        } else if (this.value instanceof Numbers) {
            Numbers v = (Numbers)this.value;
            this.name = String.valueOf(((Number)v.getValue()).doubleValue());
            if (mouseX > this.x - 9 && mouseX < this.x + 87 && (float)mouseY > this.y - 4.0f) {
                if ((float)mouseY < this.y + 0.0f + 4.0f && Mouse.isButtonDown((int)0)) {
                    double min = ((Number)v.getMinimum()).doubleValue();
                    double max = ((Number)v.getMaximum()).doubleValue();
                    double inc = ((Number)v.getIncrement()).doubleValue();
                    double valAbs = mouseX - (this.x + 1);
                    double perc = valAbs / 68.0;
                    perc = Math.min(Math.max(0.0, perc), 1.0);
                    double valRel = (max - min) * perc;
                    double val = min + valRel;
                    val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                    v.setValue(val);
                }
            }
            double number = 86.0f * (((Number)v.getValue()).floatValue() - ((Number)v.getMinimum()).floatValue()) / (((Number)v.getMaximum()).floatValue() - ((Number)v.getMinimum()).floatValue());
            GlStateManager.pushMatrix();
            GlStateManager.translate(-9.0f, 1.0f, 0.0f);
            Gui.drawRect((double)(this.x + 1), this.y - 6.0f, (float)this.x + 87.0f + (float)parent.allX, this.y + 0.0f + 6.0f, new Color(29, 29, 29).getRGB());
            Gui.drawRect((double)(this.x + 1), this.y - 6.0f, (double)this.x + number + 1.0 + (double)parent.allX, this.y + 0.0f + 6.0f, valuebackcolor);
            GlStateManager.popMatrix();
        }
        if (this.value instanceof Option) {
            int size = 2;
            if (this.change) {
                RenderUtil.drawRect(this.x + 62 + 2 + parent.allX + 4, this.y - 4.0f + 2.0f - 1.0f, this.x + 76 - 2 + parent.allX + 4, this.y + 9.0f - 2.0f, new Color(29, 29, 29).getRGB());
                Client.instance.FontLoaders.novoicons18.drawString("H", (float)this.x + 64.5f + (float)parent.allX + 4.0f, this.y + 1.0f, valuebackcolor);
            } else {
                RenderUtil.drawRect(this.x + 62 + 2 + parent.allX + 4, this.y - 4.0f + 2.0f - 1.0f, this.x + 76 - 2 + parent.allX + 4, this.y + 9.0f - 2.0f, new Color(29, 29, 29).getRGB());
            }
        }
        if (!(this.value instanceof Numbers)) {
            font.drawString(this.value.getName(), this.x - 7, this.y, -1);
        }
        if (this.value instanceof Option) {
            font.drawString(this.name, this.x + font.getStringWidth(this.value.getName()), this.y, -1);
        }
        if (this.value instanceof Numbers) {
            font.drawString(this.value.getName(), this.x - 7, this.y - 1.0f, -1);
            font.drawString(this.name, this.x + font.getStringWidth(this.value.getName()), this.y - 1.0f, -1);
        }
        if (this.value instanceof Mode) {
            font.drawString(this.name, this.x + 90 - font.getStringWidth(this.name), this.y, -1);
        }
    }

    public void key(char typedChar, int keyCode) {
    }

    public void click(int mouseX, int mouseY, int button) {
        if (!this.custom && mouseX > this.x - 9 && mouseX < this.x + 87 && (float)mouseY > this.y - 4.0f) {
            CFontRenderer cfr_ignored_0 = Client.instance.FontLoaders.Comfortaa18;
            if ((float)mouseY < this.y + 0.0f + 4.0f) {
                if (this.value instanceof Option) {
                    Option m1;
                    m1.setValue((Boolean)(m1 = (Option)this.value).getValue() == false);
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
}

