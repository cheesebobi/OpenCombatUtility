package com.LubieKakao1212.opencu.common.block.entity;


import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.energy.InternalEnergyStorage;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.init.CUBlockEntities;
import com.LubieKakao1212.opencu.init.CUPulse;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
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
        super(CUBlockEntities.REPULSOR.get(), pos, blockState);

        energyCap = OpenCUConfigCommon.REPULSOR.energyConfig.createCapFromConfig();

        setPulse(CUPulse.REPULSOR.get());
    }

    public static <T> void tick(@NotNull World level, BlockPos pos, BlockState state, T blockEntity) {
        if (!level.isClient) {
            BlockEntityRepulsor rep = (BlockEntityRepulsor) blockEntity;
            if (rep.pulseTicksLeft > 0) {
                level.updateListeners(pos, state, state, 3);
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
        markDirty();
    }

    public void setForce(double force) {
        if(Math.abs(force) > 1.0)
        {
            throw new RuntimeException("Attempting to set invalid force");
        }
        pulseData.force = force;
        markDirty();
    }

    public void setVector(@NotNull Vector3d vector) {
        setVector(vector.x(), vector.y(), vector.z());
    }

    public void setVector(double x, double y, double z) {
        pulseData.direction.set(x, y, z);
        markDirty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
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

        BlockPos pos = getPos();

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

        pulseType.executePulse(world, pulseOrigin, pulseData);
        pulseTicksLeft = pulseTicks;

        markDirty();

        BlockState state = world.getBlockState(pos);
        world.updateListeners(pos, state, state, 3);

        return new PulseExecutionResult();
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        NbtCompound pulseTag = pulseData.serializeNBT();
        pulseTag.putString("type", pulseType.getRegistryKey().toString());
        compound.put("pulse", pulseTag);

        energyCap.ifPresent(energyStorage -> compound.put("energy", energyStorage.serializeNBT()));

        super.writeNbt(compound);
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);

        if(compound.contains("pulse", NbtElement.COMPOUND_TYPE)) {
            NbtCompound pulseTag = compound.getCompound("pulse");

            setPulse(new Identifier(pulseTag.getString("type")));
            pulseData.deserializeNBT(pulseTag);
        }

        NbtElement energyTag = compound.get("energy");

        if(energyTag != null) {
            energyCap.ifPresent(energyStorage -> energyStorage.deserializeNBT(energyTag));
        }
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("anim", pulseTicksLeft);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull NbtCompound tag) {
        pulseTicksLeft = tag.getInt("anim");
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public final void onDataPacket(ClientConnection net, @NotNull BlockEntityUpdateS2CPacket packet ) {
        var tag = packet.getNbt();
        if (tag != null) handleUpdateTag(tag);
    }

    public void setPulse(Identifier id) {
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