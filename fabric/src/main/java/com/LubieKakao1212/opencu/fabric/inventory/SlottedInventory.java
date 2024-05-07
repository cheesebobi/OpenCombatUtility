package com.LubieKakao1212.opencu.fabric.inventory;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public abstract class SlottedInventory implements Inventory {

    private final SimpleInventory inv;

    public SlottedInventory(int size) {
        inv = new SimpleInventory(size);
    }

    @Override
    public int size() {
        return inv.size();
    }

    @Override
    public boolean isEmpty() {
        return inv.isEmpty();
    }

    /**
     * Fetches the stack currently stored at the given slot. If the slot is empty,
     * or is outside the bounds of this inventory, returns see {@link ItemStack#EMPTY}.
     *
     * @param slot
     */
    @Override
    public ItemStack getStack(int slot) {
        return inv.getStack(slot);
    }

    /**
     * Removes a specific number of items from the given slot.
     *
     * @param slot
     * @param amount
     * @return the removed items as a stack
     */
    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inv.removeStack(slot, amount);
    }

    /**
     * Removes the stack currently stored at the indicated slot.
     *
     * @param slot
     * @return the stack previously stored at the indicated slot.
     */
    @Override
    public ItemStack removeStack(int slot) {
        return inv.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inv.setStack(slot, stack);
    }

    @Override
    public abstract void markDirty();

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inv.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inv.clear();
    }

    public NbtList writeNbt() {
        var list = new NbtList();
        for(int i = 0; i < size(); i++) {
            list.add(getStack(i).writeNbt(new NbtCompound()));
        }
        return list;
    }

    public void readNbt(NbtList list) {
        if(list.size() != size()) {
            OpenCUModCommon.LOGGER.warn("Wrong tag length, may be an update");
        }
        var count = Math.min(size(), list.size());
        for(int i = 0; i < count; i++) {
            setStack(i, ItemStack.fromNbt(list.getCompound(i)));
        }
    }

}
