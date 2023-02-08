package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.qulib.capability.energy.InternalEnergyStorage;
import com.LubieKakao1212.qulib.math.AimUtil;
import com.LubieKakao1212.qulib.math.MathUtil;
import com.LubieKakao1212.qulib.util.joml.Vector3dUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import com.LubieKakao1212.qulib.libs.joml.Quaterniond;
import com.LubieKakao1212.qulib.libs.joml.Vector3d;

public abstract class DispenserBase implements IDispenser {

    private DispenserMappings mappings;
    private float alignmentSpeed;
    private double energyConsumption;

    public DispenserBase(DispenserMappings mappings, float alignmentSpeed, double energyConsumption) {
        this.mappings = mappings;
        this.alignmentSpeed = alignmentSpeed;
        this.energyConsumption = energyConsumption;
    }

    @Override
    public DispenseResult shoot(ICapabilityProvider shooter, Level level, ItemStack shotItem, BlockPos pos, Quaterniond aim) {
        DispenseEntry entry = getMappings().getDispenseResult(shotItem);

        DispenseResult result = new DispenseResult(shotItem);

        //TODO energy
        shooter.getCapability(CapabilityEnergy.ENERGY).ifPresent((energy) -> {

            //region Energy Handling
            if(!(energy instanceof InternalEnergyStorage)) {
                return;
            }

            InternalEnergyStorage energyStorage = (InternalEnergyStorage) energy;

            int energyRequired = (int)(entry.getEnergyMultiplier() * energyConsumption);

            if(energyStorage.getEnergyStored() < energyRequired) {
                return;
            }

            energyStorage.extractEnergyInternal(energyRequired, false);
            //endregion

            //region Shooting
            Entity entity = entry.getEntity(shotItem, level);

            Vector3d forward = AimUtil.calculateForwardWithUniformSpread(aim, (getSpread() * entry.getSpreadMultiplier() * MathUtil.degToRad), Vector3dUtil.south());

            entity.setPos(pos.getX() + 0.5 + forward.x, pos.getY() + 0.5 + forward.y, pos.getZ() + 0.5 + forward.z);
            entity.setXRot(0f);
            entity.setYRot(0f);

            double velocity = getForce() / entry.getMass();

            entity.setDeltaMovement(forward.x * velocity, forward.y * velocity, forward.z * velocity);

            entry.getPostShootAction().Execute(entity, forward, velocity);

            level.addFreshEntity(entity);

            entry.getPostSpawnAction().Execute(entity, forward, velocity);

            result.leftover = entry.getLeftover();
            //endregion
        });

        return result;
    }

    @Override
    public String trySetSpread(double spread) {
        return "Current dispenser does not support variable spread.";
    }

    @Override
    public String trySetForce(double force) {
        return "Current dispenser does not support variable force.";
    }

    public float getBaseEnergyRequired() {
        return 0;
    }

    @Override
    public float getAlignmentSpeed() {
        return alignmentSpeed;
    }

    public abstract double getSpread();

    public abstract double getForce();

    protected DispenserMappings getMappings() {
        return this.mappings;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}

