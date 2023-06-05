package org._9636dev.autosmithingtablerewrite.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class AutoBlockEntity extends BlockEntity {

    private boolean requiresUpdate;

    public final ContainerData data;

    public AutoBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pState) {
        super(pType, pPos, pState);

        this.requiresUpdate = false;
        this.data = createContainerData();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return serializeNBT();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag pTag) {
        super.load(pTag);
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt) {
        if (Objects.requireNonNull(level).isClientSide() && net.getDirection() == PacketFlow.CLIENTBOUND) {
            handleUpdateTag(Objects.requireNonNull(pkt.getTag()));
        }
    }

    public void internalTick() {
        if (level == null || level.isClientSide()) return;

        tickServer();

        if (this.requiresUpdate) {
            requestModelDataUpdate();
            setChanged();
            if (this.level != null)
                this.level.setBlockAndUpdate(this.worldPosition, getBlockState());

            this.requiresUpdate = false;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.update();
    }

    // autosmithingtable API

    public final void update() {
        this.requiresUpdate = true;
    }

    public abstract ContainerData createContainerData();

    @Override
    public abstract  <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side);

    @Override
    public abstract void invalidateCaps();

    @Override
    public abstract void load(@NotNull CompoundTag pTag);

    @Override
    protected abstract void saveAdditional(@NotNull CompoundTag pTag);

    protected abstract void tickServer();

    @NotNull
    public abstract List<ItemStack> getDrops();
}
