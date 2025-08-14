package dev.eatgrapes.keepwins.ui.font.nano;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/8/3
 **/
public class NanoFontLoader {
    public static List<NanoFontRenderer> renderers = new ArrayList<>();
    public static NanoFontRenderer misans;
    
    private static boolean initialized = false;

    public static void registerFonts() {
        // 延迟初始化，确保只初始化一次
        if (initialized) {
            return;
        }
        
        try {
            misans = new NanoFontRenderer("MiSans", "misans");

            renderers.add(misans);
            
            initialized = true;
        } catch (Exception e) {
            System.err.println("Failed to initialize fonts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String[] getRenderers() {
        String[] values = new String[renderers.size()];
        for (int i = 0; i < renderers.size(); i++) {
            NanoFontRenderer fontRenderer = renderers.get(i);
            values[i] = fontRenderer.getName();
        }
        return values;
    }
}