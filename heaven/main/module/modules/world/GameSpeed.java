/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class GameSpeed
extends Module {
    private final Option<Boolean> onmove = new Option<Boolean>("OnlyMove", false);
    public final Numbers<Double> op = new Numbers<Double>("Speed", 1.0, 0.1, 10.0, 0.1);
    private final Mode<String> mode = new Mode("Mode", new String[]{"Default", "Custom"}, "Default");

    public GameSpeed() {
        super("GameSpeed", new String[]{"timer"}, ModuleType.World);
        this.addValues(this.mode, this.op, this.onmove);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        block6: {
            block5: {
                if (Minecraft.thePlayer == null) break block5;
                if (Minecraft.theWorld != null) break block6;
            }
            return;
        }
        this.setSuffix(this.mode.is("Custom") ? (Serializable)this.op.get() : (Serializable)this.mode.get());
        if (this.mode.is("Custom")) {
            this.customModeSpeed();
        }
        if (this.mode.is("Default")) {
            this.defaultSpeed();
        }
    }

    private void customModeSpeed() {
        GameSpeed.mc.timer.timerSpeed = (Boolean)this.onmove.get() == false ? ((Double)this.op.get()).floatValue() : (this.isMoving() ? ((Double)this.op.get()).floatValue() : 1.0f);
    }

    private void defaultSpeed() {
        GameSpeed.mc.timer.timerSpeed = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.3f + 0.2f * (float)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) : 1.3f;
    }

    @Override
    public void onDisable() {
        if (GameSpeed.mc.timer.timerSpeed != 1.0f) {
            GameSpeed.mc.timer.timerSpeed = 1.0f;
        }
    }
}

