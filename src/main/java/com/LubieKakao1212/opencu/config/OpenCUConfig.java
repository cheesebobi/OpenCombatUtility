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
    public static double repulsorForceScale = 0.5;

    @Config.Comment("How much energy will be used per pulse from volume at maximum range")
    @Config.RangeDouble(min = 0)
    public static double repulsorVolumeCost = 10000;

    @Config.Comment("How much energy will be used per pulse from offset distance at maximum offset")
    @Config.RangeDouble(min = 0)
    public static double repulsorDistanceCost = 100;

    @Config.Comment("How much energy will be used per pulse from force at maximum force")
    @Config.RangeDouble(min = 0)
    public static double repulsorForceCost = 10000;

}
