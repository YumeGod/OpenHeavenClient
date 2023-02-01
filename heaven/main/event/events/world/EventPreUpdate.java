/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;

public class EventPreUpdate
extends Event {
    private static float yaw;
    public static float pitch;
    public static double y;
    public static boolean onGround;
    public static double x;
    public static double z;

    public EventPreUpdate(float yaw2, float pitch2, double x2, double y2, double z2, boolean onGround2) {
        yaw = yaw2;
        pitch = pitch2;
        y = y2;
        onGround = onGround2;
        x = x2;
        z = z2;
    }

    public static float getYaw() {
        return yaw;
    }

    public static void setYaw(float yaw2) {
        yaw = yaw2;
    }

    public static float getPitch() {
        return pitch;
    }

    public static void setPitch(float pitch2) {
        pitch = pitch2;
    }

    public double getY() {
        return y;
    }

    public static void setY(double y2) {
        y = y2;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public static void setOnGround(boolean onGround2) {
        onGround = onGround2;
    }

    public static void setZ(double z2) {
        z = z2;
    }

    public static void setX(double x2) {
        x = x2;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }
}

