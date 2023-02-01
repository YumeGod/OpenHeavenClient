/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.misc;

import heaven.main.event.Event;

public class EventSendMessage
extends Event {
    private String message;

    public EventSendMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

