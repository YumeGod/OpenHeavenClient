/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.tessellate;

import com.tessellate.Tessellation;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class BasicTess
implements Tessellation {
    int index;
    int[] raw = new int[capacity *= 6];
    ByteBuffer buffer;
    FloatBuffer fBuffer;
    IntBuffer iBuffer;
    private int colors;
    private float texU;
    private float texV;
    private boolean color;
    private boolean texture;

    BasicTess(int capacity) {
        this.buffer = ByteBuffer.allocateDirect(capacity << 2).order(ByteOrder.nativeOrder());
        this.fBuffer = this.buffer.asFloatBuffer();
        this.iBuffer = this.buffer.asIntBuffer();
    }

    @Override
    public Tessellation setColor(int color) {
        this.color = true;
        this.colors = color;
        return this;
    }

    @Override
    public Tessellation setTexture(float u, float v) {
        this.texture = true;
        this.texU = u;
        this.texV = v;
        return this;
    }

    @Override
    public Tessellation addVertex(float x, float y, float z) {
        int dex = this.index * 6;
        this.raw[dex] = Float.floatToRawIntBits(x);
        this.raw[dex + 1] = Float.floatToRawIntBits(y);
        this.raw[dex + 2] = Float.floatToRawIntBits(z);
        this.raw[dex + 3] = this.colors;
        this.raw[dex + 4] = Float.floatToRawIntBits(this.texU);
        this.raw[dex + 5] = Float.floatToRawIntBits(this.texV);
        ++this.index;
        return this;
    }

    @Override
    public Tessellation bind() {
        int dex = this.index * 6;
        this.iBuffer.put(this.raw, 0, dex);
        this.buffer.position(0);
        this.buffer.limit(dex << 2);
        if (this.color) {
            this.buffer.position(12);
            GL11.glColorPointer((int)4, (boolean)true, (int)24, (ByteBuffer)this.buffer);
        }
        if (this.texture) {
            this.fBuffer.position(4);
            GL11.glTexCoordPointer((int)2, (int)24, (FloatBuffer)this.fBuffer);
        }
        this.fBuffer.position(0);
        GL11.glVertexPointer((int)3, (int)24, (FloatBuffer)this.fBuffer);
        return this;
    }

    @Override
    public Tessellation pass(int mode) {
        GL11.glDrawArrays((int)mode, (int)0, (int)this.index);
        return this;
    }

    @Override
    public Tessellation unbind() {
        this.iBuffer.position(0);
        return this;
    }

    @Override
    public Tessellation reset() {
        this.iBuffer.clear();
        this.index = 0;
        this.color = false;
        this.texture = false;
        return this;
    }
}

