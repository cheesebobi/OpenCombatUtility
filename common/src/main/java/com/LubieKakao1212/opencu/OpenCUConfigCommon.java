package com.LubieKakao1212.opencu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.NotNull;

public class OpenCUConfigCommon {

    @NotNull
    @ExpectPlatform
    public static GeneralConfig general() {
        return Defaults.GENERAL;
    }
    @NotNull
    @ExpectPlatform
    public static CapacitorConfig capacitor() {
        return Defaults.CAPACITOR;
    }
    @NotNull
    @ExpectPlatform
    public static ModularFrameConfig modularFrame() {
        return Defaults.MODULAR_FRAME;
    }

    @NotNull
    @ExpectPlatform
    public static RepulsorDeviceConfig repulsorDevice() {
        return Defaults.REPULSOR_DEVICE;
    }

    @NotNull
    @ExpectPlatform
    public static DispenserDeviceConfig vanillaDispenserDevice() {
        return Defaults.VANILLA_DISPENSER_DEVICE;
    }
    @NotNull
    @ExpectPlatform
    public static DispenserDeviceConfig goldenDispenserDevice() {
        return Defaults.GOLDEN_DISPENSER_DEVICE;
    }
    @NotNull
    @ExpectPlatform
    public static DispenserDeviceConfig diamondDispenserDevice() {
        return Defaults.DIAMOND_DISPENSER_DEVICE;
    }
    @NotNull
    @ExpectPlatform
    public static DispenserDeviceConfig netheriteDispenserDevice() {
        return Defaults.DIAMOND_DISPENSER_DEVICE;
    }


    public interface GeneralConfig {
        boolean energyEnabled();
    }

    public interface CapacitorConfig {
        int energyCapacity();
    }

    public interface ModularFrameConfig {
        //Nothing
    }

    public interface RepulsorDeviceConfig {
        double maxOffset();
        double maxRadius();
        double forceScale();
        double powerCost();
        double distanceCost();
    }

    public interface DispenserDeviceConfig {
        double rotationSpeed();
        double spread();
        double force();
        double baseEnergy();
        double power();

        /**
         * Use only for configurable dispensers
         */
        double maxSpread();
    }

    public static class Defaults {

        public static final OpenCUConfigCommon.GeneralConfig GENERAL = new GeneralConfig(true);

        public static final OpenCUConfigCommon.CapacitorConfig CAPACITOR = new CapacitorConfig(10000);
        public static final OpenCUConfigCommon.ModularFrameConfig MODULAR_FRAME = new ModularFrameConfig();

        public static final OpenCUConfigCommon.RepulsorDeviceConfig REPULSOR_DEVICE = new RepulsorDeviceConfig(5.0, 5.0, 1.0, 20000, 1000);


        public static final OpenCUConfigCommon.DispenserDeviceConfig VANILLA_DISPENSER_DEVICE = new DispenserDeviceConfig(90, 5.0, 5.0, 1.0, 500, 1.0);
        public static final OpenCUConfigCommon.DispenserDeviceConfig GOLDEN_DISPENSER_DEVICE = new DispenserDeviceConfig(180, 10.0, 360.0, 1.0, 300, 1.0);
        public static final OpenCUConfigCommon.DispenserDeviceConfig DIAMOND_DISPENSER_DEVICE = new DispenserDeviceConfig(120, 2.5, 360.0, 1.5, 750, 1.5);
        public static final OpenCUConfigCommon.DispenserDeviceConfig NETHERITE_DISPENSER_DEVICE = new DispenserDeviceConfig(50, 0, 0.0, 5.5, 1500, 5.0);


        public record GeneralConfig(boolean energyEnabled) implements OpenCUConfigCommon.GeneralConfig { }

        public record CapacitorConfig(int energyCapacity) implements OpenCUConfigCommon.CapacitorConfig { }

        public record ModularFrameConfig() implements OpenCUConfigCommon.ModularFrameConfig { }

        public record RepulsorDeviceConfig(double maxOffset, double maxRadius, double forceScale, double powerCost, double distanceCost) implements OpenCUConfigCommon.RepulsorDeviceConfig { }

