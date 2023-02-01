/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.Client;
import heaven.main.event.Event;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.combat.TargetStrafe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class EventMove
extends Event {
    public static double x;
    public double y;
    public static double z;

    public EventMove(double x, double y, double z) {
        EventMove.x = x;
        this.y = y;
        EventMove.z = z;
    }

    public double getX() {
        return x;
    }

    public static void setX(double x2) {
        x = x2;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public static void setZ(double z2) {
        z = z2;
    }

    public void setMoveSpeed(double moveSpeed) {
        Client novoline = Client.instance;
        Minecraft mc = Minecraft.getMinecraft();
        KillAura killAura = (KillAura)novoline.getModuleManager().getModuleByClass(KillAura.class);
        TargetStrafe targetStrafe = (TargetStrafe)novoline.getModuleManager().getModuleByClass(TargetStrafe.class);
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        double moveForward = movementInput.getMoveForward();
        double moveStrafe = movementInput.getMoveStrafe();
        double yaw = Minecraft.thePlayer.rotationYaw;
        double modifier = moveForward == 0.0 ? 90.0 : (moveForward < 0.0 ? -45.0 : 45.0);
        boolean moving = moveForward != 0.0 || moveStrafe != 0.0;
        yaw += moveForward < 0.0 ? 180.0 : 0.0;
        if (moveStrafe < 0.0) {
            yaw += modifier;
        } else if (moveStrafe > 0.0) {
            yaw -= modifier;
        }
        if (moving) {
            if (this.targetstrafeCheck()) {
                targetStrafe.circleStrafe(this, moveSpeed, killAura.getTarget());
            } else {
                x = -(MathHelper.sin(Math.toRadians(yaw)) * moveSpeed);
                z = MathHelper.cos(Math.toRadians(yaw)) * moveSpeed;
            }
        } else {
            x = 0.0;
            z = 0.0;
        }
    }

    private boolean targetstrafeCheck() {
        Client heaven = Client.instance;
        KillAura killAura = (KillAura)heaven.getModuleManager().getModuleByClass(KillAura.class);
        TargetStrafe targetStrafe = (TargetStrafe)heaven.getModuleManager().getModuleByClass(TargetStrafe.class);
        if (!targetStrafe.isEnabled()) {
            return false;
        }
        if (targetStrafe.shouldTarget()) {
            return true;
        }
        if (killAura.getTarget() != null) {
            return killAura.isEnabled();
        }
        return false;
    }
}

