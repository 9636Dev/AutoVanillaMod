package org._9636dev.autosmithingtablerewrite.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org._9636dev.autosmithingtablerewrite.common.block.AutoBlocks;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoSmithingTableBlockEntity;
import org._9636dev.autosmithingtablerewrite.common.container.slot.InputSlot;
import org._9636dev.autosmithingtablerewrite.common.container.slot.OutputSlot;

public class AutoSmithingTableContainer extends AutoContainer {
    public AutoSmithingTableContainer(int pContainerId, Inventory pPlayerInv) {
        this(pContainerId, pPlayerInv, BlockPos.ZERO, new SimpleContainerData(AutoSmithingTableBlockEntity.DATA_COUNT),
                new ItemStackHandler(AutoSmithingTableBlockEntity.SLOT_COUNT));
    }

    public AutoSmithingTableContainer(int pContainerId, Inventory pPlayerInv, BlockPos pPos, ContainerData pData, IItemHandler pSlots) {
        super(AutoContainers.AUTO_SMITHING_TABLE.get(), pContainerId, pPlayerInv, pPos, pData, pSlots);
    }

    @Override
    protected void addContainerSlots(Inventory pPlayerInv, IItemHandler pSlots) {
        addInventorySlots(pPlayerInv, 8, 84);

        this.addSlot(new InputSlot(pSlots, 0, 27, 47));
        this.addSlot(new InputSlot(pSlots,  1, 76, 47));
        this.addSlot(new OutputSlot(pSlots,  2, 134, 47));
    }

    @Override
    protected Block getBlock() {
        return AutoBlocks.AUTO_SMITHING_TABLE.get();
    }

    @Override
    protected void moveItemToContainer(ItemStack pStack) {
        for (Slot slot : this.slots) {
            if (slot.mayPlace(pStack)) slot.safeInsert(pStack);
            if (pStack.isEmpty()) break;
        }
    }
}
