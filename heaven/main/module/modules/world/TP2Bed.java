/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.world.TPUtils.AStarCustomPathFinder;
import heaven.main.utils.BedUtil;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class TP2Bed
extends Module {
    public BlockPos playerBed;
    public BlockPos fuckingBed;
    public ArrayList<BlockPos> posList;
    public BedUtil bed;
    final TimerUtil timer;
    public final Numbers<Double> delay = new Numbers<Double>("Delay", 600.0, 200.0, 3000.0, 100.0);
    public final Numbers<Double> packetDist = new Numbers<Double>("PacketDist", 20.0, 10.0, 30.0, 1.0);
    private ArrayList<Vec3> path;

    public TP2Bed() {
        super("TPBed", new String[]{"TPBed"}, ModuleType.World);
        this.addValues(this.delay, this.packetDist);
        this.timer = new TimerUtil();
        this.path = new ArrayList();
    }

    @Override
    public void onEnable() {
        try {
            if (this.bed != null) {
                this.posList = new ArrayList<BlockPos>(this.bed.list);
                this.posList.sort(TP2Bed::lambda$onEnable$0);
                if (this.posList.size() != 4) {
                    Helper.sendMessage("> [Warning]Not in BedWars Beds! Bed: " + this.posList.size());
                }
                this.playerBed = this.posList.get(0);
                this.posList.remove(this.playerBed);
                this.fuckingBed = this.posList.get(0);
                Helper.sendMessage("> playerBed :" + this.playerBed.toString() + " fuckingBed :" + this.fuckingBed.toString());
            }
        }
        catch (Exception e) {
            Helper.sendMessage("> [Warning]Error TPBed? for " + e);
        }
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onRender(EventRender3D eventRender) {
        try {
            for (Vec3 Vec32 : this.path) {
                double n = Vec32.getX() - TP2Bed.mc.getRenderManager().viewerPosX;
                double n2 = Vec32.getY() - TP2Bed.mc.getRenderManager().viewerPosY;
                RenderUtil.drawEntityESP(n, n2, Vec32.getZ() - TP2Bed.mc.getRenderManager().viewerPosZ, Minecraft.thePlayer.getEntityBoundingBox().maxX - Minecraft.thePlayer.getEntityBoundingBox().minX, Minecraft.thePlayer.getEntityBoundingBox().maxY - Minecraft.thePlayer.getEntityBoundingBox().minY + 0.25, 0.0f, 1.0f, 0.0f, 0.2f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate eventUpdate) {
        try {
            if (this.posList != null) {
                if (this.posList.isEmpty()) {
                    Helper.sendMessage("[TPBed] Bed is empty");
                    this.setEnabled(true);
                    return;
                }
                for (BlockPos blockPos : this.posList) {
                    if (Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockBed) continue;
                    Helper.sendMessage("> Destory!" + blockPos);
                    this.posList.remove(blockPos);
                    this.posList.sort(TP2Bed::lambda$onUpdate$1);
                    this.fuckingBed = this.posList.get(0);
                }
                if (Minecraft.thePlayer.getDistance(this.fuckingBed.getX(), this.fuckingBed.getY(), this.fuckingBed.getZ()) < 4.0) {
                    Helper.sendMessage("> Teleported! :3");
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.fuckingBed, EnumFacing.NORTH));
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.fuckingBed, EnumFacing.NORTH));
                    return;
                }
                if (this.timer.hasReached((Double)this.delay.getValue())) {
                    if (Minecraft.thePlayer.isCollidedVertically) {
                        if (Minecraft.thePlayer.onGround) {
                            for (int i = 0; i <= 49; ++i) {
                                Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.062, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
                                Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
                            }
                            Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
                            Minecraft.thePlayer.onGround = false;
                            MoveUtils.setSpeed(0.0);
                            Minecraft.thePlayer.jumpMovementFactor = 0.0f;
                        }
                    }
                    this.path = this.computePath(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ), new Vec3(this.fuckingBed.getX() + 1, this.fuckingBed.getY(), this.fuckingBed.getZ() + 1));
                    if (Minecraft.thePlayer.getDistance(this.fuckingBed.getX(), this.fuckingBed.getY(), this.fuckingBed.getZ()) > 4.0) {
                        Helper.sendMessage("> Trying to teleport...");
                        for (Vec3 Vec32 : this.path) {
                            Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Vec32.getX(), Vec32.getY(), Vec32.getZ(), true));
                        }
                    }
                    this.timer.reset();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getDistanceToBlock(BlockPos blockPos) {
        return Minecraft.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    private static boolean canPassThrow(BlockPos blockPos) {
        Minecraft.getMinecraft();
        Block getBlock = Minecraft.theWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ())).getBlock();
        return getBlock.getMaterial() == Material.air || getBlock.getMaterial() == Material.plants || getBlock.getMaterial() == Material.vine || getBlock == Blocks.ladder || getBlock == Blocks.water || getBlock == Blocks.flowing_water || getBlock == Blocks.wall_sign || getBlock == Blocks.standing_sign;
    }

    private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!TP2Bed.canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
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
                if (pathElm.squareDistanceTo(lastDashLoc) > (Double)this.packetDist.getValue()) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    int x = (int)smallX;
                    block1: while ((double)x <= bigX) {
                        int y = (int)smallY;
                        while ((double)y <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
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

    private static int lambda$onUpdate$1(BlockPos blockPos, BlockPos blockPos2) {
        return (int)(TP2Bed.getDistanceToBlock(blockPos) - TP2Bed.getDistanceToBlock(blockPos2));
    }

    private static int lambda$onEnable$0(BlockPos blockPos, BlockPos blockPos2) {
        return (int)(TP2Bed.getDistanceToBlock(blockPos) - TP2Bed.getDistanceToBlock(blockPos2));
    }
}

