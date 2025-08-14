package dev.eatgrapes.keepwins.ui.gui.clickgui.api;

import dev.eatgrapes.keepwins.util.render.nano.NanoUtil;
import dev.eatgrapes.keepwins.ui.api.Icon;

import java.awt.*;

import static dev.eatgrapes.keepwins.util.render.nano.NanoLoader.vg;
import static org.lwjgl.nanovg.NanoVG.*;

public class ButtonAPI {
    private float x, y, width, height;
    private float cornerRadius;
    private String text;
    private String iconPath;
    private Color backgroundColor;
    private Color hoverColor;
    private Color borderColor;
    private Color textColor;
    private Color selectedColor;
    private Color selectedBorderColor;
    private Runnable onClick;
    private boolean hovered;
    private boolean selected;
    
    public ButtonAPI(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = 5f;
        this.text = "";
        this.iconPath = null;
        this.backgroundColor = new Color(240, 240, 240);
        this.hoverColor = new Color(200, 200, 200);
        this.borderColor = new Color(200, 200, 200);
        this.textColor = Color.BLACK;
        this.selectedColor = new Color(200, 200, 200);
        this.selectedBorderColor = new Color(180, 180, 180);
        this.hovered = false;
        this.selected = false;
    }
    
    public ButtonAPI(float x, float y, float width, float height, String text) {
        this(x, y, width, height);
        this.text = text;
    }
    
    public ButtonAPI(float x, float y, float width, float height, String text, Runnable onClick) {
        this(x, y, width, height, text);
        this.onClick = onClick;
    }
    
    public void render() {
        // 绘制圆角按钮
        NanoUtil.beginPath();
        nvgRoundedRect(vg, x, y, width, height, cornerRadius);
        
        // 根据状态设置颜色（选中 > 悬停 > 默认）
        if (selected) {
            NanoUtil.fillColor(selectedColor);
        } else if (hovered) {
            NanoUtil.fillColor(hoverColor);
        } else {
            NanoUtil.fillColor(backgroundColor);
        }
        nvgFill(vg);
        
        // 绘制按钮边框
        NanoUtil.beginPath();
        nvgRoundedRect(vg, x, y, width, height, cornerRadius);
        if (selected) {
            NanoUtil.strokeColor(selectedBorderColor);
        } else {
            NanoUtil.strokeColor(borderColor);
        }
        nvgStrokeWidth(vg, 1f);
        nvgStroke(vg);
        
        // 绘制图标（如果设置了图标路径）
        if (iconPath != null && !iconPath.isEmpty()) {
            float iconSize = Math.min(width, height) * 0.6f;
            float iconX = x + (width - iconSize) / 2;
            float iconY = y + (height - iconSize) / 2;
            
            if (text != null && !text.isEmpty()) {
                // 如果同时有文本和图标，将图标放在左侧
                iconX = x + 5;
                iconY = y + (height - iconSize) / 2;
                Icon.renderIcon(iconPath, iconX, iconY, iconSize, iconSize);
                
                // 文本放在图标右侧
                // 这里可以添加文本渲染逻辑
            } else {
                // 只有图标，居中显示
                Icon.renderIcon(iconPath, iconX, iconY, iconSize, iconSize);
            }
        }
        
        // 绘制文本（如果设置了文本）
        if (text != null && !text.isEmpty()) {
            // 文本渲染逻辑可以在这里添加
        }
    }
    
    public void onMouseMove(float mouseX, float mouseY) {
        hovered = (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
    }
    
    public void onMouseClick(float mouseX, float mouseY, int button) {
        if (button == 0 && hovered && onClick != null) { // 左键点击
            onClick.run();
        }
    }
    
    // Getters and Setters
    
    public float getX() {
        return x;
    }
    
    public ButtonAPI setX(float x) {
        this.x = x;
        return this;
    }
    
    public float getY() {
        return y;
    }
    
    public ButtonAPI setY(float y) {
        this.y = y;
        return this;
    }
    
    public float getWidth() {
        return width;
    }
    
    public ButtonAPI setWidth(float width) {
        this.width = width;
        return this;
    }
    
    public float getHeight() {
        return height;
    }
    
    public ButtonAPI setHeight(float height) {
        this.height = height;
        return this;
    }
    
    public float getCornerRadius() {
        return cornerRadius;
    }
    
    public ButtonAPI setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }
    
    public String getText() {
        return text;
    }
    
    public ButtonAPI setText(String text) {
        this.text = text;
        return this;
    }
    
    public String getIconPath() {
        return iconPath;
    }
    
    public ButtonAPI setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public ButtonAPI setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
    
    public Color getHoverColor() {
        return hoverColor;
    }
    
    public ButtonAPI setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
        return this;
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
    public ButtonAPI setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }
    
    public Color getTextColor() {
        return textColor;
    }
    
    public ButtonAPI setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }
    
    public Color getSelectedColor() {
        return selectedColor;
    }
    
    public ButtonAPI setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }
    
    public Color getSelectedBorderColor() {
        return selectedBorderColor;
    }
    
    public ButtonAPI setSelectedBorderColor(Color selectedBorderColor) {
        this.selectedBorderColor = selectedBorderColor;
        return this;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public ButtonAPI setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
    
    public Runnable getOnClick() {
        return onClick;
    }
    
    public ButtonAPI setOnClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }
    
    public boolean isHovered() {
        return hovered;
    }
}