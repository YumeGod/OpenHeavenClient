/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.hud.minekey;

import heaven.main.Client;
import heaven.main.module.modules.render.Keyrender;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.render.color.Colors;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class SimpleWhiteMouseButton {
    private static final String[] BUTTONS = new String[]{"LMB", "RMB"};
    private final int button;
    private final int xOffset;
    private final int yOffset;
    private float aniRadius;
    private float aniOp;
    Color textColor2 = new Color(255, 255, 255);

    public SimpleWhiteMouseButton(int button, int xOffset) {
        this.button = button;
        this.xOffset = xOffset;
        this.yOffset = 50;
    }

    public void renderMouseButton(float x, float y) {
        boolean pressed = Mouse.isButtonDown((int)this.button);
        String name = BUTTONS[this.button];
        if (pressed) {
            this.textColor2 = AnimationUtil.getColorAnimationState(this.textColor2, new Color(20, 20, 20), 800.0);
            this.aniRadius = AnimationUtil.moveUD(this.aniRadius, 20.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            this.aniOp = AnimationUtil.getAnimationState(this.aniOp, 0.55f, 5.0f);
        } else {
            this.textColor2 = AnimationUtil.getColorAnimationState(this.textColor2, new Color(255, 255, 255), 1000.0);
            this.aniRadius = AnimationUtil.moveUD(this.aniRadius, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            this.aniOp = AnimationUtil.getAnimationState(this.aniOp, 0.0f, 5.0f);
        }
        Gui.drawRect(x + (float)this.xOffset, y + (float)this.yOffset, x + (float)this.xOffset + 34.0f, y + (float)this.yOffset + 22.0f, new Color(0, 0, 0, 120).getRGB());
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        RenderUtil.doGlScissor((int)(x + (float)this.xOffset), (int)(y + (float)this.yOffset), 34, 22);
        RenderUtil.circle(x + (float)this.xOffset + 17.0f, y + (float)this.yOffset + 11.0f, this.aniRadius, SimpleRender.reAlpha(Colors.WHITE.c, this.aniOp));
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        if (((Boolean)Keyrender.LR.getValue()).booleanValue()) {
            String realname = name.substring(0, 1);
            Client.instance.FontLoaders.Comfortaa22.drawString(realname, x + (float)this.xOffset + 13.0f + (name.equals("LMB") ? 1.0f : 0.5f), y + (float)this.yOffset + 8.0f, SimpleRender.reAlpha(this.textColor2.getRGB(), 0.9f));
        } else {
            Client.instance.FontLoaders.Comfortaa20.drawString(name, x + (float)this.xOffset - 8.0f + (name.equals("LMB") ? 14.0f : 13.5f), y + (float)this.yOffset + 8.0f, SimpleRender.reAlpha(this.textColor2.getRGB(), 0.9f));
        }
    }
}

