package dev.eatgrapes.keepwins.util.render.nano;

import dev.eatgrapes.keepwins.util.Logger;
import net.minecraft.client.Minecraft;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;


public class NanoLoader {
    public static long vg;
    public static void createContext() {
        if (vg == 0) {
            vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES);
            if (vg == 0) {
                Logger.error("Failed to create NanoVG context");
                return;
            }
            NanoVG.nvgShapeAntiAlias(vg, true);
            Logger.info("NanoVG context created successfully");
        }
    }
    
    public static void deleteContext() {
        // 首先清理NanoUtil中的资源
        NanoUtil.cleanup();
        
        if (vg != 0) {
            NanoVGGL3.nvgDelete(vg);
            vg = 0;
            Logger.info("NanoVG context deleted");
        }
    }
    
    public static boolean shouldRender() {
        boolean result = (Minecraft.getMinecraft() != null) && Minecraft.getMinecraft().thePlayer != null && vg != 0;
        return result;
    }
}