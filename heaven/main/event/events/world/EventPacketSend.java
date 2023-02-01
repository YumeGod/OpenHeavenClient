/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;
import net.minecraft.network.Packet;

public class EventPacketSend
extends Event {
    public Packet packet;
    private boolean sendPacketInEvent;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
        this.sendPacketInEvent = false;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public void sendPacketInEvent() {
        this.sendPacketInEvent = true;
    }

    public boolean isSendPacketInEvent() {
        return this.sendPacketInEvent;
    }
}

