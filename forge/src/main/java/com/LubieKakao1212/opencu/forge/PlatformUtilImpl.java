package com.LubieKakao1212.opencu.forge;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.forge.registry.CUCapabilities;
import net.minecraft.item.ItemStack;

public class PlatformUtilImpl {

    public static IFramedDevice getDispenser(ItemStack stack) {
        return stack.getCapability(CUCapabilities.DISPENSER, null).resolve().orElse(null);
    }

}
