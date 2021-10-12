package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.lib.math.MathUtil;

public class DispenserConstant extends DispenserBase {

    private double constantSpread;
    private double constantForce;

    public DispenserConstant(DispenserMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy) {
        super(mappings, alignmentSpeed, baseEnergy);
        this.constantForce = constantForce;
        this.constantSpread = constantSpread;
    }

    @Override
    public double getSpread() {
        return constantSpread;
    }

    @Override
    public double getForce() {
        return constantForce;
    }

}
