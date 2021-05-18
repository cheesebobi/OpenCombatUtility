package com.LubieKakao1212.opencu.config;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraftforge.common.config.Config;

@Config(modid = OpenCUMod.MODID)
public class OpenCUConfig {

    @Config.RangeDouble(min = 0)
    public static double repulsorMaxOffset = 16;

    @Config.RangeDouble(min = 0)
    public static double repulsorMaxRadius = 16;

    @Config.RangeDouble(min = 0)
    public static double repulsorMaxForce = 0.5;

    @Config.RangeDouble(min = 0)
    public static double repulsorVolumeCost = 10000;

    @Config.RangeDouble(min = 0)
    public static double repulsorDistanceCost = 100;

    @Config.RangeDouble(min = 0)
    public static double repulsorForceCost = 10000;

}
