/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventCollideWithBlock;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Option;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockWeb;
import net.minecraft.util.AxisAlignedBB;

public class BlockWalk
extends Module {
    private final Option<Boolean> snow = new Option<Boolean>("Snow", false);
    private final Option<Boolean> web = new Option<Boolean>("Web", true);

    public BlockWalk() {
        super("BlockWalk", ModuleType.Movement);
        this.addValues(this.snow, this.web);
    }

    @EventHandler
    public void onCollideWithBlock(EventCollideWithBlock e) {
        if (e.getBlock() instanceof BlockSnow && ((Boolean)this.snow.getValue()).booleanValue()) {
            e.setBoundingBox(new AxisAlignedBB(e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ(), e.getBlockPos().getX() + 1, e.getBlockPos().getY() + 1, e.getBlockPos().getZ() + 1));
        }
        if (e.getBlock() instanceof BlockWeb && ((Boolean)this.web.getValue()).booleanValue()) {
            e.setBoundingBox(new AxisAlignedBB(e.getBlockPos().getX(), e.getBlockPos().getY(), e.getBlockPos().getZ(), e.getBlockPos().getX() + 1, e.getBlockPos().getY() + 1, e.getBlockPos().getZ() + 1));
        }
    }
}

