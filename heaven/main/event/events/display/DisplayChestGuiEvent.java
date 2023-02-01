/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.display;

import heaven.main.event.Event;

public class DisplayChestGuiEvent
extends Event {
    String string;

    public DisplayChestGuiEvent(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }
}

