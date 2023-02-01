/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.novogui;

import heaven.main.ui.gui.clickgui.novogui.ValueButton;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class ColorValueButton
extends ValueButton {
    private final float[] hue = new float[]{0.0f};
    private int position;
    private int color = new Color(125, 125, 125).getRGB();

    public ColorValueButton(int x, int y) {
        super(null, x, y);
        this.custom = true;
        this.position = -1111;
    }

    public void render(int mouseX, int mouseY) {
        float[] huee = new float[]{this.hue[0]};
        Gui.drawRect((double)(this.x - 10), this.y - 4.0f, (double)(this.x + 80), this.y + 11.0f, new Color(0, 0, 0, 100).getRGB());
        for (int i = this.x - 7; i < this.x + 79; ++i) {
            Color color = Color.getHSBColor(huee[0] / 255.0f, 0.7f, 1.0f);
            if (mouseX > i - 1 && mouseX < i + 1 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + 12.0f && Mouse.isButtonDown((int)0)) {
                this.color = color.getRGB();
                this.position = i;
            }
            if (this.color == color.getRGB()) {
                this.position = i;
            }
            Gui.drawRect((double)(i - 1), this.y, (double)i, this.y + 8.0f, color.getRGB());
            huee[0] = huee[0] + 4.0f;
            if (!(huee[0] > 255.0f)) continue;
            huee[0] = huee[0] - 255.0f;
        }
        Gui.drawRect((double)this.position, this.y, (double)(this.position + 1), this.y + 8.0f, -1);
        if (this.hue[0] > 255.0f) {
            this.hue[0] = this.hue[0] - 255.0f;
        }
    }

    @Override
    public void key(char typedChar, int keyCode) {
    }

    @Override
    public void click(int mouseX, int mouseY, int button) {
    }
}

