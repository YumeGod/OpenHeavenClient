/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world.TPUtils;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class AStarCustomPathFinder {
    private final Vec3 startVec3;
    private final Vec3 endVec3;
    private ArrayList<Vec3> path = new ArrayList();
    private final ArrayList<Hub> hubs = new ArrayList();
    private final ArrayList<Hub> hubsToWork = new ArrayList();
    private final double minDistanceSquared;
    private final boolean nearest;
    private static final Vec3[] flatCardinalDirections = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0)};

    public AStarCustomPathFinder(Vec3 Vec32, Vec3 class149) {
        this.minDistanceSquared = 9.0;
        this.nearest = true;
        this.startVec3 = Vec32.addVector(0.0, 0.0, 0.0).floor();
        this.endVec3 = class149.addVector(0.0, 0.0, 0.0).floor();
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void compute() {
        this.compute(1000, 4);
    }

    public void compute(int n, int n2) {
        this.path.clear();
        this.hubsToWork.clear();
        ArrayList<Vec3> list = new ArrayList<Vec3>();
        list.add(this.startVec3);
        this.hubsToWork.add(new Hub(this.startVec3, null, list, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));
        block0: for (int i = 0; i < n; ++i) {
            this.hubsToWork.sort(new ComparatorHub());
            int n3 = 0;
            if (this.hubsToWork.isEmpty()) break;
            for (Hub Hub2 : new ArrayList<Hub>(this.hubsToWork)) {
                Vec3 floor3;
                if (++n3 > n2) continue block0;
                this.hubsToWork.remove(Hub2);
                this.hubs.add(Hub2);
                Vec3[] flatCardinalDirections = AStarCustomPathFinder.flatCardinalDirections;
                int length = flatCardinalDirections.length;
                for (int j = 0; j < length; ++j) {
                    Vec3 floor = Hub2.getLoc().add(flatCardinalDirections[j]).floor();
                    if (this.checkPositionValidity(floor, false) && this.addHub(Hub2, floor, 0.0)) break block0;
                }
                Vec3 floor2 = Hub2.getLoc().addVector(0.0, 1.0, 0.0).floor();
                if ((!this.checkPositionValidity(floor2, false) || !this.addHub(Hub2, floor2, 0.0)) && (!this.checkPositionValidity(floor3 = Hub2.getLoc().addVector(0.0, -1.0, 0.0).floor(), false) || !this.addHub(Hub2, floor3, 0.0))) continue;
                break block0;
            }
        }
        if (this.nearest) {
            this.hubs.sort(new ComparatorHub());
            this.path = this.hubs.get(0).getPath();
        }
    }

    public boolean checkPositionValidity(Vec3 Vec32, boolean b) {
        return AStarCustomPathFinder.checkPositionValidity((int)Vec32.getX(), (int)Vec32.getY(), (int)Vec32.getZ(), b);
    }

    public static boolean checkPositionValidity(int n, int n2, int n3, boolean b) {
        BlockPos blockPos = new BlockPos(n, n2, n3);
        BlockPos blockPos2 = new BlockPos(n, n2 + 1, n3);
        BlockPos blockPos3 = new BlockPos(n, n2 - 1, n3);
        return !AStarCustomPathFinder.isBlockSolid(blockPos) && !AStarCustomPathFinder.isBlockSolid(blockPos2) && (AStarCustomPathFinder.isBlockSolid(blockPos3) || !b) && AStarCustomPathFinder.isSafeToWalkOn(blockPos3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean isBlockSolid(BlockPos blockPos) {
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock().isFullBlock()) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockSlab) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockStairs) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockCactus) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockChest) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockEnderChest) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockSkull) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockPane) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockFence) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockWall) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockGlass) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockPistonBase) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockPistonExtension) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockPistonMoving) return true;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockStainedGlass) return true;
        Minecraft.getMinecraft();
        if (!(Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockTrapDoor)) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean isSafeToWalkOn(BlockPos blockPos) {
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockFence) return false;
        Minecraft.getMinecraft();
        if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockWall) return false;
        return true;
    }

    public Hub isHubExisting(Vec3 Vec32) {
        for (Hub class149 : this.hubs) {
            if (class149.getLoc().getX() != Vec32.getX() || class149.getLoc().getY() != Vec32.getY() || class149.getLoc().getZ() != Vec32.getZ()) continue;
            return class149;
        }
        for (Hub class150 : this.hubsToWork) {
            if (class150.getLoc().getX() != Vec32.getX() || class150.getLoc().getY() != Vec32.getY() || class150.getLoc().getZ() != Vec32.getZ()) continue;
            return class150;
        }
        return null;
    }

    public boolean addHub(Hub parent, Vec3 loc, double cost) {
        Hub hubExisting = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost += parent.getTotalCost();
        }
        if (hubExisting == null) {
            if (loc.getX() == this.endVec3.getX() && loc.getY() == this.endVec3.getY() && loc.getZ() == this.endVec3.getZ() || this.minDistanceSquared != 0.0 && loc.squareDistanceTo(this.endVec3) <= this.minDistanceSquared) {
                this.path.clear();
                this.path = parent.getPath();
                this.path.add(loc);
                return true;
            }
            ArrayList<Vec3> list = new ArrayList<Vec3>(parent.getPath());
            list.add(loc);
            this.hubsToWork.add(new Hub(loc, parent, list, loc.squareDistanceTo(this.endVec3), cost, totalCost));
        } else if (hubExisting.getCost() > cost) {
            ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            hubExisting.setLoc(loc);
            hubExisting.setParent(parent);
            hubExisting.setPath(path);
            hubExisting.setSquareDistanceToFromTarget(loc.squareDistanceTo(this.endVec3));
            hubExisting.setCost(cost);
            hubExisting.setTotalCost(totalCost);
        }
        return false;
    }

    private static class Hub {
        private Vec3 loc;
        private Hub parent;
        private ArrayList<Vec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(Vec3 loc, Hub parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public Vec3 getLoc() {
            return this.loc;
        }

        public Hub getParent() {
            return this.parent;
        }

        public ArrayList<Vec3> getPath() {
            return this.path;
        }

        public double getSquareDistanceToFromTarget() {
            return this.squareDistanceToFromTarget;
        }

        public double getCost() {
            return this.cost;
        }

        public void setLoc(Vec3 loc) {
            this.loc = loc;
        }

        public void setParent(Hub parent) {
            this.parent = parent;
        }

        public void setPath(ArrayList<Vec3> path) {
            this.path = path;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getTotalCost() {
            return this.totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }

    public class ComparatorHub
    implements Comparator<Hub> {
        @Override
        public int compare(Hub Hub2, Hub class94) {
            return (int)(Hub2.getSquareDistanceToFromTarget() + Hub2.getTotalCost() - (class94.getSquareDistanceToFromTarget() + class94.getTotalCost()));
        }
    }
}

