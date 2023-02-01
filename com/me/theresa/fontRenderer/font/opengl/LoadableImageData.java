/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.opengl;

import com.me.theresa.fontRenderer.font.opengl.ImageData;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface LoadableImageData
extends ImageData {
    public void configureEdging(boolean var1);

    public ByteBuffer loadImage(InputStream var1) throws IOException;

    public ByteBuffer loadImage(InputStream var1, boolean var2, int[] var3) throws IOException;

    public ByteBuffer loadImage(InputStream var1, boolean var2, boolean var3, int[] var4) throws IOException;
}

