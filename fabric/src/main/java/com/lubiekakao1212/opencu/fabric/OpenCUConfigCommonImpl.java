package com.lubiekakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import org.jetbrains.annotations.NotNull;

public class OpenCUConfigCommonImpl {

    public static @NotNull OpenCUConfigCommon.GeneralConfig general() {
        return OpenCUConfigCommon.Defaults.GENERAL;
    }

    public static OpenCUConfigCommon.@NotNull CapacitorConfig capacitor() {
    }

    public static OpenCUConfigCommon.@NotNull ModularFrameConfig modularFrame() {
    }

    public static OpenCUConfigCommon.@NotNull RepulsorDeviceConfig repulsorDevice() {
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig vanillaDispenserDevice() {
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig goldenDispenserDevice() {
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig diamondDispenserDevice() {
    }

}
