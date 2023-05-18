package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoItemHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class InventoryBlockEntity extends AutoBlockEntity implements MenuProvider {

    protected final LazyOptional<AutoItemHandler> itemHandler;

    public InventoryBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pState) {
        super(pType, pPos, pState);

        this.itemHandler = LazyOptional.of(this::createItemHandler);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemHandler.cast();

        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        itemHandler.invalidate();
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        itemHandler.ifPresent((inv) -> inv.deserializeNBT(pTag.getCompound("autosmithingtable.inventory")));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        if (pTag.contains("autosmithingtable.inventory")) itemHandler.ifPresent((inv) -> pTag.put("autosmithingtable.inventory", inv.serializeNBT()));
    }

    @Override
    public @NotNull List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();

        itemHandler.ifPresent((inv) -> {
            for (int slot = 0; slot < inv.getSlots(); ++slot) drops.add(inv.getStackInSlot(slot));
        });

        return drops;
    }

    // autosmithingtable API

    protected final ItemStack getStackInSlot(int slot) {
        return itemHandler.lazyMap(i -> i.getStackInSlot(slot)).orElseThrow(IllegalStateException::new);
    }

    protected final void setStackInSlot(int slot, ItemStack itemStack) {
        itemHandler.orElseThrow(IllegalStateException::new).setStackInSlot(slot, itemStack);
    }

    protected final int getInventorySlots() {
        return itemHandler.lazyMap(ItemStackHandler::getSlots).orElseThrow(IllegalStateException::new);
    }

    protected final ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
        if (!simulate) update(); // Update is deferred until end of tick
        return itemHandler.lazyMap(i -> i.insertItem(slot, itemStack, simulate)).orElseThrow(IllegalStateException::new);
    }

    protected final ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!simulate) update();
        return itemHandler.lazyMap(i -> i.extractItem(slot, amount, simulate)).orElseThrow(IllegalStateException::new);
    }

    protected final boolean isItemValidInSlot(int slot, ItemStack itemStack) {
        return itemHandler.lazyMap(i -> i.isItemValid(slot, itemStack)).orElseThrow(IllegalStateException::new);
    }

    public abstract AutoItemHandler createItemHandler();
}
