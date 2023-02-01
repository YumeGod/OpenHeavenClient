/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat.utils;

import heaven.main.module.modules.combat.utils.Rotation;
import net.minecraft.util.Vec3;

public class VecRotation {
    Vec3 vec3;
    Rotation rotation;

    public VecRotation(Vec3 Vec32, Rotation Rotation2) {
        this.vec3 = Vec32;
        this.rotation = Rotation2;
    }

    public Vec3 getVec3() {
        return this.vec3;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public Vec3 getVec() {
        return this.vec3;
    }
}

