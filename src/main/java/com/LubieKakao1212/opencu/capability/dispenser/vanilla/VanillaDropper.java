package com.LubieKakao1212.opencu.capability.dispenser.vanilla;

import com.LubieKakao1212.opencu.capability.dispenser.DispenserBase;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserRegistry;

public class VanillaDropper extends DispenserBase {


    @Override
    public double getSpread() {
        return VanillaDispenser.spread;
    }

    @Override
    public double getForce() {
        return VanillaDispenser.defaultForce;
    }

    @Override
    protected DispenserMappings getMappings() {
        return DispenserRegistry.VANILLA_DISPENSER;
    }
}
