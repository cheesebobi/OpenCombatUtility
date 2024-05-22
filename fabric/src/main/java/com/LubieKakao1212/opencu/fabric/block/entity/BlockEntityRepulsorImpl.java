package com.LubieKakao1212.opencu.fabric.block.entity;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.fabric.transaction.RebornEnergyContext;
import com.LubieKakao1212.opencu.fabric.transaction.SimpleContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.InfiniteEnergyStorage;
import team.reborn.energy.api.base.LimitingEnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class BlockEntityRepulsorImpl extends BlockEntityRepulsor {

    private final EnergyStorage energyStorage;
    public final EnergyStorage exposedEnegyStorage;

    public BlockEntityRepulsorImpl(BlockPos pos, BlockState blockState) {
        super(pos, blockState);

        //region energy
        var useEnergy = OpenCUConfigCommon.general().energyEnabled();

        if(useEnergy) {
            var capacity = OpenCUConfigCommon.capacitor().energyCapacity();
            energyStorage = new SimpleEnergyStorage(capacity, capacity, capacity);
            exposedEnegyStorage = new LimitingEnergyStorage(energyStorage, capacity, 0);
        } else {
            energyStorage = new InfiniteEnergyStorage();
            exposedEnegyStorage = null;
        }
        //endregion
    }

    @Override
    protected RepulsorContext getNewContext() {
        return new RepulsorContext(
                new SimpleContext(),
                new RebornEnergyContext(energyStorage)
        );
    }

}
