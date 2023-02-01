/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.drakApi;

import heaven.main.event.Event;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.event.events.world.EventPreUpdate;

public class MotionUpdateEvent
extends Event {
    private float yaw;
    private float pitch;
    private double posY;
    private double posZ;
    private double posX;
    private boolean onGround;
    private State state;

    public MotionUpdateEvent(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround, State state) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.state = state;
    }

    public MotionUpdateEvent(State state) {
        this.state = state;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getY() {
        return this.posY;
    }

    public double getZ() {
        return this.posZ;
    }

    public double getX() {
        return this.posX;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public State getState() {
        return this.state;
    }

    public void setYaw(float yaw) {
        if (this.state == State.PRE) {
            EventPreUpdate.setYaw(yaw);
        }
        if (this.state == State.POST) {
            EventPostUpdate.setYaw(yaw);
        }
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        if (this.state == State.PRE) {
            EventPreUpdate.setPitch(pitch);
        }
        if (this.state == State.POST) {
            EventPostUpdate.setPitch(pitch);
        }
        this.pitch = pitch;
    }

    public void setY(double posY) {
        if (this.state == State.PRE) {
            EventPreUpdate.setY(posY);
        }
        this.posY = posY;
    }

    public void setZ(double posZ) {
        if (this.state == State.PRE) {
            EventPreUpdate.setZ(posZ);
        }
        this.posZ = posZ;
    }

    public void setX(double posX) {
        if (this.state == State.PRE) {
            EventPreUpdate.setX(posX);
        }
        this.posX = posX;
    }

    public void setOnGround(boolean onGround) {
        if (this.state == State.PRE) {
            EventPreUpdate.setOnGround(onGround);
        }
        this.onGround = onGround;
    }

    public void setState(State state) {
        this.state = state;
    }

    public static enum State {
        PRE,
        POST;

    }
}

