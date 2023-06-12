package org._9636dev.autovanilla.common.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * General purpose output slot implementation, that disallows stacks to be placed
 */
public class OutputSlot extends Slot {

    public OutputSlot(Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
