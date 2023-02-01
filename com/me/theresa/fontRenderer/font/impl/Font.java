/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.impl;

import com.me.theresa.fontRenderer.font.Color;

public interface Font {
    public int getWidth(String var1);

    public int getHeight(String var1);

    public int getLineHeight();

    public void drawString(float var1, float var2, String var3);

    public void drawString(float var1, float var2, String var3, Color var4);

    public void drawString(float var1, float var2, String var3, Color var4, int var5, int var6);
}

