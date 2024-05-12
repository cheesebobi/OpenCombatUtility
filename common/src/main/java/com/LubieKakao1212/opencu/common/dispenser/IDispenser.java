package com.LubieKakao1212.opencu.common.dispenser;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.lubiekakao1212.qulib.math.Aim;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Quaterniond;

public interface IDispenser {

    /**
     * @param shotItem always singe item
     * @return energy usage multiplier
     */
    DispenseResult shoot(BlockEntityModularFrame shooter, World level, ItemStack shotItem, BlockPos pos, Aim aim);

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

    NbtCompound serialize();

    void deserialize(NbtCompound nbt);
}
