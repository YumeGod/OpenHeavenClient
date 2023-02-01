/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventCollideWithBlock;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;

public class AntiCactus
extends Module {
    public AntiCactus() {
        super("AntiCactus", ModuleType.Player);
    }

    @EventHandler
    public void onBoundingBox(EventCollideWithBlock event) {
        if (event.getBlock() instanceof BlockCactus) {
            event.setBoundingBox(new AxisAlignedBB(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getPos().getX() + 1, event.getPos().getY() + 1, event.getPos().getZ() + 1));
        }
    }
}

