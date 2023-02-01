/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack
extends Event {
    public final Entity entity;

    public EventAttack(Entity targetEntity) {
        this.entity = targetEntity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

