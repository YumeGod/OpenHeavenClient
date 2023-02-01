/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class NoWeb
extends Module {
    private static final Mode<String> modes = new Mode("Mode", new String[]{"Vanilla", "AAC5.0.1", "AAC4.3.6", "AAC3.3.11", "AAC3.0.1", "Spartan", "OldMatrix"}, "Vanilla");
    private static boolean usedTimer;

    public NoWeb() {
        super("NoWeb", new String[]{"noweb"}, ModuleType.Movement);
        this.addValues(modes);
    }

    @Override
    public void onDisable() {
        NoWeb.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix((Serializable)modes.get());
        if (usedTimer) {
            NoWeb.mc.timer.timerSpeed = 1.0f;
            usedTimer = false;
        }
        if (!Minecraft.thePlayer.isInWeb) {
            return;
        }
        switch ((String)modes.getValue()) {
            case "Vanilla": {
                if (!Minecraft.thePlayer.isInWeb) break;
                Minecraft.thePlayer.isInWeb = false;
                break;
            }
            case "AAC5.0.1": {
                Minecraft.thePlayer.jumpMovementFactor = 0.42f;
                if (!Minecraft.thePlayer.onGround) break;
                Minecraft.thePlayer.jump();
                break;
            }
            case "AAC4.3.6": {
                NoWeb.mc.timer.timerSpeed = 0.99f;
                Minecraft.thePlayer.jumpMovementFactor = 0.02958f;
                Minecraft.thePlayer.motionY -= 0.00775;
                if (Minecraft.thePlayer.onGround) {
                    Minecraft.thePlayer.motionY = 0.405;
                    NoWeb.mc.timer.timerSpeed = 1.35f;
                }
                NoWeb.mc.gameSettings.keyBindJump.pressed = false;
                break;
            }
            case "AAC3.3.11": {
                Minecraft.thePlayer.jumpMovementFactor = 0.59f;
                if (NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) break;
                Minecraft.thePlayer.motionY = 0.0;
                break;
            }
            case "AAC3.0.1": {
                MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
                float f = Minecraft.thePlayer.jumpMovementFactor = MovementInput.moveStrafe != 0.0f ? 1.0f : 1.21f;
                if (!NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.thePlayer.motionY = 0.0;
                }
                if (!Minecraft.thePlayer.onGround) break;
                Minecraft.thePlayer.jump();
                break;
            }
            case "Spartan": {
                MoveUtils.strafe(0.27f);
                NoWeb.mc.timer.timerSpeed = 3.7f;
                if (!NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.thePlayer.motionY = 0.0;
                }
                if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                    NoWeb.mc.timer.timerSpeed = 1.7f;
                }
                if (Minecraft.thePlayer.ticksExisted % 40 == 0) {
                    NoWeb.mc.timer.timerSpeed = 3.0f;
                }
                usedTimer = true;
                break;
            }
            case "OldMatrix": {
                Minecraft.thePlayer.jumpMovementFactor = 0.12413333f;
                Minecraft.thePlayer.motionY = -0.0125;
                if (NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    Minecraft.thePlayer.motionY = -0.1625;
                }
                if (!Minecraft.thePlayer.onGround) break;
                Minecraft.thePlayer.jump();
                Minecraft.thePlayer.motionY = 0.2425;
            }
        }
    }
}

