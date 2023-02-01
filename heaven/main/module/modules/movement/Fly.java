/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventBoundingBox;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

public class Fly
extends Module {
    private final String[] FMode = new String[]{"Verus", "MushMC", "Motion", "HmXix", "Minemora", "Matrix", "AAC4.3.6", "Boost"};
    public final Mode<String> mode = new Mode("Mode", this.FMode, this.FMode[0]);
    private final Option<Boolean> combat = new Option<Boolean>("CombatSpoof", true);
    private final Numbers<Double> vspeed = new Numbers<Double>("MotionSpeed", 2.5, 0.1, 10.0, 0.1, () -> this.mode.is("Motion"));
    private final Option<Boolean> wait = new Option<Boolean>("Waiting", false);
    private final Numbers<Double> waitt = new Numbers<Double>("WaitingDelay", 10.0, 0.0, 1000.0, 10.0, this.wait::get);
    private final Option<Boolean> fastStop = new Option<Boolean>("FastStop", true);
    private final Option<Boolean> bob = new Option<Boolean>("ViewBobbing", false);
    public Option<Boolean> timer = new Option<Boolean>("Timer", false, () -> this.mode.is("Boost"));
    private final Numbers<Double> timerspeed = new Numbers<Double>("TimerSpeed", 1.5, 1.0, 10.0, 0.1, this.timer::get, () -> this.mode.is("Boost"));
    private final Numbers<Double> timerduration = new Numbers<Double>("TimerDuratio", 1000.0, 100.0, 8000.0, 100.0, this.timer::get, () -> this.mode.is("Boost"));
    public double moveSpeed;
    private final TimerUtil waittime = new TimerUtil();
    private final TimerUtil motiononground = new TimerUtil();
    private int mmtick;
    private int boostHypixelState = 1;
    private double lastDistance;
    private boolean failedStart;
    private int boostMotion = 0;
    private final TimerUtil timertimer = new TimerUtil();
    int tick;

    public Fly() {
        super("Fly", ModuleType.Movement);
        this.addValues(this.mode, this.vspeed, this.combat, this.wait, this.waitt, this.timer, this.timerspeed, this.timerduration, this.fastStop, this.bob);
    }

    public float getMaxFallDist() {
        PotionEffect potioneffect = Minecraft.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return Minecraft.thePlayer.getMaxFallHeight() + f;
    }

    @Override
    public void onEnable() {
        this.checkModule(Speed.class, Scaffold.class);
        if (Minecraft.thePlayer == null) {
            return;
        }
        this.waittime.reset();
        this.mmtick = 0;
        if (this.mode.is("AAC4.3.6")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
            ClientNotification.sendClientMessage("Fly", "You need to place a TNT near you", 4000L, ClientNotification.Type.WARNING);
            EventMove.setX(0.0);
            EventMove.setZ(0.0);
            Minecraft.thePlayer.jump();
        }
        if (this.mode.is("Boost")) {
            MoveUtils.stop();
            this.tick = 0;
            Minecraft.thePlayer.stepHeight = 0.0f;
            PlayerCapabilities playerCapabilities = new PlayerCapabilities();
            playerCapabilities.isFlying = true;
            mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(0, -1, false));
            mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
            NetHandlerPlayClient netHandler = mc.getNetHandler();
            EntityPlayerSP player = Minecraft.thePlayer;
            double x1 = player.posX;
            double y1 = player.posY;
            double z2 = player.posZ;
            int i = 0;
            while ((double)i < (double)this.getMaxFallDist() / 0.05510000046342611 + 1.0) {
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x1, y1 + (double)0.0601f, z2, false));
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x1, y1 + (double)5.0E-4f, z2, false));
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x1, y1 + (double)0.005f + 6.01000003516674E-8, z2, false));
                ++i;
            }
            netHandler.addToSendQueue(new C03PacketPlayer(true));
            this.timertimer.reset();
            this.motiononground.reset();
            Minecraft.thePlayer.motionY = 0.4198;
            Minecraft.thePlayer.posY += 0.4198;
            this.boostHypixelState = 1;
            this.moveSpeed = 0.1;
            this.lastDistance = 0.0;
            this.failedStart = false;
        }
        if (this.mode.is("Matrix")) {
            this.boostMotion = 0;
        }
    }

    @Override
    public void onDisable() {
        if (((Boolean)this.fastStop.get()).booleanValue()) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionY = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        }
        Minecraft.thePlayer.capabilities.isFlying = false;
        Fly.mc.timer.timerSpeed = 1.0f;
        Minecraft.thePlayer.stepHeight = 0.5f;
        this.motiononground.reset();
        this.waittime.reset();
    }

    @EventHandler
    private void onSendPacket(EventPacketSend event) {
        if (((Boolean)this.wait.get()).booleanValue() && !this.waittime.hasReached(((Double)this.waitt.get()).intValue())) {
            Minecraft.thePlayer.motionX *= 0.0;
            Minecraft.thePlayer.motionY *= 0.0;
            Minecraft.thePlayer.motionZ *= 0.0;
            Minecraft.thePlayer.jumpMovementFactor *= 0.0f;
        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix((Serializable)this.mode.get());
        if (((Boolean)this.wait.get()).booleanValue() && !this.waittime.hasReached(((Double)this.waitt.get()).intValue())) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionY = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
            Minecraft.thePlayer.jumpMovementFactor = 0.0f;
            return;
        }
        if (!((Boolean)this.bob.get()).booleanValue()) {
            Minecraft.thePlayer.cameraPitch = 0.0f;
            Minecraft.thePlayer.cameraYaw = 0.0f;
        }
        if (((Boolean)this.combat.get()).booleanValue()) {
            KillAura.target = null;
        }
        if (this.mode.is("AAC4.3.6")) {
            if (Minecraft.thePlayer.onGround) {
                EventMove.setX(0.0);
                EventMove.setZ(0.0);
            } else if (Minecraft.thePlayer.fallDistance > 2.0f) {
                Fly.mc.timer.timerSpeed = 0.85f;
                MoveUtils.setMotion(1.5);
                if (Minecraft.thePlayer.movementInput.jump) {
                    Minecraft.thePlayer.motionY = 0.75;
                    return;
                }
                if (Minecraft.thePlayer.movementInput.sneak) {
                    Minecraft.thePlayer.motionY = -0.75;
                    return;
                }
                Minecraft.thePlayer.motionY = 0.0;
            } else if (Minecraft.thePlayer.fallDistance == 0.0f) {
                EventMove.setX(0.0);
                EventMove.setZ(0.0);
            }
        }
        if (this.mode.is("MushMC")) {
            Fly.mc.timer.timerSpeed = 0.85f;
            MoveUtils.setMotion(4.0);
            if (Minecraft.thePlayer.movementInput.jump) {
                Minecraft.thePlayer.motionY = 0.75;
                return;
            }
            if (Minecraft.thePlayer.movementInput.sneak) {
                Minecraft.thePlayer.motionY = -0.75;
                return;
            }
            Minecraft.thePlayer.motionY = 0.0;
            Minecraft.thePlayer.motionY = Minecraft.thePlayer.motionY - (Minecraft.thePlayer.ticksExisted % 10 == 0 ? 0.08 : 0.0);
        }
        if (this.mode.is("Minemora")) {
            if (Minecraft.thePlayer.onGround) {
                return;
            }
            ++this.mmtick;
            if (this.mmtick >= 5) {
                Minecraft.thePlayer.motionY = 0.02;
                this.mmtick = 0;
            }
        }
    }

    @EventHandler
    private void onTick(EventTick e) {
        if (((Boolean)this.wait.get()).booleanValue() && !this.waittime.hasReached(((Double)this.waitt.get()).intValue())) {
            Minecraft.thePlayer.motionX *= 0.0;
            Minecraft.thePlayer.motionY *= 0.0;
            Minecraft.thePlayer.motionZ *= 0.0;
            Minecraft.thePlayer.jumpMovementFactor *= 0.0f;
        }
        if (this.mode.is("Boost")) {
            if (((Boolean)this.timer.get()).booleanValue()) {
                Fly.mc.timer.timerSpeed = !this.timertimer.hasReached(((Double)this.timerduration.get()).longValue()) ? (KillAura.target != null ? 1.3f : (this.isMoving() ? ((Double)this.timerspeed.get()).floatValue() : 1.0f)) : 1.0f;
            }
            ++this.tick;
            switch (this.tick) {
                case 1: 
                case 2: {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-5, Minecraft.thePlayer.posZ);
                    break;
                }
                case 3: {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0E-10, Minecraft.thePlayer.posZ);
                    this.tick = 0;
                }
            }
            if (!this.failedStart) {
                Minecraft.thePlayer.motionY = 0.0;
            }
        }
    }

    @EventHandler
    public void onPost(EventPostUpdate e) {
        double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
        double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
        this.lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventHandler
    private void onMove(EventMove e) {
        if (((Boolean)this.wait.get()).booleanValue() && !this.waittime.hasReached(((Double)this.waitt.get()).intValue())) {
            Minecraft.thePlayer.motionX *= 0.0;
            Minecraft.thePlayer.motionY *= 0.0;
            Minecraft.thePlayer.motionZ *= 0.0;
            Minecraft.thePlayer.jumpMovementFactor *= 0.0f;
            return;
        }
        this.setBob();
        if (this.mode.is("Minemora") && MoveUtils.isMovingKeyBindingActive()) {
            e.setMoveSpeed(MoveUtils.getBaseMoveSpeed());
        }
        if (this.mode.is("HmXix")) {
            float f = Fly.mc.timer.timerSpeed = this.isMoving() ? 8.5f : 1.0f;
            if (this.motiononground.hasReached(5000.0)) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
                this.motiononground.reset();
            }
            Minecraft.thePlayer.motionY = 0.0;
            e.y = 0.0;
            if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.thePlayer.motionY = 2.0;
                e.y = 2.0;
            } else if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.thePlayer.motionY = -2.0;
                e.y = -2.0;
            }
            this.moveSpeed = (Double)this.vspeed.get();
            if (MoveUtils.isMovingKeyBindingActive()) {
                e.setMoveSpeed(this.moveSpeed);
            }
        }
        if (this.mode.is("Motion")) {
            if (this.motiononground.hasReached(5000.0)) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, true));
                this.motiononground.reset();
            }
            Minecraft.thePlayer.motionY = 0.0;
            e.y = 0.0;
            if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                Minecraft.thePlayer.motionY = 2.0;
                e.y = 2.0;
            } else if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Minecraft.thePlayer.motionY = -2.0;
                e.y = -2.0;
            }
            this.moveSpeed = (Double)this.vspeed.get();
            if (MoveUtils.isMovingKeyBindingActive()) {
                e.setMoveSpeed(this.moveSpeed);
            }
        }
        if (this.mode.is("Verus")) {
            Minecraft.thePlayer.motionY = 0.0;
        }
        if (this.mode.is("Boost")) {
            if (!Minecraft.thePlayer.moving()) {
                MoveUtils.setSpeed(e, 0.0);
                return;
            }
            if (this.failedStart) {
                return;
            }
            double amplifier = 1.0 + (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) : 0.0);
            double baseSpeed = 0.29 * amplifier;
            switch (this.boostHypixelState) {
                case 1: {
                    this.moveSpeed = (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034) * baseSpeed;
                    this.boostHypixelState = 2;
                    break;
                }
                case 2: {
                    this.moveSpeed *= 2.16;
                    this.boostHypixelState = 3;
                    break;
                }
                case 3: {
                    this.moveSpeed = this.lastDistance - (Minecraft.thePlayer.ticksExisted % 2 == 0 ? 0.0103 : 0.0123) * (this.lastDistance - baseSpeed);
                    this.boostHypixelState = 4;
                    break;
                }
                default: {
                    this.moveSpeed = this.lastDistance - this.lastDistance / 159.8;
                }
            }
            this.moveSpeed = Math.max(this.moveSpeed, 0.3);
            if (!this.failedStart) {
                Minecraft.thePlayer.motionY = 0.0;
            }
            e.setMoveSpeed(this.moveSpeed);
        }
    }

    @EventHandler
    public void onBB(EventBoundingBox event) {
        if (Minecraft.thePlayer != null) {
            if (this.mode.is("Verus") && event.getBlock() instanceof BlockAir) {
                if ((double)event.getY() < Minecraft.thePlayer.posY) {
                    event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1, Minecraft.thePlayer.posY, event.getZ() + 1));
                }
            }
            if (this.mode.is("Boost") && event.getBlock() instanceof BlockAir) {
                if ((double)event.getY() < Minecraft.thePlayer.posY) {
                    event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1, Minecraft.thePlayer.posY, event.getZ() + 1));
                }
            }
        }
    }

    private void setBob() {
        Minecraft.thePlayer.cameraYaw = (Boolean)this.bob.get() != false ? 0.095f : 0.0f;
    }
}

