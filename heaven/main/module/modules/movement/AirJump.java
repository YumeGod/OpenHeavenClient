/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;

public class AirJump
extends Module {
    public AirJump() {
        super("AirJump", ModuleType.Movement);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (!Minecraft.thePlayer.onGround && AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            EventPreUpdate.setOnGround(true);
            Minecraft.thePlayer.onGround = true;
        }
    }
}

