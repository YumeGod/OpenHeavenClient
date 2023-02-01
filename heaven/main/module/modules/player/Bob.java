/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;

public class Bob
extends Module {
    public static final Option<Boolean> realBobbing = new Option<Boolean>("RealBobbing", false);
    private final Numbers<Double> boob = new Numbers<Double>("Amount", 1.0, 0.1, 5.0, 0.1, () -> (Boolean)realBobbing.get() == false);
    public final Option<Boolean> onlyground = new Option<Boolean>("OnlyGround", false, () -> (Boolean)realBobbing.get() == false);

    public Bob() {
        super("Bob", ModuleType.Player);
        this.addValues(realBobbing, this.boob, this.onlyground);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (!((Boolean)realBobbing.get()).booleanValue()) {
            if (((Boolean)this.onlyground.get()).booleanValue()) {
                if (!Minecraft.thePlayer.onGround) {
                    return;
                }
            }
            Minecraft.thePlayer.cameraYaw = (float)(0.09090908616781235 * (Double)this.boob.getValue());
        }
    }
}

