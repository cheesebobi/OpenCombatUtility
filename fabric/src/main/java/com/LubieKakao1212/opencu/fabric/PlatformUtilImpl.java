package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.dispenser.IDispenser;
import com.LubieKakao1212.opencu.fabric.apilookup.APILookupIDispenser;
import net.minecraft.item.ItemStack;

public class PlatformUtilImpl {
    public static IDispenser getDispenser(ItemStack stack) {
        return APILookupIDispenser.DISPENSER.find(stack, null);
    }
}
