package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.common.peripheral.RepulsorPeripheral;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import dan200.computercraft.api.peripheral.PeripheralLookup;

public class CUPeripheral {

    public static void register() {
        PeripheralLookup.get().registerForBlockEntity((be, dir) -> new RepulsorPeripheral(be), CUBlockEntitiesImpl.repulsor());
    }

}
