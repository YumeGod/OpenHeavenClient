/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;

public class EventStep
extends Event {
    private float stepHeight;

    public EventStep(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public float getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }
}

