/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;

public class Parkour
extends Module {
    public Parkour() {
        super("Parkour", ModuleType.Movement);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (Minecraft.thePlayer.moving()) {
            if (Minecraft.thePlayer.onGround) {
                if (!(Minecraft.thePlayer.isSneaking() || Parkour.mc.gameSettings.keyBindSneak.isKeyDown() || Parkour.mc.gameSettings.keyBindJump.isKeyDown())) {
                    if (Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty()) {
                        Minecraft.thePlayer.jump();
                    }
                }
            }
        }
    }
}

