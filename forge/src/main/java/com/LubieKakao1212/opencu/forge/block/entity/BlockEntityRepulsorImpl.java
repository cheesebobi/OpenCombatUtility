package com.LubieKakao1212.opencu.forge.block.entity;


import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class BlockEntityRepulsorImpl extends BlockEntityRepulsor {

//    private final LazyOptional<InternalEnergyStorage> energyCap;

    public BlockEntityRepulsorImpl(BlockPos pos, BlockState blockState) {
        super(pos, blockState);

//        energyCap = OpenCUConfigCommon.REPULSOR.energyConfig.createCapFromConfig();

        //setPulse(CUPulse.REPULSOR.get());
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
//        if(cap == ForgeCapabilities.ENERGY) {
//            return (LazyOptional<T>) energyCap;
//        }
        return super.getCapability(cap, side);
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
//        energyCap.ifPresent(energyStorage -> compound.put("energy", energyStorage.serializeNBT()));

        super.writeNbt(compound);
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);
//        NbtElement energyTag = compound.get("energy");
//
//        if(energyTag != null) {
//            energyCap.ifPresent(energyStorage -> energyStorage.deserializeNBT(energyTag));
//        }
    }

    /*@Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("anim", pulseTicksLeft);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull NbtCompound tag) {
        pulseTicksLeft = tag.getInt("anim");
    }*/

    /*@Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }*/

    /*@Override
    public final void onDataPacket(ClientConnection net, @NotNull BlockEntityUpdateS2CPacket packet ) {
        var tag = packet.getNbt();
        if (tag != null) handleUpdateTag(tag);
    }*/
}