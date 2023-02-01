/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.misc;

import heaven.main.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity
extends Event {
    Entity entity;
    Event.State state;

    public EventRenderEntity(Entity entity, Event.State state) {
        this.entity = entity;
        this.state = state;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }

    public Event.State getState() {
        return this.state;
    }

    public void setState(Event.State state) {
        this.state = state;
    }
}

