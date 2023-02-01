/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.move;

import heaven.main.event.Event;
import heaven.main.utils.MoveUtils;
import net.minecraft.client.Minecraft;

public final class StrafeEvent
extends Event {
    private final float forward;
    private final float strafe;
    private float friction;

    public StrafeEvent(float forward, float strafe, float friction) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
    }

    public void setSpeedPartialStrafe(float friction, float strafe) {
        float remainder = 1.0f - strafe;
        if (this.forward != 0.0f && this.strafe != 0.0f) {
            friction = (float)((double)friction * 0.91);
        }
        if (Minecraft.thePlayer.onGround) {
            this.setSpeed(friction);
        } else {
            Minecraft.thePlayer.motionX *= (double)strafe;
            Minecraft.thePlayer.motionZ *= (double)strafe;
            this.setFriction(friction * remainder);
        }
    }

    public void setSpeed(float speed, double motionMultiplier) {
        this.setFriction(this.getForward() != 0.0f && this.getStrafe() != 0.0f ? speed * 0.99f : speed);
        Minecraft.thePlayer.motionX *= motionMultiplier;
        Minecraft.thePlayer.motionZ *= motionMultiplier;
    }

    public void setSpeed(float speed) {
        this.setFriction(this.getForward() != 0.0f && this.getStrafe() != 0.0f ? speed * 0.99f : speed);
        MoveUtils.stop();
    }

    private void setFriction(float v) {
        this.friction = v;
    }

    public float getStrafe() {
        return this.strafe;
    }

    public float getForward() {
        return this.forward;
    }

    public float getFriction() {
        return this.friction;
    }
}

