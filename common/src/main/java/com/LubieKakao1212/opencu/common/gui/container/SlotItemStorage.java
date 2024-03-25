package com.lubiekakao1212.opencu.common.gui.container;

import com.lubiekakao1212.opencu.common.storage.IItemStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class SlotItemStorage extends Slot {

    private static final Inventory EMPTY_INV = new SimpleInventory(0);

    protected final IItemStorage inv;
    protected final int index;

    public SlotItemStorage(IItemStorage inventory, int index, int x, int y) {
        super(EMPTY_INV, index, x, y);
        this.inv = inventory;
        this.index = index;
    }

    @Override
    public boolean canInsert(@NotNull ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return inv.isValid(index, stack);
    }

    @Override
    public void setStackNoCallbacks(@NotNull ItemStack stack) {
        inv.set(index, stack);
        this.markDirty();
    }

    @Override
    public void onQuickTransfer(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {

    }

    @Override
    @NotNull
    public ItemStack getStack()
    {
        return inv.getStack(index);
    }

    /*@Override
    public int getMaxItemCount(@NotNull ItemStack stack)
    {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxCount();
        maxAdd.setCount(maxInput);

        ItemStack currentStack = inv.getStack(index);
        ItemStack remainder = inv.insert(index, maxAdd, true);

        int current = currentStack.getCount();
        int added = maxInput - remainder.getCount();
        return current + added;
    }*/

    @Override
    public boolean canTakeItems(PlayerEntity playerIn)
    {
        return !inv.extract(index, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack takeStack(int amount)
    {
        return inv.extract(index, amount, false);
    }

    @Override
    public int getMaxItemCount() {
        return inv.getMaxStackSize(index);
    }
}
