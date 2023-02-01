/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.event.events.move.StrafeEvent;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.combat.utils.RotationUtils;
import heaven.main.module.modules.combat.utils.rayCastUtil;
import heaven.main.module.modules.misc.AutoL;
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.Rotate;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.module.modules.world.ScaffoldUtils.Rotation;
import heaven.main.ui.RenderRotate;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.io.Serializable;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.optifine.util.MathUtils;
import org.lwjgl.opengl.GL11;

public class KillAura
extends Module {
    public static EntityLivingBase target;
    public EntityLivingBase blockTarget;
    private List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>(0);
    private boolean isBlocking;
    private int index;
    private final TimerUtil AttackTimer = new TimerUtil();
    private final TimerUtil SwitchTimer = new TimerUtil();
    private final Comparator<EntityLivingBase> angleComparator = Comparator.comparingDouble(e2 -> e2.getDistanceToEntity(Minecraft.thePlayer));
    private float yaw;
    private float pitch;
    private float alpha;
    private final Option<Boolean> players = new Option<Boolean>("Players", true);
    private final Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    private final Option<Boolean> animals = new Option<Boolean>("Animals", false);
    private final Option<Boolean> invis = new Option<Boolean>("Invisible", false);
    private final Numbers<Double> maxcps = new Numbers<Double>("MaxCPS", 9.0, 1.0, 50.0, 1.0);
    private final Numbers<Double> mincps = new Numbers<Double>("MinCPS", 9.0, 1.0, 50.0, 1.0);
    public static final Numbers<Double> range;
    private final Numbers<Double> blockRange = new Numbers<Double>("BlockRange", 1.0, 0.0, 5.0, 0.5);
    private final Numbers<Double> wallRange = new Numbers<Double>("WallRange", 8.0, 0.0, 12.0, 0.1);
    private final Numbers<Double> fovCheck = new Numbers<Double>("Fov", 180.0, 1.0, 180.0, 1.0);
    public final Mode<String> rotMode = new Mode("RotationMode", new String[]{"CustomSpeed", "Watchdog", "Legit", "SmoothPredict", "Less", "Prediction", "AAC", "HvH"}, "Watchdog");
    private final Numbers<Double> turnSpeed = new Numbers<Double>("TurnSpeed", 180.0, 0.0, 180.0, 1.0, () -> this.rotMode.is("CustomSpeed") || this.rotMode.is("Less"));
    private final Numbers<Double> switchDelay = new Numbers<Double>("SwitchDelay", 400.0, 10.0, 1000.0, 10.0);
    public final Mode<String> mode = new Mode("Mode", new String[]{"Switch", "Single", "Multi"}, "Switch");
    public final Mode<String> attackTiming = new Mode("AttackTiming", new String[]{"Pre", "Post", "Motion"}, "Post");
    private final Mode<String> sort = new Mode("Priority", new String[]{"Distance", "Health", "Armor", "FOV", "HurtTime"}, "Distance");
    private final Option<Boolean> autoBlock = new Option<Boolean>("AutoBlock", true);
    private final Mode<String> autoBlockMode = new Mode("AutoBlockMode", new String[]{"Vanilla", "Watchdog", "Watchdog2", "AAC"}, "Watchdog", this.autoBlock::get);
    private final Option<Boolean> raycast = new Option<Boolean>("RayCast", false);
    private final Mode<String> moveControl = new Mode("MoveControl", new String[]{"None", "YawMove", "YawMove2", "YawMotion"}, "None");
    private final Option<Boolean> awayAttack = new Option<Boolean>("AwayAttack", false, () -> (Boolean)this.raycast.get() == false);
    private final Option<Boolean> doubleTap = new Option<Boolean>("DoubleTap", false);
    private final Option<Boolean> autoDisabled = new Option<Boolean>("AutoDisabled", true);
    private final Option<Boolean> dc = new Option<Boolean>("ImitateDC", false);
    private final Option<Boolean> noinvattack = new Option<Boolean>("NoInvAttack", false);
    private final Option<Boolean> mark = new Option<Boolean>("Mark", true);
    private final Option<Boolean> onySward = new Option<Boolean>("OnlySward", false);
    private final Option<Boolean> visRange = new Option<Boolean>("VisualsRange", false);
    public final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, this.visRange::get);
    public final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, this.visRange::get);
    public final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, this.visRange::get);
    public static ArrayList<EntityLivingBase> attacked;
    public int killed;
    private float iyaw;
    private float ipitch;
    public static float sYaw;
    public static float sPitch;
    public static float aacB;
    private static final int[] randoms;

    public KillAura() {
        super("KillAura", new String[]{"ka", "killaura", "aura", "\u5149\u73af"}, ModuleType.Combat);
        this.addValues(this.mode, this.sort, this.attackTiming, this.maxcps, this.mincps, this.autoBlock, this.autoBlockMode, this.rotMode, range, this.blockRange, this.wallRange, this.players, this.mobs, this.animals, this.invis, this.raycast, this.moveControl, this.awayAttack, this.doubleTap, this.noinvattack, this.fovCheck, this.turnSpeed, this.switchDelay, this.dc, this.onySward, this.mark, this.visRange, this.r, this.g, this.b);
    }

    @Override
    public void onDisable() {
        target = null;
        this.blockTarget = null;
        this.targets.clear();
        if (((Boolean)this.autoBlock.getValue()).booleanValue() && this.hasSword()) {
            if (Minecraft.thePlayer.isBlocking() || this.isBlocking) {
                this.UnBlock();
            }
        }
        if (Minecraft.thePlayer.movementYaw != null) {
            Minecraft.thePlayer.movementYaw = null;
        }
    }

    @Override
    public void onEnable() {
        attacked.clear();
        target = null;
        this.blockTarget = null;
        this.index = 0;
        if (((Boolean)this.visRange.get()).booleanValue()) {
            this.alpha = 0.0f;
        }
        this.yaw = this.iyaw = EventPreUpdate.getYaw();
        this.pitch = this.ipitch = EventPreUpdate.getPitch();
        aacB = 0.0f;
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        block56: {
            this.setSuffix((Serializable)this.mode.getValue());
            if (((Boolean)this.noinvattack.getValue()).booleanValue() && KillAura.mc.currentScreen instanceof GuiContainer) {
                KillAura.target = null;
                this.blockTarget = null;
                this.targets.clear();
                return;
            }
            if (KillAura.target == null && ((Boolean)this.autoBlock.getValue()).booleanValue() && this.hasSword()) {
                if (Minecraft.thePlayer.isBlocking()) {
                    if (this.autoBlockMode.is("Vanilla")) {
                        this.UnBlock();
                    } else if (this.autoBlockMode.is("Vanilla")) {
                        Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                        this.isBlocking = false;
                    } else if (this.autoBlockMode.is("AAC")) {
                        this.UnBlock();
                    } else if (this.autoBlockMode.is("Watchdog2")) {
                        // empty if block
                    }
                }
            }
            if (((Boolean)this.autoDisabled.get()).booleanValue()) {
                if (!Minecraft.thePlayer.isEntityAlive()) {
                    this.isBlocking = false;
                    KillAura.target = null;
                    ClientNotification.sendClientMessage("KillAura", "Auto disable because player not alive or death", 4000L, ClientNotification.Type.WARNING);
                    this.setEnabled(false);
                    return;
                }
                if (Minecraft.thePlayer.ticksExisted <= 1) {
                    this.isBlocking = false;
                    KillAura.target = null;
                    ClientNotification.sendClientMessage("KillAura", "Auto disable because World change or world ticks too lower", 4000L, ClientNotification.Type.WARNING);
                    this.setEnabled(false);
                }
            }
            this.sortList(this.targets);
            if (((Boolean)this.visRange.get()).booleanValue()) {
                clamp = MathHelper.clamp_double(KillAura.mc.getDebugFPS() / 30, 1.0, 9999.0);
                if (this.alpha < 1.0f) {
                    this.alpha = (float)((double)this.alpha + (double)(1.0f - this.alpha) * (0.9900000095367432 / clamp));
                }
                this.alpha = MathHelper.clamp_float(this.alpha, 0.0f, 1.0f);
            }
            this.blockTarget = null;
            for (Entity entity : Minecraft.thePlayer.getEntityWorld().loadedEntityList) {
                if (!(entity instanceof EntityLivingBase) || !this.CanBlockToEntity(livingBase = (EntityLivingBase)entity)) continue;
                this.blockTarget = livingBase;
            }
            if (this.hasSword() && this.blockTarget != null && ((Boolean)this.autoBlock.getValue()).booleanValue() && !this.isBlocking) {
                if (this.autoBlockMode.isCurrentMode("Watchdog")) {
                    this.Block();
                } else if (this.autoBlockMode.is("Vanilla") || this.autoBlockMode.is("AAC")) {
                    Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem());
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration());
                    this.isBlocking = true;
                } else if (this.autoBlockMode.is("Watchdog2")) {
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.inventory.getCurrentItem(), 71999);
                }
            }
            this.targets = this.getTargets((Double)KillAura.range.getValue());
            this.targets.sort(this.angleComparator);
            if (this.targets.size() > 1 && this.mode.isCurrentMode("Switch") && this.SwitchTimer.delay(((Double)this.switchDelay.getValue()).longValue())) {
                ++this.index;
                this.SwitchTimer.reset();
            }
            if (Minecraft.thePlayer.ticksExisted % ((Double)this.switchDelay.getValue()).intValue() == 0 && this.targets.size() > 1 && this.mode.isCurrentMode("Single")) {
                if ((double)KillAura.target.getDistanceToEntity(Minecraft.thePlayer) > (Double)KillAura.range.getValue()) {
                    ++this.index;
                } else if (KillAura.target.isDead) {
                    ++this.index;
                }
            }
            if (KillAura.target != null) {
                KillAura.target = null;
            }
            if (this.targets.isEmpty()) break block56;
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
            KillAura.target = this.targets.get(this.index);
            var2_3 = (String)this.rotMode.get();
            var3_5 = -1;
            switch (var2_3.hashCode()) {
                case -120870090: {
                    if (!var2_3.equals("CustomSpeed")) break;
                    var3_5 = 0;
                    break;
                }
                case 2364857: {
                    if (!var2_3.equals("Less")) break;
                    var3_5 = 1;
                    break;
                }
                case -925505749: {
                    if (!var2_3.equals("SmoothPredict")) break;
                    var3_5 = 2;
                    break;
                }
                case 73298841: {
                    if (!var2_3.equals("Legit")) break;
                    var3_5 = 3;
                    break;
                }
                case 72922: {
                    if (!var2_3.equals("HvH")) break;
                    var3_5 = 4;
                    break;
                }
                case 64547: {
                    if (!var2_3.equals("AAC")) break;
                    var3_5 = 5;
                    break;
                }
                case -1140267857: {
                    if (!var2_3.equals("Prediction")) break;
                    var3_5 = 6;
                    break;
                }
                case 609795629: {
                    if (!var2_3.equals("Watchdog")) break;
                    var3_5 = 7;
                }
            }
            switch (var3_5) {
                case 0: {
                    if ((Double)this.turnSpeed.getValue() == 0.0) break;
                    turnSpeed = (Double)this.turnSpeed.getValue() >= 0.0 ? (Double)this.turnSpeed.getValue() : MathUtils.randomNumber((Double)this.turnSpeed.getValue() - 10.0, (Double)this.turnSpeed.getValue() + 10.0);
                    limitedRotation = this.limitAngleChange(new Rotation(this.yaw, this.pitch), new Rotation(this.getRotation(KillAura.target)[0], this.getRotation(KillAura.target)[1]), (float)turnSpeed);
                    limitedRotation.fixedSensitivity(Float.valueOf(KillAura.mc.gameSettings.mouseSensitivity));
                    EventPreUpdate.setYaw(limitedRotation.getYaw());
                    EventPreUpdate.setPitch(limitedRotation.getPitch());
                    break;
                }
                case 1: {
                    reach = Math.min((Double)KillAura.range.getValue() + (Double)this.blockRange.getValue(), (double)Minecraft.thePlayer.getDistanceToEntity(KillAura.target)) + 1.0;
                    raycastedEntity = rayCastUtil.raycastEntity(reach, this.yaw, this.pitch, (rayCastUtil.IEntityFilter)LambdaMetafactory.metafactory(null, null, null, (Lnet/minecraft/entity/Entity;)Z, lambda$onUpdate$3(net.minecraft.entity.Entity ), (Lnet/minecraft/entity/Entity;)Z)());
                    Hitble = raycastedEntity instanceof EntityLivingBase;
                    if (this.isFaced(KillAura.target, reach) || Hitble) ** GOTO lbl-1000
                    if (Minecraft.thePlayer.getDistanceToEntity(KillAura.target) < 1.5f) lbl-1000:
                    // 2 sources

                    {
                        v0 = 0.0;
                    } else {
                        v0 = (Double)this.turnSpeed.get();
                    }
                    turnSpeed = v0;
                    limitedRotation = this.limitAngleChange(new Rotation(this.yaw, this.pitch), new Rotation(RotationUtil.getPredictedRotations(KillAura.target)[0], RotationUtil.getPredictedRotations(KillAura.target)[1]), (float)turnSpeed);
                    limitedRotation.fixedSensitivity(Float.valueOf(KillAura.mc.gameSettings.mouseSensitivity));
                    EventPreUpdate.setYaw(limitedRotation.getYaw());
                    EventPreUpdate.setPitch(limitedRotation.getPitch());
                    break;
                }
                case 2: {
                    reach = Math.min((Double)KillAura.range.getValue() + (Double)this.blockRange.getValue(), (double)Minecraft.thePlayer.getDistanceToEntity(KillAura.target)) + 1.0;
                    if (this.isFaced(KillAura.target, reach)) ** GOTO lbl-1000
                    if (Minecraft.thePlayer.getDistanceToEntity(KillAura.target) < 2.0f) lbl-1000:
                    // 2 sources

                    {
                        v1 = 35.0 + MoveUtils.getDirection() - (double)MathUtil.getRandomInRange(-6.8f, 6.8f);
                    } else {
                        v1 = 50.0 + MoveUtils.getBPS() - (double)MathUtil.getRandomInRange(-6.8f, 6.8f);
                    }
                    turnSpeed = v1;
                    limitedRotation = this.limitAngleChange(new Rotation(this.yaw, this.pitch), new Rotation(RotationUtil.getSmoothRot(KillAura.target)[0], RotationUtil.getSmoothRot(KillAura.target)[1]), (float)turnSpeed);
                    limitedRotation.fixedSensitivity(Float.valueOf(KillAura.mc.gameSettings.mouseSensitivity));
                    EventPreUpdate.setYaw(limitedRotation.getYaw());
                    EventPreUpdate.setPitch(limitedRotation.getPitch());
                    break;
                }
                case 3: {
                    rots = RotationUtil.getAnglesAGC(KillAura.target);
                    yaw = rots[0] + MathUtil.getRandomInRange(-6.8f, 6.8f);
                    pitch = rots[1] + MathUtil.getRandomInRange(-6.8f, 6.8f);
                    EventPreUpdate.setYaw(yaw);
                    EventPreUpdate.setPitch(pitch);
                    break;
                }
                case 4: {
                    customRotation = RotationUtil.getCustomRotation(RotationUtil.getLocation(this.targets.get(this.index).getEntityBoundingBox()));
                    EventPreUpdate.setYaw(customRotation[0]);
                    EventPreUpdate.setPitch(customRotation[1]);
                    break;
                }
                case 5: {
                    rots = this.customRots(event);
                    KillAura.aacB = (float)((double)KillAura.aacB / 2.0);
                    EventPreUpdate.setYaw(rots[0]);
                    EventPreUpdate.setPitch(rots[1]);
                    break;
                }
                case 6: {
                    rotations = RotationUtil.getRotationsToEnt(KillAura.target);
                    rotations[0] = (float)((double)rotations[0] + (Math.abs(KillAura.target.posX - KillAura.target.lastTickPosX) - Math.abs(KillAura.target.posZ - KillAura.target.lastTickPosZ)) * 0.0 * 2.0);
                    rotations[1] = (float)((double)rotations[1] + (Math.abs(KillAura.target.posY - KillAura.target.lastTickPosY) - Math.abs(KillAura.target.getEntityBoundingBox().minY - KillAura.target.lastTickPosY)) * 0.0 * 2.0);
                    EventPreUpdate.setYaw(rotations[0]);
                    EventPreUpdate.setPitch(rotations[1]);
                    break;
                }
                case 7: {
                    reach = Math.min((Double)KillAura.range.getValue() + (Double)this.blockRange.getValue(), (double)Minecraft.thePlayer.getDistanceToEntity(KillAura.target)) + 1.0;
                    if (this.isFaced(KillAura.target, reach)) ** GOTO lbl-1000
                    if (Minecraft.thePlayer.getDistanceToEntity(KillAura.target) < 1.0f) lbl-1000:
                    // 2 sources

                    {
                        v2 = 65.0 + MoveUtils.getDirection() - (double)MathUtil.getRandomInRange(-6.8f, 6.8f);
                    } else {
                        v2 = (double)(Minecraft.thePlayer.onGround != false ? 30 : 0) + MoveUtils.getBPS() - (double)MathUtil.getRandomInRange(-6.8f, 6.8f);
                    }
                    turnSpeed = v2;
                    frac = MathHelper.clamp_float((float)(1.0 - turnSpeed / 100.0), 0.1f, 1.0f);
                    rotations = RotationUtil.getSmoothRot(KillAura.target);
                    this.iyaw += (rotations[0] - this.iyaw) * frac;
                    this.ipitch += (rotations[1] - this.ipitch) * frac;
                    this.yaw = this.iyaw;
                    EventPreUpdate.setYaw(this.yaw);
                    this.pitch = this.ipitch;
                    EventPreUpdate.setPitch(this.pitch);
                    break;
                }
            }
            if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled()) {
                new RenderRotate(event.getYaw(), event.getPitch());
            }
        }
    }

    @EventHandler
    public void onStrafe(StrafeEvent event) {
        if (!this.moveControl.is("None")) {
            if (this.moveControl.is("YawMotion") && target != null) {
                event.setCancelled(true);
                this.getModule(Scaffold.class);
                Scaffold.silentRotationStrafe(event, this.yaw);
            }
            if (this.moveControl.is("YawMove2")) {
                Minecraft.thePlayer.movementYaw = target != null ? Float.valueOf(this.yaw) : null;
            } else if (Minecraft.thePlayer.movementYaw != null) {
                Minecraft.thePlayer.movementYaw = null;
            }
            if (this.moveControl.is("YawMove")) {
                event.setCancelled(true);
                float yaw = this.yaw;
                float strafe = event.getStrafe();
                float forward = event.getForward();
                float friction = event.getFriction();
                float f = strafe * strafe + forward * forward;
                if (f >= 1.0E-4f) {
                    if ((f = MathHelper.sqrt_float(f)) < 1.0f) {
                        f = 1.0f;
                    }
                    f = friction / f;
                    float yawSin = (float)MathHelper.sin((double)yaw * Math.PI / 180.0);
                    float yawCos = (float)MathHelper.cos((double)yaw * Math.PI / 180.0);
                    Minecraft.thePlayer.motionX += (double)((strafe *= f) * yawCos - (forward *= f) * yawSin);
                    Minecraft.thePlayer.motionZ += (double)(forward * yawCos + strafe * yawSin);
                }
            }
        }
    }

    @EventHandler
    private void onPostUpdate(EventPostUpdate e) {
        this.yaw = EventPreUpdate.getYaw();
        this.pitch = EventPreUpdate.getPitch();
    }

    @EventHandler
    private void onMotionUpdate(MotionUpdateEvent e) {
        if (this.attackTiming.isCurrentMode("Pre") && e.getState() == MotionUpdateEvent.State.POST) {
            return;
        }
        if (this.attackTiming.isCurrentMode("Post") && e.getState() == MotionUpdateEvent.State.PRE) {
            return;
        }
        if (target != null) {
            if (this.shouldAttack()) {
                if (((Boolean)this.dc.getValue()).booleanValue() && KillAura.target.hurtResistantTime > 12 && (double)KillAura.target.hurtResistantTime < 17.0 + Math.random() * (Math.random() * 2.0 - 1.0)) {
                    return;
                }
                if (this.hasSword()) {
                    if (Minecraft.thePlayer.isBlocking() && this.hasSword()) {
                        if (this.autoBlockMode.is("Watchdog")) {
                            this.UnBlock();
                        } else {
                            Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                            this.isBlocking = false;
                        }
                    }
                }
                if ((double)Minecraft.thePlayer.getDistanceToEntity(target) <= (Double)range.getValue()) {
                    Minecraft.thePlayer.swingItem();
                }
                if (!attacked.contains(target) && target instanceof EntityPlayer) {
                    attacked.add(target);
                }
                if (this.mode.isCurrentMode("Multi")) {
                    for (EntityLivingBase ent : this.targets) {
                        if (!((double)Minecraft.thePlayer.getDistanceToEntity(ent) <= (Double)range.getValue())) continue;
                        mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity((Entity)ent, C02PacketUseEntity.Action.ATTACK));
                    }
                } else {
                    boolean Hitble;
                    EntityLivingBase fixTarget = target;
                    double reach = Math.min((Double)range.getValue() + (Double)this.blockRange.getValue(), (double)Minecraft.thePlayer.getDistanceToEntity(fixTarget)) + 1.0;
                    if (((Boolean)this.raycast.getValue()).booleanValue()) {
                        Entity raycastedEntity = rayCastUtil.raycastEntity(reach, this.yaw, this.pitch, entity -> {
                            if (!(entity instanceof EntityLivingBase)) return false;
                            if (entity instanceof EntityArmorStand) return false;
                            if (!Teams.isOnSameTeam(entity)) return true;
                            if (Minecraft.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox()).isEmpty()) return false;
                            return true;
                        });
                        if (raycastedEntity instanceof EntityLivingBase) {
                            fixTarget = (EntityLivingBase)raycastedEntity;
                            Hitble = true;
                        } else {
                            Hitble = false;
                        }
                    } else {
                        Hitble = (Boolean)this.awayAttack.get() == false ? (Double)this.turnSpeed.getValue() == 0.0 || this.isFaced(target, reach) : true;
                    }
                    if ((double)Minecraft.thePlayer.getDistanceToEntity(fixTarget) <= (Double)range.getValue() && Hitble) {
                        if (((Boolean)this.doubleTap.get()).booleanValue()) {
                            Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity((Entity)fixTarget, C02PacketUseEntity.Action.ATTACK));
                        }
                        mc.getNetHandler().getNetworkManager().sendPacket(new C02PacketUseEntity((Entity)fixTarget, C02PacketUseEntity.Action.ATTACK));
                    }
                }
                this.AttackTimer.reset();
            }
            if (!Minecraft.thePlayer.isBlocking() && this.hasSword() && ((Boolean)this.autoBlock.getValue()).booleanValue()) {
                if (this.autoBlockMode.isCurrentMode("Watchdog")) {
                    this.Block();
                } else if (this.autoBlockMode.is("Vanilla")) {
                    Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem());
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.getHeldItem(), Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration());
                    this.isBlocking = true;
                } else if (this.autoBlockMode.is("AAC")) {
                    if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                        Minecraft.playerController.interactWithEntitySendPacket(Minecraft.thePlayer, target);
                        this.sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getHeldItem()));
                    }
                } else if (this.autoBlockMode.is("Watchdog2")) {
                    Minecraft.thePlayer.setItemInUse(Minecraft.thePlayer.inventory.getCurrentItem(), 71999);
                    if (Minecraft.thePlayer.ticksExisted % 3 == 0) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    } else {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getHeldItem()));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onRender3D(EventRender3D event) {
        if (((Boolean)this.visRange.get()).booleanValue()) {
            double z;
            double y;
            double x;
            double d;
            RenderUtil.pre3D();
            GL11.glLineWidth((float)6.0f);
            GL11.glBegin((int)3);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)this.alpha);
            for (d = 0.0; d < Math.PI * 2; d += 0.12319971190548208) {
                double d2 = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * (double)event.getPartialTicks() + Math.sin(d) * (Double)range.get();
                mc.getRenderManager();
                x = d2 - RenderManager.renderPosX;
                double d3 = Minecraft.thePlayer.lastTickPosY + (Minecraft.thePlayer.posY - Minecraft.thePlayer.lastTickPosY) * (double)event.getPartialTicks();
                mc.getRenderManager();
                y = d3 - RenderManager.renderPosY;
                double d4 = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * (double)event.getPartialTicks() + Math.cos(d) * (Double)range.get();
                mc.getRenderManager();
                z = d4 - RenderManager.renderPosZ;
                GL11.glVertex3d((double)x, (double)y, (double)z);
            }
            GL11.glEnd();
            GL11.glLineWidth((float)3.0f);
            GL11.glBegin((int)3);
            GL11.glColor4f((float)((float)((Double)this.r.get()).intValue() / 255.0f), (float)((float)((Double)this.g.get()).intValue() / 255.0f), (float)((float)((Double)this.b.get()).intValue() / 255.0f), (float)1.0f);
            for (d = 0.0; d < Math.PI * 2; d += 0.12319971190548208) {
                double d5 = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * (double)event.getPartialTicks() + Math.sin(d) * (Double)range.get();
                mc.getRenderManager();
                x = d5 - RenderManager.renderPosX;
                double d6 = Minecraft.thePlayer.lastTickPosY + (Minecraft.thePlayer.posY - Minecraft.thePlayer.lastTickPosY) * (double)event.getPartialTicks();
                mc.getRenderManager();
                y = d6 - RenderManager.renderPosY;
                double d7 = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * (double)event.getPartialTicks() + Math.cos(d) * (Double)range.get();
                mc.getRenderManager();
                z = d7 - RenderManager.renderPosZ;
                GL11.glVertex3d((double)x, (double)y, (double)z);
            }
            GL11.glEnd();
            RenderUtil.post3D();
        }
    }

    private boolean hasSword() {
        if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
            return Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
        }
        return false;
    }

    public boolean checkSword() {
        if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
            return Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
        }
        return false;
    }

    public boolean Block() {
        if (this.hasSword()) {
            KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            if (Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getCurrentItem())) {
                mc.getItemRenderer().resetEquippedProgress2();
            }
            this.isBlocking = true;
        }
        return false;
    }

    private void UnBlock() {
        if (this.hasSword() && this.isBlocking) {
            KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            Minecraft.playerController.onStoppedUsingItem(Minecraft.thePlayer);
            this.isBlocking = false;
        }
    }

    public boolean shouldAttack() {
        return this.AttackTimer.hasReached(1000.0 / MathUtil.randomNumber(((Double)this.maxcps.get()).intValue(), ((Double)this.mincps.get()).intValue()));
    }

    private List<EntityLivingBase> getTargets(double n) {
        ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
        for (Entity entity : Minecraft.thePlayer.getEntityWorld().loadedEntityList) {
            EntityLivingBase entityLivingBase;
            if (!(entity instanceof EntityLivingBase) || !this.CanAttack(entityLivingBase = (EntityLivingBase)entity)) continue;
            list.add(entityLivingBase);
        }
        return list;
    }

    private boolean CanAttack(EntityLivingBase e2) {
        block48: {
            block47: {
                block46: {
                    block45: {
                        block40: {
                            block44: {
                                block43: {
                                    block42: {
                                        block41: {
                                            if (!((Boolean)this.onySward.getValue()).booleanValue()) break block40;
                                            if (e2.isDead || e2.getHealth() <= 0.0f) break block41;
                                            if (e2 != Minecraft.thePlayer) break block42;
                                        }
                                        if (attacked.contains(e2)) {
                                            if (this.isEnabled((Class<? extends Module>)AutoL.class)) {
                                                Minecraft.thePlayer.sendChatMessage(AutoL.getAutoLMessage(target.getName()));
                                            }
                                            ++this.killed;
                                            attacked.remove(e2);
                                        }
                                        return false;
                                    }
                                    if ((double)Minecraft.thePlayer.getDistanceToEntity(e2) > (Double)range.getValue()) {
                                        return false;
                                    }
                                    if (e2.isInvisible() && !((Boolean)this.invis.getValue()).booleanValue()) {
                                        return false;
                                    }
                                    if (!e2.isEntityAlive() || e2.isDead) {
                                        return false;
                                    }
                                    if (e2 == Minecraft.thePlayer || FriendManager.isFriend(e2.getName()) || e2.isDead) break block43;
                                    if (Minecraft.thePlayer.getHealth() != 0.0f) break block44;
                                }
                                return false;
                            }
                            if ((e2 instanceof EntityMob || e2 instanceof EntityGhast || e2 instanceof EntityGolem || e2 instanceof EntityDragon || e2 instanceof EntitySlime) && ((Boolean)this.mobs.getValue()).booleanValue() && ((Boolean)this.onySward.getValue()).booleanValue() && this.checkSword()) {
                                return true;
                            }
                            if ((e2 instanceof EntitySquid || e2 instanceof EntityBat || e2 instanceof EntityVillager) && ((Boolean)this.animals.getValue()).booleanValue() && ((Boolean)this.onySward.getValue()).booleanValue() && this.checkSword()) {
                                return true;
                            }
                            if (e2 instanceof EntityAnimal && ((Boolean)this.animals.getValue()).booleanValue() && ((Boolean)this.onySward.getValue()).booleanValue() && this.checkSword()) {
                                return true;
                            }
                            AntiBot ab2 = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
                            if (AntiBot.isBot(e2)) {
                                return false;
                            }
                            if (e2 instanceof EntityPlayer && ((Boolean)this.players.getValue()).booleanValue()) {
                                Teams cfr_ignored_0 = (Teams)Client.instance.getModuleManager().getModuleByClass(Teams.class);
                                if (!Teams.isOnSameTeam(e2) && ((Boolean)this.onySward.getValue()).booleanValue() && this.checkSword()) {
                                    return true;
                                }
                            }
                            if (!Minecraft.thePlayer.canEntityBeSeen(e2)) {
                                if ((double)Minecraft.thePlayer.getDistanceToEntity(e2) > (Double)this.wallRange.getValue()) {
                                    return false;
                                }
                            }
                            if (Math.abs(RotationUtils.getYawDifference(Minecraft.thePlayer.rotationYaw, e2.posX, e2.posY, e2.posZ)) > ((Double)this.fovCheck.getValue()).floatValue()) {
                                return false;
                            }
                            return false;
                        }
                        if (e2.isDead || e2.getHealth() <= 0.0f) break block45;
                        if (e2 != Minecraft.thePlayer) break block46;
                    }
                    if (attacked.contains(e2)) {
                        if (this.isEnabled((Class<? extends Module>)AutoL.class)) {
                            Minecraft.thePlayer.sendChatMessage(AutoL.getAutoLMessage(target.getName()));
                        }
                        ++this.killed;
                        attacked.remove(e2);
                    }
                    return false;
                }
                if ((double)Minecraft.thePlayer.getDistanceToEntity(e2) > (Double)range.getValue()) {
                    return false;
                }
                if (e2.isInvisible() && !((Boolean)this.invis.getValue()).booleanValue()) {
                    return false;
                }
                if (!e2.isEntityAlive() || e2.isDead) {
                    return false;
                }
                if (e2 == Minecraft.thePlayer || FriendManager.isFriend(e2.getName()) || e2.isDead) break block47;
                if (Minecraft.thePlayer.getHealth() != 0.0f) break block48;
            }
            return false;
        }
        if ((e2 instanceof EntityMob || e2 instanceof EntityGhast || e2 instanceof EntityGolem || e2 instanceof EntityDragon || e2 instanceof EntitySlime) && ((Boolean)this.mobs.getValue()).booleanValue()) {
            return true;
        }
        if ((e2 instanceof EntitySquid || e2 instanceof EntityBat || e2 instanceof EntityVillager) && ((Boolean)this.animals.getValue()).booleanValue()) {
            return true;
        }
        if (e2 instanceof EntityAnimal && ((Boolean)this.animals.getValue()).booleanValue()) {
            return true;
        }
        AntiBot ab2 = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (AntiBot.isBot(e2)) {
            return false;
        }
        if (e2 instanceof EntityPlayer && ((Boolean)this.players.getValue()).booleanValue()) {
            Teams cfr_ignored_1 = (Teams)Client.instance.getModuleManager().getModuleByClass(Teams.class);
            if (!Teams.isOnSameTeam(e2)) {
                return true;
            }
        }
        if (!Minecraft.thePlayer.canEntityBeSeen(e2)) {
            if ((double)Minecraft.thePlayer.getDistanceToEntity(e2) > (Double)this.wallRange.getValue()) {
                return false;
            }
        }
        if (Math.abs(RotationUtils.getYawDifference(Minecraft.thePlayer.rotationYaw, e2.posX, e2.posY, e2.posZ)) > ((Double)this.fovCheck.getValue()).floatValue()) {
            return false;
        }
        return false;
    }

    private boolean CanBlockToEntity(EntityLivingBase e2) {
        block17: {
            block16: {
                if ((double)Minecraft.thePlayer.getDistanceToEntity(e2) > (Double)range.getValue() + (Double)this.blockRange.getValue()) {
                    return false;
                }
                if (e2.isInvisible() && !((Boolean)this.invis.getValue()).booleanValue()) {
                    return false;
                }
                if (e2 == Minecraft.thePlayer || FriendManager.isFriend(e2.getName())) {
                    return false;
                }
                if (!e2.isEntityAlive()) {
                    return false;
                }
                if (e2 == Minecraft.thePlayer || FriendManager.isFriend(e2.getName()) || e2.isDead) break block16;
                if (Minecraft.thePlayer.getHealth() != 0.0f) break block17;
            }
            return false;
        }
        if ((e2 instanceof EntityMob || e2 instanceof EntityGhast || e2 instanceof EntityGolem || e2 instanceof EntityDragon || e2 instanceof EntitySlime) && ((Boolean)this.mobs.getValue()).booleanValue()) {
            return true;
        }
        if ((e2 instanceof EntitySquid || e2 instanceof EntityBat || e2 instanceof EntityVillager) && ((Boolean)this.animals.getValue()).booleanValue()) {
            return true;
        }
        if (e2 instanceof EntityAnimal && ((Boolean)this.animals.getValue()).booleanValue()) {
            return true;
        }
        AntiBot ab2 = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (AntiBot.isBot(e2)) {
            return false;
        }
        if (e2 instanceof EntityPlayer && ((Boolean)this.players.getValue()).booleanValue()) {
            Teams cfr_ignored_0 = (Teams)Client.instance.getModuleManager().getModuleByClass(Teams.class);
            if (!Teams.isOnSameTeam(e2)) {
                return true;
            }
        }
        if (!Minecraft.thePlayer.canEntityBeSeen(e2)) {
            if ((double)Minecraft.thePlayer.getDistanceToEntity(e2) > (Double)this.wallRange.getValue() + (Double)this.blockRange.getValue()) {
                return false;
            }
        }
        if (Math.abs(RotationUtils.getYawDifference(Minecraft.thePlayer.rotationYaw, e2.posX, e2.posY, e2.posZ)) > ((Double)this.fovCheck.getValue()).floatValue()) {
            return false;
        }
        return false;
    }

    public float[] getRotation(EntityLivingBase target) {
        double xDiff = target.posX - Minecraft.thePlayer.posX;
        double yDiff = target.posY - Minecraft.thePlayer.posY - 0.2;
        double zDiff = target.posZ - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        float[] array = new float[2];
        int n = 0;
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        array[n] = rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        int n3 = 1;
        float rotationPitch = Minecraft.thePlayer.rotationPitch;
        array[n3] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return array;
    }

    public Rotation limitAngleChange(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
        float yawDifference = KillAura.getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = KillAura.getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    public double getRotationDifference(Rotation rotation) {
        return KillAura.getRotationDifference(rotation, new Rotation(this.yaw, this.pitch));
    }

    public static double getRotationDifference(Rotation a, Rotation b) {
        return Math.hypot(KillAura.getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    private static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public boolean isFaced(EntityLivingBase targetEntity, double blockReachDistance) {
        return rayCastUtil.raycastEntity(blockReachDistance, entity -> entity == targetEntity) != null;
    }

    private void sortList(List<? extends EntityLivingBase> weed) {
        if (this.sort.isCurrentMode("Armor")) {
            weed.sort(Comparator.comparingInt(o -> o instanceof EntityPlayer ? ((EntityPlayer)o).inventory.getTotalArmorValue() : (int)o.getHealth()));
        }
        if (this.sort.isCurrentMode("Distance")) {
            weed.sort((o1, o2) -> (int)(o1.getDistanceToEntity(Minecraft.thePlayer) - o2.getDistanceToEntity(Minecraft.thePlayer)));
        }
        if (this.sort.isCurrentMode("HurtTime")) {
            weed.sort(Comparator.comparingInt(o -> o.hurtTime));
        }
        if (this.sort.isCurrentMode("FOV")) {
            weed.sort(Comparator.comparingDouble(o -> KillAura.getDistanceBetweenAngles(Minecraft.thePlayer.rotationPitch, RotationUtil.getPredictedRotations(o)[0])));
        }
        if (this.sort.isCurrentMode("Angle")) {
            weed.sort((o1, o2) -> {
                float[] rot1 = this.getRotation((EntityLivingBase)o1);
                float[] rot2 = this.getRotation((EntityLivingBase)o2);
                return (int)(Minecraft.thePlayer.rotationYaw - rot1[0] - (Minecraft.thePlayer.rotationYaw - rot2[0]));
            });
        }
        if (this.sort.isCurrentMode("Health")) {
            weed.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
    }

    public EntityLivingBase getTarget() {
        return target;
    }

    public float[] customRots(EventPreUpdate em) {
        double randomYaw = 0.05;
        double randomPitch = 0.05;
        float[] rotsN = this.getCustomRotsChange(sYaw, sPitch, KillAura.target.posX + MathUtils.randomNumber(5.0, -5.0) * 0.05, KillAura.target.posY + MathUtils.randomNumber(5.0, -5.0) * 0.05, KillAura.target.posZ + MathUtils.randomNumber(5.0, -5.0) * 0.05);
        float targetYaw = rotsN[0];
        float yawFactor = targetYaw * targetYaw / (4.7f * targetYaw);
        if (targetYaw < 5.0f) {
            yawFactor = targetYaw * targetYaw / (3.7f * targetYaw);
        }
        if (Math.abs(yawFactor) > 7.0f) {
            aacB = yawFactor * 7.0f;
            yawFactor = targetYaw * targetYaw / (3.7f * targetYaw);
        } else {
            yawFactor = targetYaw * targetYaw / (6.7f * targetYaw) + aacB;
        }
        EventPreUpdate.setYaw(sYaw + yawFactor);
        float targetPitch = rotsN[1];
        float pitchFactor = targetPitch / 3.7f;
        EventPreUpdate.setPitch(sPitch + pitchFactor);
        return new float[]{sYaw += yawFactor, sPitch += pitchFactor};
    }

    public float[] getCustomRotsChange(float yaw, float pitch, double x, double y, double z) {
        double n = x;
        double xDiff = n - Minecraft.thePlayer.posX;
        double n2 = z;
        double zDiff = n2 - Minecraft.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        double mult = 1.0 / (dist + 1.0E-4) * 2.0;
        if (mult > 0.2) {
            mult = 0.2;
        }
        WorldClient theWorld = Minecraft.theWorld;
        EntityPlayerSP thePlayer = Minecraft.thePlayer;
        if (!theWorld.getEntitiesWithinAABBExcludingEntity(thePlayer, Minecraft.thePlayer.boundingBox).contains(target)) {
            x += 0.3 * (double)randoms[0];
            y -= 0.4 + mult * (double)randoms[1];
            z += 0.3 * (double)randoms[2];
        }
        double n4 = x;
        xDiff = n4 - Minecraft.thePlayer.posX;
        double n5 = z;
        zDiff = n5 - Minecraft.thePlayer.posZ;
        double n6 = y;
        double yDiff = n6 - Minecraft.thePlayer.posY;
        float yawToEntity = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitchToEntity = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{MathHelper.wrapAngleTo180_float(-(yaw - yawToEntity)), -MathHelper.wrapAngleTo180_float(pitch - pitchToEntity) - 2.5f};
    }

    @EventHandler
    private void render(EventRender3D e) {
        if (((Boolean)this.mark.getValue()).booleanValue() && target != null) {
            if ((double)Minecraft.thePlayer.getDistanceToEntity(target) > (Double)range.getValue()) {
                return;
            }
            Color color = new Color(255, 255, 255);
            if (KillAura.target.hurtResistantTime > 0) {
                color = new Color(((Double)HUD.r.get()).intValue(), ((Double)HUD.g.get()).intValue(), ((Double)HUD.b.get()).intValue());
            }
            double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * (double)KillAura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double y = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * (double)KillAura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * (double)KillAura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if (target instanceof EntityPlayer) {
                x -= 0.5;
                z -= 0.5;
                double d = (double)target.getEyeHeight() + 0.35;
                double d2 = target.isSneaking() ? 0.25 : 0.0;
                double mid = 0.5;
                GL11.glPushMatrix();
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glTranslated((double)(x + mid), (double)((y += d - d2) + mid), (double)(z + mid));
                GL11.glRotated((double)(-KillAura.target.rotationYaw % 360.0f), (double)0.0, (double)1.0, (double)0.0);
                GL11.glTranslated((double)(-(x + mid)), (double)(-(y + mid)), (double)(-(z + mid)));
                GL11.glDisable((int)3553);
                GL11.glEnable((int)2848);
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
                GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.5f);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
                GL11.glDisable((int)2848);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                GL11.glPopMatrix();
            } else {
                double width = KillAura.target.getEntityBoundingBox().maxZ - KillAura.target.getEntityBoundingBox().minZ;
                double height = 0.1;
                float red = 0.0f;
                float green = 0.5f;
                float blue = 1.0f;
                float alpha = 0.5f;
                float lineRed = 0.0f;
                float lineGreen = 0.5f;
                float lineBlue = 1.0f;
                float lineAlpha = 1.0f;
                float lineWidth = 2.0f;
                RenderUtil.drawEntityESP(x, y + (double)target.getEyeHeight() + 0.25, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWidth);
            }
        }
    }

    private void applyStrafeToPlayer(StrafeEvent event) {
        float d;
        EntityPlayerSP player = Minecraft.thePlayer;
        int dif = (int)((MathHelper.wrapAngleTo180_float(player.rotationYaw - this.yaw - 23.5f - 135.0f) + 180.0f) / 45.0f);
        float yaw = this.yaw;
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
            }
        }
        if (calcForward > 1.0f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1.0f || calcForward > -0.9f && calcForward < -0.3f) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1.0f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
            calcStrafe *= 0.5f;
        }
        if ((d = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
            if ((d = MathHelper.sqrt_float(d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            float yawSin = (float)MathHelper.sin((double)yaw * Math.PI / 180.0);
            float yawCos = (float)MathHelper.cos((double)yaw * Math.PI / 180.0);
            player.motionX += (double)((calcStrafe *= d) * yawCos) - (double)(calcForward *= d) * (double)yawSin;
            player.motionZ += (double)(calcForward * yawCos) + (double)calcStrafe * (double)yawSin;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static /* synthetic */ boolean lambda$onUpdate$3(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) return false;
        if (entity instanceof EntityArmorStand) return false;
        if (!Teams.isOnSameTeam(entity)) return true;
        if (Minecraft.theWorld.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox()).isEmpty()) return false;
        return true;
    }

    static {
        range = new Numbers<Double>("Range", 4.2, 0.1, 10.0, 0.1);
        attacked = new ArrayList();
        randoms = new int[]{0, 1, 0};
    }
}

