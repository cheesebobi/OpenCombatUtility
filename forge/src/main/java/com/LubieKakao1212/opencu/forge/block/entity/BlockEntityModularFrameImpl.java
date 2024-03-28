package com.LubieKakao1212.opencu.forge.block.entity;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.LubieKakao1212.opencu.forge.capability.ItemStorageHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityModularFrameImpl extends BlockEntityModularFrame {

    private LazyOptional<IItemHandler> inventoryCapability;

    public BlockEntityModularFrameImpl(BlockPos pos, BlockState blockState) {
        super(pos, blockState, (be) -> {
            var inv = new ItemStackHandler(10) {
                @Override
                protected void onContentsChanged(int slot) {
                    if(slot == 0) {
                        be.updateDispenser();
                    }
                    be.markDirty();
                }

                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    if(slot == 0)
                    {
                        return PlatformUtil.getDispenser(stack) != null;
                    }
                    return true;
                }

                @Override
                public int getSlotLimit(int slot) {
                    if(slot == 0)
                    {
                        return 1;
                    }else {
                        return 64;
                    }
                }
            };
            ((BlockEntityModularFrameImpl)be).inventoryCapability = LazyOptional.of(() -> inv);
            return new ItemStorageHandler(inv);
        });
    }

    public boolean isUsableBy(PlayerEntity player) {
        assert world != null;
        return player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 64D && world.getBlockEntity(pos) == this;
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
//        //Energy
//        energyCap.ifPresent(energyStorage -> compound.put("energy", energyStorage.serializeNBT()));

        super.writeNbt(compound);
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);
//        //Energy
//        NbtElement energyTag = compound.get("energy");
//        if(energyTag != null) {
//            energyCap.ifPresent((energy) -> energy.deserializeNBT(energyTag));
//        }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if(capability == ForgeCapabilities.ITEM_HANDLER) {
            return (LazyOptional<T>)inventoryCapability;
        }
        /*if(capability == ForgeCapabilities.ENERGY) {
            return (LazyOptional<T>)energyCap;
        }*/
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryCapability.invalidate();
    }
}
