package com.LubieKakao1212.opencu.config;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraftforge.common.config.Config;

@Config(modid = OpenCUMod.MODID)
public class OpenCUConfig {

    public static Repulsor repulsor = new Repulsor();

    public static OmniDispenser omniDispenser = new OmniDispenser();

    public static class Repulsor {
        @Config.RangeDouble(min = 0)
        public double repulsorMaxOffset = 16;

        @Config.RangeDouble(min = 0)
        public double repulsorMaxRadius = 16;

        @Config.RangeDouble(min = 0)
        public double repulsorForceScale = 0.5;

        @Config.Comment("How much energy will be used per pulse from volume at maximum range")
        @Config.RangeDouble(min = 0)
        public double repulsorVolumeCost = 10000;

        @Config.Comment("How much energy will be used per pulse from offset distance at maximum offset")
        @Config.RangeDouble(min = 0)
        public double repulsorDistanceCost = 100;

        @Config.Comment("How much energy will be used per pulse from force at maximum force")
        @Config.RangeDouble(min = 0)
        public double repulsorForceCost = 10000;
    }

    public static class OmniDispenser {
        public DispenserGeneral general = new DispenserGeneral();

        public static class DispenserGeneral {
            @Config.SlidingOption()
            @Config.RangeDouble(min = 0., max = 3600.)
            public double[] rotationSpeed = new double[] { 90. };
        }
    }
}
