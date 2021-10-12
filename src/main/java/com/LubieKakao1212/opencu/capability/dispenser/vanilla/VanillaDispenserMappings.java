package com.LubieKakao1212.opencu.capability.dispenser.vanilla;

import com.LubieKakao1212.opencu.capability.dispenser.DispenseEntry;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.projectile.UpdateFireballPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

public class VanillaDispenserMappings extends DispenserMappings {

    public void init() {
        EntityMapping arrowMapping = (stack, world) -> {
            EntityLivingBase bob = new EntityPig(world);
            EntityArrow arrow = ((ItemArrow)stack.getItem()).createArrow(world, stack, bob);
            arrow.shootingEntity = null;
            arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;

            return arrow;
        };
        register(Items.ARROW, new DispenseEntry((stack, world) -> {
            Entity result = arrowMapping.get(stack, world);
            EntityTippedArrow arrow = (EntityTippedArrow)result;

            //Is this necessary?
            //Prevents normal arrows from spawning particles after restart
            NBTTagCompound tag = new NBTTagCompound();
            arrow.writeEntityToNBT(tag);
            tag.setInteger("Color", -1);
            arrow.readEntityFromNBT(tag);
            return arrow;
        }, ItemStack.EMPTY, 1., 1., 1.));
        register(Items.TIPPED_ARROW, new DispenseEntry(arrowMapping, ItemStack.EMPTY, 1.,1., 1.));
        register(Items.SPECTRAL_ARROW, new DispenseEntry(arrowMapping, ItemStack.EMPTY, 1., 1.,1.));

        register(Item.getItemFromBlock(Blocks.TNT), new DispenseEntry((stack, world) -> {
            EntityTNTPrimed tnt = new EntityTNTPrimed(world);
            return tnt;
        }, ItemStack.EMPTY, 3., 0.5, 1.));

        register(Items.FIRE_CHARGE, new DispenseEntry((stack, world) -> {
                EntitySmallFireball fireball = new EntitySmallFireball(world, 0, 0, 0, 1, 0, 0);
                fireball.shootingEntity = null;
                fireball.accelerationX = 0;
                fireball.accelerationY = 0;
                fireball.accelerationZ = 0;
                return fireball;
            },
            ItemStack.EMPTY, 3., 2, 1., (entity, forward, velocity) -> {
                EntitySmallFireball fireball = (EntitySmallFireball) entity;
                velocity = Math.min(velocity, 0.1d);
                fireball.motionX = 0;
                fireball.motionY = 0;
                fireball.motionZ = 0;
                fireball.accelerationX = forward.x * velocity * 0.1D;
                fireball.accelerationY = forward.y * velocity * 0.1D;
                fireball.accelerationZ = forward.z * velocity * 0.1D;
            },
            (entity, forward, velocity) -> {
                EntitySmallFireball fireball = (EntitySmallFireball) entity;
                NetworkHandler.enqueueEntityUpdate(new UpdateFireballPacket(fireball.getEntityId(), fireball.accelerationX, fireball.accelerationY, fireball.accelerationZ), fireball, 1);
            }));

        DispenseEntry potionMapping = new DispenseEntry((stack, world) -> {
            EntityPotion potion = new EntityPotion(world, 0, 0, 0, stack);
            return potion;
        }, ItemStack.EMPTY, 3., 1.5, 1.);

        register(Items.SPLASH_POTION, potionMapping);
        register(Items.LINGERING_POTION, potionMapping);

        register(Items.EXPERIENCE_BOTTLE, new DispenseEntry((stack, world) -> {
            EntityExpBottle bottle = new EntityExpBottle(world);
            return bottle;
        }, ItemStack.EMPTY, 3., 1.5, 1.));

        register(Items.FIREWORKS, new DispenseEntry((stack, world) -> {
            EntityFireworkRocket firework = new EntityFireworkRocket(world, 0, 0, 0, stack);
            return firework;
        },ItemStack.EMPTY, 50., 1., 1.));
    }

    @Override
    protected DispenseEntry getDefaultEntry() {
        return super.getDefaultEntry();
    }
}
