/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C16PacketClientStatus
implements Packet<INetHandlerPlayServer> {
    private EnumState status;

    public C16PacketClientStatus() {
    }

    public C16PacketClientStatus(EnumState statusIn) {
        this.status = statusIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.status = buf.readEnumValue(EnumState.class);
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeEnumValue(this.status);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processClientStatus(this);
    }

    public EnumState getStatus() {
        return this.status;
    }

    public static enum EnumState {
        PERFORM_RESPAWN,
        REQUEST_STATS,
        OPEN_INVENTORY_ACHIEVEMENT;

    }
}

