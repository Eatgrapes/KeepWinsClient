package dev.eatgrapes.keepwins.ui.font.awt;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.Objects;


public final class FontCharacter {
    private final int texture;
    private final float width;
    private final float height;

    public FontCharacter(int texture, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public void render(float x, float y) {
        GlStateManager.bindTexture(this.texture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex2f(x, y + this.height);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex2f(x + this.width, y + this.height);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex2f(x + this.width, y);
        GL11.glEnd();
    }

    public int texture() {
        return texture;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        FontCharacter that = (FontCharacter) obj;
        return this.texture == that.texture &&
                Float.floatToIntBits(this.width) == Float.floatToIntBits(that.width) &&
                Float.floatToIntBits(this.height) == Float.floatToIntBits(that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, width, height);
    }

    @Override
    public String toString() {
        return "FontCharacter[" +
                "texture=" + texture + ", " +
                "width=" + width + ", " +
                "height=" + height + ']';
    }

}
