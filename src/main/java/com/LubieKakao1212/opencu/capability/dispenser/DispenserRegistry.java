package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;

public class DispenserRegistry {
    public static final Mapping DEFAULT_MAPPING = (stack, world) -> {
        EntityItem item = new EntityItem(world);
        item.setItem(stack);
        return new DispenseEntry(0, item, ItemStack.EMPTY, 1., 1., 1.);
    };

    public static final DispenserRegistry DISPENSER = new DispenserRegistry();

    private final HashMap<Item, Mapping> mappings = new HashMap<>();

    public void register(Item item, Mapping mapping) {
        if(mappings.put(item, mapping) != null) {
            OpenCUMod.logger.warn("Assigned dispenser mapping for same item twice, previous one will be overwritten");
        }
    }

    public DispenseEntry getDispenseResult(ItemStack stack, World world) {
        Mapping mapping = mappings.getOrDefault(stack.getItem(), DEFAULT_MAPPING);
        return mapping.Get(stack, world);
    }

    public void init() {
        Mapping arrowMapping = (stack, world) -> {
            EntityLivingBase bob = new EntityPig(world);
            EntityArrow arrow = ((ItemArrow)stack.getItem()).createArrow(world, stack, bob);
            arrow.shootingEntity = null;
            arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;

            //Is this necessary?
            //Prevents normal arrows from
            if(arrow instanceof EntityTippedArrow) {
                EntityTippedArrow tipped = ((EntityTippedArrow) arrow);
                NBTTagCompound tag = new NBTTagCompound();
                tipped.writeEntityToNBT(tag);
                tag.setInteger("Color", -1);
                tipped.readEntityFromNBT(tag);
            }
            return new DispenseEntry(0, arrow, ItemStack.EMPTY, 1., 1., 1.);
        };
        register(Items.ARROW, arrowMapping);
        register(Items.TIPPED_ARROW, arrowMapping);
        register(Items.SPECTRAL_ARROW, arrowMapping);
    }

    @FunctionalInterface
    public interface Mapping {
        DispenseEntry Get(ItemStack stack, World world);
    }
}
