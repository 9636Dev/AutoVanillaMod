package org._9636dev.autosmithingtablerewrite.common.capability;

import net.minecraftforge.energy.EnergyStorage;

public class AutoEnergyStorage extends EnergyStorage {

    private boolean dirty;

    public AutoEnergyStorage(int capacity) {
        super(capacity);

        this.dirty = true;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!simulate) setDirty();
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!simulate) setDirty();
        return super.extractEnergy(maxExtract, simulate);
    }

    public int getMSBofEnergy() {
        return getMSB(this.getEnergyStored());
    }

    public static int getMSB(int num) {
        return (num >> 16) & 0xffff;
    }

    public int getLSBofEnergy() {
        return getLSB(this.getEnergyStored());
    }

    public static int getLSB(int num) {
        return (num & 0xffff);
    }

    public void setDirty() {
        setDirty(true);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        this.setDirty();
    }
}
