package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.state.BlockState;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoEnergyStorage;
import org._9636dev.autosmithingtablerewrite.common.capability.AutoItemHandler;
import org._9636dev.autosmithingtablerewrite.common.container.AutoSmithingTableContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoSmithingTableBlockEntity extends EnergyBlockEntity {

    public static final int DATA_COUNT = 2;

    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pState) {
        super(AutoBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pState);
    }

    @Override
    protected void tickServer() {

    }

    @Override
    protected AutoEnergyStorage createEnergyStorage() {
        return new AutoEnergyStorage(100000);
    }

    @Override
    public AutoItemHandler createItemHandler() {
        return new AutoItemHandler(3);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("title.autosmithingtable.smithing_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AutoSmithingTableContainer(pContainerId, pPlayerInventory, this.getBlockPos(), this.data,
                this.itemHandler.orElseThrow(() -> new IllegalStateException("Could not get the ItemHandler of block")));
    }

    @Override
    public ContainerData createContainerData() {
        return new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> getMSBofEnergy();
                    case 1 -> getLSBofEnergy();
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0,1 -> {}
                    default -> throw new IllegalArgumentException("Invalid argument for AutoSmithingTable container data: " + pIndex);
                }
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
    }
}
