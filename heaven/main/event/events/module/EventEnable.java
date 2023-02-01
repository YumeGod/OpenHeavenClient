/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.module;

import heaven.main.event.Event;
import heaven.main.module.Module;

public class EventEnable
extends Event {
    private Module module;

    public EventEnable(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}

