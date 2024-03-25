package com.LubieKakao1212.opencu.dependencies.cc.init;

import com.LubieKakao1212.opencu.dependencies.cc.event.CapabilityHandler;
import net.minecraftforge.common.MinecraftForge;

public class CCInit {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
    }

}
