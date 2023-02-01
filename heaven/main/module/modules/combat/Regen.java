/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class Regen
extends Module {
    private final Numbers<Double> packet = new Numbers<Double>("Packets", 10.0, 1.0, 1000.0, 1.0);
    private final Numbers<Double> regendelay = new Numbers<Double>("Delay", 500.0, 0.0, 10000.0, 10.0);
    private final Numbers<Double> realdelay = new Numbers<Double>("realdelay", 500.0, 0.0, 1000.0, 1.0);
    private final Option<Boolean> ground = new Option<Boolean>("OnlyGround", false);
    private final Option<Boolean> check = new Option<Boolean>("StopFly", false);
    private final Option<Boolean> onlyeffect = new Option<Boolean>("OnlyEffect", false);
    private final Numbers<Double> health = new Numbers<Double>("Health", 15.0, 0.5, 100.0, 0.5);
    private final Mode<String> mode = new Mode("Mode", new String[]{"Normal", "Ghostly", "New", "QueueSilent", "QueueWithoutEvent", "QueueSilent", "NoEvent", "NoEventDelayed"}, "Normal");
    private final TimeHelper delay = new TimeHelper();

    public Regen() {
        super("Regen", new String[]{"regen"}, ModuleType.Combat);
        this.addValues(this.mode, this.health, this.ground, this.onlyeffect, this.check, this.packet, this.regendelay, this.realdelay);
    }

    @EventHandler
    public void onMotion(EventPreUpdate event) {
        double value = (Double)this.packet.getValue();
        this.setSuffix(Double.valueOf(value));
        if (((Boolean)this.ground.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (((Boolean)this.check.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
            return;
        }
        if (((Boolean)this.onlyeffect.get()).booleanValue()) {
            if (!Minecraft.thePlayer.isPotionActive(Potion.regeneration)) {
                return;
            }
        }
        if (this.delay.isDelayComplete(((Double)this.regendelay.getValue()).intValue()) && this.PlayerCheck()) {
            int i = 0;
            while ((double)i < value) {
                switch ((String)this.mode.get()) {
                    case "Normal": {
                        this.sendPacket(new C03PacketPlayer(true));
                        break;
                    }
                    case "Ghostly": {
                        this.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-9, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, false));
                    }
                    case "New": {
                        Minecraft.thePlayer.sendQueue.addToSendNewQueue(new C03PacketPlayer(true));
                    }
                    case "QueueSilent": {
                        Minecraft.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    }
                    case "QueueWithoutEvent": {
                        Minecraft.thePlayer.sendQueue.sendQueueWithoutEvent(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    }
                    case "NewQueue": {
                        Minecraft.thePlayer.sendQueue.addToSendNewQueue(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    }
                    case "NoEvent": {
                        this.sendPacketNoEvent(new C03PacketPlayer(true));
                    }
                    case "NoEventDelayed": {
                        this.sendPacketNoEventDelayed(new C03PacketPlayer(true), ((Double)this.realdelay.getValue()).longValue());
                    }
                    case "playerpacket": {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    }
                }
                this.delay.reset();
                ++i;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean PlayerCheck() {
        if (!(Minecraft.thePlayer.fallDistance <= 2.0f)) return false;
        if (!(Minecraft.thePlayer.getHealth() < (float)((Double)this.health.getValue()).intValue())) return false;
        if (Minecraft.thePlayer.getFoodStats().getFoodLevel() < 19) return false;
        return true;
    }
}

