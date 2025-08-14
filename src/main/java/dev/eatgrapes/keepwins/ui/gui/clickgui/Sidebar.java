package dev.eatgrapes.keepwins.ui.gui.clickgui;

import dev.eatgrapes.keepwins.ui.gui.clickgui.api.ButtonAPI;
import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;

import java.awt.*;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

public class Sidebar {
    private static final int SIDEBAR_WIDTH = 60;
    private static int logoImageId = -1;
    private static ButtonAPI homeButton;
    
    public static void init() {
        // 初始化主页按钮，调整大小为30x30，位置为(15, 70)
        homeButton = new ButtonAPI(15, 70, 30, 30)
                .setIconPath("home.png")
                .setBackgroundColor(new Color(245, 245, 245)) // 比白色深一点点
                .setHoverColor(new Color(245, 245, 245)) // 悬停时颜色不变
                .setBorderColor(new Color(245, 245, 245)) // 边框颜色与背景一致
                .setSelectedColor(new Color(200, 180, 220)) // 浅紫色
                .setSelectedBorderColor(new Color(150, 100, 190)); // 深一点的紫色描边
        
        // 设置默认选中状态
        homeButton.setSelected(true);
    }
    
    public static void render(int guiX, int guiY, int guiHeight) {
        // 绘制侧边栏背景（纯白色，只有左侧有圆角）
        NanoUtil.beginPath();
        nvgRoundedRectVarying(vg, guiX, guiY, SIDEBAR_WIDTH, guiHeight, 15f, 0f, 0f, 15f); // 左上15f，右上0f，右下0f，左下15f
        NanoUtil.fillColor(new Color(255, 255, 255)); // 纯白色
        nvgFill(vg);
        
        // 绘制Logo（如果已加载）
        if (logoImageId != -1 && logoImageId != 0) {
            int logoSize = 40;
            int logoX = guiX + 10;
            int logoY = guiY + 10;
            NanoUtil.drawImageCircle(logoImageId, logoX + logoSize/2f, logoY + logoSize/2f, logoSize/2f);
        }
        
        // 渲染主页按钮
        if (homeButton != null) {
            // 临时调整按钮位置以适应侧边栏位置
            homeButton.setX(guiX + 15).setY(guiY + 70);
            homeButton.render();
        }
    }
    
    public static void onMouseMove(float mouseX, float mouseY) {
        if (homeButton != null) {
            homeButton.onMouseMove(mouseX, mouseY);
        }
    }
    
    public static void onMouseClick(float mouseX, float mouseY, int button) {
        if (homeButton != null) {
            homeButton.onMouseClick(mouseX, mouseY, button);
        }
    }
    
    public static int getWidth() {
        return SIDEBAR_WIDTH;
    }
    
    public static void setLogoImageId(int id) {
        logoImageId = id;
    }
    
    public static void cleanup() {
        // 清理操作
    }
}