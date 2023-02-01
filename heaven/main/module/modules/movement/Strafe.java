/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventMove;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Speed;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;

public class Strafe
extends Module {
    private final Option<Boolean> fullStrafe = new Option<Boolean>("FullStrafe", false);

    public Strafe() {
        super("Strafe", ModuleType.Movement);
        this.addValues(this.fullStrafe);
    }

    @EventHandler
    private void onUpdate(EventMove e) {
        if (((Boolean)this.fullStrafe.get()).booleanValue()) {
            if (MoveUtils.isMovingKeyBindingActive()) {
                if (!Minecraft.thePlayer.onGround) {
                    MoveUtils.strafe(MoveUtils.getBaseMoveSpeed() * 0.98);
                }
            }
        } else {
            Speed.strafe();
        }
    }
}

