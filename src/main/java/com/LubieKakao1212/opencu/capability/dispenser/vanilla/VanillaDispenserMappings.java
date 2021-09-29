package com.LubieKakao1212.opencu.capability.dispenser.vanilla;

import com.LubieKakao1212.opencu.capability.dispenser.DispenseEntry;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class VanillaDispenserMappings extends DispenserMappings {

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
            return new DispenseEntry(arrow, ItemStack.EMPTY, 1., 1., 1.);
        };
        register(Items.ARROW, arrowMapping);
        register(Items.TIPPED_ARROW, arrowMapping);
        register(Items.SPECTRAL_ARROW, arrowMapping);

        register(Item.getItemFromBlock(Blocks.TNT), (stack, world) -> {
            EntityTNTPrimed tnt = new EntityTNTPrimed(world);
            return new DispenseEntry(tnt, ItemStack.EMPTY, 3., 0.5, 1.);
        });

        register(Items.FIRE_CHARGE, (stack, world) -> {
            EntitySmallFireball fireball = new EntitySmallFireball(world, 0, 0, 0, 0, 0, 0);
            return new DispenseEntry(fireball, ItemStack.EMPTY, 2., 1.5, 1.);
        });

        Mapping potionMapping = (stack, world) -> {
            EntityPotion potion = new EntityPotion(world, 0, 0, 0, stack);
            return new DispenseEntry(potion, ItemStack.EMPTY, 3., 1.5, 1.);
        };

        register(Items.SPLASH_POTION, potionMapping);
        register(Items.LINGERING_POTION, potionMapping);

        register(Items.EXPERIENCE_BOTTLE, (stack, world) -> {
            EntityExpBottle potion = new EntityExpBottle(world);
            return new DispenseEntry(potion, ItemStack.EMPTY, 3., 1.5, 1.);
        });
    }

    @Override
    protected Mapping getDefaultMapping() {
        return super.getDefaultMapping();
    }
}
