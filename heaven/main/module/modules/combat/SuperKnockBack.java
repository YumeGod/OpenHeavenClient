/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventAttack;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class SuperKnockBack
extends Module {
    private final Numbers<Double> maxdelay = new Numbers<Double>("MaxDelay", 0.0, 0.0, 2000.0, 0.1);
    private final Numbers<Double> mindelay = new Numbers<Double>("Delay", 0.0, 0.0, 2000.0, 0.1);
    private final Option<Boolean> usepacket = new Option<Boolean>("UsePacket", false);
    private final Numbers<Double> packets = new Numbers<Double>("Packets", 100.0, 50.0, 2000.0, 50.0, this.usepacket::getValue);
    private final Numbers<Double> maxptd = new Numbers<Double>("MaxPacketTriggerDistance", 1.0, 0.0, 6.0, 0.1, this.usepacket::getValue);
    private final Numbers<Double> minptd = new Numbers<Double>("MinPacketTriggerDistance", 1.0, 0.0, 6.0, 0.1, this.usepacket::getValue);

    public SuperKnockBack() {
        super("SuperKnockBack", new String[]{"superkb"}, ModuleType.Combat);
        this.addValues(this.maxdelay, this.mindelay, this.usepacket, this.packets, this.maxptd, this.minptd);
    }

    @EventHandler
    public void onAttack(EventAttack e) {
        block5: {
            double value;
            block6: {
                if (!((Boolean)this.usepacket.getValue()).booleanValue()) break block5;
                if ((double)Minecraft.thePlayer.getDistanceToEntity(e.getEntity()) <= MathUtil.randomNumber((Double)this.maxptd.getValue(), (Double)this.minptd.getValue())) break block6;
                if (!Minecraft.thePlayer.getEntityBoundingBox().intersectsWith(e.getEntity().getEntityBoundingBox())) break block5;
            }
            int i = 0;
            while ((double)i < (value = ((Double)this.packets.getValue()).doubleValue())) {
                if (Minecraft.thePlayer.onGround) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer());
                }
                ++i;
            }
        }
    }

    @EventHandler
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof C02PacketUseEntity && TimerUtils.hasReached(MathUtil.randomNumber((Double)this.maxdelay.getValue(), (Double)this.mindelay.getValue()))) {
            if (Minecraft.thePlayer.isSprinting() && SuperKnockBack.mc.gameSettings.keyBindForward.isKeyDown()) {
                Minecraft.thePlayer.setSprinting(false);
            }
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            Minecraft.thePlayer.setSprinting(true);
            Minecraft.thePlayer.serverSprintState = true;
            TimerUtils.reset();
        }
    }
}

