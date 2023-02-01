/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.ui.gui.hud.minekey;

import heaven.main.Client;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Key {
    private final KeyBinding key;
    private final int xOffset;
    private final int yOffset;
    private boolean wasPressed = true;
    private long lastPress;
    int color;

    public Key(KeyBinding key, int xOffset, int yOffset) {
        this.key = key;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void renderKey(float x, float y, int textColor) {
        double textBrightness;
        boolean pressed = this.key.isKeyDown();
        String name = Keyboard.getKeyName((int)this.key.getKeyCode());
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }
        if (pressed) {
            this.color = Math.min(255, (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.max(0.0, 1.0 - (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        } else {
            this.color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            textBrightness = Math.min(1.0, (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        }
        Gui.drawRect(x + (float)this.xOffset, y + (float)this.yOffset, x + (float)this.xOffset + 22.0f, y + (float)this.yOffset + 22.0f, 0x78000000 + (this.color << 16) + (this.color << 8) + this.color);
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        if (name.contains("W")) {
            Client.instance.FontLoaders.Comfortaa17.drawString(name, x + (float)this.xOffset + 7.0f, y + (float)this.yOffset + 9.0f, -16777216 + ((int)((double)red * textBrightness) << 16) + ((int)((double)green * textBrightness) << 8) + (int)((double)blue * textBrightness));
        } else {
            Client.instance.FontLoaders.Comfortaa17.drawString(name, x + (float)this.xOffset + 8.0f, y + (float)this.yOffset + 9.0f, -16777216 + ((int)((double)red * textBrightness) << 16) + ((int)((double)green * textBrightness) << 8) + (int)((double)blue * textBrightness));
        }
    }
}

