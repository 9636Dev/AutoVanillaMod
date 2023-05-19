package org._9636dev.autosmithingtablerewrite.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org._9636dev.autosmithingtablerewrite.AutoSmithingTableMod;
import org._9636dev.autosmithingtablerewrite.client.screen.widget.ProgressBar;
import org._9636dev.autosmithingtablerewrite.client.screen.widget.Texture;
import org._9636dev.autosmithingtablerewrite.common.container.AutoSmithingTableContainer;
import org.jetbrains.annotations.NotNull;

public class AutoSmithingTableScreen extends AutoScreen<AutoSmithingTableContainer> {

    private static final int PROGRESS_BAR_ONS_LEFT = 100;
    private static final int PROGRESS_BAR_ONS_TOP = 46;
    private static final int PROGRESS_BAR_OFS_LEFT = 177;
    private static final int PROGRESS_BAR_OFS_TOP = 1;
    private static final int ENERGY_BAR_ONS_LEFT = 164;
    private static final int ENERGY_BAR_ONS_TOP = 8;
    private static final int ENERGY_BAR_OFS_LEFT = 177;
    private static final int ENERGY_BAR_OFS_TOP = 22;

    public static final int PROGRESS_BAR_WIDTH = 27;
    public static final int PROGRESS_BAR_HEIGHT = 20;
    private static final int ENERGY_BAR_WIDTH = 4;
    private static final int ENERGY_BAR_HEIGHT = 71;


    public static final ResourceLocation TEXTURE_LOCATION =
            new ResourceLocation(AutoSmithingTableMod.MODID, "textures/gui/auto_smithing_table.png");

    public AutoSmithingTableScreen(AutoSmithingTableContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();

        // TODO: 19.05.23 Make 100_000 not constant
        this.addRenderableWidget(new ProgressBar(ENERGY_BAR_ONS_LEFT, ENERGY_BAR_ONS_TOP,
                ENERGY_BAR_WIDTH, 0, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, 0, 0,
                0, 100_000, ProgressBar.ProgressDirection.BOTTOM_TO_TOP,
                new Texture(TEXTURE_LOCATION, ENERGY_BAR_OFS_LEFT, ENERGY_BAR_OFS_TOP), () -> menu.data.get(2)));
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pPoseStack);

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        System.out.println("Progress / Energy: " + menu.data.get(2) + "/" +
                ScreenUtil.convertNumberFromTruncated(menu.data.get(0), menu.data.get(1)));

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
