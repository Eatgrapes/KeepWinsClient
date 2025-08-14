package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.Event;


public class Render3DEvent extends Event {
    public float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
