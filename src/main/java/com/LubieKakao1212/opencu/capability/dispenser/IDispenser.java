package com.LubieKakao1212.opencu.capability.dispenser;

import glm.quat.Quat;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Connector;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDispenser {

    /**
     *
     * @param context
     * @param world
     * @param shotItem always singe item
     * @param pos
     * @param pitch
     * @param yaw
     * @return energy usage multiplier
     */
    DispenseResult Shoot(Connector connector, World world, ItemStack shotItem, BlockPos pos, Quat aim);

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


    float getBaseEnergyRequired();

    //Math angle per tick
    float getAlignmentSpeed();

}
