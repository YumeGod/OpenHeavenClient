/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;

public class AirLadder
extends Module {
    public AirLadder() {
        super("AirLadder", ModuleType.Movement);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (Minecraft.thePlayer.isOnLadder() && AirLadder.mc.gameSettings.keyBindJump.isKeyDown()) {
            Minecraft.thePlayer.motionY = 0.11;
        }
    }
}

