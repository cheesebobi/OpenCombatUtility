package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import net.minecraft.nbt.NbtCompound;

import javax.sound.midi.Track;

public class TrackerDeviceState implements IDeviceState {

    private double trackingRange;
    private double energyPerTick;

    public double energyLeftover;
    public boolean noEnergy;

    public TrackerDeviceState(double trackingRange, double energyPerTick) {
        this.trackingRange = trackingRange;
        this.energyPerTick = energyPerTick;
        this.energyLeftover = 0;
        this.noEnergy = false;
    }


    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     *
     * @return
     */
    @Override
    public IDeviceApi getApi() {
        return null;
    }

    @Override
    public NbtCompound serialize() {
        return new NbtCompound();
    }

    @Override
    public void deserialize(NbtCompound nbt) {

    }

    public double getTrackingRange() {
        return trackingRange;
    }

    public double getEnergyPerTick() {
        return energyPerTick;
    }
}
