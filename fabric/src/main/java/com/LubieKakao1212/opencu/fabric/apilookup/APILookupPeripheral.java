package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.common.peripheral.ModularFramePeripheral;
import com.LubieKakao1212.opencu.common.peripheral.RepulsorPeripheral;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import dan200.computercraft.api.peripheral.PeripheralLookup;

public class APILookupPeripheral {

    public static void register() {
        PeripheralLookup.get().registerForBlockEntity((be, dir) -> new RepulsorPeripheral(be), CUBlockEntitiesImpl.repulsor());
        PeripheralLookup.get().registerForBlockEntity((be, dir) -> new ModularFramePeripheral(be), CUBlockEntities.modularFrame());
    }
}
