package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DispenseEntry {

    //private Entity entity;
    private DispenserMappings.EntityMapping entity;
    private double mass;
    private double spreadMultiplier;
    private double energyMultiplier;
    private ItemStack leftover;
    private DispenserMappings.PostShootAction postShootAction;
    private DispenserMappings.PostSpawnAction postSpawnAction;

    public DispenseEntry(DispenserMappings.EntityMapping entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier, DispenserMappings.PostShootAction postShootAction, DispenserMappings.PostSpawnAction postSpawnAction) {
        this.entity = entity;
        this.mass = mass;
        this.spreadMultiplier = spreadMultiplier;
        this.energyMultiplier = energyMultiplier;
        this.leftover = leftover;
        this.postShootAction = postShootAction;
        this.postSpawnAction = postSpawnAction;
    }

    public DispenseEntry(DispenserMappings.EntityMapping entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier, DispenserMappings.PostShootAction postShootAction) {
        this(entity, leftover, mass, spreadMultiplier, energyMultiplier, postShootAction, DispenserMappings.DEFAULT_SPAWN_ACTION);
    }

    public DispenseEntry(DispenserMappings.EntityMapping entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier) {
        this(entity, leftover, mass, spreadMultiplier, energyMultiplier, DispenserMappings.DEFAULT_SHOOT_ACTION);
    }

    public Entity getEntity(ItemStack stack, World world) {
        return entity.get(stack, world);
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

    public DispenserMappings.PostShootAction getPostShootAction() {
        return postShootAction;
    }

    public DispenserMappings.PostSpawnAction getPostSpawnAction() {
        return postSpawnAction;
    }
}
