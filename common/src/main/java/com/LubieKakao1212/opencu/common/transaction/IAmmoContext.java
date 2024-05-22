package com.LubieKakao1212.opencu.common.transaction;

import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;

public interface IAmmoContext {

    ItemStack useAmmoFirst(IContext ctx);

    ItemStack useAmmoRandom(Random random, IContext ctx);

    /**
     * Returns a list of available ammo, all stacks are non-empty
     * Does not take, {@link IContext} since it does not modify the state
     * @return
     */
    List<ItemStack> availableAmmo();

}
