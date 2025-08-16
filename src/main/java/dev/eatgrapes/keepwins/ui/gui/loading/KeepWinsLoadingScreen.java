package dev.eatgrapes.keepwins.ui.gui.loading;

import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import dev.eatgrapes.keepwins.ui.font.nano.NanoFontLoader;
import dev.eatgrapes.keepwins.ui.font.nano.NanoFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import java.awt.Color;
import java.io.InputStream;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

public class KeepWinsLoadingScreen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("KeepWins/image/LoadingImage.png");
    private static int backgroundImageId = -1;
    private static boolean backgroundLoaded = false;
    private static boolean loadingBackground = false;
    private static long startTime = -1; // 使用-1表示尚未初始化
    private static float currentProgress = 0.0f;
    
    /**
     * 使用NanoVG绘制自定义加载界面背景
     * @param width 屏幕宽度
     * @param height 屏幕高度
     */
    public static void drawCustomBackground(int width, int height) {
        if (vg == 0) {
            // NanoVG上下文未初始化，尝试初始化
            dev.eatgrapes.keepwins.util.render.nano.NanoLoader.createContext();
            if (vg == 0) {
                return;
            }
        }
        
        // 每次绘制时都检查并可能初始化开始时间
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        
        // 开始NanoVG帧
        NanoUtil.beginFrame();
        
        // 绘制背景图片
        drawBackground(width, height);
        
        // 绘制"Initializing..."文本
        drawInitializingText(width, height);
        
        // 结束NanoVG帧
        NanoUtil.endFrame();
    }
    
    /**
     * 绘制背景图片
     * @param width 屏幕宽度
     * @param height 屏幕高度
     */
    private static void drawBackground(int width, int height) {
        // 确保背景图片已加载
        loadBackgroundImage();
        
        if (backgroundImageId != -1 && backgroundImageId != 0) {
            // 绘制放大并居中的背景图片，保持宽高比
            nvgBeginPath(vg);
            nvgRect(vg, 0, 0, width, height);
            
            // 创建图像画笔，使用NVG_IMAGE_REPEATX | NVG_IMAGE_REPEATY模式放大图片
            NVGPaint imgPaint = NVGPaint.calloc();
            // 放大图片以填充整个屏幕，可能会裁剪图片边缘
            float scale = Math.max((float)width / 1920, (float)height / 1080) * 1.5f; // 1.5倍放大
            nvgImagePattern(vg, (width - 1920 * scale) / 2, (height - 1080 * scale) / 2, 1920 * scale, 1080 * scale, 0, backgroundImageId, 1.0f, imgPaint);
            nvgFillPaint(vg, imgPaint);
            nvgFill(vg);
            imgPaint.free();
        } else {
            // 如果图片加载失败，绘制黑色背景
            drawBlackBackground(width, height);
        }
    }
    
    /**
     * 绘制黑色背景
     * @param width 屏幕宽度
     * @param height 屏幕高度
     */
    private static void drawBlackBackground(int width, int height) {
        // 使用NanoVG绘制纯黑色背景
        nvgBeginPath(vg);
        nvgRect(vg, 0, 0, width, height);
        nvgFillColor(vg, getColor(0, 0, 0, 255));
        nvgFill(vg);
    }
    
    /**
     * 绘制"Initializing..."文本
     * @param width 屏幕宽度
     * @param height 屏幕高度
     */
    private static void drawInitializingText(int width, int height) {
        // 确保字体已加载
        NanoFontLoader.registerFonts();
        
        if (NanoFontLoader.misans != null) {
            // 使用Misans字体绘制大号"Initializing..."文本
            NanoFontLoader.misans.drawString("Initializing...", width / 2.0f, height / 2.0f, 60f, 
                NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE, 
                new Color(255, 255, 255, 255));
        } else {
            // 如果Misans字体未加载成功，使用默认NanoVG字体
            nvgFontSize(vg, 60.0f); // 大字体
            nvgTextAlign(vg, NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
            nvgFillColor(vg, getColor(255, 255, 255, 255)); // 白色文本
            
            // 在屏幕中央绘制文本
            nvgText(vg, width / 2.0f, height / 2.0f, "Initializing...");
        }
    }
    
    /**
     * 加载背景图像
     */
    private static void loadBackgroundImage() {
        if (backgroundLoaded || loadingBackground) {
            return;
        }
        
        loadingBackground = true;
        
        try {
            Minecraft mc = Minecraft.getMinecraft();
            // 尝试从资源管理器加载图像
            InputStream inputStream = mc.getResourceManager().getResource(BACKGROUND_TEXTURE).getInputStream();
            backgroundImageId = NanoUtil.genImageId(inputStream);
            
            // 关闭输入流以释放资源
            try {
                inputStream.close();
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            // 加载失败
            backgroundImageId = -1;
        }
        
        backgroundLoaded = true;
        loadingBackground = false;
    }
    
    /**
     * 获取NanoVG颜色 - 修复UNSAFE相关错误
     */
    private static NVGColor getColor(int r, int g, int b, int a) {
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
            putFloatMethod.invoke(unsafe, null, address, r / 255f);       // r
            putFloatMethod.invoke(unsafe, null, address + 4, g / 255f); // g
            putFloatMethod.invoke(unsafe, null, address + 8, b / 255f);  // b
            putFloatMethod.invoke(unsafe, null, address + 12, a / 255f); // a
        } catch (Exception e) {
            // 如果Unsafe方法失败，回退到传统方法
            nvgColor.r(r / 255f)
                    .g(g / 255f)
                    .b(b / 255f)
                    .a(a / 255f);
        }
        return nvgColor;
    }

    
    /**
     * 重置背景状态，用于重新加载
     */
    public static void reset() {
        backgroundLoaded = false;
        backgroundImageId = -1;
        loadingBackground = false;
        startTime = -1; // 重置为-1，将在下次绘制时重新初始化
    }
    
    /**
     * 设置进度值
     * @param progress 进度值 (0.0 - 100.0)
     */
    public static void setProgress(float progress) {
        currentProgress = progress / 100.0f;
    }
}