package com.LubieKakao1212.opencu.config;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraftforge.energy.EnergyStorage;
//import net.minecraftforge.common.config.Config;

//@Config(modid = OpenCUMod.MODID)
public class OpenCUConfig {

    public static Repulsor repulsor = new Repulsor();

    public static OmniDispenser omniDispenser = new OmniDispenser();

    public static class Repulsor {

        public EnergyConfig energy = new EnergyConfig(20000, 20000);

        //@Config.RangeDouble(min = 0)
        public double repulsorMaxOffset = 5;

        //@Config.RangeDouble(min = 0)
        public double repulsorMaxRadius = 5;

        //@Config.RangeDouble(min = 0)
        public double repulsorForceScale = 0.5;

        //@Config.Comment("How much energy will be used per pulse from volume at maximum range")
        //@Config.RangeDouble(min = 0)
        public double repulsorVolumeCost = 5000;

        //@Config.Comment("How much energy will be used per pulse from offset distance at maximum offset")
        //@Config.RangeDouble(min = 0)
        public double repulsorDistanceCost = 100;

        //@Config.Comment("How much energy will be used per pulse from force at maximum force")
        //@Config.RangeDouble(min = 0)
        public double repulsorForceCost = 5000;
    }

    public static class OmniDispenser {

        public EnergyConfig energy = new EnergyConfig(10000, 10000);

        public Dispenser vanilla = new Dispenser(90., 5., 1., 1000);

        public DispenserConfigurable tier2 = new DispenserConfigurable(180., 5., 360., 1., 1000);

        public DispenserConfigurable tier3 = new DispenserConfigurable(3600., 0, 360., 1.5, 1000);

        public static class Dispenser {
            //@Config.SlidingOption()
            //@Config.RangeDouble(min = 0., max = 3600.)
            public double rotationSpeed;

            //@Config.RangeDouble(min = 0., max = 360.)
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

        public static class DispenserConfigurable {
            //@Config.SlidingOption()
            //@Config.RangeDouble(min = 0., max = 3600.)
            public double rotationSpeed;

            //@Config.RangeDouble(min = 0., max = 360.)
            public double spread;

            //@Config.RangeDouble(min = 0., max = 360.)
            public double spread_max;

            public double force;

            public double base_energy;

            public DispenserConfigurable(double rotationSpeed, double spread, double spread_max, double force, double base_energy) {
                this.rotationSpeed = rotationSpeed;
                this.spread = spread;
                this.spread_max = spread_max;
                this.force = force;
                this.base_energy = base_energy;
            }
        }
    }

    public static class EnergyConfig {
        public int capacity;
        public int maxReceive;

        public EnergyConfig(int capacity, int maxReceive) {
            this.capacity = capacity;
            this.maxReceive = maxReceive;
        }
    }
}
