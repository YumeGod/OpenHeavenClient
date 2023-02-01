/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.value.Numbers;
import net.minecraft.client.Minecraft;

public class FastFall
extends Module {
    private final Numbers<Double> distance = new Numbers<Double>("StartDistance", 2.0, 0.1, 10.0, 0.1);
    private final Numbers<Double> fallspeed = new Numbers<Double>("FallSpeed", 2.0, 1.0, 10.0, 0.1);

    public FastFall() {
        super("FastFall", ModuleType.Movement);
        this.addValues(this.distance, this.fallspeed);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() || Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
            return;
        }
        if (Minecraft.thePlayer.fallDistance > ((Double)this.distance.getValue()).floatValue()) {
            FastFall.mc.timer.timerSpeed = ((Double)this.fallspeed.getValue()).floatValue();
        } else if (FastFall.mc.timer.timerSpeed != 1.0f) {
            FastFall.mc.timer.timerSpeed = 1.0f;
        }
    }
}

