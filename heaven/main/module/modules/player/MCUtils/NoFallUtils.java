/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player.MCUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class NoFallUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int getJumpEffect() {
        return Minecraft.thePlayer.isPotionActive(Potion.jump) ? Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0;
    }

    public static boolean isOnLiquid() {
        if (Minecraft.thePlayer == null) {
            return false;
        }
        boolean onLiquid = false;
        int y = (int)Minecraft.thePlayer.getEntityBoundingBox().offset((double)0.0, (double)-0.01, (double)0.0).minY;
        int x = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX);
        while (true) {
            if (x >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1) break;
            int z = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ);
            while (true) {
                if (z >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1) break;
                Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
                ++z;
            }
            ++x;
        }
        return onLiquid;
    }

    public static boolean isBlockUnder() {
        int offset = 0;
        while (true) {
            if (!((double)offset < Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight())) break;
            AxisAlignedBB boundingBox = Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
            offset += 2;
        }
        return false;
    }
}

