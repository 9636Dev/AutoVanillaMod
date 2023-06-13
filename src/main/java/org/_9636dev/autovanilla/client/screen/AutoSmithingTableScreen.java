package org._9636dev.autovanilla.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org._9636dev.autovanilla.AutoVanilla;
import org._9636dev.autovanilla.client.screen.widget.ProgressBar;
import org._9636dev.autovanilla.client.screen.widget.SideConfigMenu;
import org._9636dev.autovanilla.client.screen.widget.Texture;
import org._9636dev.autovanilla.common.capability.SidedConfig;
import org._9636dev.autovanilla.common.container.AutoSmithingTableContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static org._9636dev.autovanilla.common.blockenttiy.AutoSmithingTableBlockEntity.*;

public class AutoSmithingTableScreen extends AutoScreen<AutoSmithingTableContainer> {

    private static final int PROGRESS_BAR_ONS_LEFT = 100;
    private static final int PROGRESS_BAR_ONS_TOP = 46;
    private static final int PROGRESS_BAR_OFS_LEFT = 177;
    private static final int PROGRESS_BAR_OFS_TOP = 1;
    private static final int ENERGY_BAR_ONS_LEFT = 157;
    private static final int ENERGY_BAR_ONS_TOP = 7;
    private static final int ENERGY_BAR_OFS_LEFT = 176;
    private static final int ENERGY_BAR_OFS_TOP = 44;

    public static final int PROGRESS_BAR_WIDTH = 27;
    public static final int PROGRESS_BAR_HEIGHT = 20;
    private static final int ENERGY_BAR_WIDTH = 9;
    private static final int ENERGY_BAR_HEIGHT = 71;
    private static final int NO_PROGRESS_OFS_LEFT = 177;
    private static final int NO_PROGRESS_OFS_TOP = 23;

    private static final int SIDE_CONFIG_OFS_LEFT = 6;
    private static final int SIDE_CONFIG_OFS_TOP = 9;
    private static final int SIDE_CONFIG_WIDTH = 71;
    private static final int SIDE_CONFIG_HEIGHT = 71;
    private static final int SIDE_CONFIG_ONS_LEFT = -SIDE_CONFIG_WIDTH - 10;
    private static final int SIDE_CONFIG_ONS_TOP = 20;

    public static final ResourceLocation TEXTURE_LOCATION =
            new ResourceLocation(AutoVanilla.MODID, "textures/gui/auto_smithing_table.png");
    // Directions are Up, Left, Front, Right, Down, Back
    private static final List<SideConfigMenu.TextureSide> TEXTURE_SIDES = List.of(
            new SideConfigMenu.TextureSide(33, 18, 17, 17),
            new SideConfigMenu.TextureSide(15, 36, 17, 17),
            new SideConfigMenu.TextureSide(33, 36, 17, 17),
            new SideConfigMenu.TextureSide(51, 36, 17, 17),
            new SideConfigMenu.TextureSide(33, 54, 17, 17),
            new SideConfigMenu.TextureSide(51, 54, 17, 17)
    );
    private static final List<SidedConfig.Side> AVAILABLE_SIDES = List.of(
            SidedConfig.Side.NONE, SidedConfig.Side.INPUT_1, SidedConfig.Side.INPUT_2,
            SidedConfig.Side.INPUT_3, SidedConfig.Side.OUTPUT_1
    );

    private static final HashMap<SidedConfig.Side, SideConfigMenu.TextureSide> OFFSCREEN_SIDES;
    static {
        OFFSCREEN_SIDES = new HashMap<>();
        OFFSCREEN_SIDES.put(SidedConfig.Side.INPUT_1, new SideConfigMenu.TextureSide(79, 41, 17, 17));
        OFFSCREEN_SIDES.put(SidedConfig.Side.INPUT_2, new SideConfigMenu.TextureSide(98, 41, 17, 17));
        OFFSCREEN_SIDES.put(SidedConfig.Side.INPUT_3, new SideConfigMenu.TextureSide(79, 60, 17, 17));
        OFFSCREEN_SIDES.put(SidedConfig.Side.OUTPUT_1, new SideConfigMenu.TextureSide(98, 60, 17, 17));
    }

