/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.geom;

import java.io.Serializable;

public interface Triangulator
extends Serializable {
    public int getTriangleCount();

    public float[] getTrianglePoint(int var1, int var2);

    public void addPolyPoint(float var1, float var2);

    public void startHole();

    public boolean triangulate();
}

