package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoItemHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class InventoryBlockEntity extends AutoBlockEntity implements MenuProvider, Container {

    protected final LazyOptional<AutoItemHandler> itemHandler;

    public InventoryBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pState) {
        super(pType, pPos, pState);

        this.itemHandler = LazyOptional.of(this::createItemHandler);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) return this.getInventoryCap(cap, side);

        return LazyOptional.empty();
    }

    protected <T> LazyOptional<T> getInventoryCap(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.itemHandler.cast();
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

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return itemHandler.lazyMap(i -> i.getStackInSlot(pSlot)).orElseThrow(IllegalStateException::new);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        itemHandler.orElseThrow(IllegalStateException::new).setStackInSlot(pSlot, pStack);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        assert this.level != null;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D,
                    (double)this.worldPosition.getY() + 0.5D,
                    (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Inserts item, ignoring the filter
     * @param slot slot
     * @param itemStack item
     * @param simulate simulate
     * @return ItemStack remaining
     */
    protected final ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
        if (!simulate) this.update(); // Update is deferred until end of tick
        return itemHandler.orElseThrow(IllegalStateException::new).insertItem(slot, itemStack, simulate);
    }

    protected final ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!simulate) this.update();
        return itemHandler.orElseThrow(IllegalStateException::new).extractItem(slot, amount, simulate);
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        return this.extractItem(pSlot, pAmount, false);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        AutoItemHandler handler = itemHandler.orElseThrow(IllegalStateException::new);
        return handler.extractItem(pSlot, handler.getSlotLimit(pSlot), false);
    }

    @Override
    public boolean canPlaceItem(int pIndex, @NotNull ItemStack pStack) {
        return itemHandler.orElseThrow(IllegalStateException::new).isItemValid(pIndex, pStack);

    }

    public abstract AutoItemHandler createItemHandler();

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty()) return false;
        }

        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }
    }
}
