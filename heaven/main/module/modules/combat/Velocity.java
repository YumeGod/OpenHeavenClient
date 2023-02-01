/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    private final String[] modes = new String[]{"Normal", "SNCP", "AAC4.3.6", "AAC5.2.0", "Jump"};
    public final Mode<String> mode = new Mode("Mode", this.modes, this.modes[0]);
    private final Option<Boolean> ground = new Option<Boolean>("OnlyGround", false);
    private final Option<Boolean> nomove = new Option<Boolean>("NoMove", false);
    private final Option<Boolean> waterchecks = new Option<Boolean>("WaterCheck", false);
    private final Option<Boolean> onlywhentarget = new Option<Boolean>("OnlyWhenTargeting", false);
    private final Numbers<Double> hurttime = new Numbers<Double>("HurtTime", 20.0, 0.0, 20.0, 1.0);
    private final Numbers<Double> h = new Numbers<Double>("Horizontal", 0.0, 0.0, 100.0, 1.0, () -> this.mode.isCurrentMode("Normal"));
    private final Numbers<Double> v = new Numbers<Double>("Vertical", 0.0, 0.0, 100.0, 1.0, () -> this.mode.isCurrentMode("Normal"));

    public Velocity() {
        super("Velocity", ModuleType.Combat);
        this.addValues(this.mode, this.h, this.v, this.hurttime, this.onlywhentarget, this.nomove, this.ground, this.waterchecks);
    }

    @EventHandler
    private void onUpdate(EventPacketReceive e) {
        if ((double)Minecraft.thePlayer.hurtResistantTime > (Double)this.hurttime.getValue()) {
            return;
        }
        if (((Boolean)this.ground.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (((Boolean)this.nomove.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (((Boolean)this.waterchecks.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.isInWater()) {
                return;
            }
        }
        if (((Boolean)this.onlywhentarget.getValue()).booleanValue() && !(e.getPacket() instanceof C02PacketUseEntity)) {
            return;
        }
        if (this.mode.isCurrentMode("Jump")) {
            if (Minecraft.thePlayer.hurtResistantTime > 0) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.jump();
                }
            }
        }
        if (this.mode.isCurrentMode("AAC5.2.0") && (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion)) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Double.MAX_VALUE, Minecraft.thePlayer.posZ, true));
            e.setCancelled(true);
        }
        if (this.mode.isCurrentMode("AAC4.3.6")) {
            if (Minecraft.thePlayer.hurtResistantTime > 0) {
                Minecraft.thePlayer.motionX *= 0.6;
                Minecraft.thePlayer.motionZ *= 0.6;
            }
        }
    }

    @EventHandler
    private void onPacket(EventPacketReceive e) {
        this.setSuffix((Serializable)((Object)(this.h.getValue() + "% " + this.v.getValue() + "%")));
        if ((double)Minecraft.thePlayer.hurtResistantTime > (Double)this.hurttime.getValue()) {
            return;
        }
        if (((Boolean)this.ground.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (((Boolean)this.nomove.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (((Boolean)this.waterchecks.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.isInWater()) {
                return;
            }
        }
        if (((Boolean)this.onlywhentarget.getValue()).booleanValue() && !(e.getPacket() instanceof C02PacketUseEntity)) {
            return;
        }
        if (this.mode.isCurrentMode("Normal") && (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion)) {
            if (((Double)this.h.getValue()).equals(0.0) && ((Double)this.v.getValue()).equals(0.0)) {
                e.setCancelled(true);
            } else {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                packet.motionX = (int)((Double)this.v.getValue() / 100.0);
                packet.motionY = (int)((Double)this.h.getValue() / 100.0);
                packet.motionZ = (int)((Double)this.v.getValue() / 100.0);
            }
        }
        if (this.mode.isCurrentMode("SNCP") && (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion)) {
            if (Minecraft.thePlayer.onGround) {
                e.setCancelled(true);
            } else if (Minecraft.thePlayer.hurtResistantTime > 0) {
                Minecraft.thePlayer.motionX *= 0.6;
                Minecraft.thePlayer.motionZ *= 0.6;
            }
        }
    }
}

