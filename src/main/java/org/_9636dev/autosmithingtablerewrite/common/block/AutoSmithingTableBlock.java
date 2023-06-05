package org._9636dev.autosmithingtablerewrite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoBlockEntity;
import org._9636dev.autosmithingtablerewrite.common.blockenttiy.AutoSmithingTableBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AutoSmithingTableBlock extends AutoBlock implements WorldlyContainerHolder {

    public AutoSmithingTableBlock() {
        super(Properties.copy(Blocks.SMITHING_TABLE));
    }

    @Override
    public @Nullable AutoSmithingTableBlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new AutoSmithingTableBlockEntity(pPos, pState);
    }

    @Override
    public @NotNull WorldlyContainer getContainer(@NotNull BlockState pState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pPos) {
        return (AutoSmithingTableBlockEntity) Objects.requireNonNull(pLevel.getBlockEntity(pPos));
    }
}
