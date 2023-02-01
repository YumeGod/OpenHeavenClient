/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class AuraCore {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotationNeededHypixelBetter(Entity p) {
        double d1 = p.posX - Minecraft.thePlayer.posX;
        double d2 = p.posY + (double)p.getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double d3 = p.posZ - Minecraft.thePlayer.posZ;
        double d4 = Math.sqrt(d1 * d1 + d3 * d3);
        float yaw = (float)(Math.atan2(d3, d1) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(d2, d4) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float wrapDegrees(float value) {
        if ((value %= 360.0f) >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    public static float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.thePlayer.posX;
        double deltaZ = posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return AuraCore.wrapDegrees(-(yaw - (float)yawToEntity));
    }

    public static float angleDifference(float a, float b) {
        float c = Math.abs(a % 360.0f - b % 360.0f);
        c = Math.min(c, 360.0f - c);
        return c;
    }
}

