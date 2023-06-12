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
public abstract class SideConfigMenu extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {
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
    private final Map<Direction, TextureSide> textureSides;
    private final Map<SidedConfig.Side, TextureSide> offscreenSides;
    private final List<SidedConfig.Side> availableSides;

    private final SidedConfig sidedConfig;

    public SideConfigMenu(int pX, int pY, int pWidth, int pHeight, Texture texture, Map<Direction, TextureSide> textureSides,
                          Map<SidedConfig.Side, TextureSide> offscreenSides, SidedConfig sidedConfig, List<SidedConfig.Side> availableSides) {
        this.xPos = pX;
        this.yPos = pY;
        this.width = pWidth;
        this.height = pHeight;
        this.texture = texture;
        this.textureSides = textureSides;
        this.offscreenSides = offscreenSides;
        this.sidedConfig = sidedConfig;
        this.availableSides = availableSides;
    }

    public SidedConfig getSidedConfig() {
        return sidedConfig;
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
        // TODO: 6/12/2023 Render background and render texture in each slot
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton != 1 && pButton != 2) return false;
        assert Minecraft.getInstance().player != null;
        boolean isShifted = Minecraft.getInstance().player.isShiftKeyDown();

        TextureSide ts;
        for (Map.Entry<Direction, TextureSide> side : this.textureSides.entrySet()) {
            ts = side.getValue();
            if (ScreenUtil.isPointInRect((int) pMouseX, (int) pMouseY, ts.minX(), ts.maxX(), ts.minY(), ts.maxY())) {
                if (isShifted) this.sidedConfig.setSide(side.getKey(), SidedConfig.Side.NONE);
                else if (pButton == 1) System.out.println(); // TODO: 6/12/2023 Increase by 1 or shift
                else System.out.println(); // TODO: 6/12/2023 Decrease by 1 or shift
                return true;
            }
        }

        return false;
    }
}
