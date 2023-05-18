package org._9636dev.autosmithingtablerewrite.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org._9636dev.autosmithingtablerewrite.common.block.AutoSmithingTableBlocks;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoSmithingTableBlockEntity;

public class AutoSmithingTableContainer extends AutoContainer {
    public AutoSmithingTableContainer(int pContainerId, Inventory pPlayerInv) {
        this(pContainerId, pPlayerInv, BlockPos.ZERO, new SimpleContainerData(AutoSmithingTableBlockEntity.DATA_COUNT),
                new ItemStackHandler(3));
    }

    public AutoSmithingTableContainer(int pContainerId, Inventory pPlayerInv, BlockPos pPos, ContainerData pData, IItemHandler pSlots) {
        super(AutoSmithingTableContainers.AUTO_SMITHING_TABLE.get(), pContainerId, pPlayerInv, pPos, pData, pSlots);
    }

    @Override
    protected void addContainerSlots(Inventory pPlayerInv, IItemHandler pSlots) {
        addInventorySlots(pPlayerInv, 8, 84);
    }

    @Override
    protected Block getBlock() {
        return AutoSmithingTableBlocks.AUTO_SMITHING_TABLE.get();
    }

    @Override
    protected void moveItemToContainer(ItemStack pStack) {

    }
}
