package com.lubiekakao1212.opencu.common.storage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;

public interface IItemStorage {

    ItemStack getStack(int slot);

    ItemStack extract(int slot, int amount, boolean simulate);

    ItemStack insert(int slot, ItemStack stack, boolean simulate);

    int getMaxStackSize(int slot);

    void set(int slot, ItemStack stack);

    boolean isValid(int slot, ItemStack stack);

    NbtElement serialize();

    void deserialize(NbtElement nbt);


}
