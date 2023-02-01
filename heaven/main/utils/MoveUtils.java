/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import heaven.main.event.events.world.EventMove;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class MoveUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isOnGround(double height) {
        return !Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static int getJumpEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public static int getSpeedEffect() {
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += forward > 0.0 ? -45.0f : 45.0f;
            } else if (strafe < 0.0) {
                yaw += forward > 0.0 ? 45.0f : -45.0f;
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        EventMove.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        EventMove.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static void setSpeed(EventMove moveEvent, double moveSpeed) {
        MoveUtils.setSpeed(moveEvent, moveSpeed, Minecraft.thePlayer.rotationYaw, MovementInput.moveStrafe, MovementInput.moveForward);
    }

    public static void setSpeed(double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += forward > 0.0 ? -45.0f : 45.0f;
            } else if (strafe < 0.0) {
                yaw += forward > 0.0 ? 45.0f : -45.0f;
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        EventMove.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        EventMove.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
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
        return Math.toRadians(rotationYaw);
    }

    public static float getyaw() {
        float wrapAngleTo180_float = MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw);
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        float a = movementInput.getMoveStrafe();
        float b = movementInput.getMoveForward();
        if (a != 0.0f) {
            if (b < 0.0f) {
                wrapAngleTo180_float += a < 0.0f ? 135.0f : 45.0f;
            } else if (b > 0.0f) {
                wrapAngleTo180_float -= a < 0.0f ? 135.0f : 45.0f;
            } else if (b == 0.0f && a < 0.0f) {
                wrapAngleTo180_float -= 180.0f;
            }
        } else if (b < 0.0f) {
            wrapAngleTo180_float += 90.0f;
        } else if (b > 0.0f) {
            wrapAngleTo180_float -= 90.0f;
        }
        return MathHelper.wrapAngleTo180_float(wrapAngleTo180_float);
    }

    public static double getDirectionForAura() {
        float rotationYaw = Minecraft.thePlayer.rotationYawHead;
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
        return Math.toRadians(rotationYaw);
    }

    public static float getDirection2() {
        float yaw = Minecraft.thePlayer.rotationYaw;
        float forward = Minecraft.thePlayer.moveForward;
        float strafe = Minecraft.thePlayer.moveStrafing;
        yaw += (float)(forward < 0.0f ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += forward < 0.0f ? -45.0f : (forward == 0.0f ? 90.0f : 45.0f);
        }
        if (strafe > 0.0f) {
            yaw -= forward < 0.0f ? -45.0f : (forward == 0.0f ? 90.0f : 45.0f);
        }
        return yaw * ((float)Math.PI / 180);
    }

    public static void setSpeed(double speed) {
        Minecraft.thePlayer.motionX = (double)(-MathHelper.sin(MoveUtils.getDirection2())) * speed;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos(MoveUtils.getDirection2()) * speed;
    }

    public static void setSpeed2(double speed) {
        Minecraft.thePlayer.motionX = (double)(-MathHelper.sin((float)MoveUtils.getDirection())) * speed;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos((float)MoveUtils.getDirection()) * speed;
    }

    public static boolean MovementInput() {
        return MoveUtils.mc.gameSettings.keyBindForward.pressed || MoveUtils.mc.gameSettings.keyBindLeft.pressed || MoveUtils.mc.gameSettings.keyBindRight.pressed || MoveUtils.mc.gameSettings.keyBindBack.pressed;
    }

    public static void strafe(double speed) {
        if (!Minecraft.thePlayer.moving()) {
            return;
        }
        double yaw = MoveUtils.getDirection();
        Minecraft.thePlayer.motionX = -Math.sin(yaw) * speed;
        Minecraft.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static boolean isMovingKeyBindingActive() {
        return MoveUtils.mc.gameSettings.keyBindForward.isKeyDown() || MoveUtils.mc.gameSettings.keyBindLeft.isKeyDown() || MoveUtils.mc.gameSettings.keyBindRight.isKeyDown() || MoveUtils.mc.gameSettings.keyBindBack.isKeyDown();
    }

    public static double getJumpBoostModifier(double baseJumpHeight) {
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (double)((float)(amplifier + 1) * 0.1f);
        }
        return baseJumpHeight;
    }

    public static float getSpeed() {
        return (float)Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
    }

    public static void toFwd(double speed) {
        float yaw = Minecraft.thePlayer.rotationYaw * ((float)Math.PI / 180);
        Minecraft.thePlayer.motionX -= (double)MathHelper.sin(yaw) * speed;
        Minecraft.thePlayer.motionZ += (double)MathHelper.cos(yaw) * speed;
    }

    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getBaseMovementSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getBaseMoveSpeed(float customSpeed) {
        double baseSpeed = customSpeed;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) && baseSpeed > 0.0) {
            baseSpeed *= 1.0 + 0.2 * (double)(Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }

    public static double getBPS() {
        double xDif = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
        double zDif = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
        return Math.sqrt(xDif * xDif + zDif * zDif) * 20.0;
    }

    public static void setSpeedEvent(EventMove event, double speed) {
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        double forward = MovementInput.moveForward;
        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
        double strafe = MovementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            EventMove.setX(0.0);
            EventMove.setZ(0.0);
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
            EventMove.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            EventMove.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static void pause(EventMove e) {
        MoveUtils.setSpeedEvent(e, 0.0);
        MoveUtils.setSpeed(0.0);
    }

    public static void setMotion(double speed) {
        float yaw = Minecraft.thePlayer.rotationYaw;
        double forward = Minecraft.thePlayer.moveForward;
        double strafe = Minecraft.thePlayer.moveStrafing;
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
            Minecraft.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Minecraft.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static void setMoveSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
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
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        EventMove.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        EventMove.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static void setMoveSpeed(EventMove moveEvent, double moveSpeed) {
        Minecraft.getMinecraft();
        float rotationYaw = Minecraft.thePlayer.rotationYaw;
        Minecraft.getMinecraft();
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        double pseudoStrafe = MovementInput.moveStrafe;
        Minecraft.getMinecraft();
        MovementInput movementInput2 = Minecraft.thePlayer.movementInput;
        MoveUtils.setMoveSpeed(moveEvent, moveSpeed, rotationYaw, pseudoStrafe, MovementInput.moveForward);
    }

    public static void stop() {
        Minecraft.thePlayer.motionZ = 0.0;
        Minecraft.thePlayer.motionX = 0.0;
    }

    public static double getPredictedMotionY(double motionY) {
        return (motionY - 0.08) * (double)0.98f;
    }
}

