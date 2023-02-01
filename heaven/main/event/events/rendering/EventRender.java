/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.rendering;

import heaven.main.event.Event;

public class EventRender
extends Event {
    public static float partialTicks;

    public EventRender(float partialTicks) {
        EventRender.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

