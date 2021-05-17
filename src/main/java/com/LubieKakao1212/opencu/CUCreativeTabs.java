package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.block.CUBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CUCreativeTabs {

    public static CreativeTabs tabCUMain = new CreativeTabs("ocu") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(CUBlocks.repulsor);
        }
    };

}
