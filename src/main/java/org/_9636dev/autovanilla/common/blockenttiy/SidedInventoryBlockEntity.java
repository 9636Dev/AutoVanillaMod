package org._9636dev.autovanilla.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class SidedInventoryBlockEntity extends AutoBlockEntity implements MenuProvider, Container, WorldlyContainer {

    protected final NonNullList<ItemStack> items;

    public SidedInventoryBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pState) {
        super(pType, pPos, pState);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public ContainerData createContainerData() {
        return null;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ITEM_HANDLER) return LazyOptional.of(() -> new SidedInvWrapper(this, side)).cast();

        return LazyOptional.empty();
    }


    @Override
    public void invalidateCaps() {

    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        ContainerHelper.loadAllItems(pTag, this.items);
    }
    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        items.set(pSlot, pStack);
        this.onInventoryChanged();
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


    public @NotNull ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemStack = ContainerHelper.removeItem(this.items, pIndex, pCount);
        this.onInventoryChanged();
        return itemStack;
    }

    protected void onInventoryChanged() {

    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public @NotNull ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }


    @Override
    public boolean canPlaceItem(int pIndex, @NotNull ItemStack pStack) {
        return pIndex != 2;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public abstract int @NotNull [] getSlotsForFace(@NotNull Direction pSide);

    @Override
    public abstract boolean canPlaceItemThroughFace(int pIndex, @NotNull ItemStack pItemStack, @Nullable Direction pDirection);

    @Override
    public abstract boolean canTakeItemThroughFace(int pIndex, @NotNull ItemStack pStack, @NotNull Direction pDirection);

    @Override
    public abstract @NotNull Component getDisplayName();

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer);

    @Override
    public abstract int getContainerSize();
}
