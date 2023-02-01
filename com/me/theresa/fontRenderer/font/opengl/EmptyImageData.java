/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 */
package com.me.theresa.fontRenderer.font.opengl;

import com.me.theresa.fontRenderer.font.opengl.ImageData;
import com.me.theresa.fontRenderer.font.opengl.InternalTextureLoader;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class EmptyImageData
implements ImageData {
    private final int width;
    private final int height;

    public EmptyImageData(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getDepth() {
        return 32;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public ByteBuffer getImageBufferData() {
        return BufferUtils.createByteBuffer((int)(this.getTexWidth() * this.getTexHeight() * 4));
    }

    @Override
    public int getTexHeight() {
        return InternalTextureLoader.get2Fold(this.height);
    }

    @Override
    public int getTexWidth() {
        return InternalTextureLoader.get2Fold(this.width);
    }

    @Override
    public int getWidth() {
        return this.width;
    }
}

