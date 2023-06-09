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

import java.util.List;

import static org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoSmithingTableBlockEntity.*;

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

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        // TODO: 28.05.23 Progress bar seems to be offset, implemented fix, not tested.
        this.addRenderableWidget(new ProgressBar(i + ENERGY_BAR_ONS_LEFT, j + ENERGY_BAR_ONS_TOP,
                ENERGY_BAR_WIDTH, 0, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, () -> 0, () -> 0,
                () -> 0, () -> ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_MAX_ENERGY_MSB),
                    menu.data.get(DATA_MAX_ENERGY_LSB)), ProgressBar.ProgressDirection.BOTTOM_TO_TOP,
                new Texture(TEXTURE_LOCATION, ENERGY_BAR_OFS_LEFT, ENERGY_BAR_OFS_TOP),
                () -> ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_ENERGY_MSB), menu.data.get(DATA_ENERGY_LSB))));

        this.addRenderableWidget(new ProgressBar(i + PROGRESS_BAR_ONS_LEFT, j + PROGRESS_BAR_ONS_TOP,
                0, PROGRESS_BAR_HEIGHT, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT, () -> 0,
                () -> this.menu.data.get(DATA_MAX_PROGRESS), () -> 0, () -> 0, ProgressBar.ProgressDirection.LEFT_TO_RIGHT,
                new Texture(TEXTURE_LOCATION, PROGRESS_BAR_OFS_LEFT, PROGRESS_BAR_OFS_TOP), () -> menu.data.get(DATA_PROGRESS)));
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pPoseStack);

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        System.out.println("Progress / Energy: " + menu.data.get(DATA_PROGRESS) + "/" +
                ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_ENERGY_MSB), menu.data.get(DATA_ENERGY_LSB)));

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderTooltip(@NotNull PoseStack pPoseStack, int pX, int pY) {
        super.renderTooltip(pPoseStack, pX, pY);

        int minX = this.getGuiLeft() + ENERGY_BAR_ONS_LEFT;
        int minY = this.getGuiTop() + ENERGY_BAR_ONS_TOP;
        if (ScreenUtil.isPointInRect(pX, pY, minX, minX + ENERGY_BAR_WIDTH, minY, minY + ENERGY_BAR_HEIGHT)) {
            this.renderComponentTooltip(pPoseStack,
                    List.of(Component.translatable("tooltip.autosmithingtable.energy_stored",
                            ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_ENERGY_MSB), menu.data.get(DATA_ENERGY_LSB)),
                            ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_MAX_ENERGY_MSB),
                                    menu.data.get(DATA_MAX_ENERGY_LSB)))), pX, pY);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
