/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat.utils;

public class Rotation {
    public float yaw;
    public float pitch = 0.0f;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }
}

