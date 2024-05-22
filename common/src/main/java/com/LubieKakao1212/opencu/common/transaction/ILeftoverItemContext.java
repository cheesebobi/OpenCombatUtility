package com.LubieKakao1212.opencu.common.transaction;

import net.minecraft.item.ItemStack;

public interface ILeftoverItemContext extends AutoCloseable {

    void handleLeftover(ItemStack stack, IContext ctx);

    //Disable exception
    @Override
    void close();
}
