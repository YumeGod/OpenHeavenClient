/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.rendering;

import heaven.main.event.Event;

public class EventRender3D
extends Event {
    public static float ticks;

    public EventRender3D() {
    }

    public EventRender3D(float ticks) {
        EventRender3D.ticks = ticks;
    }

    public float getPartialTicks() {
        return ticks;
    }
}

