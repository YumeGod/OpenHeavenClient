/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.vec;

import heaven.main.module.modules.world.ScaffoldUtils.Rotation;
import net.minecraft.util.Vec3;

public class VecRotation {
    Vec3 vec;
    Rotation rotation;

    public VecRotation(Vec3 vec, Rotation rotation) {
        this.vec = vec;
        this.rotation = rotation;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public Vec3 getVec() {
        return this.vec;
    }
}

