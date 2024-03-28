package com.LubieKakao1212.opencu.forge;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import org.jetbrains.annotations.NotNull;

public class OpenCUConfigCommonImpl {

    public static OpenCUConfigCommon.@NotNull GeneralConfig general() {
        return OpenCUConfigCommon.Defaults.GENERAL;
    }

    public static OpenCUConfigCommon.@NotNull CapacitorConfig capacitor() {
        return OpenCUConfigCommon.Defaults.CAPACITOR;
    }

    public static OpenCUConfigCommon.@NotNull ModularFrameConfig modularFrame() {
        return OpenCUConfigCommon.Defaults.MODULAR_FRAME;
    }

    public static OpenCUConfigCommon.@NotNull RepulsorDeviceConfig repulsorDevice() {
        return OpenCUConfigCommon.Defaults.REPULSOR_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig vanillaDispenserDevice() {
        return OpenCUConfigCommon.Defaults.VANILLA_DISPENSER_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig goldenDispenserDevice() {
        return OpenCUConfigCommon.Defaults.GOLDEN_DISPENSER_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig diamondDispenserDevice() {
        return OpenCUConfigCommon.Defaults.DIAMOND_DISPENSER_DEVICE;
    }

}
