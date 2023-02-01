/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;
import net.minecraft.entity.Entity;

public class RenderArmEvent
extends Event {
    private final Entity entity;
    private final boolean pre;

    public RenderArmEvent(Entity entity, boolean pre) {
        this.entity = entity;
        this.pre = pre;
    }

    public boolean isPre() {
        return this.pre;
    }

    public boolean isPost() {
        return !this.pre;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

