package com.LubieKakao1212.opencu.block.entity;


import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.init.CUBlockEntities;
import com.LubieKakao1212.opencu.init.CUPulse;
import com.LubieKakao1212.opencu.pulse.*;
import com.LubieKakao1212.qulib.capability.energy.InternalEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import javax.annotation.Nullable;

public class BlockEntityRepulsor extends BlockEntity {

    private EntityPulseType pulseType;
    private final PulseData pulseData = new PulseData();

    public int pulseTicksLeft;
    public static final int pulseTicks = 10;

    private final LazyOptional<InternalEnergyStorage> energyCap;

    public BlockEntityRepulsor(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.REPULSOR, pos, blockState);

        energyCap = OpenCUConfigCommon.REPULSOR.energyConfig.createCapFromConfig();

        setPulse(CUPulse.REPULSOR);
    }

    public static <T> void tick(@NotNull Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (!level.isClientSide) {
            BlockEntityRepulsor rep = (BlockEntityRepulsor) blockEntity;
            if (rep.pulseTicksLeft > 0) {
                level.sendBlockUpdated(pos, state, state, 3);
            }
            rep.pulseTicksLeft--;
        }
    }

    public void setRadius(double radius) {
        if(radius > OpenCUConfigCommon.REPULSOR.getMaxRadius())
        {
            throw new RuntimeException("Attempting to set invalid radius");
        }
        pulseData.radius = radius;
        setChanged();
    }

    public void setForce(double force) {
        if(Math.abs(force) > 1.0)
        {
            throw new RuntimeException("Attempting to set invalid force");
        }
        pulseData.force = force;
        setChanged();
    }

    public void setVector(@NotNull Vector3d vector) {
        setVector(vector.x(), vector.y(), vector.z());
    }

    public void setVector(double x, double y, double z) {
        pulseData.direction.set(x, y, z);
        setChanged();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
        if(cap == CapabilityEnergy.ENERGY) {
            return (LazyOptional<T>) energyCap;
        }
        return super.getCapability(cap, side);
    }

    public PulseExecutionResult pulse(Vector3d offset) {
        double distanceSqr = offset.lengthSquared();

        OpenCUConfigCommon.RepulsorDeviceConfig config = OpenCUConfigCommon.REPULSOR;

        double maxOffset = config.getMaxOffset();
        double maxRadius = config.getMaxRadius();

        if(distanceSqr > maxOffset * maxOffset) {
            return new PulseExecutionResult("Offset to large");
        }

        BlockPos pos = getBlockPos();

        Vector3d pulseOrigin = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).add(offset);

        final PulseExecutionResult[] result = { null };

        energyCap.ifPresent((energyStorage) -> {
            double radius = pulseData.radius;
            double volumeRatio = (radius * radius * radius) / (maxRadius * maxRadius * maxRadius);
            double forceRatio = Math.abs(pulseData.force);
            double distanceRatio = Math.sqrt(distanceSqr) / maxOffset;

            EntityPulseType.EnergyUsage energyUsageMul = pulseType.getEnergyUsage();
            int energyUsage = (int)Math.floor(
                    distanceRatio * config.getDistanceCost() * energyUsageMul.fromDistance +
                            volumeRatio * Math.abs(forceRatio) * config.getPowerCost() * energyUsageMul.fromPower);

            if(energyStorage.getEnergyStored() < energyUsage) {
                result[0] = new PulseExecutionResult("Not enough energy stored!!!");
            }
            else {
                int energyUsed = energyStorage.extractEnergyInternal(energyUsage, false);

                //Just in case something goes very wrong
                if (energyUsed != energyUsage) {
                    OpenCUMod.LOGGER.warn("RepulsorEnergyDrainError, If you see this report this to the mod author.");
                }
            }
        });

        if(result[0] != null) {
            //Something went wrong during energy calculations
            return result[0];
        }

        pulseType.executePulse(level, pulseOrigin, pulseData);
        pulseTicksLeft = pulseTicks;

        setChanged();

        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 3);

        return new PulseExecutionResult();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        CompoundTag pulseTag = pulseData.serializeNBT();
        pulseTag.putString("type", pulseType.getRegistryName().toString());
        compound.put("pulse", pulseTag);

        energyCap.ifPresent(energyStorage -> compound.put("energy", energyStorage.serializeNBT()));

        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);

        if(compound.contains("pulse", Tag.TAG_COMPOUND)) {
            CompoundTag pulseTag = compound.getCompound("pulse");

            setPulse(new ResourceLocation(pulseTag.getString("type")));
            pulseData.deserializeNBT(pulseTag);
        }

        Tag energyTag = compound.get("energy");

        if(energyTag != null) {
            energyCap.ifPresent(energyStorage -> energyStorage.deserializeNBT(energyTag));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("anim", pulseTicksLeft);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        pulseTicksLeft = tag.getInt("anim");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public final void onDataPacket(Connection net, @NotNull ClientboundBlockEntityDataPacket packet ) {
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

    public void setPulse(@NotNull EntityPulseType type) {
        pulseType = type;
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