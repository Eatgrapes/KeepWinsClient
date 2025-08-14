package me.guichaguri.betterfps.gui;

import me.guichaguri.betterfps.BetterFpsConfig;
import me.guichaguri.betterfps.BetterFpsHelper;
import me.guichaguri.betterfps.gui.GuiCycleButton.GuiBooleanButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Guilherme Chaguri
 */
public class GuiBetterFpsConfig extends GuiScreen {

    private GuiScreen parent;
    public GuiBetterFpsConfig(GuiScreen parent) {
        this.parent = parent;
    }

    private List<GuiButton> initButtons() {
        List<GuiButton> buttons = new ArrayList<>();
        BetterFpsConfig config = BetterFpsConfig.getConfig();
        buttons.add(new AlgorithmButton(2, "Algorithm", BetterFpsHelper.displayHelpers,
                config.algorithm, new String[]{
                "The algorithm of sine & cosine methods"
        }));
        buttons.add(new GuiBooleanButton(3, "Fast Box Render", config.fastBoxRender, new String[]{
                "Whether will only render the exterior of boxes.",
                "Default in Vanilla: Off",
                "Default in BetterFps: On"
        }));
        buttons.add(new GuiBooleanButton(4, "Fog", config.fog, new String[]{
                "Whether will render the fog.",
                "Default: On"
        }));
        buttons.add(new GuiBooleanButton(5, "Fast Hopper", config.fastHopper, new String[]{
                "Whether will improve the hopper.",
                "Default in Vanilla: Off",
                "Default in BetterFps: On"
        }));
        return buttons;
    }

    @Override
    public void initGui() {
        int x1 = width / 2 - 155;
        int x2 = width / 2 + 5;

        buttonList.clear();
        buttonList.add(new GuiButton(-1, x1, height - 27, 150, 20, I18n.format("gui.done")));
        buttonList.add(new GuiButton(-2, x2, height - 27, 150, 20, I18n.format("gui.cancel")));

        List<GuiButton> buttons = initButtons();

        int y = 25;
        int lastId = 0;

        for (GuiButton button : buttons) {
            boolean first = button.id % 2 != 0;
            boolean large = button.id - 1 != lastId;
            button.xPosition = (first || large) ? x1 : x2;
            button.yPosition = y;
            button.width = large ? 310 : 150;
            button.height = 20;
            buttonList.add(button);
            if ((!first) || (large)) y += 25;
            lastId = button.id;
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        if (mouseY < fontRendererObj.FONT_HEIGHT + 14) {
            if (Mouse.isButtonDown(1)) {
                drawCenteredString(fontRendererObj, "This is not a button", this.width / 2, 7, 0xC0C0C0);
            } else {
                drawCenteredString(fontRendererObj, "Hold right-click on a button for information", this.width / 2, 7, 0xC0C0C0);
            }
        } else {
            drawCenteredString(fontRendererObj, "BetterFps Options", this.width / 2, 7, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (Mouse.isButtonDown(1)) { // Right Click
            for (GuiButton button : buttonList) {
                if ((button instanceof GuiCycleButton) && (button.isMouseOver())) {
                    int y = mouseY + 5;

                    String[] help = ((GuiCycleButton) button).getHelpText();
                    int fontHeight = fontRendererObj.FONT_HEIGHT, i = 0;
                    drawGradientRect(0, y, mc.displayWidth, y + (fontHeight * help.length) + 10, -1072689136, -804253680);
                    for (String h : help) {
                        if (!h.isEmpty()) fontRendererObj.drawString(h, 5, y + (i * fontHeight) + 5, 0xFFFFFF);
                        i++;
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button instanceof GuiCycleButton) {
            ((GuiCycleButton) button).actionPerformed();
        } else if (button.id == -1) {
            // Save
            BetterFpsConfig config = BetterFpsConfig.getConfig();

            GuiCycleButton algorithmButton = getCycleButton(2);
            config.algorithm = algorithmButton.getSelectedValue();

            GuiCycleButton boxRenderButton = getCycleButton(3);
            config.fastBoxRender = boxRenderButton.getSelectedValue();

            GuiCycleButton fogButton = getCycleButton(4);
            config.fog = fogButton.getSelectedValue();

            GuiCycleButton hopperButton = getCycleButton(5);
            config.fastHopper = hopperButton.getSelectedValue();

            BetterFpsHelper.saveConfig();
            mc.displayGuiScreen(parent);
        } else if (button.id == -2) {
            mc.displayGuiScreen(parent);
        }
    }

    private GuiCycleButton getCycleButton(int id) {
        for (GuiButton button : buttonList) {
            if (button.id == id) {
                return (GuiCycleButton) button;
            }
        }
        return null;
    }

    private static class AlgorithmButton extends GuiCycleButton {
        Process process = null;

        public <T> AlgorithmButton(int buttonId, String title, HashMap<T, String> values, T defaultValue, String[] helpLines) {
            super(buttonId, title, values, defaultValue, helpLines);
        }

        private boolean isRunning() {
            try {
                process.exitValue();
                return false;
            } catch (Exception ex) {
                return true;
            }
        }

        private void updateAlgorithm() {
            if ((process != null) && (!isRunning())) {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (BetterFpsHelper.helpers.containsKey(line)) {
                            for (int i = 0; i < keys.size(); i++) {
                                if (keys.get(i).equals(line)) {
                                    key = i;
                                    updateTitle();
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                }
                process = null;
            }
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            updateAlgorithm();
            super.drawButton(mc, mouseX, mouseY);
        }
    }
}
