/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacket;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Unstuck
extends Module {
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 500.0, 50.0, 1000.0, 50.0);
    private final Option<Boolean> automated = new Option<Boolean>("AutoMated", true);
    private int setbackCount;
    private final TimerUtil timer = new TimerUtil();

    public Unstuck() {
        super("UnStuck", new String[]{"Unstuck", "Unstuck"}, ModuleType.Movement);
        this.addValues(this.delay, this.automated);
    }

    @EventHandler
    public void packetEvent(EventPacket e) {
        if (e.isOutgoing()) {
            if (e.getPacket() instanceof C03PacketPlayer && (this.isStuck() || !((Boolean)this.automated.getValue()).booleanValue())) {
                e.setCancelled(true);
            }
        } else if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.setbackCount = !this.timer.hasReached((Double)this.delay.getValue()) ? ++this.setbackCount : 1;
            this.timer.reset();
        }
    }

    @EventHandler
    public void moveEvent(EventPreUpdate e) {
        if (this.isStuck() || !((Boolean)this.automated.getValue()).booleanValue()) {
            Minecraft.thePlayer.motionX = 0.0;
            EventPreUpdate.setX(0.0);
            Minecraft.thePlayer.motionY = 0.0;
            EventPreUpdate.setY(0.0);
            Minecraft.thePlayer.motionZ = 0.0;
            EventPreUpdate.setZ(0.0);
        }
    }

    private boolean isStuck() {
        return this.setbackCount > 5 && !this.timer.hasReached((Double)this.delay.getValue());
    }
}

