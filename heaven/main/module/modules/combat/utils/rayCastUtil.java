/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 */
package heaven.main.module.modules.combat.utils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.modules.world.ScaffoldUtils.Rotation;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;

public class rayCastUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static MovingObjectPosition getRotationOver(Rotation rot) {
        return rayCastUtil.getRotationOver(rot.getYaw(), rot.getPitch());
    }

    public static MovingObjectPosition getRotationOver(float yaw, float pitch) {
        float partialTicks = rayCastUtil.mc.timer.renderPartialTicks;
        Entity entity = mc.getRenderViewEntity();
        if (entity != null) {
            if (Minecraft.theWorld != null) {
                double d0 = Minecraft.playerController.getBlockReachDistance();
                MovingObjectPosition objectMouseOver = rayCastUtil.rayTrace(yaw, pitch);
                double d1 = d0;
                Vec3 vec3 = entity.getPositionEyes(partialTicks);
                boolean flag = false;
                int i = 3;
                if (Minecraft.playerController.extendedReach()) {
                    d0 = 6.0;
                    d1 = 6.0;
                } else if (d0 > 3.0) {
                    flag = true;
                }
                if (objectMouseOver != null) {
                    d1 = objectMouseOver.hitVec.distanceTo(vec3);
                }
                Vec3 vec31 = rayCastUtil.getVectorForRotation(pitch, yaw);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                Entity targetEntity = null;
                Vec3 vec33 = null;
                float f = 1.0f;
                List<Entity> list = Minecraft.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), (Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate)new Predicate<Entity>(){

                    public boolean apply(Entity p_apply_1_) {
                        return p_apply_1_.canBeCollidedWith();
                    }
                }));
                double d2 = d1;
                for (int j = 0; j < list.size(); ++j) {
                    double d3;
                    Entity entity1 = list.get(j);
                    float f1 = 0.1f;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                    if (axisalignedbb.isVecInside(vec3)) {
                        if (!(d2 >= 0.0)) continue;
                        targetEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0;
                        continue;
                    }
                    if (movingobjectposition == null || !((d3 = vec3.distanceTo(movingobjectposition.hitVec)) < d2) && d2 != 0.0) continue;
                    boolean flag1 = false;
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                    }
                    if (!flag1 && entity1 == entity.ridingEntity) {
                        if (d2 != 0.0) continue;
                        targetEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        continue;
                    }
                    targetEntity = entity1;
                    vec33 = movingobjectposition.hitVec;
                    d2 = d3;
                }
                if (targetEntity != null && (d2 < d1 || objectMouseOver == null)) {
                    objectMouseOver = new MovingObjectPosition(targetEntity, vec33);
                }
                return objectMouseOver;
            }
        }
        return null;
    }

    public static MovingObjectPosition rayTrace(Rotation rot) {
        return rayCastUtil.rayTrace(rot.getYaw(), rot.getPitch());
    }

    public static MovingObjectPosition rayTrace(float yaw, float pitch) {
        double distance = Minecraft.playerController.getBlockReachDistance();
        Vec3 vec3 = mc.getRenderViewEntity().getPositionEyes(1.0f);
        Vec3 vec31 = rayCastUtil.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        return Minecraft.theWorld.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    private static final Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
        float f3 = MathHelper.sin(-pitch * ((float)Math.PI / 180));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    public static Entity raycastEntity(double range, IEntityFilter entityFilter) {
        return rayCastUtil.raycastEntity(range, EventPreUpdate.getYaw(), EventPreUpdate.getPitch(), entityFilter);
    }

    public static Entity raycastEntity(double range, float yaw, float pitch, IEntityFilter entityFilter) {
        Entity renderViewEntity = mc.getRenderViewEntity();
        if (renderViewEntity != null) {
            if (Minecraft.theWorld != null) {
                double blockReachDistance = range;
                Vec3 eyePosition = renderViewEntity.getPositionEyes(1.0f);
                float yawCos = MathHelper.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
                float yawSin = MathHelper.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI);
                float pitchCos = -MathHelper.cos(-pitch * ((float)Math.PI / 180));
                float pitchSin = MathHelper.sin(-pitch * ((float)Math.PI / 180));
                Vec3 entityLook = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
                Vec3 vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
                List<Entity> entityList = Minecraft.theWorld.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).expand(1.0, 1.0, 1.0), (Predicate<? super Entity>)Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
                Entity pointedEntity = null;
                for (Entity entity : entityList) {
                    double eyeDistance;
                    if (!entityFilter.canRaycast(entity)) continue;
                    float collisionBorderSize = entity.getCollisionBorderSize();
                    AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                    MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);
                    if (axisAlignedBB.isVecInside(eyePosition)) {
                        if (!(blockReachDistance >= 0.0)) continue;
                        pointedEntity = entity;
                        blockReachDistance = 0.0;
                        continue;
                    }
                    if (movingObjectPosition == null || !((eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec)) < blockReachDistance) && blockReachDistance != 0.0) continue;
                    if (entity == renderViewEntity.ridingEntity && !Reflector.callBoolean(renderViewEntity, Reflector.ForgeEntity_canRiderInteract, new Object[0])) {
                        if (blockReachDistance != 0.0) continue;
                        pointedEntity = entity;
                        continue;
                    }
                    pointedEntity = entity;
                    blockReachDistance = eyeDistance;
                }
                return pointedEntity;
            }
        }
        return null;
    }

    public static interface IEntityFilter {
        public boolean canRaycast(Entity var1);
    }
}

