/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.apiuse;

import com.darkmagician6.eventapi.events.Cancellable;
import com.darkmagician6.eventapi.events.Event;

public abstract class CancellableEvent
implements Event,
Cancellable {
    private boolean cancelled;

    protected CancellableEvent() {
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }
}

