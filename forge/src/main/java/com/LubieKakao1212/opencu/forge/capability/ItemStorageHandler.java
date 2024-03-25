package com.lubiekakao1212.opencu.forge.capability;

import com.lubiekakao1212.opencu.common.storage.IItemStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStorageHandler implements IItemStorage {

    private ItemStackHandler itemStackHandler;

    public ItemStorageHandler(ItemStackHandler itemStackHandler) {
        this.itemStackHandler = itemStackHandler;
    }

    @Override
    public ItemStack getStack(int slot) {
        return itemStackHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        return itemStackHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        return itemStackHandler.insertItem(slot, stack, simulate);
    }

    @Override
    public int getMaxStackSize(int slot) {
        return itemStackHandler.getSlotLimit(slot);
    }

    @Override
    public void set(int slot, ItemStack stack) {

    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return itemStackHandler.isItemValid(slot, stack);
    }

    @Override
    public NbtElement serialize() {
        return itemStackHandler.serializeNBT();
    }

    @Override
    public void deserialize(NbtElement nbt) {
        itemStackHandler.deserializeNBT((NbtCompound) nbt);
    }
}
