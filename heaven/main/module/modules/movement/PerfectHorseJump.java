/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;

public class PerfectHorseJump
extends Module {
    public PerfectHorseJump() {
        super("PerfectHorseJump", ModuleType.Movement);
    }

    @EventHandler
    public void onTick(EventTick e) {
        Minecraft.thePlayer.horseJumpPowerCounter = 9;
        Minecraft.thePlayer.horseJumpPower = 1.0f;
    }
}

