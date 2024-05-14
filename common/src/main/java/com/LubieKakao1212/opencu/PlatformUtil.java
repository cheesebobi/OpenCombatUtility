package com.LubieKakao1212.opencu;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemStack;

public class PlatformUtil {

    @ExpectPlatform
    public static IFramedDevice getDispenser(ItemStack stack) { return null; }

}
