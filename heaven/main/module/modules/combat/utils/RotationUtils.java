/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat.utils;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import net.optifine.util.MathUtils;

public class RotationUtils {
    static Minecraft mc = Minecraft.getMinecraft();

    public static float getYawDifference(float currentYaw, double targetX, double targetY, double targetZ) {
        double deltaX = targetX - Minecraft.thePlayer.posX;
        double deltaY = targetY - Minecraft.thePlayer.posY;
        double deltaZ = targetZ - Minecraft.thePlayer.posZ;
        double yawToEntity = 0.0;
        double degrees = Math.toDegrees(Math.atan(deltaZ / deltaX));
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + degrees;
            }
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + degrees;
            }
        } else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(currentYaw - (float)yawToEntity));
    }

    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtils.getRotationFromPosition(x, z, y);
    }

    public float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtils.getRotationFromPosition(x, z, y);
    }

    public float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (Entity entity : targetList) {
            posX += entity.posX;
            posY += entity.boundingBox.maxY - 2.0;
            posZ += entity.posZ;
        }
        return new float[]{RotationUtils.getRotationFromPosition(posX /= (double)targetList.size(), posZ /= (double)targetList.size(), posY /= (double)targetList.size())[0], RotationUtils.getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public float getStraitYaw() {
        float YAW = MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw);
        YAW = YAW < 45.0f && YAW > -45.0f ? 0.0f : (YAW > 45.0f && YAW < 135.0f ? 90.0f : (YAW > 135.0f || YAW < -135.0f ? 180.0f : -90.0f));
        return YAW;
    }

    public float[] getBowAngles(Entity entity) {
        double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = Minecraft.thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        double n = entity.posX + xMulti;
        double x = n - Minecraft.thePlayer.posX;
        double n2 = entity.posZ + zMulti;
        double z = n2 - Minecraft.thePlayer.posZ;
        double posY = Minecraft.thePlayer.posY;
        double y = posY + (double)Minecraft.thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        double dist = Minecraft.thePlayer.getDistanceToEntity(entity);
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        double d2 = MathHelper.sqrt_double(x * x + z * z);
        float pitch = (float)(-(Math.atan2(y, d2) * 180.0 / Math.PI)) + (float)dist * 0.11f;
        return new float[]{yaw, -pitch};
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.thePlayer.posX;
        double zDiff = z - Minecraft.thePlayer.posZ;
        double yDiff = y - Minecraft.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - 0.006f * (0.006f * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(0.006f * d3)));
    }

    public float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.thePlayer.posX;
        double deltaZ = posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        } else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }

    public float getPitchChange(float pitch, Entity entity, double posY) {
        double posX = entity.posX;
        double deltaX = posX - Minecraft.thePlayer.posX;
        double posZ = entity.posZ;
        double deltaZ = posZ - Minecraft.thePlayer.posZ;
        double n = posY - 2.2 + (double)entity.getEyeHeight();
        double deltaY = n - Minecraft.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float)pitchToEntity) - 2.5f;
    }

    public float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public boolean canEntityBeSeen2(Entity e) {
        boolean see;
        Vec3 vec1 = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (double)(e.getEyeHeight() / 1.32f), e.posZ);
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY);
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean bl = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, minz);
        boolean bl2 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, minz);
        boolean bl3 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, maxz);
        boolean bl4 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, maxz);
        boolean bl5 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, minz);
        boolean bl6 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, minz);
        boolean bl7 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, maxz - 0.1);
        boolean bl8 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, maxz);
        see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        return see;
    }

    public static boolean canEntityBeSeen(Entity e) {
        boolean see;
        Vec3 vec1 = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (double)(e.getEyeHeight() / 1.32f), e.posZ);
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY);
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean bl = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, minz);
        boolean bl2 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, minz);
        boolean bl3 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, maxz);
        boolean bl4 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, maxz);
        boolean bl5 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, minz);
        boolean bl6 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, minz);
        boolean bl7 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, maxz - 0.1);
        boolean bl8 = see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, maxz);
        see = Minecraft.theWorld.rayTraceBlocks(vec1, vec2) == null;
        return see;
    }

    public float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }

    public float[] getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = Minecraft.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public boolean isOnHypixel() {
        return !mc.isSingleplayer() && RotationUtils.mc.getCurrentServerData().serverIP.contains("hypixel");
    }

    public float[] getRotationsEntity(EntityLivingBase entity) {
        if (this.isOnHypixel() && Minecraft.thePlayer.moving()) {
            return this.getRotations(entity.posX + MathUtils.randomNumber(0.03, -0.03), entity.posY + (double)entity.getEyeHeight() - 0.4 + MathUtils.randomNumber(0.07, -0.07), entity.posZ + MathUtils.randomNumber(0.03, -0.03));
        }
        return this.getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }

    public Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f3 = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
        float f4 = MathHelper.sin(-pitch * ((float)Math.PI / 180));
        return new Vec3(f2 * f3, f4, f * f3);
    }

    public static float[] getRotations5(Entity curTarget) {
        double diffY;
        if (curTarget == null) {
            return null;
        }
        double posX = curTarget.posX;
        double diffX = posX - Minecraft.thePlayer.posX;
        double posZ = curTarget.posZ;
        double diffZ = posZ - Minecraft.thePlayer.posZ;
        if (curTarget instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)curTarget;
            double n = elb.posY + ((double)elb.getEyeHeight() - 0.8);
            double posY = Minecraft.thePlayer.posY;
            diffY = n - (posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            double n2 = (curTarget.boundingBox.minY + curTarget.boundingBox.maxY) / 2.0;
            double posY2 = Minecraft.thePlayer.posY;
            diffY = n2 - (posY2 + (double)Minecraft.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getNeededRotations(Entity target) {
        double posX = Minecraft.thePlayer.posX;
        double posY = Minecraft.thePlayer.posY;
        double y = posY + (double)Minecraft.thePlayer.getEyeHeight();
        Vec3 eyesPos = new Vec3(posX, y, Minecraft.thePlayer.posZ);
        double diffX = target.posX - eyesPos.xCoord;
        double diffY = target.posY - eyesPos.yCoord + 0.1;
        double diffZ = target.posZ - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - Minecraft.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - Minecraft.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        double d1 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - y;
        double d2 = Math.sqrt(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d2) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    public float lilililllllllllllllllllliiiiiiillllllllllllllllllllllilllllllllllIIIIlllliIIII(float p_706631, float p_706632, float p_706633) {
        float var4 = MathHelper.abs(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < -p_706633) {
            var4 = -p_706633;
        }
        return p_706631 + var4;
    }

    public static float getYawChange(double posX, double posZ) {
        double deltaX = posX - Minecraft.thePlayer.posX;
        double deltaZ = posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)yawToEntity));
    }

    public float getYawChangeGiven(double posX, double posZ, float yaw) {
        double deltaX = posX - Minecraft.thePlayer.posX;
        double deltaZ = posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }

    public float clampRotation() {
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        float n = 1.0f;
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        if (MovementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        } else {
            MovementInput movementInput2 = Minecraft.thePlayer.movementInput;
            if (MovementInput.moveForward > 0.0f) {
                n = 0.5f;
            }
        }
        MovementInput movementInput3 = Minecraft.thePlayer.movementInput;
        if (MovementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        MovementInput movementInput4 = Minecraft.thePlayer.movementInput;
        if (MovementInput.moveStrafe < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * ((float)Math.PI / 180);
    }
}

