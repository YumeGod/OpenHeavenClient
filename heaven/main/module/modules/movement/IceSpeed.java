/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.movement.Step;
import heaven.main.module.modules.world.GameSpeed;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.io.Serializable;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;

public class IceSpeed
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Speed", "Timer", "NCP"}, "Timer");
    private static final Numbers<Double> Speed = new Numbers<Double>("Speed", 0.5, 0.01, 5.0, 0.01);

    public IceSpeed() {
        super("IceSpeed", ModuleType.Movement);
        this.addValues(this.mode, Speed);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        block30: {
            block29: {
                block28: {
                    block27: {
                        this.setSuffix((Serializable)this.mode.getValue());
                        if (Minecraft.thePlayer == null) break block27;
                        if (Minecraft.theWorld != null) break block28;
                    }
                    return;
                }
                if (!(BlockUtils.getBlock(Minecraft.thePlayer.getPosition().down()) instanceof BlockIce)) break block29;
                if (BlockUtils.getBlock(Minecraft.thePlayer.getPosition().down()) instanceof BlockPackedIce) break block30;
            }
            if (this.getModule(Speed.class).isEnabled() || this.getModule(Fly.class).isEnabled() || this.getModule(GameSpeed.class).isEnabled()) {
                return;
            }
            if (this.getModule(Step.class).isEnabled()) {
                if (Minecraft.thePlayer.isCollidedHorizontally) {
                    return;
                }
            }
            if (IceSpeed.mc.timer.timerSpeed != 1.0f) {
                IceSpeed.mc.timer.timerSpeed = 1.0f;
            }
        }
        if (BlockUtils.getBlock(Minecraft.thePlayer.getPosition().down()) instanceof BlockIce) {
            if (this.mode.is("NCP")) {
                if (Minecraft.thePlayer.moving()) {
                    MoveUtils.setSpeed(0.7f);
                }
            }
            if (this.mode.is("Speed")) {
                if (Minecraft.thePlayer.moving()) {
                    MoveUtils.setSpeed((Double)Speed.getValue());
                }
            }
            if (this.mode.is("Timer")) {
                if (Minecraft.thePlayer.moving()) {
                    IceSpeed.mc.timer.timerSpeed = ((Double)Speed.getValue()).floatValue();
                }
            }
        } else if (BlockUtils.getBlock(Minecraft.thePlayer.getPosition().down()) instanceof BlockPackedIce) {
            if (this.mode.is("NCP")) {
                if (Minecraft.thePlayer.moving()) {
                    MoveUtils.setSpeed(0.7f);
                }
            }
            if (this.mode.is("Speed")) {
                if (Minecraft.thePlayer.moving()) {
                    MoveUtils.setSpeed((Double)Speed.getValue());
                }
            }
            if (this.mode.is("Timer")) {
                if (Minecraft.thePlayer.moving()) {
                    IceSpeed.mc.timer.timerSpeed = ((Double)Speed.getValue()).floatValue();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        MoveUtils.setSpeed(0.0);
        IceSpeed.mc.timer.timerSpeed = 1.0f;
    }
}

