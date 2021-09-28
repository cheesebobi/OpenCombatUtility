package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.HashSet;

public class DispenseEntry {

    private int tier;
    private Entity entity;
    private double mass;
    private double spreadMultiplier;
    private double energyMultiplier;
    private ItemStack leftover;

    public DispenseEntry(int tier, Entity entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier) {
        this.tier = tier;
        this.entity = entity;
        this.mass = mass;
        this.spreadMultiplier = spreadMultiplier;
        this.energyMultiplier = energyMultiplier;
        this.leftover = leftover;
    }

    public int getTier() {
        return tier;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getMass() {
        return mass;
    }

    public double getSpreadMultiplier() {
        return spreadMultiplier;
    }

    public double getEnergyMultiplier() {
        return energyMultiplier;
    }

    public ItemStack getLeftover() {
        return leftover;
    }
}
