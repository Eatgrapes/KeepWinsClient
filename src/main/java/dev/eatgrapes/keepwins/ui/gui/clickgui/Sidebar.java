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
    private static ButtonAPI modulesButton; // 新增modules按钮

    public static void init() {
        homeButton = new ButtonAPI(15, 70, 30, 30)
                .setIconPath("home.svg")
                .setBackgroundColor(new Color(245, 245, 245))
                .setHoverColor(new Color(245, 245, 245))
                .setBorderColor(new Color(245, 245, 245))
                .setSelectedColor(new Color(200, 180, 220))
                .setSelectedBorderColor(new Color(150, 100, 190));

        // 新增modules按钮
        modulesButton = new ButtonAPI(15, 110, 30, 30)
                .setIconPath("modules.svg")
                .setBackgroundColor(new Color(245, 245, 245))
                .setHoverColor(new Color(245, 245, 245))
                .setBorderColor(new Color(245, 245, 245))
                .setSelectedColor(new Color(200, 180, 220))
                .setSelectedBorderColor(new Color(150, 100, 190));

        homeButton.setSelected(true);
    }

    public static void render(int guiX, int guiY, int guiHeight) {
        NanoUtil.beginPath();
        nvgRoundedRectVarying(vg, guiX, guiY, SIDEBAR_WIDTH, guiHeight, 15f, 0f, 0f, 15f);
        NanoUtil.fillColor(new Color(255, 255, 255));
        nvgFill(vg);

        if (logoImageId != -1 && logoImageId != 0) {
            int logoSize = 40;
            int logoX = guiX + 10;
            int logoY = guiY + 10;
            NanoUtil.drawImageCircle(logoImageId, logoX + logoSize/2f, logoY + logoSize/2f, logoSize/2f);
        }

        if (homeButton != null) {
            homeButton.setX(guiX + 15).setY(guiY + 70);
            homeButton.render();
        }

        // 渲染modules按钮
        if (modulesButton != null) {
            modulesButton.setX(guiX + 15).setY(guiY + 110);
            modulesButton.render();
        }
    }

    public static void onMouseMove(float mouseX, float mouseY) {
        if (homeButton != null) {
            homeButton.onMouseMove(mouseX, mouseY);
        }
        if (modulesButton != null) {
            modulesButton.onMouseMove(mouseX, mouseY);
        }
    }

    public static void onMouseClick(float mouseX, float mouseY, int button) {
        if (homeButton != null) {
            // 使用ButtonAPI的hover状态来判断是否点击
            homeButton.onMouseMove(mouseX, mouseY); // 先更新hover状态
            if (homeButton.isHovered() && button == 0) {
                homeButton.onMouseClick(mouseX, mouseY, button);
                if (modulesButton != null) {
                    modulesButton.setSelected(false);
                }
                homeButton.setSelected(true);
                PageManager.setCurrentPage(PageManager.PageType.HOME);
            }
        }

        if (modulesButton != null) {
            modulesButton.onMouseMove(mouseX, mouseY); // 先更新hover状态
            if (modulesButton.isHovered() && button == 0) {
                modulesButton.onMouseClick(mouseX, mouseY, button);
                if (homeButton != null) {
                    homeButton.setSelected(false);
                }
                modulesButton.setSelected(true);
                PageManager.setCurrentPage(PageManager.PageType.MODULES);
            }
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