/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.key;

import heaven.main.event.apiuse.CancellableEvent;

public class KeyPressEvent
extends CancellableEvent {
    private int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

