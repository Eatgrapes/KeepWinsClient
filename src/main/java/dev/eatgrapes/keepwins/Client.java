package dev.eatgrapes.keepwins;

import dev.eatgrapes.keepwins.event.EventBus;
import dev.eatgrapes.keepwins.event.KeyboardListener;
import dev.eatgrapes.keepwins.util.Logger;
import dev.eatgrapes.keepwins.config.ConfigManager;
import dev.eatgrapes.keepwins.ui.UiManager;
import dev.eatgrapes.keepwins.util.render.nano.NanoLoader;
import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;

public class Client {
    public static final Client instance = new Client();
    private final EventBus eventBus = new EventBus();
    private final KeyboardListener keyboardListener = new KeyboardListener();
    
    public void init() {
        Logger.info("Initializing KeepWins");

        
        // 初始化UI管理器
        UiManager.getInstance();

        
        // 注册事件监听器
        eventBus.register(keyboardListener, dev.eatgrapes.keepwins.event.type.SubscriberDepth.NONE);
        
        Logger.info("KeepWins initialized successfully");
    }
    
    public EventBus getEventBus() {
        return eventBus;
    }
}