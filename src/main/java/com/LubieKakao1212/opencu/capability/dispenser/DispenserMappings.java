package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
import glm.quat.Quat;
import glm.vec._3.Vec3;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashMap;

public abstract class DispenserMappings {
    public static final EntityMapping ITEM_MAPPING = (stack, world) -> {
        EntityItem item = new EntityItem(world);
        item.setItem(stack);
        return new DispenseEntry(item, ItemStack.EMPTY, 1., 1., 1.);
    };

    /**
     * Experimental
     */
    public static final EntityMapping BLOCK_MAPPING = (stack, world) -> {
        ItemBlock blockItem = (ItemBlock)stack.getItem();
        Block block = blockItem.getBlock();
        EntityFallingBlock sand = new EntityFallingBlock(world, 0, 0, 0, block.getStateFromMeta(stack.getItemDamage()));
        return new DispenseEntry(sand, ItemStack.EMPTY, 1., 0.1, 1);
    };

    public static final PostShootAction DEFAULT_SHOOT_ACTION = (forward, velocity) -> { };

    public static final PostSpawnAction DEFAULT_SPAWN_ACTION = (forward, velocity) -> { };

    private final HashMap<Item, EntityMapping> mappings = new HashMap<>();

    public void register(Item item, EntityMapping mapping) {
        if(mappings.put(item, mapping) != null) {
            OpenCUMod.logger.warn("Assigned dispenser mapping for same item twice, previous one will be overwritten");
        }
    }

    public DispenseEntry getDispenseResult(ItemStack stack, World world) {
        EntityMapping mapping = mappings.getOrDefault(stack.getItem(), ITEM_MAPPING);
        return mapping.Get(stack, world);
    }

    public abstract void init();

    protected EntityMapping getDefaultMapping() {
        return ITEM_MAPPING;
    }

    @FunctionalInterface
    public interface EntityMapping {
        DispenseEntry Get(ItemStack stack, World world);
    }

    @FunctionalInterface
    public interface PostShootAction {
        void Execute(Vec3 forward, double velocity);
    }

    @FunctionalInterface
    public interface PostSpawnAction {
        void Execute(Vec3 forward, double velocity);
    }

}
