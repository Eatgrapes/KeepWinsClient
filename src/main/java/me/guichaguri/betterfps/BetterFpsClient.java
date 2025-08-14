package me.guichaguri.betterfps;

import net.minecraft.client.Minecraft;

/**
 * Client event handling
 * @author Guilherme Chaguri
 */
public class BetterFpsClient {
    protected static Minecraft mc;
    public static BetterFpsConfig config;

    // Called in Minecraft.startGame
    public static void start() {
        mc = Minecraft.getMinecraft();
        if(BetterFpsConfig.instance == null) {
            BetterFpsHelper.loadConfig();
        }
        BetterFpsHelper.init();
    }
}