        public record DispenserDeviceConfig(double rotationSpeed, double spread, double maxSpread, double force, double baseEnergy, double power) implements OpenCUConfigCommon.DispenserDeviceConfig {  }

    }


    /*public interface EnergyStorageConfig {

    }*/

    /*public interface DeviceConfig {
        boolean getEnergyUsageEnabled();

    }*/

    /*public static class RepulsorDeviceConfig {
        public final EnergyStorageConfig energyConfig;

        private ForgeConfigSpec.ConfigValue<Double> repulsorMaxOffset;

        private ForgeConfigSpec.ConfigValue<Double> repulsorMaxRadius;

        private ForgeConfigSpec.ConfigValue<Double> repulsorForceScale;

        private ForgeConfigSpec.ConfigValue<Double> repulsorPowerCost;

        private ForgeConfigSpec.ConfigValue<Double> repulsorDistanceCost;

        private final double defaultMaxOffset,
                defaultMaxRadius,
                defaultForceScale,
                defaultPowerCost,
                defaultDistanceCost;

        public RepulsorDeviceConfig(EnergyStorageConfig energyConfig, double defaultMaxOffset, double defaultMaxRadius, double defaultForceScale, double defaultPowerCost, double defaultDistanceCost) {
            this.energyConfig = energyConfig;

            this.defaultMaxOffset = defaultMaxOffset;
            this.defaultMaxRadius = defaultMaxRadius;
            this.defaultForceScale = defaultForceScale;
            this.defaultPowerCost = defaultPowerCost;
            this.defaultDistanceCost = defaultDistanceCost;
        }

        public void init(ForgeConfigSpec.Builder builder) {
            builder.push("Repulsor");
            {
                energyConfig.init(builder);

                builder.push("Cost (Only if energy is enabled)");
                {
                    repulsorPowerCost = builder.comment("How much energy will be used per pulse with maximum force and volume")
                            .define("Power Cost", defaultPowerCost);
                    repulsorDistanceCost = builder.comment("How much energy will be used per pulse from offset distance at maximum offset")
                            .define("Distance Cost", defaultDistanceCost);
                }
                builder.pop();

                repulsorMaxRadius = builder.comment("Max radius of a single pulse")
                        .define("Max Radius", defaultMaxRadius, (value) -> value != null && (Double) value > 0);

                repulsorMaxOffset = builder.comment("Max offset of the pulse center from the repulsor")
                        .define("Max Offset", defaultMaxOffset, (value) -> value != null && (Double) value > 0);

                repulsorForceScale = builder.comment("Pulse Force Multiplier")
                        .define("Force Mult", defaultForceScale);
            }
            builder.pop();
        }

        public double getMaxOffset() {
            return repulsorMaxOffset.get();
        }

        public double getMaxRadius() {
            return repulsorMaxRadius.get();
        }

        public double getForceScale() {
            return repulsorForceScale.get();
        }

        public double getPowerCost() {
            return repulsorPowerCost.get();
        }

        public double getDistanceCost() {
            return repulsorDistanceCost.get();
        }

    }

    public static class DispenserDeviceConfig {
        public final EnergyStorageConfig energyConfig;

        public final DispenserConfig vanillaDispenser;
        public final DispenserConfig dispenserT2;
        public final DispenserConfig dispenserT3;

        public DispenserDeviceConfig(EnergyStorageConfig energyConfig, DispenserConfig vanillaDispenserConfig, DispenserConfig dispenserT2Config, DispenserConfig dispenserT3Config) {
            this.energyConfig = energyConfig;
            this.vanillaDispenser = vanillaDispenserConfig;
            this.dispenserT2 = dispenserT2Config;
            this.dispenserT3 = dispenserT3Config;
        }

        public void init(ForgeConfigSpec.Builder builder) {
            builder.push("Dispenser");
            {
                energyConfig.init(builder);
                builder.push("Vanilla");
                    vanillaDispenser.init(builder);
                builder.pop();
                builder.push("Tier 2");
                    dispenserT2.init(builder);
                builder.pop();
                builder.push("Tier 3");
                    dispenserT3.init(builder);
                builder.pop();
            }
            builder.pop();
        }

        public static class DispenserConfig {
            private ForgeConfigSpec.ConfigValue<Double> rotationSpeed;

            private ForgeConfigSpec.ConfigValue<Double> spread;

            private ForgeConfigSpec.ConfigValue<Double> maxSpread;

            private ForgeConfigSpec.ConfigValue<Double> force;

            private ForgeConfigSpec.ConfigValue<Double> baseEnergy;

            private final double defaultRotationSpeed;
            private final double defaultSpread;
            private final double defaultMaxSpread;
            private final double defaultForce;
            private final double defaultBaseEnergy;

            public DispenserConfig(double defaultRotationSpeed, double defaultSpread, double defaultForce, double defaultBaseEnergy) {
                this(defaultRotationSpeed, defaultSpread, -1.0, defaultForce, defaultBaseEnergy);
            }

            public DispenserConfig(double defaultRotationSpeed, double defaultSpread, double defaultMaxSpread, double defaultForce, double defaultBaseEnergy) {
                this.defaultRotationSpeed = defaultRotationSpeed;
                this.defaultSpread = defaultSpread;
                this.defaultForce = defaultForce;
                this.defaultBaseEnergy = defaultBaseEnergy;
                this.defaultMaxSpread = defaultMaxSpread;
            }

            public void init(ForgeConfigSpec.Builder builder) {
                this.rotationSpeed = builder.comment("Alignment Speed in degrees per tick (°/s)").define("Speed", defaultRotationSpeed);
                if(defaultMaxSpread > 0) {
                    this.spread = builder.comment("Min Configurable Spread in degrees").define("Spread", defaultSpread);
                    this.maxSpread = builder.comment("Max configurable spread in degrees").define("Max Spread", defaultMaxSpread);
                    this.force = builder.comment("Max Configurable Base Force").define("Force", defaultForce);
                }
                else {
                    this.spread = builder.comment("Base Spread in degrees").define("Spread", defaultSpread);
                    this.force = builder.comment("Base Force").define("Force", defaultForce);
                }
                this.baseEnergy = builder.comment("Base Energy Usage (If Enabled)").define("Energy Usage", defaultBaseEnergy);
            }

            public double getRotationSpeed() {
                return rotationSpeed.get();
            }

            public double getSpread() {
                return spread.get();
            }

            public double getForce() {
                return force.get();
            }

            public double getBaseEnergy() {
                return baseEnergy.get();
            }

            *//**
             * Use only for configurable dispensers
             *//*
            public double getMaxSpread() {
                if(defaultMaxSpread < 0){
                    return -1.0;
                }
                return maxSpread.get();
            }
        }
    }

    public static class EnergyConfig {
        private ForgeConfigSpec.ConfigValue<Boolean> enable;
        private ForgeConfigSpec.ConfigValue<Integer> capacity;
        private ForgeConfigSpec.ConfigValue<Integer> maxInput;

        private final int defaultCapacity;
        private final int defaultReceive;

        public EnergyConfig(int defaultCapacity, int defaultReceive) {
            this.defaultCapacity = defaultCapacity;
            this.defaultReceive = defaultReceive;
        }

        public void init(ForgeConfigSpec.@NotNull Builder builder) {
            builder.push("Energy Config");
            {
                enable = builder.comment("Enable/Disable Energy requirement for this device\n" +
                                "Devices are balanced with energy usage in mind, use if you want what you are doing")
                        .worldRestart().define("Enable", true);
                capacity = builder.comment("Energy Capacity of this device")
                        .worldRestart().define("Capacity", defaultCapacity);
                maxInput = builder.comment("Maximum FE/t this device can receive")
                        .worldRestart().define("Max Input", defaultReceive);
            }
            builder.pop();
        }

        public boolean isEnabled() {
            return GENERAL.getEnergyEnabled() && enable.get();
        }

        public int getCapacity() {
            return capacity.get();
        }

        public int getMaxIn(){
            return maxInput.get();
        }

        public LazyOptional<InternalEnergyStorage> createCapFromConfig() {
            if(!isEnabled()) {
                return LazyOptional.empty();
            }
            return LazyOptional.of(() -> new InternalEnergyStorage(getCapacity(), getMaxIn(), 0));
        }

    }*/
}
