/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  io.netty.util.internal.ThreadLocalRandom
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.module.modules.movement;

import com.google.common.util.concurrent.AtomicDouble;
import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventStep;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.combat.TargetStrafe;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Jesus;
import heaven.main.module.modules.world.GameSpeed;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.PlayerUtil;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

public class Speed
extends Module {
    private final String[] SpeedMode = new String[]{"Watchdog", "ACRGround", "NCP", "OldNCP", "SpartanBhop", "AAC3.5.0", "Matrix", "LowHop", "Custom", "Jump", "HmXix", "VerusYport"};
    public final Mode<String> mode = new Mode("Mode", this.SpeedMode, this.SpeedMode[0]);
    private final Option<Boolean> should = new Option<Boolean>("LowHopJump", false, () -> this.mode.is("LowHop"));
    private final Option<Boolean> watercheck = new Option<Boolean>("WaterCheck", false);
    private final Numbers<Double> vspeed = new Numbers<Double>("CustomSpeed", 3.0, 0.1, 5.0, 0.01, () -> this.mode.is("Custom"));
    private final Numbers<Double> customy = new Numbers<Double>("CustomY", 0.42, 0.01, 1.0, 0.01, () -> this.mode.is("Custom"));
    public static final Option<Boolean> dmgboost = new Option<Boolean>("DamageBoost", false);
    public static final Numbers<Double> dmgzoom = new Numbers<Double>("DamageBoosts", 2.0, 0.1, 10.0, 0.1, dmgboost::getValue);
    private final Option<Boolean> timerboost = new Option<Boolean>("TimerBoost", false);
    private final Numbers<Double> timermax = new Numbers<Double>("TimerMax", 0.98, 0.4, 5.0, 0.01, this.timerboost::getValue);
    private final Numbers<Double> timermin = new Numbers<Double>("TimerMin", 1.17, 0.4, 5.0, 0.01, this.timerboost::getValue);
    private final Numbers<Double> timertick = new Numbers<Double>("TimerTick", 77.2, 0.0, 200.0, 1.0, this.timerboost::getValue);
    private final Option<Boolean> ncpZoom = new Option<Boolean>("NCPZoom", false, () -> this.mode.is("OldNCP"));
    private final Option<Boolean> jumpModeNoStrafe = new Option<Boolean>("JumpModeNoStrafe", false, () -> this.mode.is("Jump"));
    private final Option<Boolean> autodisable = new Option<Boolean>("DisableOnDeath", false);
    private final TimeHelper timer;
    private final TimeHelper lastCheck;
    private final AtomicDouble hDist = new AtomicDouble();
    private int airMoves;
    private int ticks;
    private int stage;
    public int level = 1;
    private double slow;
    private double distance;
    private double waterSpeed;
    private double moveSpeed;
    private double speed;
    private double xDist;
    private double less;
    private double stair;
    public boolean collided;
    public boolean cooldown;
    public boolean shouldslow;
    public boolean onGround;
    public boolean lessSlow;
    public double movementSpeed;
    private static float lastYaw;
    private static float lastPitch;
    private float blockPitch;
    private int groundTick;
    private boolean canGroundLess;

    public Speed() {
        super("Speed", ModuleType.Movement);
        this.addValues(this.mode, this.vspeed, this.customy, dmgboost, dmgzoom, this.jumpModeNoStrafe, this.ncpZoom, this.timerboost, this.timermax, this.timermin, this.timertick, this.should, this.watercheck, this.autodisable);
        this.shouldslow = false;
        this.timer = new TimeHelper();
        this.lastCheck = new TimeHelper();
    }

    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        Minecraft.thePlayer.speedInAir = 0.02f;
        Minecraft.thePlayer.jumpMovementFactor = 0.02f;
    }

    @Override
    public void onEnable() {
        this.checkModule((Class<? extends Module>)Fly.class);
        if (!this.mode.is("NCP")) {
            this.lessSlow = false;
            this.less = 0.0;
            this.stage = 2;
            Speed.mc.timer.timerSpeed = 1.0f;
            if (this.mode.is("OldNCP")) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.motionZ = 0.0;
                    Minecraft.thePlayer.motionX = 0.0;
                }
            }
        } else {
            Speed.mc.timer.timerSpeed = 1.0f;
        }
        if (this.mode.is("NCP")) {
            this.collided = Minecraft.thePlayer.isCollidedHorizontally;
            this.lessSlow = false;
            this.speed = MoveUtils.defaultSpeed();
            this.less = 0.0;
            this.stage = 2;
            Speed.mc.timer.timerSpeed = 1.0f;
        }
    }

    @EventHandler
    public void onStep(EventStep event) {
        if (this.mode.is("OldNCP")) {
            double height = Minecraft.thePlayer.getEntityBoundingBox().minY - Minecraft.thePlayer.posY;
            if (height > 0.7) {
                this.less = 0.0;
            }
            if (height == 0.5) {
                this.stair = 0.75;
            }
        }
    }

    @EventHandler
    public void onPost(EventPostUpdate event) {
        if (((Boolean)this.watercheck.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.isInWater() || BlockUtils.isInLiquid()) {
                return;
            }
        }
        if (this.mode.is("Matrix")) {
            Minecraft.thePlayer.setSprinting(MoveUtils.isMovingKeyBindingActive());
            if (this.isMoving()) {
                if (Minecraft.thePlayer.onGround) {
                    if (Minecraft.thePlayer.ticksExisted % 10 != 0) {
                        double f = MoveUtils.getDirection();
                        Minecraft.thePlayer.motionX -= MathHelper.sin(f) * (double)0.202f;
                        Minecraft.thePlayer.motionZ += MathHelper.cos(f) * (double)0.202f;
                    }
                    Minecraft.thePlayer.motionY = 0.4125;
                }
            }
            if (!Minecraft.thePlayer.onGround) {
                if (Minecraft.thePlayer.ticksExisted % 10 == 0) {
                    if (Minecraft.thePlayer.motionY > 0.175) {
                        Speed.mc.timer.timerSpeed = 1.0f;
                        Minecraft.thePlayer.motionY = -0.04 - 0.032 * Math.random();
                    } else {
                        Minecraft.thePlayer.jumpMovementFactor = (float)((double)Minecraft.thePlayer.jumpMovementFactor + 0.001);
                        Speed.mc.timer.timerSpeed = 1.1f;
                    }
                }
            }
        }
        if (this.mode.is("AAC3.5.0")) {
            if (Minecraft.thePlayer.moving()) {
                if (!Minecraft.thePlayer.isInWater()) {
                    if (!Minecraft.thePlayer.isInLava()) {
                        if (Minecraft.thePlayer.fallDistance <= 1.0f) {
                            if (Minecraft.thePlayer.onGround) {
                                Minecraft.thePlayer.jump();
                                Minecraft.thePlayer.motionX *= (double)1.0118f;
                                Minecraft.thePlayer.motionZ *= (double)1.0118f;
                            } else {
                                Minecraft.thePlayer.motionY -= (double)0.0147f;
                                Minecraft.thePlayer.motionX *= (double)1.00138f;
                                Minecraft.thePlayer.motionZ *= (double)1.00138f;
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    public void onPre(EventPreUpdate event) {
        block20: {
            block23: {
                block22: {
                    block21: {
                        block17: {
                            block19: {
                                block18: {
                                    if (!this.mode.is("Jump")) break block17;
                                    if (BlockUtils.isInLiquid() || BlockUtils.isOnLiquid()) break block18;
                                    if (Minecraft.thePlayer.isInWater()) break block18;
                                    if (!Minecraft.thePlayer.isInWeb && !BlockUtils.isInsideBlock()) break block19;
                                }
                                return;
                            }
                            if (!this.isMoving()) break block17;
                            if (!MoveUtils.isOnGround(0.01)) ** GOTO lbl-1000
                            if (Minecraft.thePlayer.isCollidedVertically) {
                                Minecraft.thePlayer.jump();
                            } else if (!Minecraft.thePlayer.onGround && !((Boolean)this.jumpModeNoStrafe.get()).booleanValue()) {
                                MoveUtils.setSpeed(MoveUtils.getSpeed());
                            }
                        }
                        if (!this.mode.is("Watchdog")) break block20;
                        if (BlockUtils.isInLiquid() || BlockUtils.isOnLiquid()) break block21;
                        if (Minecraft.thePlayer.isInWater()) break block21;
                        if (!Minecraft.thePlayer.isInWeb && !BlockUtils.isInsideBlock()) break block22;
                    }
                    if (Speed.mc.timer.timerSpeed != 1.0f) {
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                    return;
                }
                if (!this.isMoving()) break block23;
                if (Minecraft.thePlayer.onGround) {
                    Speed.mc.timer.timerSpeed = 1.0f;
                }
                if (!MoveUtils.isOnGround(0.01)) ** GOTO lbl-1000
                if (Minecraft.thePlayer.isCollidedVertically) {
                    if (!Minecraft.thePlayer.isSneaking() || this.getModule(KillAura.class).getTarget() == null) {
                        Speed.setMotion(null, Math.max(0.2709999978542328, MoveUtils.defaultSpeed() * 0.8999999761581421));
                    }
                    Minecraft.thePlayer.jump();
                    Minecraft.thePlayer.motionY -= 0.0010000000474974513;
                } else if (!Minecraft.thePlayer.onGround) {
                    Speed.mc.timer.timerSpeed = Minecraft.thePlayer.fallDistance < 1.0f ? (Minecraft.thePlayer.fallDistance > 0.75f ? 1.07f : 1.04f) : 1.0f;
                    MoveUtils.setSpeed(MoveUtils.getSpeed());
                }
                break block20;
            }
            Speed.mc.timer.timerSpeed = 1.0f;
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    private void onPreUpdate(EventPreUpdate event) {
        block13: {
            block14: {
                if (!this.mode.is("HmXix")) break block13;
                Minecraft.thePlayer.setSprinting(MoveUtils.isMovingKeyBindingActive());
                if ((double)Minecraft.thePlayer.fallDistance > 1.0) {
                    Speed.strafe();
                    return;
                }
                if (!this.isMoving()) break block14;
                Speed.mc.gameSettings.keyBindJump.pressed = false;
                reach = 2.0;
                x = Minecraft.thePlayer.posX;
                z = Minecraft.thePlayer.posZ;
                forward = MovementInput.moveForward;
                strafe = MovementInput.moveStrafe;
                yaw = Minecraft.thePlayer.rotationYaw;
                cos = Math.cos(Math.toRadians(yaw + 90.0));
                sin = Math.sin(Math.toRadians(yaw + 90.0));
                blockBelow = new BlockPos(x += forward * reach * cos + strafe * reach * sin, Minecraft.thePlayer.posY + 0.1, z += forward * reach * sin - strafe * reach * cos);
                predict = Minecraft.theWorld.getBlockState(blockBelow).getBlock();
                stair = Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ)).getBlock() instanceof BlockStairs;
                if (Keyboard.isKeyDown((int)Speed.mc.gameSettings.keyBindJump.getKeyCode())) ** GOTO lbl-1000
                if (Minecraft.thePlayer.isCollidedHorizontally) ** GOTO lbl-1000
                if (!Minecraft.theWorld.getBlockState(Minecraft.thePlayer.getPosition().add(0, -1, 0)).getBlock().isFullBlock() || predict != Blocks.air && predict.isNormalCube() || stair) lbl-1000:
                // 3 sources

                {
                    v0 = true;
                } else {
                    v0 = false;
                }
                legitJump = v0;
                if (Minecraft.thePlayer.onGround) {
                    Speed.mc.timer.timerSpeed = 1.13f;
                    if (legitJump) {
                        ++this.groundTick;
                    }
                    if (legitJump) {
                        if (this.groundTick > 0) {
                            Minecraft.thePlayer.jump();
                        }
                    } else if (MoveUtils.isOnGround(0.01)) {
                        Minecraft.thePlayer.jump();
                    }
                    Minecraft.thePlayer.motionY = 0.4000000059604645;
                    if (MoveUtils.isOnGround(0.01)) {
                        MoveUtils.strafe(Math.max(0.515999972820282, MoveUtils.getBaseMoveSpeed()));
                    }
                } else {
                    Speed.setMotion(Math.max(this.getSpeed() * 1.00027, MoveUtils.getBaseMoveSpeed()));
                    this.groundTick = 0;
                }
                break block13;
            }
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        }
        if (this.mode.is("NCP") && this.isMoving()) {
            if (Minecraft.thePlayer.onGround) {
                EventPreUpdate.y += ThreadLocalRandom.current().nextDouble() / 1000.0;
                EventPreUpdate.onGround = true;
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        block19: {
            this.setSuffix((Serializable)this.mode.getValue());
            if (((Boolean)this.autodisable.getValue()).booleanValue()) {
                if (!Minecraft.thePlayer.isEntityAlive()) {
                    ClientNotification.sendClientMessage("Speed", "Auto disable because player not alive or death", 4000L, ClientNotification.Type.WARNING);
                    this.setEnabled(false);
                    return;
                }
                if (Minecraft.thePlayer.ticksExisted <= 1) {
                    ClientNotification.sendClientMessage("Speed", "Auto disable because World change or world ticks too lower", 4000L, ClientNotification.Type.WARNING);
                    this.setEnabled(false);
                }
            }
            if (((Boolean)this.watercheck.getValue()).booleanValue()) {
                if (Minecraft.thePlayer.isInWater() || BlockUtils.isInLiquid()) {
                    return;
                }
            }
            if (Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() && ((Boolean)Scaffold.StopSpeed.getValue()).booleanValue()) {
                return;
            }
            if (!((Boolean)this.timerboost.getValue()).booleanValue() || Client.instance.getModuleManager().getModuleByClass(GameSpeed.class).isEnabled()) break block19;
            if (this.xDist > 200.0) {
                this.xDist = 0.0;
            }
            if (!MoveUtils.isMovingKeyBindingActive()) ** GOTO lbl-1000
            v0 = this.xDist;
            this.xDist = v0 + 1.0;
            if (v0 > (double)((Double)this.timertick.getValue()).intValue() && MoveUtils.isMovingKeyBindingActive() && ((Double)this.timermax.getValue()).floatValue() > ((Double)this.timermin.getValue()).floatValue()) {
                Speed.mc.timer.timerSpeed = (float)(((Double)this.timermax.getValue()).floatValue() > ((Double)this.timermin.getValue()).floatValue() ? io.netty.util.internal.ThreadLocalRandom.current().nextDouble((double)((Double)this.timermin.getValue()).floatValue(), (double)((Double)this.timermax.getValue()).floatValue()) : (double)((Double)this.timermin.getValue()).floatValue() - Math.random() / 10.0);
            } else lbl-1000:
            // 2 sources

            {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
            if ((double)Minecraft.thePlayer.fallDistance > 3.0) {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
        }
        if (this.mode.is("LowHop")) {
            Minecraft.thePlayer.cameraPitch = 0.0f;
            Minecraft.thePlayer.cameraYaw = 0.0f;
        }
        if (this.mode.is("SpartanBhop")) {
            Speed.strafe();
            if (Minecraft.thePlayer.fallDistance < 1.0f) {
                if (Minecraft.thePlayer.moving()) {
                    Speed.mc.timer.timerSpeed = 1.1f;
                }
                if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (Minecraft.thePlayer.onGround) {
                        Minecraft.thePlayer.jump();
                        this.airMoves = 0;
                    } else {
                        if (this.airMoves >= 3) {
                            Minecraft.thePlayer.jumpMovementFactor = 0.0275f;
                        }
                        if (this.airMoves >= 4 && (double)(this.airMoves % 2) == 0.0) {
                            Minecraft.thePlayer.motionY = -0.3199999928474426 - 0.009 * Math.random();
                            Minecraft.thePlayer.jumpMovementFactor = 0.0238f;
                        }
                        ++this.airMoves;
                    }
                }
            }
        }
    }

    public double getSpeed() {
        double x = Minecraft.thePlayer.motionX;
        double z = Minecraft.thePlayer.motionZ;
        return Math.sqrt(x * Minecraft.thePlayer.motionX + z * Minecraft.thePlayer.motionZ) * (double)(((Boolean)dmgboost.getValue()).booleanValue() ? (Minecraft.thePlayer.hurtResistantTime > 0 ? 1 + ((Double)dmgzoom.getValue()).intValue() : 1) : 1);
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    public void onMove(EventMove e) {
        block56: {
            block59: {
                block61: {
                    block60: {
                        block58: {
                            block57: {
                                if (((Boolean)this.watercheck.getValue()).booleanValue()) {
                                    if (Minecraft.thePlayer.isInWater() || BlockUtils.isInLiquid()) {
                                        return;
                                    }
                                }
                                if (((Boolean)Scaffold.StopSpeed.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()) {
                                    return;
                                }
                                if (Client.instance.getModuleManager().getModuleByClass(Jesus.class).isEnabled() && ((Boolean)Jesus.stopspeed.getValue()).booleanValue() && (Jesus.shouldJesus() || Jesus.canJeboos())) {
                                    return;
                                }
                                if ((this.mode.is("NCP") || this.mode.is("HmXix")) && this.isEnabled((Class<? extends Module>)TargetStrafe.class)) {
                                    e.setMoveSpeed(Math.max(MoveUtils.getBPS() + this.getSpeed(), MoveUtils.getBaseMoveSpeed()));
                                }
                                var2_2 = (String)this.mode.get();
                                var3_5 = -1;
                                switch (var2_2.hashCode()) {
                                    case 609795629: {
                                        if (!var2_2.equals("Watchdog")) break;
                                        var3_5 = 0;
                                        break;
                                    }
                                    case 2320462: {
                                        if (!var2_2.equals("Jump")) break;
                                        var3_5 = 1;
                                        break;
                                    }
                                    case -930943079: {
                                        if (!var2_2.equals("VerusYport")) break;
                                        var3_5 = 2;
                                    }
                                }
                                switch (var3_5) {
                                    case 0: 
                                    case 1: {
                                        if (!Minecraft.thePlayer.moving()) break;
                                        speed = this.getSpeed();
                                        if (!this.isEnabled((Class<? extends Module>)TargetStrafe.class)) break;
                                        e.setMoveSpeed(speed);
                                        break;
                                    }
                                    case 2: {
                                        if (Minecraft.thePlayer.isInWeb) break;
                                        if (Minecraft.thePlayer.isInLava()) break;
                                        if (Minecraft.thePlayer.isInWater()) break;
                                        if (Minecraft.thePlayer.isOnLadder()) break;
                                        if (Minecraft.thePlayer.ridingEntity != null || !PlayerUtil.isMoving2()) break;
                                        Speed.mc.gameSettings.keyBindJump.pressed = false;
                                        if (Minecraft.thePlayer.onGround) {
                                            Minecraft.thePlayer.jump();
                                            Minecraft.thePlayer.motionY = 0.0;
                                            MoveUtils.strafe(0.6100000143051147);
                                            e.setY(0.41999998688698);
                                        }
                                        Speed.strafe();
                                    }
                                }
                                if (!this.mode.is("LowHop")) break block56;
                                if (!MoveUtils.MovementInput()) break block57;
                                if (!Minecraft.thePlayer.isInWater()) break block58;
                            }
                            this.speed = 0.0;
                            this.stage = 0;
                            return;
                        }
                        if (!MoveUtils.isOnGround(0.01)) break block59;
                        if (this.stage >= 0) break block60;
                        if (!Minecraft.thePlayer.isCollidedHorizontally) break block59;
                    }
                    this.stage = 0;
                    reach = 3.0;
                    y = 0.4001999986886975 + (double)MoveUtils.getJumpEffect() * 0.099;
                    x = Minecraft.thePlayer.posX;
                    z = Minecraft.thePlayer.posZ;
                    forward = MovementInput.moveForward;
                    strafe = MovementInput.moveStrafe;
                    yaw = Minecraft.thePlayer.rotationYaw;
                    cos = Math.cos(Math.toRadians(yaw + 90.0));
                    sin = Math.sin(Math.toRadians(yaw + 90.0));
                    blockBelow = new BlockPos(x += forward * reach * cos + strafe * reach * sin, Minecraft.thePlayer.posY + 0.1, z += forward * reach * sin - strafe * reach * cos);
                    predict = Minecraft.theWorld.getBlockState(blockBelow).getBlock();
                    if (!((Boolean)this.should.getValue()).booleanValue()) break block61;
                    if (Keyboard.isKeyDown((int)Speed.mc.gameSettings.keyBindJump.getKeyCode())) ** GOTO lbl-1000
                    if (Minecraft.thePlayer.isCollidedHorizontally) ** GOTO lbl-1000
                    if (!Minecraft.theWorld.getBlockState(Minecraft.thePlayer.getPosition().add(0, -1, 0)).getBlock().isFullBlock() || KillAura.target != null || predict != Blocks.air && predict.isNormalCube()) lbl-1000:
                    // 3 sources

                    {
                        v0 = true;
                    } else {
                        v0 = shouldJump = false;
                    }
                    if (shouldJump && !Keyboard.isKeyDown((int)Speed.mc.gameSettings.keyBindSprint.getKeyCode())) {
                        Minecraft.thePlayer.motionY = y;
                    }
                }
                e.setY(y);
            }
            this.speed = (Speed.getSpeed(this.stage) + Math.random() / 5000.0) * (0.9 - (double)MoveUtils.getSpeedEffect() * 0.03);
            if (this.stage < 0) {
                this.speed = Speed.getBasemovementSpeed();
            }
            e.setMoveSpeed(this.speed);
            ++this.stage;
        }
        if (this.mode.is("ACRGround")) {
            Speed.strafe();
            if (Minecraft.thePlayer.moving()) {
                if (!Minecraft.thePlayer.isOnLadder()) {
                    if (!Minecraft.thePlayer.isInWater()) {
                        if (!Minecraft.thePlayer.isInLava()) {
                            if (!Minecraft.thePlayer.isInWeb && !Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                                if (Minecraft.thePlayer.fallDistance <= 1.0f) {
                                    if (Minecraft.thePlayer.onGround) {
                                        Minecraft.thePlayer.jump();
                                        Minecraft.thePlayer.motionY -= 0.28999999165534973;
                                        Speed.mc.timer.timerSpeed = 1.0f;
                                        Minecraft.thePlayer.motionX *= 0.962;
                                        Minecraft.thePlayer.motionZ *= 0.962;
                                    } else {
                                        Speed.mc.timer.timerSpeed = 1.0f;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.mode.is("Custom")) {
            if (Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
                return;
            }
            if (Minecraft.thePlayer.moving()) {
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.motionY = ((Double)this.customy.getValue()).floatValue();
                    e.setY(((Double)this.customy.getValue()).floatValue());
                }
                this.speed = (Double)this.vspeed.getValue();
                e.setMoveSpeed(this.speed);
            }
        }
        if (this.mode.is("NCP")) {
            Minecraft.thePlayer.setSprinting(true);
            if (!this.isEnabled((Class<? extends Module>)Scaffold.class)) {
                Speed.mc.gameSettings.keyBindJump.pressed = false;
            }
        }
        if (this.mode.is("OldNCP")) {
            if (!this.isMoving()) {
                this.speed = 0.0;
            }
            if (Minecraft.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && PlayerUtil.isMoving2()) {
                this.collided = Minecraft.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
                    motY = 0.39938 + (double)MoveUtils.getJumpEffect() * 0.1;
                    if (((Boolean)this.ncpZoom.get()).booleanValue()) {
                        if (Minecraft.thePlayer.moving()) {
                            if ((double)Minecraft.thePlayer.fallDistance < 0.5) {
                                Speed.mc.timer.timerSpeed = 1.24f;
                                Minecraft.thePlayer.motionY *= 1.0;
                            } else {
                                Speed.mc.timer.timerSpeed = 1.3f;
                            }
                        }
                    }
                    Minecraft.thePlayer.speedInAir = (float)(0.021800000220537186 + MathUtil.getRandom(0.1, 0.001));
                    Minecraft.thePlayer.jumpMovementFactor += 0.0015f;
                    if (this.stair == 0.0) {
                        Minecraft.thePlayer.jump();
                        Minecraft.thePlayer.motionY = motY;
                        e.setY(Minecraft.thePlayer.motionY);
                    }
                    this.less += 1.0;
                    v1 = this.lessSlow = this.less > 1.0 && this.lessSlow == false;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = Math.max(this.getWatchdogSpeed(this.stage) + 0.0331 - 0.02889, MoveUtils.getBaseMoveSpeed() * 1.309999942779541);
            this.speed = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? (this.speed *= Math.max(0.906 + (double)(MoveUtils.getSpeedEffect() / 100), MoveUtils.getBaseMoveSpeed())) : (this.speed *= Math.max(0.9121, MoveUtils.getBaseMoveSpeed() * 3.0));
            if (this.stair > 0.0) {
                this.speed *= 0.7 - (double)MoveUtils.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                // empty if block
            }
            if (this.lessSlow) {
                // empty if block
            }
            if (BlockUtils.isInLiquid()) {
                this.speed = 0.11999999731779099;
            }
            if (this.isMoving()) {
                e.setMoveSpeed(this.speed);
                ++this.stage;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventHandler
    public void onMotion(EventPreUpdate e) {
        if (!this.mode.is("NCP")) return;
        if (Minecraft.thePlayer == null) return;
        if (Minecraft.theWorld == null) {
            return;
        }
        Minecraft.thePlayer.setSprinting(MoveUtils.isMovingKeyBindingActive());
        if (Minecraft.thePlayer.fallDistance < 2.0f) {
            Speed.mc.timer.timerSpeed = this.isMoving() ? 1.07f : 1.0f;
        }
        if (!this.isMoving()) return;
        EntityPlayerSP thePlayer = Minecraft.thePlayer;
        BlockPos blockPos = new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
        boolean bl = this.canGroundLess = !(Minecraft.theWorld.getBlockState(blockPos).getBlock() instanceof BlockStairs);
        if (BlockUtils.isInLiquid() && !thePlayer.isCollidedHorizontally) {
            return;
        }
        if (this.canGroundLess ? thePlayer.onGround : thePlayer.onGround && MoveUtils.isOnGround(0.01)) {
            if (!Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.thePlayer.motionY = 0.399544464469 + (double)MoveUtils.getJumpEffect() * 0.1;
                EventPreUpdate.setY(Minecraft.thePlayer.motionY);
            }
            Speed.setMotion(Math.max(0.4804 + (double)MoveUtils.getSpeedEffect() * 0.1 * (double)(Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() ? 0.6f : 1.0f), MoveUtils.getBaseMoveSpeed()));
            return;
        }
        if (Minecraft.thePlayer.fallDistance > 0.1f) {
            if (Minecraft.thePlayer.fallDistance < 0.6f) {
                Speed.setMotion(Math.max(this.getSpeed() * 1.0027, MoveUtils.getBaseMoveSpeed()));
                return;
            }
        }
        Speed.setMotion(Math.max(this.getSpeed() * 1.0006, MoveUtils.getBaseMoveSpeed()));
    }

    public static void strafe() {
        double movementSpeed = MathHelper.sqrt_double(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        double yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        } else if (forward != 0.0) {
            if (strafe >= 1.0) {
                yaw = Minecraft.thePlayer.rotationYaw + (float)(forward > 0.0 ? -45 : 45);
                strafe = 0.0;
            } else if (strafe <= -1.0) {
                yaw = Minecraft.thePlayer.rotationYaw + (float)(forward > 0.0 ? 45 : -45);
                strafe = 0.0;
            }
        }
        if (forward > 0.0) {
            forward = 1.0;
        } else if (forward < 0.0) {
            forward = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0));
        double mz = Math.sin(Math.toRadians(yaw + 90.0));
        Minecraft.thePlayer.motionX = forward * movementSpeed * mx + strafe * movementSpeed * mz;
        Minecraft.thePlayer.motionZ = forward * movementSpeed * mz - strafe * movementSpeed * mx;
    }

    public void setmovementSpeed(double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (((Boolean)dmgboost.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.hurtResistantTime > 0) {
                speed *= (double)(1 + ((Double)dmgzoom.getValue()).intValue());
            }
        }
        if (forward == 0.0 && strafe == 0.0) {
            EventMove.x = 0.0;
        } else {
            if (forward != 0.0) {
                MoveUtils.setSpeed(0.279);
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            EventMove.x = forward * speed * cos + strafe * speed * sin;
            EventMove.z = forward * speed * sin - strafe * speed * cos;
        }
    }

    public double defaultSpeed() {
        double n = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    private static double getSpeed(int stage) {
        double value = Speed.getBasemovementSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect() + (double)MoveUtils.getSpeedEffect() / 15.0;
        double first = 0.4145 + (double)MoveUtils.getSpeedEffect() / 12.5;
        double pre = (double)stage / 500.0 * 2.0;
        if (stage == 0) {
            value = 0.64 + ((double)MoveUtils.getSpeedEffect() + 0.028 * (double)MoveUtils.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = first;
        } else if (stage >= 2) {
            value = first - pre;
        }
        if (Minecraft.thePlayer.isCollidedHorizontally) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, Speed.getBasemovementSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect());
    }

    public double getWatchdogSpeed(int stage) {
        double value = this.defaultSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect() + (double)MoveUtils.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)MoveUtils.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 2.0;
        if (stage == 0) {
            if (this.timer.delay(300.0)) {
                this.timer.reset();
            }
            if (!this.lastCheck.delay(500.0)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)MoveUtils.getSpeedEffect() + 0.028 * (double)MoveUtils.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage >= 2) {
            value = firstvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : this.defaultSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect());
    }

    static double getBasemovementSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static void setMotion(double speed) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Minecraft.thePlayer.motionX = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            Minecraft.thePlayer.motionZ = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
        }
    }

    public float[] getRotation(float minturnspeed, float maxturnspeed) {
        if (Speed.mc.gameSettings.keyBindForward.isKeyDown()) {
            if ((double)MovementInput.moveStrafe == 0.0) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 0.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Speed.mc.gameSettings.keyBindLeft.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 225.0f - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Speed.mc.gameSettings.keyBindRight.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 135.0f - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
        } else if (Speed.mc.gameSettings.keyBindBack.isKeyDown()) {
            if ((double)MovementInput.moveStrafe == 0.0) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Speed.mc.gameSettings.keyBindLeft.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 315.0f - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Speed.mc.gameSettings.keyBindRight.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 45.0f - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
        } else {
            if (Speed.mc.gameSettings.keyBindLeft.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 270.0f - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Speed.mc.gameSettings.keyBindRight.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 90.0f - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
        }
        return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 0.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
    }

    public static double getDirection() {
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return rotationYaw;
    }

    public static void setMotion(EventMove em, double speed) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
            if (em != null) {
                EventMove.setX(0.0);
                EventMove.setZ(0.0);
            }
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            Minecraft.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            Minecraft.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
            if (em != null) {
                EventMove.setX(Minecraft.thePlayer.motionX);
                EventMove.setZ(Minecraft.thePlayer.motionZ);
            }
        }
    }
}

