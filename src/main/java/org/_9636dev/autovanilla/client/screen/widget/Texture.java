package org._9636dev.autovanilla.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;

public record Texture(ResourceLocation location, int x, int y) {
    public void load() {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if (location != null) {
            RenderSystem.setShaderTexture(0, location);
        }
    }
}