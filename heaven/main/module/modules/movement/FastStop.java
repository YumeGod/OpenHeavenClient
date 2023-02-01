/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;

public class FastStop
extends Module {
    private final Option<Boolean> onlyGround = new Option<Boolean>("OnlyGround", false);

    public FastStop() {
        super("FastStop", ModuleType.Movement);
        this.addValues(this.onlyGround);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (!this.isMoving() || !MoveUtils.isMovingKeyBindingActive()) {
            if (((Boolean)this.onlyGround.get()).booleanValue()) {
                if (Minecraft.thePlayer.onGround) {
                    MoveUtils.setSpeed(0.0);
                    Minecraft.thePlayer.motionX = 0.0;
                    Minecraft.thePlayer.motionZ = 0.0;
                }
            } else {
                MoveUtils.setSpeed(0.0);
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
            }
        }
    }
}

