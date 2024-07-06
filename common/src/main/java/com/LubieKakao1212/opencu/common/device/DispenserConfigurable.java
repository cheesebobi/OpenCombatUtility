package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;

public class DispenserConfigurable extends ShooterBase {

    private final double minSpread;
    private final double maxSpread;
    private final double maxForce;
    private final double baseEnergy;

    public DispenserConfigurable(ShotMappings mappings, float alignmentSpeed, double minSpread, double maxSpread, double maxForce, double baseEnergy) {
        super(mappings, alignmentSpeed);
        this.minSpread = minSpread;
        this.maxSpread = maxSpread;
        this.maxForce = maxForce;
        this.baseEnergy = baseEnergy;
    }

    //TODO Not working
    @Override
    public IDeviceState getNewState() {
        return new ShooterDeviceState(maxForce, minSpread, baseEnergy, -1.0);
    }

}
