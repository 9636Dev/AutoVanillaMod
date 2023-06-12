package org._9636dev.autovanilla.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org._9636dev.autovanilla.client.screen.ScreenUtil;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * GUI of a progress bar, automatically maps number from minimum range to maximum range
 */
public class ProgressBar extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    /**
     * Direction of ProgressBar, enum name describes the start to end of the progress direction
     */
    public enum ProgressDirection {
        /**
         * The progress bar will start from the left of the texture and render more of the right as the value increases.
         */
        LEFT_TO_RIGHT((x, mapped, w) -> x, (y, mapped, h) -> y, (w, mapped) -> mapped, (h, mapped) -> h),

        /**
         * The progress bar will start rendering on the right to the left, the texture isn't shifted,
         * it starts the rendering from the furthest left pixel.
         */
        RIGHT_TO_LEFT((x, mapped, w) -> x + w - mapped, (y, mapped, h) -> y, (w, mapped) -> mapped, (h, mapped) -> h),

        /**
         * The texture starts rendering at the top, increasing in width as the value increases.
         */
        TOP_TO_BOTTOM((x, mapped, w) -> x, (y, mapped, h) -> y, (w, mapped) -> w, (h, mapped) -> mapped),

        /**
         * The texture starts rendering at the top, starting the blit at the highest pixel.
         * Does not shift the texture while rendering
         */
        BOTTOM_TO_TOP((x, mapped, w) -> x, (y, mapped, h) -> y + h - mapped, (w, mapped) -> w, (h, mapped) -> mapped);

        /**
         * Function to map the actual value of X, takes in parameters (in this order): starting X, mapped number, width
         * @returns Starting X pixel for blit
         */
        private final TriFunction<Integer, Integer, Integer, Integer> xMapFunction;
        /**
         * Function to map the actual value of Y, takes in parameters (in this order): starting Y, mapped number, height.
         * @returns Starting Y pixel for blit
         */
        private final TriFunction<Integer, Integer, Integer, Integer> yMapFunction;
        /**
         * Function to map the width of the progress bar. Takes in the parameters (in this order): width, mapped
         * @returns width of the progress bar
         */
        private final BiFunction<Integer, Integer, Integer> widthMapFunction;
        /**
         * Function to map the width of the progress bar. Takes in the parameters (in this order): height, mapped
         * @returns height of the progress bar
         */
        private final BiFunction<Integer, Integer, Integer> heightMapFunction;

        /**
         * Internal enum constructor for ProgressDirection
         * @param pXMapFunction Function for mapping X
         * @param pYMapFunction Function for mapping Y
         * @param pWidthMapFunction Function for mapping Width
         * @param pHeightMapFunction Function for mapping Height
         */
        ProgressDirection(TriFunction<Integer, Integer, Integer, Integer> pXMapFunction, TriFunction<Integer, Integer, Integer, Integer> pYMapFunction,
                          BiFunction<Integer, Integer, Integer> pWidthMapFunction, BiFunction<Integer, Integer, Integer> pHeightMapFunction) {
            this.xMapFunction = pXMapFunction;
            this.yMapFunction = pYMapFunction;
            this.widthMapFunction = pWidthMapFunction;
            this.heightMapFunction = pHeightMapFunction;
        }
    }

    protected final int xPos, yPos, minWidth, minHeight, maxWidth, maxHeight;
    private final IntSupplier minXProgress, maxXProgress, minYProgress, maxYProgress;
    private final ProgressDirection direction;
    private final Supplier<Integer> progress;
    private final Texture texture;

    /**
     *
     * @param pXPos X Position of the most left point
     * @param pYPos Y position of the highest point
     * @param pMinWidth Minimum width of ProgressBar
     * @param pMinHeight Minimum height of ProgressBar
     * @param pMaxWidth Maximum width of ProgressBar
     * @param pMaxHeight Maximum height of ProgressBar
     * @param pMinXProgress Supplier for minimum Progress for X
     * @param pMaxXProgress Supplier for maximum Progress for X
     * @param pMinYProgress Supplier for minimum Progress for Y
     * @param pMaxYProgress Supplier for maximum Progress for Y
     * @param pDirection Direction of the ProgressBar
     * @param texture Texture coordinates and atlas of the ProgressBar
     * @param progress Supplier for the progress of the progress bar
     */
    public ProgressBar(int pXPos, int pYPos, int pMinWidth, int pMinHeight, int pMaxWidth, int pMaxHeight,
                       IntSupplier pMinXProgress, IntSupplier pMaxXProgress, IntSupplier pMinYProgress, IntSupplier pMaxYProgress,
                       ProgressDirection pDirection, Texture texture, Supplier<Integer> progress) {
        this.xPos = pXPos;
        this.yPos = pYPos;
        this.minWidth = pMinWidth;
        this.minHeight = pMinHeight;
        this.maxWidth = pMaxWidth;
        this.maxHeight = pMaxHeight;
        this.direction = pDirection;
        this.minXProgress = pMinXProgress;
        this.maxXProgress = pMaxXProgress;
        this.minYProgress = pMinYProgress;
        this.maxYProgress = pMaxYProgress;
        this.texture = texture;
        this.progress = progress;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // If any of the max progress is -1, render alternative texture
        if (this.maxXProgress.getAsInt() == -1 || this.maxYProgress.getAsInt() == -1) {
            this.renderAlternative(pPoseStack, pMouseX, pMouseY, pPartialTick);
            return;
        }

        // Sets the texture slot of needed
        this.texture.load();
        int progress = this.progress.get();

        int xMapped = ScreenUtil.mapNumberToRange(this.minXProgress.getAsInt(),
                this.maxXProgress.getAsInt(), this.minWidth, this.maxWidth, progress);
        int yMapped = ScreenUtil.mapNumberToRange(this.minYProgress.getAsInt(),
                this.maxYProgress.getAsInt(), this.minHeight, this.maxHeight, progress);

        this.blit(pPoseStack,
                direction.xMapFunction.apply(this.xPos, xMapped, this.maxWidth),
                direction.yMapFunction.apply(this.yPos, yMapped, this.maxHeight),
                direction.xMapFunction.apply(this.texture.x(), xMapped, this.maxWidth),
                direction.yMapFunction.apply(this.texture.y(), yMapped, this.maxHeight),
                direction.widthMapFunction.apply(this.maxWidth, xMapped),
                direction.heightMapFunction.apply(this.maxHeight, yMapped)
        );
    }

    /**
     * Alternative render for the ProgressBar if maximum is -1
     * @param pPoseStack PoseStack used for rendering by Mojang
     * @param pMouseX Mouse coordinate X
     * @param pMouseY Mouse coordinate Y
     * @param pPartialTicks partial tick in the current GT
     */
    @SuppressWarnings("unused")
    protected void renderAlternative(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {

    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {}

}
