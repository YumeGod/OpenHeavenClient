/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class AutoDoor
extends Module {
    public AutoDoor() {
        super("AutoDoor", ModuleType.Player);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        double yaw = MoveUtils.getDirection();
        double x = Minecraft.thePlayer.posX + -Math.sin(yaw) * 1.0;
        double z = Minecraft.thePlayer.posZ + Math.cos(yaw) * 1.0;
        double y = Minecraft.thePlayer.posY;
        BlockPos pos = new BlockPos(x, y, z);
        Block b = Minecraft.theWorld.getBlockState(pos).getBlock();
        if (b instanceof BlockDoor) {
            if (!BlockDoor.isOpen(Minecraft.theWorld, pos)) {
                Minecraft.thePlayer.swingItem();
                Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), pos, AutoDoor.mc.objectMouseOver.sideHit, AutoDoor.mc.objectMouseOver.hitVec);
            }
        }
    }
}

