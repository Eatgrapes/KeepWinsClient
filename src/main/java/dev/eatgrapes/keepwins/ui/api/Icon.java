package dev.eatgrapes.keepwins.ui.api;

import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NanoVG;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;

public class Icon {
    private static final Map<String, Integer> iconCache = new HashMap<>();
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    /**
     * 从assets/minecraft/KeepWins/icon文件夹加载图标
     * @param iconPath 图标路径（包含扩展名）
     * @return 图标ID，可用于渲染
     */
    public static int loadIcon(String iconPath) {
        // 检查缓存中是否已存在该图标
        if (iconCache.containsKey(iconPath)) {
            return iconCache.get(iconPath);
        }
        
        try {
            // 构建正确的资源路径
            ResourceLocation resourceLocation;
            if (iconPath.startsWith("KeepWins/")) {
                // 如果路径已经包含了完整的KeepWins前缀
                resourceLocation = new ResourceLocation(iconPath);
            } else if (iconPath.contains("/")) {
                // 如果路径包含了文件夹结构
                resourceLocation = new ResourceLocation("KeepWins/" + iconPath);
            } else {
                // 如果只是文件名，放在icon文件夹下
                resourceLocation = new ResourceLocation("KeepWins/icon/" + iconPath);
            }
            
            InputStream inputStream = mc.getResourceManager().getResource(resourceLocation).getInputStream();
            int iconId = NanoUtil.genImageId(inputStream);
            
            // 缓存图标ID
            iconCache.put(iconPath, iconId);
            
            // 关闭输入流
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
            
            return iconId;
        } catch (Exception e) {
            // 加载失败
            e.printStackTrace();
            return -1; // 返回-1表示加载失败
        }
    }
    
    /**
     * 从assets/minecraft/KeepWins/icon文件夹加载图标（不包含扩展名的旧方法）
     * @param iconName 图标名称（不包含扩展名，默认为png格式）
     * @return 图标ID，可用于渲染
     */
    public static int loadIconByName(String iconName) {
        return loadIcon(iconName + ".png");
    }
    
    /**
     * 渲染图标
     * @param iconPath 图标路径（包含扩展名）
     * @param x X坐标
     * @param y Y坐标
     * @param width 宽度
     * @param height 高度
     */
    public static void renderIcon(String iconPath, float x, float y, float width, float height) {
        int iconId = loadIcon(iconPath);
        if (iconId != -1 && iconId != 0) {
            NanoUtil.drawImageRect(iconId, x, y, width, height);
        }
    }
    
    /**
     * 渲染带颜色的图标
     * @param iconPath 图标路径（包含扩展名）
     * @param x X坐标
     * @param y Y坐标
     * @param width 宽度
     * @param height 高度
     * @param color 颜色
     */
    public static void renderIcon(String iconPath, float x, float y, float width, float height, java.awt.Color color) {
        int iconId = loadIcon(iconPath);
        if (iconId != -1 && iconId != 0) {
            NanoUtil.drawImageRect(iconId, x, y, width, height, color);
        }
    }
    
    /**
     * 清理缓存的图标资源并调用NanoVG的图像清理方法
     */
    public static void cleanup() {
        // 清理图标缓存中的资源
        for (Map.Entry<String, Integer> entry : iconCache.entrySet()) {
            Integer iconId = entry.getValue();
            if (iconId != null && iconId != -1 && iconId != 0) {
                // 调用NanoVG中的图像删除方法
                NanoVG.nvgDeleteImage(vg, iconId);
            }
        }
        
        // 清空缓存
        iconCache.clear();
    }
}