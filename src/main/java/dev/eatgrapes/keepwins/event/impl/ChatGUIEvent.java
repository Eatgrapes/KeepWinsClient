package dev.eatgrapes.keepwins.event.impl;

import dev.eatgrapes.keepwins.event.api.Event;

public class ChatGUIEvent extends Event {
    public int mouseX, mouseY;

    public ChatGUIEvent(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
