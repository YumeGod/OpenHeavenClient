/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.skid;

import heaven.main.event.EventHandler;
import heaven.main.event.events.More.PacketEvent;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.modules.world.ScaffoldUtils.Rotation;
import heaven.main.utils.shader.MinecraftInstance;
import heaven.main.utils.vec.VecRotation;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class RotationUtils
extends MinecraftInstance {
    private static final Random random = new Random();
    private static int keepLength;
    private static int revTick;
    public static Rotation targetRotation;
    public static Rotation serverRotation;
    public static boolean keepCurrentRotation;
    private static double x;
    private static double y;
    private static double z;

    public static VecRotation faceBlock(BlockPos blockPos) {
        if (blockPos == null) {
            return null;
        }
        VecRotation vecRotation = null;
        for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
            for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                    Vec3 eyesPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
                    Vec3 posVec = new Vec3(blockPos).addVector(xSearch, ySearch, zSearch);
                    double dist = eyesPos.distanceTo(posVec);
                    double diffX = posVec.xCoord - eyesPos.xCoord;
                    double diffY = posVec.yCoord - eyesPos.yCoord;
                    double diffZ = posVec.zCoord - eyesPos.zCoord;
                    double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
                    Rotation rotation = new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)))));
                    Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                    Vec3 vector = eyesPos.addVector(rotationVector.xCoord * dist, rotationVector.yCoord * dist, rotationVector.zCoord * dist);
                    MovingObjectPosition obj = Minecraft.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);
                    if (obj.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) continue;
                    VecRotation currentVec = new VecRotation(posVec, rotation);
                    if (vecRotation != null && !(RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static Rotation getRotationsEntity(EntityLivingBase entity) {
        return RotationUtils.getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }

    public static Rotation getRotationsNonLivingEntity(Entity entity) {
        return RotationUtils.getRotations(entity.posX, entity.posY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 0.5, entity.posZ);
    }

    public static void faceBow(Entity target, boolean silent, boolean predict, float predictSize) {
        EntityPlayerSP player = Minecraft.thePlayer;
        double posX = target.posX + (predict ? (target.posX - target.prevPosX) * (double)predictSize : 0.0) - (player.posX + (predict ? player.posX - player.prevPosX : 0.0));
        double posY = target.getEntityBoundingBox().minY + (predict ? (target.getEntityBoundingBox().minY - target.prevPosY) * (double)predictSize : 0.0) + (double)target.getEyeHeight() - 0.15 - (player.getEntityBoundingBox().minY + (predict ? player.posY - player.prevPosY : 0.0)) - (double)player.getEyeHeight();
        double posZ = target.posZ + (predict ? (target.posZ - target.prevPosZ) * (double)predictSize : 0.0) - (player.posZ + (predict ? player.posZ - player.prevPosZ : 0.0));
        double posSqrt = Math.sqrt(posX * posX + posZ * posZ);
        float velocity = (float)player.getItemInUseDuration() / 20.0f;
        if ((velocity = (velocity * velocity + velocity * 2.0f) / 3.0f) > 1.0f) {
            velocity = 1.0f;
        }
        Rotation rotation = new Rotation((float)(Math.atan2(posZ, posX) * 180.0 / Math.PI) - 90.0f, (float)(-Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)(velocity * velocity * velocity * velocity) - (double)0.006f * ((double)0.006f * (posSqrt * posSqrt) + 2.0 * posY * (double)(velocity * velocity)))) / ((double)0.006f * posSqrt)))));
        if (silent) {
            RotationUtils.setTargetRotation(rotation);
        } else {
            RotationUtils.limitAngleChange(new Rotation(player.rotationYaw, player.rotationPitch), rotation, 10 + new Random().nextInt(6)).toPlayer(Minecraft.thePlayer);
        }
    }

    public static Rotation toRotation(Vec3 vec, boolean predict) {
        Vec3 eyesPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        if (predict) {
            if (Minecraft.thePlayer.onGround) {
                eyesPos.addVector(Minecraft.thePlayer.motionX, 0.0, Minecraft.thePlayer.motionZ);
            } else {
                eyesPos.addVector(Minecraft.thePlayer.motionX, Minecraft.thePlayer.motionY, Minecraft.thePlayer.motionZ);
            }
        }
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        return new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static Vec3 getCenter(AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static VecRotation searchCenter(AxisAlignedBB bb, boolean outborder, boolean random, boolean predict, boolean throughWalls) {
        if (outborder) {
            Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return new VecRotation(vec3, RotationUtils.toRotation(vec3, predict));
        }
        Vec3 randomVec = new Vec3(bb.minX + (bb.maxX - bb.minX) * (x * 0.8 + 0.2), bb.minY + (bb.maxY - bb.minY) * (y * 0.8 + 0.2), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.8 + 0.2));
        Rotation randomRotation = RotationUtils.toRotation(randomVec, predict);
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    if (!throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(random ? RotationUtils.getRotationDifference(currentVec.getRotation(), randomRotation) < RotationUtils.getRotationDifference(vecRotation.getRotation(), randomRotation) : RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static VecRotation calculateCenter(String calMode, String randMode, double randomRange, AxisAlignedBB bb, boolean predict, boolean throughWalls) {
        VecRotation vecRotation = null;
        double xMin = 0.0;
        double yMin = 0.0;
        double zMin = 0.0;
        double xMax = 0.0;
        double yMax = 0.0;
        double zMax = 0.0;
        double xDist = 0.0;
        double yDist = 0.0;
        double zDist = 0.0;
        xMin = 0.15;
        xMax = 0.85;
        xDist = 0.1;
        yMin = 0.15;
        yMax = 1.0;
        yDist = 0.1;
        zMin = 0.15;
        zMax = 0.85;
        zDist = 0.1;
        Vec3 curVec3 = null;
        switch (calMode) {
            case "LiquidBounce": {
                xMin = 0.15;
                xMax = 0.85;
                xDist = 0.1;
                yMin = 0.15;
                yMax = 1.0;
                yDist = 0.1;
                zMin = 0.15;
                zMax = 0.85;
                zDist = 0.1;
                break;
            }
            case "Full": {
                xMin = 0.0;
                xMax = 1.0;
                xDist = 0.1;
                yMin = 0.0;
                yMax = 1.0;
                yDist = 0.1;
                zMin = 0.0;
                zMax = 1.0;
                zDist = 0.1;
                break;
            }
            case "HalfUp": {
                xMin = 0.1;
                xMax = 0.9;
                xDist = 0.1;
                yMin = 0.5;
                yMax = 0.9;
                yDist = 0.1;
                zMin = 0.1;
                zMax = 0.9;
                zDist = 0.1;
                break;
            }
            case "HalfDown": {
                xMin = 0.1;
                xMax = 0.9;
                xDist = 0.1;
                yMin = 0.1;
                yMax = 0.5;
                yDist = 0.1;
                zMin = 0.1;
                zMax = 0.9;
                zDist = 0.1;
                break;
            }
            case "CenterSimple": {
                xMin = 0.45;
                xMax = 0.55;
                xDist = 0.0125;
                yMin = 0.65;
                yMax = 0.75;
                yDist = 0.0125;
                zMin = 0.45;
                zMax = 0.55;
                zDist = 0.0125;
                break;
            }
            case "CenterLine": {
                xMin = 0.45;
                xMax = 0.55;
                xDist = 0.0125;
                yMin = 0.1;
                yMax = 0.9;
                yDist = 0.1;
                zMin = 0.45;
                zMax = 0.55;
                zDist = 0.0125;
            }
        }
        for (double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    Rotation rotation = RotationUtils.toRotation(vec3, predict);
                    if (!throughWalls && !RotationUtils.isVisible(vec3)) continue;
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(RotationUtils.getRotationDifference(currentVec.getRotation()) < RotationUtils.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                    curVec3 = vec3;
                }
            }
        }
        if (vecRotation == null || randMode == "Off") {
            return vecRotation;
        }
        double rand1 = random.nextDouble();
        double rand2 = random.nextDouble();
        double rand3 = random.nextDouble();
        double xRange = bb.maxX - bb.minX;
        double yRange = bb.maxY - bb.minY;
        double zRange = bb.maxZ - bb.minZ;
        double minRange = 999999.0;
        if (xRange <= minRange) {
            minRange = xRange;
        }
        if (yRange <= minRange) {
            minRange = yRange;
        }
        if (zRange <= minRange) {
            minRange = zRange;
        }
        rand1 = rand1 * minRange * randomRange;
        rand2 = rand2 * minRange * randomRange;
        rand3 = rand3 * minRange * randomRange;
        double xPrecent = minRange * randomRange / xRange;
        double yPrecent = minRange * randomRange / yRange;
        double zPrecent = minRange * randomRange / zRange;
        Vec3 randomVec3 = new Vec3(curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1, curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2, curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3);
        switch (randMode) {
            case "Horizonal": {
                randomVec3 = new Vec3(curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1, curVec3.yCoord, curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3);
                break;
            }
            case "Vertical": {
                randomVec3 = new Vec3(curVec3.xCoord, curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2, curVec3.zCoord);
            }
        }
        Rotation randomRotation = RotationUtils.toRotation(randomVec3, predict);
        vecRotation = new VecRotation(randomVec3, randomRotation);
        return vecRotation;
    }

    public static double getRotationDifference(Entity entity) {
        Rotation rotation = RotationUtils.toRotation(RotationUtils.getCenter(entity.getEntityBoundingBox()), true);
        return RotationUtils.getRotationDifference(rotation, new Rotation(Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch));
    }

    public static double getRotationDifference(Rotation rotation) {
        return serverRotation == null ? 0.0 : RotationUtils.getRotationDifference(rotation, serverRotation);
    }

    public static double getRotationDifference(Rotation a, Rotation b) {
        return Math.hypot(RotationUtils.getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    public static Rotation limitAngleChange(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
        float yawDifference = RotationUtils.getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = RotationUtils.getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    public static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static Vec3 getVectorForRotation(Rotation rotation) {
        float yawCos = MathHelper.cos(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float yawSin = MathHelper.sin(-rotation.getYaw() * ((float)Math.PI / 180) - (float)Math.PI);
        float pitchCos = -MathHelper.cos(-rotation.getPitch() * ((float)Math.PI / 180));
        float pitchSin = MathHelper.sin(-rotation.getPitch() * ((float)Math.PI / 180));
        return new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
    }

    public static boolean isVisible(Vec3 vec3) {
        Vec3 eyesPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        return Minecraft.theWorld.rayTraceBlocks(eyesPos, vec3) == null;
    }

    public static float[] getFacingRotations2(int paramInt1, double d, int paramInt3) {
        Minecraft.getMinecraft();
        EntitySnowball localEntityPig = new EntitySnowball(Minecraft.theWorld);
        localEntityPig.posX = (double)paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = (double)paramInt3 + 0.5;
        return RotationUtils.getRotationsNeeded(localEntityPig);
    }

    public static float[] getRotationsNeeded(Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getMinecraft();
        double xSize = entity.posX - Minecraft.thePlayer.posX;
        double ySize = entity.posY + (double)(entity.getEyeHeight() / 2.0f) - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double zSize = entity.posZ - Minecraft.thePlayer.posZ;
        double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        float yaw = (float)(Math.atan2(zSize, xSize) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(ySize, theta) * 180.0 / Math.PI));
        float[] fArray = new float[2];
        fArray[0] = (Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw)) % 360.0f;
        fArray[1] = (Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch)) % 360.0f;
        return fArray;
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (targetRotation != null && --keepLength <= 0) {
            if (revTick > 0) {
                --revTick;
                RotationUtils.reset();
            } else {
                RotationUtils.reset();
            }
        }
        if (random.nextGaussian() > 0.8) {
            x = Math.random();
        }
        if (random.nextGaussian() > 0.8) {
            y = Math.random();
        }
        if (random.nextGaussian() > 0.8) {
            z = Math.random();
        }
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (!(targetRotation == null || keepCurrentRotation || targetRotation.getYaw() == serverRotation.getYaw() && targetRotation.getPitch() == serverRotation.getPitch())) {
                packetPlayer.yaw = targetRotation.getYaw();
                packetPlayer.pitch = targetRotation.getPitch();
                packetPlayer.rotating = true;
            }
            if (packetPlayer.rotating) {
                serverRotation = new Rotation(packetPlayer.yaw, packetPlayer.pitch);
            }
        }
    }

    public static void setTargetRotation(Rotation rotation) {
        RotationUtils.setTargetRotation(rotation, 0);
    }

    public static void setTargetRotation(Rotation rotation, int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        rotation.fixedSensitivity(Float.valueOf(RotationUtils.mc.gameSettings.mouseSensitivity));
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
        revTick = 0;
    }

    public static void setTargetRotationReverse(Rotation rotation, int keepLength, int revTick) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        rotation.fixedSensitivity(Float.valueOf(RotationUtils.mc.gameSettings.mouseSensitivity));
        targetRotation = rotation;
        RotationUtils.keepLength = keepLength;
        RotationUtils.revTick = revTick + 1;
    }

    public static void reset() {
        keepLength = 0;
        targetRotation = revTick > 0 ? new Rotation(targetRotation.getYaw() - RotationUtils.getAngleDifference(targetRotation.getYaw(), Minecraft.thePlayer.rotationYaw) / (float)revTick, targetRotation.getPitch() - RotationUtils.getAngleDifference(targetRotation.getPitch(), Minecraft.thePlayer.rotationPitch) / (float)revTick) : null;
    }

    public static Rotation getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtils.getRotationFromPosition(x, z, y);
    }

    public static Rotation getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = Minecraft.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.thePlayer.posX;
        double zDiff = z - Minecraft.thePlayer.posZ;
        double yDiff = y - Minecraft.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        return new Rotation(yaw, pitch);
    }

    public static float[] getRotationFromPotion(double x, double z, double y) {
        Minecraft.getMinecraft();
        double xDiff = x - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double yDiff = y - Minecraft.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static VecRotation searchCenterforTargetStrafe(AxisAlignedBB bb) {
        VecRotation vecRotation = null;
        Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, 0.0, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        Rotation rotation = RotationUtils.toRotation(vec3, false);
        VecRotation currentVec = new VecRotation(vec3, rotation);
        if (vecRotation == null) {
            vecRotation = currentVec;
        }
        return vecRotation;
    }

    public static Rotation rotationSmooth(Rotation currentRotation, Rotation targetRotation, float smooth) {
        return new Rotation(currentRotation.getYaw() + (targetRotation.getYaw() - currentRotation.getYaw()) / smooth, currentRotation.getPitch() + (targetRotation.getPitch() - currentRotation.getPitch()) / smooth);
    }

    public boolean handleEvents() {
        return true;
    }

    static {
        serverRotation = new Rotation(0.0f, 0.0f);
        x = random.nextDouble();
        y = random.nextDouble();
        z = random.nextDouble();
    }
}

