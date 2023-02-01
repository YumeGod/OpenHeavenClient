/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.BlockUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.Minecraft;

public class SlimeJump
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Add", "Set"}, "Add");
    private final Numbers<Double> Motion = new Numbers<Double>("Motion", 0.5, 0.0, 5.0, 0.1);

    public SlimeJump() {
        super("SlimeJump", ModuleType.Movement);
        this.addValues(this.mode, this.Motion);
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (Minecraft.thePlayer != null) {
            if (Minecraft.theWorld != null) {
                if (BlockUtils.getBlock(Minecraft.thePlayer.getPosition().down()) instanceof BlockSlime) {
                    if (this.mode.is("Add")) {
                        Minecraft.thePlayer.motionY += ((Double)this.Motion.getValue()).doubleValue();
                    } else if (this.mode.is("Set")) {
                        Minecraft.thePlayer.motionY = (Double)this.Motion.getValue();
                    }
                }
            }
        }
    }
}

