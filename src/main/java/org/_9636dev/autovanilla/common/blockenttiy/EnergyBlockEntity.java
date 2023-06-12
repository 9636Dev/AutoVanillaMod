package org._9636dev.autovanilla.common.blockenttiy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org._9636dev.autovanilla.common.capability.AutoEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class EnergyBlockEntity extends SidedInventoryBlockEntity {

    protected final LazyOptional<AutoEnergyStorage> energyHandler;

    public EnergyBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pState) {
        super(pType, pPos, pState);

        this.energyHandler = LazyOptional.of(this::createEnergyStorage);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        energyHandler.ifPresent(e -> pTag.put("autosmithingtable.energy", e.serializeNBT()));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("autosmithingtable.energy")) energyHandler.ifPresent(e -> e.deserializeNBT(pTag.get("autosmithingtable.energy")));
    }

    @Override
    public void internalTick() {
        if (this.energyHandler.lazyMap(AutoEnergyStorage::isDirty).orElseThrow(IllegalStateException::new)) update();
        super.internalTick();
    }

    // autosmithingtable API
    public int insertEnergy(int pAmount, boolean pSimulate) {
        return energyHandler.lazyMap(e -> e.receiveEnergy(pAmount, pSimulate)).orElseThrow(IllegalStateException::new);
    }

    public int extractEnergy(int pAmount, boolean pSimulate) {
        return energyHandler.lazyMap(e -> e.extractEnergy(pAmount, pSimulate)).orElseThrow(IllegalStateException::new);
    }

    public int getEnergyStored() {
        return energyHandler.lazyMap(AutoEnergyStorage::getEnergyStored).orElseThrow(IllegalStateException::new);
    }

    public int getMaxEnergyStored() {
        return energyHandler.lazyMap(AutoEnergyStorage::getMaxEnergyStored).orElseThrow(IllegalStateException::new);
    }

    public int getMSBofEnergy() {
        return energyHandler.lazyMap(AutoEnergyStorage::getMSBofEnergy).orElseThrow(IllegalStateException::new);
    }

    public int getLSBofEnergy() {
        return energyHandler.lazyMap(AutoEnergyStorage::getLSBofEnergy).orElseThrow(IllegalStateException::new);
    }

    protected abstract AutoEnergyStorage createEnergyStorage();
}
