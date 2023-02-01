/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C09PacketHeldItemChange
implements Packet<INetHandlerPlayServer> {
    private int slotId;

    public C09PacketHeldItemChange() {
    }

    public C09PacketHeldItemChange(int slotId) {
        this.slotId = slotId;
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.slotId = buf.readShort();
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeShort(this.slotId);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processHeldItemChange(this);
    }

    public int getSlotId() {
        return this.slotId;
    }
}

