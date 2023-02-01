/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.combat.TPAura;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Longjump;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.movement.Step;
import heaven.main.module.modules.world.GameSpeed;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class LagBackCheck
extends Module {
    private final Numbers<Double> disdelay = new Numbers<Double>("DisableDelay", 477.7, 0.0, 750.0, 50.0);
    private final Option<Boolean> fly = new Option<Boolean>("FlyCheck", true);
    private final Option<Boolean> speed = new Option<Boolean>("SpeedCheck", true);
    private final Option<Boolean> lj = new Option<Boolean>("LongJumpCheck", true);
    private final Option<Boolean> step = new Option<Boolean>("StepCheck", false);
    private final Option<Boolean> timer = new Option<Boolean>("TimerCheck", false);
    private final Option<Boolean> tpaura = new Option<Boolean>("TPAuraCheck", false);
    private final Option<Boolean> ka = new Option<Boolean>("KillAuraCheck", false);
    private final TimerUtil time = new TimerUtil();

    public LagBackCheck() {
        super("LagBackCheck", ModuleType.Misc);
        this.addValues(this.disdelay, this.fly, this.speed, this.lj, this.step, this.timer, this.tpaura, this.ka);
    }

    @EventHandler
    private void onPacket(EventPacketReceive e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            if (((Boolean)this.fly.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled() && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                Client.instance.getModuleManager().getModuleByClass(Fly.class).setEnabled(false);
                ClientNotification.sendClientMessage("LagBackCheck", "\u00a77Fly LagBack, AutoDisabled", 2500L, ClientNotification.Type.WARNING);
                this.time.reset();
            }
            if (((Boolean)this.speed.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
                if (Minecraft.thePlayer.moving() && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                    Client.instance.getModuleManager().getModuleByClass(Speed.class).setEnabled(false);
                    ClientNotification.sendClientMessage("LagBackCheck", "\u00a77Speed LagBack,Auto Disabled", 2500L, ClientNotification.Type.WARNING);
                    this.time.reset();
                }
            }
            if (((Boolean)this.lj.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Longjump.class).isEnabled()) {
                if (Minecraft.thePlayer.moving() && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                    Client.instance.getModuleManager().getModuleByClass(Longjump.class).setEnabled(false);
                    ClientNotification.sendClientMessage("LagBackCheck", "\u00a77LongJump LagBack,Auto Disabled", 2500L, ClientNotification.Type.WARNING);
                    this.time.reset();
                }
            }
            if (((Boolean)this.timer.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(GameSpeed.class).isEnabled() && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                Client.instance.getModuleManager().getModuleByClass(GameSpeed.class).setEnabled(false);
                ClientNotification.sendClientMessage("LagBackCheck", "\u00a77Timer LagBack,Auto Disabled", 2500L, ClientNotification.Type.WARNING);
                this.time.reset();
            }
            if (((Boolean)this.ka.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() && KillAura.target != null) {
                if (Minecraft.thePlayer.moving() && e.getPacket() instanceof C02PacketUseEntity && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                    Client.instance.getModuleManager().getModuleByClass(KillAura.class).setEnabled(false);
                    ClientNotification.sendClientMessage("LagBackCheck", "\u00a7KillAura LagBack,Auto Disabled", 2500L, ClientNotification.Type.WARNING);
                    this.time.reset();
                }
            }
            if (((Boolean)this.tpaura.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(TPAura.class).isEnabled() && !new TPAura().targets.isEmpty() && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                Client.instance.getModuleManager().getModuleByClass(TPAura.class).setEnabled(false);
                ClientNotification.sendClientMessage("LagBackCheck", "\u00a7TPAura LagBack,Auto Disabled", 2500L, ClientNotification.Type.WARNING);
                this.time.reset();
            }
            if (((Boolean)this.step.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(Step.class).isEnabled()) {
                if (Minecraft.thePlayer.isCollidedHorizontally && this.time.delay(((Double)this.disdelay.getValue()).intValue() * 15)) {
                    Client.instance.getModuleManager().getModuleByClass(Step.class).setEnabled(false);
                    ClientNotification.sendClientMessage("LagBackCheck", "\u00a77Step LagBack,AutoDisabled", 2500L, ClientNotification.Type.WARNING);
                    this.time.reset();
                }
            }
        }
    }
}

