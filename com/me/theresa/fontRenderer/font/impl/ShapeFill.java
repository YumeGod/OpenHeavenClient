/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.impl;

import com.me.theresa.fontRenderer.font.Color;
import com.me.theresa.fontRenderer.font.geom.Shape;
import com.me.theresa.fontRenderer.font.geom.Vector2f;

public interface ShapeFill {
    public Color colorAt(Shape var1, float var2, float var3);

    public Vector2f getOffsetAt(Shape var1, float var2, float var3);
}

