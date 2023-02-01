/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.More;

import heaven.main.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent
extends Event {
    private final State state;
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet, State state) {
        this.state = state;
        this.packet = packet;
    }

    public State getState() {
        return this.state;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public static enum State {
        INCOMING,
        OUTGOING;

    }
}

