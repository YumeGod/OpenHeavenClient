/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;
import net.minecraft.entity.Entity;

public class EntityAddedEvent
extends Event {
    public Entity entity;

    public EntityAddedEvent(Entity targetEntity) {
        this.entity = targetEntity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

