/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.rendering;

import heaven.main.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D
extends Event {
    private float partialTicks;
    private final ScaledResolution resolution;

    public EventRender2D(ScaledResolution resolution, float partialTicks) {
        this.resolution = resolution;
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getResolution() {
        return this.resolution;
    }
}

