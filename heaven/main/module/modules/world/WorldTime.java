/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class WorldTime
extends Module {
    private final String[] AntiMode = new String[]{"Custom", "Change"};
    private final Mode<String> mode = new Mode("Mode", this.AntiMode, this.AntiMode[0]);
    private final Numbers<Double> time = new Numbers<Double>("Time", 8000.0, 0.0, 24000.0, 1000.0, () -> this.mode.isCurrentMode("Custom"));
    int i;

    public WorldTime() {
        super("WorldTime", ModuleType.World);
        this.addValues(this.mode, this.time);
    }

    @EventHandler
    public final void onReceivePacket(EventPacketReceive event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.setCancelled(true);
        }
        if (this.mode.isCurrentMode("Custom")) {
            Minecraft.theWorld.setWorldTime(((Double)this.time.getValue()).intValue());
        }
        if (this.mode.isCurrentMode("Change")) {
            Minecraft.theWorld.setWorldTime(this.i);
        }
    }

    @EventHandler
    public final void onMotionUpdate(EventTick event) {
        this.setSuffix((Serializable)this.mode.getValue());
        if (this.mode.isCurrentMode("Custom")) {
            Minecraft.theWorld.setWorldTime(((Double)this.time.getValue()).intValue());
        }
        if (this.mode.isCurrentMode("Change")) {
            this.i += 100;
            if (this.i > 24000) {
                this.i = 0;
            }
            Minecraft.theWorld.setWorldTime(this.i);
        }
    }
}

