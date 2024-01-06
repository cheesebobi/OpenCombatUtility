package com.LubieKakao1212.opencu.pulse;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class EntityPulseType extends ForgeRegistryEntry<EntityPulseType> {

    private final EnergyUsage energyUsage;

    private final Supplier<EntityPulse> pulseFactory;

    private EntityPulseType(Supplier<EntityPulse> factory, EnergyUsage energyUsage) {
        this.energyUsage = energyUsage;
        this.pulseFactory = factory;
    }

    public EntityPulse createPulse() {
        return pulseFactory.get();
    }

    public EnergyUsage getEnergyUsage() {
        return energyUsage;
    }

    public static class Builder {
        private final EnergyUsage energyUsage;

        private final Supplier<EntityPulse> pulseFactory;

        public Builder(Supplier<EntityPulse> factory) {
            this.energyUsage = new EnergyUsage();
            this.pulseFactory = factory;
        }

        public Builder forceEnergy(float energyMul) {
            energyUsage.fromPower = energyMul;
            return this;
        }

        public Builder powerEnergy(float energyMul) {
            energyUsage.fromDistance = energyMul;
            return this;
        }

        public EntityPulseType build() {
            return new EntityPulseType(pulseFactory, energyUsage.copy());
        }
    }

    public static class EnergyUsage {
        public double fromPower = 1.;
        public double fromDistance = 1.;

        public EnergyUsage copy() {
            EnergyUsage out = new EnergyUsage();
            out.fromPower = fromPower;
            out.fromDistance = fromDistance;
            return out;
        }
    }
}
