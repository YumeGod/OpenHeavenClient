/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.guimainmenu.mainmenu;

import heaven.main.Client;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class FlatButton
extends GuiButton {
    final int buttonId;
    int buttonalpha;
    int textalpha;
    public static boolean hoverstatic;
    float x2;
    int color;

    public FlatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.buttonId = buttonId;
        this.x2 = -240.0f;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && this.isMouseOver();
    }

    private int RectColor() {
        if (this.color != (this.hovered ? 255 : 233)) {
            this.color = (int)AnimationUtil.moveUD(this.color, this.hovered ? 255.0f : 233.0f, 0.2f, 0.15f);
        }
        return new Color(this.color, this.color, this.color).getRGB();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width + 40 && mouseY < this.yPosition + this.height;
        this.GotAlpha();
        this.x2 = AnimationUtil.moveUD(this.x2, this.hovered ? 40.0f : 0.0f, 0.2f, 0.1f);
        Client.instance.FontLoaders.bold45.drawString(this.displayString, (float)(this.xPosition + this.width / 2) + this.x2 + 7.0f - (float)(Client.instance.FontLoaders.bold50.getStringWidth(this.displayString) / 2), (float)this.yPosition + (float)(this.height - 6) / 2.0f, this.RectColor());
    }

    private void GotAlpha() {
        if (this.buttonalpha != 170) {
            this.buttonalpha = (int)AnimationUtil.moveUD(this.buttonalpha, 170.0f, 0.2f, 0.15f);
        }
        if (this.textalpha != 200) {
            this.textalpha = (int)AnimationUtil.moveUD(this.textalpha, 200.0f, 0.2f, 0.15f);
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
    }
}

