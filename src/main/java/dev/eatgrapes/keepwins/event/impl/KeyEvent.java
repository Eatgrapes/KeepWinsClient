package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.CancellableEvent;


public class KeyEvent extends CancellableEvent {
    public int key;

    public KeyEvent(int key) {
        this.key = key;
    }
}
