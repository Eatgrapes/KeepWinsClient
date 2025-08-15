package dev.eatgrapes.keepwins.ui.gui.clickgui;

import dev.eatgrapes.keepwins.ui.UiManager;
import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import dev.eatgrapes.keepwins.util.Logger;
import dev.eatgrapes.keepwins.ui.gui.clickgui.page.HomePage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

public class ClickGUI extends GuiScreen {
    private long openTime = -1;
    private boolean closing = false;
    private long closeTime = -1;
    private static final long ANIMATION_DURATION = 300; // 增加动画持续时间到300毫秒
    private static final ResourceLocation LOGO_LOCATION = new ResourceLocation("KeepWins/logo.png");
    
    // 缓存图像ID以避免重复加载
    private static final Map<String, Integer> imageCache = new HashMap<>();
    private static int logoImageId = -1;
    
    protected static final net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();

    @Override
    public void initGui() {
        super.initGui();
        Logger.info("ClickGUI initialized");
        if (openTime == -1) {
            openTime = System.currentTimeMillis();
        }
        
        // 初始化侧边栏
        Sidebar.init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 强制确保NanoVG上下文已创建
        if (vg == 0) {
            dev.eatgrapes.keepwins.util.render.nano.NanoLoader.createContext();
        }
        
        // 开始NanoVG绘制
        NanoUtil.beginFrame();
        
        // 计算动画插值
        float animationProgress = getAnimationProgress();
        
        // 如果关闭动画已完成，则关闭界面
        if (closing && animationProgress <= 0) {
            NanoUtil.endFrame();
            mc.displayGuiScreen(null);
            UiManager.getInstance().setClickGuiOpen(false);
            return;
        }
        
        // 应用缓动曲线 (easeOutQuart)
        float easedProgress = 1 - (float) Math.pow(1 - animationProgress, 4);
        
        // 获取屏幕分辨率并计算自适应大小
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        
        // 根据屏幕大小自适应计算ClickGUI尺寸（稍微增大宽度）
        int guiWidth = Math.max(470, Math.min(670, screenWidth / 3 + 70));  // 增大最小宽度和最大宽度
        int guiHeight = Math.max(300, Math.min(450, screenHeight / 3 + 50)); // 保持高度不变
        
        // 计算ClickGUI位置（居中）
        int guiX = (screenWidth - guiWidth) / 2;
        int guiY = (screenHeight - guiHeight) / 2;
        
        // 应用缩放动画效果（使用缓动曲线）
        // 打开时：从小到大 (0.5f to 1.0f)
        // 关闭时：从大到小 (1.0f to 0.5f)
        float scale = closing ? (1.0f - 0.5f * (1.0f - easedProgress)) : (0.5f + 0.5f * easedProgress);
        
        // 绘制主窗口背景（比白色深一点点的颜色，只有右侧有圆角）
        nvgSave(vg);
        nvgTranslate(vg, screenWidth / 2f, screenHeight / 2f);
        nvgScale(vg, scale, scale);
        nvgTranslate(vg, -guiWidth / 2f, -guiHeight / 2f);
        
        // 加载或获取缓存的Logo图像
        loadLogoImage();
        // 将logoImageId传递给侧边栏
        Sidebar.setLogoImageId(logoImageId);
        
        // 绘制侧边栏
        Sidebar.render(0, 0, guiHeight);
        
        // 绘制主内容区域背景（比白色深一点点，只有右侧有圆角）
        NanoUtil.beginPath();
        nvgRoundedRectVarying(vg, Sidebar.getWidth(), 0, guiWidth - Sidebar.getWidth(), guiHeight, 0f, 15f, 15f, 0f); // 左上0f，右上15f，右下15f，左下0f
        NanoUtil.fillColor(new Color(245, 245, 245)); // 比白色深一点点的颜色
        nvgFill(vg);
        
        // 渲染当前页面内容
        HomePage.render(Sidebar.getWidth(), 0, guiWidth - Sidebar.getWidth(), guiHeight);
        
        nvgRestore(vg);
        
        // 结束NanoVG绘制
        NanoUtil.endFrame();
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        // 将鼠标点击事件传递给侧边栏按钮
        Sidebar.onMouseClick(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        
        // 将鼠标移动事件传递给侧边栏按钮
        Sidebar.onMouseMove(mouseX, mouseY);
    }
    
    /**
     * 加载Logo图像并缓存ID
     */
    private void loadLogoImage() {
        // 如果已经加载过图像，直接返回
        if (logoImageId != -1) {
            return;
        }
        
        try {
            // 尝试从资源管理器加载图像
            java.io.InputStream inputStream = mc.getResourceManager().getResource(LOGO_LOCATION).getInputStream();
            logoImageId = NanoUtil.genImageId(inputStream);
            
            // 关闭输入流以释放资源
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        } catch (Exception e) {
            Logger.error("Failed to load logo image: " + e.getMessage());
            logoImageId = -1;
        }
    }
    
    private float getAnimationProgress() {
        if (closing) {
            if (closeTime == -1) {
                closeTime = System.currentTimeMillis();
                Logger.info("ClickGUI closing");
            }
            long elapsed = System.currentTimeMillis() - closeTime;
            float progress = 1.0f - Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
            return Math.max(0, progress); // 确保不会返回负数
        } else {
            if (openTime == -1) {
                openTime = System.currentTimeMillis(); // 确保openTime已初始化
            }
            long elapsed = System.currentTimeMillis() - openTime;
            return Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == 1) { // ESC键
            close();
        }
    }
    
    private void close() {
        if (!closing) {
            closing = true;
            closeTime = -1; // 重置关闭时间以触发关闭动画
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Logger.info("ClickGUI onGuiClosed called");
        UiManager.getInstance().setClickGuiOpen(false);
        
        // 清理资源
        cleanup();
    }
    
    /**
     * 清理NanoVG资源
     */
    private void cleanup() {
        // 不要删除图像，因为它们可能被其他地方使用
        // 只是重置引用
        logoImageId = -1;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}