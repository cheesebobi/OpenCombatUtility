package com.lubiekakao1212.opencu.forge;

import com.lubiekakao1212.opencu.common.dispenser.IDispenser;
import com.lubiekakao1212.opencu.forge.registry.CUCapabilities;
import net.minecraft.item.ItemStack;

public class PlatformUtilImpl {

    public static IDispenser getDispenser(ItemStack stack) {
        return stack.getCapability(CUCapabilities.DISPENSER, null).resolve().orElse(null);
    }

}
