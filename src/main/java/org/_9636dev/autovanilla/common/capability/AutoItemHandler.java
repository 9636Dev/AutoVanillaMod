package org._9636dev.autovanilla.common.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AutoItemHandler extends ItemStackHandler {

    public AutoItemHandler(AutoItemHandler autoItemHandler) {
        this.stacks = autoItemHandler.stacks;
    }

    public AutoItemHandler(int size) {
        super(size);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(slot, stack);
    }

    public boolean slotsAreEmpty(int ... slots) {
        for (int slot : slots) {
            if (!this.getStackInSlot(slot).isEmpty()) return false;
        }
        return true;
    }

    public boolean slotsAreFull(int ... slots) {
        for (int slot : slots) {
            if (!slotIsFull(slot)) return false;
        }
        return true;
    }

    public boolean slotIsFull(int slot) {
        return this.getStackInSlot(slot).getCount() == this.getSlotLimit(slot);
    }
}
