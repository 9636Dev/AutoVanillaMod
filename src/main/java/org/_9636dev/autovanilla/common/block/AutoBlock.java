package org._9636dev.autovanilla.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org._9636dev.autovanilla.common.blockenttiy.AutoBlockEntity;
import org._9636dev.autovanilla.common.blockenttiy.SidedInventoryBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class AutoBlock extends Block implements EntityBlock {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AutoBlock(Properties pProperties) {
        super(pProperties);
    }

    @Deprecated
    @Override
    public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
        if (pLevel.getBlockEntity(pPos) instanceof SidedInventoryBlockEntity be) {
            // Drop inventory items
            Containers.dropContents(pLevel, pPos, be);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Deprecated
    public boolean triggerEvent(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity != null && blockentity.triggerEvent(pId, pParam);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(HORIZONTAL_FACING, pContext.getHorizontalDirection().getOpposite());
    }

    public @NotNull BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(HORIZONTAL_FACING, pRot.rotate(pState.getValue(HORIZONTAL_FACING)));
    }

    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(HORIZONTAL_FACING)));
    }

    // TODO: 2/15/2023 Implement hasAnalogOutput

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return (level, pos, state, be) -> {
            // Sanity Check
            if (be instanceof AutoBlockEntity abe) abe.internalTick();
        };
    }

    @Override
    public final @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide) return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);

        if (pLevel.getBlockEntity(pPos) instanceof AutoBlockEntity be) {
            InteractionResult result = this.onUse(pState, pLevel, pPos, pPlayer, pHand, pHit, be);
            if (result != InteractionResult.PASS) return result;

            if (this.hasContainer() && be instanceof SidedInventoryBlockEntity) {
                pPlayer.openMenu((MenuProvider) be);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }



    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState);

    /**
     * On use function wrapper for the Block, as the 'use' function is used internally and is final
     */
    @SuppressWarnings("unused")
    protected InteractionResult onUse(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                      @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit, AutoBlockEntity pBlockEntity)
    {
        return InteractionResult.PASS;
    }

    protected boolean hasContainer() {
        return true;
    }
}
