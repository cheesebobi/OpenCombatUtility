package com.LubieKakao1212.opencu.fabric.transaction;

import com.LubieKakao1212.opencu.common.transaction.IContext;
import com.LubieKakao1212.opencu.common.transaction.IEnergyContext;
import team.reborn.energy.api.EnergyStorage;

public class RebornEnergyContext implements IEnergyContext {

    private EnergyStorage energyStorage;

    public RebornEnergyContext(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public long useEnergy(long amount, IContext ctx) {
        return energyStorage.extract(amount, ITransactionAccess.transactionFromContext(ctx));
    }
}
