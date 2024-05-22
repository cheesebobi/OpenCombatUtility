package com.LubieKakao1212.opencu.common.device.state;

import com.lubiekakao1212.qulib.math.mc.Vector3m;

public class AdvancedTrackerDeviceState extends TrackerDeviceState {

    public Vector3m targetPosition;

    public AdvancedTrackerDeviceState(double trackingRange, double energyPerTick) {
        super(trackingRange, energyPerTick);
    }



}
