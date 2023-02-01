/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.utils.math.MathUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;

public class AimAssist
extends Module {
    private static final Minecraft mc = Minecraft.getMinecraft();
    protected final Random rand = new Random();
    private final Numbers<Double> Horizontal = new Numbers<Double>("Horizontal", 4.2, 0.0, 10.0, 0.1);
    private final Numbers<Double> Vertical = new Numbers<Double>("Vertical", 2.4, 0.0, 10.0, 0.1);
    private final Numbers<Double> MaxSpeed = new Numbers<Double>("MaxSpeed", 0.2, 0.0, 1.0, 0.01);
    private final Numbers<Double> MinSpeed = new Numbers<Double>("MinSpeed", 0.2, 0.0, 1.0, 0.01);
    private final Numbers<Double> Range = new Numbers<Double>("AARange", 4.2, 1.0, 8.1, 0.1);
    private final Numbers<Double> anglemin = new Numbers<Double>("AngleMin", 0.0, 0.0, 1.0, 1.0);
    private final Numbers<Double> anglemax = new Numbers<Double>("AngleMax", 100.0, 20.0, 360.0, 1.0);
    private final Option<Boolean> ClickAim = new Option<Boolean>("ClickAim", false);
    private final Option<Boolean> Strafe = new Option<Boolean>("StrafeIncrease", false);
    private final Option<Boolean> team = new Option<Boolean>("Team", true);
    private final Option<Boolean> held = new Option<Boolean>("HeldItem", false);

    public AimAssist() {
        super("AimAssist", new String[]{"aimbot"}, ModuleType.Combat);
        this.addValues(this.Horizontal, this.Vertical, this.MaxSpeed, this.MinSpeed, this.Range, this.anglemin, this.anglemax, this.ClickAim, this.Strafe, this.team, this.held);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        double yawdistance;
        if (((Boolean)this.ClickAim.getValue()).booleanValue() && !AimAssist.mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }
        if (Minecraft.thePlayer.getHeldItem() == null && ((Boolean)this.held.getValue()).booleanValue()) {
            return;
        }
        Entity entity = null;
        double maxDistance = 360.0;
        double maxAngle = (Double)this.anglemax.getValue();
        double minAngle = (Double)this.anglemin.getValue();
        for (Entity e : Minecraft.theWorld.getLoadedEntityList()) {
            if (e == Minecraft.thePlayer || !this.isValid(e)) continue;
            yawdistance = this.getDistanceBetweenAngles(this.getAngles(e)[1], Minecraft.thePlayer.rotationYaw);
            if (!(maxDistance > yawdistance)) continue;
            entity = e;
            maxDistance = yawdistance;
        }
        if (entity != null) {
            float yaw = this.getAngles(entity)[1];
            float pitch = this.getAngles(entity)[0];
            yawdistance = this.getDistanceBetweenAngles(yaw, Minecraft.thePlayer.rotationYaw);
            double pitchdistance = this.getDistanceBetweenAngles(pitch, Minecraft.thePlayer.rotationPitch);
            if (pitchdistance <= maxAngle && yawdistance >= minAngle && yawdistance <= maxAngle) {
                double horizontalSpeed = (Double)this.Horizontal.getValue() * 3.0 + ((Double)this.Horizontal.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                double verticalSpeed = (Double)this.Vertical.getValue() * 3.0 + ((Double)this.Vertical.getValue() > 0.0 ? this.rand.nextDouble() : 0.0);
                if (((Boolean)this.Strafe.getValue()).booleanValue()) {
                    if (Minecraft.thePlayer.moveStrafing != 0.0f) {
                        horizontalSpeed *= 1.25;
                    }
                }
                if (this.getEntity(24.0) != null && Objects.equals(this.getEntity(24.0), entity)) {
                    horizontalSpeed *= MathUtil.randomNumber((Double)this.MaxSpeed.getValue(), (Double)this.MinSpeed.getValue());
                    verticalSpeed *= MathUtil.randomNumber((Double)this.MaxSpeed.getValue(), (Double)this.MinSpeed.getValue());
                }
                this.faceTarget(entity, 0.0f, (float)verticalSpeed);
                this.faceTarget(entity, (float)horizontalSpeed, 0.0f);
            }
        }
    }

    protected static float getRotation(float currentRotation, float targetRotation, float maxIncrement) {
        float deltaAngle = MathHelper.wrapAngleTo180_float(targetRotation - currentRotation);
        if (deltaAngle > maxIncrement) {
            deltaAngle = maxIncrement;
        }
        if (deltaAngle < -maxIncrement) {
            deltaAngle = -maxIncrement;
        }
        return currentRotation + deltaAngle / 2.0f;
    }

    private void faceTarget(Entity target, float yawspeed, float pitchspeed) {
        EntityPlayerSP player = Minecraft.thePlayer;
        float yaw = this.getAngles(target)[1];
        float pitch = this.getAngles(target)[0];
        player.rotationYaw = AimAssist.getRotation(player.rotationYaw, yaw, yawspeed);
        player.rotationPitch = AimAssist.getRotation(player.rotationPitch, pitch, pitchspeed);
    }

    public float[] getAngles(Entity entity) {
        double x = entity.posX - Minecraft.thePlayer.posX;
        double z = entity.posZ - Minecraft.thePlayer.posZ;
        double y = entity instanceof EntityEnderman ? entity.posY - Minecraft.thePlayer.posY : entity.posY + ((double)entity.getEyeHeight() - 1.9) - Minecraft.thePlayer.posY + ((double)Minecraft.thePlayer.getEyeHeight() - 1.9);
        double helper = MathHelper.sqrt_double(x * x + z * z);
        float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
        float newPitch = (float)(-Math.toDegrees(Math.atan(y / helper)));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(z / x)));
        } else if (z < 0.0 && x > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return new float[]{newPitch, newYaw};
    }

    public double getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }

    public Object[] getEntity(double distance, double expand) {
        Entity var2 = mc.getRenderViewEntity();
        Entity entity = null;
        if (var2 != null) {
            if (Minecraft.theWorld != null) {
                double var3;
                AimAssist.mc.mcProfiler.startSection("pick");
                double var5 = var3 = distance;
                Vec3 var7 = var2.getPositionEyes(0.0f);
                Vec3 var8 = var2.getLook(0.0f);
                Vec3 var9 = var7.addVector(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3);
                Vec3 var10 = null;
                float var11 = 1.0f;
                List<Entity> var12 = Minecraft.theWorld.getEntitiesWithinAABBExcludingEntity(var2, var2.getEntityBoundingBox().addCoord(var8.xCoord * var3, var8.yCoord * var3, var8.zCoord * var3).expand(var11, var11, var11));
                double var13 = var5;
                for (Entity o : var12) {
                    double var20;
                    if (!o.canBeCollidedWith()) continue;
                    float var17 = o.getCollisionBorderSize();
                    AxisAlignedBB var18 = o.getEntityBoundingBox().expand(var17, var17, var17);
                    var18 = var18.expand(expand, expand, expand);
                    MovingObjectPosition var19 = var18.calculateIntercept(var7, var9);
                    if (var18.isVecInside(var7)) {
                        if (!(0.0 < var13) && var13 != 0.0) continue;
                        entity = o;
                        var10 = var19 == null ? var7 : var19.hitVec;
                        var13 = 0.0;
                        continue;
                    }
                    if (var19 == null || !((var20 = var7.distanceTo(var19.hitVec)) < var13) && var13 != 0.0) continue;
                    boolean canRiderInteract = false;
                    if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                        canRiderInteract = Reflector.callBoolean(o, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                    }
                    if (o == var2.ridingEntity && !canRiderInteract) {
                        if (var13 != 0.0) continue;
                        entity = o;
                        var10 = var19.hitVec;
                        continue;
                    }
                    entity = o;
                    var10 = var19.hitVec;
                    var13 = var20;
                }
                if (var13 < var5 && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
                    entity = null;
                }
                AimAssist.mc.mcProfiler.endSection();
                if (entity == null || var10 == null) {
                    return null;
                }
                return new Object[]{entity, var10};
            }
        }
        return null;
    }

    public Entity getEntity(double distance) {
        if (this.getEntity(distance, 0.0) == null) {
            return null;
        }
        return (Entity)Objects.requireNonNull(this.getEntity(distance, 0.0))[0];
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isValid(Entity e) {
        boolean flag1 = true;
        Module ab2 = Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (ab2.isEnabled() && ab2.isEnabled()) {
            flag1 = !AntiBot.isBot((EntityLivingBase)e);
        }
        if (!(e instanceof EntityLivingBase)) return false;
        if (e instanceof EntityArmorStand) return false;
        if (e instanceof EntityAnimal) return false;
        if (e instanceof EntityMob) return false;
        if (e == Minecraft.thePlayer) return false;
        if (e instanceof EntityVillager) return false;
        if ((double)Minecraft.thePlayer.getDistanceToEntity(e) > (Double)this.Range.getValue()) return false;
        if (e.getName().contains("#")) return false;
        if (((Boolean)this.team.getValue()).booleanValue()) {
            if (e.getDisplayName().getFormattedText().startsWith("\u00a7" + Minecraft.thePlayer.getDisplayName().getFormattedText().charAt(1))) return false;
        }
        if (!flag1) return false;
        return true;
    }
}

