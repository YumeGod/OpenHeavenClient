/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.optifine.BlockPosM;

public class FastStairs
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"OldAAC", "NCP", "Legit"}, "NCP");

    public FastStairs() {
        super("FastStairs", ModuleType.Movement);
        this.addValues(this.mode);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (!MoveUtils.isMovingKeyBindingActive()) {
            return;
        }
        BlockPosM bp = new BlockPosM(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.3, Minecraft.thePlayer.posZ);
        if (Minecraft.theWorld.getBlockState(bp).getBlock() instanceof BlockStairs) {
            if (this.mode.is("NCP")) {
                MoveUtils.toFwd(0.12);
            }
            if (this.mode.is("OldAAC")) {
                Minecraft.thePlayer.motionX *= 1.53;
                Minecraft.thePlayer.motionZ *= 1.53;
            }
            if (this.mode.is("Legit")) {
                FastStairs.mc.gameSettings.keyBindJump.pressed = true;
            }
        }
    }
}

