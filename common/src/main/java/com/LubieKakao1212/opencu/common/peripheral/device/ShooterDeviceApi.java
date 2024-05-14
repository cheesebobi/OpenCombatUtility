package com.LubieKakao1212.opencu.common.peripheral.device;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;
import dan200.computercraft.api.lua.LuaFunction;

public class ShooterDeviceApi implements IDeviceApi {

    //Reference cycle?
    private final ShooterDeviceState state;

    public ShooterDeviceApi(ShooterDeviceState state) {
        this.state = state;
    }

    @Override
    @LuaFunction
    public final String getApiId() {
        return OpenCUModCommon.MODID + ":shooter";
    }

    @LuaFunction
    public final double getSpread() {
        return state.getSpread();
    }

    @LuaFunction
    public final double getForce() {
        return state.getForce();
    }

    @LuaFunction
    public final double getEnergyUsage() {
        return state.getBaseEnergyUsage();
    }
}
