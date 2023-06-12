package org._9636dev.autovanilla.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org._9636dev.autovanilla.client.screen.ScreenUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Textured button, acts like a Vanilla Button Widget, but is a custom texture
 */
@SuppressWarnings("unused")
public class TexturedButton extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {

    public interface OnClick {
        /**
         * Called when button is clicked
         * @param button TextureButton instance
         * @param mouseButton mouse button that was clicked
         */
        void onClick(TexturedButton button, int mouseButton);
    }

    private final int posX, posY, width, height;
    private Texture texture;
    private final OnClick onClick;
    private boolean visible;

    /**
     * @param posX Left x coordinate for button
     * @param posY Top y coordinate for button
     * @param width width in pixels
     * @param height height in pixels
     * @param texture texture instance for button texture
     * @param onClick interface called when clicked
     */
    public TexturedButton(int posX, int posY, int width, int height, Texture texture, OnClick onClick) {
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.onClick = onClick;

        this.visible = true;
    }

    /**
     * Changes the visibility of the button
     * @param visible whether the button will be rendered
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (!visible) return; // Only render if button is visible

        this.texture.load();
        this.blit(pPoseStack, this.posX, this.posY, this.texture.x(), this.texture.y(), this.width, this.height);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!visible) return false; // Don't detect mouse clicks if not visible

        if (ScreenUtil.isPointInRect((int) pMouseX, (int) pMouseY, this.posX, this.posX + this.width,
                this.posY,this.posY + this.height)) {
            this.onClick.onClick(this, pButton);
            return true;
        }

        return false;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {}

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
