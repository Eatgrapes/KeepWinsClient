package dev.eatgrapes.keepwins.util.render.nano;

import dev.eatgrapes.keepwins.config.ConfigManager;
import dev.eatgrapes.keepwins.util.Logger;
import dev.eatgrapes.keepwins.util.MinecraftInstance;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

public class NanoUtil extends MinecraftInstance {
    public static final Map<String, Integer> imageMap = new HashMap<>();
    
    // 添加清理方法
    public static void cleanup() {
        // 清理所有缓存的图像
        for (Integer imageId : imageMap.values()) {
            if (imageId != null && imageId != 0) {
                nvgDeleteImage(vg, imageId);
            }
        }
        imageMap.clear();
    }

    public static void beginFrame() {
        // 检查NanoVG上下文是否已初始化，如果未初始化则创建
        if (vg == 0) {
            dev.eatgrapes.keepwins.util.render.nano.NanoLoader.createContext();
        }
        
        // 确保上下文有效后再调用nvgBeginFrame
        if (vg != 0) {
            nvgBeginFrame(vg, mc.displayWidth, mc.displayHeight, 1f);
            ScaledResolution sr = new ScaledResolution(mc);
            // 适应mc的scale
            scaleStart(0, 0, sr.getScaleFactor());
        } else {
            dev.eatgrapes.keepwins.util.Logger.error("Failed to initialize NanoVG context in beginFrame");
        }
    }
    
    public static void endFrame() {
        if (vg != 0) {
            nvgEndFrame(vg);
        }
    }

    public static void beginPath() {
        nvgBeginPath(vg);
    }

    public static void closePath() {
        nvgClosePath(vg);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        beginPath();
        nvgRoundedRect(vg, x, y, width, height, radius);
        fillColor(color);
        nvgFill(vg);
    }

    public static void drawRect(float x, float y, float width, float height, Color color) {
        beginPath();
        nvgRect(vg, x, y, width, height);
        fillColor(color);
        nvgFill(vg);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float leftTopRadius, float rightTopRadius, float rightBottomRadius, float leftBottomRadius, Color color) {
        beginPath();
        nvgRoundedRectVarying(vg, x, y, width, height, leftTopRadius, rightTopRadius, rightBottomRadius, leftBottomRadius);
        fillColor(color);
        nvgFill(vg);
    }

    public static NVGColor getColor(Color color) {
        // 使用不同的方式设置NVGColor对象值，避免UNSAFE错误
        NVGColor nvgColor = NVGColor.calloc();
        // 直接通过address获取内存地址并使用Unsafe设置值
        long address = nvgColor.address();
        // 使用Unsafe设置float值 (如果可用)
        try {
            // 使用反射获取Unsafe实例
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            java.lang.reflect.Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Object unsafe = unsafeField.get(null);
            
            // 使用Unsafe设置内存值
            // NVGColor结构: float r, g, b, a (每个4字节)
            java.lang.reflect.Method putFloatMethod = unsafeClass.getMethod("putFloat", Object.class, long.class, float.class);
            putFloatMethod.invoke(unsafe, null, address, color.getRed() / 255f);       // r
            putFloatMethod.invoke(unsafe, null, address + 4, color.getGreen() / 255f); // g
            putFloatMethod.invoke(unsafe, null, address + 8, color.getBlue() / 255f);  // b
            putFloatMethod.invoke(unsafe, null, address + 12, color.getAlpha() / 255f); // a
        } catch (Exception e) {
            // 如果Unsafe方法失败，回退到传统方法
            nvgColor.r(color.getRed() / 255f)
                    .g(color.getGreen() / 255f)
                    .b(color.getBlue() / 255f)
                    .a(color.getAlpha() / 255f);
        }
        return nvgColor;
    }

    public static int genImageId(InputStream inputStream) {
        byte[] data;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] dataChunk = new byte[8192];

            while ((nRead = inputStream.read(dataChunk, 0, dataChunk.length)) != -1) {
                buffer.write(dataChunk, 0, nRead);
            }

            buffer.flush();
            data = buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteBuffer byteBuffer = MemoryUtil.memAlloc(data.length);
        byteBuffer.put(data).flip();

