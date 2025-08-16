package dev.eatgrapes.keepwins.ui.gui.clickgui;

import dev.eatgrapes.keepwins.ui.UiManager;
import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import dev.eatgrapes.keepwins.util.Logger;
import dev.eatgrapes.keepwins.ui.gui.clickgui.page.HomePage;
import dev.eatgrapes.keepwins.ui.gui.clickgui.page.ModulesPage;
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
    private static final long ANIMATION_DURATION = 400;
    private static final ResourceLocation LOGO_LOCATION = new ResourceLocation("KeepWins/logo.png");

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

        Sidebar.init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (vg == 0) {
            dev.eatgrapes.keepwins.util.render.nano.NanoLoader.createContext();
        }

        NanoUtil.beginFrame();

        float animationProgress = getAnimationProgress();

        if (closing && animationProgress <= 0) {
            NanoUtil.endFrame();
            mc.displayGuiScreen(null);
            UiManager.getInstance().setClickGuiOpen(false);
            return;
        }

        float easedProgress = 1 - (float) Math.pow(1 - animationProgress, 4);
        float alpha = easingFunction(animationProgress);

        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        int guiWidth = Math.max(470, Math.min(670, screenWidth / 3 + 70));
        int guiHeight = Math.max(300, Math.min(450, screenHeight / 3 + 50));

        int guiX = (screenWidth - guiWidth) / 2;
        int guiY = (screenHeight - guiHeight) / 2;

        float scale = closing ? (1.0f - 0.5f * (1.0f - easedProgress)) : (0.5f + 0.5f * easedProgress);

        nvgSave(vg);
        nvgTranslate(vg, screenWidth / 2f, screenHeight / 2f);
        nvgScale(vg, scale, scale);
        nvgTranslate(vg, -guiWidth / 2f, -guiHeight / 2f);

        nvgGlobalAlpha(vg, alpha);

        // 处理鼠标移动事件（在drawScreen中处理）
        float transformedMouseX = (mouseX - screenWidth / 2f) / scale + guiWidth / 2f;
        float transformedMouseY = (mouseY - screenHeight / 2f) / scale + guiHeight / 2f;
        Sidebar.onMouseMove(transformedMouseX, transformedMouseY);

        loadLogoImage();
        Sidebar.setLogoImageId(logoImageId);

        Sidebar.render(0, 0, guiHeight);

        NanoUtil.beginPath();
        nvgRoundedRectVarying(vg, Sidebar.getWidth(), 0, guiWidth - Sidebar.getWidth(), guiHeight, 0f, 15f, 15f, 0f);
        NanoUtil.fillColor(new Color(245, 245, 245, (int) (alpha * 255)));
        nvgFill(vg);

        // 修改：根据当前页面渲染不同内容
        switch (PageManager.getCurrentPage()) {
            case HOME:
                HomePage.render(Sidebar.getWidth(), 0, guiWidth - Sidebar.getWidth(), guiHeight);
                break;
            case MODULES:
                ModulesPage.render(Sidebar.getWidth(), 0, guiWidth - Sidebar.getWidth(), guiHeight);
                break;
        }

        nvgRestore(vg);
        NanoUtil.endFrame();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // 计算变换后的坐标
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        int guiWidth = Math.max(470, Math.min(670, screenWidth / 3 + 70));
        int guiHeight = Math.max(300, Math.min(450, screenHeight / 3 + 50));

        float animationProgress = getAnimationProgress();
        float easedProgress = 1 - (float) Math.pow(1 - animationProgress, 4);
        float scale = closing ? (1.0f - 0.5f * (1.0f - easedProgress)) : (0.5f + 0.5f * easedProgress);

        // 反向变换鼠标坐标
        float transformedMouseX = (mouseX - screenWidth / 2f) / scale + guiWidth / 2f;
        float transformedMouseY = (mouseY - screenHeight / 2f) / scale + guiHeight / 2f;

        // 传递变换后的坐标给侧边栏
        Sidebar.onMouseClick(transformedMouseX, transformedMouseY, mouseButton);
    }

    private void loadLogoImage() {
        if (logoImageId != -1) {
            return;
        }

        try {
            java.io.InputStream inputStream = mc.getResourceManager().getResource(LOGO_LOCATION).getInputStream();
            logoImageId = NanoUtil.genImageId(inputStream);

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
            return Math.max(0, progress);
        } else {
            if (openTime == -1) {
                openTime = System.currentTimeMillis();
            }
            long elapsed = System.currentTimeMillis() - openTime;
            return Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            close();
        }
    }

    private void close() {
        if (!closing) {
            closing = true;
            closeTime = -1;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Logger.info("ClickGUI onGuiClosed called");
        UiManager.getInstance().setClickGuiOpen(false);
        cleanup();
    }

    private void cleanup() {
        logoImageId = -1;
    }

    private float easingFunction(float progress) {
        if (closing) {
            return (float) (1.0f - Math.pow(1 - progress, 3));
        } else {
            return (float) (1.0f - Math.pow(1 - progress, 3));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}