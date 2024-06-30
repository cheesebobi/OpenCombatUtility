package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.fabric.apilookup.APILookupIFramedDevice;
import net.minecraft.item.ItemStack;

public class PlatformUtilImpl {
    public static IFramedDevice getDispenser(ItemStack stack) {
        return APILookupIFramedDevice.DISPENSER.find(stack, null);
    }
}
