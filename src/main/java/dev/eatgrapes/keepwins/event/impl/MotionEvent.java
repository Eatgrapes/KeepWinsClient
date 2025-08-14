package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.CancellableEvent;


public class MotionEvent extends CancellableEvent {
    public double x, y, z;
    public float yaw, pitch;
    public boolean ground;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean ground) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
    }
}
