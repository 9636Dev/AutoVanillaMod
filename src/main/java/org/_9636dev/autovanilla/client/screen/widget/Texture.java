package org._9636dev.autovanilla.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;

/**
 * Texture used by textured widgets for rendering
 * @param location ResourceLocation of widget image
 * @param x x (most left) coordinate of widget
 * @param y y (highest) coordinate of widget
 */
public record Texture(ResourceLocation location, int x, int y) {
    /**
     * Will be called when needing to render texture
     */
    public void load() {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (location != null) {
            RenderSystem.setShaderTexture(0, location);
        }
    }
}