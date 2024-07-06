package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;

public class DispenserConstant extends ShooterBase {

    private final double constantSpread;
    private final double constantForce;
    private final double baseEnergy;
    private final double basePower;

    public DispenserConstant(ShotMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy, double power) {
        super(mappings, alignmentSpeed);
        this.constantForce = constantForce;
        this.constantSpread = constantSpread;
        this.baseEnergy = baseEnergy;
        this.basePower = power;
    }

    public DispenserConstant(ShotMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy) {
        this(mappings, alignmentSpeed, constantSpread, constantForce, baseEnergy, 1.0);
    }


    @Override
    public IDeviceState getNewState() {
        return new ShooterDeviceState(constantForce, constantSpread, baseEnergy, basePower);
    }
}
