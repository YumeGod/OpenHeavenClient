/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Speed;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals
extends Module {
    private final String[] CritMode = new String[]{"Packet", "Packet2", "Mineplex", "Watchdog", "Watchdog2", "NCP", "OldNCP", "AAC", "Jump", "Hop", "TPHop", "NoGround"};
    private final String[] packets = new String[]{"Simple", "Normal", "Switch", "Switch2", "Switch3", "Yaw", "Silent"};
    private final Mode<String> mode = new Mode("Mode", this.CritMode, this.CritMode[0]);
    private final Mode<String> packet = new Mode("Packet", this.packets, this.packets[0]);
    private final Option<Boolean> stopspeed = new Option<Boolean>("StopSpeed", false);
    private final Option<Boolean> stopfood = new Option<Boolean>("StopOnFood", false);
    private final Option<Boolean> stopmove = new Option<Boolean>("NoMove", false);
    private final Option<Boolean> visuals = new Option<Boolean>("Visuals", false);
    private final Option<Boolean> debug = new Option<Boolean>("DeBug", false);
    private final Numbers<Double> maxdelayy = new Numbers<Double>("MaxDelay", 300.0, 0.0, 1000.0, 5.0);
    private final Numbers<Double> mindelayy = new Numbers<Double>("MinDelay", 300.0, 0.0, 1000.0, 5.0);
    private final Numbers<Double> hopheight = new Numbers<Double>("HopHeight", 0.1, 0.001, 0.42, 0.001, () -> this.mode.isCurrentMode("Hop"));
    private final TimerUtil delay = new TimerUtil();
    private final TimerUtil debugdelay = new TimerUtil();

    public Criticals() {
        super("Criticals", ModuleType.Combat);
        this.addValues(this.mode, this.packet, this.maxdelayy, this.mindelayy, this.hopheight, this.stopspeed, this.stopfood, this.stopmove, this.visuals, this.debug);
    }

    @Override
    public void onEnable() {
        if (this.mode.isCurrentMode("NoGround")) {
            Minecraft.thePlayer.motionY = 0.42;
        }
    }

    public double getDelay() {
        return MathUtil.randomNumber((Double)this.maxdelayy.getValue(), (Double)this.mindelayy.getValue());
    }

    @EventHandler
    private void onPacket(EventPacketSend e) {
        this.setSuffix((Serializable)this.mode.getValue());
        if (((Boolean)this.stopspeed.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
            return;
        }
        if (((Boolean)this.stopfood.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
                return;
            }
        }
        if (((Boolean)this.stopmove.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (((Boolean)this.visuals.getValue()).booleanValue() && e.getPacket() instanceof C02PacketUseEntity && KillAura.target != null) {
            Minecraft.thePlayer.onCriticalHit(KillAura.target);
        }
        if (e.getPacket() instanceof C02PacketUseEntity) {
            if (this.mode.isCurrentMode("Watchdog") && this.SafeCheck() && this.delay.hasReached(this.getDelay())) {
                if (!this.isEating()) {
                    double[] y1 = new double[]{0.104080378093037, 0.105454222033912, 0.102888018147468, 0.099634532004642};
                    double[] dArray = new double[2];
                    dArray[0] = 0.075 + ThreadLocalRandom.current().nextDouble(0.008) * (new Random().nextBoolean() ? 0.96 : 0.97) + (double)Minecraft.thePlayer.ticksExisted % 0.0215 * 0.92;
                    dArray[1] = (new Random().nextBoolean() ? 0.010634691223 : 0.013999777) * (new Random().nextBoolean() ? 0.95 : 0.96) * y1[new Random().nextInt(y1.length)] * 9.5;
                    double[] edit = dArray;
                    EntityPlayerSP p = Minecraft.thePlayer;
                    for (double offset : edit) {
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY + offset * (1.0 + this.getRandomDoubleInRange(-0.005, 0.005)), p.posZ, false));
                    }
                }
                this.delay.reset();
            }
            if (this.mode.isCurrentMode("Mineplex") && this.NCPCheck() && this.delay.hasReached(this.getDelay())) {
                this.Crit(new double[]{4.5E-15, 4.5E-15});
                this.delay.reset();
            }
            if (this.mode.isCurrentMode("OldNCP")) {
                if (Minecraft.thePlayer.onGround && this.delay.hasReached(this.getDelay())) {
                    this.Crit(new double[]{0.05, 0.0, 0.012511, 0.0});
                    this.delay.reset();
                }
            }
            if (this.mode.isCurrentMode("NCP") && this.SafeCheck() && this.delay.hasReached(this.getDelay())) {
                this.Crit(new double[]{0.11, 0.1100013579, 1.3579E-6});
                this.delay.reset();
            }
            if (this.mode.isCurrentMode("Packet")) {
                if (Minecraft.thePlayer.onGround && this.delay.hasReached(this.getDelay())) {
                    this.Crit(new double[]{0.00521, 0.0052, 0.00419, 0.0123333333, 0.01213213132123, 0.05, 1.086765133E-10});
                    this.delay.reset();
                }
            }
            if (this.mode.isCurrentMode("Packet2")) {
                if ((Minecraft.thePlayer.onGround || MoveUtils.isOnGround(0.01)) && this.delay.hasReached(this.getDelay())) {
                    this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.001, Minecraft.thePlayer.posZ, false));
                    this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.426, Minecraft.thePlayer.posZ, false));
                    this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.001, Minecraft.thePlayer.posZ, false));
                    this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.681, Minecraft.thePlayer.posZ, false));
                    this.delay.reset();
                }
            }
            if (this.mode.isCurrentMode("AAC") && this.SafeCheck() && this.delay.hasReached(this.getDelay())) {
                this.Crit(new double[]{0.0014749900000101});
                this.delay.reset();
            }
            if (this.mode.isCurrentMode("Jump") && this.check() && this.delay.hasReached(this.getDelay())) {
                Minecraft.thePlayer.jump();
                this.delay.reset();
            }
            if (this.mode.isCurrentMode("Hop") && this.check() && this.delay.hasReached(this.getDelay())) {
                Minecraft.thePlayer.motionY = ((Double)this.hopheight.getValue()).floatValue();
                Minecraft.thePlayer.fallDistance = ((Double)this.hopheight.getValue()).floatValue();
                this.delay.reset();
            }
            if (this.mode.isCurrentMode("TPHop")) {
                if (Minecraft.thePlayer.onGround && this.delay.hasReached(this.getDelay())) {
                    this.Crit(new double[]{0.02});
                    this.Crit(new double[]{0.01});
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.01, Minecraft.thePlayer.posZ);
                    this.delay.reset();
                }
            }
        }
        if (this.mode.isCurrentMode("NoGround") && this.check() && this.delay.hasReached(this.getDelay()) && e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
            e.sendPacketInEvent();
            packet.setOnGround(false);
            this.delay.reset();
        }
    }

    private void Crit(double[] value) {
        double X = Minecraft.thePlayer.posX;
        double Y = Minecraft.thePlayer.posY;
        double Z = Minecraft.thePlayer.posZ;
        for (int i = 0; i < value.length; ++i) {
            if (((Boolean)this.debug.getValue()).booleanValue() && this.debugdelay.hasReached(this.getDelay())) {
                Helper.sendMessage("Critical: " + value[i] * Math.random() + " to " + (KillAura.target != null ? KillAura.target.getName() : "Entity"));
                this.debugdelay.reset();
            }
            if (this.packet.isCurrentMode("Simple")) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, false));
            }
            if (this.packet.isCurrentMode("Switch")) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, false));
            }
            if (this.packet.isCurrentMode("Switch2")) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, true));
            }
            if (this.packet.isCurrentMode("Switch3")) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, false));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, false));
            }
            if (this.packet.isCurrentMode("Normal")) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Y + value[i], Z, true));
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y, Z, false));
            }
            if (this.packet.isCurrentMode("Silent")) {
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + value[i], Z, false));
            }
            if (!this.packet.isCurrentMode("Yaw")) continue;
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(X, Y + value[i], Z, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, false));
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean check() {
        if (Minecraft.thePlayer.isInWater()) return false;
        if (Minecraft.thePlayer.isInWeb) return false;
        if (!Minecraft.thePlayer.onGround) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean SafeCheck() {
        if (!Minecraft.thePlayer.onGround) return false;
        if (Minecraft.thePlayer.isOnLadder()) return false;
        if (Minecraft.thePlayer.isInWeb) return false;
        if (Minecraft.thePlayer.isInWater()) return false;
        if (Minecraft.thePlayer.isInLava()) return false;
        if (Minecraft.thePlayer.ridingEntity != null) return false;
        if (Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean NCPCheck() {
        if (Minecraft.thePlayer.isInWater()) return false;
        if (Minecraft.thePlayer.isInWeb) return false;
        if (Minecraft.thePlayer.isCollidedHorizontally) return false;
        if (!Minecraft.thePlayer.onGround) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isEating() {
        if (!Minecraft.thePlayer.isUsingItem()) return false;
        if (Minecraft.thePlayer.getItemInUse().getItem().getItemUseAction(Minecraft.thePlayer.getItemInUse()) != EnumAction.EAT) return false;
        return true;
    }

    public double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }
}

