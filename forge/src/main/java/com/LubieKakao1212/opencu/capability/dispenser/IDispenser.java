package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import org.joml.Quaterniond;

public interface IDispenser extends INBTSerializable<NbtCompound> {

    /**
     * @param shotItem always singe item
     * @return energy usage multiplier
     */
    DispenseResult shoot(ICapabilityProvider shooter, World level, ItemStack shotItem, BlockPos pos, Quaterniond aim);

    /**
     * @param spread spread value that would be set
     * @return error message or null if successful
     */
    String trySetSpread(double spread);

    /**
     *
     *  @param force force value that would be set
     * @return error message or null if successful
     */
    String trySetForce(double force);


    //Rad angle per tick
    float getAlignmentSpeed();

    boolean hasConfigurableForce();

    boolean hasConfigurableSpread();

    double getMaxSpread();

    double getMinSpread();

    double getMaxForce();

    double getMinForce();
}
