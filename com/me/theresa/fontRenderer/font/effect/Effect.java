/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.effect;

import com.me.theresa.fontRenderer.font.Glyph;
import com.me.theresa.fontRenderer.font.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public interface Effect {
    public void draw(BufferedImage var1, Graphics2D var2, UnicodeFont var3, Glyph var4);
}

