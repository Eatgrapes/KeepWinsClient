package dev.eatgrapes.keepwins.util;

import dev.eatgrapes.keepwins.util.misc.Logger;
import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    /**
     * 从资源路径加载图像
     * @param resourcePath 资源路径
     * @return BufferedImage对象
     */
    public static BufferedImage loadImage(String resourcePath) {
        try {
            ResourceLocation location = new ResourceLocation(resourcePath);
            InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            inputStream.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
          Logger.error("Failed to load image: " + resourcePath);
            return null;
        }
    }
    
    /**
     * 从资源路径加载图像并转换为NanoVG图像ID
     * @param resourcePath 资源路径
     * @return NanoVG图像ID
     */
    public static int loadImageAsNanoVG(String resourcePath) {
        try {
            ResourceLocation location = new ResourceLocation(resourcePath);
            InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
            int imageId = NanoUtil.genImageId(inputStream);
            
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
            
            return imageId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * 调整图像大小
     * @param originalImage 原始图像
     * @param targetWidth 目标宽度
     * @param targetHeight 目标高度
     * @return 调整大小后的图像
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
    
    /**
     * 将BufferedImage转换为NanoVG图像ID
     * @param image BufferedImage对象
     * @return NanoVG图像ID
     */
    public static int convertToNanoVG(BufferedImage image) {
        return NanoUtil.genImageId(image);
    }
}