/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat.utils;

import net.minecraft.entity.Entity;

public class Motion {
    private double motionX;
    private double motionY;
    private double motionZ;

    public Motion(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public Motion(Entity entity) {
        this.motionX = entity.motionX;
        this.motionY = entity.motionY;
        this.motionZ = entity.motionZ;
    }

    public void reset() {
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }

    public void setTo(Motion motion) {
        this.motionX = motion.getMotionX();
        this.motionY = motion.getMotionY();
        this.motionZ = motion.getMotionZ();
    }

    public void add(double x, double y, double z) {
        this.motionX += x;
        this.motionY += y;
        this.motionZ += z;
    }

    public void remove(double x, double y, double z) {
        this.motionX -= x;
        this.motionY -= y;
        this.motionZ -= z;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public double getMotionY() {
        return this.motionY;
    }

    public double getMotionZ() {
        return this.motionZ;
    }

    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }
}

