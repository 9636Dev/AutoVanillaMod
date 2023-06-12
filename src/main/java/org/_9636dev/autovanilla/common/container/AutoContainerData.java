package org._9636dev.autovanilla.common.container;

import net.minecraft.world.inventory.ContainerData;
import org._9636dev.autovanilla.common.blockenttiy.AutoBlockEntity;

// TODO: 5/18/2023 Maybe doesn't need
/**
 * Implemented for data sync between container screen and server
 */
@SuppressWarnings("unused")
public class AutoContainerData implements ContainerData {

    private final AutoBlockEntity blockEntity;

    public AutoContainerData(AutoBlockEntity pBlockEntity) {
        this.blockEntity = pBlockEntity;
    }

    @Override
    public int get(int pIndex) {
        return blockEntity.data.get(pIndex);
    }

    @Override
    public void set(int pIndex, int pValue) {
        blockEntity.data.set(pIndex, pValue);
    }

    @Override
    public int getCount() {
        return blockEntity.data.getCount();
    }
}
