package dev.eatgrapes.keepwins.event;

import dev.eatgrapes.keepwins.event.api.SubscribeEvent;
import dev.eatgrapes.keepwins.ui.UiManager;
import dev.eatgrapes.keepwins.ui.gui.clickgui.ClickGUI;
import dev.eatgrapes.keepwins.util.MinecraftInstance;
import dev.eatgrapes.keepwins.event.impl.KeyEvent;
import dev.eatgrapes.keepwins.util.Logger;
import org.lwjgl.input.Keyboard;

public class KeyboardListener extends MinecraftInstance {
    
    @SubscribeEvent
    public void onKeyInput(KeyEvent event) {
        // 检查是否按下了右Shift键
        if (event.key == Keyboard.KEY_RSHIFT) {
            // 确保游戏没有打开其他GUI界面
            if (mc.currentScreen == null) {
                // 确保ClickGUI未被打开
                if (!UiManager.getInstance().isClickGuiOpen()) {
                    Logger.info("Opening ClickGUI");
                    mc.displayGuiScreen(new ClickGUI());
                    UiManager.getInstance().setClickGuiOpen(true);
                }
            }
        }
    }
}