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
        public Dispenser vanilla = new Dispenser(90., 5., 1., 500);

        public Dispenser tier2 = new Dispenser(180., 360., 1., 500);

        public Dispenser tier3 = new Dispenser(3600., 360., 1.5, 500);

        @Config.Name("Frequent Shooting Energy Cost Multiplier")
        public double frequentShootingEnergyMultiplier = 1.5;

        @Config.Ignore
        public Energy energyAlt = new Energy();

        public static class Dispenser {
            @Config.SlidingOption()
            @Config.RangeDouble(min = 0., max = 3600.)
            public double rotationSpeed;

            @Config.RangeDouble(min = 0., max = 360.)
            public double spread;

            public double force;

            public double base_energy;

            public Dispenser(double rotationSpeed, double spread, double force, double base_energy) {
                this.rotationSpeed = rotationSpeed;
                this.spread = spread;
                this.force = force;
                this.base_energy = base_energy;
            }
        }

        public static class EnergyUsage {
            public double frequentShootingEnergyMultiplier = 1.5;
        }

        public static class Energy {
            //TODO Proper comments
            @Config.Comment("How many ticks back is relevant")
            public int relevantTicks = 1;

            @Config.Comment("How many shots is last period will cause the energy multiplier to become non-linear")
            public int cutoff = 1;

            @Config.RangeDouble(min = 0, max = Double.MAX_VALUE)
            public double linearSteepness = 0;

            @Config.RangeDouble(min = 0, max = Double.MAX_VALUE)
            public double nonLinearSteepness = 0.05;

            public double base = 1;

            @Config.Ignore
            private Double b2;
            @Config.Ignore
            private Double c;

            public double getB2() {
                if (b2 == null) {
                    b2 = linearSteepness - 2 * nonLinearSteepness * cutoff;
                }
                return b2;
            }

            public double getC() {
                if (c == null) {
                    c = base + nonLinearSteepness * cutoff * cutoff;
                }
                return c;
            }
        }
    }
}
