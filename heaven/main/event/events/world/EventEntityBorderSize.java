/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;

public class EventEntityBorderSize
extends Event {
    private float borderSize;

    public EventEntityBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }

    public float getBorderSize() {
        return this.borderSize;
    }

    public void setBorderSize(float borderSize) {
        this.borderSize = borderSize;
    }
}