        return nvgCreateImageMem(vg, 0, byteBuffer);
    }

    public static int genImageId(BufferedImage image) {
        File cacheFile = new File(ConfigManager.cacheDir, ThreadLocalRandom.current().nextFloat() + ".png");
        try {
            ImageIO.write(image, "png", cacheFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nvgCreateImage(vg, cacheFile.getAbsolutePath(), 0);
    }

    public static int genImageId(File file) {
        return nvgCreateImage(vg, file.getAbsolutePath(), 0);
    }

    public static void drawImageRect(String name, InputStream inputStream, float x, float y, float width, float height) {
        int imageId = imageMap.containsKey(name) ? imageMap.get(name) : genImageId(inputStream);

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageRect(imageId, x, y, width, height);

        imageMap.put(name, imageId);
    }

    public static void drawImageRect(ResourceLocation location, float x, float y, float width, float height) {
        int imageId = 0;
        if (imageMap.containsKey(location.getResourcePath())) {
            imageId = imageMap.get(location.getResourcePath());
        } else {
            try {
                InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
                imageId = genImageId(inputStream);
            } catch (IOException e) {

            }
        }

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageRect(imageId, x, y, width, height);

        imageMap.put(location.getResourcePath(), imageId);
    }

    public static void drawImageRect(ResourceLocation location, float x, float y, float width, float height, Color color) {
        int imageId = 0;
        if (imageMap.containsKey(location.getResourcePath())) {
            imageId = imageMap.get(location.getResourcePath());
        } else {
            try {
                InputStream inputStream = mc.getResourceManager().getResource(location).getInputStream();
                imageId = genImageId(inputStream);
            } catch (IOException e) {

            }
        }

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageRect(imageId, x, y, width, height, color);

        imageMap.put(location.getResourcePath(), imageId);
    }

    public static void drawImageRect(int imageId, float x, float y, float width, float height) {
        NVGPaint imgPaint = NVGPaint.calloc();

        nvgBeginPath(vg);  // 开始一个新的路径
        nvgRect(vg, x, y, width, height);  // 定义一个矩形区域用于绘制图像
        nvgImagePattern(vg, x, y, width, height, 0, imageId, 1.0f, imgPaint);
        nvgFillPaint(vg, imgPaint);  // 设置填充样式为图像
        nvgFill(vg);  // 填充图像

        imgPaint.free();
    }

    public static void drawImageRect(int imageId, float x, float y, float width, float height, Color color) {
        NVGPaint imgPaint = NVGPaint.calloc();

        nvgBeginPath(vg);  // 开始一个新的路径
        nvgRect(vg, x, y, width, height);  // 定义一个矩形区域用于绘制图像
        nvgImagePattern(vg, x, y, width, height, 0, imageId, 1.0f, imgPaint);
        fillColor(color);
        nvgFillPaint(vg, imgPaint);  // 设置填充样式为图像
        nvgFill(vg);  // 填充图像

        imgPaint.free();
    }

    public static void drawImageCircle(String name, InputStream inputStream, float x, float y, float radius) {
        int imageId = imageMap.containsKey(name) ? imageMap.get(name) : genImageId(inputStream);

        if (imageId == -1 || imageId == 0) {
            // 处理图像加载失败的情况
            return;
        }

        drawImageCircle(imageId, x, y, radius);

        imageMap.put(name, imageId);
    }

    public static void drawImageCircle(int imageId, float x, float y, float radius) {
        drawImageCircle(imageId, x, y, radius, 0f);
    }

    public static void drawImageCircle(int imageId, float x, float y, float radius, float angle) {
        NVGPaint imgPaint = NVGPaint.calloc();

        nvgBeginPath(vg);  // 开始一个新的路径
        nvgCircle(vg, x, y, radius);  // 定义一个圆形区域用于绘制图像
        nvgImagePattern(vg, x - radius, y - radius, radius * 2f, radius * 2f, angle, imageId, 1.0f, imgPaint);
        nvgFillPaint(vg, imgPaint);  // 设置填充样式为图像
        nvgFill(vg);  // 填充图像

        imgPaint.free();
    }

    public static void drawRoundedOutlineRect(float x, float y, float width, float height, float radius, float outlineWidth, Color fillColor, Color outlineColor) {
        drawRoundedRect(x, y, width, height, radius, fillColor);
        strokeColor(outlineColor);
        nvgStrokeWidth(vg, outlineWidth);
        nvgStroke(vg);
    }

    public static void drawCircle(float centerX, float centerY, float radius, Color color) {
        beginPath();
        nvgCircle(vg, centerX, centerY, radius);
        fillColor(color);
        nvgFill(vg);
    }

    public static void scaleStart(float centerX, float centerY, float scale) {
        nvgSave(vg);
        nvgTranslate(vg, centerX, centerY);
        nvgScale(vg, scale, scale);
        nvgTranslate(vg, -centerX, -centerY);
    }

    public static void scaleEnd() {
        nvgRestore(vg);
    }

    /**
     * 绘制折线（Polyline）
     *
     * @param points    折线顶点数组，每两个浮点数为一个点 (x, y)
     * @param color     线条颜色
     * @param lineWidth 线条宽度
     */
    public static void drawPolyline(float[] points, int color, float lineWidth) {
        if (points.length < 4) {
            throw new IllegalArgumentException("At least two points are required to render a line.");
        }

        // 开始绘制路径
        nvgBeginPath(vg);

        // 设置线条颜色和宽度
        strokeColor(new Color(color));
        nvgStrokeWidth(vg, lineWidth);

        // 移动到第一个点
        nvgMoveTo(vg, points[0], points[1]);

        // 添加折线的每个点
        for (int i = 2; i < points.length; i += 2) {
            nvgLineTo(vg, points[i], points[i + 1]);
        }

        // 描边路径
        nvgStroke(vg);
    }

    public static void rotateStart(float centerX, float centerY, float angle) {
        // 保存当前状态
        nvgSave(vg);

        // 移动到绘制区域的中心点
        nvgTranslate(vg, centerX, centerY);

        nvgRotate(vg, NVG_PI / angle);
    }

    public static void rotateEnd() {
        nvgRestore(vg);
    }

    public static void transformStart(float x, float y) {
        nvgSave(vg);
        nvgTranslate(vg, x, y);
    }

    public static void transformEnd() {
        nvgRestore(vg);
    }

    public static void scissorStart(float x, float y, float width, float height) {
        nvgSave(vg);
        nvgScissor(vg, x, y, width, height);
    }

    public static void scissorEnd() {
        nvgRestore(vg);
    }

    public static void fillColor(Color color) {
        NVGColor nvgColor = getColor(color);
        nvgFillColor(NanoLoader.vg, nvgColor);
        nvgColor.free();
    }

    public static void strokeColor(Color color) {
        NVGColor nvgColor = getColor(color);
        NanoVG.nvgStrokeColor(vg, nvgColor);
        nvgColor.free();
    }
}
