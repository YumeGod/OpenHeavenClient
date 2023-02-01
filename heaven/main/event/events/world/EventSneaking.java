/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EventSneaking
extends Event {
    public double x;
    public double y;
    public double z;
    public double d3;
    public double d5;
    public double offset = -1.0;
    public World worldObj;
    public AxisAlignedBB boundingBox;
    public Entity entity;
    public boolean sneaking;

    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}

