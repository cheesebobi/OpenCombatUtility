package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.fabric.OpenCUConfigFabric;
import org.jetbrains.annotations.NotNull;

public class OpenCUConfigCommonImpl {

    private static final OpenCUConfigFabric CONFIG = OpenCUConfigFabric.createAndLoad();

    private static final GeneralConfig GENERAL = new GeneralConfig(CONFIG.GENERAL);

    private static final ModularFrameConfig MODULAR_FRAME = new ModularFrameConfig(CONFIG.MODULAR_FRAME, GENERAL);
    private static final RepulsorDeviceConfig REPULSOR_DEVICE = new RepulsorDeviceConfig(CONFIG.REPULSOR_DEVICE, GENERAL);

    private static final DispenserDeviceConfig VANILLA_DISPENSER_DEVICE = new DispenserDeviceConfig(CONFIG.DEVICES_DISPENSERS.VANILLA_DISPENSER_DEVICE);
    private static final DispenserDeviceConfig GOLDEN_DISPENSER_DEVICE = new DispenserDeviceConfig(CONFIG.DEVICES_DISPENSERS.GOLDEN_DISPENSER_DEVICE);
    private static final DispenserDeviceConfig DIAMOND_DISPENSER_DEVICE = new DispenserDeviceConfig(CONFIG.DEVICES_DISPENSERS.DIAMOND_DISPENSER_DEVICE);
    private static final DispenserDeviceConfig NETHERITE_DISPENSER_DEVICE = new DispenserDeviceConfig(CONFIG.DEVICES_DISPENSERS.NETHERITE_DISPENSER_DEVICE);

    private static final TrackerDeviceConfig TRACKER_DEVICE = new TrackerDeviceConfig(CONFIG.TRACKER_DEVICE);

    //Empty, used to call static init
    public static void init() { }

    public static OpenCUConfigCommon.@NotNull GeneralConfig general() {
        return GENERAL;
    }

    public static OpenCUConfigCommon.@NotNull ModularFrameConfig modularFrame() {
        return MODULAR_FRAME;
    }

    public static OpenCUConfigCommon.@NotNull RepulsorDeviceConfig repulsorDevice() {
        return REPULSOR_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig vanillaDispenserDevice() {
        return VANILLA_DISPENSER_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig goldenDispenserDevice() {
        return GOLDEN_DISPENSER_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig diamondDispenserDevice() {
        return DIAMOND_DISPENSER_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull DispenserDeviceConfig netheriteDispenserDevice() {
        return NETHERITE_DISPENSER_DEVICE;
    }

    public static OpenCUConfigCommon.@NotNull TrackerDeviceConfig trackerDevice() {
        return TRACKER_DEVICE;
    }

    public static class GeneralConfig implements OpenCUConfigCommon.GeneralConfig {

        private final OpenCUConfigFabric.GeneralConfig CONFIG;

        public GeneralConfig(OpenCUConfigFabric.GeneralConfig configIn) {
            this.CONFIG = configIn;
        }


        @Override
        public boolean energyEnabled() {
            return CONFIG.energyEnabled();
        }
    }

    public static class CapacitorConfig implements OpenCUConfigCommon.CapacitorConfig {

        private final OpenCUConfigFabric.CapacitorConfig CONFIG;
        private final OpenCUConfigCommon.IEnergyToggle PARENT_TOGGLE;

        public CapacitorConfig(OpenCUConfigFabric.CapacitorConfig configIn, OpenCUConfigCommon.IEnergyToggle parentToggle) {
            this.CONFIG = configIn;
            this.PARENT_TOGGLE = parentToggle;
        }

        /**
         * Internal method. Use {@link #isEnergyEnabled()} instead
         */
        @Override
        public boolean energyEnabled() {
            return CONFIG.energyEnabled();
        }

        @Override
        public int energyCapacity() {
            return CONFIG.capacity();
        }

        @Override
        public OpenCUConfigCommon.IEnergyToggle parent() {
            return PARENT_TOGGLE;
        }
    }

    public static class ModularFrameConfig implements OpenCUConfigCommon.ModularFrameConfig {

        private final CapacitorConfig CAPACITOR;
        private final OpenCUConfigFabric.MODULAR_FRAME CONFIG;

        public ModularFrameConfig(OpenCUConfigFabric.MODULAR_FRAME config, OpenCUConfigCommon.IEnergyToggle parentToggle) {
            this.CAPACITOR = new CapacitorConfig(config.energy, parentToggle);
            this.CONFIG = config;
        }

        @Override
        public OpenCUConfigCommon.CapacitorConfig energy() {
            return CAPACITOR;
        }
    }

    public static class RepulsorDeviceConfig implements OpenCUConfigCommon.RepulsorDeviceConfig {

        private final CapacitorConfig CAPACITOR;
        private final OpenCUConfigFabric.REPULSOR_DEVICE CONFIG;

        public RepulsorDeviceConfig(OpenCUConfigFabric.REPULSOR_DEVICE config, OpenCUConfigCommon.IEnergyToggle parentToggle) {
            this.CAPACITOR = new CapacitorConfig(config.energy, parentToggle);
            this.CONFIG = config;
        }

        @Override
        public double maxOffset() {
            return CONFIG.maxOffset();
        }

        @Override
        public double maxRadius() {
            return CONFIG.maxRadius();
        }

        @Override
        public double forceScale() {
            return CONFIG.forceScale();
        }

        @Override
        public double powerCost() {
            return CONFIG.powerCost();
        }

        @Override
        public double distanceCost() {
            return CONFIG.distanceCost();
        }

        @Override
        public OpenCUConfigCommon.CapacitorConfig energy() {
            return CAPACITOR;
        }
    }

    public static class DispenserDeviceConfig implements OpenCUConfigCommon.DispenserDeviceConfig {

        private final OpenCUConfigFabric.DispenserDeviceConfig CONFIG;

        public DispenserDeviceConfig(OpenCUConfigFabric.DispenserDeviceConfig configIn) {
            this.CONFIG = configIn;
        }


        @Override
        public double rotationSpeed() {
            return CONFIG.rotationSpeed();
        }

        @Override
        public double spread() {
            return CONFIG.spread();
        }

        @Override
        public double force() {
            return CONFIG.force();
        }

        @Override
        public double baseEnergy() {
            return CONFIG.baseEnergy();
        }

        @Override
        public double power() {
            return CONFIG.power();
        }

        /**
         * Use only for configurable dispensers
         */
        @Override
        public double maxSpread() {
            return CONFIG.maxSpread();
        }
    }

    public static class TrackerDeviceConfig implements OpenCUConfigCommon.TrackerDeviceConfig {

        private final OpenCUConfigFabric.TrackerDeviceConfig CONFIG;

        public TrackerDeviceConfig(OpenCUConfigFabric.TrackerDeviceConfig configIn) {
            this.CONFIG = configIn;
        }

        @Override
        public double range() {
            return CONFIG.range();
        }

        @Override
        public double energyPerTick() {
            return CONFIG.energyPerTick();
        }

        @Override
        public double energyPerActiveConnectionPerTick() {
            return CONFIG.energyPerConnectionPerTick();
        }
    }
}
