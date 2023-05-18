package org._9636dev.autosmithingtablerewrite.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org._9636dev.autosmithingtablerewrite.common.container.AutoSmithingTableContainer;
import org.jetbrains.annotations.NotNull;

public class AutoSmithingTableScreen extends AutoScreen<AutoSmithingTableContainer> {
    public AutoSmithingTableScreen(AutoSmithingTableContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pPoseStack);
    }
}
