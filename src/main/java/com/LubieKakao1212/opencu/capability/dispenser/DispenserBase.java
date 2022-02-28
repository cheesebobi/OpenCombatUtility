package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.lib.math.AimUtil;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import li.cil.oc.api.network.Connector;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaterniond;
import org.joml.Vector3d;

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
    public DispenseResult Shoot(Connector connector, World world, ItemStack shotItem, BlockPos pos, Quaterniond aim, double energyMultiplierFromFrequency) {
        DispenseEntry entry = getMappings().getDispenseResult(shotItem);
        if(connector.tryChangeBuffer(-entry.getEnergyMultiplier() * energyMultiplierFromFrequency * energyConsumption)) {
            Entity entity = entry.getEntity(shotItem, world);

            Vector3d forward = AimUtil.calculateForwardWithSpread(aim, (getSpread() * entry.getSpreadMultiplier() * MathUtil.degToRad));

            entity.setLocationAndAngles(pos.getX() + 0.5 + forward.x, pos.getY() + 0.5 + forward.y, pos.getZ() + 0.5 + forward.z, 0.f,0.f);

            double velocity = getForce() / entry.getMass();

            entity.motionX = forward.x * velocity;
            entity.motionY = forward.y * velocity;
            entity.motionZ = forward.z * velocity;

            entry.getPostShootAction().Execute(entity, forward, velocity);

            world.spawnEntity(entity);

            entry.getPostSpawnAction().Execute(entity, forward, velocity);

            return new DispenseResult(entry.getLeftover());
        }else
        {
            return new DispenseResult(shotItem);
        }
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
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}

