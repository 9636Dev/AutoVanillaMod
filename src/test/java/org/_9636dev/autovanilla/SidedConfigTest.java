package org._9636dev.autovanilla;

import net.minecraft.core.Direction;
import org._9636dev.autovanilla.common.capability.SidedConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SidedConfigTest {
    @Test
    public void testSidedConfig() {
        SidedConfig sidedConfig = new SidedConfig();
        Assertions.assertArrayEquals(new SidedConfig.Side[] {
                SidedConfig.Side.NONE, SidedConfig.Side.NONE, SidedConfig.Side.NONE,
                SidedConfig.Side.NONE, SidedConfig.Side.NONE, SidedConfig.Side.NONE,
        }, sidedConfig.getSides());

        sidedConfig.setSide(Direction.UP, SidedConfig.Side.INPUT_2_OUTPUT_1);
        Assertions.assertArrayEquals(new SidedConfig.Side[] {
                SidedConfig.Side.NONE, SidedConfig.Side.NONE, SidedConfig.Side.NONE,
                SidedConfig.Side.NONE, SidedConfig.Side.INPUT_2_OUTPUT_1, SidedConfig.Side.NONE,
        }, sidedConfig.getSides());

        for (Direction direction : Direction.values()) {
            for (SidedConfig.Side side : SidedConfig.Side.values()) {
                sidedConfig.setSide(direction, side);
                Assertions.assertEquals(side, sidedConfig.getSide(direction));
            }
        }
    }
}
