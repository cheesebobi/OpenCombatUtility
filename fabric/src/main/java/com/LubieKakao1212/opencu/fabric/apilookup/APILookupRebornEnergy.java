package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityRepulsorImpl;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import team.reborn.energy.api.EnergyStorage;

public class APILookupRebornEnergy {

    public static void register() {
        EnergyStorage.SIDED.registerForBlockEntity((be, side) -> {
            var frame = (BlockEntityModularFrameImpl) be;
            return frame.exposedEnegyStorage;
        }, CUBlockEntities.modularFrame());

        EnergyStorage.SIDED.registerForBlockEntity((be, side) -> {
            var repulsor = (BlockEntityRepulsorImpl) be;
            return repulsor.exposedEnegyStorage;
        }, CUBlockEntities.repulsor());
    }


}
