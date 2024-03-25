package com.lubiekakao1212.opencu.common.dispenser;

import net.minecraft.item.ItemStack;

public class DispenseResult {

    public ItemStack leftover;

    public DispenseResult(ItemStack leftover) {
        this.leftover = leftover;
    }

    public ItemStack getLeftover() {
        return leftover;
    }
}
