/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.vecmath;

import net.minecraft.VLOUBOOS.javax.vecmath.Tuple3d;
import net.minecraft.VLOUBOOS.javax.vecmath.Tuple3f;

public class TexCoord3f
extends Tuple3f {
    static final long serialVersionUID = -3517736544731446513L;

    public TexCoord3f(float x, float y, float z) {
        super(x, y, z);
    }

    public TexCoord3f(float[] v) {
        super(v);
    }

    public TexCoord3f(TexCoord3f v1) {
        super(v1);
    }

    public TexCoord3f(Tuple3f t1) {
        super(t1);
    }

    public TexCoord3f(Tuple3d t1) {
        super(t1);
    }

    public TexCoord3f() {
    }
}

