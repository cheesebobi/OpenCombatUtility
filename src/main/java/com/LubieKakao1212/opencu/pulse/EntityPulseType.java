package com.LubieKakao1212.opencu.pulse;

import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityPulseType extends ForgeRegistryEntry<EntityPulseType> {

    private final EnergyUsage energyUsage;

    private final List<IPulse> pulsePasses;

    private EntityPulseType(List<IPulse> pulsePasses, EnergyUsage energyUsage) {
        this.energyUsage = energyUsage;
        this.pulsePasses = pulsePasses;
    }

    public void executePulse(Level level, Vector3d pos, PulseData pulseData) {
        executePulse(level, pos, new Vector3d(pulseData.direction), pulseData.radius, pulseData.force);
    }

    public void executePulse(Level level, Vector3d pos, Vector3d direction, double radius, double force) {
        for(IPulse pulsePass : pulsePasses)
        {
            pulsePass.doPulse(level, pos, direction, radius, force);
        }
    }

    public EnergyUsage getEnergyUsage() {
        return energyUsage;
    }

    public static class Builder {
        private final EnergyUsage energyUsage;

        private final List<IPulse> pulsePasses;

        public Builder(IPulse mainPulse) {
            this.energyUsage = new EnergyUsage();
            this.pulsePasses = new ArrayList<>();
            this.pulsePasses.add(mainPulse);
        }

        public Builder forceEnergy(float energyMul) {
            energyUsage.fromPower = energyMul;
            return this;
        }

        public Builder powerEnergy(float energyMul) {
            energyUsage.fromDistance = energyMul;
            return this;
        }

        public Builder addDependentPulse(boolean condition, Supplier<IPulse> pulseSupplier) {
            if(condition) {
                pulsePasses.add(pulseSupplier.get());
            }
            return this;
        }


        public EntityPulseType build() {
            return new EntityPulseType(pulsePasses, energyUsage.copy());
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
