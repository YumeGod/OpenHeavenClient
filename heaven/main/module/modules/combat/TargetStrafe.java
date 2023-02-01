/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventTick;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.render.HUD;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.timer.Timer;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.lang.invoke.LambdaMetafactory;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class TargetStrafe
extends Module {
    private final Numbers<Double> radius = new Numbers<Double>("Radius", 2.0, 0.25, 6.0, 0.25);
    private final Option<Boolean> dynamicRange = new Option<Boolean>("DynamicRange", true);
    private final Numbers<Double> safeRadius = new Numbers<Double>("SafeRadius", 2.0, 0.25, 6.0, 0.25, this.dynamicRange::get);
    private final Numbers<Double> attackRadius = new Numbers<Double>("AttackRadius", 2.0, 0.5, 4.5, 0.5, this.dynamicRange::get);
    private final Numbers<Double> switchHurtTick = new Numbers<Double>("SwitchHurtTick", 9.0, 5.0, 20.0, 1.0);
    private final Numbers<Double> height = new Numbers<Double>("Height", 5.0, 5.0, 10.0, 1.0);
    private final Option<Boolean> target = new Option<Boolean>("Target", false);
    private final Option<Boolean> points = new Option<Boolean>("Point ", false);
    private final Numbers<Double> pointsMultiplier = new Numbers<Double>("PointsMultiplier", 2.0, 0.25, 3.5, 0.25, this.points::get);
    private final Option<Boolean> space = new Option<Boolean>("OnlySpace", false);
    private final Option<Boolean> controllable = new Option<Boolean>("Controllable", true);
    private final Option<Boolean> behind = new Option<Boolean>("Behind", false);
    private final Option<Boolean> autoThirdPerson = new Option<Boolean>("AutoThirdPerson", false);
    private final Option<Boolean> avoidEdges = new Option<Boolean>("AvoidEdges", false);
    private boolean inverted;
    private boolean thirded;
    private final Timer timer = new Timer();
    private static final double DOUBLED_PI = Math.PI * 2;
    private int position;
    private int direction = 1;
    private float dist;

    public TargetStrafe() {
        super("TargetStrafe", new String[]{"ts"}, ModuleType.Combat);
        this.addValues(this.radius, this.safeRadius, this.attackRadius, this.pointsMultiplier, this.switchHurtTick, this.height, this.dynamicRange, this.target, this.points, this.space, this.controllable, this.behind, this.autoThirdPerson, this.avoidEdges);
    }

    @EventHandler
    public void onUpdate(EventTick tickUpdateEvent) {
        double posY;
        if (Minecraft.thePlayer.movementInput.getMoveStrafe() < 0.0f) {
            this.direction = -1;
        } else if (Minecraft.thePlayer.movementInput.getMoveStrafe() > 0.0f) {
            this.direction = 1;
        }
        if (this.isCollided()) {
            if (this.timer.delay(200.0)) {
                this.inverted = !this.inverted;
                this.position = this.inverted ? this.position - 1 : this.position + 1;
            }
            this.timer.reset();
        }
        KillAura killAura = this.getModule(KillAura.class);
        double rad = this.getRadius(killAura.getTarget());
        int positionsCount = (int)(Math.PI * rad);
        double radianPerPosition = Math.PI * 2 / (double)positionsCount;
        EntityLivingBase target = killAura.getTarget();
        double posX = MathHelper.sin(radianPerPosition * (double)(this.position + 1) * rad * (double)((Boolean)this.controllable.get() != false ? this.direction : 1));
        if (!this.isVoidBelow(target.posX + posX, target.posY, target.posZ + (posY = MathHelper.cos(radianPerPosition * (double)(this.position + 1)) * rad))) {
            this.inverted = !this.inverted;
        }
        this.dist = 0.7f;
        if (!(!((Boolean)this.autoThirdPerson.get()).booleanValue() || killAura.isEnabled() && killAura.getTarget() != null && killAura.shouldAttack() && this.shouldTarget())) {
            this.thirded = false;
            TargetStrafe.mc.gameSettings.thirdPersonView = 0;
        }
    }

    public void circleStrafe(EventMove event, double movementSpeed, Entity target) {
        double rad = this.getRadius(target);
        int positionsCount = (int)((double)((int)(Math.PI * rad)) * (Double)this.pointsMultiplier.get());
        double radianPerPosition = Math.PI * 2 / (double)positionsCount;
        double posX = MathHelper.sin(radianPerPosition * (double)this.position) * rad * (double)((Boolean)this.controllable.get() != false ? this.direction : 1);
        double posY = MathHelper.cos(radianPerPosition * (double)this.position) * rad;
        Vec3 myPos = new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
        Vec3 rotVec = this.getVectorForRotation(90.0f, 0.0f);
        Vec3 multiplied = myPos.addVector(rotVec.xCoord * (Double)this.height.get(), rotVec.yCoord * (Double)this.height.get(), rotVec.zCoord * (Double)this.height.get());
        MovingObjectPosition movingObjectPosition = Minecraft.theWorld.rayTraceBlocks(myPos, multiplied, false, false, false);
        if (!((Boolean)this.avoidEdges.get()).booleanValue() || movingObjectPosition != null) {
            if (((Boolean)this.autoThirdPerson.get()).booleanValue()) {
                this.thirded = true;
                TargetStrafe.mc.gameSettings.thirdPersonView = 1;
            }
            if (((Boolean)this.behind.get()).booleanValue()) {
                double xPos = target.posX + -Math.sin(Math.toRadians(target.rotationYaw)) * -2.0;
                double zPos = target.posZ + Math.cos(Math.toRadians(target.rotationYaw)) * -2.0;
                EventMove.setX(movementSpeed * -MathHelper.sin(Math.toRadians(RotationUtil.getRotations(xPos, target.posY, zPos)[0])));
                EventMove.setZ(movementSpeed * MathHelper.cos(Math.toRadians(RotationUtil.getRotations(xPos, target.posY, zPos)[0])));
            } else {
                EventMove.setX(movementSpeed * -MathHelper.sin(Math.toRadians(RotationUtil.getRotations(target.posX + posX, target.posY, target.posZ + posY)[0])));
                EventMove.setZ(movementSpeed * MathHelper.cos(Math.toRadians(RotationUtil.getRotations(target.posX + posX, target.posY, target.posZ + posY)[0])));
            }
        } else {
            EventMove.setX(0.0);
            EventMove.setZ(0.0);
        }
        double x = Math.abs(target.posX + posX - Minecraft.thePlayer.posX);
        double z = Math.abs(target.posZ + posY - Minecraft.thePlayer.posZ);
        double sqrt = Math.sqrt(x * x + z * z);
        if (sqrt <= (double)this.dist) {
            this.position += (this.inverted ? -1 : 1) % positionsCount;
        } else if (sqrt > 3.0) {
            this.position = this.getClosestPoint(target);
        }
    }

    private double getRadius(Entity target) {
        if (((Boolean)this.dynamicRange.get()).booleanValue()) {
            if (target.hurtResistantTime <= ((Double)this.switchHurtTick.get()).intValue()) {
                return (Double)this.attackRadius.get();
            }
            return (Double)this.safeRadius.get();
        }
        return (Double)this.radius.get();
    }

    protected final Vec3 getVectorForRotation(float pitch, float yaw) {
        double f = MathHelper.cos(Math.toRadians(-yaw) - 3.1415927410125732);
        double f1 = MathHelper.sin(Math.toRadians(-yaw) - 3.1415927410125732);
        double f2 = -MathHelper.cos(Math.toRadians(-pitch));
        double f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new Vec3(f1 * f2, f3, f * f2);
    }

    private boolean isVoidBelow(double x, double y, double z) {
        for (int i = (int)y; i > 0; --i) {
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() != Blocks.air) {
                return true;
            }
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() != Blocks.lava) {
                return true;
            }
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() != Blocks.flowing_lava) {
                return true;
            }
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() != Blocks.flowing_water) {
                return true;
            }
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() != Blocks.cactus) {
                return true;
            }
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() != Blocks.fire) {
                return true;
            }
            if (Minecraft.theWorld.getBlockState(new BlockPos(x, (double)i, z)).getBlock() == Blocks.web) continue;
            return true;
        }
        return false;
    }

    private int getClosestPoint(Entity target) {
        return ((Point)this.getPoints((Entity)target).stream().sorted(Comparator.comparingDouble((ToDoubleFunction<Point>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)D, getDistanceToPlayer(), (Lheaven/main/module/modules/combat/TargetStrafe$Point;)D)())).collect(Collectors.toList()).get((int)0)).poscount;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isCollided() {
        if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, 0.0, -0.5)).isEmpty()) return true;
        if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.5, 0.0, 0.0)).isEmpty()) return true;
        if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, 0.0, 0.5)).isEmpty()) return true;
        if (Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(-0.5, 0.0, 0.0)).isEmpty()) return false;
        return true;
    }

    private boolean space() {
        return (Boolean)this.space.get() == false || TargetStrafe.mc.gameSettings.keyBindJump.isKeyDown();
    }

    public boolean support() {
        return this.space() && this.isEnabled((Class<? extends Module>)Speed.class);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean shouldTarget() {
        if (!this.isEnabled((Class<? extends Module>)KillAura.class)) return false;
        if (this.getModule(KillAura.class).getTarget() == null) return false;
        if (!Minecraft.thePlayer.canEntityBeSeen(this.getModule(KillAura.class).getTarget())) return false;
        if (((Boolean)this.target.get()).booleanValue()) {
            if (FriendManager.isFriend(KillAura.target.getName())) return false;
        }
        if (!(Minecraft.thePlayer.getDistance2D(this.getModule(KillAura.class).getTarget()) < (Double)KillAura.range.get() + 2.0)) return false;
        if (!(Minecraft.thePlayer.posY >= this.getModule(KillAura.class).getTarget().posY - 3.4)) return false;
        if (!this.getModule(KillAura.class).getTarget().isEntityAlive()) return false;
        if (!Minecraft.thePlayer.isMoving()) return false;
        if (!(Minecraft.thePlayer.posY <= this.getModule(KillAura.class).getTarget().posY + 3.4)) return false;
        if (!this.support()) return false;
        return true;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    private List<Point> getPoints(Entity target) {
        CopyOnWriteArrayList<Point> points = new CopyOnWriteArrayList<Point>();
        double rad = this.getRadius(target);
        int positionsCount = (int)((double)((int)(Math.PI * rad)) * (Double)this.pointsMultiplier.get());
        for (int i = 0; i <= positionsCount; ++i) {
            double radianPerPosition = Math.PI * 2 / (double)positionsCount;
            double posX = MathHelper.sin(radianPerPosition * (double)i) * rad;
            double posY = MathHelper.cos(radianPerPosition * (double)i) * rad;
            points.add(new Point(target.posX + posX, target.posZ + posY, i));
        }
        return points;
    }

    @EventHandler
    public void onRender3D(EventRender3D render3DEvent) {
        Color color = new Color(((Double)HUD.r.get()).intValue(), ((Double)HUD.g.get()).intValue(), ((Double)HUD.b.get()).intValue());
        KillAura aura = this.getModule(KillAura.class);
        RenderUtil.pre3D();
        if (aura.getTarget() != null && ((Boolean)this.points.get()).booleanValue()) {
            if ((double)Minecraft.thePlayer.getDistanceToEntity(aura.getTarget()) < (Double)KillAura.range.get() && aura.getTarget().isEntityAlive()) {
                GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                this.renderCicle(5L, aura, render3DEvent);
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
                this.renderCicle(2L, aura, render3DEvent);
            }
        }
        GlStateManager.disableBlend();
        RenderUtil.post3D();
    }

    private void renderCicle(long lw, KillAura aura, EventRender3D render3DEvent) {
        GL11.glLineWidth((float)lw);
        GL11.glBegin((int)3);
        double rad = this.getRadius(aura.getTarget());
        double piDivider = Math.PI * 2 / (Math.PI * rad * (Double)this.pointsMultiplier.get());
        for (double d = 0.0; d < Math.PI * 2; d += piDivider) {
            double d2 = aura.getTarget().lastTickPosX + (aura.getTarget().posX - aura.getTarget().lastTickPosX) * (double)render3DEvent.getPartialTicks() + Math.sin(d) * rad;
            mc.getRenderManager();
            double x = d2 - RenderManager.renderPosX;
            double d3 = aura.getTarget().lastTickPosY + (aura.getTarget().posY - aura.getTarget().lastTickPosY) * (double)render3DEvent.getPartialTicks();
            mc.getRenderManager();
            double y = d3 - RenderManager.renderPosY;
            double d4 = aura.getTarget().lastTickPosZ + (aura.getTarget().posZ - aura.getTarget().lastTickPosZ) * (double)render3DEvent.getPartialTicks() + Math.cos(d) * rad;
            mc.getRenderManager();
            double z = d4 - RenderManager.renderPosZ;
            GL11.glVertex3d((double)x, (double)y, (double)z);
        }
        double d = aura.getTarget().lastTickPosX + (aura.getTarget().posX - aura.getTarget().lastTickPosX) * (double)render3DEvent.getPartialTicks() + 0.0 * rad;
        mc.getRenderManager();
        double x = d - RenderManager.renderPosX;
        double d5 = aura.getTarget().lastTickPosY + (aura.getTarget().posY - aura.getTarget().lastTickPosY) * (double)render3DEvent.getPartialTicks();
        mc.getRenderManager();
        double y = d5 - RenderManager.renderPosY;
        double d6 = aura.getTarget().lastTickPosZ + (aura.getTarget().posZ - aura.getTarget().lastTickPosZ) * (double)render3DEvent.getPartialTicks() + rad;
        mc.getRenderManager();
        double z = d6 - RenderManager.renderPosZ;
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
    }

    public boolean isThirded() {
        return this.thirded;
    }

    public void setThirded(boolean thirded) {
        this.thirded = thirded;
    }

    public static class Point {
        public double x;
        public double z;
        public int poscount;

        public Point(double x, double z, int poscount) {
            this.x = x;
            this.z = z;
            this.poscount = poscount;
        }

        public double getDistanceToPlayer() {
            Minecraft.getMinecraft();
            double x2 = Math.abs(Minecraft.thePlayer.posX - this.x);
            Minecraft.getMinecraft();
            double z2 = Math.abs(Minecraft.thePlayer.posZ - this.z);
            return Math.sqrt(x2 * x2 + z2 * z2);
        }
    }
}

