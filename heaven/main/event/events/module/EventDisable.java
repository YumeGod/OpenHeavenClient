/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.module;

import heaven.main.event.Event;
import heaven.main.module.Module;

public class EventDisable
extends Event {
    private Module module;

    public EventDisable(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}

