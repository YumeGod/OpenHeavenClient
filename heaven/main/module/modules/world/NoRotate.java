/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate
extends Module {
    private final Option<Boolean> Confirm = new Option<Boolean>("Confirm", false);
    private final Option<Boolean> ConfirmIllegalRotation = new Option<Boolean>("ConfirmIllegalRotation", false);
    private final Option<Boolean> NoZero = new Option<Boolean>("NoZero", false);
    private final Option<Boolean> onground = new Option<Boolean>("OnGround", false);

    public NoRotate() {
        super("NoRotate", ModuleType.World);
        this.addValues(this.Confirm, this.ConfirmIllegalRotation, this.NoZero, this.onground);
    }

    @EventHandler
    private void onPacket(EventPacketReceive event) {
        block8: {
            block10: {
                S08PacketPlayerPosLook thePacket;
                block9: {
                    Packet packet;
                    if (Minecraft.thePlayer == null) {
                        return;
                    }
                    if (((Boolean)this.onground.get()).booleanValue()) {
                        if (!Minecraft.thePlayer.onGround) {
                            return;
                        }
                    }
                    if (!((packet = event.packet) instanceof S08PacketPlayerPosLook)) break block8;
                    thePacket = (S08PacketPlayerPosLook)packet;
                    if (((Boolean)this.NoZero.getValue()).booleanValue() && thePacket.getYaw() == 0.0f && thePacket.getPitch() == 0.0f) {
                        return;
                    }
                    if (((Boolean)this.ConfirmIllegalRotation.getValue()).booleanValue()) break block9;
                    if (!(thePacket.getPitch() <= 90.0f) || !(thePacket.getPitch() >= -90.0f)) break block10;
                    if (thePacket.getYaw() == Minecraft.thePlayer.rotationYaw) break block10;
                    if (thePacket.getPitch() == Minecraft.thePlayer.rotationPitch) break block10;
                }
                if (((Boolean)this.Confirm.getValue()).booleanValue()) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(thePacket.getYaw(), thePacket.getPitch(), Minecraft.thePlayer.onGround));
                }
            }
            thePacket.yaw = Minecraft.thePlayer.rotationYaw;
            thePacket.pitch = Minecraft.thePlayer.rotationPitch;
        }
    }
}

