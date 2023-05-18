package org._9636dev.autosmithingtablerewrite.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org._9636dev.autosmithingtablerewrite.common.container.AutoContainer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class AutoScreen<T extends AutoContainer> extends AbstractContainerScreen<T> {
    public AutoScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected abstract void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY);

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}
