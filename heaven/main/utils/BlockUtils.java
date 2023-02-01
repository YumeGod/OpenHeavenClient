/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import heaven.main.utils.object.WorldBlockObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class BlockUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static Block getBlock(int x2, int y2, int z2) {
        return Minecraft.theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
    }

    public static Block getBlock(double x2, double y2, double z2) {
        return Minecraft.theWorld.getBlockState(new BlockPos((int)x2, (int)y2, (int)z2)).getBlock();
    }

    public static boolean lookingAtBlock(BlockPos blockFace, float yaw, float pitch, EnumFacing enumFacing, boolean strict) {
        MovingObjectPosition movingObjectPosition = Minecraft.thePlayer.rayTraceCustom(Minecraft.playerController.getBlockReachDistance(), BlockUtils.mc.timer.renderPartialTicks, yaw, pitch);
        if (movingObjectPosition == null) {
            return false;
        }
        Vec3 hitVec = movingObjectPosition.hitVec;
        if (hitVec == null) {
            return false;
        }
        if (hitVec.xCoord - (double)blockFace.getX() > 1.0 || hitVec.xCoord - (double)blockFace.getX() < 0.0) {
            return false;
        }
        if (hitVec.yCoord - (double)blockFace.getY() > 1.0 || hitVec.yCoord - (double)blockFace.getY() < 0.0) {
            return false;
        }
        return !(hitVec.zCoord - (double)blockFace.getZ() > 1.0 || hitVec.zCoord - (double)blockFace.getZ() < 0.0 || movingObjectPosition.sideHit != enumFacing && strict);
    }

    public static WorldBlockObject getWillFallInBlock(double startX, double startY, double startZ) {
        for (double i = startY; i > -1.0; i -= 1.0) {
            Block currentBlock = Minecraft.theWorld.getBlock(startX, i, startZ);
            if (currentBlock instanceof BlockAir) continue;
            return new WorldBlockObject(currentBlock, new BlockPos(startX, i, startZ));
        }
        return new WorldBlockObject(Minecraft.theWorld.getBlock(startX, 0.0, startZ), new BlockPos(startX, 0.0, startZ));
    }

    public static boolean collideBlock2(AxisAlignedBB axisAlignedBB, Collidable collide) {
        int var3 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1;
        for (int x = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX); x < var3; ++x) {
            int var5 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1;
            for (int z = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ); z < var5; ++z) {
                Block block = BlockUtils.getBlock(new BlockPos((double)x, axisAlignedBB.minY, (double)z));
                if (collide.collideBlock(block)) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean collideBlock(AxisAlignedBB axisAlignedBB, Collidable collide) {
        Minecraft.getMinecraft();
        double x = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minX);
        while (true) {
            Minecraft.getMinecraft();
            if (!(x < (double)(MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxX) + 1))) break;
            Minecraft.getMinecraft();
            double z = MathHelper.floor_double(Minecraft.thePlayer.boundingBox.minZ);
            while (true) {
                Minecraft.getMinecraft();
                if (!(z < (double)(MathHelper.floor_double(Minecraft.thePlayer.boundingBox.maxZ) + 1))) break;
                Block block = BlockUtils.getBlock(x, axisAlignedBB.minY, z);
                if (!collide.collideBlock(block)) {
                    return false;
                }
                z += 1.0;
            }
            x += 1.0;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isOnLadder() {
        if (Minecraft.thePlayer == null) {
            return false;
        }
        boolean onLadder = false;
        int y2 = (int)Minecraft.thePlayer.getEntityBoundingBox().offset((double)0.0, (double)1.0, (double)0.0).minY;
        int x2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX);
        while (true) {
            if (x2 >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1) break;
            int z2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ);
            while (true) {
                if (z2 >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1) break;
                Block block = BlockUtils.getBlock(x2, y2, z2);
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                        return false;
                    }
                    onLadder = true;
                }
                ++z2;
            }
            ++x2;
        }
        if (onLadder) return true;
        if (!Minecraft.thePlayer.isOnLadder()) return false;
        return true;
    }

    public static boolean isInsideBlock() {
        int x2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX);
        while (true) {
            if (x2 >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1) break;
            int y2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minY);
            while (true) {
                if (y2 >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxY) + 1) break;
                int z2 = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ);
                while (true) {
                    if (z2 >= MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1) break;
                    Block block = Minecraft.theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(x2, y2, z2), Minecraft.theWorld.getBlockState(new BlockPos(x2, y2, z2)));
                        if (boundingBox != null) {
                            if (Minecraft.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                                return true;
                            }
                        }
                    }
                    ++z2;
                }
                ++y2;
            }
            ++x2;
        }
        return false;
    }

    public static int getBlockID(Block block) {
        return Block.getIdFromBlock(block);
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.theWorld.getBlockState(pos).getBlock();
    }

    public static boolean isInLiquid() {
        return Minecraft.thePlayer.isInWater();
    }

    public static void updateTool(BlockPos pos) {
        Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || !(itemStack.getStrVsBlock(block) > strength)) continue;
            strength = itemStack.getStrVsBlock(block);
            bestItemIndex = i;
        }
        if (bestItemIndex != -1) {
            Minecraft.thePlayer.inventory.currentItem = bestItemIndex;
        }
    }

    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = Minecraft.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.contract(0.01, 0.0, 0.01).offset(0.0, -0.01, 0.0);
        boolean onLiquid = false;
        int y = (int)boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0); ++x) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0); ++z) {
                Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == Blocks.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static boolean isReplaceable() {
        return false;
    }

    public static interface Collidable {
        public boolean collideBlock(Block var1);
    }
}

