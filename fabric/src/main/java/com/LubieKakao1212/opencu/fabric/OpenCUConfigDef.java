package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import io.wispforest.owo.config.annotation.*;

//@Modmenu(modId = OpenCUModCommon.MODID)
@Config(name = "opencu-common", wrapperName = "OpenCUConfigFabric")
public class OpenCUConfigDef {

    @Nest
    public GeneralConfig GENERAL = new GeneralConfig();

    @Nest
    public ModularFrameConfig MODULAR_FRAME = new ModularFrameConfig(OpenCUConfigCommon.Defaults.MODULAR_FRAME);

    @Nest
    public RepulsorDeviceConfig REPULSOR_DEVICE = new RepulsorDeviceConfig(OpenCUConfigCommon.Defaults.REPULSOR_DEVICE);

    @SectionHeader("Devices")
    @Nest
    public Dispensers DEVICES_DISPENSERS = new Dispensers();

    @Nest
    public TrackerDeviceConfig TRACKER_DEVICE = new TrackerDeviceConfig(OpenCUConfigCommon.Defaults.TRACKER_DEVICE);

    public static class Dispensers {
        @Nest
        public DispenserDeviceConfig VANILLA_DISPENSER_DEVICE = new DispenserDeviceConfig(OpenCUConfigCommon.Defaults.VANILLA_DISPENSER_DEVICE);
        @Nest
        public DispenserDeviceConfig GOLDEN_DISPENSER_DEVICE = new DispenserDeviceConfig(OpenCUConfigCommon.Defaults.GOLDEN_DISPENSER_DEVICE);
        @Nest
        public DispenserDeviceConfig DIAMOND_DISPENSER_DEVICE = new DispenserDeviceConfig(OpenCUConfigCommon.Defaults.DIAMOND_DISPENSER_DEVICE);
        @Nest
        public DispenserDeviceConfig NETHERITE_DISPENSER_DEVICE = new DispenserDeviceConfig(OpenCUConfigCommon.Defaults.NETHERITE_DISPENSER_DEVICE);

    }

    public static class GeneralConfig {

        @RestartRequired
        public boolean energyEnabled = OpenCUConfigCommon.Defaults.GENERAL.energyEnabled();

    }

    public static class CapacitorConfig {

        @RestartRequired
        public boolean energyEnabled;
        @RestartRequired
        public int capacity;

        public CapacitorConfig(OpenCUConfigCommon.CapacitorConfig defaults) {
            this.energyEnabled = defaults.energyEnabled();
            this.capacity = defaults.energyCapacity();
        }
    }

    public static class ModularFrameConfig {

        @Nest
        public CapacitorConfig energy;

        public ModularFrameConfig(OpenCUConfigCommon.ModularFrameConfig defaults) {
            this.energy = new CapacitorConfig(defaults.energy());
        }
    }

    public static class RepulsorDeviceConfig  {

        public double maxOffset;

        public double maxRadius;

        public double forceScale;

        public double powerCost;

        public double distanceCost;

        @Nest
        public CapacitorConfig energy;

        public RepulsorDeviceConfig(OpenCUConfigCommon.RepulsorDeviceConfig defaults) {
            this.maxOffset = defaults.maxOffset();
            this.maxRadius = defaults.maxRadius();
            this.forceScale = defaults.forceScale();
            this.powerCost = defaults.powerCost();
            this.distanceCost = defaults.distanceCost();
            this.energy = new CapacitorConfig(defaults.energy());
        }
    }

    public static class DispenserDeviceConfig {

        public double rotationSpeed;
        public double spread;
        public double maxSpread;
        public double force;
        public double baseEnergy;
        public double power;

        public DispenserDeviceConfig(OpenCUConfigCommon.DispenserDeviceConfig defaults) {
            this.rotationSpeed = defaults.rotationSpeed();
            this.spread = defaults.spread();
            this.maxSpread = defaults.maxSpread();
            this.force = defaults.force();
            this.baseEnergy = defaults.baseEnergy();
            this.power = defaults.power();
        }
    }

    public static class TrackerDeviceConfig {

        public double range;

        public double energyPerTick;

        public double energyPerConnectionPerTick;

        public TrackerDeviceConfig(OpenCUConfigCommon.TrackerDeviceConfig config) {
            this.range = config.range();
            this.energyPerTick = config.energyPerTick();
            this.energyPerConnectionPerTick = config.energyPerActiveConnectionPerTick();
        }
    }

}
