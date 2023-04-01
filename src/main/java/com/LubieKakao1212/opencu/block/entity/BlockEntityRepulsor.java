package com.LubieKakao1212.opencu.block.entity;


import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.init.CUBlockEntities;
import com.LubieKakao1212.opencu.init.CUPulse;
import com.LubieKakao1212.opencu.pulse.*;
import com.LubieKakao1212.qulib.capability.energy.InternalEnergyStorage;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class BlockEntityRepulsor extends BlockEntity {
/*
    private static final Supplier<EntityPulse>[] pulseFactories = new Supplier[] { RepulsorPulse::new, VectorPulse::new, StasisPulse::new };*/

    private EntityPulseType pulseType;
    private EntityPulse pulseInstance;
    private final ArrayList<String> filter = new ArrayList<>();

    public int pulseTicksLeft;
    public static final int pulseTicks = 10;

    private final LazyOptional<InternalEnergyStorage> energyCap;

    private final InternalEnergyStorage energy;

    public BlockEntityRepulsor(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.REPULSOR, pos, blockState);
        energy = new InternalEnergyStorage(OpenCUConfig.repulsor.energy.capacity, OpenCUConfig.repulsor.energy.maxReceive, 0);
        energyCap = LazyOptional.of(() -> energy);
        setPulse(CUPulse.REPULSOR);
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (!level.isClientSide) {
            BlockEntityRepulsor rep = (BlockEntityRepulsor) blockEntity;
            if (rep.pulseTicksLeft > 0) {
                level.sendBlockUpdated(pos, state, state, 3);
            }
            rep.pulseTicksLeft--;
        }
    }

    public void setRadius(double radius) {
        if(radius > OpenCUConfig.repulsor.repulsorMaxRadius)
        {
            throw new RuntimeException("Attempting to set invalid radius");
        }
        pulseInstance.setRadius(radius);
    }

    public void setForce(double force) {
        if(Math.abs(force) > 1.0)
        {
            throw new RuntimeException("Attempting to set invalid force");
        }
        pulseInstance.setBaseForce(pulseType.getForceTransformer().transform(force));
    }

    public void setVector(Vector3d vector) {
        pulseInstance.setVector(vector.x, vector.y, vector.z);
    }

    public void setVector(double x, double y, double z) {
        pulseInstance.setVector(x, y, z);
    }

    public void setWhitelist(boolean whitelist) {
        pulseInstance.setWhitelist(whitelist);
    }

    public void addToFilter(String name) {
        filter.add(name);
    }

    public void removeFromFilter(String name) {
        filter.remove(name);
    }

    public Object[] getFilter()
    {
        return filter.toArray();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY) {
            return (LazyOptional<T>) energyCap;
        }
        return super.getCapability(cap, side);
    }

    public PulseExecutionResult pulse(double xOffset, double yOffset, double zOffset) {
        double distanceSqr = xOffset*xOffset + yOffset*yOffset + zOffset*zOffset;

        if(distanceSqr > OpenCUConfig.repulsor.repulsorMaxOffset * OpenCUConfig.repulsor.repulsorMaxOffset) {
            return new PulseExecutionResult("Offset to large");
        }

        BlockPos pos = getBlockPos();

        pulseInstance.lock(level, xOffset + pos.getX() + 0.5, yOffset + pos.getY() + 0.5, zOffset + pos.getZ() + 0.5);
        pulseInstance.filter(filter);

        double radiusRatio = pulseInstance.getRadius() * pulseInstance.getRadius() * pulseInstance.getRadius() / (OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius);
        double forceRatio = pulseInstance.getBaseForce() / OpenCUConfig.repulsor.repulsorForceScale;
        double distanceRatio = Math.sqrt(distanceSqr) / OpenCUConfig.repulsor.repulsorDistanceCost;
        EntityPulseType.EnergyUsage energyUsageMul = pulseType.getEnergyUsage();
        int energyUsage = (int)Math.floor(
                distanceRatio * OpenCUConfig.repulsor.repulsorDistanceCost * energyUsageMul.distance +
                radiusRatio * OpenCUConfig.repulsor.repulsorVolumeCost * energyUsageMul.volume +
                (Math.pow(2, Math.abs(forceRatio)) - 1.0) * OpenCUConfig.repulsor.repulsorForceCost * energyUsageMul.force);

        if(energy.getEnergyStored() < energyUsage) {
            return new PulseExecutionResult("Not enough energy stored!!!");
        }

        int energyUsed = energy.extractEnergyInternal(energyUsage, false);

        //Just in case something goes very wrong
        if(energyUsed != energyUsage) {
            OpenCUMod.LOGGER.warn("RepulsorEnergyDrainError, If you see this report this to the mod author.");
        }

        pulseInstance.execute();
        pulseTicksLeft = pulseTicks;

        setChanged();

        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 3);

        return new PulseExecutionResult();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        CompoundTag pulseTag = new CompoundTag();
        pulseTag.putString("type", pulseType.getRegistryName().toString());
        compound.put("pulse", pulseInstance.writeToNBT(pulseTag));

        compound.put("energy", energy.serializeNBT());

        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        if(compound.contains("pulse", Tag.TAG_COMPOUND)) {
            CompoundTag pulseTag = compound.getCompound("pulse");

            setPulse(new ResourceLocation(pulseTag.getString("type")));
            pulseInstance.readFromNBT(pulseTag);
        }

        Tag energyTag = compound.get("energy");

        if(energyTag != null) {
            energy.deserializeNBT(energyTag);
        }

    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("anim", pulseTicksLeft);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        pulseTicksLeft = tag.getInt("anim");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public final void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet ) {
        var tag = packet.getTag();
        if (tag != null) handleUpdateTag(tag);
    }

    public void setPulse(ResourceLocation id) {
        EntityPulseType type = CUPulse.getRegistry().getValue(id);
        if(type == null) {
            throw new RuntimeException("Invalid pulse type id" + id.toString());
        }
        setPulse(type);
    }

    public void setPulse(EntityPulseType type) {
        pulseType = type;
        this.pulseInstance = type.createPulse();
        filter.clear();
    }

    public static class PulseExecutionResult {
        public String errorDescription;

        public boolean wasSuccessfull;

        public PulseExecutionResult() {
            this.errorDescription = null;
            this.wasSuccessfull = true;
        }

        public PulseExecutionResult(String errorDescription) {
            this.errorDescription = errorDescription;
            this.wasSuccessfull = false;
        }
    }
}