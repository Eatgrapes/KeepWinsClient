package dev.eatgrapes.keepwins.ui;

import dev.eatgrapes.keepwins.util.MinecraftInstance;

public class UiManager extends MinecraftInstance {
    private static UiManager instance;
    private boolean clickGuiOpen = false;

    public static UiManager getInstance() {
        if (instance == null) {
            instance = new UiManager();
        }
        return instance;
    }

    public boolean isClickGuiOpen() {
        return clickGuiOpen;
    }

    public void setClickGuiOpen(boolean clickGuiOpen) {
        this.clickGuiOpen = clickGuiOpen;
    }
}