/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.util;

import heaven.main.Client;
import heaven.main.module.modules.movement.InvMove;
import heaven.main.module.modules.world.Scaffold;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import org.lwjgl.input.Keyboard;

public class MovementInputFromOptions
extends MovementInput {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void updatePlayerMoveState() {
        block18: {
            block17: {
                if (!Client.instance.getModuleManager().getModuleByClass(InvMove.class).isEnabled() || Minecraft.getMinecraft().currentScreen instanceof GuiChat || !InvMove.mode.is("KeepMove")) break block17;
                MovementInputFromOptions.moveStrafe = 0.0f;
                MovementInputFromOptions.moveForward = 0.0f;
                if (Keyboard.isKeyDown((int)this.gameSettings.keyBindForward.getKeyCode())) {
                    MovementInputFromOptions.moveForward += 1.0f;
                }
                if (Keyboard.isKeyDown((int)this.gameSettings.keyBindBack.getKeyCode())) {
                    MovementInputFromOptions.moveForward -= 1.0f;
                }
                if (Keyboard.isKeyDown((int)this.gameSettings.keyBindLeft.getKeyCode())) {
                    MovementInputFromOptions.moveStrafe += 1.0f;
                }
                if (Keyboard.isKeyDown((int)this.gameSettings.keyBindRight.getKeyCode())) {
                    MovementInputFromOptions.moveStrafe -= 1.0f;
                }
                this.jump = Keyboard.isKeyDown((int)this.gameSettings.keyBindJump.getKeyCode());
                v0 = this.sneak = Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() != false && (Boolean)Scaffold.down.getValue() != false ? false : this.gameSettings.keyBindSneak.getIsKeyPressed();
                if (this.sneak) {
                    MovementInputFromOptions.moveStrafe = (float)((double)MovementInputFromOptions.moveStrafe * 0.3);
                    MovementInputFromOptions.moveForward = (float)((double)MovementInputFromOptions.moveForward * 0.3);
                }
                mc = Minecraft.getMinecraft();
                if (mc.currentScreen == null) break block18;
                if (!Keyboard.isKeyDown((int)200)) ** GOTO lbl-1000
                if (Minecraft.thePlayer.rotationPitch > -90.0f) {
                    Minecraft.thePlayer.rotationPitch -= 5.0f;
                } else if (Keyboard.isKeyDown((int)208)) {
                    if (Minecraft.thePlayer.rotationPitch < 90.0f) {
                        Minecraft.thePlayer.rotationPitch += 5.0f;
                    }
                }
                if (Keyboard.isKeyDown((int)203)) {
                    Minecraft.thePlayer.rotationYaw -= 5.0f;
                } else if (Keyboard.isKeyDown((int)205)) {
                    Minecraft.thePlayer.rotationYaw += 5.0f;
                }
                break block18;
            }
            MovementInputFromOptions.moveStrafe = 0.0f;
            MovementInputFromOptions.moveForward = 0.0f;
            if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
                MovementInputFromOptions.moveForward += 1.0f;
            }
            if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
                MovementInputFromOptions.moveForward -= 1.0f;
            }
            if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
                MovementInputFromOptions.moveStrafe += 1.0f;
            }
            if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
                MovementInputFromOptions.moveStrafe -= 1.0f;
            }
            this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
            v1 = this.sneak = Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() != false && (Boolean)Scaffold.down.getValue() != false ? false : this.gameSettings.keyBindSneak.getIsKeyPressed();
            if (this.sneak) {
                MovementInputFromOptions.moveStrafe = (float)((double)MovementInputFromOptions.moveStrafe * 0.3);
                MovementInputFromOptions.moveForward = (float)((double)MovementInputFromOptions.moveForward * 0.3);
            }
        }
    }
}

