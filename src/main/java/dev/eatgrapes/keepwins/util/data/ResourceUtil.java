package dev.eatgrapes.keepwins.util.data;

import dev.eatgrapes.keepwins.util.MinecraftInstance;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;

public class ResourceUtil extends MinecraftInstance {
    public static ResourceLocation getResource(String fileName, ResourceType type) {
        return new ResourceLocation(
                "KeepWins/" +
                        (getTypePath(type)) +
                        fileName
        );
    }

    private static String getTypePath(ResourceType type) {
        switch (type) {
            case FONT:
                return "font/";
            case ICON:
                return "icon/";
            case IMAGE:
                return "image/";
            case VIDEO:
                return "video/";
            default:
                return "";
        }
    }

    public static InputStream getResourceAsStream(String fileName, ResourceType type) {
        try {
            String location = "/assets/minecraft/KeepWins/" +
                    (getTypePath(type)) +
                    fileName;
            return ResourceUtil.class.getResourceAsStream(location);
        } catch (Exception e) {
            return null;
        }
    }
}