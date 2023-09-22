package org._9636dev.autovanilla.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import org._9636dev.autovanilla.client.screen.ScreenUtil;
import org._9636dev.autovanilla.common.capability.SidedConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class SideConfigMenu extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
    public record TextureSide(int x, int y, int width, int height) {
        public int minX() {
            return x;
        }

        public int maxX() {
            return x + width;
        }

        public int minY() {
            return y;
        }

        public int maxY() {
            return y + height;
        }
    }

    private final Texture texture;
    private final int xPos, yPos, width, height;
    private final List<TextureSide> textureSides;
    private final Map<SidedConfig.Side, TextureSide> offscreenSides;
    private final List<SidedConfig.Side> availableSides;
    private final Direction front;

    private final SidedConfig sidedConfig;

    public SideConfigMenu(int pX, int pY, int pWidth, int pHeight, Texture texture, List<TextureSide> textureSides,
                          Map<SidedConfig.Side, TextureSide> offscreenSides, SidedConfig sidedConfig,
                          List<SidedConfig.Side> availableSides, Direction front) {
        this.xPos = pX;
        this.yPos = pY;
        this.width = pWidth;
        this.height = pHeight;
        this.texture = texture;
        this.textureSides = textureSides;
        this.offscreenSides = offscreenSides;
        this.sidedConfig = sidedConfig;
        this.availableSides = availableSides;
        this.front = front;
    }


    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.texture.load();
        this.blit(pPoseStack, this.xPos, this.yPos, this.width, this.height, this.texture.x(), this.texture.y());

        // TODO: 6/12/2023 Render background and render texture in each slot
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton != 1 && pButton != 2) return false;
        assert Minecraft.getInstance().player != null;
        boolean isShifted = Minecraft.getInstance().player.isShiftKeyDown();

        TextureSide ts;
        for (int i = 0; i < 6;i++) {
            ts = this.textureSides.get(i);
            if (ScreenUtil.isPointInRect((int) pMouseX, (int) pMouseY, ts.minX(), ts.maxX(), ts.minY(), ts.maxY())) {
                if (isShifted) this.sidedConfig.setSide(this.getRelativeSide(i), SidedConfig.Side.NONE);
                else if (pButton == 1) System.out.println(); // TODO: 6/12/2023 Increase by 1 or shift
                else System.out.println(); // TODO: 6/12/2023 Decrease by 1 or shift
                return true;
            }
        }

        return false;
    }

    private Direction getRelativeSide(int i) {
        return switch (i) {
            case 0 -> Direction.UP;
            case 1 -> this.front.getClockWise();
            case 2 -> this.front;
            case 3 -> this.front.getCounterClockWise();
            case 4 -> Direction.DOWN;
            case 5 -> this.front.getOpposite();
            default -> throw new IllegalArgumentException("Relative side number should be in range 0-5");
        };
    }
}
