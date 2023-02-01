/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.hud.minekey;

import heaven.main.Client;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.render.color.Colors;
import java.awt.Color;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class SimpleWhiteKey {
    private final KeyBinding key;
    private final int xOffset;
    private final int yOffset;
    private float aniRadius;
    private float aniOp;
    Color textColor2 = new Color(255, 255, 255);

    public SimpleWhiteKey(KeyBinding key, int xOffset, int yOffset) {
        this.key = key;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void renderKey(float x, float y) {
        boolean pressed = this.key.isKeyDown();
        String name = Keyboard.getKeyName((int)this.key.getKeyCode());
        if (pressed) {
            this.textColor2 = AnimationUtil.getColorAnimationState(this.textColor2, new Color(20, 20, 20), 800.0);
            this.aniRadius = AnimationUtil.moveUD(this.aniRadius, 16.0f, SimpleRender.processFPS(1000.0f, 0.011f), SimpleRender.processFPS(1000.0f, 0.009f));
            this.aniOp = AnimationUtil.getAnimationState(this.aniOp, 0.55f, SimpleRender.processFPS(1000.0f, 4.0f));
        } else {
            this.textColor2 = AnimationUtil.getColorAnimationState(this.textColor2, new Color(255, 255, 255), 1000.0);
            this.aniRadius = AnimationUtil.moveUD(this.aniRadius, 0.0f, SimpleRender.processFPS(1000.0f, 0.011f), SimpleRender.processFPS(1000.0f, 0.009f));
            this.aniOp = AnimationUtil.getAnimationState(this.aniOp, 0.0f, SimpleRender.processFPS(1000.0f, 0.1f));
        }
        SimpleRender.drawRect(x + (float)this.xOffset, y + (float)this.yOffset, x + (float)this.xOffset + 22.0f, y + (float)this.yOffset + 22.0f, new Color(0, 0, 0, 120).getRGB());
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        RenderUtil.doGlScissor((int)(x + (float)this.xOffset), (int)(y + (float)this.yOffset), 22, 22);
        RenderUtil.circle(x + (float)this.xOffset + 11.0f, y + (float)this.yOffset + 11.0f, this.aniRadius, SimpleRender.reAlpha(Colors.WHITE.c, this.aniOp));
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
        if (name.contains("W")) {
            Client.instance.FontLoaders.regular17.drawString(name, x + (float)this.xOffset + 7.0f, y + (float)this.yOffset + 8.0f, SimpleRender.reAlpha(this.textColor2.getRGB(), 0.9f));
        } else {
            Client.instance.FontLoaders.regular17.drawString(name, x + (float)this.xOffset + 8.0f, y + (float)this.yOffset + 8.0f, SimpleRender.reAlpha(this.textColor2.getRGB(), 0.9f));
        }
    }
}

