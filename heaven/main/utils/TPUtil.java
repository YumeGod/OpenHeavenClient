/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import heaven.main.utils.TPUtil;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class TPUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static ArrayList<Vec3> computePath(Vec3 from, Vec3 to, double dashDistance, boolean isTeleport) {
        if (!TPUtil.canPassThrow(new BlockPos(from))) {
            from = from.addVector(0.0, 1.0, 0.0);
        }
        TPUtil.1 pathfinder = new TPUtil.1(from, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                double d = pathElm.squareDistanceTo(lastDashLoc);
                double d2 = isTeleport ? dashDistance : dashDistance * dashDistance;
                if (d > d2) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.xCoord, pathElm.xCoord);
                    double smallY = Math.min(lastDashLoc.yCoord, pathElm.yCoord);
                    double smallZ = Math.min(lastDashLoc.zCoord, pathElm.zCoord);
                    double bigX = Math.max(lastDashLoc.xCoord, pathElm.xCoord);
                    double bigY = Math.max(lastDashLoc.yCoord, pathElm.yCoord);
                    double bigZ = Math.max(lastDashLoc.zCoord, pathElm.zCoord);
                    int x = (int)smallX;
                    block1: while ((double)x <= bigX) {
                        int y = (int)smallY;
                        while ((double)y <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!TPUtil.1.checkPositionValidity(x, y, z)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y;
                        }
                        ++x;
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    private static boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.theWorld.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() != Material.air && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine && block != Blocks.ladder && block != Blocks.water && block != Blocks.flowing_water && block != Blocks.wall_sign && block != Blocks.standing_sign;
    }

    static /* synthetic */ Minecraft access$000() {
        return mc;
    }
}

