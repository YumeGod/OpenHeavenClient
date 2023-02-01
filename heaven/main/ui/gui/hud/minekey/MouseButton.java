/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.hud.minekey;

import heaven.main.Client;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MouseButton {
    private static final String[] BUTTONS = new String[]{"LMB", "RMB"};
    private final int button;
    private final int xOffset;
    private final int yOffset;
    private final List clicks = new ArrayList();
    private boolean wasPressed = true;
    private long lastPress;

    public MouseButton(int button, int xOffset) {
        this.button = button;
        this.xOffset = xOffset;
        this.yOffset = 50;
    }

    private int getCPS() {
        long time = System.currentTimeMillis();
        Iterator iterator = this.clicks.iterator();
        while (iterator.hasNext()) {
            if ((Long)iterator.next() + 1000L >= time) continue;
            iterator.remove();
        }
        return this.clicks.size();
    }

    public void renderMouseButton(float x, float y, int textColor) {
        double textBrightness;
        int color;
        boolean pressed = Mouse.isButtonDown((int)this.button);
        String name = BUTTONS[this.button];
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
            if (pressed) {
                this.clicks.add(this.lastPress);
            }
        }
        if (pressed) {
            color = Math.min(255, (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.max(0.0, 1.0 - (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        } else {
            color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0, (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        }
        Gui.drawRect(x + (float)this.xOffset, y + (float)this.yOffset, x + (float)this.xOffset + 34.0f, y + (float)this.yOffset + 22.0f, 0x78000000 + (color << 16) + (color << 8) + color);
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        Client.instance.FontLoaders.Comfortaa16.drawString(name, x + (float)this.xOffset + 8.0f, y + (float)this.yOffset + 4.0f, -16777216 + ((int)((double)red * textBrightness) << 16) + ((int)((double)green * textBrightness) << 8) + (int)((double)blue * textBrightness));
        String cpsText = this.getCPS() + " CPS";
        int cpsTextWidth = Client.instance.FontLoaders.Comfortaa17.getStringWidth(cpsText);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        Client.instance.FontLoaders.Comfortaa17.drawString(cpsText, (x + (float)this.xOffset + 17.0f) * 2.0f - (float)(cpsTextWidth / 2), (y + (float)this.yOffset + 14.0f) * 2.0f, -16777216 + ((int)(255.0 * textBrightness) << 16) + ((int)(255.0 * textBrightness) << 8) + (int)(255.0 * textBrightness));
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }
}

