package com.LubieKakao1212.opencu.common.device.vanilla;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.device.ShotEntry;
import com.LubieKakao1212.opencu.common.device.ShotMappings;
import com.LubieKakao1212.opencu.common.network.packet.projectile.PacketClientUpdateFireball;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import java.util.Optional;

public class VanillaDispenserMappings extends ShotMappings {

    private static final EntityMapping LOCKED = (stack, level, state) -> {
        var itemEntity = new ItemEntity(level, 0, 0, 0, stack, 0,0,0);
        itemEntity.setCustomName(Text.of("Ah ah ah, you didn't say the magic word"));
        itemEntity.setCustomNameVisible(true);
        itemEntity.setPickupDelay(20);
        return itemEntity;
    };

    public void init() {
        //region Mappings
        EntityMapping arrowMapping = (stack, level, state) -> {
            //TODO find a way to stop using bob
            LivingEntity bob = new PigEntity(EntityType.PIG, level);
            bob.setCustomName(Text.of("Bob"));
            PersistentProjectileEntity arrow = ((ArrowItem)stack.getItem()).createArrow(level, stack, bob);

            bob.remove(Entity.RemovalReason.DISCARDED);

            arrow.setDamage(arrow.getDamage() * state.getPower());
            arrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
            return arrow;
        };
        register(Items.ARROW, new ShotEntry((stack, level, state) -> {
            Entity result = arrowMapping.get(stack, level, state);
            ArrowEntity arrow = (ArrowEntity)result;

            //Is this necessary?
            //Prevents normal arrows from spawning particles after restart
            NbtCompound tag = new NbtCompound();
            arrow.saveNbt(tag);
            tag.putInt("Color", -1);
            arrow.readNbt(tag);
            return arrow;
        }, ItemStack.EMPTY, 1., 1., 1.));
        register(Items.TIPPED_ARROW, new ShotEntry(arrowMapping, ItemStack.EMPTY, 1.,1., 1.));
        //register(Items.SPECTRAL_ARROW, new DispenseEntry(arrowMapping, ItemStack.EMPTY, 1., 1.,1.));

        register(Items.TNT, new ShotEntry((stack, level, state) -> {
            TntEntity tnt = new TntEntity(EntityType.TNT, level);
            return tnt;
        }, ItemStack.EMPTY, 3., 0.5, 1.));

        register(Items.FIRE_CHARGE, new ShotEntry(LOCKED, ItemStack.EMPTY, 100., 1., 1.));
        /*register(Items.FIRE_CHARGE, new ShotEntry((stack, level) -> {
                SmallFireballEntity fireball = new SmallFireballEntity(level, 0, 0, 0, 1, 0, 0);
                fireball.setOwner(null);
                fireball.powerX = 0;
                fireball.powerY = 0;
                fireball.powerZ = 0;
                return fireball;
            },
            ItemStack.EMPTY, 3., 2, 1., (entity, forward, velocity) -> {
            SmallFireballEntity fireball = (SmallFireballEntity) entity;
                velocity = Math.min(velocity, 0.1d);
                fireball.setVelocity(Vec3d.ZERO);
                fireball.powerX = forward.x * velocity * 0.1D;
                fireball.powerY = forward.y * velocity * 0.1D;
                fireball.powerZ = forward.z * velocity * 0.1D;
            },
            (entity, forward, velocity) -> {
                SmallFireballEntity fireball = (SmallFireballEntity) entity;
                NetworkUtil.enqueueEntityPacket(new PacketClientUpdateFireball(fireball.getId(), (float)fireball.powerX, (float)fireball.powerY, (float)fireball.powerZ), fireball, 1);
            }));*/

        ShotEntry potionMapping = new ShotEntry((stack, level, state) -> {
            PotionEntity potion = new PotionEntity(EntityType.POTION, level);
            potion.setItem(stack);
            return potion;
        }, ItemStack.EMPTY, 3., 1.5, 1.);

        register(Items.SPLASH_POTION, potionMapping);
        register(Items.LINGERING_POTION, potionMapping);

        register(Items.EXPERIENCE_BOTTLE, new ShotEntry((stack, level, state) -> {
            ExperienceBottleEntity bottle = new ExperienceBottleEntity(EntityType.EXPERIENCE_BOTTLE, level);
            return bottle;
        }, ItemStack.EMPTY, 3., 1.5, 1.));

        register(Items.FIREWORK_ROCKET, new ShotEntry((stack, level, state) -> {
            //FireworkRocketEntity firework = new FireworkRocketEntity(level, 0, 0, 0, stack);
            FireworkRocketEntity firework = new FireworkRocketEntity(level, stack, 0, 0, 0, true);
            return firework;
        },ItemStack.EMPTY, 2.5, 2.5, 1.));
        //endregion

        //region Redirections
        register((stack) -> {
            Item i = stack.getItem();
            if(i != Items.ARROW && i instanceof ArrowItem) {
                return Optional.of(Items.TIPPED_ARROW);
            }else {
                return Optional.empty();
            }
        });
        //endregion
    }

    @Override
    protected ShotEntry getDefaultEntry() {
        return super.getDefaultEntry();
    }
}
