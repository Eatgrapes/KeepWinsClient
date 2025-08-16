package dev.eatgrapes.keepwins.ui.gui.clickgui.page;

import dev.eatgrapes.keepwins.ui.font.nano.NanoFontLoader;
import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.Color;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

public class ModulesPage {

    public static void render(int guiX, int guiY, int guiWidth, int guiHeight) {
        NanoFontLoader.registerFonts();

        if (NanoFontLoader.misans != null) {
            NanoFontLoader.misans.drawString(
                    "Modules",
                    guiX + guiWidth / 2.0f,
                    guiY + guiHeight / 2.0f,
                    24,
                    NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE,
                    Color.BLACK
            );
        } else {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            String text = "Modules";
            int textWidth = fontRenderer.getStringWidth(text);
            int textHeight = fontRenderer.FONT_HEIGHT;
            int x = guiX + (guiWidth - textWidth) / 2;
            int y = guiY + (guiHeight - textHeight) / 2;
            fontRenderer.drawString(text, x, y, Color.BLACK.getRGB());
        }
    }
}