package org._9636dev.autosmithingtablerewrite.common.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AutoItemHandler extends ItemStackHandler {
    protected ItemInputFilter inputFilter;
    protected ItemOutputFilter outputFilter;

    public AutoItemHandler(int size) {
        super(size);

        this.inputFilter = ItemInputFilter.ALLOW_ALL;
        this.outputFilter = ItemOutputFilter.ALLOW_ALL;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (inputFilter.isValid(this, stack)) return super.insertItem(slot, stack, simulate);
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (outputFilter.isValid(this, slot)) return super.extractItem(slot, amount, simulate);
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (inputFilter.isValid(this, stack)) return super.isItemValid(slot, stack);
        return false;
    }

    @NotNull
    public ItemStack insertItemBypassFilter(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @NotNull
    public ItemStack extractItemBypassFilter(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public boolean isItemValidBypassFilter(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(slot, stack);
    }

    public void setInputFilter(ItemInputFilter inputFilter) {
        this.inputFilter = inputFilter;
    }

    public void setOutputFilter(ItemOutputFilter outputFilter) {
        this.outputFilter = outputFilter;
    }

    public ItemInputFilter getInputFilter() {
        return inputFilter;
    }

    public ItemOutputFilter getOutputFilter() {
        return outputFilter;
    }
}
