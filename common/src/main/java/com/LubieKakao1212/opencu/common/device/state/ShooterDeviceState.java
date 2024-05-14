package com.LubieKakao1212.opencu.common.device.state;

import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.peripheral.device.ShooterDeviceApi;
import com.LubieKakao1212.opencu.common.util.Lazy;
import net.minecraft.nbt.NbtCompound;

public class ShooterDeviceState implements IDeviceState {

    private final Lazy<ShooterDeviceApi> api;

    private double force;
    private double spread;
    private double baseEnergyUsage;

    public ShooterDeviceState(double force, double spread, double baseEnergyUsage) {
        api = new Lazy<>(() -> new ShooterDeviceApi(this));
        this.force = force;
        this.spread = spread;
        this.baseEnergyUsage = baseEnergyUsage;
    }

    /**
     * Returns a cc api for this state instance.
     * Do not call unless CC:Tweaked is present
     *
     * @return
     */
    @Override
    public IDeviceApi getApi() {
        return api.getValue();
    }

    public double getSpread() {
        return spread;
    }

    public double getForce() {
        return force;
    }


    public double getBaseEnergyUsage() {
        return baseEnergyUsage;
    }

    @Override
    public NbtCompound serialize() {
        return new NbtCompound();
    }

    @Override
    public void deserialize(NbtCompound nbt) {

    }
}
