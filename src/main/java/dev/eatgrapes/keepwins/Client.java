package dev.eatgrapes.keepwins;

import dev.eatgrapes.keepwins.event.EventBus;
import dev.eatgrapes.keepwins.event.KeyboardListener;
import dev.eatgrapes.keepwins.util.Logger;
import dev.eatgrapes.keepwins.ui.UiManager;
import dev.eatgrapes.keepwins.util.data.ResourceLocation;
import dev.eatgrapes.keepwins.ui.font.nano.NanoFontLoader;

public class Client {
    public static final Client instance = new Client();
    private final EventBus eventBus = new EventBus();
    private final KeyboardListener keyboardListener = new KeyboardListener();
    
    public void init() {
        Logger.info("Initializing KeepWins");

        ResourceLocation.getDirectory();
        UiManager.getInstance();
        eventBus.register(keyboardListener, dev.eatgrapes.keepwins.event.type.SubscriberDepth.NONE);
        
        // 注意：字体初始化需要在OpenGL上下文准备好后进行
        // 因此我们不在这里初始化字体，而是在第一次需要使用字体时进行初始化
        
        Logger.info("KeepWins initialized successfully");
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}