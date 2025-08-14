package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.Event;


public class ShaderEvent extends Event {
    public boolean bloom;

    public ShaderEvent(boolean bloom) {
        this.bloom = bloom;
    }
}
