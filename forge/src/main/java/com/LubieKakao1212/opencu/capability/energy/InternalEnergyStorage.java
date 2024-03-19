package com.LubieKakao1212.opencu.capability.energy;

import net.minecraftforge.energy.EnergyStorage;

public class InternalEnergyStorage extends EnergyStorage {

    public InternalEnergyStorage(int capacity) {
        super(capacity);
    }

    public InternalEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    public int receiveEnergyInternal(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, this.maxReceive);
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

}