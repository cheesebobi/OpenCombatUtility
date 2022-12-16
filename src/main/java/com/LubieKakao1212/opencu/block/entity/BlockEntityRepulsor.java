package com.LubieKakao1212.opencu.block.entity;


import com.LubieKakao1212.opencu.init.CUBlockEntities;
import com.LubieKakao1212.opencu.pulse.EntityPulse;
import com.LubieKakao1212.opencu.pulse.RepulsorPulse;
import com.LubieKakao1212.opencu.pulse.VectorPulse;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
                rep.pulseTicksLeft--;
                level.sendBlockUpdated(pos, state, state, 2);
            }else {
                rep.pulse();
            }
        }
    }

    //TODO CC
    /*public Object[] recalibrate(Context context, Arguments args) {
        int id = args.checkInteger(0);
        if(id < 0) {
            return new Object[] { false, "id cannot be negative" };
        }
        if(id > pulseFactories.length) {
            return new Object[] { false, "id to large" };
        }

        setPulse(id);
        return new Object[] { true };
    }*/

    //TODO CC
    /*public Object[] setRadius(Context context, Arguments args) {
        double radius = args.checkDouble(0);
        if(radius > OpenCUConfig.repulsor.repulsorMaxRadius)
        {
            return new Object[]{ false, "Radius to large" };
        }
        pulse.setRadius(radius);
        return new Object[] { true };
    }*/

    //TODO CC
    /*public Object[] setForce(Context context, Arguments args) {
        double force = args.checkDouble(0);
        if(force > 1.0)
        {
            return new Object[]{ false, "Force to large" };
        }
        pulse.setBaseForce(force);
        return new Object[]{ true };
    }*/

    //TODO CC
    /*public Object[] setVector(Context context, Arguments args) {
        double x = args.checkInteger(0);
        double y = args.checkInteger(1);
        double z = args.checkInteger(2);
        pulse.setVector(x, y, z);
        return new Object[] { true };
    }*/

    //TODO CC
    /*public Object[] setWhitelist(Context context, Arguments args) {
        pulse.setWhitelist(args.checkBoolean(0));
        return new Object[] { true };
    }*/

    //TODO CC
    /*public Object[] addToFilter(Context context, Arguments args) {
        filter.add(args.checkString(0));
        return new Object[] { true };
    }*/

    //TODO CC
    /*public void removeFromFilter(Context context, Arguments args) {
        filter.remove(args.checkString(0));
        return new Object[] { true };
    }*/

    //TODO CC
    /*public Object[] getFilter(Context context, Arguments args)
    {
        return filter.toArray();
    }*/

    public void pulse() {

        //TODO offset

        /*double x1 = args.checkDouble(0);
        double y1 = args.checkDouble(1);
        double z1 = args.checkDouble(2);
        double distanceSqr = x1*x1 + y1*y1 + z1*z1;

        if(distanceSqr > OpenCUConfig.repulsor.repulsorMaxOffset * OpenCUConfig.repulsor.repulsorMaxOffset) {
            return new Object[]{false, "Offset to large"};
        }*/

        double x1 = 0, y1 = 0, z1 = 0;

        BlockPos pos = getBlockPos();

        pulse.lock(level, x1 + pos.getX() + 0.5, y1 + pos.getY() + 0.5, z1 + pos.getZ() + 0.5);
        pulse.filter(filter);

        //TODO energy
        /*double radiusRatio = pulse.getRadius() * pulse.getRadius() * pulse.getRadius() / (OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius);
        double forceRatio = pulse.getBaseForce() / OpenCUConfig.repulsor.repulsorForceScale;
        double distanceRatio = Math.sqrt(distanceSqr) / OpenCUConfig.repulsor.repulsorDistanceCost;
        int energyUsage = MathHelper.floor(distanceRatio * OpenCUConfig.repulsor.repulsorDistanceCost + radiusRatio * OpenCUConfig.repulsor.repulsorVolumeCost + (Math.pow(2, Math.abs(forceRatio))-1.0) * OpenCUConfig.repulsor.repulsorForceCost);
        if(!node.tryChangeBuffer(-energyUsage))//if(energyBuffer.getEnergyStored() < energyUsage)
        {
            return new Object[]{ false, "Not enough energy stored!!!", energyUsage };
        }*/
        pulse.execute();
        pulseTicksLeft = pulseTicks;

        setChanged();

        BlockState state = level.getBlockState(pos);
        level.sendBlockUpdated(pos, state, state, 2);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        CompoundTag pulseTag = new CompoundTag();
        pulseTag.putInt("type", pulseType);
        compound.put("pulse", pulse.writeToNBT(pulseTag));
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        pulseTicksLeft = tag.getInt("anim");
    }

    public void setPulse(int type) {
        pulse = pulseFactories[type].get();
        this.pulseType = type;
    }

    /*@Override
    public boolean hasFastRenderer() {
        return true;
    }*/
}