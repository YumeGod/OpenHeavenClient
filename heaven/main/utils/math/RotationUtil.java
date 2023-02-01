/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  org.apache.commons.lang3.RandomUtils
 */
package heaven.main.utils.math;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import heaven.main.event.events.world.EventRotation;
import heaven.main.module.modules.combat.CustomAura;
import heaven.main.module.modules.combat.utils.VecRotation;
import heaven.main.module.modules.render.ParticlesUtils.Location;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.math.Angle;
import heaven.main.utils.math.AngleUtility;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.vecmath.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;
import net.optifine.util.MathUtils;
import org.apache.commons.lang3.RandomUtils;

public class RotationUtil {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static heaven.main.module.modules.combat.utils.Rotation serverRotation = new heaven.main.module.modules.combat.utils.Rotation(0.0f, 0.0f);

    public static float[] getEntityRotations(Entity entity, float[] array, int n) {
        AngleUtility rotationUtils = new AngleUtility(n);
        Angle smoothAngle = rotationUtils.smoothAngle(AngleUtility.calculateAngle(new Vector3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ), new Vector3d(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ)), new Angle(array[0], array[1]));
        float[] fArray = new float[2];
        fArray[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(smoothAngle.getYaw() - Minecraft.thePlayer.rotationYaw);
        fArray[1] = smoothAngle.getPitch();
        return fArray;
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253) {
        double var4 = target.posX - Minecraft.thePlayer.posX;
        double var8 = target.posZ - Minecraft.thePlayer.posZ;
        double var6 = target instanceof EntityLivingBase ? target.posY + (double)target.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) : (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / Math.PI) - 90.0f;
        float var13 = (float)(-Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0.0), var14) * 180.0 / Math.PI);
        float pitch = RotationUtil.changeRotation(Minecraft.thePlayer.rotationPitch, var13, p_706253);
        float yaw = RotationUtil.changeRotation(Minecraft.thePlayer.rotationYaw, var12, p_706252);
        return new float[]{yaw, pitch};
    }

    private static float[] getRotations2(Entity entity) {
        float pitch;
        boolean close;
        double yDist;
        EntityPlayerSP player = Minecraft.thePlayer;
        double xDist = entity.posX - player.posX;
        double zDist = entity.posZ - player.posZ;
        if (((Boolean)CustomAura.rotresist.getValue()).booleanValue()) {
            Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
            Location myEyePos = new Location(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
            for (yDist = entity.boundingBox.minY + 0.7; yDist < entity.boundingBox.maxY - 0.1; yDist += 0.1) {
                if (myEyePos.distanceTo(new Location(entity.posX, yDist, entity.posZ)) >= myEyePos.distanceTo(BestPos)) continue;
                BestPos = new Location(entity.posX, yDist, entity.posZ);
            }
            if (myEyePos.distanceTo(BestPos) >= (Double)CustomAura.rotrange.getValue() + 1.0) {
                return null;
            }
            yDist = BestPos.getY() - entity.posY - player.posY + (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
        } else {
            yDist = entity.posY - player.posY + (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
        }
        double dist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
        AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.1f, 0.1f, 0.1f);
        double playerEyePos = player.posY + (double)player.getEyeHeight();
        boolean bl = close = dist < 2.0 && Math.abs(yDist) < 2.0;
        if (close && playerEyePos > entityBB.minY) {
            pitch = 60.0f;
        } else {
            yDist = playerEyePos > entityBB.maxY ? entityBB.maxY - playerEyePos : (playerEyePos < entityBB.minY ? entityBB.minY - playerEyePos : 0.0);
            pitch = (float)(-(StrictMath.atan2(yDist, dist) * 57.29577951308232));
        }
        float yaw = (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f;
        if (close) {
            int inc = dist < 1.0 ? 180 : 90;
            yaw = Math.round(yaw / (float)inc) * inc;
        }
        float[] fArray = new float[2];
        fArray[0] = RotationUtil.changeRotation(Minecraft.thePlayer.rotationYaw, yaw, 600.0f);
        fArray[1] = RotationUtil.changeRotation(Minecraft.thePlayer.rotationPitch, pitch, 1000.0f);
        return fArray;
    }

    public static float[] faceTarget(Entity target) {
        double yDiff;
        double xDiff = target.posX - Minecraft.thePlayer.posX;
        double zDiff = target.posZ - Minecraft.thePlayer.posZ;
        if (target instanceof EntityLivingBase) {
            if (((Boolean)CustomAura.rotresist.getValue()).booleanValue()) {
                Location BestPos = new Location(target.posX, target.posY, target.posZ);
                Location myEyePos = new Location(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
                for (yDiff = target.boundingBox.minY + 0.7; yDiff < target.boundingBox.maxY - 0.1; yDiff += 0.1) {
                    if (myEyePos.distanceTo(new Location(target.posX, yDiff, target.posZ)) >= myEyePos.distanceTo(BestPos)) continue;
                    BestPos = new Location(target.posX, yDiff, target.posZ);
                }
                if (myEyePos.distanceTo(BestPos) >= (Double)CustomAura.rotrange.getValue() + 1.0) {
                    return null;
                }
                yDiff = BestPos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) - (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
            } else {
                yDiff = target.posY + (double)target.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) - (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
            }
        } else {
            yDiff = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) - (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
        }
        double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff - (target instanceof EntityPlayer ? 0.25 : 0.0), dist) * 180.0 / Math.PI);
        if (yDiff > -0.2 && yDiff < 0.2) {
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 1.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        } else if (yDiff > -0.2) {
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 3.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        } else if (yDiff < 0.3) {
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        }
        float[] fArray = new float[2];
        fArray[0] = RotationUtil.changeRotation(Minecraft.thePlayer.rotationYaw, yaw, 600.0f);
        fArray[1] = RotationUtil.changeRotation(Minecraft.thePlayer.rotationPitch, pitch, 1000.0f);
        return fArray;
    }

    public static float[] teleportRot(Entity entity, Entity target, float p_706252, float p_706253) {
        double xDist = target.posX - entity.posX;
        double zDist = target.posZ - entity.posZ;
        double y = target instanceof EntityLivingBase ? target.posY + (double)target.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight()) : (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (entity.posY + (double)entity.getEyeHeight());
        double distance = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        float realYaw = (float)(Math.atan2(zDist, xDist) * 180.0 / Math.PI) - 90.0f;
        float realPitch = (float)(-Math.atan2(y - (target instanceof EntityPlayer ? 0.25 : 0.0), distance) * 180.0 / Math.PI);
        float yaw = RotationUtil.changeRotation(entity.rotationYaw, realYaw, p_706252);
        float pitch = RotationUtil.changeRotation(entity.rotationPitch, realPitch, p_706253);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(float p_706631, float p_706632, float p_706633) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < -p_706633) {
            var4 = -p_706633;
        }
        return p_706631 + var4;
    }

    public static float[] getRotateForReturn(float lastYaw, float lastPitch, float backspeed) {
        Minecraft.getMinecraft();
        float f = Minecraft.thePlayer.rotationYaw;
        Minecraft.getMinecraft();
        EventRotation smoothAngle = RotationUtil.smoothAngle(new EventRotation(f, Minecraft.thePlayer.rotationPitch), new EventRotation(lastYaw, lastPitch), backspeed);
        float[] fArray = new float[2];
        Minecraft.getMinecraft();
        float f2 = Minecraft.thePlayer.rotationYaw;
        float f3 = smoothAngle.getYaw();
        Minecraft.getMinecraft();
        fArray[0] = f2 + MathHelper.wrapAngleTo180_float(f3 - Minecraft.thePlayer.rotationYaw);
        fArray[1] = smoothAngle.getPitch();
        return fArray;
    }

    public static float[] getRotateForScaffold(float needYaw, float needPitch, float yaw, float pitch, float minturnspeed, float maxturnspeed) {
        EventRotation smoothAngle = RotationUtil.smoothAngle(new EventRotation(needYaw, needPitch), new EventRotation(yaw, pitch), RandomUtils.nextFloat((float)minturnspeed, (float)maxturnspeed));
        float[] fArray = new float[2];
        fArray[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(smoothAngle.getYaw() - Minecraft.thePlayer.rotationYaw);
        fArray[1] = smoothAngle.getPitch();
        return fArray;
    }

    private static EventRotation smoothAngle(EventRotation e, EventRotation e2, float turnSpeed) {
        EventRotation angle = new EventRotation(e2.getYaw() - e.getYaw(), e2.getPitch() - e.getPitch()).getAngle();
        angle.setYaw(e2.getYaw() - angle.getYaw() / 180.0f * turnSpeed);
        angle.setPitch(e2.getPitch() - angle.getPitch() / 180.0f * turnSpeed);
        return angle.getAngle();
    }

    public static float[] RotAura(EntityLivingBase entity) {
        double diffY;
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        if (((Boolean)CustomAura.rotresist.getValue()).booleanValue()) {
            Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
            Location myEyePos = new Location(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
            for (diffY = entity.boundingBox.minY + 0.7; diffY < entity.boundingBox.maxY - 0.1; diffY += 0.1) {
                if (myEyePos.distanceTo(new Location(entity.posX, diffY, entity.posZ)) >= myEyePos.distanceTo(BestPos)) continue;
                BestPos = new Location(entity.posX, diffY, entity.posZ);
            }
            if (myEyePos.distanceTo(BestPos) >= (Double)CustomAura.rotrange.getValue() + 1.0) {
                return null;
            }
            diffY = BestPos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) - (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
        } else {
            diffY = entity.posY + (double)entity.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) - (double)((Boolean)CustomAura.rotfall.getValue() != false ? 2 : 0);
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        float[] fArray = new float[2];
        fArray[0] = RotationUtil.changeRotation(Minecraft.thePlayer.rotationYaw, yaw, 600.0f);
        fArray[1] = RotationUtil.changeRotation(Minecraft.thePlayer.rotationPitch, pitch, 1000.0f);
        return fArray;
    }

    private static float getRotVoidHead(EntityLivingBase entity) {
        if (((Boolean)CustomAura.rotperty.getValue()).booleanValue()) {
            return Objects.requireNonNull(RotationUtil.getRotations2(entity))[0];
        }
        if (((Boolean)CustomAura.autopitch.getValue()).booleanValue()) {
            return Objects.requireNonNull(RotationUtil.faceTarget(entity))[0];
        }
        return Objects.requireNonNull(RotationUtil.RotAura(entity))[0];
    }

    private static float getRotVoidPitch(EntityLivingBase entity) {
        if (((Boolean)CustomAura.rotperty.getValue()).booleanValue()) {
            return Objects.requireNonNull(RotationUtil.getRotations2(entity))[1];
        }
        if (((Boolean)CustomAura.autopitch.getValue()).booleanValue()) {
            return Objects.requireNonNull(RotationUtil.faceTarget(entity))[1];
        }
        return Objects.requireNonNull(RotationUtil.RotAura(entity))[1];
    }

    public static float[] getRotateForAura(EntityLivingBase entityLivingBase, float yaw, float pitch, float minturnspeed, float maxturnspeed) {
        if (entityLivingBase == null) {
            return null;
        }
        EventRotation smoothAngle = RotationUtil.smoothAngle(new EventRotation(RotationUtil.getRotVoidHead(entityLivingBase), RotationUtil.getRotVoidPitch(entityLivingBase)), new EventRotation(yaw, pitch), RandomUtils.nextFloat((float)minturnspeed, (float)maxturnspeed));
        float[] fArray = new float[2];
        fArray[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(smoothAngle.getYaw() - Minecraft.thePlayer.rotationYaw);
        fArray[1] = smoothAngle.getPitch();
        return fArray;
    }

    public static float[] getRotationBlock(BlockPos pos) {
        return RotationUtil.getRotationsByVec(Minecraft.thePlayer.getPositionVector().addVector(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0), new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5));
    }

    private static float[] getRotationsByVec(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = difference.flat().lengthVector();
        float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsForDown(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(-Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.4) - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(double posX, double posY, double posZ, double eyeHeight, BlockPos blockPos, EnumFacing enumFacing) {
        double n = (double)blockPos.getX() + 0.5 - posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double n2 = (double)blockPos.getZ() + 0.5 - posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double n3 = posY + eyeHeight - ((double)blockPos.getY() + 0.5);
        double n4 = MathHelper.sqrt_double(n * n + n2 * n2);
        float n5 = (float)(Math.atan2(n2, n) * 180.0 / Math.PI) - 90.0f;
        float n6 = (float)(Math.atan2(n3, n4) * 180.0 / Math.PI);
        if (n5 < 0.0f) {
            n5 += 360.0f;
        }
        return new float[]{n5, n6};
    }

    public static Entity getMouseOver(float partialTicks, Entity entity) {
        Entity mcpointedentity = null;
        if (entity != null) {
            if (Minecraft.theWorld != null) {
                double d0 = Minecraft.playerController.getBlockReachDistance();
                MovingObjectPosition objectMouseOver = entity.rayTrace(d0, partialTicks);
                Vec3 vec3 = entity.getPositionEyes(partialTicks);
                d0 = 6.0;
                double d1 = 6.0;
                if (objectMouseOver != null) {
                    d1 = objectMouseOver.hitVec.distanceTo(vec3);
                }
                Vec3 vec31 = entity.getLook(partialTicks);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                Entity pointedEntity = null;
                Vec3 vec33 = null;
                float f = 1.0f;
                List<Entity> list = Minecraft.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), (Predicate<? super Entity>)Predicates.and((Predicate[])new Predicate[]{EntitySelectors.NOT_SPECTATING}));
                double d2 = d1;
                Iterator<Entity> iterator = list.iterator();
                while (iterator.hasNext()) {
                    double d3;
                    Entity o;
                    Entity entity1 = o = iterator.next();
                    float f1 = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                    if (axisalignedbb.isVecInside(vec3)) {
                        if (!(d2 >= 0.0)) continue;
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0;
                        continue;
                    }
                    if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                    boolean flag2 = false;
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                    }
                    if (entity1 == entity.ridingEntity && !flag2) {
                        if (d2 != 0.0) continue;
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        continue;
                    }
                    pointedEntity = entity1;
                    vec33 = movingobjectposition.hitVec;
                    d2 = d3;
                }
                if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                    new MovingObjectPosition(pointedEntity, vec33);
                    if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                        mcpointedentity = pointedEntity;
                    }
                }
            }
        }
        return mcpointedentity;
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = Minecraft.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - Minecraft.thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        return Double.isNaN((double)Minecraft.thePlayer.rotationYaw - yawToEntity) ? 0.0f : MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)yawToEntity));
    }

    public static float getPitchChangeToEntity(Entity entity) {
        double deltaX = entity.posX - Minecraft.thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
        double deltaY = entity.posY - 1.6 + (double)entity.getEyeHeight() - Minecraft.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return Double.isNaN((double)Minecraft.thePlayer.rotationPitch - pitchToEntity) ? 0.0f : -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
    }

    public static float[] getAnglesWatchdog(Entity e) {
        float[] fArray = new float[2];
        fArray[0] = RotationUtil.getYawChangeToEntity(e) + Minecraft.thePlayer.rotationYaw;
        fArray[1] = RotationUtil.getPitchChangeToEntity(e) + Minecraft.thePlayer.rotationPitch;
        return fArray;
    }

    public static float[] getAngles(EntityLivingBase entity) {
        if (entity == null) {
            return null;
        }
        EntityPlayerSP player = Minecraft.thePlayer;
        double diffX = entity.posX - player.posX;
        double diffY = entity.posY + (double)entity.getEyeHeight() * 0.9 - (player.posY + (double)player.getEyeHeight());
        double diffZ = entity.posZ - player.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{player.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw), player.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch)};
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = (float)MathHelper.cos(Math.toRadians(-yaw) - 3.1415927410125732);
        float f1 = (float)MathHelper.sin(Math.toRadians(-yaw) - 3.1415927410125732);
        float f2 = (float)(-MathHelper.cos(Math.toRadians(-pitch)));
        float f3 = (float)MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static float getYawToPoint(double posX, double posZ) {
        double xDiff = posX - (Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * (double)RotationUtil.mc.timer.elapsedPartialTicks);
        double zDiff = posZ - (Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * (double)RotationUtil.mc.timer.elapsedPartialTicks);
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        return (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
    }

    public static float[] getOffsetRotations(EntityLivingBase curTarget) {
        double d;
        double d2 = curTarget.posX;
        Minecraft.getMinecraft();
        double diffX = d2 - Minecraft.thePlayer.posX;
        double d3 = curTarget.posZ;
        Minecraft.getMinecraft();
        double diffZ = d3 - Minecraft.thePlayer.posZ;
        if (curTarget instanceof EntityPlayer) {
            double d4 = curTarget.posY;
            Minecraft.getMinecraft();
            d = d4 - Minecraft.thePlayer.posY - (curTarget.isSneaking() ? 0.3 : 0.1);
        } else {
            double d5 = curTarget.posY;
            Minecraft.getMinecraft();
            d = d5 - Minecraft.thePlayer.posY - 1.2;
        }
        double diffY = d;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] getRemixRotations(Entity target) {
        double xDiff = target.posX - Minecraft.thePlayer.posX;
        double yDiff = target.posY - Minecraft.thePlayer.posY;
        double zDiff = target.posZ - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 0.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        if (yDiff > -0.2 && yDiff < 0.2) {
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 1.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        } else if (yDiff > -0.2) {
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 3.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        } else if (yDiff < 0.3) {
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 1.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / Math.PI);
        }
        return new float[]{yaw, pitch};
    }

    public static float[] getFluxRotations(Entity entity, double maxRange) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        Location BestPos = new Location(entity.posX, entity.posY, entity.posZ);
        Location myEyePos = new Location(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        for (diffY = entity.boundingBox.minY + 0.7; diffY < entity.boundingBox.maxY - 0.1; diffY += 0.1) {
            Location location = new Location(entity.posX, diffY, entity.posZ);
            if (!(myEyePos.distanceTo(location) < myEyePos.distanceTo(BestPos))) continue;
            BestPos = new Location(entity.posX, diffY, entity.posZ);
        }
        if (myEyePos.distanceTo(BestPos) > maxRange) {
            return null;
        }
        diffY = BestPos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsFromAutumn(double posX, double posY, double posZ) {
        EntityPlayerSP player = Minecraft.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getAutumnRotationsEntity(EntityLivingBase entity) {
        if (Minecraft.thePlayer.moving()) {
            return RotationUtil.getRotationsFromAutumn(entity.posX + MathUtils.randomNumber(0.03, -0.03), entity.posY + (double)entity.getEyeHeight() - 0.4 + MathUtils.randomNumber(0.07, -0.07), entity.posZ + MathUtils.randomNumber(0.03, -0.03));
        }
        return RotationUtil.getRotationsFromAutumn(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }

    public static float[] getWatchdogZitterRotation(EntityLivingBase entity) {
        if (Minecraft.thePlayer.moving()) {
            return RotationUtil.getRotationsFromAutumn(entity.posX + MoveUtils.getDirection() / (entity.posZ / 100.0), entity.posY + (double)(entity.getEyeHeight() / 2.0f), entity.posZ + MoveUtils.getDirection() / (entity.posX / 100.0));
        }
        return RotationUtil.getRotationsFromAutumn(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }

    public static float[] getRotationsToEnt(Entity ent) {
        double differenceX = ent.posX - Minecraft.thePlayer.posX;
        double differenceY = ent.posY + (double)ent.height - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.height) - 0.5;
        double differenceZ = ent.posZ - Minecraft.thePlayer.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float)(Math.atan2(differenceY, Minecraft.thePlayer.getDistanceToEntity(ent)) * 180.0 / Math.PI);
        float finishedYaw = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - Minecraft.thePlayer.rotationYaw);
        float finishedPitch = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - Minecraft.thePlayer.rotationPitch);
        return new float[]{finishedYaw, -MathHelper.clamp_float(finishedPitch, -90.0f, 90.0f)};
    }

    public static float[] PredicteRot(double x, double z, double y) {
        double xDiff = x - Minecraft.thePlayer.posX;
        double zDiff = z - Minecraft.thePlayer.posZ;
        double yDiff = y - Minecraft.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getSmoothRot(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 6.0f);
        return RotationUtil.PredicteRot(x + Math.min(MoveUtils.getDirectionForAura() / 100.0, -MoveUtils.getDirectionForAura() / 100.0), z + Math.min(MoveUtils.getDirectionForAura() / 100.0, -MoveUtils.getDirectionForAura() / 100.0), y);
    }

    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.PredicteRot(x, z, y);
    }

    public static Vec3 getPositionEyes(float partialTicks) {
        return new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        Minecraft.getMinecraft();
        double xDiff = x - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double yDiff = y - Minecraft.thePlayer.posY;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float getSensitivityMultiplier() {
        float f = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return f * f * f * 8.0f * 0.15f;
    }

    public static heaven.main.module.modules.combat.utils.Rotation toRotationMisc(Vec3 vec, boolean predict) {
        Vec3 eyesPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        if (predict) {
            eyesPos.addVector(Minecraft.thePlayer.motionX, Minecraft.thePlayer.motionY, Minecraft.thePlayer.motionZ);
        }
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        return new heaven.main.module.modules.combat.utils.Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static Rotation toRotation(Vec3 vec, boolean predict) {
        Vec3 eyesPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.getEntityBoundingBox().minY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        if (predict) {
            eyesPos.addVector(Minecraft.thePlayer.motionX, Minecraft.thePlayer.motionY, Minecraft.thePlayer.motionZ);
        }
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        return new Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    public static float[] getAnglesAGC(Entity e) {
        EntityPlayerSP playerSP = Minecraft.thePlayer;
        double differenceX = e.posX - playerSP.posX;
        double differenceY = e.posY + (double)e.height - (playerSP.posY + (double)playerSP.height);
        double differenceZ = e.posZ - playerSP.posZ;
        float rotationYaw = (float)(Math.atan2(differenceZ, differenceX) * 180.0 / Math.PI) - 90.0f;
        float rotationPitch = (float)(Math.atan2(differenceY, playerSP.getDistanceToEntity(e)) * 180.0 / Math.PI);
        float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[]{finishedYaw, -finishedPitch};
    }

    public static float[] getCustomRotation(Vec3 vec) {
        Vec3 playerVector = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        double y = vec.yCoord - playerVector.yCoord;
        double x = vec.xCoord - playerVector.xCoord;
        double z = vec.zCoord - playerVector.zCoord;
        double dff = Math.sqrt(x * x + z * z);
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(y, dff)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public static Vec3 getLocation(AxisAlignedBB bb) {
        double yaw = 0.5;
        double pitch = 0.5;
        VecRotation rotation = RotationUtil.searchCenter(bb, true);
        return rotation != null ? rotation.getVec() : new Vec3(bb.minX + (bb.maxX - bb.minX) * yaw, bb.minY + (bb.maxY - bb.minY) * pitch, bb.minZ + (bb.maxZ - bb.minZ) * yaw);
    }

    private static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static double getRotationDifference(heaven.main.module.modules.combat.utils.Rotation a, heaven.main.module.modules.combat.utils.Rotation b) {
        return Math.hypot(RotationUtil.getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    public static VecRotation searchCenter(AxisAlignedBB bb, boolean predict) {
        VecRotation vecRotation = null;
        for (double xSearch = 0.15; xSearch < 0.85; xSearch += 0.1) {
            for (double ySearch = 0.15; ySearch < 1.0; ySearch += 0.1) {
                for (double zSearch = 0.15; zSearch < 0.85; zSearch += 0.1) {
                    Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    heaven.main.module.modules.combat.utils.Rotation rotation = RotationUtil.toRotationMisc(vec3, predict);
                    VecRotation currentVec = new VecRotation(vec3, rotation);
                    if (vecRotation != null && !(RotationUtil.getRotationDifference(currentVec.getRotation()) < RotationUtil.getRotationDifference(vecRotation.getRotation()))) continue;
                    vecRotation = currentVec;
                }
            }
        }
        return vecRotation;
    }

    public static double getRotationDifference(heaven.main.module.modules.combat.utils.Rotation rotation) {
        return RotationUtil.getRotationDifference(rotation, serverRotation);
    }

    public static class Rotation {
        float yaw;
        float pitch;

        public Rotation(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getYaw() {
            return this.yaw;
        }

        public float getPitch() {
            return this.pitch;
        }

        public void toPlayer(EntityPlayer player) {
            if (Float.isNaN(this.yaw) || Float.isNaN(this.pitch)) {
                return;
            }
            this.fixedSensitivity(Float.valueOf(RotationUtil.mc.gameSettings.mouseSensitivity));
            player.rotationYaw = this.yaw;
            player.rotationPitch = this.pitch;
        }

        public void fixedSensitivity(Float sensitivity) {
            float f = sensitivity.floatValue() * 0.6f + 0.2f;
            float gcd = f * f * f * 1.2f;
            this.yaw -= this.yaw % gcd;
            this.pitch -= this.pitch % gcd;
        }
    }
}

