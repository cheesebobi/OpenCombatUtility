package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.init.CUBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class OCUCreativeTabs {

    public static CreativeModeTab tabCUMain = new CreativeModeTab("ocu") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack((ItemLike) CUBlocks.REPULSOR);
        }
    };

}
