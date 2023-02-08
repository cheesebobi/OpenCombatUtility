package com.LubieKakao1212.opencu.capability.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import com.LubieKakao1212.qulib.libs.joml.Quaterniond;

public interface IDispenser extends INBTSerializable<CompoundTag> {

    /**
     * @param shotItem always singe item
     * @return energy usage multiplier
     */
    DispenseResult shoot(ICapabilityProvider shooter, Level level, ItemStack shotItem, BlockPos pos, Quaterniond aim);

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
