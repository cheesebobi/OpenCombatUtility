package com.LubieKakao1212.opencu.block.entity;


import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.init.CUBlockEntities;
import com.LubieKakao1212.opencu.pulse.EntityPulse;
import com.LubieKakao1212.opencu.pulse.RepulsorPulse;
import com.LubieKakao1212.opencu.pulse.VectorPulse;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class BlockEntityRepulsor extends BlockEntity {

    private static final Supplier<EntityPulse>[] pulseFactories = new Supplier[] { RepulsorPulse::new, VectorPulse::new };

    private EntityPulse pulse;
    private final ArrayList<String> filter = new ArrayList<>();
    private int pulseType;

    public int pulseTicksLeft;
    public static final int pulseTicks = 10;

    public BlockEntityRepulsor(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.REPULSOR, pos, blockState);
        setPulse(0);
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
        pulse.setRadius(radius);
    }

    public void setForce(double force) {
        if(Math.abs(force) > 1.0)
        {
            throw new RuntimeException("Attempting to set invalid force");
        }
        pulse.setBaseForce(force);
    }

    public void setVector(Vector3d vector) {
        pulse.setVector(vector.x, vector.y, vector.z);
    }

    public void setVector(double x, double y, double z) {
        pulse.setVector(x, y, z);
    }

    public void setWhitelist(boolean whitelist) {
        pulse.setWhitelist(whitelist);
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

    public PulseExecutionResult pulse(double xOffset, double yOffset, double zOffset) {
        double distanceSqr = xOffset*xOffset + yOffset*yOffset + zOffset*zOffset;

        if(distanceSqr > OpenCUConfig.repulsor.repulsorMaxOffset * OpenCUConfig.repulsor.repulsorMaxOffset) {
            return new PulseExecutionResult("Offset to large");
        }

        BlockPos pos = getBlockPos();

        pulse.lock(level, xOffset + pos.getX() + 0.5, yOffset + pos.getY() + 0.5, zOffset + pos.getZ() + 0.5);
        pulse.filter(filter);

        double radiusRatio = pulse.getRadius() * pulse.getRadius() * pulse.getRadius() / (OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius);
        double forceRatio = pulse.getBaseForce() / OpenCUConfig.repulsor.repulsorForceScale;
        double distanceRatio = Math.sqrt(distanceSqr) / OpenCUConfig.repulsor.repulsorDistanceCost;
        int energyUsage = (int)Math.floor(distanceRatio * OpenCUConfig.repulsor.repulsorDistanceCost + radiusRatio * OpenCUConfig.repulsor.repulsorVolumeCost + (Math.pow(2, Math.abs(forceRatio))-1.0) * OpenCUConfig.repulsor.repulsorForceCost);
        //TODO Check and drain energy buffer
        /*if(!node.tryChangeBuffer(-energyUsage))//if(energyBuffer.getEnergyStored() < energyUsage)
        {
            return new Object[]{ false, "Not enough energy stored!!!", energyUsage };
        }*/

        pulse.execute();
        pulseTicksLeft = pulseTicks;

        setChanged();

        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 3);

        return new PulseExecutionResult();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        CompoundTag pulseTag = new CompoundTag();
        pulseTag.putInt("type", pulseType);
        compound.put("pulse", pulse.writeToNBT(pulseTag));

        //pulseTag.putInt("anim", pulseTicksLeft);

        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        if(compound.contains("pulse", Tag.TAG_COMPOUND)) {
            CompoundTag pulseTag = compound.getCompound("pulse");
            setPulse(pulseTag.getInt("type"));
            pulse.readFromNBT(pulseTag);
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

    public void setPulse(int type) {
        pulse = pulseFactories[type].get();
        filter.clear();
        this.pulseType = type;
    }

    public int getPulseTypeCount() {
        return pulseFactories.length;
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