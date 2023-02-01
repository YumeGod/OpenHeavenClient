/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.guimainmenu.mainmenu;

import heaven.main.Client;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.guimainmenu.mainmenu.ClientMainMenu;
import heaven.main.ui.sound.SoundManger;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class ShaderButton
extends GuiButton {
    final int buttonId;
    float textY;
    public static boolean ioHover;
    final ClientMainMenu gui = new ClientMainMenu();
    int color;
    int colorr;
    int colorrr;

    public ShaderButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.buttonId = buttonId;
        this.textY = -10.0f;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && this.isMouseOver();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        if (this.textY != (float)(this.hovered ? 3 : 0)) {
            this.textY = AnimationUtil.moveUD(this.textY, this.hovered ? 5.0f : 0.0f, 0.2f, 0.15f);
        }
        ioHover = this.hovered;
        Client.instance.FontLoaders.bold16.drawString("Mix:" + ClientMainMenu.useShader, this.xPosition + this.width / 2 - Client.instance.FontLoaders.bold16.getStringWidth(this.displayString) / 2, (float)this.yPosition + this.textY + (float)(this.height - 6) / 2.0f, this.TheColor());
    }

    private int TheColor() {
        this.color = (int)(this.hovered ? AnimationUtil.moveUD(this.color, 56.0f, 0.2f, 0.15f) : AnimationUtil.moveUD(this.color, 233.0f, 0.2f, 0.15f));
        this.colorr = (int)(this.hovered ? AnimationUtil.moveUD(this.colorr, 121.0f, 0.2f, 0.15f) : AnimationUtil.moveUD(this.color, 233.0f, 0.2f, 0.15f));
        this.colorrr = (int)(this.hovered ? AnimationUtil.moveUD(this.colorrr, 241.0f, 0.2f, 0.15f) : AnimationUtil.moveUD(this.color, 233.0f, 0.2f, 0.15f));
        return new Color(this.color, this.colorr, this.colorrr, this.gui.textalpha).getRGB();
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        if (ClientMainMenu.useShader) {
            new SoundManger().jelloEnableSound();
        } else {
            new SoundManger().jelloDisableSound();
        }
    }
}

