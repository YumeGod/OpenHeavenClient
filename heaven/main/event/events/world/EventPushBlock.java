/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;

public class EventPushBlock
extends Event {
    boolean isPre;

    public void fire(boolean pre) {
        this.isPre = pre;
    }

    public boolean isPre() {
        return this.isPre;
    }
}

