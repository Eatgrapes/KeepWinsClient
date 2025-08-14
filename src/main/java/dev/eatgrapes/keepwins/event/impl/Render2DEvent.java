package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.Event;

public class Render2DEvent extends Event {
    public float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
