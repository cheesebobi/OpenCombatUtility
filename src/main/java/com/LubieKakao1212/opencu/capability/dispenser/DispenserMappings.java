package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;

public abstract class DispenserMappings {
    public static final Mapping ITEM_MAPPING = (stack, world) -> {
        EntityItem item = new EntityItem(world);
        item.setItem(stack);
        return new DispenseEntry(item, ItemStack.EMPTY, 1., 1., 1.);
    };

    /**
     * Experimental
     */
    public static final Mapping BLOCK_MAPPING = (stack, world) -> {
        ItemBlock blockItem = (ItemBlock)stack.getItem();
        Block block = blockItem.getBlock();
        EntityFallingBlock sand = new EntityFallingBlock(world, 0, 0, 0, block.getStateFromMeta(stack.getItemDamage()));
        return new DispenseEntry(sand, ItemStack.EMPTY, 1., 0.1, 1);
    };

    //public static final DispenserMappings DISPENSER = new DispenserMappings();

    private final HashMap<Item, Mapping> mappings = new HashMap<>();

    public void register(Item item, Mapping mapping) {
        if(mappings.put(item, mapping) != null) {
            OpenCUMod.logger.warn("Assigned dispenser mapping for same item twice, previous one will be overwritten");
        }
    }

    public DispenseEntry getDispenseResult(ItemStack stack, World world) {
        Mapping mapping = mappings.getOrDefault(stack.getItem(), ITEM_MAPPING);
        return mapping.Get(stack, world);
    }

    public abstract void init();

    protected Mapping getDefaultMapping() {
        return ITEM_MAPPING;
    }

    @FunctionalInterface
    public interface Mapping {
        DispenseEntry Get(ItemStack stack, World world);
    }
}
