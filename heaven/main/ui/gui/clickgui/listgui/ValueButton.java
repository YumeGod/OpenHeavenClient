/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.listgui;

import heaven.main.Client;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.listgui.Window;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import org.lwjgl.input.Mouse;

public class ValueButton {
    public final Value value;
    public String name;
    public final boolean custom;
    public boolean change;
    public int x;
    public float y;

    public ValueButton(Value value, int x, float y) {
        this.custom = false;
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

    public void render(Window windows, int mouseX, int mouseY, Limitation limitation) {
        CFontRenderer mfont = Client.instance.FontLoaders.Comfortaa16;
        if (!this.custom) {
            Numbers v;
            if (this.value instanceof Option) {
                this.change = (Boolean)this.value.getValue();
            } else if (this.value instanceof Mode) {
                this.name = (String)((Mode)this.value).getValue();
            } else if (this.value instanceof Numbers) {
                v = (Numbers)this.value;
                this.name = String.valueOf(v.isInteger() ? (double)((Number)v.getValue()).intValue() : ((Number)v.getValue()).doubleValue());
                if (mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)mfont.getStringHeight() + 5.0f && Mouse.isButtonDown((int)0)) {
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
            if (this.value instanceof Numbers) {
                v = (Numbers)this.value;
                double render = 68.0f * (((Number)v.getValue()).floatValue() - ((Number)v.getMinimum()).floatValue()) / (((Number)v.getMaximum()).floatValue() - ((Number)v.getMinimum()).floatValue());
                SimpleRender.drawRect((float)this.x - 6.0f, this.y + 8.0f, (float)((double)this.x + render + 6.5), this.y + 9.0f, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), 120).getRGB());
            }
            if (this.value instanceof Option) {
                boolean size = true;
                if (this.change) {
                    RenderUtil.rectangleBordered(this.x + 62 + 1, this.y - 4.0f + 1.0f, this.x + 76 - 1, this.y + 9.0f - 1.0f, 0.5, new Color(61, 141, 255, 0).getRGB(), new Color(255, 255, 255).getRGB());
                    Client.instance.FontLoaders.FLUXICON16.drawString("v", this.x + 65, this.y + 2.0f, new Color(255, 255, 255).getRGB());
                } else {
                    RenderUtil.rectangleBordered(this.x + 62 + 1, this.y - 4.0f + 1.0f, this.x + 76 - 1, this.y + 9.0f - 1.0f, 0.5, new Color(61, 141, 255, 0).getRGB(), new Color(255, 255, 255).getRGB());
                }
            }
            Client.instance.FontLoaders.regular15.drawString(this.value.getName() + ":", this.x - 5, this.y, new Color(255, 255, 255).getRGB());
            Client.instance.FontLoaders.regular15.drawString(this.name, this.x + 76 - 76 + Client.instance.FontLoaders.Comfortaa16.getStringWidth(this.value.getName()), this.y, new Color(255, 255, 255).getRGB());
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        if (!this.custom && mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)Client.instance.FontLoaders.Comfortaa16.getStringHeight() + 5.0f) {
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

