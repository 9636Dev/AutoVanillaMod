package org._9636dev.autovanilla.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org._9636dev.autovanilla.common.container.AutoContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Base screen used by AutoVanilla
 * @param <T> AutoContainer type
 */
@SuppressWarnings("unused")
public abstract class AutoScreen<T extends AutoContainer> extends AbstractContainerScreen<T> {

    /**
     * Default minecraft screen constructor
     * @param pMenu container instance
     * @param pPlayerInventory player inventory
     * @param pTitle screen title
     */
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

    /**
     * Should be called by the screen to send clicks to the server
     * @param id button id that was clicked
     */
    protected void clickInventoryButton(int id) {
        assert this.minecraft != null;
        assert this.minecraft.player != null;
        if (this.menu.clickMenuButton(this.minecraft.player, id)) {
            assert this.minecraft.gameMode != null;
            this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, id);
        }
    }
}
