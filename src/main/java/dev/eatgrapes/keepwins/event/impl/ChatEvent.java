package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.CancellableEvent;

public class ChatEvent extends CancellableEvent {
    public String text;

    public ChatEvent(String text) {
        this.text = text;
    }
}
