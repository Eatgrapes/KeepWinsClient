package dev.eatgrapes.keepwins.util;

import net.minecraft.client.Minecraft;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ResourceLocation {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final List<String> DIRS = Arrays.asList("config");
    
    public static File getDirectory() {
        File keepWinsDir = new File(mc.mcDataDir, "KeepWins");
        if (!keepWinsDir.exists()) {
            keepWinsDir.mkdirs();
        }
        
        for (String dir : DIRS) {
            File subDir = new File(keepWinsDir, dir);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }
        }
        
        return keepWinsDir;
    }
    
    public static File getFile(String fileName) {
        return new File(getDirectory(), fileName);
    }
    
    public static File getConfigDirectory() {
        return new File(getDirectory(), "config");
    }
    
    public static File getConfigFile(String fileName) {
        return new File(getConfigDirectory(), fileName);
    }
}