package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
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

    public static final DispenseEntry ITEM_ENTRY = new DispenseEntry((stack, world) -> {
        EntityItem item = new EntityItem(world);
        item.setItem(stack);
        return item;
    }, ItemStack.EMPTY, 1., 1., 1.);

    /**
     * Experimental
     */
    public static final DispenseEntry BLOCK_ENTRY = new DispenseEntry((stack, world) -> {
        ItemBlock blockItem = (ItemBlock)stack.getItem();
        Block block = blockItem.getBlock();
        EntityFallingBlock sand = new EntityFallingBlock(world, 0, 0, 0, block.getStateFromMeta(stack.getItemDamage()));
        return sand;
    }, ItemStack.EMPTY, 1., 0.1, 1.);

    public static final PostShootAction DEFAULT_SHOOT_ACTION = (entity, forward, velocity) -> { };

    public static final PostSpawnAction DEFAULT_SPAWN_ACTION = (entity, forward, velocity) -> { };

    private final HashMap<Item, DispenseEntry> mappings = new HashMap<>();

    public void register(Item item, DispenseEntry mapping) {
        if(mappings.put(item, mapping) != null) {
            OpenCUMod.logger.warn("Assigned dispenser mapping for same item twice, previous one will be overwritten");
        }
    }

    public DispenseEntry getDispenseResult(ItemStack stack) {
        DispenseEntry mapping = mappings.getOrDefault(stack.getItem(), getDefaultEntry());
        return mapping;
    }

    public abstract void init();

    protected DispenseEntry getDefaultEntry() {
        return ITEM_ENTRY;
    }

    @FunctionalInterface
    public interface EntityMapping {
        Entity get(ItemStack stack, World world);
    }

    @FunctionalInterface
    public interface PostShootAction {
        void Execute(Entity entity, Vec3 forward, double velocity);
    }

    @FunctionalInterface
    public interface PostSpawnAction {
        void Execute(Entity entity, Vec3 forward, double velocity);
    }

}
