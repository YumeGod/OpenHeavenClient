/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import heaven.main.Client;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.simplecore.SimpleRender;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiButton
extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    public float cs;
    public int alpha;
    public int width;
    public int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean visible = true;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    protected int getHoverState(boolean mouseOver) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }
        return i;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        Color color1 = new Color(0.0f, 0.0f, 0.0f, (float)this.alpha / 255.0f);
        int col1 = color1.getRGB();
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.updatefade();
            if (this.hovered) {
                if (this.cs < 8.0f) {
                    this.cs = AnimationUtil.moveUD(this.cs, 8.0f, SimpleRender.processFPS(1000.0f, 0.02f), SimpleRender.processFPS(1000.0f, 0.018f));
                }
            } else if (this.cs > 0.0f) {
                this.cs = AnimationUtil.moveUD(this.cs, 0.0f, SimpleRender.processFPS(1000.0f, 0.02f), SimpleRender.processFPS(1000.0f, 0.018f));
            }
            if (this.enabled) {
                GuiButton.drawRoundRect((float)this.xPosition + this.cs, this.yPosition, (float)(this.xPosition + this.width) - this.cs, this.yPosition + this.height, col1);
            } else {
                GuiButton.drawRoundRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, new Color(0.5f, 0.5f, 0.5f, 0.5f).hashCode());
            }
            this.mouseDragged(mc, mouseX, mouseY);
            Client.instance.fontManager.chinese17.drawCenteredString(this.displayString, this.xPosition + this.width / 2, (float)(this.yPosition + (this.height - 5) / 2 - 1), 0xE0E0E0);
        }
    }

    private int HUDColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), 200).getRGB();
    }

    public static void drawRoundRect(double d, double e, double g, double h, int color) {
        GuiButton.drawRect(d + 1.0, e, g - 1.0, h, color);
        GuiButton.drawRect(d, e + 1.0, d + 1.0, h - 1.0, color);
        GuiButton.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        GuiButton.drawRect(d + 1.0, e + 1.0, d + 0.5, e + 0.5, color);
        GuiButton.drawRect(g - 1.0, e + 1.0, g - 0.5, e + 0.5, color);
        GuiButton.drawRect(g - 1.0, e + 1.0, g, h - 1.0, color);
        GuiButton.drawRect(d + 1.0, h - 1.0, d + 0.5, h - 0.5, color);
        GuiButton.drawRect(g - 1.0, h - 1.0, g - 0.5, h - 0.5, color);
    }

    private void updatefade() {
        if (this.enabled) {
            this.alpha = this.hovered ? AnimationUtil.moveUDl(this.alpha, 210.0f, SimpleRender.processFPS(1000.0f, 0.022f), SimpleRender.processFPS(1000.0f, 0.016f)) : AnimationUtil.moveUDl(this.alpha, 120.0f, SimpleRender.processFPS(1000.0f, 0.022f), SimpleRender.processFPS(1000.0f, 0.016f));
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