    private final SideConfigMenu sideConfigMenu;

    public AutoSmithingTableScreen(AutoSmithingTableContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        // TODO: 6/13/2023 Get sided config from server
        this.sideConfigMenu = new SideConfigMenu(SIDE_CONFIG_ONS_LEFT, SIDE_CONFIG_ONS_TOP, SIDE_CONFIG_WIDTH, SIDE_CONFIG_HEIGHT,
                new Texture(WIDGETS, SIDE_CONFIG_OFS_LEFT, SIDE_CONFIG_OFS_TOP), TEXTURE_SIDES, OFFSCREEN_SIDES,
                new SidedConfig(), AVAILABLE_SIDES,Direction.from2DDataValue(this.menu.data.get(DATA_DIRECTION)));
    }

    @Override
    protected void init() {
        super.init();

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        /*
            Creates a ProgressBar, and provides it with information about the energy, and max energy of the block
            The UpdateNarration and NarrationPriority functions are overridden to allow narrations of the energy
         */
        this.addRenderableWidget(new ProgressBar(i + ENERGY_BAR_ONS_LEFT, j + ENERGY_BAR_ONS_TOP,
                ENERGY_BAR_WIDTH, 0, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, () -> 0, () -> 0,
                () -> 0, () -> ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_MAX_ENERGY_MSB),
                    menu.data.get(DATA_MAX_ENERGY_LSB)), ProgressBar.ProgressDirection.BOTTOM_TO_TOP,
                new Texture(TEXTURE_LOCATION, ENERGY_BAR_OFS_LEFT, ENERGY_BAR_OFS_TOP),
                () -> ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_ENERGY_MSB), menu.data.get(DATA_ENERGY_LSB))) {

            @Override
            public @NotNull NarrationPriority narrationPriority() {
                return NarrationPriority.HOVERED;
            }

            @Override
            public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
                pNarrationElementOutput.add(NarratedElementType.HINT,
                        Component.translatable("narration.autovanilla.energy_bar",
                                ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_ENERGY_MSB), menu.data.get(DATA_ENERGY_LSB)),
                                ScreenUtil.convertNumberFromTruncated(menu.data.get(DATA_MAX_ENERGY_MSB),
                                        menu.data.get(DATA_MAX_ENERGY_LSB))));
            }
        });

        /*
         * Creates a progress bar, render an arrow with an X for alternate,
         * as the only reason for alternate render is if no recipe is present
         */
        this.addRenderableWidget(new ProgressBar(i + PROGRESS_BAR_ONS_LEFT, j + PROGRESS_BAR_ONS_TOP,
                0, PROGRESS_BAR_HEIGHT, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT, () -> 0,
                () -> this.menu.data.get(DATA_MAX_PROGRESS), () -> 0, () -> 0, ProgressBar.ProgressDirection.LEFT_TO_RIGHT,
                new Texture(TEXTURE_LOCATION, PROGRESS_BAR_OFS_LEFT, PROGRESS_BAR_OFS_TOP), () -> menu.data.get(DATA_PROGRESS)) {
            @Override
            protected void renderAlternative(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
                this.blit(pPoseStack, this.xPos, this.yPos, NO_PROGRESS_OFS_LEFT,
                        NO_PROGRESS_OFS_TOP, this.maxWidth, this.maxHeight);
            }
        });
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pPoseStack);

        int i = this.getGuiLeft();
        int j = this.getGuiTop();

        // Blit the background GUI
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
            // Mouse hover point is in rect of energy bar, display number tooltip
            this.renderComponentTooltip(pPoseStack,
                    List.of(Component.translatable("tooltip.autovanilla.energy_stored",
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
