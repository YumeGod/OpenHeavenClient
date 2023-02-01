/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player.DodgeUtils;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Utils {
    public static BlockPos getBlockPos(Vec3 vec) {
        return new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public static boolean isBlockPosAir(BlockPos blockPos) {
        Minecraft.getMinecraft();
        return Minecraft.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
    }
}

