package org._9636dev.autovanilla.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class AutoContainer extends AbstractContainerMenu {

    public static final int INVENTORY_SLOTS_START = 0;
    public static final int INVENTORY_SLOTS_END = 26;

    public static final int HOTBAR_SLOTS_START = 27;
    public static final int HOTBAR_SLOTS_END = 35;


    public final ContainerData data;
    protected final ContainerLevelAccess levelAccess;

    protected AutoContainer(MenuType<?> pMenuType, int pContainerId, Inventory pPlayerInv, BlockPos pPos,
                            ContainerData pData, Container pContainer) {
        super(pMenuType, pContainerId);

        this.data = pData;
        this.addDataSlots(pData);
        this.addContainerSlots(pPlayerInv, pContainer);
        this.levelAccess = pPlayerInv.player.level.isClientSide() ? ContainerLevelAccess.NULL : ContainerLevelAccess.create(pPlayerInv.player.level, pPos);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return AutoContainer.stillValid(levelAccess, pPlayer, getBlock());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack returnStack = ItemStack.EMPTY;

        final Slot slot = getSlot(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            returnStack = item.copy();
            if (index <= 35) {
                // Check for slots in Container first
                moveItemToContainer(item);

                // Inventory slots
                if (index <= INVENTORY_SLOTS_END) {
                    if (!moveItemStackTo(item, HOTBAR_SLOTS_START, HOTBAR_SLOTS_END + 1, true))
                        return ItemStack.EMPTY;
                }
                else {
                    if (!moveItemStackTo(item, INVENTORY_SLOTS_START, INVENTORY_SLOTS_END + 1, false))
                        return ItemStack.EMPTY;
                }
            }
            else { // From Container
                if (!moveItemStackTo(item, INVENTORY_SLOTS_START, HOTBAR_SLOTS_END + 1, true))
                    return ItemStack.EMPTY;
            }

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return returnStack;
    }



    /**
     * Adds inventory slots in container
     * @param pLeft Default is 8
     * @param pTop Default is 84
     */
    protected void addInventorySlots(Inventory pPlayerInv, int pLeft, int pTop) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInv, j + i * 9 + 9, pLeft + j * 18, pTop + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInv, k, pLeft + k * 18, pTop + 58));
        }
    }

    protected abstract void addContainerSlots(Inventory pPlayerInv, Container pContainer);
    protected abstract Block getBlock();
    protected abstract void moveItemToContainer(ItemStack pStack);
}
