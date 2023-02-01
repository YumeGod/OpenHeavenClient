/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.effect;

import com.me.theresa.fontRenderer.font.Glyph;
import com.me.theresa.fontRenderer.font.UnicodeFont;
import com.me.theresa.fontRenderer.font.effect.ConfigurableEffect;
import com.me.theresa.fontRenderer.font.util.EffectUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorEffect
implements ConfigurableEffect {
    private Color color = Color.white;

    public ColorEffect() {
    }

    public ColorEffect(Color color) {
        this.color = color;
    }

    @Override
    public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
        g.setColor(this.color);
        g.fill(glyph.getShape());
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null.");
        }
        this.color = color;
    }

    public String toString() {
        return "Color";
    }

    @Override
    public List getValues() {
        ArrayList<ConfigurableEffect.Value> values = new ArrayList<ConfigurableEffect.Value>();
        values.add(EffectUtil.colorValue("Color", this.color));
        return values;
    }

    @Override
    public void setValues(List values) {
        for (ConfigurableEffect.Value value : values) {
            if (!value.getName().equals("Color")) continue;
            this.setColor((Color)value.getObject());
        }
    }
}

