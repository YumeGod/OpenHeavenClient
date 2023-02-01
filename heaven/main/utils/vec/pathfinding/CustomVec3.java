/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.vec.pathfinding;

import net.minecraft.util.Vec3;

public final class CustomVec3 {
    private final double x;
    private final double y;
    private final double z;

    public CustomVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public CustomVec3 addVector(double x, double y, double z) {
        return new CustomVec3(this.x + x, this.y + y, this.z + z);
    }

    public CustomVec3 floor() {
        return new CustomVec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(CustomVec3 v) {
        return Math.pow(v.x - this.x, 2.0) + Math.pow(v.y - this.y, 2.0) + Math.pow(v.z - this.z, 2.0);
    }

    public CustomVec3 add(CustomVec3 v) {
        return this.addVector(v.x, v.y, v.z);
    }

    public Vec3 mc() {
        return new Vec3(this.x, this.y, this.z);
    }

    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}

