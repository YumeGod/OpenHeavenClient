/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.world;

import heaven.main.event.Event;

public class EventPostUpdate
extends Event {
    public static float yaw;
    public static float pitch;

    public EventPostUpdate(float yaw2, float pitch2) {
        yaw = yaw2;
        pitch = pitch2;
    }

    public float getYaw() {
        return yaw;
    }

    public static void setYaw(float yaw2) {
        yaw = yaw2;
    }

    public float getPitch() {
        return pitch;
    }

    public static void setPitch(float pitch2) {
        pitch = pitch2;
    }
}

