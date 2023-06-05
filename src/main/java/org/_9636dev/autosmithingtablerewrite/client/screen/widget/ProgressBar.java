package org._9636dev.autosmithingtablerewrite.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org._9636dev.autosmithingtablerewrite.client.screen.ScreenUtil;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ProgressBar extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    public enum ProgressDirection {
        LEFT_TO_RIGHT((x, mapped, w) -> x, (y, mapped, h) -> y, (w, mapped) -> mapped, (h, mapped) -> h, false),
        RIGHT_TO_LEFT((x, mapped, w) -> x + w - mapped, (y, mapped, h) -> y, (w, mapped) -> mapped, (h, mapped) -> h, false),
        TOP_TO_BOTTOM((x, mapped, w) -> x, (y, mapped, h) -> y, (w, mapped) -> w, (h, mapped) -> mapped, true),
        BOTTOM_TO_TOP((x, mapped, w) -> x, (y, mapped, h) -> y + h - mapped, (w, mapped) -> w, (h, mapped) -> mapped, true);

        private final TriFunction<Integer, Integer, Integer, Integer> xMapFunction, yMapFunction;
        private final BiFunction<Integer, Integer, Integer> widthMapFunction, heightMapFunction;

        private final boolean isVertical;
        ProgressDirection(TriFunction<Integer, Integer, Integer, Integer> pXMapFunction, TriFunction<Integer, Integer, Integer, Integer> pYMapFunction,
                          BiFunction<Integer, Integer, Integer> pWidthMapFunction, BiFunction<Integer, Integer, Integer> pHeightMapFunction, boolean isVertical) {
            this.xMapFunction = pXMapFunction;
            this.yMapFunction = pYMapFunction;
            this.widthMapFunction = pWidthMapFunction;
            this.heightMapFunction = pHeightMapFunction;
            this.isVertical = isVertical;
        }
    }

    private final int xPos, yPos, minWidth, minHeight, maxWidth, maxHeight;
    private final IntSupplier minXProgress, maxXProgress, minYProgress, maxYProgress;
    private final ProgressDirection direction;
    private final Supplier<Integer> progress;
    private final Texture texture;

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
        this.texture.load();

        int progress = this.progress.get();

        // -1 as max progress makes the progress bar always at 0
        int xMapped = minWidth;
        int yMapped = minHeight;

        if (this.maxXProgress.getAsInt() != -1) xMapped = this.direction.isVertical ? this.xPos : ScreenUtil.mapNumberToRange(this.minXProgress.getAsInt(),
                this.maxXProgress.getAsInt(), this.minWidth, this.maxWidth, progress);
        if (this.maxYProgress.getAsInt() != -1) yMapped = !this.direction.isVertical ? this.yPos : ScreenUtil.mapNumberToRange(this.minYProgress.getAsInt(),
                this.maxYProgress.getAsInt(), this.minHeight, this.maxHeight, progress);

        this.blit(pPoseStack,
                direction.xMapFunction.apply(this.xPos, xMapped, this.maxWidth),
                direction.yMapFunction.apply(this.yPos, yMapped, this.maxHeight),
                this.texture.x(),
                this.texture.y(),
                direction.widthMapFunction.apply(this.maxWidth, xMapped),
                direction.heightMapFunction.apply(this.maxHeight, yMapped)
        );
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {}

}
