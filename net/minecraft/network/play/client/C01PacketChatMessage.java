/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C01PacketChatMessage
implements Packet<INetHandlerPlayServer> {
    public String message;

    public C01PacketChatMessage() {
    }

    public C01PacketChatMessage(String messageIn) {
        if (messageIn.length() > 100) {
            messageIn = messageIn.substring(0, 100);
        }
        this.message = messageIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        this.message = buf.readStringFromBuffer(100);
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        buf.writeString(this.message);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processChatMessage(this);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

