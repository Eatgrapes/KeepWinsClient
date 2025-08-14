package dev.eatgrapes.keepwins.ui.font.awt;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;


public class FontUtil {
    private static final IResourceManager RESOURCE_MANAGER = Minecraft.getMinecraft().getResourceManager();

    public static Font getResource(String resource, int size) {
        try {
            return Font.createFont(0, RESOURCE_MANAGER.getResource(new ResourceLocation(resource)).getInputStream()).deriveFont((float) size);
        } catch (IOException | FontFormatException var3) {
            return null;
        }
    }
}
