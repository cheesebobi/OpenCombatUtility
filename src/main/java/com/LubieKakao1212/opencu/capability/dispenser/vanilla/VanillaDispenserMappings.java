package com.LubieKakao1212.opencu.capability.dispenser.vanilla;

import com.LubieKakao1212.opencu.capability.dispenser.DispenseEntry;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserMappings;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.projectile.UpdateFireballPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.phys.Vec3;

public class VanillaDispenserMappings extends DispenserMappings {

    public void init() {
        EntityMapping arrowMapping = (stack, level) -> {
            //TODO find a way to stop using bob
            LivingEntity bob = new Pig(EntityType.PIG, level);
            AbstractArrow arrow = ((ArrowItem)stack.getItem()).createArrow(level, stack, bob);
            arrow.setOwner(null);
            arrow.pickup = Arrow.Pickup.ALLOWED;
            return arrow;
        };
        register(Items.ARROW, new DispenseEntry((stack, level) -> {
            Entity result = arrowMapping.get(stack, level);
            Arrow arrow = (Arrow)result;

            //Is this necessary?
            //Prevents normal arrows from spawning particles after restart
            CompoundTag tag = new CompoundTag();
            arrow.save(tag);
            tag.putInt("Color", -1);
            arrow.load(tag);
            return arrow;
        }, ItemStack.EMPTY, 1., 1., 1.));
        register(Items.TIPPED_ARROW, new DispenseEntry(arrowMapping, ItemStack.EMPTY, 1.,1., 1.));
        register(Items.SPECTRAL_ARROW, new DispenseEntry(arrowMapping, ItemStack.EMPTY, 1., 1.,1.));

        register(Items.TNT, new DispenseEntry((stack, level) -> {
            PrimedTnt tnt = new PrimedTnt(EntityType.TNT, level);
            return tnt;
        }, ItemStack.EMPTY, 3., 0.5, 1.));

        register(Items.FIRE_CHARGE, new DispenseEntry((stack, level) -> {
                SmallFireball fireball = new SmallFireball(level, 0, 0, 0, 1, 0, 0);
                fireball.setOwner(null);
                fireball.xPower = 0;
                fireball.yPower = 0;
                fireball.zPower = 0;
                return fireball;
            },
            ItemStack.EMPTY, 3., 2, 1., (entity, forward, velocity) -> {
            SmallFireball fireball = (SmallFireball) entity;
                velocity = Math.min(velocity, 0.1d);
                fireball.setDeltaMovement(Vec3.ZERO);
                fireball.xPower = forward.x * velocity * 0.1D;
                fireball.yPower = forward.y * velocity * 0.1D;
                fireball.zPower = forward.z * velocity * 0.1D;
            },
            (entity, forward, velocity) -> {
                SmallFireball fireball = (SmallFireball) entity;
                NetworkHandler.enqueueEntityUpdate(new UpdateFireballPacket(fireball.getId(), fireball.xPower, fireball.yPower, fireball.zPower), fireball, 1);
            }));

        DispenseEntry potionMapping = new DispenseEntry((stack, level) -> {
            ThrownPotion potion = new ThrownPotion(EntityType.POTION, level);
            potion.setItem(stack);
            return potion;
        }, ItemStack.EMPTY, 3., 1.5, 1.);

        register(Items.SPLASH_POTION, potionMapping);
        register(Items.LINGERING_POTION, potionMapping);

        register(Items.EXPERIENCE_BOTTLE, new DispenseEntry((stack, level) -> {
            ThrownExperienceBottle bottle = new ThrownExperienceBottle(EntityType.EXPERIENCE_BOTTLE, level);
            return bottle;
        }, ItemStack.EMPTY, 3., 1.5, 1.));

        register(Items.FIREWORK_ROCKET, new DispenseEntry((stack, level) -> {
            FireworkRocketEntity firework = new FireworkRocketEntity(level, 0, 0, 0, stack);
            return firework;
        },ItemStack.EMPTY, 50., 1., 1.));
    }

    @Override
    protected DispenseEntry getDefaultEntry() {
        return super.getDefaultEntry();
    }
}
