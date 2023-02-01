/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event;

import net.minecraft.client.Minecraft;

public abstract class Event {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private boolean cancelled;
    public byte type;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void cancelEvent() {
        this.cancelled = true;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public static enum State {
        PRE,
        POST;

    }
}

