/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AutoBreak
extends Module {
    private EnumFacing facing;
    private TimerUtil skipCheckTimer;
    static final double max;
    private static final Numbers<Double> blockhight;
    private final Option<Boolean> auto = new Option<Boolean>("BetterBreak", true);

    public AutoBreak() {
        super("AutoBreak", new String[]{"automine"}, ModuleType.World);
        this.addValues(this.auto, blockhight);
    }

    @Override
    public void onEnable() {
        if (((Boolean)this.auto.getValue()).booleanValue()) {
            this.facing = EnumFacing.fromAngle(Minecraft.thePlayer.rotationYaw);
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        AutoBreak.mc.gameSettings.keyBindForward.pressed = false;
        AutoBreak.mc.gameSettings.keyBindAttack.pressed = false;
        super.onDisable();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (((Boolean)this.auto.getValue()).booleanValue()) {
            double posX = Minecraft.thePlayer.posX;
            double posY = Minecraft.thePlayer.posY;
            double y2 = posY + (double)Minecraft.thePlayer.getEyeHeight();
            BlockPos toFace = new BlockPos(posX, y2, Minecraft.thePlayer.posZ).offset(this.facing);
            boolean foundItem = false;
            boolean foundOre = false;
            if (this.skipCheckTimer.hasReached(5000.0)) {
                IBlockState stateUnder = this.getBlockState(this.getBlockPosRelativeToEntity(Minecraft.thePlayer, -0.01));
                EntityItem theItem = null;
                for (EntityItem item : this.getNearbyItems(5)) {
                    if (!Minecraft.thePlayer.canEntityBeSeen(item) || item.ticksExisted <= 20 || item.ticksExisted >= 150) continue;
                    foundItem = true;
                    theItem = item;
                }
                if (foundItem) {
                    this.faceEntity(theItem);
                    Minecraft.thePlayer.moveFlying(0.0f, 1.0f, 0.1f);
                    double posY2 = theItem.posY;
                    if (posY2 > Minecraft.thePlayer.posY) {
                        if (Minecraft.thePlayer.onGround) {
                            Minecraft.thePlayer.jump();
                        }
                    }
                } else {
                    for (int x = -3; x <= 3; ++x) {
                        block2: for (int y = -3; y <= 5; ++y) {
                            for (int z = -3; z <= 3; ++z) {
                                BlockPos blockPosTrace;
                                MovingObjectPosition trace0;
                                double x2 = Minecraft.thePlayer.posX + (double)x;
                                double y3 = Minecraft.thePlayer.posY + (double)y;
                                BlockPos blockPos = new BlockPos(x2, y3, Minecraft.thePlayer.posZ + (double)z);
                                Block block = this.getBlock(blockPos);
                                IBlockState state = this.getBlockState(blockPos);
                                if (state.getBlock().getMaterial() == Material.air) continue;
                                if (block instanceof BlockLiquid) {
                                    WorldClient theWorld = Minecraft.theWorld;
                                    double posX2 = Minecraft.thePlayer.posX;
                                    double posY3 = Minecraft.thePlayer.posY;
                                    double y4 = posY3 + (double)Minecraft.thePlayer.getEyeHeight();
                                    trace0 = theWorld.rayTraceBlocks(new Vec3(posX2, y4, Minecraft.thePlayer.posZ), new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5), true, false, true);
                                    if (trace0.getBlockPos() == null) continue;
                                    blockPosTrace = trace0.getBlockPos();
                                    Block blockTrace = this.getBlock(blockPosTrace);
                                    if (blockTrace instanceof BlockLiquid) {
                                        this.facing = this.facing.getOpposite();
                                        double posX3 = Minecraft.thePlayer.posX;
                                        double posY4 = Minecraft.thePlayer.posY;
                                        double y5 = posY4 + (double)Minecraft.thePlayer.getEyeHeight();
                                        toFace = new BlockPos(posX3, y5, Minecraft.thePlayer.posZ).offset(this.facing);
                                        if (this.isBlockPosAir(toFace)) {
                                            toFace = toFace.down();
                                        }
                                        this.skipCheckTimer.reset();
                                        continue block2;
                                    }
                                }
                                if (!(block instanceof BlockOre) && !(block instanceof BlockRedstoneOre)) continue;
                                WorldClient theWorld2 = Minecraft.theWorld;
                                double posX4 = Minecraft.thePlayer.posX;
                                double posY5 = Minecraft.thePlayer.posY;
                                double y6 = posY5 + (double)Minecraft.thePlayer.getEyeHeight();
                                trace0 = theWorld2.rayTraceBlocks(new Vec3(posX4, y6, Minecraft.thePlayer.posZ), new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5), true, false, true);
                                if (trace0.getBlockPos() == null) continue;
                                blockPosTrace = trace0.getBlockPos();
                                double dist = this.getVec3(blockPos).distanceTo(this.getVec3(blockPosTrace));
                                double dist2 = Math.sqrt(x * x + y * y + z * z);
                                if (dist2 >= 1.5) {
                                    float yaw = this.getFacePos(this.getVec3(blockPosTrace))[0];
                                    if ((yaw = this.normalizeAngle(yaw)) == 45.0f || yaw == -45.0f || yaw == 135.0f || yaw == -135.0f) continue;
                                }
                                if (!(dist <= 0.0)) continue;
                                toFace = blockPosTrace;
                                foundOre = true;
                                continue block2;
                            }
                        }
                    }
                    if (foundOre) {
                        this.faceBlock(toFace);
                        if (!this.isBlockPosAir(toFace)) {
                            this.mineBlock(toFace);
                        }
                    } else if (Minecraft.thePlayer.posY > (Double)blockhight.getValue()) {
                        if (stateUnder.getBlock().getMaterial() != Material.air) {
                            this.mineBlockUnderPlayer();
                        } else if (Minecraft.thePlayer.onGround) {
                            Minecraft.thePlayer.moveFlying(0.0f, 0.5f, 0.1f);
                        }
                    } else {
                        if (this.isBlockPosAir(toFace)) {
                            if (this.isBlockPosSafe(toFace = toFace.down())) {
                                if (this.isBlockPosAir(toFace)) {
                                    if (Minecraft.thePlayer.onGround) {
                                        Minecraft.thePlayer.moveFlying(0.0f, 1.0f, 0.1f);
                                    }
                                }
                            } else {
                                this.facing = EnumFacing.fromAngle(Minecraft.thePlayer.rotationYaw + 90.0f);
                                double posX5 = Minecraft.thePlayer.posX;
                                double posY7 = Minecraft.thePlayer.posY;
                                double y7 = posY7 + (double)Minecraft.thePlayer.getEyeHeight();
                                toFace = new BlockPos(posX5, y7, Minecraft.thePlayer.posZ).offset(this.facing);
                                if (!this.isBlockPosSafe(toFace)) {
                                    this.facing = EnumFacing.fromAngle(Minecraft.thePlayer.rotationYaw - 90.0f);
                                    double posX6 = Minecraft.thePlayer.posX;
                                    double posY8 = Minecraft.thePlayer.posY;
                                    double y8 = posY8 + (double)Minecraft.thePlayer.getEyeHeight();
                                    toFace = new BlockPos(posX6, y8, Minecraft.thePlayer.posZ).offset(this.facing);
                                    if (!this.isBlockPosSafe(toFace.down())) {
                                        this.facing = EnumFacing.fromAngle(Minecraft.thePlayer.rotationYaw + 180.0f);
                                        double posX7 = Minecraft.thePlayer.posX;
                                        double posY9 = Minecraft.thePlayer.posY;
                                        double y9 = posY9 + (double)Minecraft.thePlayer.getEyeHeight();
                                        toFace = new BlockPos(posX7, y9, Minecraft.thePlayer.posZ).offset(this.facing);
                                    }
                                }
                            }
                        }
                        if (this.isBlockPosAir(toFace)) {
                            double posX8 = Minecraft.thePlayer.posX;
                            toFace = new BlockPos(posX8, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ).offset(this.facing);
                        }
                        this.faceBlock(toFace);
                        if (!this.isBlockPosAir(toFace)) {
                            this.mineBlock(toFace);
                        }
                    }
                }
            } else {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.moveFlying(0.0f, 1.0f, 0.1f);
                }
                if (this.isBlockPosAir(toFace)) {
                    toFace = toFace.down();
                }
                this.faceBlock(toFace);
                if (!this.isBlockPosAir(toFace)) {
                    this.mineBlock(toFace);
                }
            }
        } else {
            AutoBreak.mc.gameSettings.keyBindForward.pressed = true;
            AutoBreak.mc.gameSettings.keyBindAttack.pressed = true;
        }
    }

    public void mineBlockUnderPlayer() {
        BlockPos pos = this.getBlockPosRelativeToEntity(Minecraft.thePlayer, -0.01);
        this.mineBlock(pos);
    }

    public void mineBlock(BlockPos pos) {
        this.faceBlock(pos);
        Minecraft.thePlayer.swingItem();
        Minecraft.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
    }

    public boolean isBlockPosSafe(BlockPos pos) {
        return this.checkBlockPos(pos, 10);
    }

    public boolean checkBlockPos(BlockPos pos, int checkHeight) {
        boolean safe = true;
        boolean blockInWay = false;
        int fallDist = 0;
        if (this.getBlock(pos).getMaterial() == Material.lava || this.getBlock(pos).getMaterial() == Material.water) {
            return false;
        }
        if (this.getBlock(pos.up(1)).getMaterial() == Material.lava || this.getBlock(pos.up(1)).getMaterial() == Material.water) {
            return false;
        }
        if (this.getBlock(pos.up(2)).getMaterial() == Material.lava || this.getBlock(pos.up(2)).getMaterial() == Material.water) {
            return false;
        }
        for (int i = 1; i < checkHeight + 1; ++i) {
            BlockPos pos2 = pos.down(i);
            Block block = this.getBlock(pos2);
            if (block.getMaterial() == Material.air) {
                if (blockInWay) continue;
                ++fallDist;
                continue;
            }
            if (!(blockInWay || block.getMaterial() != Material.lava && block.getMaterial() != Material.water)) {
                return false;
            }
            if (blockInWay) continue;
            blockInWay = true;
        }
        if (fallDist > 2) {
            safe = false;
        }
        return safe;
    }

    public Block getBlock(BlockPos pos) {
        return Minecraft.theWorld.getBlockState(pos).getBlock();
    }

    public BlockPos getBlockPosRelativeToEntity(Entity en, double d) {
        return new BlockPos(en.posX, en.posY + d, en.posZ);
    }

    public IBlockState getBlockState(BlockPos blockPos) {
        return Minecraft.theWorld.getBlockState(blockPos);
    }

    public ArrayList<EntityItem> getNearbyItems(int range) {
        ArrayList<EntityItem> eList = new ArrayList<EntityItem>();
        for (Entity o : Minecraft.theWorld.getLoadedEntityList()) {
            EntityItem e = (EntityItem)o;
            if (!(o instanceof EntityItem)) continue;
            if (Minecraft.thePlayer.getDistanceToEntity(e) >= (float)range) continue;
            eList.add(e);
        }
        return eList;
    }

    public void faceEntity(Entity en) {
        this.facePos(new Vec3(en.posX - 0.5, en.posY + ((double)en.getEyeHeight() - (double)en.height / 1.5), en.posZ - 0.5));
    }

    public void facePos(Vec3 vec) {
        double n = vec.xCoord + 0.5;
        double diffX = n - Minecraft.thePlayer.posX;
        double n2 = vec.yCoord + 0.5;
        double posY = Minecraft.thePlayer.posY;
        double diffY = n2 - (posY + (double)Minecraft.thePlayer.getEyeHeight());
        double n3 = vec.zCoord + 0.5;
        double diffZ = n3 - Minecraft.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        Minecraft.thePlayer.rotationYaw = rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        float rotationPitch = Minecraft.thePlayer.rotationPitch;
        Minecraft.thePlayer.rotationPitch = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
    }

    public boolean isBlockPosAir(BlockPos blockPos) {
        return Minecraft.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
    }

    public Vec3 getVec3(BlockPos blockPos) {
        return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public float[] getFacePos(Vec3 vec) {
        double n = vec.xCoord + 0.5;
        double diffX = n - Minecraft.thePlayer.posX;
        double n2 = vec.yCoord + 0.5;
        double posY = Minecraft.thePlayer.posY;
        double diffY = n2 - (posY + (double)Minecraft.thePlayer.getEyeHeight());
        double n3 = vec.zCoord + 0.5;
        double diffZ = n3 - Minecraft.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        float[] array = new float[2];
        int n4 = 0;
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        array[n4] = rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        int n6 = 1;
        float rotationPitch = Minecraft.thePlayer.rotationPitch;
        array[n6] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return array;
    }

    public float normalizeAngle(float angle) {
        return (angle + 360.0f) % 360.0f;
    }

    public void faceBlock(BlockPos blockPos) {
        this.facePos(this.getVec3(blockPos));
    }

    static {
        int height = Minecraft.theWorld == null ? 256 : Minecraft.theWorld.getHeight();
        max = height;
        blockhight = new Numbers<Double>("Reach", 16.0, 1.0, max, 1.0);
    }
}

