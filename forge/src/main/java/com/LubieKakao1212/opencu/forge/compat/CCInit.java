package com.LubieKakao1212.opencu.forge.compat;

import com.LubieKakao1212.opencu.forge.capability.event.CapabilityHandler;
import net.minecraftforge.common.MinecraftForge;

public class CCInit {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
    }

}
