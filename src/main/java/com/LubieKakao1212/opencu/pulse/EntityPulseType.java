package com.LubieKakao1212.opencu.pulse;

import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Function;
import java.util.function.Supplier;

public class EntityPulseType extends ForgeRegistryEntry<EntityPulseType> {

    private ForceTransformer forceTransformer;

    private final EnergyUsage energyUsage;

    private final Supplier<EntityPulse> pulseFactory;

    private EntityPulseType(Supplier<EntityPulse> factory, EnergyUsage energyUsage) {
        this.energyUsage = energyUsage;
        this.pulseFactory = factory;
    }

    public EntityPulse createPulse() {
        return pulseFactory.get();
    }

    public ForceTransformer getForceTransformer() {
        return forceTransformer;
    }

    public EnergyUsage getEnergyUsage() {
        return energyUsage;
    }

    public static class Builder {

        private ForceTransformer forceTransformer;

        private final EnergyUsage energyUsage;

        private final Supplier<EntityPulse> pulseFactory;

        public Builder(Supplier<EntityPulse> factory) {
            this.energyUsage = new EnergyUsage();
            this.pulseFactory = factory;
        }

        public Builder forceEnergy(float energyMul) {
            energyUsage.force = energyMul;
            return this;
        }

        public Builder volumeEnergy(float energyMul) {
            energyUsage.volume = energyMul;
            return this;
        }

        public Builder distanceEnergy(float energyMul) {
            energyUsage.distance = energyMul;
            return this;
        }

        public Builder forceTransformer(ForceTransformer transformer) {
            forceTransformer = transformer;
            return this;
        }

        public EntityPulseType build() {
            EntityPulseType type = new EntityPulseType(pulseFactory, energyUsage.copy());

            if(forceTransformer == null) {
                type.forceTransformer = ForceTransformer.identity();
            }else {
                type.forceTransformer = forceTransformer;
            }

            return type;
        }
    }

    public static class EnergyUsage {
        public double force = 1.;
        public double volume = 1.;
        public double distance = 1.;

        public EnergyUsage copy() {
            EnergyUsage out = new EnergyUsage();
            out.force = force;
            out.volume = volume;
            out.distance = distance;
            return out;
        }
    }
}
