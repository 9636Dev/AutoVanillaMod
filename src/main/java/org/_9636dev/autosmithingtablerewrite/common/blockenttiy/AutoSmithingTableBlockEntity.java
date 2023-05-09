package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AutoSmithingTableBlockEntity extends BlockEntity {
    public AutoSmithingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AutoSmithingTableBlockEntities.AUTO_SMITHING_TABLE.get(), pPos, pBlockState);
    }

    public void onServerTick() {

    }

    public static class Data {
        private

        public Data()
    }
}
