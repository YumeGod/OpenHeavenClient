/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventStep;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step
extends Module {
    private final String[] StepMode = new String[]{"Vanilla", "Watchdog", "NCP", "Spartan"};
    private final Numbers<Double> maxdelay = new Numbers<Double>("MaxDelay", 250.0, 0.0, 1000.0, 5.0);
    private final Numbers<Double> mindelay = new Numbers<Double>("MinDelay", 250.0, 0.0, 1000.0, 5.0);
    public final Numbers<Double> height = new Numbers<Double>("Height", 1.0, 1.0, 20.0, 0.5);
    public final Mode<String> mode = new Mode("Mode", this.StepMode, this.StepMode[0]);
    public final Option<Boolean> timer = new Option<Boolean>("Timer", false, () -> !this.mode.isCurrentMode("Vanilla"));
    public final Numbers<Double> timers = new Numbers<Double>("TimerSpeed", 0.3, 0.1, 1.0, 0.1, this.timer::getValue);
    private final Option<Boolean> scaffold = new Option<Boolean>("CanScaffold", false, () -> !this.mode.is("Vanilla"));
    private final Option<Boolean> SpeedMod = new Option<Boolean>("CanSpeed", false, () -> !this.mode.is("Vanilla"));
    private final TimerUtil StepDelay = new TimerUtil();
    private int groundTicks;

    public Step() {
        super("Step", ModuleType.Movement);
        this.addValues(this.mode, this.maxdelay, this.mindelay, this.height, this.timer, this.timers, this.scaffold, this.SpeedMod);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.groundTicks = 0;
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.stepHeight = 0.6f;
        Step.mc.timer.timerSpeed = 1.0f;
    }

    private boolean moduleCheck() {
        if (!this.mode.is("Vanilla")) {
            return (Boolean)this.scaffold.getValue() == false && Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() || (Boolean)this.SpeedMod.getValue() == false && Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled();
        }
        return false;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        this.setSuffix((Serializable)this.height.get());
        if (this.moduleCheck()) {
            return;
        }
        if (this.mode.is("Watchdog")) {
            int n = this.groundTicks = MoveUtils.isOnGround(0.01) ? (this.groundTicks = this.groundTicks + 1) : 0;
            if (this.groundTicks > 20) {
                this.groundTicks = 20;
            }
            if (!this.canStep()) {
                return;
            }
        }
        Minecraft.thePlayer.stepHeight = 0.6f;
        if (this.StepDelay.hasReached(MathUtil.randomNumber((Double)this.maxdelay.getValue(), (Double)this.mindelay.getValue()))) {
            if (this.mode.isCurrentMode("Vanilla")) {
                Minecraft.thePlayer.stepHeight = ((Double)this.height.getValue()).floatValue();
            } else {
                if (Minecraft.thePlayer.movementInput != null && !Step.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (!Minecraft.thePlayer.movementInput.jump) {
                        if (Minecraft.thePlayer.isCollidedHorizontally) {
                            if (Minecraft.thePlayer.onGround && !BlockUtils.isInLiquid()) {
                                Minecraft.thePlayer.stepHeight = 1.0f;
                                Minecraft.thePlayer.stepHeight = ((Double)this.height.getValue()).floatValue();
                                return;
                            }
                        }
                    }
                }
                Minecraft.thePlayer.stepHeight = 0.6f;
            }
            this.StepDelay.reset();
        }
    }

    @EventHandler
    public void onStep(EventStep event) {
        if (this.moduleCheck()) {
            return;
        }
        if (this.mode.is("Watchdog") || this.mode.is("NCP") || this.mode.is("Spartan")) {
            if (!this.canStep()) {
                return;
            }
            if (this.StepDelay.hasReached(MathUtil.randomNumber((Double)this.maxdelay.getValue(), (Double)this.mindelay.getValue()))) {
                if (event.getStepHeight() > 0.6f && event.getStepHeight() <= ((Double)this.height.getValue()).floatValue()) {
                    if (this.mode.is("Watchdog")) {
                        this.watchdogStep(Minecraft.thePlayer.getEntityBoundingBox().minY - Minecraft.thePlayer.posY);
                    } else {
                        Minecraft.thePlayer.motionZ = 0.0;
                        Minecraft.thePlayer.motionX = 0.0;
                        this.NCPStep(Minecraft.thePlayer.getEntityBoundingBox().minY - Minecraft.thePlayer.posY);
                    }
                    Minecraft.thePlayer.stepHeight = ((Double)this.height.getValue()).floatValue();
                    if (((Boolean)this.timer.getValue()).booleanValue()) {
                        Step.mc.timer.timerSpeed = ((Double)this.timers.getValue()).floatValue();
                        new Thread(() -> {
                            try {
                                Thread.sleep(150L);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!Minecraft.thePlayer.isCollidedHorizontally) {
                                Step.mc.timer.timerSpeed = 1.0f;
                            }
                        }).start();
                    }
                    Minecraft.thePlayer.stepHeight = 0.6f;
                }
                this.StepDelay.reset();
            }
        }
    }

    private boolean canStep() {
        if (this.mode.is("Watchdog")) {
            if (this.groundTicks > 3) {
                return Minecraft.thePlayer.isCollidedVertically && !Step.mc.gameSettings.keyBindJump.pressed && !BlockUtils.isInLiquid();
            }
            return false;
        }
        return true;
    }

    private void NCPStep(double stepHeight) {
        double posX = Minecraft.thePlayer.posX;
        double posZ = Minecraft.thePlayer.posZ;
        double posY = Minecraft.thePlayer.posY;
        if (stepHeight < 1.0) {
            double[] array = new double[]{0.39, 0.698};
            int length = array.length;
            for (int j = 0; j < length; ++j) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
            }
        } else if (stepHeight < 1.1) {
            double[] array = new double[]{0.42, 0.748};
            int length = array.length;
            for (int j = 0; j < length; ++j) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
            }
        } else if (stepHeight < 1.6) {
            double[] array = new double[]{0.42, 0.753, 1.001, 1.084, 1.006};
            int length = array.length;
            for (int j = 0; j < length; ++j) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
            }
        } else if (stepHeight < 2.1) {
            double[] array = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            int length = array.length;
            for (int j = 0; j < length; ++j) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
            }
        } else {
            double[] array = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            int length = array.length;
            for (int j = 0; j < length; ++j) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
            }
        }
    }

    public void watchdogStep(double height) {
        double[] values = new double[]{};
        if (height > 1.015) {
            values = new double[]{0.42, 0.7532, 1.0, 0.98};
        } else if (height > 0.875) {
            values = new double[]{0.42, 0.7532};
        } else if (height > 0.7) {
            values = new double[]{0.39, 0.6938};
        }
        for (double d : values) {
            this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (d + Math.random() / 200.0), Minecraft.thePlayer.posZ, false));
        }
    }
}

