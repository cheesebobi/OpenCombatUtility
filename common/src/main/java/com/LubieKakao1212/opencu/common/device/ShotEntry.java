package com.LubieKakao1212.opencu.common.device;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShotEntry {

    //private Entity entity;
    private final ShotMappings.EntityMapping entity;
    private final double mass;
    private final double spreadMultiplier;
    private final double energyMultiplier;
    private final ItemStack leftover;
    private final ShotMappings.PostShootAction postShootAction;
    private final ShotMappings.PostSpawnAction postSpawnAction;

    public ShotEntry(ShotMappings.EntityMapping entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier, ShotMappings.PostShootAction postShootAction, ShotMappings.PostSpawnAction postSpawnAction) {
        this.entity = entity;
        this.mass = mass;
        this.spreadMultiplier = spreadMultiplier;
        this.energyMultiplier = energyMultiplier;
        this.leftover = leftover;
        this.postShootAction = postShootAction;
        this.postSpawnAction = postSpawnAction;
    }

    public ShotEntry(ShotMappings.EntityMapping entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier, ShotMappings.PostShootAction postShootAction) {
        this(entity, leftover, mass, spreadMultiplier, energyMultiplier, postShootAction, ShotMappings.DEFAULT_SPAWN_ACTION);
    }

    public ShotEntry(ShotMappings.EntityMapping entity, ItemStack leftover, double mass, double spreadMultiplier, double energyMultiplier) {
        this(entity, leftover, mass, spreadMultiplier, energyMultiplier, ShotMappings.DEFAULT_SHOOT_ACTION);
    }

    public Entity getEntity(ItemStack stack, World level) {
        return entity.get(stack, level);
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

    public ShotMappings.PostShootAction getPostShootAction() {
        return postShootAction;
    }

    public ShotMappings.PostSpawnAction getPostSpawnAction() {
        return postSpawnAction;
    }
}
