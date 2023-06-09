package org._9636dev.autosmithingtablerewrite.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org._9636dev.autosmithingtablerewrite.common.block.AutoBlocks;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoSmithingTableBlockEntity;
import org._9636dev.autosmithingtablerewrite.common.container.slot.InputSlot;
import org._9636dev.autosmithingtablerewrite.common.container.slot.OutputSlot;
import org.jetbrains.annotations.NotNull;

public class AutoSmithingTableContainer extends AutoContainer {
    public AutoSmithingTableContainer(int pContainerId, Inventory pPlayerInv) {
        this(pContainerId, pPlayerInv, BlockPos.ZERO, new SimpleContainerData(AutoSmithingTableBlockEntity.DATA_COUNT),
                new SimpleContainer(AutoSmithingTableBlockEntity.SLOT_COUNT));
    }

    public AutoSmithingTableContainer(int pContainerId, Inventory pPlayerInv, BlockPos pPos, ContainerData pData, Container pSlots) {
        super(AutoContainers.AUTO_SMITHING_TABLE.get(), pContainerId, pPlayerInv, pPos, pData, pSlots);
    }

    @Override
    protected void addContainerSlots(Inventory pPlayerInv, Container pContainer) {
        addInventorySlots(pPlayerInv, 8, 84);

        this.addSlot(new InputSlot(pContainer, 0, 27, 47));
        this.addSlot(new InputSlot(pContainer,  1, 76, 47));
        this.addSlot(new OutputSlot(pContainer,  2, 134, 47));
    }

    @Override
    protected Block getBlock() {
        return AutoBlocks.AUTO_SMITHING_TABLE.get();
    }

    @Override
    protected void moveItemToContainer(ItemStack pStack) {
        moveItemStackTo(pStack, 36, 36 + AutoSmithingTableBlockEntity.SLOT_COUNT, false);
    }

    @Override
    public boolean clickMenuButton(@NotNull Player pPlayer, int pId) {

        return true;
    }
}
