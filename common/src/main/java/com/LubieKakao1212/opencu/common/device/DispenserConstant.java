package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;

public class DispenserConstant extends ShooterBase {

    private final double constantSpread;
    private final double constantForce;
    private final double baseEnergy;

    public DispenserConstant(ShotMappings mappings, float alignmentSpeed, double constantSpread, double constantForce, double baseEnergy) {
        super(mappings, alignmentSpeed);
        this.constantForce = constantForce;
        this.constantSpread = constantSpread;
        this.baseEnergy = baseEnergy;
    }

    @Override
    public IDeviceState getNewState() {
        return new ShooterDeviceState(constantForce, constantSpread, baseEnergy);
    }
}
