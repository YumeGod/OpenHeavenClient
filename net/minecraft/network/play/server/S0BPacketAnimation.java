/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0BPacketAnimation
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private int type;

    public S0BPacketAnimation() {
    }

    public S0BPacketAnimation(Entity ent, int animationType) {
        this.entityId = ent.getEntityId();
        this.type = animationType;
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.entityId = buf.readVarIntFromBuffer();
        this.type = buf.readUnsignedByte();
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeByte(this.type);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleAnimation(this);
    }

    public int getEntityID() {
        return this.entityId;
    }

    public int getAnimationType() {
        return this.type;
    }
}

