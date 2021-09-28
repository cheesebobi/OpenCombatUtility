package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.item.ItemStack;

public class DispenseResult {

    private double energyMultiplier;
    private ItemStack leftover;

    public DispenseResult(double energyMultiplier, ItemStack leftover) {
        this.energyMultiplier = energyMultiplier;
        this.leftover = leftover;
    }

    public ItemStack getLeftover() {
        return leftover;
    }

    public double getEnergyMultiplier() {
        return energyMultiplier;
    }
}
