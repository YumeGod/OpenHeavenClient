/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C13PacketPlayerAbilities
implements Packet<INetHandlerPlayServer> {
    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    public C13PacketPlayerAbilities() {
    }

    public C13PacketPlayerAbilities(PlayerCapabilities capabilities) {
        this.invulnerable = capabilities.disableDamage;
        this.flying = capabilities.isFlying;
        this.allowFlying = capabilities.allowFlying;
        this.creativeMode = capabilities.isCreativeMode;
        this.flySpeed = capabilities.getFlySpeed();
        this.walkSpeed = capabilities.getWalkSpeed();
    }

    @Override
    public void readPacketData(PacketBuffer buf) {
        byte b0 = buf.readByte();
        this.invulnerable = (b0 & 1) > 0;
        this.flying = (b0 & 2) > 0;
        this.allowFlying = (b0 & 4) > 0;
        this.creativeMode = (b0 & 8) > 0;
        this.flySpeed = buf.readFloat();
        this.walkSpeed = buf.readFloat();
    }

    @Override
    public void writePacketData(PacketBuffer buf) {
        byte b0 = 0;
        if (this.invulnerable) {
            b0 = (byte)(b0 | 1);
        }
        if (this.flying) {
            b0 = (byte)(b0 | 2);
        }
        if (this.allowFlying) {
            b0 = (byte)(b0 | 4);
        }
        if (this.creativeMode) {
            b0 = (byte)(b0 | 8);
        }
        buf.writeByte(b0);
        buf.writeFloat(this.flySpeed);
        buf.writeFloat(this.walkSpeed);
    }

    @Override
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processPlayerAbilities(this);
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean isInvulnerable) {
        this.invulnerable = isInvulnerable;
    }

    public boolean isFlying() {
        return this.flying;
    }

    public void setFlying(boolean isFlying) {
        this.flying = isFlying;
    }

    public boolean isAllowFlying() {
        return this.allowFlying;
    }

    public void setAllowFlying(boolean isAllowFlying) {
        this.allowFlying = isAllowFlying;
    }

    public boolean isCreativeMode() {
        return this.creativeMode;
    }

    public void setCreativeMode(boolean isCreativeMode) {
        this.creativeMode = isCreativeMode;
    }

    public void setFlySpeed(float flySpeedIn) {
        this.flySpeed = flySpeedIn;
    }

    public void setWalkSpeed(float walkSpeedIn) {
        this.walkSpeed = walkSpeedIn;
    }
}

