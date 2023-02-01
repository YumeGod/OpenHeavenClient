/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world.ScaffoldUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation(Entity ent) {
        this.yaw = ent.rotationYaw;
        this.pitch = ent.rotationPitch;
    }

    public void add(float yaw, float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
    }

    public void remove(float yaw, float pitch) {
        this.yaw -= yaw;
        this.pitch -= pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float[] getBoth() {
        return new float[]{this.yaw, this.pitch};
    }

    public void toPlayer(EntityPlayer player) {
        if (Float.isNaN(this.yaw) || Float.isNaN(this.pitch)) {
            return;
        }
        this.fixedSensitivity(Float.valueOf(Minecraft.getMinecraft().gameSettings.mouseSensitivity));
        player.rotationYaw = this.yaw;
        player.rotationPitch = this.pitch;
    }

    public void fixedSensitivity(Float sensitivity) {
        float f = sensitivity.floatValue() * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        this.yaw -= this.yaw % gcd;
        this.pitch -= this.pitch % gcd;
    }
}

